package com.db.persistence.wsSoap;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import static com.db.persistence.wsRest.Constants.WS_NAMESPACE;

@WebService(targetNamespace = WS_NAMESPACE + "/QuerySvcRemote")
//@SOAPBinding(style = Style.RPC)
public interface QuerySvcRemote {

//	@WebMethod
//	<T extends BaseObject> QueryResponseRemote runNativeQueryWithClass(@WebParam String queryString, @WebParam String clz) throws QueryRemoteException;
//
//	@WebMethod
//	<T extends BaseObject> QueryResponseRemote runNativeQuery(@WebParam String queryString) throws QueryRemoteException;
//
	@WebMethod
	<T extends BaseObject> QueryResponseRemote runNamedQuery(@WebParam String queryString, @WebParam String clz) throws QueryRemoteException;
	
	@WebMethod
	<T extends BaseObject> QueryResponseRemote query(@WebParam QueryRequestRemote queryRequestRemote) throws QueryRemoteException;

}
