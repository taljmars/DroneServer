
package com.dronedb.persistence.scheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for takeoff complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="takeoff">
 *   &lt;complexContent>
 *     &lt;extension base="{http://scheme.persistence.dronedb.com/}missionItem">
 *       &lt;sequence>
 *         &lt;element name="finishedAlt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "takeoff", propOrder = {
    "finishedAlt"
})
public class Takeoff
    extends MissionItem
{

    protected double finishedAlt;

    /**
     * Gets the value of the finishedAlt property.
     * 
     */
    public double getFinishedAlt() {
        return finishedAlt;
    }

    /**
     * Sets the value of the finishedAlt property.
     * 
     */
    public void setFinishedAlt(double value) {
        this.finishedAlt = value;
    }

}
