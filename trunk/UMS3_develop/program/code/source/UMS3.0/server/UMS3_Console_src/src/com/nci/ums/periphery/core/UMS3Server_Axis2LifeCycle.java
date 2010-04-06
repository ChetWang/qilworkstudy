package com.nci.ums.periphery.core;

import com.nci.ums.util.Res;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.ServiceLifeCycle;

public class UMS3Server_Axis2LifeCycle implements ServiceLifeCycle {

	public void shutDown(ConfigurationContext arg0, AxisService arg1) {		
		UMSServer_V3 server = UMSServer_V3.getInstance();
		if (server != null && UMSServer_V3.isDbFlag() && UMSServer_V3.isSocketFlag()) {
			Res.log(Res.INFO, "UMS Server收到退出信息，准备关闭服务...");
			server.pleaseStop();
			server=null;
			Res.log(Res.INFO, "UMS Server已关闭");			
		}
	}

	public void startUp(ConfigurationContext arg0, AxisService arg1) {
		UMSServer_V3 server = UMSServer_V3.getInstance();
		server.start();
	}
}
