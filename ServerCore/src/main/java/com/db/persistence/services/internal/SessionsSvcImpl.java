package com.db.persistence.services.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.scheme.*;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.services.QuerySvc;
import com.db.persistence.services.SessionsSvc;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Modifier;
import java.util.List;

@Lazy
@Component
public class SessionsSvcImpl implements SessionsSvc {

	final static Logger logger = Logger.getLogger(SessionsSvcImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RevisionManager revisionManager;

	@Autowired
	private QuerySvc querySvc;

	@Autowired
	private ObjectCrudSvc objectCrudSvc;

	@Override
	@Transactional
	public void publish() {
		// In case we've modified Revision
		logger.debug("PUBLISH START !!!");
		logger.debug("Update revision value in revision manager");
		handlePublish(revisionManager.getNextRevision());
		revisionManager.advance();
		logger.debug("PUBLISH END !!!");
	}

	private void handlePublish(int nextRevision) {
		// Handle Mission Item
		Metamodel mm = entityManager.getMetamodel();
		for (final ManagedType<?> managedType : mm.getManagedTypes()) {
			Class clz = managedType.getJavaType();
			logger.error("Found class '" + clz.getCanonicalName() + "'");
			if (Modifier.isAbstract(clz.getModifiers()))
				continue;

			if (!BaseObject.class.isAssignableFrom(clz) || clz == BaseObject.class)
				continue;

			if (!clz.isAnnotationPresent(Sessionable.class))
				continue;

			logger.error("going to handle " + clz);
			handlePublishForType(clz, nextRevision);
		}
	}

	private <T extends BaseObject> void handlePublishForType(Class<T> clz, int nextRevision) {
		logger.debug("Handle publish for object of type '" + clz .getCanonicalName()+ "'");
		List<T> objs = (List<T>) querySvc.runNativeQueryWithClass(buildGetPrivateQuery(clz), clz);
		for (T item : objs) {
			T tip = getPublishedByPrivateObject(clz, item);

			if (tip != null) {
				logger.debug("Tip object " + tip);
				movePrivateToPublic(item, tip, clz, nextRevision);
			}
			else {
				logger.debug("Tip object not found, object is written to the public DB for the first time");
				movePrivateToPublic(item, clz, nextRevision);
			}
		}
	}

	private <T extends BaseObject> T getPublishedByPrivateObject(Class<T> clz, T item) {
		logger.debug("Search for published object of " + item.getKeyId());
		KeyId key = item.getKeyId().copy();
		key.setPrivatelyModified(false);
		logger.debug("Search for publish object of key " + key);
		return entityManager.find(clz, key);
	}

	private String buildGetPrivateQuery(Class<? extends BaseObject> objClass) {
		logger.debug("Build query to get object of " + objClass.getSimpleName());
		return String.format("SELECT * FROM %s WHERE privatelyModified = true", objClass.getSimpleName().toLowerCase());
	}

	private <T extends BaseObject> void movePrivateToPublic(T privateItem, BaseObject publicItem, Class<T> clz, int nextRevision) {
		logger.debug("Moving private to existing published object");
		T publicItemDup = (T) publicItem.copy();

		// Rewrite the last tip as old version
		publicItemDup.setDeleted(privateItem.isDeleted());
		publicItemDup.setCreationDate(publicItem.getCreationDate());
		publicItemDup.setFromRevision(publicItem.getFromRevision());
		publicItemDup.getKeyId().setToRevision(nextRevision);
		publicItemDup.getKeyId().setPrivatelyModified(false);
		entityManager.persist(publicItemDup);

		// Updating the tip to be align to the private
		if (privateItem.isDeleted()) {

			MarkObjectDeref(privateItem, nextRevision);

			logger.debug("Object for deletion " + publicItemDup);
			entityManager.remove(publicItem);
		}
		else {
			publicItem.set(privateItem);
			publicItem.setFromRevision(nextRevision);
			logger.debug("Old " + publicItemDup);
			logger.debug("New " + publicItem);
			entityManager.flush();
		}

		// Clean the private
		entityManager.remove(privateItem);
		logger.debug("Done");
	}

	private void MarkObjectDeref(BaseObject item, int nextRevision) {
		try {
			ObjectDeref objectDeref = objectCrudSvc.readByClass(item.getKeyId().getObjId(), ObjectDeref.class);
			if (!item.isDeleted() && !objectDeref.getKeyId().getPrivatelyModified())
				return;

			ObjectDeref objectDerefDup = objectDeref.copy();
			objectDerefDup.getKeyId().setPrivatelyModified(false);
			if (item.isDeleted())
				objectDerefDup.getKeyId().setToRevision(nextRevision);
			objectDerefDup.setDeleted(item.isDeleted());
//			logger.debug("TALMA DBG " + objectDerefDup);
			entityManager.persist(objectDerefDup);
			entityManager.remove(objectDeref);
//			objectDeref = objectCrudSvc.readByClass(objectDeref.getKeyId().getObjId(), ObjectDeref.class);
//			logger.debug("TALMA DBG " + objectDeref);
		}
		catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
	}

	private <T extends BaseObject> void movePrivateToPublic(T privateItem, Class<T> clz, int nextRevision) {
		logger.debug("Moving private to NON existing published object");
		T privateItemDup = (T) privateItem.copy();

		MarkObjectDeref(privateItem, nextRevision);

		// Building a new tip
		privateItemDup.setFromRevision(nextRevision);
		privateItemDup.getKeyId().setPrivatelyModified(false);
		privateItemDup.getKeyId().setToRevision(Constants.TIP_REVISION);
		entityManager.persist(privateItemDup);

		// Clean private
//		logger.debug("Removing " + privateItem);
		entityManager.remove(privateItem);

		logger.debug("Object writing to public " + privateItemDup);
//		try {
//			BaseObject b = objectCrudSvc.read(privateItemDup.getKeyId().getObjId());
//			logger.debug("Talma writing to public " + b);
//		} catch (ObjectNotFoundException e) {
//			e.printStackTrace();
//		}
		logger.debug("Done");
	}

	@Override
	@Transactional
	public void discard() {
		logger.debug("DISCARD START !!!");
		Metamodel mm = entityManager.getMetamodel();
		for (final ManagedType<?> managedType : mm.getManagedTypes()) {
			Class clz = managedType.getJavaType();
			logger.error("Found class " + clz);

			if (!clz.isAnnotationPresent(Sessionable.class))
				continue;

			logger.error("going to handle " + clz);
			handleDiscardForType(clz);
		}

		logger.debug("DISCARD END !!!");
	}

	public <T extends BaseObject> void handleDiscardForType(Class<T> clz) {
			List<T> objs = (List<T>) querySvc.runNativeQueryWithClass(buildGetPrivateQuery(clz), clz);
			for (T item : objs) {
				try {
					ObjectDeref objectDeref = objectCrudSvc.readByClass(item.getKeyId().getObjId(), ObjectDeref.class);
					if (objectDeref.getKeyId().getPrivatelyModified())
						entityManager.remove(objectDeref);
					entityManager.remove(item);
				}
				catch (ObjectNotFoundException e) {
					e.printStackTrace();
					logger.debug("Failed to clear Object Deref for " + objectCrudSvc);
				}

			}
	}
}
