package com.db.persistence.wsRest;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.scheme.BaseObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface ObjectCrudRestSvcRemote
{
	@RequestMapping(value = "/createForUser", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> createForUser(@RequestParam String clz, @RequestParam String userName)  throws ObjectInstanceRemoteException;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> create(@RequestParam String clzName) throws ObjectInstanceRemoteException;

    @RequestMapping(value = "/updateForUser", method = RequestMethod.POST
            ,consumes={MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    <T extends BaseObject> ResponseEntity<T> updateForUser(@RequestBody T object, @RequestParam String userName) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> update(@RequestBody T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

	@RequestMapping(value = "/updateArray", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> void updateArray(@RequestBody T[] objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/deleteForUser", method = RequestMethod.POST)
    @ResponseBody
    <T extends BaseObject> ResponseEntity deleteForUser(@RequestBody T object, @RequestParam String userName) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	<T extends BaseObject> ResponseEntity delete(@RequestBody T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException;

	@RequestMapping(value = "/readForUser", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> readForUser(@RequestParam UUID objId, @RequestParam String userName) throws ObjectNotFoundRemoteException;

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> read(@RequestParam UUID objId) throws ObjectNotFoundRemoteException;

	@RequestMapping(value = "/readByClassForUser", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> readByClassForUser(@RequestParam UUID objId, @RequestParam String clz, @RequestParam String userName) throws ObjectNotFoundRemoteException;

	@RequestMapping(value = "/readByClass", method = RequestMethod.GET)
	@ResponseBody
	<T extends BaseObject> ResponseEntity<T> readByClass(@RequestParam UUID objId, @RequestParam String clz) throws ObjectNotFoundRemoteException;
}