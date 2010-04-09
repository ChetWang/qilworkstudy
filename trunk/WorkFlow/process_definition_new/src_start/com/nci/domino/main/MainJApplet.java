package com.nci.domino.main;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * Applet 主入口程序
 * 
 * @author Qil.Wong
 */
public class MainJApplet extends JApplet {

	JPanel editor = null;

	public void init() {
		try {
			editor = (JPanel) Class.forName("com.nci.domino.WfEditor")
					.getConstructors()[0].newInstance(null);
			editor.putClientProperty("appletOrFrame", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(editor, BorderLayout.CENTER);
		getAppletParams();
		invokeEditorInit();
	}

	public String getJavaVersion() {
		String v = System.getProperty("java.class.version");
		int javaVersion = (int) Double.parseDouble(v);
		String version = "1.";
		int classVersion1_0 = 44;// 1.0版本java的class文件版本
		String ext = String.valueOf(javaVersion - classVersion1_0);
		return version + ext;
	}

	private void fireEditorPackListeners() {
		try {
			editor.getClass().getMethod("fireEditorPackListeners", null)
					.invoke(editor, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 触发Editor初始化，这个初始化必须要在applet的参数设置完才能正常进行
	 */
	private void invokeEditorInit() {
		try {
			editor.getClass().getMethod("init", null).invoke(editor, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		fireEditorPackListeners();
	}

	private void getAppletParams() {
		if (!loadFromPage()) {
			Properties p = new Properties();
			try {
				InputStream is = getClass().getResourceAsStream("debug.debug");
				p.load(is);
				is.close();
				p.load(getClass().getResourceAsStream("debug.debug"));
				editor.putClientProperty("WOFO_SERVLETPATH", p
						.getProperty("SERVLET_PATH"));

				String userid = p.getProperty("userId");
				userid = (userid.equals("")) ? "NoName" : userid;
				editor.putClientProperty("WOFO_USERID", userid);
			} catch (IOException ex) {
				System.out.println(ex.getClass().getName() + ":"
						+ ex.getMessage());
			}
		}
	}

	private boolean loadFromPage() {
		boolean flag = true;
		try {
			String servletpath = getParameter("servletpath");
			editor.putClientProperty("WOFO_SERVLETPATH", servletpath);
			String userid = getParameter("userid");
			userid = (userid == null) ? "NoName" : userid;
			editor.putClientProperty("WOFO_USERID", userid);
			if (servletpath == null || servletpath.trim().equals("")) {
				flag = false;
			}
			String actType = getParameter("actType");
			editor.putClientProperty("WFACTIVITY_TYPE", actType);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		if (!flag) {
			System.err.println("从页面获取配置信息失败，将以文件debug.debug为配置信息");
		}
		return flag;
	}
}
