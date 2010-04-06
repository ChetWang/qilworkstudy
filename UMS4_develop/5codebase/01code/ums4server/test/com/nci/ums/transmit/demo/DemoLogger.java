package com.nci.ums.transmit.demo;

import com.nci.ums.transmit.common.UMSTransmitLogger;
import com.nci.ums.util.Res;

public class DemoLogger implements UMSTransmitLogger {

	public void log(int level, Object info) {
		if (info instanceof Exception) {
			Res.logExceptionTrace((Exception) info);
		} else
			Res.log(level, info.toString());

	}

}
