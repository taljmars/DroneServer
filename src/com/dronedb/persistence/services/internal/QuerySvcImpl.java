package com.dronedb.services.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;

import com.dronedb.scheme.BaseObject;
import com.dronedb.services.QuerySvc;

public class QuerySvcImpl implements QuerySvc {
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public <T extends BaseObject> List<T> runNativeQuery(String queryString, Class<T> clz) {
		Query query = entityManager.createNativeQuery(queryString, clz);
		List<?> lst = query.getResultList();
		System.err.println("Service " + lst);
		List<T> arr = new ArrayList<>();
		
		Iterator<?> it = lst.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (clz.isInstance(o))
				arr.add((T) o);
		}
		
		return arr;
	}
	
	@Override
	public <T extends BaseObject> List<T> runNamedQuery(String queryString, Class<T> clz) {
		TypedQuery<T> query = entityManager.createNamedQuery(queryString, clz);
		List<T> lst = query.getResultList();
		System.err.println("Service " + lst);
		List<T> arr = new ArrayList<>();
		
		Iterator<?> it = lst.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (clz.isInstance(o))
				arr.add((T) o);
		}
		
		return arr;
	}

}
