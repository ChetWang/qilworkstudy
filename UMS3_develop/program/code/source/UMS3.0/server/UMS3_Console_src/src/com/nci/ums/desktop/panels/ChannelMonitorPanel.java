/*
 * ChannelMonitorPanel.java
 *
 * Created on __DATE__, __TIME__
 */
package com.nci.ums.desktop.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.nci.ums.desktop.ConsoleUtil;
import com.nci.ums.desktop.DesktopConsole;
import com.nci.ums.desktop.HotKey;
import com.nci.ums.desktop.bean.MediaBean;
import com.nci.ums.desktop.bean.SwingWorker;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Qil.Wong
 */
public class ChannelMonitorPanel extends javax.swing.JPanel implements HotKey {

	private DesktopConsole parent;
	private Map showChartThreadMap;
	//how many seconds should be waited while a chart showing thread start to work once
	private static final int TIME_DURATION = 3;
	private String inChartSQL;
	private String outChartSQL;
	private String nciMedia_ID;
	private String nciV3Media_ID;
//	private Timer twoDayCleanTimer;
	private static int TWODAY = 2 * 24 * 60 * 60 * 1000;

	// private static int TWODAY = 5*1000;

	public ChannelMonitorPanel() {
		initComponents();
	}

	/**
	 * 初始化函数
	 * 
	 * @param parent
	 *            父容器
	 */
	public ChannelMonitorPanel(DesktopConsole parent) {
		this.parent = parent;
		initComponents();
		activeChannelCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboActionPerform(e);
			}
		});
		this.clearActiveChannels();
		showChartThreadMap = new ConcurrentHashMap();
		outChartSQL = Util.getDialect().getChartShowCountsOutSQL_V3();
		inChartSQL = Util.getDialect().getChartShowCountsInSQL_V3();
		Properties props = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream(
					"/resources/media.props");
			props.load(is);
			is.close();
			;
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}
		nciMedia_ID = props.getProperty("NCIMEDIA_ID");
		nciV3Media_ID = props.getProperty("NCIV3MEDIA_ID");
