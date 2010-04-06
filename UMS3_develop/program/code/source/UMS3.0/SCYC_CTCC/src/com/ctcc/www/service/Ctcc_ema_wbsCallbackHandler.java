/**
 * Ctcc_ema_wbsCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.3  Built on : Aug 10, 2007 (04:45:47 LKT)
 */
package com.ctcc.www.service;


/**
 *  Ctcc_ema_wbsCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class Ctcc_ema_wbsCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public Ctcc_ema_wbsCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public Ctcc_ema_wbsCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for sendSms method
     * override this method for handling normal response from sendSms operation
     */
    public void receiveResultsendSms(
        com.ctcc.www.service.Ctcc_ema_wbsStub.SendSmsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendSms operation
     */
    public void receiveErrorsendSms(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for notifySmsDeliveryStatus method
     * override this method for handling normal response from notifySmsDeliveryStatus operation
     */
    public void receiveResultnotifySmsDeliveryStatus() {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from notifySmsDeliveryStatus operation
     */
    public void receiveErrornotifySmsDeliveryStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for GetReceivedSms method
     * override this method for handling normal response from GetReceivedSms operation
     */
    public void receiveResultGetReceivedSms(
        com.ctcc.www.service.Ctcc_ema_wbsStub.GetReceivedSmsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from GetReceivedSms operation
     */
    public void receiveErrorGetReceivedSms(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for GetSmsDeliveryStatus method
     * override this method for handling normal response from GetSmsDeliveryStatus operation
     */
    public void receiveResultGetSmsDeliveryStatus(
        com.ctcc.www.service.Ctcc_ema_wbsStub.GetSmsDeliveryStatusResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from GetSmsDeliveryStatus operation
     */
    public void receiveErrorGetSmsDeliveryStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for notifySmsReception method
     * override this method for handling normal response from notifySmsReception operation
     */
    public void receiveResultnotifySmsReception() {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from notifySmsReception operation
     */
    public void receiveErrornotifySmsReception(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendMessage method
     * override this method for handling normal response from sendMessage operation
     */
    public void receiveResultsendMessage(
        com.ctcc.www.service.Ctcc_ema_wbsStub.SendMessageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendMessage operation
     */
    public void receiveErrorsendMessage(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getMessageDeliveryStatus method
     * override this method for handling normal response from getMessageDeliveryStatus operation
     */
    public void receiveResultgetMessageDeliveryStatus(
        com.ctcc.www.service.Ctcc_ema_wbsStub.GetMessageDeliveryStatusResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getMessageDeliveryStatus operation
     */
    public void receiveErrorgetMessageDeliveryStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getReceivedMessages method
     * override this method for handling normal response from getReceivedMessages operation
     */
    public void receiveResultgetReceivedMessages(
        com.ctcc.www.service.Ctcc_ema_wbsStub.GetReceivedMessagesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getReceivedMessages operation
     */
    public void receiveErrorgetReceivedMessages(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getMessage method
     * override this method for handling normal response from getMessage operation
     */
    public void receiveResultgetMessage(
        com.ctcc.www.service.Ctcc_ema_wbsStub.GetMessageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getMessage operation
     */
    public void receiveErrorgetMessage(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for notifyMessageReception method
     * override this method for handling normal response from notifyMessageReception operation
     */
    public void receiveResultnotifyMessageReception() {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from notifyMessageReception operation
     */
    public void receiveErrornotifyMessageReception(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for notifyMessageDeliveryReceipt method
     * override this method for handling normal response from notifyMessageDeliveryReceipt operation
     */
    public void receiveResultnotifyMessageDeliveryReceipt() {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from notifyMessageDeliveryReceipt operation
     */
    public void receiveErrornotifyMessageDeliveryReceipt(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendPush method
     * override this method for handling normal response from sendPush operation
     */
    public void receiveResultsendPush(
        com.ctcc.www.service.Ctcc_ema_wbsStub.SendPushResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendPush operation
     */
    public void receiveErrorsendPush(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getPushDeliveryStatus method
     * override this method for handling normal response from getPushDeliveryStatus operation
     */
    public void receiveResultgetPushDeliveryStatus(
        com.ctcc.www.service.Ctcc_ema_wbsStub.GetPushDeliveryStatusResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getPushDeliveryStatus operation
     */
    public void receiveErrorgetPushDeliveryStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for notifyPushDeliveryReceipt method
     * override this method for handling normal response from notifyPushDeliveryReceipt operation
     */
    public void receiveResultnotifyPushDeliveryReceipt() {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from notifyPushDeliveryReceipt operation
     */
    public void receiveErrornotifyPushDeliveryReceipt(java.lang.Exception e) {
    }
}
