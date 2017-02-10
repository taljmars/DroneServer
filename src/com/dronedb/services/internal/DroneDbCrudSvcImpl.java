package com.dronedb.services.internal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.stereotype.Component;

import com.dronedb.scheme.BaseObject;
import com.dronedb.services.DroneDbCrudSvc;

@Component
public class DroneDbCrudSvcImpl implements DroneDbCrudSvc {
	
	EntityManager entitymanager;
	EntityManagerFactory emfactory;
	
	public String CheckConnection() {
		return "Inside Implementation";
	}
	
	public DroneDbCrudSvcImpl() {
		emfactory = Persistence.createEntityManagerFactory("DroneDB");
		entitymanager = emfactory.createEntityManager( );
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
		entitymanager.getTransaction().begin();
		BaseObject mergedObject = entitymanager.find(object.getClass() ,object.getObjId());
		mergedObject.set(object);
		entitymanager.persist(mergedObject);
		entitymanager.getTransaction().commit();
	}
	
	public <T extends BaseObject> void delete(T object) {
		entitymanager.getTransaction().begin();
		T mergedObject = entitymanager.merge(object);
		entitymanager.remove(mergedObject);
		entitymanager.getTransaction().commit();
	}
	
	public <T extends BaseObject> T read(final Integer uid) {
		Assert.fail("Not implemeted yet");
		return null;
	}
	
	public <T extends BaseObject> T readByClass(final Integer objId, final Class<T> clz) {
		return entitymanager.find(clz ,objId);
	}
	
	@Override
	public void finalize() {
		entitymanager.close();
		emfactory.close();
	}

}
