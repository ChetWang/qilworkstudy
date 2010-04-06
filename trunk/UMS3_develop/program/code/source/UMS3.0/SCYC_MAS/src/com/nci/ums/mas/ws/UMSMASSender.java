package com.nci.ums.mas.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.URI;
import org.csapi.www.service.Cmcc_mas_wbsStub;
import org.csapi.www.service.Cmcc_mas_wbsStub.SendMethodType;
import org.csapi.www.service.Cmcc_mas_wbsStub.SendSmsRequest;
import org.csapi.www.service.Cmcc_mas_wbsStub.SendSmsResponse;

import com.nci.ums.util.Res;
import com.nci.ums.util.ServletTemp;

/**
 * Mas消息发送对象
 * @author Qil.Wong
 *
 */
public class UMSMASSender {

	/**
	 * Mas webservice的存根
	 */
	Cmcc_mas_wbsStub stub = null;
	/**
	 * Mas webservice请求对象
	 */
	SendSmsRequest sendSmsRequest = null;
	Properties masConfig = new Properties();

	public UMSMASSender() {
		try {
			InputStream in = new ServletTemp()
					.getInputStreamFromFile("/com/nci/ums/mas/ws/mas.props");
			masConfig.load(in);
			in.close();
			stub = new Cmcc_mas_wbsStub(masConfig.getProperty("endPoint"));
		} catch (AxisFault e) {
			Res.log(Res.ERROR, "初始化Mas Webservice Stub失败！");
			Res.logExceptionTrace(e);
		} catch (IOException e) {
			Res.log(Res.ERROR, "初始化Mas Webservice 配置失败！");
			Res.logExceptionTrace(e);
		}
		sendSmsRequest = new SendSmsRequest();
	}

	public synchronized String sendToMas(String mobile, String content,
			String extCode) {
		try {
			sendSmsRequest.setApplicationID(masConfig
					.getProperty("applicationID"));
			sendSmsRequest.setDeliveryResultRequest(true);

			sendSmsRequest.setDestinationAddresses(new URI[] { new URI(mobile
					.indexOf("tel") == 0 ? mobile : "tel:" + mobile) });// 13408615876
			sendSmsRequest.setExtendCode(extCode);
			sendSmsRequest.setMessage(content);
			sendSmsRequest
					.setMessageFormat(Cmcc_mas_wbsStub.MessageFormat.GB2312);
			sendSmsRequest.setSendMethod(SendMethodType.Long);
			SendSmsResponse resp = stub.sendSms(sendSmsRequest);
			return resp.getRequestIdentifier();
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		}
		return "MAS ERROR";
	}

	public static void main(String[] xx) {
		new UMSMASSender();
	}

}
