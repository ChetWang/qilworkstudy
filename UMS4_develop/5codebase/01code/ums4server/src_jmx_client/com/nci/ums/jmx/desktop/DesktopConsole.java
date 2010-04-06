/*
 * DesktopConsole.java
 *
 * Created on 2007.12.01
 * 
 * 
 */
package com.nci.ums.jmx.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.ums.jmx.ConsoleUtil;
import com.nci.ums.jmx.UMSManageStandardMBean;
import com.nci.ums.jmx.client.ClientListener;
import com.nci.ums.jmx.desktop.bean.SwingWorker;
import com.nci.ums.jmx.desktop.panels.AboutPanel;
import com.nci.ums.jmx.desktop.panels.ChannelMonitorPanel;
import com.nci.ums.jmx.desktop.panels.MediaChannelPanel;

/**
 * 
 * @author Qil.Wong
 */
public class DesktopConsole extends javax.swing.JFrame implements HotKey {

	private static final long serialVersionUID = 1L;
//	private UMSAxisServer server;
	private JPopupMenu logAreaMenu;
	private JMenuItem clearLogItem;
	// private JCheckBoxMenuItem scrollLockItem;
	private MediaChannelPanel channelPanel;
	private ChannelMonitorPanel monitorPanel;

	// UMS是否已经开启的标记
	// private static boolean umsStatusFlag = false;

	private UMSManageStandardMBean umsMBean = null;
	private RMIInputPanel rmiInput = new RMIInputPanel(this);
	private static DesktopConsole desktop;

