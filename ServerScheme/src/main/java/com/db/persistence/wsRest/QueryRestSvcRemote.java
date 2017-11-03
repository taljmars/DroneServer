package com.db.persistence.wsRest;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface QueryRestSvcRemote {

    @RequestMapping(value = "/runNativeQueryForUser", method = RequestMethod.GET)
    @ResponseBody
    <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQueryForUser(@RequestParam String queryString, @RequestParam String userName) throws QueryRemoteException;

    @RequestMapping(value = "/runNativeQuery", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQuery(@RequestBody String queryString) throws QueryRemoteException;

    @RequestMapping(value = "/runNativeQueryWithClassForName", method = RequestMethod.GET)
    @ResponseBody
    <T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQueryWithClassForUser(@RequestParam String queryString, @RequestParam String clz, @RequestParam String userName) throws QueryRemoteException;

    @RequestMapping(value = "/runNativeQueryWithClass", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> runNativeQueryWithClass(@RequestBody String queryString, @RequestBody String clz) throws QueryRemoteException;

	@RequestMapping(value = "/runNamedQueryForUser", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> runNamedQueryForUser(@RequestParam String queryString, @RequestParam String clz, @RequestParam String userName) throws QueryRemoteException;

	@RequestMapping(value = "/runNamedQuery", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> runNamedQuery(@RequestBody String queryString, @RequestBody String clz) throws QueryRemoteException;

	@RequestMapping(value = "/queryForUser", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> queryForUser(@RequestBody QueryRequestRemote queryRequestRemote, @RequestParam String userName) throws QueryRemoteException;

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> query(@RequestBody QueryRequestRemote queryRequestRemote) throws QueryRemoteException;

}
