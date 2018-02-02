package com.db.persistence.wsRest.internal;

import com.db.persistence.exception.QueryException;
import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;
import com.db.persistence.wsRest.QueryRequestRemote;
import com.db.persistence.wsRest.QueryResponseRemote;
import com.db.persistence.wsRest.QueryRestSvcRemote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QueryRestSvcRemoteImpl implements QueryRestSvcRemote {

	private final static Logger LOGGER = Logger.getLogger(QueryRestSvcRemoteImpl.class);
	
	@Autowired
	private QuerySvc querySvc;

//	@Override
//	@RequestMapping(value = "/runNativeQueryWithClassForUser", method = RequestMethod.GET)
//	@ResponseBody
//	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQueryWithClassForUser(@RequestParam String queryString, @RequestParam String clz, @RequestParam String userName) throws QueryRemoteException {
//		querySvc.setForUser(userName);
//		return runNativeQueryWithClass(queryString, clz);
//	}
//
//	@Override
//	@RequestMapping(value = "/runNativeQueryWithClass", method = RequestMethod.GET)
//	@ResponseBody
//	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQueryWithClass(@RequestParam String queryString, @RequestParam String clz) throws QueryRemoteException {
//		QueryResponseRemote response = new QueryResponseRemote();
//		try {
//			List<T> res = (List<T>) querySvc.runNativeQueryWithClass(queryString, (Class<T>) Class.forName(clz));
//
//			List<T> clonedRes = new ArrayList<>();
//			for (T obj : res) {
//				clonedRes.add((T) obj.copy());
//			}
//			response.setResult(clonedRes);
//			return new ResponseEntity<QueryResponseRemote>(response, HttpStatus.OK);
//		}
//		catch (ClassNotFoundException e) {
//			LOGGER.error(e);
//			throw new QueryRemoteException(e.getMessage());
//		}
//	}
//
//	@Override
//	@RequestMapping(value = "/runNativeQueryForUser", method = RequestMethod.GET)
//	@ResponseBody
//	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQueryForUser(@RequestParam String queryString, @RequestParam String userName) throws QueryRemoteException {
//		querySvc.setForUser(userName);
//		return runNativeQuery(queryString);
//	}
//
//	@Override
//	@RequestMapping(value = "/runNativeQuery", method = RequestMethod.GET)
//	@ResponseBody
//	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQuery(@RequestParam String queryString) throws QueryRemoteException {
//		try {
//			QueryResponseRemote response = new QueryResponseRemote();
//			List<T> res = (List<T>) querySvc.runNativeQuery(queryString);
//
//			List<T> clonedRes = new ArrayList<>();
//			for (T obj : res) {
//				clonedRes.add((T) obj.copy());
//			}
//			response.setResult(clonedRes);
//			return new ResponseEntity<QueryResponseRemote>(response, HttpStatus.OK);
//		}
//		catch (QueryException e) {
//			LOGGER.error("Failed to run query", e);
//			throw new QueryRemoteException(e.getMessage());
//		}
//	}

	@Override
	@RequestMapping(value = "/runNamedQueryForUser", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNamedQueryForUser(@RequestParam String queryString, @RequestParam String clz, @RequestParam String userName) throws QueryRemoteException {
		querySvc.setForUser(userName);
		return runNamedQuery(queryString, clz);
	}

	@Override
	@RequestMapping(value = "/runNamedQuery", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNamedQuery(@RequestParam String queryString, @RequestParam String clz) throws QueryRemoteException{
		QueryResponseRemote response = new QueryResponseRemote();
		try {
			List<T> res = (List<T>) querySvc.runNamedQuery(queryString, (Class<T>) Class.forName(clz));

			List<T> clonedRes = new ArrayList<>();
			for (T obj : res) {
				clonedRes.add((T) obj.copy());
			}

			response.setResult(clonedRes);
			return new ResponseEntity(response, HttpStatus.OK);
		}
		catch (ClassNotFoundException e) {
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}

	@Override
	@RequestMapping(value = "/queryForUser", method = RequestMethod.POST)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> queryForUser(@RequestBody QueryRequestRemote queryRequestRemote, @RequestParam String userName) throws QueryRemoteException {
		querySvc.setForUser(userName);
		return query(queryRequestRemote);
	}

	@Override
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> query(@RequestBody QueryRequestRemote queryRequestRemote) throws QueryRemoteException{
		LOGGER.debug("Query request from client: " + queryRequestRemote);
		QueryRequest queryRequest = new QueryRequest();
		QueryResponseRemote response = new QueryResponseRemote();
		try {
			queryRequest.setClz((Class<? extends BaseObject>) Class.forName(queryRequestRemote.getClz()));

			queryRequest.setQuery(queryRequestRemote.getQuery());
			queryRequest.setParameters(queryRequestRemote.getParameters());
			List<T> res = (List<T>) querySvc.query(queryRequest);

			List<T> clonedRes = new ArrayList<>();
			for (T obj : res) {
				clonedRes.add((T) obj.copy());
			}

			response.setResult(clonedRes);
			return new ResponseEntity<QueryResponseRemote>(response, HttpStatus.OK);
		}
		catch (ClassNotFoundException  | QueryException e) {
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}

}