//		twoDayCleanTimer = new Timer(TWODAY, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				clearTimeSeries();
//			}
//		});
//		twoDayCleanTimer.start();
	}

	/**
	 * 添加快捷键
	 */
	public void addHotKey() {
		stopAllMonitorsBtn.setMnemonic(java.awt.event.KeyEvent.VK_P);
	}

	static Class getClass(String s) {
		Class cls = null;
		try {
			cls = Class.forName(s);
		} catch (ClassNotFoundException cnfe) {
			throw new NoClassDefFoundError(cnfe.getMessage());
		}
		return cls;
	}

	/**
	 * 激活所有运行状态的渠道到界面上显示。
	 * 
	 */
	public void initActiveChannels() {
		ArrayList activeChannels = this.getActiveChannels();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(ConsoleUtil.getLocaleString("Select_One"));
		for (int i = 0; i < activeChannels.size(); i++) {
			MediaBean bean = (MediaBean) activeChannels.get(i);
			model.addElement(bean);
			// 如果是移动短消息渠道，并且渠道是通过华为API来发送消息的，则该类型渠道是通过监听来接收消息的，这里要给一个伪渠道
			if (bean.getMediaID().equals(nciMedia_ID)
					|| bean.getMediaID().equals(nciV3Media_ID)) {
				MediaBean smsBean = new MediaBean(bean.getMediaID(), bean
						.getMediaName().replaceAll("外拨", "内拨"),
						MediaBean.TYPE_CMSMS_IN, bean.getMediaStatus());
				model.addElement(smsBean);
			}
		}
		activeChannelCombo.setModel(model);
	}

	/**
	 * 在界面上清空所有活动的渠道信息
	 */
	public void clearActiveChannels() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(ConsoleUtil.getLocaleString("Select_One"));
		activeChannelCombo.setModel(model);
	}

	/**
	 * 获取活动状态的渠道。如果上次有运行的渠道但当前状态是停止的，应该将该渠道从activeChannelCombo、showChartThreadMap中移除
	 * 
	 * @return 活动的渠道集合
	 */
	private ArrayList getActiveChannels() {
		ArrayList channels = parent.getChannelPanel().getOriginalChannels();
		ArrayList activeChannels = new ArrayList();
		if (channels != null) {
			for (int i = 0; i < channels.size(); i++) {
				MediaBean media = (MediaBean) channels.get(i);
				// 如果是活动状态的渠道
				if (media.getMediaStatus() == MediaBean.STATUS_RUNNING) {
					activeChannels.add(media);
				} else {// 如果是停止状态的渠道
					if (this.showChartThreadMap.containsKey(media.getMediaID()
							+ media.getMediaType())) {
						((ChartShowThread) showChartThreadMap.get(media
								.getMediaID()
								+ media.getMediaType())).pleaseStop();
						showChartThreadMap.remove(media.getMediaID()
								+ media.getMediaType());
						if (media.getMediaID().equals(nciMedia_ID)
								|| media.getMediaID().equals(nciV3Media_ID)) {
							((ChartShowThread) showChartThreadMap.get(media
									.getMediaID()
									+ MediaBean.TYPE_CMSMS_IN)).pleaseStop();
							showChartThreadMap.remove(media.getMediaID()
									+ MediaBean.TYPE_CMSMS_IN);
						}
					}
				}
			}
		}
		return activeChannels;
	}

	/**
	 * 初始化实时显示的图形
	 * 
	 * @param media
	 *            对应的渠道
	 * @return Vector，包含TimeSeries对象和ChartPanel对象，用来对应显示的效果
	 */
	private Vector initChart(MediaBean media) {
		TimeSeries timeseries = new TimeSeries(media.toString(),
				Millisecond.class);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(
				timeseries);
		ChartPanel chartpanel = new ChartPanel(createChart(media,
				timeseriescollection));
		this.removeCompFromContainer(rootChartPanel);
		rootChartPanel.add(chartpanel);
		chartpanel.updateUI();
		Vector v = new Vector();
		v.add(timeseries);
		v.add(chartpanel);
		return v;
	}

	/**
	 * 清除容器上的所有组件
	 * 
	 * @param container
	 *            指定的容器
	 */
	public void removeCompFromContainer(Container container) {
		Component[] comps = container.getComponents();
		if (comps != null && comps.length > 0) {
			for (int i = 0; i < comps.length; i++) {
				container.remove(comps[i]);
				comps[i] = null;
			}
		}
		container.repaint();
	}

	private JFreeChart createChart(MediaBean media, XYDataset xydataset) {
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(media
				.toString(), ConsoleUtil.getLocaleString("Time"), ConsoleUtil
				.getLocaleString("Count(s)"), xydataset, false, false, false);
		XYPlot xyplot = jfreechart.getXYPlot();
		ValueAxis valueaxis = xyplot.getDomainAxis();
		valueaxis.setAutoRange(true);
		valueaxis.setFixedAutoRange(600000);
		valueaxis = xyplot.getRangeAxis();
		return jfreechart;
	}

	private void comboActionPerform(ActionEvent e) {
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				int index = activeChannelCombo.getSelectedIndex();
				if (index > 0) {
					removeCompFromContainer(rootChartPanel);
					MediaBean media = (MediaBean) activeChannelCombo
							.getSelectedItem();
					if (showChartThreadMap.containsKey(media.getMediaID()
							+ media.getMediaType())) {
						ChartShowThread t = (ChartShowThread) showChartThreadMap
								.get(media.getMediaID() + media.getMediaType());
						rootChartPanel.add(t.getChartPanel());
						t.getChartPanel().updateUI();
					} else {
						parent.getProgressBar().setIndeterminate(true);
						Vector chartPanelVector = initChart(media);
						ChartShowThread t = new ChartShowThread(media,
								chartPanelVector);
						showChartThreadMap.put(media.getMediaID()
								+ media.getMediaType(), t);
						return t;
					}
				} else {
					removeCompFromContainer(rootChartPanel);
				}
				return null;
			}

			public void finished() {
				Object obj = this.get();
				if (obj != null) {
					ChartShowThread t = (ChartShowThread) obj;
					t.start();
					parent.getProgressBar().setIndeterminate(false);
				}
			}
		};
		worker.start();
	}

	/**
	 * 停止所有显示图像的线程
	 */
	private void stopShowChartThreads() {
		Iterator it_values = showChartThreadMap.values().iterator();
		while (it_values.hasNext()) {
			ChartShowThread t = (ChartShowThread) it_values.next();
			t.pleaseStop();
		}
		showChartThreadMap.clear();
	}

