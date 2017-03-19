package com.dronedb.persistence.services.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import com.dronedb.persistence.services.QueryRequest;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.persistence.scheme.BaseObject;

@Component
public class QuerySvcImpl implements QuerySvc {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	//@Transactional
	public <T extends BaseObject> List<T> runNativeQuery(String queryString, Class<T> clz)
	{
		Query query = entityManager.createNativeQuery(queryString, clz);
		List<?> lst = query.getResultList();
		System.err.println("Service " + lst);
		List<T> arr = new ArrayList();
		
		Iterator<?> it = lst.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (clz.isInstance(o))
				arr.add((T) o);
		}
		
		return arr;
	}
	
	@Override
	//@Transactional
	public <T extends BaseObject> List<T> runNamedQuery(String queryString, Class<T> clz)
	{
		System.err.println("Running named query");
		TypedQuery<T> query = entityManager.createNamedQuery(queryString, clz);
		query = query.setParameter(1, "9c0ee519-c4eb-420b-ac05-613bce92a1c0");
		List<T> lst = query.getResultList();
		System.err.println("Service " + lst);
		List<T> arr = new ArrayList();
		
		Iterator<?> it = lst.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (clz.isInstance(o))
				arr.add((T) o);
		}
		
		return arr;
	}

	@Override
	//@Transactional
	public <T extends BaseObject> List<? extends BaseObject> query(QueryRequest queryRequest)
	{
		System.out.println(queryRequest.toString());
		Class<? extends BaseObject> clz = queryRequest.getClz();
		String queryStr = queryRequest.getQuery();
		Map<String, String> params = queryRequest.getParameters();
		
		
		System.err.println("Running named query");
		TypedQuery<? extends BaseObject> query = entityManager.createNamedQuery(queryStr, clz);
		
//		for (int i = 1; i <= params.size(); i++) {
//			query = query.setParameter(i, params.get(i-1));
//		}
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		
		List<? extends BaseObject> lst = query.getResultList();
		System.err.println("Service " + lst);
		List<T> arr = new ArrayList();
		
		Iterator<?> it = lst.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (clz.isInstance(o))
				arr.add((T) o);
		}
		
		return arr;
		
	}

}
