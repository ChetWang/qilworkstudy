package nci.graph;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
	 * ��ʼ��������
	 */
	private HashMap<String, Object> map_config = new HashMap<String, Object>();
	private String strFileName = "";
	private String strxitongtu = "";
	private int init_flag = 0;
	private String strLineID = "";

	/**
	 * ���ڳ�ʼ��
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
				// ����Editor���󣬵�����ʼ��
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

			// strFileName= "1215591913701.svg";
			if (strFileName.length() > 0) {
				openSVGFile(strFileName);
			}

			if (strxitongtu.length() > 0) {
				openSVGFile("0", strxitongtu, "0");
			}

			if (strLineID.length() > 0) {
				openFileByLineID(strLineID, 1, 1);
			}

			// saveJpegFile("1.jpg");
		} catch (Exception e) {
			init_flag = INIT_FAILED;
			e.printStackTrace();
		}
	}

	/**
	 * ������������
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
					System.out.println("��ʼ���أ�" + key);
					context.downloadJar(map_config, key);
					isUpdated = true;
				}
				// Ϊ��ֹ��ɾ�������汾�ļ����Դ�����Ϣ����Ҫ�ټ��һ��
				File localJarFile = new File(context.getCacheJarPath() + key);
				if (!localJarFile.exists()) {
					context.downloadJar(map_config, key);
				}
			}
			// �����jarҪɾ������������ext����ԭ��ԭ�����õİ�װ�ؽ�jvm
			Iterator itLocal = localVersions.keySet().iterator();
			while (itLocal.hasNext()) {
				key = (String) itLocal.next();
				if (!serverVersionMap.containsKey(key)) {
					new File(context.getCacheJarPath() + key).delete();
					isUpdated = true;
				}
			}
			// �����õļ��ص�jvm
			loadLocalJars(serverVersionMap);
			// ���汾��Ϣд��汾�ļ�
			context.writeToVersionFile(serverVersionMap);
			if (isUpdated) {
				JOptionPane.showConfirmDialog(this.getContentPane(),
						"SVG ģ��������ɣ����������ں���Ч!", "��ʾ",
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
	 * ���ص�������
	 * 
	 * @param serverVersionMap
	 */
	public void loadLocalJars(HashMap<String, String> serverVersionMap) {
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
	 * ������Ȩ�ļ����Ա�jsҲ����ͨ��applet���ʱ�����Դ
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
				// ˢ��policy�ļ�

			}
			Policy.getPolicy().refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeMemoryLimit() throws IOException{
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
			// �滻
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
			// ����
			if (args == null)
				args = "";
			args = args + memoryArgs;
			changed = true;
		}
		if (changed) {
			// System.out.println("�����ڴ�" + memoryArgs);
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
	 * �����༭��
	 */
	private void createEditor() {
		proxy.createEditor(map_config);
	}

	/**
	 * ��svg�ļ������һ�����ⲿjs���õķ���
	 * 
	 * @param filename
	 */
	public void openSVGFile(String filename) {
		proxy.openSVGFile(filename);
	}

	/**
	 * ��svg�ļ������һ�����ⲿjs���õķ���
	 * 
	 * @param fileType
	 *            �ļ�����
	 * @param fileRemark�ļ�����
	 * @param voltageClass
	 *            ��ѹ�ȼ�
	 */
	public void openSVGFile(String fileType, String fileRemark,
			String voltageClass) {
		proxy.openSVGFile(fileType, fileRemark, voltageClass);
	}

	/**
	 * ��svg�ļ������һ�����ⲿjs���õķ���
	 * 
	 * @param lineID
	 *            ��·���
	 * @param fileType
	 *            �ļ�����
	 * @param fileRemark
	 *            �ļ�����
	 * @param voltageClass
	 *            ��ѹ�ȼ�
	 */
	public void openSVGFile(String lineID, String fileType, String fileRemark,
			String voltageClass) {
		proxy.openSVGFile(lineID, fileType, fileRemark, voltageClass);
	}

	/**
	 * ��ʼ��html�ж���Ĳ���
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

		if (getParameter("filename") != null) {
			strFileName = getParameter("filename");
		}

		if (getParameter("xitongtu") != null) {
			strxitongtu = getParameter("xitongtu");
		}

		if (getParameter("lineid") != null) {
			strLineID = getParameter("lineid");
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

	/**
	 * ���ݴ�����ļ������ļ�
	 * 
	 * @param strFileName
	 *            Զ���ļ���
	 */
	public void openFile(String strFileName) {
		proxy.openFile(strFileName);
	}

	/**
	 * ��ȡ��ǰhandle���ļ�����
	 * 
	 * @return
	 */
	public String getSVGContent() {
		String strContent = proxy.getSvgContent();
		return strContent;
	}

	/**
	 * ����������ļ�������ʾ
	 * 
	 * @param strContent
	 */
	public void setFileContent(String strContent) {

		proxy.setFileContent(strContent);
	}

	/**
	 * ��ȡ�ļ���
	 * 
	 * @return
	 */
	public String getFileName() {
		return strFileName;
	}

	public String updateFile() {
		try {
			String s = getSVGContent();
			String id = getFileName();// System.currentTimeMillis() + ".svg";
			if (id == null || id.equals("")) {
				id = insertFile("");
			}

			String desc = "";
			String cim = "";
			String type = "-1";
			String templet = "0";
			String code = "";
			String[][] params = new String[][] { { "id", id },
					{ "desc", desc }, { "cim", cim }, { "type", type },
					{ "content", s }, { "templet", templet }, { "code", code } };
			String url = (String) proxy.getGCParam("approot")
					+ (String) proxy.getGCParam("servletPath")
					+ "?action=saveSvgFileOld";
			String ret = getUrlRetText(url, params);

			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public String insertFile(String desc) {
		try {
			String s = getSVGContent();
			String id = System.currentTimeMillis() + ".svg";
			String cim = "";
			String type = "-1";
			String templet = "0";
			String code = "";
			String[][] params = new String[][] { { "id", id },
					{ "desc", desc }, { "cim", cim }, { "type", type },
					{ "content", s }, { "templet", templet }, { "code", code } };
			String url = (String) proxy.getGCParam("approot")
					+ (String) proxy.getGCParam("servletPath")
					+ "?action=saveAsSvgFileOld";
			String ret = getUrlRetText(url, params);

			if (ret.equals("0")) {

			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * �����ļ�
	 * 
	 * @param desc
	 * @return
	 */
	public String saveFile(String desc) {
		return insertFile(desc, "0", "");
	}

	public String insertFile(String desc, String templet, String code) {
		try {
			String s = getSVGContent();

			String id = System.currentTimeMillis() + ".svg";
			String cim = "";
			String type = "-1";

			String[][] params = new String[][] { { "id", id },
					{ "desc", desc }, { "cim", cim }, { "type", type },
					{ "content", s }, { "templet", templet }, { "code", code } };
			String url = (String) proxy.getGCParam("approot")
					+ (String) proxy.getGCParam("servletPath")
					+ "?action=saveAsSvgFileOld";
			String ret = getUrlRetText(url, params);

			if (ret.equals("0")) {

			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * ����ΪJPEG�ļ�
	 * 
	 * @param fileName
	 *            JPEG�ļ���
	 * @return String �����ɹ���������ʧ��
	 */
	public String saveJpegFile(String fileName) {
		return proxy.saveJpegFile(this, fileName);
	}

	public void writeImage(BufferedImage image, File destFile) {
		proxy.writeImage(image, destFile);
	}

	/**
	 * �õ�URL��Դ���ص������ַ���
	 * 
	 * @param url
	 *            URL��Դ��·�� �� "http://192.168.0.201/java/servlet"
	 * @param params
	 *            ��ά�ַ������飬�ṩһϵ�У���/ֵ�� String params[][]={ {"request","query"},
	 *            {"format","xml"}, {"query","select * from substation where
	 *            name like '%�̱�%'"} };
	 * @return ���ص������ַ���
	 */
	public static String getUrlRetText(String url, String[][] params) {
		try {
			String rs = "";
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if (i != 0)
						rs += "&";
					rs += URLEncoder.encode(params[i][0], "UTF-8") + '='
							+ URLEncoder.encode(params[i][1], "UTF-8");
				}
			}
			StringBuffer bf = new StringBuffer();

			URL ur = new URL(url);
			URLConnection rConn = ur.openConnection();
			rConn.setDoOutput(true);
			rConn.setDoInput(true);
			PrintStream output = new PrintStream(rConn.getOutputStream());
			output.print(rs);
			output.flush();
			DataInputStream input = new DataInputStream(rConn.getInputStream());
			int b;
			while ((b = input.read()) != -1) {
				bf.append((char) b);
			}
			return new String(bf.toString().getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			System.out.print(e.toString());
			return null;
		}
	}

	public String getByteHexStr(byte[] bs, int start, int count) {
		int n;
		StringBuffer buf = new StringBuffer();
		for (int i = start; i < start + count; i++) {
			n = bs[i] & 0xFF;
			buf.append(n >= 16 ? Integer.toHexString(n) : "0"
					+ Integer.toHexString(n));
		}
		return buf.toString();
	}

	public int getInitFlag() {
		return init_flag;
	}

	public int setSymbolTypeByID(String id, String type) {
		return proxy.setSymbolTypeByID(id, type);
	}

	public int setSymbolTypeByObjectID(String id, String type) {
		return proxy.setSymbolTypeByObjectID(id, type);
	}

	public String getSelectedDeviceID() {
		return proxy.getSelectedDeviceID();
	}

	public int setSymbolWink(String id, boolean wink) {
		int result = -1;
		setSymbolTypeByID(id, "");

		return setWink(wink, 1);
	}

	public int setWink(boolean wink, int seccend) {
		return proxy.setWink(wink, seccend);
	}

	/**
	 * ���������sap�������Ӧ�ĳ�վͼ
	 * 
	 * @param strSapCode
	 *            PSMS��ţ����ţ�
	 */
	public void openFileByCode(String strSapCode) {
		// �ӹ���λ������ļ�
		proxy.openFileByCode(strSapCode);

	}

	/**
	 * �������Ŷα�Ŵ�SVGͼ
	 * 
	 * @param strLineID
	 *            PSMS�����Ŷ˵ı�ţ��̺ţ�
	 * @param fileType
	 *            ���ļ����ͣ�1��ʾվ��ͼ��2��ʾ����ͼ
	 * @param type:���type=1ʱ����һ�ν���ͼ����˸�������豸
	 */
	public void openFileByLineID(String strLineID, int fileType, int type) {
		proxy.openFileByLineID(strLineID, fileType, type);
		return;
	}

	/**
	 * ����misID��SVGͼ
	 * 
	 * @param misID
	 *            String mis���
	 */
	public void openFileByMisID(String misID) {
		proxy.openFileByMisID(misID);
		return;
	}

	public Object invoke(String methodName, Object[] params, String[] classNames) {
		Class[] paramTypes = new Class[classNames.length];
		for (int i = 0; i < classNames.length; i++) {
			try {
				paramTypes[i] = Class.forName(classNames[i]);
			} catch (ClassNotFoundException e) {
				System.out.println("�޷��ҵ���" + classNames[i]);
			}
		}
		return proxy.invoke(methodName, params, paramTypes);
	}

}
