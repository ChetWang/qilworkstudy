
package com.nci.ums.v3.service.impl;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class UMS3_MsgSendClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public UMS3_MsgSendClient() {
        create0();
        Endpoint UMS3_MsgSendPortTypeLocalEndpointEP = service0 .addEndpoint(new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendPortTypeLocalEndpoint"), new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendPortTypeLocalBinding"), "xfire.local://UMS3_MsgSend");
        endpoints.put(new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendPortTypeLocalEndpoint"), UMS3_MsgSendPortTypeLocalEndpointEP);
        Endpoint UMS3_MsgSendSOAP11port_httpEP = service0 .addEndpoint(new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendSOAP11port_http"), new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendSOAP11Binding"), "http://127.0.0.1:19829/axis2/services/UMS3_MsgSend");
        endpoints.put(new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendSOAP11port_http"), UMS3_MsgSendSOAP11port_httpEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((com.nci.ums.v3.service.impl.UMS3_MsgSendPortType.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendSOAP11Binding"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendPortTypeLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public UMS3_MsgSendPortType getUMS3_MsgSendPortTypeLocalEndpoint() {
        return ((UMS3_MsgSendPortType)(this).getEndpoint(new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendPortTypeLocalEndpoint")));
    }

    public UMS3_MsgSendPortType getUMS3_MsgSendPortTypeLocalEndpoint(String url) {
        UMS3_MsgSendPortType var = getUMS3_MsgSendPortTypeLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public UMS3_MsgSendPortType getUMS3_MsgSendSOAP11port_http() {
        return ((UMS3_MsgSendPortType)(this).getEndpoint(new QName("http://impl.service.v3.ums.nci.com", "UMS3_MsgSendSOAP11port_http")));
    }

    public UMS3_MsgSendPortType getUMS3_MsgSendSOAP11port_http(String url) {
        UMS3_MsgSendPortType var = getUMS3_MsgSendSOAP11port_http();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
