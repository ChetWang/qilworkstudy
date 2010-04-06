/*
 * ChannelMonitorPanel.java
 *
 * Created on __DATE__, __TIME__
 */
package com.nci.ums.jmx.desktop.panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
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

import com.jidesoft.swing.JideSplitButton;
import com.nci.ums.jmx.ConsoleUtil;
import com.nci.ums.jmx.desktop.DesktopConsole;
import com.nci.ums.jmx.desktop.HotKey;
import com.nci.ums.jmx.desktop.JMXLogger;
import com.nci.ums.jmx.desktop.bean.SwingWorker;
import com.nci.ums.media.MediaBean;
//import com.nci.ums.util.Res;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Qil.Wong
 */
public class ChannelMonitorPanel extends javax.swing.JPanel implements HotKey {

	private DesktopConsole parent;
	private Map showChartThreadMap;

	// private Timer twoDayCleanTimer;
	private static int TWODAY = 2 * 24 * 60 * 60 * 1000;

	// private static int TWODAY = 5*1000;
	public ChannelMonitorPanel() {
		initComponents();
	}

	/**
	 * ��ʼ������
	 * 
	 * @param parent
	 *            ������
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
		
//		Properties props = new Properties();
//		try {
//			InputStream is = getClass().getResourceAsStream(
//					"/resources/media.props");
//			props.load(is);
//			is.close();
//			;
//		} catch (IOException e) {
//			JMXLogger.logExceptionTrace(e);
//		}

		JMenuItem stopCurrentItem = new JMenuItem("ֹͣ��ǰ����");
		JMenuItem stopAllItem = new JMenuItem("ֹͣ���м���");
		stopAllMonitorsBtn.setText("ֹͣ����");
		stopAllMonitorsBtn.setSize(55, 35);
		stopAllMonitorsBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
		stopAllMonitorsBtn.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/com/nci/ums/jmx/desktop/icons/stop.png")));
		//Ϊֹͣ��ť��Ӹ����¼�
		stopAllMonitorsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getActiveChannelCombo().getSelectedIndex() > 0) {
					MediaBean m = (MediaBean) getActiveChannelCombo()
							.getSelectedItem();
					stopMonitor(m);
				}
			}
		});
		stopCurrentItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (getActiveChannelCombo().getSelectedIndex() > 0) {
					MediaBean m = (MediaBean) getActiveChannelCombo()
							.getSelectedItem();
					stopMonitor(m);
				}
			}
		});
		stopAllItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopAllMonitors();
				getActiveChannelCombo().setSelectedIndex(0);
			}
		});

		stopAllMonitorsBtn.add(stopCurrentItem);
		stopAllMonitorsBtn.add(stopAllItem);
	}

	/**
	 * ��ӿ�ݼ�
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
	 * ������������״̬����������������ʾ��
	 * 
	 */
	public void initActiveChannels() {
		ArrayList activeChannels = this.getActiveChannels();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(ConsoleUtil.getLocaleString("Select_One"));
		Object currentSelect = activeChannelCombo.getModel().getSelectedItem();
		for (int i = 0; i < activeChannels.size(); i++) {
			MediaBean bean = (MediaBean) activeChannels.get(i);
			model.addElement(bean);
		}
		activeChannelCombo.setModel(model);
		activeChannelCombo.setSelectedItem(currentSelect);
	}

