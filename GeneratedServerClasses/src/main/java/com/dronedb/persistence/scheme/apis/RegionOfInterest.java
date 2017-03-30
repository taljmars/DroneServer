
package com.dronedb.persistence.scheme.apis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for regionOfInterest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="regionOfInterest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://apis.scheme.persistence.dronedb.com/}missionItem">
 *       &lt;sequence>
 *         &lt;element name="altitude" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "regionOfInterest", propOrder = {
    "altitude"
})
public class RegionOfInterest
    extends MissionItem
{

    protected Double altitude;

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

}
