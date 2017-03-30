
package com.dronedb.persistence.scheme.apis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.dronedb.persistence.scheme.apis package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryRequestRemote_QNAME = new QName("http://apis.scheme.persistence.dronedb.com/", "queryRequestRemote");
    private final static QName _QueryResponseRemote_QNAME = new QName("http://apis.scheme.persistence.dronedb.com/", "queryResponseRemote");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.dronedb.persistence.scheme.apis
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryRequestRemote }
     * 
     */
    public QueryRequestRemote createQueryRequestRemote() {
        return new QueryRequestRemote();
    }

    /**
     * Create an instance of {@link QueryRequestRemote.Parameters }
     * 
     */
    public QueryRequestRemote.Parameters createQueryRequestRemoteParameters() {
        return new QueryRequestRemote.Parameters();
    }

    /**
     * Create an instance of {@link QueryResponseRemote }
     * 
     */
    public QueryResponseRemote createQueryResponseRemote() {
        return new QueryResponseRemote();
    }

    /**
     * Create an instance of {@link Waypoint }
     * 
     */
    public Waypoint createWaypoint() {
        return new Waypoint();
    }

    /**
     * Create an instance of {@link PolygonPerimeter }
     * 
     */
    public PolygonPerimeter createPolygonPerimeter() {
        return new PolygonPerimeter();
    }

    /**
     * Create an instance of {@link CirclePerimeter }
     * 
     */
    public CirclePerimeter createCirclePerimeter() {
        return new CirclePerimeter();
    }

    /**
     * Create an instance of {@link Takeoff }
     * 
     */
    public Takeoff createTakeoff() {
        return new Takeoff();
    }

    /**
     * Create an instance of {@link RegionOfInterest }
     * 
     */
    public RegionOfInterest createRegionOfInterest() {
        return new RegionOfInterest();
    }

    /**
     * Create an instance of {@link ReturnToHome }
     * 
     */
    public ReturnToHome createReturnToHome() {
        return new ReturnToHome();
    }

    /**
     * Create an instance of {@link Point }
     * 
     */
    public Point createPoint() {
        return new Point();
    }

    /**
     * Create an instance of {@link Mission }
     * 
     */
    public Mission createMission() {
        return new Mission();
    }

    /**
     * Create an instance of {@link Land }
     * 
     */
    public Land createLand() {
        return new Land();
    }

    /**
     * Create an instance of {@link Circle }
     * 
     */
    public Circle createCircle() {
        return new Circle();
    }

    /**
     * Create an instance of {@link QueryRequestRemote.Parameters.Entry }
     * 
     */
    public QueryRequestRemote.Parameters.Entry createQueryRequestRemoteParametersEntry() {
        return new QueryRequestRemote.Parameters.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRequestRemote }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apis.scheme.persistence.dronedb.com/", name = "queryRequestRemote")
    public JAXBElement<QueryRequestRemote> createQueryRequestRemote(QueryRequestRemote value) {
        return new JAXBElement<QueryRequestRemote>(_QueryRequestRemote_QNAME, QueryRequestRemote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryResponseRemote }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://apis.scheme.persistence.dronedb.com/", name = "queryResponseRemote")
    public JAXBElement<QueryResponseRemote> createQueryResponseRemote(QueryResponseRemote value) {
        return new JAXBElement<QueryResponseRemote>(_QueryResponseRemote_QNAME, QueryResponseRemote.class, null, value);
    }

}
