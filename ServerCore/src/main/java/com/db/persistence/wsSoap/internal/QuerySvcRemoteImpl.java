package com.db.persistence.wsSoap.internal;

import com.db.persistence.exception.QueryException;
import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.wsSoap.QueryRequestRemote;
import com.db.persistence.wsSoap.QueryResponseRemote;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;
import com.db.persistence.wsSoap.QuerySvcRemote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@Component
@WebService(endpointInterface = "com.db.persistence.wsSoap.QuerySvcRemote")
//		,targetNamespace = "http://scheme.persistence.db.com/")
public class QuerySvcRemoteImpl implements QuerySvcRemote {

	private final static Logger LOGGER = Logger.getLogger(QuerySvcRemoteImpl.class);
	
	@Autowired
	private QuerySvc querySvc;

	@Override
	public <T extends BaseObject> QueryResponseRemote runNativeQueryWithClass(String queryString, String clz) throws QueryRemoteException{
		QueryResponseRemote response = new QueryResponseRemote();
		try {
			List<T> res = (List<T>) querySvc.runNativeQueryWithClass(queryString, (Class<T>) Class.forName(clz));

			List<T> clonedRes = new ArrayList<>();
			for (T obj : res) {
				clonedRes.add((T) obj.copy());
			}

			response.setResultList(clonedRes);
			return response;
		}
		catch (ClassNotFoundException e) {
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}

	@Override
	public <T extends BaseObject> QueryResponseRemote runNativeQuery(String queryString) throws QueryRemoteException {
		try {
			QueryResponseRemote response = new QueryResponseRemote();
			List<T> res = (List<T>) querySvc.runNativeQuery(queryString);

			List<T> clonedRes = new ArrayList<>();
			for (T obj : res) {
				clonedRes.add((T) obj.copy());
			}

			response.setResultList(clonedRes);
			return response;
		}
		catch (QueryException e) {
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}
	
	@Override
	public <T extends BaseObject> QueryResponseRemote runNamedQuery(String queryString, String clz) throws QueryRemoteException{
		QueryResponseRemote response = new QueryResponseRemote();
		try {
			List<T> res = (List<T>) querySvc.runNamedQuery(queryString, (Class<T>) Class.forName(clz));

			List<T> clonedRes = new ArrayList<>();
			for (T obj : res) {
				clonedRes.add((T) obj.copy());
			}

			response.setResultList(clonedRes);
			return response;
		}
		catch (ClassNotFoundException e) {
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}

	@Override
	public <T extends BaseObject> QueryResponseRemote query(QueryRequestRemote queryRequestRemote) throws QueryRemoteException {
		try {
			QueryRequest queryRequest = new QueryRequest();
			queryRequest.setClz((Class<? extends BaseObject>) Class.forName(queryRequestRemote.getClz()));
			queryRequest.setQuery(queryRequestRemote.getQuery());
			queryRequest.setParameters(queryRequestRemote.getParameters());
			List<T> res = (List<T>) querySvc.query(queryRequest);

			List<T> clonedRes = new ArrayList<>();
			for (T obj : res) {
				clonedRes.add((T) obj.copy());
			}

			QueryResponseRemote response = new QueryResponseRemote();
			response.setResultList(clonedRes);
			return response;
		}
		catch (ClassNotFoundException | QueryException e) {
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}

}
