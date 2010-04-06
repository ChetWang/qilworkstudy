/*
 * DesktopConsole.java
 *
 * Created on 2007.12.01
 * 
 * 
 */
package com.nci.ums.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import org.apache.axis2.AxisFault;

import snoozesoft.systray4j.SubMenu;
import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuEvent;
import snoozesoft.systray4j.SysTrayMenuIcon;
import snoozesoft.systray4j.SysTrayMenuItem;
import snoozesoft.systray4j.SysTrayMenuListener;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.desktop.axis2.UMSAxisServer_V3;
import com.nci.ums.desktop.bean.ComponentAppender;
import com.nci.ums.desktop.bean.SwingWorker;
import com.nci.ums.desktop.panels.AboutPanel;
import com.nci.ums.desktop.panels.ChannelMonitorPanel;
import com.nci.ums.desktop.panels.MediaChannelPanel;
import com.nci.ums.periphery.core.UMSServer_V3;
import com.nci.ums.util.Res;

/**
 * 
 * @author Qil.Wong
 */
public class DesktopConsole extends javax.swing.JFrame implements
		SysTrayMenuListener, HotKey {

	private static final long serialVersionUID = 1L;
	private UMSAxisServer_V3 server;
	// 托盘的图标，分运行和停止两类
	private final SysTrayMenuIcon[] icons = {
			new SysTrayMenuIcon(getClass().getResource(
					"/com/nci/ums/desktop/icons/ums_running.ico")),
			new SysTrayMenuIcon(getClass().getResource(
					"/com/nci/ums/desktop/icons/ums_stopped.ico")) };
	private static final String[] toolTips = {
			ConsoleUtil.getLocaleString("UMS_3.0_(running)"),
			ConsoleUtil.getLocaleString("UMS_3.0_(stopped)"),
			ConsoleUtil.getLocaleString("UMS_3.0_(starting)"),
			ConsoleUtil.getLocaleString("UMS_3.0_(stopping)") };
	// 托盘菜单
	private SysTrayMenu menu;
	private SysTrayMenuItem itemRestore_hide;
	private SysTrayMenuItem item_start;
	private SysTrayMenuItem item_stop;
	private JPopupMenu logAreaMenu;
	private JMenuItem clearLogItem;
	// private JCheckBoxMenuItem scrollLockItem;
	private static String HIDE = ConsoleUtil.getLocaleString("Hide");
	private static String RESTORE = ConsoleUtil.getLocaleString("Restore");
	private MediaChannelPanel channelPanel;
	private ChannelMonitorPanel monitorPanel;
	// UMS日志显示控件
	private static ComponentAppender componentAppender;
	// UMS是否已经开启的标记
	private static boolean umsStatusFlag = false;
	// 应用程序所在的根路径
	private static String appBinDir = System.getProperty("user.dir");

	/** Creates new form DesktopConsole */
	public DesktopConsole() {
		// loadSystemClasspath();
		loadExternalClasspath();
		initComponents();
		jSplitPane1.setBorder(null);
		componentAppender = new ComponentAppender(loggerArea, 150);
		loggerArea.setEditable(false);
		loggerArea.setBackground(Color.black);
		loggerArea.setForeground(Color.green);
		// Res.getLogger().addAppender(componentAppender);
		stopBtn.setEnabled(false);
		this.setIconImage(new ImageIcon(getClass().getResource(
				"/com/nci/ums/desktop/icons/ums_logo.gif")).getImage());
		createMenu();
		this.setTitle("UMS 3.0");
		iniPanels();
		showWelcome();
		initHotKey();
	}

	private static void loadExternalClasspath() {
		File ext_dir = new File(appBinDir + "/lib/ext");
		File[] jars = ext_dir.listFiles();
		if (jars != null) {
			for (int i = 0; i < jars.length; i++) {
				if (jars[i].getName().lastIndexOf(".jar") >= 0) {
					ClassLoaderUtil.addClassPath(jars[i]);
				}
			}
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
			Res.log(Res.ERROR, "初始化渠道列表过程中发生异常。" + e.getMessage());
			Res.logExceptionTrace(e);
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
		menu = new SysTrayMenu(icons[1], toolTips[1]);
		SubMenu serverSubMenu = new SubMenu(ConsoleUtil
				.getLocaleString("Server"));
		SysTrayMenuItem itemExit = new SysTrayMenuItem(ConsoleUtil
				.getLocaleString("Exit"), "exit");
		itemExit.addSysTrayMenuListener(this);
		SysTrayMenuItem itemAbout = new SysTrayMenuItem(ConsoleUtil
				.getLocaleString("About..."), "about");
		itemAbout.addSysTrayMenuListener(this);
		itemRestore_hide = new SysTrayMenuItem(HIDE, "restore");
		itemRestore_hide.addSysTrayMenuListener(this);
		menu.addItem(itemExit);
		menu.addItem(itemAbout);
		menu.addItem(itemRestore_hide);
		icons[0].addSysTrayMenuListener(this);
		icons[1].addSysTrayMenuListener(this);
		item_start = new SysTrayMenuItem(ConsoleUtil.getLocaleString("Start"),
				"start");
		item_start.addSysTrayMenuListener(this);
		item_stop = new SysTrayMenuItem(ConsoleUtil.getLocaleString("Stop"),
				"stop");
		item_stop.addSysTrayMenuListener(this);
		item_stop.setEnabled(false);
		serverSubMenu.addItem(item_stop);
		serverSubMenu.addItem(item_start);
		menu.addItem(serverSubMenu);
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

	private boolean checkChannelStarting() {
		boolean allStarted = true;
		try {

			Iterator it_out = ChannelManager.getOutMediaInfoHash().values()
					.iterator();
			Iterator it_in = ChannelManager.getInMediaInfoHash().values()
					.iterator();
			while (it_out.hasNext()) {
				MediaInfo media = (MediaInfo) it_out.next();
				ChannelIfc ifc = media.getChannelObject();
				if (ifc == null || ifc.isStartOnce() == false)
					allStarted = false;
			}
			while (it_in.hasNext()) {
				MediaInfo media = (MediaInfo) it_in.next();
				ChannelIfc ifc = media.getChannelObject();
				if (ifc == null || ifc.isStartOnce() == false)
					allStarted = false;
			}
		} catch (Exception e) {
			allStarted = false;
		}
		System.out.println("没完没了checkChannelStarting:  " + allStarted);
		return allStarted;
	}

	/**
	 * 开启Axis2服务
	 */
	private void startAxis2() {
		
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				startBtn.setEnabled(false);
				item_start.setEnabled(false);
				Res.log(Res.INFO, "UMS Web Service Server 开始启动...");
				progressBar.setIndeterminate(true);		
				final String repository = appBinDir + "/repository";
				final String config = appBinDir + "/conf/ums_axis2.xml";
				menu.setToolTip(toolTips[2]);
				if (server == null) {
					try {
						server = new UMSAxisServer_V3(repository, config);// 同时将调用Axis2的生命周期设置,执行UMS3Server_Axis2LifeCycle的startUp
						umsStatusFlag = true;
					} catch (AxisFault e) {
						umsStatusFlag = false;
						Res.log(Res.ERROR, "UMS Web Service Server启动失败！"
								+ e.getMessage());
						Res.logExceptionTrace(e);
					}
				}
				try {
					if (umsStatusFlag) {
						server.start();
					}
				} catch (AxisFault e) {
					umsStatusFlag = false;
					Res.log(Res.ERROR, "UMS Web Service Server启动失败！"
							+ e.getMessage());
					Res.logExceptionTrace(e);
				}
				umsStatusFlag = UMSServer_V3.isDbFlag() & umsStatusFlag
						& UMSServer_V3.isSocketFlag();
				if (umsStatusFlag) {// UMS启动完成
					String port = "";
					try {
						// 得到axis2 server的端口
						port = ConsoleUtil.getAxis2PortVector(config).get(1)
								.toString();
						Thread.sleep(3000);// 等待其他渠道产生完，增加用户体验
						boolean flag = false;
						while (!flag) {
							Thread.sleep(1000);
							flag = checkChannelStarting();
						}
					} catch (Exception e) {
						Res.logExceptionTrace(e);
					}
					try {
						Res.log(Res.INFO, new StringBuffer().append(
								"UMS Web Service启动完成，端口为:").append(port)
								.append(". 详细服务请查看：http://").append(
										InetAddress.getLocalHost()
												.getHostAddress()).append(":")
								.append(port).toString());
					} catch (UnknownHostException e) {
					}
					Res.log(Res.INFO, "UMS3.0启动完成!");
				} else {// UMS没有启动起来
					try {
						server.stop();
					} catch (AxisFault e) {
						Res.log(Res.ERROR, "UMS3.0启动失败!" + e.getMessage());
						Res.logExceptionTrace(e);
					}
					server = null;
					Res.log(Res.ERROR, "UMS3.0启动失败! 详细原因，请查看控制台日志文件和UMS日志文件.");
				}
				return null;
			}

			public void finished() {

				progressBar.setIndeterminate(false);
				// 启动成功
				if (umsStatusFlag) {
					channelPanel.showChannels();
					stopBtn.setEnabled(true);
					item_stop.setEnabled(true);
					menu.setIcon(icons[0]);
					menu.setToolTip(toolTips[0]);
					getMonitorPanel().initActiveChannels();
					System.out.println("System Free Memeroy:"
							+ Runtime.getRuntime().freeMemory());
				} else {// 启动失败
					channelPanel.clearChannels();
					startBtn.setEnabled(true);
					item_start.setEnabled(true);
					menu.setIcon(icons[1]);
					menu.setToolTip(toolTips[1]);
				}
			}
		};
		worker.start();
	}

	private void stopAxis2() {
		Res.log(Res.INFO, "UMS3.0准备停止...");
		progressBar.setIndeterminate(true);
		this.stopBtn.setEnabled(false);
		this.item_stop.setEnabled(false);
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				menu.setToolTip(toolTips[3]);
				try {
					if (server != null) {

						// 这两个按钮的变化过程不放在channelPanel.clearChannels()方法内部是为了给用户更好的体验
						channelPanel.getApplyBtn().setEnabled(false);
						channelPanel.getRefreshBtn().setEnabled(false);
						server.stop();// 同时将调用Axis2的生命周期设置,执行UMS3Server_Axis2LifeCycle的shutdown
						server = null;
						umsStatusFlag = false;
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
						}
						Res.log(Res.INFO, "UMS Server已停止。");
					}
				} catch (AxisFault e) {
					Res.log(Res.ERROR, "UMS3.0已停止过程中发生异常。" + e.getMessage());
					Res.logExceptionTrace(e);
					umsStatusFlag = true;
				}
				return null;
			}

			public void finished() {
				if (umsStatusFlag == false) {// 成功关闭

					progressBar.setIndeterminate(false);
					startBtn.setEnabled(true);
					item_start.setEnabled(true);
					menu.setIcon(icons[1]);
					menu.setToolTip(toolTips[1]);
					channelPanel.clearChannels();
					monitorPanel.clearActiveChannels();
					monitorPanel.stopAllMonitors();
				} else {
					startBtn.setEnabled(false);
					item_start.setEnabled(false);
					menu.setIcon(icons[0]);
					menu.setToolTip(toolTips[0]);
				}
				System.gc();
			}
		};
		worker.start();
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
		if (!umsStatusFlag) {
			System.exit(0);
		}
		final JFrame parent = (JFrame) this;
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				int result = JOptionPane.showConfirmDialog(parent, ConsoleUtil
						.getLocaleString("Quit_Info"), ConsoleUtil
						.getLocaleString("Quit?"),
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (result == 0) {
					progressBar.setIndeterminate(true);
					Res.log(Res.INFO, "控制台准备退出，开始停止正在运行的服务...");
					try {
						server.stop();
					} catch (AxisFault e) {
						Res.log(Res.ERROR, "控制台退出过程中发生异常。" + e.getMessage());
						Res.logExceptionTrace(e);
					} catch (Exception e) {
						Res.logExceptionTrace(e);
					}
					Res.log(Res.INFO, "所有服务已停止，控制台退出。");
					try {
						Thread.sleep(1000);// 无实际作用，仅用于增加用户体验。
					} catch (InterruptedException e) {
						Res.logExceptionTrace(e);
					}
					progressBar.setIndeterminate(false);
				}
				return new Integer(result);
			}

			public void finished() {
				if (((Integer) get()).intValue() == 0) {
					try {
						Thread.sleep(500);// 无实际作用，仅用于增加用户体验。
					} catch (InterruptedException e) {
						Res.logExceptionTrace(e);
					}
					System.exit(0);
				}
			}
		};
		worker.start();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		startBtn = new javax.swing.JButton();
		stopBtn = new javax.swing.JButton();
		jSplitPane1 = new javax.swing.JSplitPane();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		loggerArea = new javax.swing.JTextArea();
		progressBar = new javax.swing.JProgressBar();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
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

		startBtn.setText(ConsoleUtil.getLocaleString("Start"));
		startBtn.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startBtnActionPerformed(evt);
			}
		});

		stopBtn.setText(ConsoleUtil.getLocaleString("Stop"));
		stopBtn.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stopBtnActionPerformed(evt);
			}
		});

		jSplitPane1.setDividerLocation(230);
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane1.setResizeWeight(0.5);
		jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.TOP);
		jSplitPane1.setTopComponent(jTabbedPane1);

		loggerArea.setColumns(20);
		loggerArea.setRows(5);
		loggerArea.addMouseListener(new java.awt.event.MouseAdapter() {

			public void mouseClicked(java.awt.event.MouseEvent evt) {
				loggerAreaMouseClicked(evt);
			}
		});

		jScrollPane1.setViewportView(loggerArea);

		jSplitPane1.setRightComponent(jScrollPane1);

		jMenu1.setText(ConsoleUtil.getLocaleString("File"));
		exitMenuItem.setText(ConsoleUtil.getLocaleString("Exit"));
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		jMenu1.add(exitMenuItem);

		jMenuBar1.add(jMenu1);

		jMenu2.setText(ConsoleUtil.getLocaleString("Tool"));
		settingMenuItem.setText(ConsoleUtil.getLocaleString("Settings"));
		settingMenuItem.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				settingMenuItemActionPerformed(evt);
			}
		});

		jMenu2.add(settingMenuItem);

		jMenuBar1.add(jMenu2);

		jMenu3.setText(ConsoleUtil.getLocaleString("Help"));
		aboutMenuItem.setText(ConsoleUtil.getLocaleString("About"));
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutMenuItemActionPerformed(evt);
			}
		});

		jMenu3.add(aboutMenuItem);

		jMenuBar1.add(jMenu3);

		setJMenuBar(jMenuBar1);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																jSplitPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																709,
																Short.MAX_VALUE)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				progressBar,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				348,
																				Short.MAX_VALUE)
																		.add(
																				200,
																				200,
																				200)
																		.add(
																				startBtn,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				76,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.add(
																				9,
																				9,
																				9)
																		.add(
																				stopBtn)))
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { startBtn, stopBtn },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jSplitPane1,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												410, Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(
																layout
																		.createParallelGroup(
																				org.jdesktop.layout.GroupLayout.BASELINE)
																		.add(
																				startBtn)
																		.add(
																				stopBtn))
														.add(
																progressBar,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));
		pack();
	}// </editor-fold>

	private void settingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
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
	}

	private void loggerAreaMouseClicked(java.awt.event.MouseEvent evt) {
		// if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
		// logAreaMenu.show(loggerArea, evt.getX(), evt.getY());
		// }
	}

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		showAbout();
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

		this.exit();
	}

	private void ServerClosing(java.awt.event.WindowEvent evt) {
		itemRestore_hide.setLabel(RESTORE);
	}

	private void stopBtnActionPerformed(java.awt.event.ActionEvent evt) {

		this.stopAxis2();
	}

	private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {

		this.startAxis2();
	}

	/**
	 * @param args
	 *            the command line arguments
	 * @throws UnknownHostException
	 */
	public static void main(final String args[]) throws UnknownHostException {
		// final String argument = null;
		// if(args.length>0 )
		// argument = args[0];
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {

					if (System.getProperty("os.name").indexOf("Windows") >= 0) // UIManager.setLookAndFeel(new
					// com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
					// UIManager.setLookAndFeel(new
					// com.jgoodies.looks.plastic.PlasticLookAndFeel());
					// UIManager.setLookAndFeel(new
					// com.jgoodies.looks.windows.WindowsLookAndFeel());
					{
						UIManager
								.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticXPLookAndFeel());
//						LookAndFeelFactory.installDefaultLookAndFeel();
//						LookAndFeelFactory.installJideExtension(LookAndFeelFactory.ECLIPSE3X_STYLE);
					} // UIManager.setLookAndFeel(new
					// com.jtattoo.plaf.aero.AeroLookAndFeel());
					// UIManager.setLookAndFeel(new
					// com.jtattoo.plaf.smart.SmartLookAndFeel());
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					else {
//						UIManager.setLookAndFeel(UIManager
//								.getSystemLookAndFeelClassName());
						UIManager
						.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticXPLookAndFeel());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				DesktopConsole console = new DesktopConsole();
				console.setExtendedState(JFrame.MAXIMIZED_HORIZ);
				centerOnScreen(console);
				console.setVisible(true);
				if (args != null && args.length > 0)
					if (args[0] != null && args[0].equalsIgnoreCase("start")) {
						console.startAxis2();
					}
			}
		});
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JMenuItem aboutMenuItem;
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

	// End of variables declaration
	public void iconLeftClicked(SysTrayMenuEvent arg0) {
	}

	public void iconLeftDoubleClicked(SysTrayMenuEvent arg0) {
		boolean visible = this.isVisible();
		if (visible) {
			itemRestore_hide.setLabel(RESTORE);
			this.setExtendedState(JFrame.MAXIMIZED_VERT);

		} else {
			this.setExtendedState(JFrame.ICONIFIED);
			itemRestore_hide.setLabel(HIDE);
		}
		this.setVisible(!visible);
	}

	public void menuItemSelected(SysTrayMenuEvent arg0) {
		if (arg0.getActionCommand().equalsIgnoreCase("exit")) {
			this.exit();
		} else if (arg0.getActionCommand().equalsIgnoreCase("about")) {
			showAbout();
		} else if (arg0.getActionCommand().equalsIgnoreCase("restore")) {
			if (this.isVisible() == false) {
				this.setVisible(true);
				itemRestore_hide.setLabel(HIDE);
			} else {
				this.setVisible(false);
				itemRestore_hide.setLabel(RESTORE);
			}
		} else if (arg0.getActionCommand().equalsIgnoreCase("start")) {
			this.startAxis2();
		} else if (arg0.getActionCommand().equalsIgnoreCase("stop")) {
			this.stopAxis2();
		}

	}

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

	public static ComponentAppender getComponentAppender() {
		return componentAppender;
	}

	public ChannelMonitorPanel getMonitorPanel() {
		return monitorPanel;
	}

	public void setMonitorPanel(ChannelMonitorPanel monitorPanel) {
		this.monitorPanel = monitorPanel;
	}

	public SysTrayMenuItem getItemRestore_hide() {
		return itemRestore_hide;
	}

	public void setItemRestore_hide(SysTrayMenuItem itemRestore_hide) {
		this.itemRestore_hide = itemRestore_hide;
	}

	public SysTrayMenuItem getItem_start() {
		return item_start;
	}

	public void setItem_start(SysTrayMenuItem item_start) {
		this.item_start = item_start;
	}

	public SysTrayMenuItem getItem_stop() {
		return item_stop;
	}

	public void setItem_stop(SysTrayMenuItem item_stop) {
		this.item_stop = item_stop;
	}

	public static boolean isServerStarted() {
		return umsStatusFlag;
	}
}
