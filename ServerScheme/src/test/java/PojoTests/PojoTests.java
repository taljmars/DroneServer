package PojoTests;


import com.db.persistence.scheme.DummyBaseObject;
import com.db.persistence.scheme.LockedObject;
import com.db.persistence.scheme.ObjectDeref;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static PojoTests.PojoTestHelper.testPojo;
import static org.junit.Assert.assertTrue;

@Component
public class PojoTests {

    @Test
    public void DummyBaseObject() {
        DummyBaseObject dummyBaseObject = new DummyBaseObject();
        dummyBaseObject.setName("Test1");
        assertTrue(testPojo(dummyBaseObject));
    }

    @Test
    public void LockedObject() {
        LockedObject lockedObject = new LockedObject();
        lockedObject.setReferredCtx(3);
        lockedObject.setReferredObjId("asdasd");
        assertTrue(testPojo(lockedObject));
    }

    @Test
    public void TestPOJO() {
        ObjectDeref objectDeref = new ObjectDeref();
        objectDeref.setClzType(DummyBaseObject.class);
        assertTrue(testPojo(objectDeref));
    }
}
