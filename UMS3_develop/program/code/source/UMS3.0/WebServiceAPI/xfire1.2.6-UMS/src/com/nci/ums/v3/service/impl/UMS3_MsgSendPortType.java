
package com.nci.ums.v3.service.impl;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBElement;

@WebService(name = "UMS3_MsgSendPortType", targetNamespace = "http://impl.service.v3.ums.nci.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface UMS3_MsgSendPortType {


    @WebMethod(operationName = "sendWithAck", action = "urn:sendWithAck")
    @WebResult(name = "return", targetNamespace = "http://impl.service.v3.ums.nci.com")
    public JAXBElement<String> sendWithAck(
        @WebParam(name = "appID", targetNamespace = "http://impl.service.v3.ums.nci.com")
        JAXBElement<String> appID,
        @WebParam(name = "password", targetNamespace = "http://impl.service.v3.ums.nci.com")
        JAXBElement<String> password,
        @WebParam(name = "basicMsgsXML", targetNamespace = "http://impl.service.v3.ums.nci.com")
        JAXBElement<String> basicMsgsXML);

    @WebMethod(operationName = "sendWithoutAck", action = "urn:sendWithoutAck")
    @Oneway
    public void sendWithoutAck(
        @WebParam(name = "appID", targetNamespace = "http://impl.service.v3.ums.nci.com")
        JAXBElement<String> appID,
        @WebParam(name = "password", targetNamespace = "http://impl.service.v3.ums.nci.com")
        JAXBElement<String> password,
        @WebParam(name = "basicMsgsXML", targetNamespace = "http://impl.service.v3.ums.nci.com")
        JAXBElement<String> basicMsgsXML);

}
