package com.nci.domino.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * �������в����࣬������뱣֤��Applet������ͬ�������������������κ�����
 * 
 * @author Qil.Wong
 */
public class MainJFrame extends JFrame {

	JPanel editor;

	public MainJFrame() {
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		try {
			editor = (JPanel) Class.forName("com.nci.domino.WfEditor")
					.getConstructors()[0].newInstance(null);
			editor.putClientProperty("appletOrFrame", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getContentPane().add(editor);
		getParams();
		invokeEditorInit();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("WorkFlow");
		pack();
		fireEditorPackListeners();
	}

	/**
	 * У�鱾��java�汾
	 * 
	 * @return
	 */
	private boolean checkJava() {
		String v = System.getProperty("java.class.version");
		double java6version = 49.0;
		boolean flag = Double.valueOf(v).doubleValue() >= java6version;
		if (!flag) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			JOptionPane.showConfirmDialog(null,
					"���̶�������ҪJava6�����ϰ汾!\n����ǰJava�汾Ϊ"
							+ System.getProperty("java.runtime.version"),
					"��Ҫ���µ�Java", JOptionPane.CLOSED_OPTION,
					JOptionPane.ERROR_MESSAGE);
		}
		return flag;
	}

	/**
	 * ��������pack��ļ������¼�
	 */
	private void fireEditorPackListeners() {
		try {
			editor.getClass().getMethod("fireEditorPackListeners", null)
					.invoke(editor, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����Editor��ʼ���������ʼ������Ҫ��JFrame�Ĳ��������������������
	 */
	private void invokeEditorInit() {
		try {
			editor.getClass().getMethod("init", null).invoke(editor, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getParams() {
		Properties p = new Properties();
		try {
			InputStream is = MainJFrame.class
					.getResourceAsStream("debug.debug");
			p.load(is);
			is.close();
			editor.putClientProperty("WOFO_SERVLETPATH", p
					.getProperty("SERVLET_PATH"));

			String userid = p.getProperty("userId");
			userid = (userid.equals("")) ? "NoName" : userid;
			editor.putClientProperty("WOFO_USERID", userid);
			editor.putClientProperty("WFACTIVITY_TYPE", p
					.getProperty("actType"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] xxx) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainJFrame frame = new MainJFrame();
				if (frame.checkJava()) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.init();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setLocationByPlatform(true);
					frame.setVisible(true);
				} else {
					System.exit(0);
				}
			}
		});
	}
}
