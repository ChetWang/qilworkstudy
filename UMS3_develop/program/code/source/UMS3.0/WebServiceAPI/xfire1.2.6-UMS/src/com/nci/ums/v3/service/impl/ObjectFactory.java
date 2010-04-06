
package com.nci.ums.v3.service.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.nci.ums.v3.service.impl package. 
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

    private final static QName _SendWithoutAckBasicMsgsXML_QNAME = new QName("http://impl.service.v3.ums.nci.com", "basicMsgsXML");
    private final static QName _SendWithoutAckPassword_QNAME = new QName("http://impl.service.v3.ums.nci.com", "password");
    private final static QName _SendWithoutAckAppID_QNAME = new QName("http://impl.service.v3.ums.nci.com", "appID");
    private final static QName _SendWithAckResponseReturn_QNAME = new QName("http://impl.service.v3.ums.nci.com", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.nci.ums.v3.service.impl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendWithoutAck }
     * 
     */
    public SendWithoutAck createSendWithoutAck() {
        return new SendWithoutAck();
    }

    /**
     * Create an instance of {@link SendWithAck }
     * 
     */
    public SendWithAck createSendWithAck() {
        return new SendWithAck();
    }

    /**
     * Create an instance of {@link SendWithAckResponse }
     * 
     */
    public SendWithAckResponse createSendWithAckResponse() {
        return new SendWithAckResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "basicMsgsXML", scope = SendWithoutAck.class)
    public JAXBElement<String> createSendWithoutAckBasicMsgsXML(String value) {
        return new JAXBElement<String>(_SendWithoutAckBasicMsgsXML_QNAME, String.class, SendWithoutAck.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "password", scope = SendWithoutAck.class)
    public JAXBElement<String> createSendWithoutAckPassword(String value) {
        return new JAXBElement<String>(_SendWithoutAckPassword_QNAME, String.class, SendWithoutAck.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "appID", scope = SendWithoutAck.class)
    public JAXBElement<String> createSendWithoutAckAppID(String value) {
        return new JAXBElement<String>(_SendWithoutAckAppID_QNAME, String.class, SendWithoutAck.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "basicMsgsXML", scope = SendWithAck.class)
    public JAXBElement<String> createSendWithAckBasicMsgsXML(String value) {
        return new JAXBElement<String>(_SendWithoutAckBasicMsgsXML_QNAME, String.class, SendWithAck.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "password", scope = SendWithAck.class)
    public JAXBElement<String> createSendWithAckPassword(String value) {
        return new JAXBElement<String>(_SendWithoutAckPassword_QNAME, String.class, SendWithAck.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "appID", scope = SendWithAck.class)
    public JAXBElement<String> createSendWithAckAppID(String value) {
        return new JAXBElement<String>(_SendWithoutAckAppID_QNAME, String.class, SendWithAck.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.v3.ums.nci.com", name = "return", scope = SendWithAckResponse.class)
    public JAXBElement<String> createSendWithAckResponseReturn(String value) {
        return new JAXBElement<String>(_SendWithAckResponseReturn_QNAME, String.class, SendWithAckResponse.class, value);
    }

}
