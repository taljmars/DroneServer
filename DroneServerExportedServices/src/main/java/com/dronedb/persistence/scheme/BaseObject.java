
package com.dronedb.persistence.scheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="keyId" type="{http://scheme.persistence.dronedb.com/}keyId" minOccurs="0"/>
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
    "keyId"
})
@XmlSeeAlso({
    Point.class,
    ObjectDeref.class,
    Mission.class,
    Perimeter.class,
    MissionItem.class
})
public abstract class BaseObject {

    protected KeyId keyId;

    /**
     * Gets the value of the keyId property.
     * 
     * @return
     *     possible object is
     *     {@link KeyId }
     *     
     */
    public KeyId getKeyId() {
        return keyId;
    }

    /**
     * Sets the value of the keyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyId }
     *     
     */
    public void setKeyId(KeyId value) {
        this.keyId = value;
    }

}
