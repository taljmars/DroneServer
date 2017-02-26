package com.dronedb.persistence.ws;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.dronedb.persistence.scheme.BaseObject;

@WebService
@SOAPBinding(style = Style.RPC)
public interface DroneDbCrudSvcRemote
{
	@WebMethod 
	String CheckConnection();
	
	@WebMethod 
	<T extends BaseObject> T create(@WebParam final Class<T> clz);
	
	@WebMethod 
	<T extends BaseObject> T update(@WebParam T object);
	
//	@WebMethod 
//	<T extends BaseObject> void updateSet(@WebParam Set<T> objects);
//	
	@WebMethod 
	<T extends BaseObject> void delete(@WebParam T object);
	
	@WebMethod 
	<T extends BaseObject> T read(@WebParam final String objId);
	
	@WebMethod 
	<T extends BaseObject> T readByClass(@WebParam final String objId, @WebParam final Class<T> clz);
}
