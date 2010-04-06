package com.nci.ums.v3.service.impl.activemq;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.activemq.security.SimpleAuthenticationPlugin;

import com.nci.ums.periphery.application.AppInfo;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.LoginUtil_V3;

/**
 * JMS���ӵ������֤
 * @author Qil.Wong
 *
 */
public class UMSJMSAuthorization extends SimpleAuthenticationPlugin {
	
	private static UMSJMSAuthorization auth = new UMSJMSAuthorization();

	public static UMSJMSAuthorization getInstance(){
		return auth;
	}
	
	private UMSJMSAuthorization(){
		Map map = new HashMap();
		map.put("DESKTOPADMIN", "");
		//�������ݺ���ע���Ӧ��һ�£�����appID��appPassword��Ӧ
		Map apps = AppServiceUtil.getAppMap();
		Iterator it = apps.values().iterator();
		while (it.hasNext()) {
			AppInfo app = (AppInfo) it.next();
			map.put(app.getAppID(), app.getPassword());
		}
		setUserPasswords(map);
		setUserGroups(new HashMap());
	}
}
