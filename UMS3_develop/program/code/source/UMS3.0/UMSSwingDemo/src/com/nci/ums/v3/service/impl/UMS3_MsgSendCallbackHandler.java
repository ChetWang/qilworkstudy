
/**
 * UMS3_MsgSendCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.3  Built on : Aug 10, 2007 (04:45:47 LKT)
 */

    package com.nci.ums.v3.service.impl;

    /**
     *  UMS3_MsgSendCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class UMS3_MsgSendCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public UMS3_MsgSendCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public UMS3_MsgSendCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for sendTestAck method
            * override this method for handling normal response from sendTestAck operation
            */
           public void receiveResultsendTestAck(
                    com.nci.ums.v3.service.impl.UMS3_MsgSendStub.SendTestAckResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendTestAck operation
           */
            public void receiveErrorsendTestAck(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for sendWithAck method
            * override this method for handling normal response from sendWithAck operation
            */
           public void receiveResultsendWithAck(
                    com.nci.ums.v3.service.impl.UMS3_MsgSendStub.SendWithAckResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendWithAck operation
           */
            public void receiveErrorsendWithAck(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                


    }
    