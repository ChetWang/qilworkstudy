package com.nci.ums.util;

import com.nci.ums.client.UMSWebService;

/**
 * 兼容2.0接口的发送类
 * 
 * @author Qil.Wong
 * 
 */
public class Ums {

	public synchronized static int sendMessage(String content, String tel) {
		int ret = -1;
		UMSWebService service = UMSWebService.getInstance();
		String result = service.sendMessage(tel.trim(), "",content.trim());
		System.out.println("发送结果：" + result);
		if (result.equals("WSV_SUCCESS")) {
			ret = 0; // 成功
		} else if (result.equals("WSV_PSW_ERR") || result.equals("WSV_NO_APP")
				|| result.equals("WSV_NO_LOGIN")
				|| result.equals("WSV_APP_DISA")) {
			ret = 1;// 登陆失败
		} else if (result.equals("WSV_ILLEGAL")) {
			ret = 2;
		} else {
			ret = 3;
		}
		return ret;
	}
	
	public static void main(String[] x){
		Ums.sendMessage(x[0], x[1]);
	}

}
