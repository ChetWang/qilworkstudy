package com.nci.svg.applet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Policy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

public class DrawSvg extends JApplet {

	private AppletServletContext context = new AppletServletContext();
	private AppletProxy proxy = null;

	/**
	 * 初始化配置项
	 */
	private HashMap<String, Object> map_config = new HashMap<String, Object>();

	private int init_flag = 0;

	/**
	 * 正在初始化
	 */
	public static final int INIT_PROCESSING = 0;

	public static final int INIT_COMPLETE = 1;

	public static final int INIT_FAILED = -1;

	private boolean debug = true;

	@Override
	public void init() {

		try {
			iniDebugParam();
			iniParam();
			if (loadPlugins(debug)) {

				proxy = new AppletProxy(this);
				// 创建Editor对象，但不初始化
				createEditor();
				String socket_url = "";
				if (debug) {
					socket_url = "192.168.133.125:8084";
				} else {
					String approot = getParameter("approot");// http://192.168.133.125:8084/cserv
					if (approot.indexOf("http://") == 0) {
						socket_url = approot.substring(7);
						socket_url = socket_url.substring(0, socket_url
								.indexOf("/"));
					} else {
						socket_url = approot.substring(0, approot.indexOf("/"));
					}
				}
				if (!debug) {
					grantPermissions(new String[] {
							"permission java.io.FilePermission \"<<ALL FILES>>\", \"read,write,delete\";",
							"permission java.awt.AWTPermission \"listenToAllAWTEvents\";",
							"permission java.net.SocketPermission \""
									+ socket_url + "\",\"connect,resolve\";" });
					changeMemoryLimit();
				}
				createGUI();
				init_flag = INIT_COMPLETE;
			}

		} catch (Exception e) {
			init_flag = INIT_FAILED;
			e.printStackTrace();
		}
	}

