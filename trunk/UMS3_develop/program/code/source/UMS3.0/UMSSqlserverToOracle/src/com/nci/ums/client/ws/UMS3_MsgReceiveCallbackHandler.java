
/**
 * UMS3_MsgReceiveCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.3  Built on : Aug 10, 2007 (04:45:47 LKT)
 */

    package com.nci.ums.client.ws;

    /**
     *  UMS3_MsgReceiveCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class UMS3_MsgReceiveCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public UMS3_MsgReceiveCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public UMS3_MsgReceiveCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for receive method
            * override this method for handling normal response from receive operation
            */
           public void receiveResultreceive(
                    com.nci.ums.client.ws.UMS3_MsgReceiveStub.ReceiveResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from receive operation
           */
            public void receiveErrorreceive(java.lang.Exception e) {
            }
                


    }
    