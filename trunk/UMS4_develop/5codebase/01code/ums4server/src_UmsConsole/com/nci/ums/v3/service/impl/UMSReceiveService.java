package com.nci.ums.v3.service.impl;

import com.nci.ums.periphery.application.ReceiveCenter;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.v3.service.common.ReceiveService;

public class UMSReceiveService implements ReceiveService {
	
	public static String connectionTestSignal = "UMSCONNTEST";
	
	public static String connectionTestReturn = "CONN_SUCESS";
	
	public UMSReceiveService(){
	}

	/**
	 * 
	 * @param appID
	 * @param password
	 * @param serviceID
	 */
	public String receive(String appID, String password, String serviceID){
		if(appID.equals(connectionTestSignal)&&password.equals(connectionTestSignal)&&serviceID.equals(connectionTestSignal)){
			return connectionTestReturn;
		}
		int loginFlag = DBUtil_V3.login(appID, password);
		if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
			return DBUtil_V3.parseLoginErrResult(loginFlag);
		}
		String receivedXML = ReceiveCenter.getInMsg4Service(serviceID);
		return receivedXML;
	}

}