	/**
	 * 下载所需的组件
	 */
	@SuppressWarnings("unchecked")
	private boolean loadPlugins(boolean debug) {
		if (debug)
			return true;
		boolean flag = true;
		boolean isUpdated = false;
		try {
			HashMap<String, String> serverVersionMap = (HashMap<String, String>) context
					.communicateWithURL((String) map_config.get("approot")
							+ (String) map_config.get("servletpath")
							+ "?action=getSvgJarsVersion", null);
			Properties localVersions = new Properties();
			File localVersionFile = new File(context.getCacheJarPath()
					+ context.getVersionFileName());
			if (localVersionFile.exists()) {
				FileInputStream localFis = new FileInputStream(localVersionFile);
				localVersions.load(localFis);
				localFis.close();
			}
			Iterator<String> itServer = serverVersionMap.keySet().iterator();
			String key = null;
			String serverVersion = null;
			String localVersion = null;
			while (itServer.hasNext()) {
				key = itServer.next();
				serverVersion = serverVersionMap.get(key);
				localVersion = localVersions.getProperty(key);
				if (localVersion == null || localVersion.equals("")
						|| !localVersion.equals(serverVersion)) {
					System.out.println("开始下载：" + key);
					context.downloadJar(map_config, key);
					isUpdated = true;
				}
				// 为防止被删掉，但版本文件中仍存在信息，需要再检查一次
				File localJarFile = new File(context.getCacheJarPath() + key);
				if (!localJarFile.exists()) {
					context.downloadJar(map_config, key);
				}
			}
			// 多余的jar要删除，否则会根据ext优先原则将原先无用的包装载进jvm
			Iterator itLocal = localVersions.keySet().iterator();
			while (itLocal.hasNext()) {
				key = (String) itLocal.next();
				if (!serverVersionMap.containsKey(key)) {
					new File(context.getCacheJarPath() + key).delete();
					isUpdated = true;
				}
			}
			// 将有用的加载到jvm
			loadLocalJarsToClassloader(serverVersionMap);
			// 将版本信息写入版本文件
			context.writeToVersionFile(serverVersionMap);
			if (isUpdated) {
				JOptionPane.showConfirmDialog(this.getContentPane(),
						"SVG 模块升级完成，重启本窗口后生效!", "提示",
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 加载第三方库
	 * 
	 * @param serverVersionMap
	 */
	public void loadLocalJarsToClassloader(HashMap<String, String> serverVersionMap) {
		File appjarDir = new File(context.getCacheJarPath());
		File[] jarFiles = appjarDir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				try {
					String extension = pathname.getName().substring(
							pathname.getName().lastIndexOf("."));
					if (extension.equalsIgnoreCase(".jar"))
						return true;
				} catch (Exception e) {
					return false;
				}
				return false;
			}

		});
		for (File jar : jarFiles) {
			if (serverVersionMap.keySet().contains(jar.getName()))
				NCIAppletClassLoader.addExtClassPath(jar);
		}
	}

	/**
	 * 更改授权文件，以便js也可以通过applet访问本地资源
	 * 
	 * @param permissions
	 */
	private void grantPermissions(String[] permissions) {
		StringBuffer sb = new StringBuffer("grant {");
		for (int i = 0; i < permissions.length; i++) {
			sb.append(permissions[i]);
		}
		sb.append("};");
		String java_home = System.getProperty("java.home");
		System.out.println("java_home:" + java_home);
		File policyFile = new File(java_home + "/lib/security/java.policy");

		try {
			BufferedReader br = new BufferedReader(new FileReader(policyFile));
			String s = null;
			boolean hasBeenWrite = false;
			while ((s = br.readLine()) != null) {
				if (s.equalsIgnoreCase(sb.toString())) {
					hasBeenWrite = true;
					break;
				}
			}
			if (!hasBeenWrite) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						policyFile.getAbsolutePath(), true));
				bw.write("\r\n" + sb.toString());
				bw.close();
				// 刷新policy文件

			}
			Policy.getPolicy().refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更改jre6u10前的jre内存限制，但不能超过128m
	 * @throws IOException
	 */
	private void changeMemoryLimit() throws IOException {
		String deployPropPath = System.getProperty("user.home")
				+ "/Application Data/Sun/Java/Deployment/deployment.properties";
		// System.out.println("deployPropPath:" + deployPropPath);
		String jre_version = System.getProperty("java.version");
		// String jre_version = "1.6.0_07";
		// System.out.println("jre_version:" + jre_version);
		String eleName = "deployment.javapi.jre." + jre_version + ".args";
		String memoryArgs = "-Xmx128m";
		Properties p = new Properties();
		p.load(new FileInputStream(new File(deployPropPath)));
		String args = p.getProperty(eleName);
		int index = -1;
		boolean changed = false;
		if (args != null)
			index = args.indexOf("-Xmx");
		if (args != null && !args.equals("") && index >= 0) {
			// 替换
			String[] argss = args.split(" ");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < argss.length; i++) {
				if (argss[i].startsWith("-Xmx") && !argss[i].equals(memoryArgs)) {
					argss[i] = memoryArgs;
					changed = true;
				}
				sb.append(argss[i]);
			}
			args = sb.toString();
		} else {
			// 新增
			if (args == null)
				args = "";
			args = args + memoryArgs;
			changed = true;
		}
		if (changed) {
			// System.out.println("更改内存" + memoryArgs);
			p.put(eleName, args);
			FileOutputStream fos = new FileOutputStream(
					new File(deployPropPath));
			p.store(fos, "SVG Channged");
		}
	}

	/**
	 * creates the GUI
	 */
	private void createGUI() {

		proxy.createAppletUI();

	}

	/**
	 * 创建编辑器
	 */
	private void createEditor() {
		proxy.createEditor(map_config);
	}

	/**
	 * 初始化html中定义的参数
	 */
	private void iniParam() {
		if (getParameter("approot") != null) {
			map_config.put("approot", getParameter("approot"));
		}
		if (getParameter("showmenu") != null) {
			map_config.put("showmenu", getParameter("showmenu").toUpperCase());
		}

		if (getParameter("showtoolbar") != null) {
			map_config.put("showtoolbar", getParameter("showtoolbar")
					.toUpperCase());
		}

		if (getParameter("showstatusbar") != null) {
			map_config.put("showstatusbar", getParameter("showstatusbar")
					.toUpperCase());
		}

		if (getParameter("mode") != null) {
			map_config.put("mode", getParameter("mode").toUpperCase());
		}

		if (getParameter("servletpath") != null) {
			map_config.put("servletpath", getParameter("servletpath"));
		}
		if (getParameter("default_new_doc_width") != null) {
			map_config.put("default_new_doc_width",
					getParameter("default_new_doc_width"));
		}
		if (getParameter("default_new_doc_height") != null) {
			map_config.put("default_new_doc_height",
					getParameter("default_new_doc_height"));
		}
		if (getParameter("webflag") != null) {
			map_config.put("webflag", getParameter("webflag").toUpperCase());
		}

		if (getParameter("codebase") != null) {
			map_config.put("codebase", getParameter("codebase").toUpperCase());
		}
	}

	private void iniDebugParam() {
		if (getParameter("debug") != null)
			debug = getParameter("debug").equalsIgnoreCase("true");
	}

	@Override
	public void destroy() {
		proxy.destroyApplet();
		super.destroy();
	}

	public void setUser(String user) {
		proxy.setUser(user);
	}

	public String getUser() {
		return proxy.getEditor().getSvgSession().getUser();
	}

	public int getInitFlag() {
		return init_flag;
	}


	public Object invoke(String methodName, Object[] params, String[] classNames) {
		Class[] paramTypes = new Class[classNames.length];
		for (int i = 0; i < classNames.length; i++) {
			try {
				paramTypes[i] = Class.forName(classNames[i]);
			} catch (ClassNotFoundException e) {
				System.out.println("无法找到类" + classNames[i]);
			}
		}
		return proxy.invoke(methodName, params, paramTypes);
	}

}
