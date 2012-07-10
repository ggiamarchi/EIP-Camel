//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.10 at 03:32:56 PM CEST 
//


package com.zenika.camel.notifier.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.zenika.com/camel/notifier/model}admin" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.zenika.com/camel/notifier/model}user" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "admin",
    "user"
})
@XmlRootElement(name = "notifications")
public class Notifications
    implements Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected List<Admin> admin;
    @XmlElement(required = true)
    protected List<User> user;

    /**
     * Gets the value of the admin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the admin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdmin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Admin }
     * 
     * 
     */
    public List<Admin> getAdmin() {
        if (admin == null) {
            admin = new ArrayList<Admin>();
        }
        return this.admin;
    }

    /**
     * Gets the value of the user property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the user property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link User }
     * 
     * 
     */
    public List<User> getUser() {
        if (user == null) {
            user = new ArrayList<User>();
        }
        return this.user;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            List<Admin> theAdmin;
            theAdmin = (((this.admin!= null)&&(!this.admin.isEmpty()))?this.getAdmin():null);
            strategy.appendField(locator, this, "admin", buffer, theAdmin);
        }
        {
            List<User> theUser;
            theUser = (((this.user!= null)&&(!this.user.isEmpty()))?this.getUser():null);
            strategy.appendField(locator, this, "user", buffer, theUser);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Notifications)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Notifications that = ((Notifications) object);
        {
            List<Admin> lhsAdmin;
            lhsAdmin = (((this.admin!= null)&&(!this.admin.isEmpty()))?this.getAdmin():null);
            List<Admin> rhsAdmin;
            rhsAdmin = (((that.admin!= null)&&(!that.admin.isEmpty()))?that.getAdmin():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "admin", lhsAdmin), LocatorUtils.property(thatLocator, "admin", rhsAdmin), lhsAdmin, rhsAdmin)) {
                return false;
            }
        }
        {
            List<User> lhsUser;
            lhsUser = (((this.user!= null)&&(!this.user.isEmpty()))?this.getUser():null);
            List<User> rhsUser;
            rhsUser = (((that.user!= null)&&(!that.user.isEmpty()))?that.getUser():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "user", lhsUser), LocatorUtils.property(thatLocator, "user", rhsUser), lhsUser, rhsUser)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<Admin> theAdmin;
            theAdmin = (((this.admin!= null)&&(!this.admin.isEmpty()))?this.getAdmin():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "admin", theAdmin), currentHashCode, theAdmin);
        }
        {
            List<User> theUser;
            theUser = (((this.user!= null)&&(!this.user.isEmpty()))?this.getUser():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "user", theUser), currentHashCode, theUser);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}