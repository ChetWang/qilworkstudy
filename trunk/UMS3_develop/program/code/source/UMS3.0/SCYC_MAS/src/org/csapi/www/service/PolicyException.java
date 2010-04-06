
/**
 * PolicyException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.3  Built on : Aug 10, 2007 (04:45:47 LKT)
 */

package org.csapi.www.service;

public class PolicyException extends java.lang.Exception{
    
    private org.csapi.www.service.Cmcc_mas_wbsStub.PolicyException0 faultMessage;
    
    public PolicyException() {
        super("PolicyException");
    }
           
    public PolicyException(java.lang.String s) {
       super(s);
    }
    
    public PolicyException(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(org.csapi.www.service.Cmcc_mas_wbsStub.PolicyException0 msg){
       faultMessage = msg;
    }
    
    public org.csapi.www.service.Cmcc_mas_wbsStub.PolicyException0 getFaultMessage(){
       return faultMessage;
    }
}
    