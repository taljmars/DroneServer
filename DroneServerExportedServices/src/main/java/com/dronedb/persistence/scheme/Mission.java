
package com.dronedb.persistence.scheme;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mission">
 *   &lt;complexContent>
 *     &lt;extension base="{http://scheme.persistence.dronedb.com/}baseObject">
 *       &lt;sequence>
 *         &lt;element name="defaultAlt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="missionItemsUids" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mission", propOrder = {
    "defaultAlt",
    "missionItemsUids",
    "name"
})
public class Mission
    extends BaseObject
{

    protected double defaultAlt;
    @XmlElement(nillable = true)
    protected List<String> missionItemsUids;
    protected String name;

    /**
     * Gets the value of the defaultAlt property.
     * 
     */
    public double getDefaultAlt() {
        return defaultAlt;
    }

    /**
     * Sets the value of the defaultAlt property.
     * 
     */
    public void setDefaultAlt(double value) {
        this.defaultAlt = value;
    }

    /**
     * Gets the value of the missionItemsUids property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the missionItemsUids property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMissionItemsUids().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMissionItemsUids() {
        if (missionItemsUids == null) {
            missionItemsUids = new ArrayList<String>();
        }
        return this.missionItemsUids;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
