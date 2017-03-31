
package com.dronedb.persistence.scheme;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryResponseRemote complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryResponseRemote">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultListBase" type="{http://scheme.persistence.dronedb.com/}baseObject" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryResponseRemote", propOrder = {
    "resultListBase"
})
public class QueryResponseRemote {

    @XmlElement(required = true)
    protected List<BaseObject> resultListBase;

    /**
     * Gets the value of the resultListBase property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultListBase property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultListBase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BaseObject }
     * 
     * 
     */
    public List<BaseObject> getResultListBase() {
        if (resultListBase == null) {
            resultListBase = new ArrayList<BaseObject>();
        }
        return this.resultListBase;
    }

}
