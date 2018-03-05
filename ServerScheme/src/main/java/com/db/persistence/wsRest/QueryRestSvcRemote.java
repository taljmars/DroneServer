package com.db.persistence.wsRest;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.QueryRequestRemote;
import com.db.persistence.scheme.QueryResponseRemote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface QueryRestSvcRemote {

	@RequestMapping(value = "/runNamedQuery", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> runNamedQuery(@RequestParam String queryString, @RequestParam String clz, @RequestHeader("token") String token) throws QueryRemoteException;

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<QueryResponseRemote> query(@RequestBody QueryRequestRemote queryRequestRemote, @RequestHeader("token") String token) throws QueryRemoteException;

}
