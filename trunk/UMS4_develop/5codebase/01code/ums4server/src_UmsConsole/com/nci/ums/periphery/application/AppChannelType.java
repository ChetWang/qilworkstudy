package com.nci.ums.periphery.application;

import java.util.*;

public final class AppChannelType {
	private int value;

	private AppChannelType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return (String) stringMap.get(this);
	}

	public static String typeToString(AppChannelType type) {
		return (String) stringMap.get(type);
	}

	public static AppChannelType stringToType(String type) {
		Iterator keys = stringMap.keySet().iterator();
		while (keys.hasNext()) {
			AppChannelType key = (AppChannelType) keys.next();
			String stringValue = (String) stringMap.get(key);
			if (stringValue.equals(type)) {
				return key;
			}
		}
		return null;
	}

	public static String getType(int type) {
		String result = "";
		if (type == AppChannelType.EMAIL_TYPE) {
			result = AppChannelType.typeToString(AppChannelType.EMAIL);
		} else if (type == AppChannelType.API_TYPE) {
			result = AppChannelType.typeToString(AppChannelType.API);
		} else if (type == AppChannelType.SOCKET_TYPE) {
			result = AppChannelType.typeToString(AppChannelType.SOCKET);
		}
		return result;
	}

	public static final int EMAIL_TYPE = 0;
	public static final int API_TYPE = 1;
	public static final int SOCKET_TYPE = 2;

	public static final AppChannelType EMAIL = new AppChannelType(EMAIL_TYPE);
	public static final AppChannelType API = new AppChannelType(API_TYPE);
	public static final AppChannelType SOCKET = new AppChannelType(SOCKET_TYPE);

	private static HashMap stringMap = new HashMap();

	static {
		stringMap.put(EMAIL, "Email");
		stringMap.put(API, "Api");
		stringMap.put(SOCKET, "±¨ÎÄ");
	}

}
