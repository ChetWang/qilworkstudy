
package com.nci.ums.v3.service.impl;

import javax.jws.WebService;
import javax.xml.bind.JAXBElement;

@WebService(serviceName = "UMS3_MsgSend", targetNamespace = "http://impl.service.v3.ums.nci.com", endpointInterface = "com.nci.ums.v3.service.impl.UMS3_MsgSendPortType")
public class UMS3_MsgSendImpl
    implements UMS3_MsgSendPortType
{


    public JAXBElement<String> sendWithAck(JAXBElement<String> appID, JAXBElement<String> password, JAXBElement<String> basicMsgsXML) {
        throw new UnsupportedOperationException();
    }

    public void sendWithoutAck(JAXBElement<String> appID, JAXBElement<String> password, JAXBElement<String> basicMsgsXML) {
        throw new UnsupportedOperationException();
    }

}
