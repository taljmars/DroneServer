/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.ws.internal;

import com.db.persistence.exception.QueryException;
import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.QueryRequestRemote;
import com.db.persistence.scheme.QueryResponseRemote;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;
import com.db.persistence.ws.QuerySvcRemote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuerySvcRemoteImpl implements QuerySvcRemote {

	private final static Logger LOGGER = Logger.getLogger(QuerySvcRemoteImpl.class);
	
	@Autowired
	private QuerySvc querySvc;

	@Override
	@RequestMapping(value = "/runNamedQuery", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNamedQuery(
			@RequestParam String queryString,
			@RequestParam String clz) throws QueryRemoteException {

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
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<QueryResponseRemote> query(
			@RequestBody QueryRequestRemote queryRequestRemote) throws QueryRemoteException {

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
			LOGGER.error("Usually the issue is within the class value, in this reqesut the value is '" + queryRequestRemote.getClz() + "'");
			LOGGER.error(e);
			throw new QueryRemoteException(e.getMessage());
		}
	}

}
