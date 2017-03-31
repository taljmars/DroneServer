
package com.dronedb.persistence.scheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for waypoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="waypoint">
 *   &lt;complexContent>
 *     &lt;extension base="{http://scheme.persistence.dronedb.com/}missionItem">
 *       &lt;sequence>
 *         &lt;element name="acceptanceRadius" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="altitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="delay" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="orbitCCW" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="orbitalRadius" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="yawAngle" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "waypoint", propOrder = {
    "acceptanceRadius",
    "altitude",
    "delay",
    "orbitCCW",
    "orbitalRadius",
    "yawAngle"
})
public class Waypoint
    extends MissionItem
{

    protected double acceptanceRadius;
    protected Double altitude;
    protected Double delay;
    protected boolean orbitCCW;
    protected double orbitalRadius;
    protected double yawAngle;

    /**
     * Gets the value of the acceptanceRadius property.
     * 
     */
    public double getAcceptanceRadius() {
        return acceptanceRadius;
    }

    /**
     * Sets the value of the acceptanceRadius property.
     * 
     */
    public void setAcceptanceRadius(double value) {
        this.acceptanceRadius = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAltitude(Double value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the delay property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDelay() {
        return delay;
    }

    /**
     * Sets the value of the delay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDelay(Double value) {
        this.delay = value;
    }

    /**
     * Gets the value of the orbitCCW property.
     * 
     */
    public boolean isOrbitCCW() {
        return orbitCCW;
    }

    /**
     * Sets the value of the orbitCCW property.
     * 
     */
    public void setOrbitCCW(boolean value) {
        this.orbitCCW = value;
    }

    /**
     * Gets the value of the orbitalRadius property.
     * 
     */
    public double getOrbitalRadius() {
        return orbitalRadius;
    }

    /**
     * Sets the value of the orbitalRadius property.
     * 
     */
    public void setOrbitalRadius(double value) {
        this.orbitalRadius = value;
    }

    /**
     * Gets the value of the yawAngle property.
     * 
     */
    public double getYawAngle() {
        return yawAngle;
    }

    /**
     * Sets the value of the yawAngle property.
     * 
     */
    public void setYawAngle(double value) {
        this.yawAngle = value;
    }

}
