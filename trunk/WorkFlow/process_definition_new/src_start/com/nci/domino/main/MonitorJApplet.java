package com.nci.domino.main;

import java.awt.BorderLayout;
import java.lang.reflect.Method;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/**
 * ��ص�applet
 * 
 * @author Qil.Wong
 * 
 */
public class MonitorJApplet extends JApplet {

	private JComponent board;

	private JScrollPane scroll;

	private String servletPath;

	private String processID;

	public void init() {
		getContentPane().setLayout(new BorderLayout());
		scroll = new JScrollPane();
		getContentPane().add(scroll, BorderLayout.CENTER);
		getAppletParams();
	}

	/**
	 * ��ȡҳ�����
	 */
	private void getAppletParams() {
		servletPath = getParameter("servletpath");
		System.out.println("servletpath��" + servletPath);
		processID = getParameter("processid");
		System.out.println("processid��" + processID);
		// if (processID != null) {
		// openProcess(processID);
		// }

		// test
		servletPath = "http://localhost:8084/workflow/wofo";
		openProcess("A7EF8BD8-E632-D582-3FE5-02881FFC11EB");
	}

	public String getJavaVersion() {
		String v = System.getProperty("java.class.version");
		int javaVersion = (int) Double.parseDouble(v);
		String version = "1.";
		int classVersion1_0 = 44;// 1.0�汾java��class�ļ��汾
		String ext = String.valueOf(javaVersion - classVersion1_0);
		return version + ext;
	}

	/**
	 * ��ָ�������̣����м��
	 * 
	 * @param processID
	 */
	public void openProcess(String processID) {
		if (board != null) {
			board.getParent().remove(board);
		}
		try {
			board = (JComponent) Class
					.forName("com.nci.domino.PaintBoardBasic")
					.getConstructors()[0].newInstance(null);
			scroll.setViewportView(board);
			board.putClientProperty("WOFO_SERVLETPATH", servletPath);
			Method m = board.getClass().getMethod("openRemoteProcess",
					new Class[] { String.class });
			m.invoke(board, new Object[] { processID });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
