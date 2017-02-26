package com.dronedb.persistence.ws.internal;

import java.util.List;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dronedb.persistence.services.QueryRequest;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.persistence.ws.QueryRequestRemote;
import com.dronedb.persistence.ws.QueryResponseRemote;
import com.dronedb.persistence.ws.QuerySvcRemote;
import com.dronedb.persistence.scheme.BaseObject;

@Component
@WebService(endpointInterface = "com.dronedb.persistence.ws.QuerySvcRemote")
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

	@Override
	public <T extends BaseObject> QueryResponseRemote query(QueryRequestRemote queryRequestRemote) {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setClz(queryRequestRemote.getClz());
		queryRequest.setQuery(queryRequestRemote.getQuery());
		queryRequest.setParameters(queryRequestRemote.getParameters());
		List<? extends BaseObject> res = querySvc.query(queryRequest);
		
		QueryResponseRemote response = new QueryResponseRemote();
		response.setResult(res);
		return response;
	}

}
