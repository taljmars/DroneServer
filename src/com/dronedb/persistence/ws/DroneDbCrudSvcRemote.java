package com.dronedb.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.dronedb.scheme.BaseObject;

@WebService
@SOAPBinding(style = Style.RPC)
public interface DroneDbCrudSvcRemote
{
	@WebMethod 
	String CheckConnection();
	
	@WebMethod 
	<T extends BaseObject> T create(@WebParam final Class<T> clz);
	
	@WebMethod 
	<T extends BaseObject> void update(@WebParam T object);
	
	@WebMethod 
	<T extends BaseObject> void delete(@WebParam T object);
	
	@WebMethod 
	<T extends BaseObject> T read(@WebParam final Integer objId);
	
	@WebMethod 
	<T extends BaseObject> T readByClass(@WebParam final Integer objId, @WebParam final Class<T> clz);
}
