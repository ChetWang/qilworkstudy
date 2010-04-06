package com.nci.ums.desktop.axis2;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisServer;

import com.nci.ums.util.Res;

public class UMSAxisServer_V3 extends AxisServer {
	private static String appBinDir = System.getProperty("user.dir");

	public UMSAxisServer_V3(String repoLocation, String confLocation)
			throws AxisFault {
		super(false);
		configContext = ConfigurationContextFactory
				.createConfigurationContextFromFileSystem(repoLocation,
						confLocation);
	}

	public void start() throws AxisFault {
		Res.log(Res.INFO, "启动Axis2Server");
		super.start();
		Res.log(Res.INFO, "启动附加线程");
		File ext_dir = new File(appBinDir + "/lib/ext");
		File[] jars = ext_dir.listFiles();
		if (jars != null) {
			for (int i = 0; i < jars.length; i++) {
				if (jars[i].getName().lastIndexOf(".jar") >= 0) {
					JarFile currentJar;
					try {
						currentJar = new JarFile(jars[i]);
						JarEntry dbEntry = currentJar
								.getJarEntry("ExtLoad.ums");// 允许外部定制自启动线程
						InputStream in = currentJar.getInputStream(dbEntry);
						Properties p = new Properties();
						p.load(in);
						in.close();
						String className = p.getProperty("loadThread");
						if (className != null && !className.trim().equals("")) {
							Class c = Class.forName(className);
							Thread t = (Thread) c.getConstructors()[0]
									.newInstance(null);
							t.start();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

}
