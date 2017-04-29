package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.scheme.*;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.persistence.services.SessionsSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Lazy
@Component
public class SessionsSvcImpl implements SessionsSvc {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RevisionManager revisionManager;

	@Autowired
	private QuerySvc querySvc;

	@Autowired
	private DroneDbCrudSvc droneDbCrudSvc;

	@Override
	@Transactional
	public void publish() {
		// In case we've modified Revision
		handlePublish(revisionManager.getNextRevision());
		revisionManager.advance();
	}

	private void handlePublish(int nextRevision) {
		// Handle Mission Item
		handlePublishForType(Point.class, nextRevision);
		handlePublishForType(Waypoint.class, nextRevision);
		handlePublishForType(Mission.class, nextRevision);

		System.out.println("Publishing!!!!");
	}

	private <T extends BaseObject> void handlePublishForType(Class<T> clz, int nextRevision) {
		List<T> objs = (List<T>) querySvc.runNativeQuery(buildGetPrivateQuery(clz), clz);
		for (T item : objs) {
			T tip = getPublishedByPrivateObject(clz, item);
			System.out.println("Tip object " + tip);
			if (tip != null)
				movePrivateToPublic(item, tip, clz, nextRevision);
			else {
				movePrivateToPublic(item, clz, nextRevision);
			}
		}
	}

	private <T extends BaseObject> T getPublishedByPrivateObject(Class<T> clz, T item) {
		KeyId key = item.getKeyId().copy();
		key.setPrivatelyModified(false);
		System.err.println("Search for publish object of key " + key);
		return entityManager.find(clz, key);
	}

	private String buildGetPrivateQuery(Class<? extends BaseObject> objClass) {
		System.out.println(objClass + " " + objClass.getSimpleName());
		return String.format("SELECT * FROM %s WHERE privatelyModified = true", objClass.getSimpleName().toLowerCase());
	}

	private <T extends BaseObject> void movePrivateToPublic(BaseObject privateItem, BaseObject publicItem, Class<T> clz, int nextRevision) {
		System.out.println("Moving private to existing published object");
		BaseObject publicItemDup = publicItem.copy();

		// Rewrite the last tip as old version
		publicItemDup.setDeleted(privateItem.isDeleted());
		publicItemDup.setCreationDate(publicItem.getCreationDate());
		publicItemDup.setFromRevision(publicItem.getFromRevision());
		publicItemDup.getKeyId().setToRevision(nextRevision);
		publicItemDup.getKeyId().setPrivatelyModified(false);
		entityManager.persist(publicItemDup);

		// Updating the tip to be align to the private
		if (privateItem.isDeleted()) {
			System.out.println("Object for deletion " + publicItemDup);
			entityManager.remove(publicItem);
		}
		else {
			publicItem.set(privateItem);
			publicItem.setFromRevision(nextRevision);
			System.out.println("Old " + publicItemDup);
			System.out.println("New " + publicItem);
			entityManager.flush();
		}

		// Clean the private
		entityManager.remove(privateItem);
	}

	private <T extends BaseObject> void movePrivateToPublic(BaseObject privateItem, Class<T> clz, int nextRevision) {
		System.out.println("Moving private to NON existing published object");
		BaseObject privateItemDup = privateItem.copy();

		// Building a new tip
		privateItemDup.setFromRevision(nextRevision);
		privateItemDup.getKeyId().setPrivatelyModified(false);
		privateItemDup.getKeyId().setToRevision(Constants.TIP_REVISION);
		entityManager.persist(privateItemDup);

		// Clean private
		entityManager.remove(privateItem);

		System.out.println("Object writting to public " + privateItemDup);
	}

	@Override
	@Transactional
	public void discard() {
		// Handle Mission Item
		handleDiscardForType(Point.class);
		handleDiscardForType(Waypoint.class);
		handleDiscardForType(Mission.class);

		System.out.println("Discarding!!!!");
	}

	public <T extends BaseObject> void handleDiscardForType(Class<T> clz) {
		List<T> objs = (List<T>) querySvc.runNativeQuery(buildGetPrivateQuery(clz), clz);
		for (T item : objs) {
			entityManager.remove(item);
		}
	}
}
