package PojoTests;


import com.dronedb.persistence.scheme.*;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Date;

import static PojoTests.PojoTestHelper.testPojo;
import static org.junit.Assert.assertTrue;

@Component
public class PojoTests {

    @Test
    public void LoiterTurns() {
        LoiterTurns obj = new LoiterTurns();
        assertTrue(testPojo(obj));
    }

    @Test
    public void LoiterTime() {
        LoiterTime obj = new LoiterTime();
        obj.setAltitude(1.0);
        obj.setSeconds(4);
        obj.setLat(1.1);
        obj.setLon(2.2);
        obj.setClz(LoiterTime.class);
        obj.setDeleted(false);
        obj.setUpdatedAt(new Date());
        obj.setFromRevision(3);
        obj.setCreationDate(new Date());
        assertTrue(testPojo(obj));
    }

    @Test
    public void LoiterUnlimited() {
        LoiterUnlimited obj = new LoiterUnlimited();
        assertTrue(testPojo(obj));
    }

    @Test
    public void Waypoint() {
        Waypoint obj = new Waypoint();
        assertTrue(testPojo(obj));
    }

    @Test
    public void Takeoff() {
        Takeoff obj = new Takeoff();
        assertTrue(testPojo(obj));
    }

    @Test
    public void ReturnToHome() {
        ReturnToHome obj = new ReturnToHome();
        assertTrue(testPojo(obj));
    }

    @Test
    public void Mission() {
        Mission obj = new Mission();
        assertTrue(testPojo(obj));
    }

    @Test
    public void CirclePerimeter() {
        CirclePerimeter obj = new CirclePerimeter();
        assertTrue(testPojo(obj));
    }

    @Test
    public void PolygonPerimeter() {
        PolygonPerimeter obj = new PolygonPerimeter();
        assertTrue(testPojo(obj));
    }

    @Test
    public void Point() {
        Point obj = new Point();
        assertTrue(testPojo(obj));
    }

    @Test
    public void Land() {
        Land obj = new Land();
        assertTrue(testPojo(obj));
    }

    @Test
    public void RegionOfInterest() {
        RegionOfInterest obj = new RegionOfInterest();
        assertTrue(testPojo(obj));
    }

    @Test
    public void SplineWaypoint() {
        SplineWaypoint obj = new SplineWaypoint();
        assertTrue(testPojo(obj));
    }
}
