package com.db.persistence.services.internal;

import com.db.persistence.KeyAspect;
import com.db.persistence.exception.QueryException;
import com.db.persistence.workSessions.WorkSession;
import com.db.persistence.workSessions.WorkSessionManager;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class QuerySvcImpl implements QuerySvc {

	private final static Logger LOGGER = Logger.getLogger(QuerySvcImpl.class);

	private WorkSession workSession;

	@Autowired
	private WorkSessionManager workSessionManager;

	@PostConstruct
	public void init() {
		setForUser("PUBLIC");
	}

	private String currentUserName = "";
	@Override
	@Transactional
	public void setForUser(String userName) {
		currentUserName = userName;
		LOGGER.debug("Context was changed for user : " + userName);
		workSession = workSessionManager.createSession(userName);
		KeyAspect.setTenantContext(workSession.getSessionId());
	}

	@Override
	@Transactional
	public <T extends BaseObject> List<T> runNativeQueryWithClass(String queryString, Class<T> clz)
	{
		List<?> lst = workSession.getQueryExecutor().createNativeQuery(queryString, clz);
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
			List<?> lst = workSession.getQueryExecutor().createQuery(queryString);
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
		LOGGER.debug("Running named query");
		List<T> lst = workSession.getQueryExecutor().createNamedQuery(queryString, clz);
		LOGGER.debug("Service " + lst);
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
		LOGGER.debug(queryRequest.toString());
		Class<? extends BaseObject> clz = queryRequest.getClz();
		String queryStr = queryRequest.getQuery();
		Map<String, String> params = queryRequest.getParameters();


		LOGGER.debug("Running named query: " + queryStr + ", for class: " + clz.getCanonicalName());
		List<? extends BaseObject> lst = workSession.getQueryExecutor().createNamedQuery(queryStr, clz);
		LOGGER.debug("Service " + lst);
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