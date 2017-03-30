
package com.dronedb.persistence.scheme.apis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for baseObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="createdAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="updatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseObject", propOrder = {
    "objId",
    "createdAt",
    "updatedAt",
    "version"
})
@XmlSeeAlso({
    Point.class,
    Mission.class,
    Perimeter.class,
    MissionItem.class
})
public abstract class BaseObject {

    @XmlElement(required = true)
    protected String objId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdAt;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updatedAt;
    protected long version;

    /**
     * Gets the value of the objId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjId() {
        return objId;
    }

    /**
     * Sets the value of the objId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjId(String value) {
        this.objId = value;
    }

    /**
     * Gets the value of the createdAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the value of the createdAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedAt(XMLGregorianCalendar value) {
        this.createdAt = value;
    }

    /**
     * Gets the value of the updatedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the value of the updatedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdatedAt(XMLGregorianCalendar value) {
        this.updatedAt = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public long getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(long value) {
        this.version = value;
    }

}
