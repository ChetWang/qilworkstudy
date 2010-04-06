package com.nci.ums.periphery.core;

import java.io.*;
import java.util.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.parser.*;
import com.nci.ums.periphery.exception.*;

/**
 * ���ݽ��״��룬�����ʵ��Ľ�����
 */
public class ParserController {
	// private PrintWriter out; // ������Ϣ���ͻ�
	private String ip;
	DataOutputStream out;
	private Map parserMap;
	private Vector nns = new Vector(); // ����Ҫռ����ˮ�ŵĽ���

	public ParserController(DataOutputStream out, Map parserMap) {
		nns.addElement("1003");
		nns.addElement("1001");
		nns.addElement("1005");
		nns.addElement("1004");

		this.out = out;
		this.parserMap = parserMap;
	}

	public void parse(byte[] parserString, int len) {
		// ����С��4
		if (len < 4) {
			writeBack(Res.getMessage("1011", Res.getStringFromCode("1011", "")));
			return;
		}
		// �õ�������
		byte[] temp = new byte[812];
		for (int i = 0; i < 4; i++)
			temp[i] = parserString[i];
		temp[4] = 0x0;
		String tradeCode = "";
		Object parserClass = null;

		try {
			tradeCode = new String(temp, 0, 4, "GB2312");
			parserClass = parserMap.get(tradeCode);
			Res.log(Res.DEBUG, "parser String:" + tradeCode);
		} catch (Exception e) {
			Res.log(Res.DEBUG, "tradeCode tran error!");
		}

		if (parserClass == null) {
			writeBack(Res.getMessage("1011", Res.getStringFromCode("1011",
					tradeCode)));
			return;
		}

		try {
			Parser parser = (Parser) createObject((String) parserClass);
			// �õ�������ˮ��
			String serial = "99999999";

			// ����1001.1003,1005,1004��ռ����ˮ���
			if (!nns.contains(tradeCode)) {
				try {
					serial = SerialNO.getInstance().getSerial();
				} catch (Exception ex) {
					writeBack("0001���ݿ��������æ�����Ժ�������...");
				}
			}

			// 1003����Խ�������������,ֱ�ӷ���
			if (tradeCode.equalsIgnoreCase("1003")) {
				writeBack(Res.getMessage("0000", "0000"));
			} else if (tradeCode.equalsIgnoreCase("1005")) {

				int i = 0;
				for (i = 4; i < len; i++)
					temp[i - 4] = parserString[i];
				temp[i - 4] = 0;
				String ret = parser.parser(temp, serial);
				// 1005�������κ���Ϣ
				// writeBack(Res.getMessage(ret));
			} else {
				if (CacheDataHelper.getIpLogin(ip) == null
						&& (!tradeCode.equalsIgnoreCase("1001"))) {
					writeBack(Res.getMessage("1041"));
				} else {

					int i = 0;
					for (i = 4; i < len; i++)
						temp[i - 4] = parserString[i];
					temp[i - 4] = 0;
					String ret = parser.parser(temp, serial);
					if (tradeCode.equalsIgnoreCase("1001")
							&& ret.equalsIgnoreCase("0000")) {
						CacheDataHelper.setIpLogin(ip);
					}
					writeBack(Res.getMessage(ret));
				}
			}

		} catch (ParserException ex) {
			Res.log(Res.DEBUG, "parser controller exception:" + ex);
			writeExceptionBack(ex);
		}
	}

	/**
	 * Tell client application error information
	 */
	private void writeExceptionBack(ParserException pe) {
		Res.log(Res.DEBUG, "Parser error" + ":"
				+ Res.getMessage(pe.getMessage()));
		String message = pe.getMessage();
		writeBack(Res.getMessage(message));
	}

	private void writeBack(String msg) {
		Res.log(Res.DEBUG, "Parser Success :" + msg + ":" + msg.length());
		try {
			out.write(msg.getBytes("gb2312"));
			out.flush();
		} catch (Exception e) {
		}
	}

	/**
	 * create a object used class name
	 */
	private Object createObject(String className) throws ParserException {
		Object object = null;
		try {
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			throw new ParserException("1012", Res.getStringFromCode("1012"));
		} catch (IllegalAccessException e) {
			throw new ParserException("1012", Res.getStringFromCode("1012"));
		} catch (ClassNotFoundException e) {
			throw new ParserException("1012", Res.getStringFromCode("1012"));
		}
		return object;
	}

	/**
	 * @return Returns the ip.
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            The ip to set.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
}