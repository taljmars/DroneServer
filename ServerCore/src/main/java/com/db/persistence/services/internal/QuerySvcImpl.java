package com.db.persistence.services.internal;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class QuerySvcImpl extends TokenAwareSvcImpl<QuerySvc> implements QuerySvc {

	private final static Logger LOGGER = Logger.getLogger(QuerySvcImpl.class);

	@PostConstruct
	public void init() {
		LOGGER.debug("Initialize QuerySvc");
	}

	@Override
	@Transactional
	public <T extends BaseObject> List<T> runNamedQuery(String queryString, Class<T> clz)
	{
		LOGGER.debug("Running named query");
		List<T> lst = workSession().getQueryExecutor().createNamedQuery(queryString, clz);
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
		Map<String, Object> params = queryRequest.getParameters();


		LOGGER.debug("Running named query: " + queryStr + ", for class: " + clz.getCanonicalName());
		List<? extends BaseObject> lst = workSession().getQueryExecutor().createNamedQuery(queryStr, params, clz);
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