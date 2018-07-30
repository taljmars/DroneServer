package ServerIntegrationTest;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.DummyBaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.services.QueryRequest;
import com.db.persistence.services.QuerySvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QuerySvcTest {

    private ObjectCrudSvc objectCrudSvc;
    private QuerySvc querySvc;

    public QuerySvcTest(QuerySvc querySvc, ObjectCrudSvc objectCrudSvc) {
        this.querySvc = querySvc;
        this.objectCrudSvc = objectCrudSvc;
    }

    public void go() throws Exception {

        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setQuery("GetAllDummyBaseObject");
        queryRequest.setClz(DummyBaseObject.class);
        List<? extends BaseObject> res = querySvc.query(queryRequest);
        assertTrue(res.isEmpty());

        DummyBaseObject dummyBaseObject1 = objectCrudSvc.create(DummyBaseObject.class.getName());
        dummyBaseObject1.setName("dm1");
        dummyBaseObject1 = objectCrudSvc.update(dummyBaseObject1);

        DummyBaseObject dummyBaseObject2 = objectCrudSvc.create(DummyBaseObject.class.getName());
        dummyBaseObject2.setName("dm2");
        dummyBaseObject2 = objectCrudSvc.update(dummyBaseObject2);

        queryRequest = new QueryRequest();
        queryRequest.setQuery("GetAllDummyBaseObject");
        queryRequest.setClz(DummyBaseObject.class);
        res = querySvc.query(queryRequest);

//        System.err.println(res);

        assertTrue(res.size() == 2);
        assertTrue(res.get(0).equals(dummyBaseObject1) || res.get(0).equals(dummyBaseObject2));
        assertTrue(res.get(1).equals(dummyBaseObject1) || res.get(1).equals(dummyBaseObject2));

        res = querySvc.runNamedQuery("GetAllDummyBaseObject", DummyBaseObject.class, 0, 0);
        assertTrue(res.size() == 2);
        assertTrue(res.get(0).equals(dummyBaseObject1) || res.get(0).equals(dummyBaseObject2));
        assertTrue(res.get(1).equals(dummyBaseObject1) || res.get(1).equals(dummyBaseObject2));

        objectCrudSvc.delete(dummyBaseObject1);
        objectCrudSvc.delete(dummyBaseObject2);

        res = querySvc.runNamedQuery("GetAllDummyBaseObject", DummyBaseObject.class, 0, 0);
        assertTrue(res.isEmpty());

    }
}
