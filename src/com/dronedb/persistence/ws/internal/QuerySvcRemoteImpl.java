package com.dronedb.ws.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jws.WebService;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dronedb.scheme.BaseObject;
import com.dronedb.services.QuerySvc;
import com.dronedb.ws.QueryResponseRemote;
import com.dronedb.ws.QuerySvcRemote;

@Component
@WebService(endpointInterface = "com.dronedb.ws.QuerySvcRemote")
public class QuerySvcRemoteImpl implements QuerySvcRemote {
	
	@Autowired
	private QuerySvc querySvc;

	@Override
	public <T extends BaseObject> QueryResponseRemote runNativeQuery(String queryString, Class<T> clz) {
		
		List<? extends BaseObject> res = querySvc.runNativeQuery(queryString, clz);
		
		QueryResponseRemote response = new QueryResponseRemote();
		response.setResult(res);
		return response;
	}
	
	@Override
	public <T extends BaseObject> QueryResponseRemote runNamedQuery(String queryString, Class<T> clz) {
		
		List<? extends BaseObject> res = querySvc.runNamedQuery(queryString, clz);
		
		QueryResponseRemote response = new QueryResponseRemote();
		response.setResult(res);
		return response;
	}

}
