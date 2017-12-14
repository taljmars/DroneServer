package com.dbanalyzer;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.wsSoap.QueryRequestRemote;
import com.db.persistence.wsSoap.QueryResponseRemote;
import com.db.persistence.wsSoap.QuerySvcRemote;
import com.dbanalyzer.internal.RestClientHelper;
import com.generic_tools.Pair.Pair;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuerySvcRemoteWrapper {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuerySvcRemoteWrapper.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Initializing QuerySvcRemoteWrapper");
    }

//    @Autowired
    private QuerySvcRemote querySvcRemote;

    @Autowired
    private RestClientHelper restClientHelper;

    public static String userName = "PUBLIC";
    public QueryResponseRemote query(QueryRequestRemote queryRequestRemote) throws QueryRemoteException {
        try {
            //        return querySvcRemote.query(queryRequestRemote);
            WebResource.Builder builder = restClientHelper.getWebResource("queryForUser", "userName", userName);
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.debug("Request to be send: {} " + mapper.writeValueAsString(queryRequestRemote));
            ClientResponse response = builder.post(ClientResponse.class, mapper.writeValueAsString(queryRequestRemote));
            return resolveResponse(response);
        }
        catch (IOException e) {
            LOGGER.error("Failed to run query", e);
            throw new QueryRemoteException(e.getMessage());
        }
    }

    public <T extends BaseObject> QueryResponseRemote runNativeQueryWithClass(String queryString, String clz) throws QueryRemoteException {
        //        return querySvcRemote.runNativeQuery(queryString, clz);
        MultivaluedMap multivaluedMap = new MultivaluedMapImpl();
        multivaluedMap.add("queryString", queryString);
        multivaluedMap.add("clz", clz);
        multivaluedMap.add("userName", userName);
        WebResource.Builder builder = restClientHelper.getWebResource("runNativeQueryWithClassForUser", multivaluedMap);
        ClientResponse response = builder.get(ClientResponse.class);
        return resolveResponse(response);
    }

    public <T extends BaseObject> QueryResponseRemote runNativeQuery(String queryString) throws QueryRemoteException {
        //        return querySvcRemote.runNativeQuery(queryString, clz);
        MultivaluedMap multivaluedMap = new MultivaluedMapImpl();
        multivaluedMap.add("queryString", queryString);
        multivaluedMap.add("userName", userName);
        WebResource.Builder builder = restClientHelper.getWebResource("runNativeQueryForUser", multivaluedMap);
        ClientResponse response = builder.get(ClientResponse.class);
        return resolveResponse(response);
    }

    public <T extends BaseObject> QueryResponseRemote runNamedQuery(String queryString, String clz) throws QueryRemoteException{
    //        return querySvcRemote.runNativeQuery(queryString, clz);
        MultivaluedMap multivaluedMap = new MultivaluedMapImpl();
        multivaluedMap.add("queryString", queryString);
        multivaluedMap.add("clz", clz);
        multivaluedMap.add("userName", userName);
        WebResource.Builder builder = restClientHelper.getWebResource("runNamedQueryForUser", multivaluedMap);
        ClientResponse response = builder.get(ClientResponse.class);
        return resolveResponse(response);
    }

    private QueryResponseRemote resolveResponse(ClientResponse response) throws QueryRemoteException {
        try {
            ClientResponse.Status status = response.getClientResponseStatus();

            if (status != ClientResponse.Status.OK) {
                Pair<Class, ? extends Exception> pair = restClientHelper.getErrorAndMessage(response);
                Class cls = pair.getFirst();

                if (cls.isAssignableFrom(QueryRemoteException.class))
                    throw new QueryRemoteException(pair.getSecond().getMessage());

                throw new RuntimeException("Failed to run query: " + response.getResponseDate());
            }

            String jsonString = response.getEntity(String.class);
            LOGGER.debug("Query response: {}", jsonString);

            JSONObject jsonObject = new JSONObject(jsonString);
            List<? extends BaseObject> resultList = new ArrayList<>();
            if (jsonObject.has("resultList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("resultList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    resultList.add(restClientHelper.resolve((JSONObject) jsonArray.get(i)));
                }
            }
            QueryResponseRemote queryResponseRemote = new QueryResponseRemote();
            queryResponseRemote.setResultList(resultList);
            LOGGER.debug("Query response size: {}", queryResponseRemote.getResultList() == null ? 0 : queryResponseRemote.getResultList().size());
            return queryResponseRemote;
        }
        catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Failed to run query", e);
            throw new QueryRemoteException(e.getMessage());
        }
    }
}
