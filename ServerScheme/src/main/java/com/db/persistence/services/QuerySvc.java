package com.db.persistence.services;

import java.util.List;

import com.db.persistence.exception.QueryException;
import com.db.persistence.scheme.BaseObject;

public interface QuerySvc {

    void setForUser(String userName);

    /**
	 * The following enable the user to run any queries he wish to run by writing the query syntax
	 * and send it to this API.
	 *
	 * @param query The SQL statement of the query
	 * @param clz The required type to be returned
	 * @param <T> Generic type of the object
	 * @return List of query results
	 */
//	<T extends BaseObject> List<? extends BaseObject> runNativeQueryWithClass(String query, Class<T> clz);

	/**
	 * The following enable the user to run any queries he wish to run by writing the query syntax
	 * and send it to this API.
	 *
	 * @param query The SQL statement of the query
	 * @param <T> Generic type of the object
	 * @return List of query results
	 */
//	<T extends BaseObject> List<? extends BaseObject> runNativeQuery(String query) throws QueryException;

	/**
	 * The following enable the user to run a ready made queries written in the code itself.
	 *
	 * @param query The SQL statement of the query
	 * @param clz The required type to be returned
	 * @param <T> Generic type of the object
	 * @return List of query results
	 */
	<T extends BaseObject> List<? extends BaseObject> runNamedQuery(String query, Class<T> clz);

	/**
	 * The following enable the user to send query request in order to get the list of objects
	 * corresponding to the query execution.
	 *
	 * @param queryRequest Object represent the query demand
	 * @param <T> Generic type of an object
	 * @return List of query results
	 */
	<T extends BaseObject> List<? extends BaseObject> query(QueryRequest queryRequest) throws QueryException;
}
