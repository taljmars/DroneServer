package com.db.persistence.services.internal;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.db.persistence.exception.QueryException;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.db.persistence.scheme.BaseObject;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QuerySvcImpl implements QuerySvc {

	private final static Logger LOGGER = LoggerFactory.getLogger(QuerySvcImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public <T extends BaseObject> List<T> runNativeQueryWithClass(String queryString, Class<T> clz)
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
	@Transactional
	public <T extends BaseObject> List<? extends BaseObject> runNativeQuery(String queryString) throws QueryException
	{
		try {
		Query query = entityManager.createQuery(queryString);
		List<?> lst = query.getResultList();
		System.err.println("Service " + lst);
		List<T> arr = new ArrayList();

		Iterator<?> it = lst.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			arr.add((T) o);
		}

		return arr;
		}
		catch (Exception e) {
			LOGGER.error("Failed to run query", e);
			throw new QueryException(e);
		}
	}

	@Override
	@Transactional
	public <T extends BaseObject> List<T> runNamedQuery(String queryString, Class<T> clz)
	{
		System.err.println("Running named query");
		TypedQuery<T> query = entityManager.createNamedQuery(queryString, clz);
		System.out.println(query.toString());
//		query = query.setParameter(1, "9c0ee519-c4eb-420b-ac05-613bce92a1c0");
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
	@Transactional
	public <T extends BaseObject> List<? extends BaseObject> query(QueryRequest queryRequest)
	{
		System.out.println(queryRequest.toString());
		Class<? extends BaseObject> clz = queryRequest.getClz();
		String queryStr = queryRequest.getQuery();
		Map<String, String> params = queryRequest.getParameters();


		System.err.println("Running named query: " + queryStr + ", for class: " + clz.getCanonicalName());
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