	/** Creates new form DesktopConsole */
	private DesktopConsole() {

		initComponents();

		// Res.getLogger().addAppender(componentAppender);
		// stopBtn.setEnabled(false);
		this.setIconImage(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/ums_logo.gif")).getImage());

		createMenu();
		this.setTitle("UMS 4.0");

		iniPanels();
		showWelcome();
		initHotKey();

		startBtn.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/start.png")));
		stopBtn.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/stop.png")));
		exitMenuItem.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/exit.gif")));
		connectMenuItem.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/connect.png")));

		setExtendedState(JFrame.MAXIMIZED_HORIZ);
		centerOnScreen(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

	}

	private void showRmiInput() {
		int result = rmiInput.showDialog();

		if (result != -1) {
			initMBean();
		} else {
			// resetUnconnected();
			rmiInput.reset();
		}
	}

	public static DesktopConsole getInstance() {
		if (desktop == null) {
			desktop = new DesktopConsole();
		}
		return desktop;
	}

	private JMXConnector current_jmxc;

	// private MBeanServerConnection current_mbsc;

	private void initMBean() {
		try {
			if (current_jmxc != null) {
				current_jmxc.close();
			}

			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://" + rmiInput.getIP() + ":"
							+ rmiInput.getPort() + "/server");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			ObjectName mbeanName = new ObjectName(
					"MBeans:type=com.nci.ums.jmx.UMSManageStandard");
			try {
				mbsc.createMBean("com.nci.ums.jmx.UMSManageStandard",
						mbeanName, null, null);
			} catch (javax.management.InstanceAlreadyExistsException e) {
				System.err.println(e.getClass() + "," + e.getMessage());
			}
			umsMBean = (UMSManageStandardMBean) MBeanServerInvocationHandler
					.newProxyInstance(mbsc, mbeanName,
							UMSManageStandardMBean.class, false);
			ClientListener listener = new ClientListener();
			mbsc.addNotificationListener(mbeanName, listener, null, null);
			current_jmxc = jmxc;
			// current_mbsc = mbsc;
			if (umsMBean.isUMSStarted()) {
				resetUIStarted();
				List channels = umsMBean.getMediaList();
				getChannelPanel().refresh(channels);

			} else {
				resetUIUnStarted();
			}
		} catch (IOException e) {
			e.printStackTrace();
			JMXLogger.log(JMXLogger.INFO, "无法连接UMS服务：" + rmiInput.getIP() + ":"
					+ rmiInput.getPort());
			JMXLogger.log(JMXLogger.INFO, "请确认输入正确，并且UMS服务已经运行");
		} catch (Exception e) {
			e.printStackTrace();
			JMXLogger.logExceptionTrace(e);
		}
	}

	// private static void loadSystemClasspath() {
	// File system_dir = new File(appBinDir + "/lib");
	// File[] jars = system_dir.listFiles();
	// for (int i = 0; i < jars.length; i++) {
	// if (jars[i].getName().lastIndexOf(".jar") >= 0) {
	// ClassLoaderUtil.addClassPath(jars[i]);
	// }
	// }
	// }
	private void initHotKey() {
		addHotKey();
		Component[] comps = this.jTabbedPane1.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof HotKey) {
				((HotKey) comps[i]).addHotKey();
			}
		}
	}

	private static void centerOnScreen(Component component) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		component.setLocation(
				screenSize.width / 2 - (component.getWidth() / 2),
				screenSize.height / 2 - (component.getHeight() / 2));
	}

	public void addHotKey() {
		exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_E,
				java.awt.event.InputEvent.ALT_MASK
						| java.awt.event.InputEvent.SHIFT_MASK));
		settingMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_T,
				java.awt.event.InputEvent.ALT_MASK
						| java.awt.event.InputEvent.SHIFT_MASK));
		aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F1, 0));
		startBtn.setMnemonic(java.awt.event.KeyEvent.VK_S);
		stopBtn.setMnemonic(java.awt.event.KeyEvent.VK_T);
	}

	public void showWelcome() {
		// loggerArea.append("* * * * *****\r\n");
		// loggerArea.append("* * * * * * *\r\n");
		// loggerArea.append("* * * * * *****\r\n");
		// loggerArea.append("* * * * *\r\n");
		// loggerArea.append(" ****** * * *****\r\n");
		loggerArea.append(ConsoleUtil.getLocaleString("Welcome"));
		loggerArea.append("\r\n");
	}

	private void iniPanels() {
		try {
			channelPanel = new MediaChannelPanel(this);
			this.jTabbedPane1.add(ConsoleUtil.getLocaleString("Channels"),
					channelPanel);
		} catch (SQLException e) {
			JMXLogger.log(JMXLogger.ERROR, "初始化渠道列表过程中发生异常。" + e.getMessage());
			JMXLogger.logExceptionTrace(e);
		}
		monitorPanel = new ChannelMonitorPanel(this);
		this.jTabbedPane1.add(ConsoleUtil.getLocaleString("Monitoer"),
				monitorPanel);
	}

	/**
	 * 菜单的生成，包括托盘菜单和日志菜单
	 */
	private void createMenu() {
		// create tray menu

		// create log area menu
		logAreaMenu = new JPopupMenu();
		clearLogItem = new JMenuItem(ConsoleUtil.getLocaleString("Clear"));
		logAreaMenu.add(clearLogItem);
		clearLogItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				clearLogAction(e);
			}
		});
	}

	private void clearLogAction(ActionEvent e) {
		loggerArea.setText("");
	}

	/**
	 * 开启Axis2服务
	 */
	private void startUMS() {
		new Thread() {

			public void run() {
				startBtn.setEnabled(false);
				umsMBean.startUMS();
			}
		}.start();
		// SwingWorker worker = new SwinW

	}

	private void stopUMS() {
		new Thread() {
			public void run() {
				try {
					umsMBean.stopUMS();
				} catch (Exception e) {
					resetUnconnected();
					JMXLogger.logExceptionTrace(e);
					JMXLogger.log(JMXLogger.INFO, "服务已停止，但过程中出现JMX异常，可能服务已经关闭");
				}
			}
		}.start();
		stopBtn.setEnabled(false);
		progressBar.setIndeterminate(true);
	}

	private void showAbout() {
		final JFrame parent = (JFrame) this;
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				JDialog dialog = new JDialog(parent, true);
				dialog.setResizable(true);
				dialog.getContentPane().setLayout(new BorderLayout());
				dialog.getContentPane().add(new AboutPanel(dialog));
				dialog.pack();
				dialog.setLocationRelativeTo(parent);
				ConsoleUtil.addEscapeHotKey(dialog);
				dialog.setTitle(ConsoleUtil.getLocaleString("About_Title"));
				return dialog;
			}

			public void finished() {
				JDialog d = (JDialog) getValue();
				d.setVisible(true);
			}
		};
		worker.start();
	}

	/**
	 * 退出控制台，这个动作会将已经启动的UMS Server也停止
	 */
	private void exit() {
		System.exit(0);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startBtn = new javax.swing.JButton();
        stopBtn = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new JideTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        loggerArea = JMXLogger.consoleloggerArea;
        progressBar = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        connectMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        settingMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ServerClosing(evt);
            }
        });

        startBtn.setText("开启");
        startBtn.setEnabled(false);
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });

        stopBtn.setText("停止");
        stopBtn.setEnabled(false);
        stopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopBtnActionPerformed(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(230);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setTopComponent(jTabbedPane1);

        loggerArea.setEditable(false);
        loggerArea.setBackground(Color.black);
        loggerArea.setForeground(Color.green);
        loggerArea.setColumns(20);
        loggerArea.setForeground(new java.awt.Color(102, 255, 0));
        loggerArea.setRows(5);
        loggerArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loggerAreaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(loggerArea);

        jSplitPane1.setRightComponent(jScrollPane1);

        jMenu1.setText("文件");

        connectMenuItem.setText("连接...");
        connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(connectMenuItem);

        exitMenuItem.setText("退出");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("工具");

        settingMenuItem.setText("设置");
        settingMenuItem.setEnabled(false);
        settingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(settingMenuItem);

//        jMenuBar1.add(jMenu2);

        jMenu3.setText("帮助");

        aboutMenuItem.setText("关于");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(aboutMenuItem);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                        .add(198, 198, 198)
                        .add(startBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(9, 9, 9)
                        .add(stopBtn)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {startBtn, stopBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(startBtn)
                        .add(stopBtn))
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectMenuItemActionPerformed
		showRmiInput();
	}// GEN-LAST:event_connectMenuItemActionPerformed

	private void settingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:settingMenuItemActionPerformed
		final JFrame f = this;
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				SettingDialog set = new SettingDialog(f, true);
				return set;
			}

			public void finished() {
				SettingDialog set = (SettingDialog) this.get();
				set.setLocationRelativeTo(f);
				set.setVisible(true);
			}
		};
		worker.start();
	}// GEN-LAST:settingMenuItemActionPerformed

	private void loggerAreaMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:loggerAreaMouseClicked
		// if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
		// logAreaMenu.show(loggerArea, evt.getX(), evt.getY());
		// }
	}// GEN-LAST:loggerAreaMouseClicked

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:aboutMenuItemActionPerformed
		showAbout();
	}// GEN-LAST:aboutMenuItemActionPerformed

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:exitMenuItemActionPerformed
		int i = JOptionPane.showConfirmDialog(this, "确定退出?", "提醒",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (i == JOptionPane.OK_OPTION) {
			this.exit();
		}
	}// GEN-LAST:exitMenuItemActionPerformed

	private void ServerClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:ServerClosing
		System.out.println("Exit");
	}// GEN-LAST:ServerClosing

	private void stopBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:stopBtnActionPerformed
		this.stopUMS();
	}// GEN-LAST:stopBtnActionPerformed

	private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:startBtnActionPerformed
		this.startUMS();
	}// GEN-LAST:startBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea loggerArea;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem settingMenuItem;
    private javax.swing.JButton startBtn;
    private javax.swing.JButton stopBtn;
    // End of variables declaration//GEN-END:variables

	public MediaChannelPanel getChannelPanel() {
		return channelPanel;
	}

	public void setChannelPanel(MediaChannelPanel channelPanel) {
		this.channelPanel = channelPanel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JButton getStopButton() {
		return stopBtn;
	}

	public JButton getStartButton() {
		return startBtn;
	}

	public ChannelMonitorPanel getMonitorPanel() {
		return monitorPanel;
	}

	public void setMonitorPanel(ChannelMonitorPanel monitorPanel) {
		this.monitorPanel = monitorPanel;
	}

	/**
	 * 判断服务器是否已经启动
	 * 
	 * @return
	 */
	public boolean isServerStarted() {

		return umsMBean.isUMSStarted();
	}

	public UMSManageStandardMBean getUMSMBean() {
		return umsMBean;
	}

	/**
	 * 重置界面为已断开JMX服务器状态
	 */
	public void resetUnconnected() {
		startBtn.setEnabled(false);
		stopBtn.setEnabled(false);
		settingMenuItem.setEnabled(false);
		progressBar.setIndeterminate(false);
	}

	/**
	 * 重置界面为已连接JMX服务器状态
	 */
	public void resetUIConnected() {
		startBtn.setEnabled(false);
		stopBtn.setEnabled(true);

		settingMenuItem.setEnabled(false);
		progressBar.setIndeterminate(false);
	}

	/**
	 * 重置界面为已关闭UMS服务器状态
	 */
	public void resetUIUnStarted() {
		resetUnconnected();
		channelPanel.clearChannels();

		monitorPanel.clearActiveChannels();
		monitorPanel.stopAllMonitors();
		startBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		settingMenuItem.setEnabled(false);
		progressBar.setIndeterminate(false);
	}

	/**
	 * 重置界面为已开启UMS服务器状态
	 */
	public void resetUIStarted() {
		startBtn.setEnabled(false);
		stopBtn.setEnabled(true);
		// channelPanel
		progressBar.setIndeterminate(false);
		settingMenuItem.setEnabled(true);
	}

	public void resetUIStarting() {
		startBtn.setEnabled(false);
		stopBtn.setEnabled(false);
		progressBar.setIndeterminate(true);
		settingMenuItem.setEnabled(false);
	}

	public void resetUIStopping() {
		startBtn.setEnabled(false);
		stopBtn.setEnabled(false);
		progressBar.setIndeterminate(true);
		settingMenuItem.setEnabled(false);
	}

	/**
	 * @param args
	 *            the command line arguments
	 * @throws UnknownHostException
	 */
	public static void main(final String args[]) throws UnknownHostException {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
					// LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
					if (System.getProperty("os.name").indexOf("Windows") >= 0) {

						LookAndFeelFactory
								.installJideExtension(LookAndFeelFactory.ECLIPSE3X_STYLE);

					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				DesktopConsole desktop = DesktopConsole.getInstance();
				desktop.setVisible(true);
				desktop.showRmiInput();
				// console.setVisible(true);
				if (args != null && args.length > 0) {
					if (args[0] != null && args[0].equalsIgnoreCase("start")) {
						DesktopConsole.getInstance().startUMS();
					}
				}
			}
		});
	}
}
