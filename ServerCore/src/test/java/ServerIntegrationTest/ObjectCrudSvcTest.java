package ServerIntegrationTest;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.DummyBaseObject;
import com.db.persistence.services.ObjectCrudSvc;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ObjectCrudSvcTest {

    private ObjectCrudSvc objectCrudSvc;

    public ObjectCrudSvcTest(ObjectCrudSvc objectCrudSvc) {
        this.objectCrudSvc = objectCrudSvc;
    }

    public void go() throws Exception {
        System.out.println("Checking CREATE");
        DummyBaseObject dummyBaseObject = objectCrudSvc.create(DummyBaseObject.class.getCanonicalName());
        assertNotNull(dummyBaseObject);
        assertNotNull(dummyBaseObject.getKeyId());
        assertNotNull(dummyBaseObject.getKeyId().getObjId());

        System.out.println("Checking UPDATE");
        dummyBaseObject.setName("DummyObject1");
        DummyBaseObject dummyBaseObjectAfterUpdate = objectCrudSvc.update(dummyBaseObject);
        assertNotNull(dummyBaseObjectAfterUpdate);
        assertTrue(dummyBaseObjectAfterUpdate.getKeyId().equals(dummyBaseObject.getKeyId()));

        System.out.println("Checking READ");
        String uuid = dummyBaseObject.getKeyId().getObjId();
        BaseObject foundObject = objectCrudSvc.read(uuid);
        assertNotNull(foundObject);
        assertTrue(foundObject instanceof DummyBaseObject);
        DummyBaseObject foundDummyBaseObject = (DummyBaseObject) foundObject;
        assertTrue(dummyBaseObject.equals(foundDummyBaseObject));

        System.out.println("Checking READBYCLASS");
        dummyBaseObject = objectCrudSvc.readByClass(foundDummyBaseObject.getKeyId().getObjId(), DummyBaseObject.class);
        assertNotNull(foundObject);
        assertTrue(dummyBaseObject.equals(foundDummyBaseObject));

        System.out.println("Checking UPDATEARRAY");
        dummyBaseObject.setName("DummyObject1.1");
        DummyBaseObject dummyBaseObject1 = objectCrudSvc.update(dummyBaseObject);
        assertNotNull(dummyBaseObject1);

        DummyBaseObject dummyBaseObject2 = objectCrudSvc.create(DummyBaseObject.class.getCanonicalName());
        assertNotNull(dummyBaseObject2);
        dummyBaseObject2.setName("DummyObject2.1");
        dummyBaseObject2 = objectCrudSvc.update(dummyBaseObject2);
        assertNotNull(dummyBaseObject2);

        BaseObject[] arr = (BaseObject[]) Arrays.asList(dummyBaseObject1, dummyBaseObject2).toArray();
        objectCrudSvc.updateArray(arr);

        DummyBaseObject dummyBaseObject1Found = objectCrudSvc.readByClass(dummyBaseObject1.getKeyId().getObjId(), DummyBaseObject.class);
        assertTrue(dummyBaseObject1.equals(dummyBaseObject1Found));
        DummyBaseObject dummyBaseObject2Found = objectCrudSvc.readByClass(dummyBaseObject2.getKeyId().getObjId(), DummyBaseObject.class);
        assertTrue(dummyBaseObject2.equals(dummyBaseObject2Found));

        System.out.println("Checking DELETE");
        BaseObject deletedObject = objectCrudSvc.delete(dummyBaseObject1);
        assertNotNull(deletedObject);
        assertTrue(foundObject instanceof DummyBaseObject);
        assertTrue(dummyBaseObjectAfterUpdate.getKeyId().equals(dummyBaseObject.getKeyId()));

        foundObject = objectCrudSvc.read(deletedObject.getKeyId().getObjId());
        assertTrue(foundObject == null);

        deletedObject = objectCrudSvc.delete(dummyBaseObject2);
        foundObject = objectCrudSvc.read(deletedObject.getKeyId().getObjId());
        assertTrue(foundObject == null);
    }
}
