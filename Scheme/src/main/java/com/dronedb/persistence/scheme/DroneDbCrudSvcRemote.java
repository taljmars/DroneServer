package com.dronedb.persistence.scheme;

import javassist.tools.rmi.ObjectNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import java.util.List;
import java.util.UUID;

@WebService
@SOAPBinding(style = Style.RPC)
public interface DroneDbCrudSvcRemote
{
	@WebMethod 
	<T extends BaseObject> T create(@WebParam final Class<T> clz) throws ObjectInstanceRemoteException;
	
	@WebMethod 
	<T extends BaseObject> T update(@WebParam T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;
	
	@WebMethod
	<T extends BaseObject> void updateSet(@WebParam List<T> objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException;

	@WebMethod 
	<T extends BaseObject> void delete(@WebParam T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException;
	
	@WebMethod 
	<T extends BaseObject> T read(@WebParam final UUID objId) throws ObjectNotFoundException;
	
	@WebMethod 
	<T extends BaseObject> T readByClass(@WebParam final UUID objId, @WebParam final Class<T> clz) throws ObjectNotFoundException;
}
