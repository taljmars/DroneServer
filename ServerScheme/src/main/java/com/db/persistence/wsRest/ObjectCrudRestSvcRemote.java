package com.db.persistence.wsRest;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.scheme.BaseObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ObjectCrudRestSvcRemote
{
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> create(@RequestParam String clz, @RequestHeader("token") String token)  throws ObjectInstanceRemoteException;

    @RequestMapping(value = "/update", method = RequestMethod.POST ,consumes={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    <T extends BaseObject> ResponseEntity<T> update(@RequestBody T object, @RequestHeader("token") String token) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

	@RequestMapping(value = "/updateArray", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> void updateArray(@RequestBody T[] objects, @RequestHeader("token") String token) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    <T extends BaseObject> ResponseEntity delete(@RequestBody T object, @RequestHeader("token") String token) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException;

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> read(@RequestParam String objId, @RequestHeader("token") String token) throws ObjectNotFoundRemoteException;

	@RequestMapping(value = "/readByClass", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> readByClass(@RequestParam String objId, @RequestHeader("token") String clz, @RequestParam String token) throws ObjectNotFoundRemoteException;

}
