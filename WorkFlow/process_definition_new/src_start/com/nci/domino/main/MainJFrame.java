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
 * 程序运行测试类，此类必须保证和Applet构造相同，不建议往此类新增任何内容
 * 
 * @author Qil.Wong
 */
public class MainJFrame extends JFrame {

	JPanel editor;

	public MainJFrame() {
	}

	/**
	 * 初始化
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
	 * 校验本地java版本
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
					"流程定义器需要Java6及以上版本!\n您当前Java版本为"
							+ System.getProperty("java.runtime.version"),
					"需要更新的Java", JOptionPane.CLOSED_OPTION,
					JOptionPane.ERROR_MESSAGE);
		}
		return flag;
	}

	/**
	 * 触发界面pack后的监听器事件
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
	 * 触发Editor初始化，这个初始化必须要在JFrame的参数设置完才能正常进行
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
