package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.QueryRequestRemote;
import com.dronedb.persistence.scheme.QueryResponseRemote;
import com.dronedb.persistence.scheme.QuerySvcRemote;
import com.dronedb.persistence.services.QueryRequest;
import com.dronedb.persistence.services.QuerySvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.QuerySvcRemote")
public class QuerySvcRemoteImpl implements QuerySvcRemote {
	
	@Autowired
	private QuerySvc querySvc;

	@Override
	public <T extends BaseObject> QueryResponseRemote runNativeQuery(String queryString, Class<T> clz) {

		List<T> res = (List<T>) querySvc.runNativeQuery(queryString, clz);

		List<T> clonedRes = new ArrayList<>();
		for (T obj : res) {
			clonedRes.add((T) obj.copy());
		}
		
		QueryResponseRemote response = new QueryResponseRemote();
		response.setResult(clonedRes);
		return response;
	}
	
	@Override
	public <T extends BaseObject> QueryResponseRemote runNamedQuery(String queryString, Class<T> clz) {
		
		List<T> res = (List<T>) querySvc.runNamedQuery(queryString, clz);

		List<T> clonedRes = new ArrayList<>();
		for (T obj : res) {
			clonedRes.add((T) obj.copy());
		}

		QueryResponseRemote response = new QueryResponseRemote();
		response.setResult(clonedRes);
		return response;
	}

	@Override
	public <T extends BaseObject> QueryResponseRemote query(QueryRequestRemote queryRequestRemote) {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setClz(queryRequestRemote.getClz());
		queryRequest.setQuery(queryRequestRemote.getQuery());
		queryRequest.setParameters(queryRequestRemote.getParameters());
		List<T> res = (List<T>) querySvc.query(queryRequest);

		List<T> clonedRes = new ArrayList<>();
		for (T obj : res) {
			clonedRes.add((T) obj.copy());
		}

		QueryResponseRemote response = new QueryResponseRemote();
		response.setResult(clonedRes);
		return response;
	}

}
