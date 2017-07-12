
package com.dronedb.persistence.scheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for objectDeref complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="objectDeref">
 *   &lt;complexContent>
 *     &lt;extension base="{http://scheme.persistence.dronedb.com/}baseObject">
 *       &lt;sequence>
 *         &lt;element name="clz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "objectDeref", propOrder = {
    "clz"
})
public class ObjectDeref
    extends BaseObject
{

    protected String clz;

    /**
     * Gets the value of the clz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClz() {
        return clz;
    }

    /**
     * Sets the value of the clz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClz(String value) {
        this.clz = value;
    }

}