//	/**
//	 * 每个一段时间后，就要清空图像显示数据，否则容易导致内存不断增长，影响性能
//	 */
//	private void clearTimeSeries() {
//		SwingWorker worker = new SwingWorker() {
//
//			public Object construct() {
//				Iterator it = showChartThreadMap.values().iterator();
//				while (it.hasNext()) {
//					ChartShowThread t = (ChartShowThread) it.next();
//					t.getTimeseries().clear();
//				}
//				// 图片显示所占内存偏大，因此最好每次结束时都要强制回收一下
//				System.gc();
//				return null;
//			}
//		};
//		worker.start();
//	}

	/**
	 * 停止所有监视
	 */
	public void stopAllMonitors() {
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				removeCompFromContainer(rootChartPanel);
				stopShowChartThreads();
				// 图片显示所占内存偏大，因此最好每次结束时都要强制回收一下
				System.gc();
				return null;
			}
		};
		worker.start();
	}

	public void stopMonitor(MediaBean media) {
		ChartShowThread t = (ChartShowThread) showChartThreadMap.get(media
				.getMediaID()
				+ media.getMediaType());
		if (t != null) {
			t.pleaseStop();
		}
		showChartThreadMap.remove(media.getMediaID() + media.getMediaType());
		removeCompFromContainer(rootChartPanel);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		activeChannelCombo = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		rootChartPanel = new javax.swing.JPanel();
		stopAllMonitorsBtn = new javax.swing.JButton();

		activeChannelCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "1", "2" }));

		jLabel1
				.setText(ConsoleUtil
						.getLocaleString("Please_choose_a_channel:"));

		rootChartPanel.setLayout(new java.awt.BorderLayout());

		stopAllMonitorsBtn.setText(ConsoleUtil.getLocaleString("Stop_Monitor"));
		stopAllMonitorsBtn
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						stopMonitorBtnActionPerformed(evt);
					}
				});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																rootChartPanel,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																380,
																Short.MAX_VALUE)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				jLabel1)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				activeChannelCombo,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				129,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED,
																				33,
																				Short.MAX_VALUE)
																		.add(
																				stopAllMonitorsBtn)))
										.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup().addContainerGap().add(
						layout.createParallelGroup(
								org.jdesktop.layout.GroupLayout.BASELINE).add(
								jLabel1).add(activeChannelCombo,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(stopAllMonitorsBtn)).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(
						rootChartPanel,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 249,
						Short.MAX_VALUE).addContainerGap()));
	}// </editor-fold>

	private void stopMonitorBtnActionPerformed(java.awt.event.ActionEvent evt) {

		// if (activeChannelCombo.getSelectedIndex() > 0) {
		// stopAllMonitors();
		// activeChannelCombo.setSelectedIndex(0);
		// }
		// Component c = this;
		// boolean flag = false;
		// while(!flag){
		// flag = c instanceof JFrame;
		// c = c.getParent();
		// }
		StopDialog dialog = new StopDialog(this);
		dialog.setModal(false);
		dialog.setLocation(stopAllMonitorsBtn.getLocationOnScreen().x,
				stopAllMonitorsBtn.getLocationOnScreen().y
						+ stopAllMonitorsBtn.getHeight());
		// dialog.setLocationRelativeTo(stopAllMonitorsBtn);
		dialog.setVisible(true);
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JComboBox activeChannelCombo;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel rootChartPanel;
	private javax.swing.JButton stopAllMonitorsBtn;

	// End of variables declaration
	public class ChartShowThread extends Thread {

		private boolean runningFlag = true;
		private MediaBean media;
		private ChartPanel chartPanel;
		private TimeSeries timeseries;
		private String currentDelayed = null;
		private String currentDate = null;
		private String currentTime = null;
		private String someTimeAgo = null;
		private Connection conn = null;

		public ChartShowThread(MediaBean media, Vector chartPanelVector) {
			super("ChartShow" + media.toString());
			this.media = media;
			this.chartPanel = (ChartPanel) chartPanelVector.get(1);
			this.timeseries = (TimeSeries) chartPanelVector.get(0);
			timeseries.setMaximumItemAge(TWODAY);
		}

		public void run() {
			while (runningFlag) {
				// 为保证实时效果，当前时间应比正常晚，这里设定为3秒
				long currentLong = Long.parseLong(ConsoleUtil
						.getCurrentTimeStr("yyyyMMddHHmmss"))-3;
				showChart(media,currentLong);
				try {
					sleep(TIME_DURATION * 1000);
				} catch (InterruptedException e) {
					Res.logExceptionTrace(e);
				}
			}
			Res.log(Res.INFO, this.getName()+"退出");
		}

//		long currentMill = 20071205085245L;

		/**
		 * 显示动态统计图形
		 * 
		 * @param media
		 *            指定的渠道
		 */
		private void showChart(MediaBean media,long currentLong) {			
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				currentDelayed = String.valueOf(currentLong);
				currentDate = currentDelayed.substring(0, 8);
				currentTime = currentDelayed.substring(8, 14);
				someTimeAgo = String.valueOf(currentLong-TIME_DURATION).substring(
						8, 14);
				PreparedStatement prep = null;
				switch (media.getMediaType()) {
				case MediaBean.TYPE_OUT:
					prep = conn.prepareStatement(outChartSQL);
					break;
				case MediaBean.TYPE_IN:
					prep = conn.prepareStatement(inChartSQL);
					break;
				case MediaBean.TYPE_CMSMS_IN:
					prep = conn.prepareStatement(inChartSQL);
					break;
				}
				prep.setString(1, media.getMediaID());
				prep.setString(2, currentDate);
				prep.setString(3, someTimeAgo);
				prep.setString(4, currentTime);
				prep.setString(5, media.getMediaID());
				prep.setString(6, currentDate);
				prep.setString(7, someTimeAgo);
				prep.setString(8, currentTime);
				ResultSet rs = prep.executeQuery();
				int count = 0;
				while (rs.next()) {
					count = rs.getInt(1);
				}
				
				timeseries.add(new Millisecond(), count);
				// timeseries.add(millisecond, count);
				rs.close();
				prep.close();
			} catch (SQLException e) {
				Res.log(Res.ERROR, "实时显示渠道流量过程中出现数据库错误。"
						+ e.getMessage());
				Res.logExceptionTrace(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
						conn=null;
					} catch (SQLException e) {
						Res.logExceptionTrace(e);
					}
				}
			}
		}

		public void pleaseStop() {
			runningFlag = false;
		}

		public ChartPanel getChartPanel() {
			return chartPanel;
		}

		public void setChartPanel(ChartPanel chartPanel) {
			this.chartPanel = chartPanel;
		}

		public TimeSeries getTimeseries() {
			return timeseries;
		}

		public void setTimeseries(TimeSeries timeseries) {
			this.timeseries = timeseries;
		}
	}

	public javax.swing.JPanel getRootChartPanel() {
		return rootChartPanel;
	}

	public void setRootChartPanel(javax.swing.JPanel rootChartPanel) {
		this.rootChartPanel = rootChartPanel;
	}

	public javax.swing.JComboBox getActiveChannelCombo() {
		return activeChannelCombo;
	}

	public void setActiveChannelCombo(javax.swing.JComboBox activeChannelCombo) {
		this.activeChannelCombo = activeChannelCombo;
	}

	public Map getShowChartThreadMap() {
		return showChartThreadMap;
	}

	public void setShowChartThreadMap(Map showChartThreadMap) {
		this.showChartThreadMap = showChartThreadMap;
	}
}

class StopDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 9039929618722582269L;
	javax.swing.JList commandList = new javax.swing.JList();
	ChannelMonitorPanel panel;

//	public StopDialog() {
//		super();
//		init();
//	}

	public StopDialog(ChannelMonitorPanel panel) {
		super();
		init();
		this.panel = panel;
	}

	private void init() {
		this.setUndecorated(true);
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(ConsoleUtil
				.getLocaleString("Stop_Current_Monitor"));
		listModel.addElement(ConsoleUtil.getLocaleString("Stop_All_Monitors"));
		commandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandList.setModel(listModel);
		// commandList.setBackground(new Color(194,243,254));
		JPanel p = new JPanel();
		// p.setBorder(BorderFactory.createEtchedBorder());
		// p.setBorder(BorderFactory.createLineBorder(Color.));
		p.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED), BorderFactory
				.createLineBorder(new Color(156, 122, 180), 1)));
		p.setLayout(new BorderLayout());
		p.add(commandList);
		this.getContentPane().add(p);
		this.addWindowListener(new WindowAdapter() {

			public void windowDeactivated(WindowEvent e) {
				dispose();
			}
		});
		commandList.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int index = commandList.getSelectedIndex();
				switch (index) {
				case 0:// 停止当前监视
					if (panel.getActiveChannelCombo().getSelectedIndex() > 0) {
						MediaBean m = (MediaBean) panel.getActiveChannelCombo()
								.getSelectedItem();
						panel.stopMonitor(m);
					}
					dispose();
					break;
				case 1:// 停止所有监视
					if (panel.getActiveChannelCombo().getSelectedIndex() > 0) {
						panel.stopAllMonitors();
						panel.getActiveChannelCombo().setSelectedIndex(0);
					}
					dispose();
					break;
				}
			}
		});
		pack();
	}
}
