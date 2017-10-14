package com.db.persistence.wsSoap;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.scheme.BaseObject;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.UUID;

import static com.db.persistence.wsRest.Constants.WS_NAMESPACE;

//@WebService(targetNamespace = "http://ws.persistence.dronedb.com/")
@WebService(targetNamespace = WS_NAMESPACE + "/ObjectCrudSvcRemote",
	wsdlLocation = "scheme.persistence.db.com")
//@WebService
public interface ObjectCrudSvcRemote
{
	@WebMethod
	<T extends BaseObject> T create(@WebParam String clzName) throws ObjectInstanceRemoteException;

	@WebMethod 
	<T extends BaseObject> T update(@WebParam T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;
	
	@WebMethod
	<T extends BaseObject> void updateArray(@WebParam T[] objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

	@WebMethod 
	<T extends BaseObject> T delete(@WebParam T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException;
	
	@WebMethod 
	<T extends BaseObject> T read(@WebParam final UUID objId) throws ObjectNotFoundRemoteException;
	
	@WebMethod
	<T extends BaseObject> T readByClass(@WebParam final UUID objId, @WebParam final Class<T> clz) throws ObjectNotFoundRemoteException;
}
