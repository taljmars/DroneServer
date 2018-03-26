package com.db.persistence.ws;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.scheme.BaseObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ObjectCrudSvcRemote
{
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> create(@RequestParam String clz)  throws ObjectInstanceRemoteException;

    @RequestMapping(value = "/update", method = RequestMethod.POST ,consumes={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    <T extends BaseObject> ResponseEntity<T> update(@RequestBody T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

	@RequestMapping(value = "/updateArray", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> void updateArray(@RequestBody T[] objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    <T extends BaseObject> ResponseEntity delete(@RequestBody T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException;

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> read(@RequestParam String objId) throws ObjectNotFoundRemoteException;

	@RequestMapping(value = "/readByClass", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> readByClass(@RequestParam String objId, @RequestParam String clz) throws ObjectNotFoundRemoteException;

}
