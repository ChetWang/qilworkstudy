package com.nci.ums.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisServer;

/**
 * UMSµÄWebservice ·þÎñÆ÷
 * 
 * @author Qil.Wong
 * 
 */
public class UMSAxisServer extends AxisServer {

	static String appBinDir = System.getProperty("user.dir");
	static String repository = appBinDir + "/repository";
	static String config = appBinDir + "/conf/ums_axis2.xml";
	static UMSAxisServer wsServer = null;

	public synchronized static UMSAxisServer getInstance() {
		if (wsServer == null) {
			wsServer = new UMSAxisServer(repository, config);
		}
		return wsServer;
	}

	private UMSAxisServer(String repoLocation, String confLocation) {
		super(false);
		try {

			configContext = ConfigurationContextFactory
					.createConfigurationContextFromFileSystem(repoLocation,
							confLocation);
			start();
		} catch (AxisFault f) {
			f.printStackTrace();
			wsServer = null;
		}
	}

	public void stop() throws AxisFault {
		super.stop();
		wsServer = null;
	}

}
