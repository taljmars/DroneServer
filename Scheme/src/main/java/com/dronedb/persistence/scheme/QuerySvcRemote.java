package com.dronedb.persistence.scheme.apis;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface QuerySvcRemote {
	
	@WebMethod
	<T extends BaseObject> QueryResponseRemote runNativeQuery(@WebParam String queryString,@WebParam Class<T> clz);
	
	@WebMethod
	<T extends BaseObject> QueryResponseRemote runNamedQuery(@WebParam String queryString,@WebParam Class<T> clz);
	
	@WebMethod
	<T extends BaseObject> QueryResponseRemote query(@WebParam QueryRequestRemote queryRequestRemote);
}