	/**
	 * �ڽ�����������л��������Ϣ
	 */
	public void clearActiveChannels() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(ConsoleUtil.getLocaleString("Select_One"));
		activeChannelCombo.setModel(model);
	}

	/**
	 * ��ȡ�״̬������������ϴ������е���������ǰ״̬��ֹͣ�ģ�Ӧ�ý���������activeChannelCombo��
	 * showChartThreadMap���Ƴ�
	 * 
	 * @return �����������
	 */
	private ArrayList getActiveChannels() {
		List channels = parent.getChannelPanel().getOriginalChannels();
		ArrayList activeChannels = new ArrayList();
		if (channels != null) {
			for (int i = 0; i < channels.size(); i++) {
				MediaBean media = (MediaBean) channels.get(i);
				// ����ǻ״̬������
				if (media.getMediaStatus() == MediaBean.STATUS_RUNNING) {
					activeChannels.add(media);
				} else {// �����ֹͣ״̬������
					if (this.showChartThreadMap.containsKey(media.getMediaID()
							)) {
						// ((ChartShowThread)
						// showChartThreadMap.get(media.getMediaID() +
						// media.getMediaType())).pleaseStop();
						showChartThreadMap.remove(media.getMediaID());
						// if (media.getMediaID().equals(nciMedia_ID) ||
						// media.getMediaID().equals(nciV3Media_ID)) {
						// ((ChartShowThread)
						// showChartThreadMap.get(media.getMediaID() +
						// MediaBean.TYPE_CMSMS_IN)).pleaseStop();
						// showChartThreadMap.remove(media.getMediaID() +
						// MediaBean.TYPE_CMSMS_IN);
						// }
					}
				}
			}
		}
		return activeChannels;
	}

	/**
	 * ��ʼ��ʵʱ��ʾ��ͼ��
	 * 
	 * @param media
	 *            ��Ӧ������
	 * @return Vector������TimeSeries�����ChartPanel����������Ӧ��ʾ��Ч��
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
	 * ��������ϵ��������
	 * 
	 * @param container
	 *            ָ��������
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
							)) {
						ChartShowThread t = (ChartShowThread) showChartThreadMap
								.get(media.getMediaID());
						rootChartPanel.add(t.getChartPanel());
						t.getChartPanel().updateUI();
					} else {
						parent.getProgressBar().setIndeterminate(true);
						Vector chartPanelVector = initChart(media);
						ChartShowThread t = new ChartShowThread(media,
								chartPanelVector);
						showChartThreadMap.put(media.getMediaID(), t);
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
					// t.start();
					parent.getProgressBar().setIndeterminate(false);
				}
			}
		};
		worker.start();
	}


	// /**
	// * ÿ��һ��ʱ��󣬾�Ҫ���ͼ����ʾ���ݣ��������׵����ڴ治��������Ӱ������
	// */
	// private void clearTimeSeries() {
	// SwingWorker worker = new SwingWorker() {
	//
	// public Object construct() {
	// Iterator it = showChartThreadMap.values().iterator();
	// while (it.hasNext()) {
	// ChartShowThread t = (ChartShowThread) it.next();
	// t.getTimeseries().clear();
	// }
	// // ͼƬ��ʾ��ռ�ڴ�ƫ��������ÿ�ν���ʱ��Ҫǿ�ƻ���һ��
	// System.gc();
	// return null;
	// }
	// };
	// worker.start();
	// }
	/**
	 * ֹͣ���м���
	 */
	public void stopAllMonitors() {
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				removeCompFromContainer(rootChartPanel);
				showChartThreadMap.clear();
				// ͼƬ��ʾ��ռ�ڴ�ƫ��������ÿ�ν���ʱ��Ҫǿ�ƻ���һ��
				System.gc();
				return null;
			}
		};
		worker.start();
	}

	public void stopMonitor(MediaBean media) {
		showChartThreadMap.remove(media.getMediaID());
		removeCompFromContainer(rootChartPanel);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		activeChannelCombo = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		rootChartPanel = new javax.swing.JPanel();
		stopAllMonitorsBtn = new JideSplitButton();

		activeChannelCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "1", "2" }));

		jLabel1.setText("��ѡ���������:");

		rootChartPanel
				.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		rootChartPanel.setLayout(new java.awt.BorderLayout());

		stopAllMonitorsBtn.setText("Stop All Monitors");
		stopAllMonitorsBtn
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						stopAllMonitorBtnActionPerformed(evt);
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
																533,
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
																				0,
																				220,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				stopAllMonitorsBtn)
																		.add(
																				67,
																				67,
																				67)))
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
	}// </editor-fold>//GEN-END:initComponents

	private void stopAllMonitorBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:stopAllMonitorBtnActionPerformed

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
		// StopDialog dialog = new StopDialog(this);
		// dialog.setModal(false);
		// dialog.setLocation(stopAllMonitorsBtn.getLocationOnScreen().x,
		// stopAllMonitorsBtn.getLocationOnScreen().y +
		// stopAllMonitorsBtn.getHeight());
		// // dialog.setLocationRelativeTo(stopAllMonitorsBtn);
		// dialog.setVisible(true);
	}// GEN-LAST:stopAllMonitorBtnActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JComboBox activeChannelCombo;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel rootChartPanel;
	private JideSplitButton stopAllMonitorsBtn;

	// End of variables declaration//GEN-END:variables

	public class ChartShowThread {

		private ChartPanel chartPanel;
		private TimeSeries timeseries;

		public ChartShowThread(MediaBean media, Vector chartPanelVector) {

			this.chartPanel = (ChartPanel) chartPanelVector.get(1);
			this.timeseries = (TimeSeries) chartPanelVector.get(0);
			timeseries.setMaximumItemAge(TWODAY);
		}

		// public void run() {
		// while (runningFlag) {
		// // Ϊ��֤ʵʱЧ������ǰʱ��Ӧ�������������趨Ϊ3��
		// long currentLong =
		// Long.parseLong(ConsoleUtil.getCurrentTimeStr("yyyyMMddHHmmss")) - 3;
		// showChart(media, currentLong);
		// try {
		// sleep(TIME_DURATION * 1000);
		// } catch (InterruptedException e) {
		// Res.logExceptionTrace(e);
		// }
		// }
		// Res.log(Res.INFO, this.getName() + "�˳�");
		// }

		// long currentMill = 20071205085245L;
		/**
		 * ��ʾ��̬ͳ��ͼ��
		 * 
		 * @param media
		 *            ָ��������
		 */
		public void showChart(int count) {
			timeseries.add(new Millisecond(), count);
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
