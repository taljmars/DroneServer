package com.dronedb.services.internal;

import javax.persistence.EntityManager;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dronedb.scheme.BaseObject;
import com.dronedb.services.DroneDbCrudSvc;

@Component
public class DroneDbCrudSvcImpl implements DroneDbCrudSvc {
	
	@Autowired
	private EntityManager entityManager;
	
	public String CheckConnection() {
		return "Inside Implementation";
	}
	
	public DroneDbCrudSvcImpl() {
	}
	
	public <T extends BaseObject> T create(final Class<T> clz) {
		try {
			return clz.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T extends BaseObject> void update(T object) {
		entityManager.getTransaction().begin();
		BaseObject mergedObject = object;
		if (object.getObjId() != null) {
			mergedObject = entityManager.find(object.getClass() ,object.getObjId());
			mergedObject.set(object);
		}
		entityManager.persist(mergedObject);
		entityManager.getTransaction().commit();
	}
	
	public <T extends BaseObject> void delete(T object) {
		entityManager.getTransaction().begin();
		T mergedObject = entityManager.merge(object);
		entityManager.remove(mergedObject);
		entityManager.getTransaction().commit();
	}
	
	public <T extends BaseObject> T read(final Integer uid) {
		Assert.fail("Not implemeted yet");
		return null;
	}
	
	public <T extends BaseObject> T readByClass(final Integer objId, final Class<T> clz) {
		return entityManager.find(clz ,objId);
	}
}
