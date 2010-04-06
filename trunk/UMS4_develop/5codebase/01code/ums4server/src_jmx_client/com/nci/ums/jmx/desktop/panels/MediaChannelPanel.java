/*
 * MediaChannelPanel.java
 *
 * Created on __DATE__, __TIME__
 */
package com.nci.ums.jmx.desktop.panels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import com.nci.ums.jmx.ConsoleUtil;
import com.nci.ums.jmx.desktop.DesktopConsole;
import com.nci.ums.jmx.desktop.HotKey;
import com.nci.ums.jmx.desktop.bean.CellKeyObj;
import com.nci.ums.jmx.desktop.bean.SwingWorker;
import com.nci.ums.jmx.desktop.bean.tablesorter.DefaultSimpleTableSortRenderer;
import com.nci.ums.media.MediaBean;

/**
 * 
 * @author Qil.Wong
 */
public class MediaChannelPanel extends javax.swing.JPanel implements HotKey {

	private JComboBox statusCombo = new JComboBox();
	private DesktopConsole parent;
	private List originalChannels = new ArrayList();

	// private Timer refreshTimer;

	public MediaChannelPanel() {
		initComponents();
	}

	/**
	 * Creates new form MediaChannelPanel
	 * 
	 * @throws SQLException
	 */
	public MediaChannelPanel(DesktopConsole parent) throws SQLException {
		this.parent = parent;
		initComponents();
		refreshBtn.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/refresh.gif")));
		applyBtn.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/save.png")));
		statusCombo.setModel(new DefaultComboBoxModel(new String[] {
				MediaBean.STATUS_RUNNING_String,
				MediaBean.STATUS_STOPPED_String }));
		channelTable.getColumnModel().getColumn(3).setCellEditor(
				new DefaultCellEditor(statusCombo));
		new DefaultSimpleTableSortRenderer(this.channelTable).initSortHeader();
		// iniTimer();
	}

	// private void iniTimer() {
	// refreshTimer = new Timer();
	// refreshTimer.schedule(new TimerTask() {
	// public void run() {
	// refresh();
	// }
	// }, 120 * 1000, 120 * 1000);
	//
	// }

	public void addHotKey() {
		applyBtn.setMnemonic(java.awt.event.KeyEvent.VK_A);
		refreshBtn.setMnemonic(java.awt.event.KeyEvent.VK_R);
	}

	// /**
	// * 显示所有的渠道
	// *
	// */
	// public boolean showChannels() {
	// boolean flag = true;
	// originalChannels.clear();
	// List channels = ConsoleUtil.getChannels();
	// if (channels.size() == 0) // Actually，Exception happened,server
	// // starting
	// // failed
	// {
	// return false;
	// }
	// this.showChannels(channels);
	// this.applyBtn.setEnabled(true);
	// this.refreshBtn.setEnabled(true);
	// return flag;
	// }

	public void showChannels(List channels) {
		originalChannels = channels;
		DefaultTableModel model = (DefaultTableModel) channelTable.getModel();
		for (int i = 0; i < originalChannels.size(); i++) {
			MediaBean media = (MediaBean) originalChannels.get(i);
			CellKeyObj c3 = new CellKeyObj();
			CellKeyObj c4 = new CellKeyObj();
			c3.setKeyObj(new Integer(media.getMediaType()));
			if (media.getMediaType() == 0) {
				c3.setName(MediaBean.TYPE_IN_String);
			} else {
				c3.setName(MediaBean.TYPE_OUT_String);
			}
			c4.setKeyObj(new Integer(media.getMediaStatus()));
			if (media.getMediaStatus() == MediaBean.STATUS_RUNNING) {
				c4.setName(MediaBean.STATUS_RUNNING_String);
			} else {
				c4.setName(MediaBean.STATUS_STOPPED_String);
			}
			Object[] rowData = new Object[] { media.getMediaID(),
					media.getMediaName(), c3, c4 };
			model.addRow(rowData);// object[]
		}
		reset(true);
		DesktopConsole.getInstance().getMonitorPanel().initActiveChannels();
	}

	public void reset(boolean started) {
		refreshBtn.setEnabled(started);
		applyBtn.setEnabled(started);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		channelTable = new javax.swing.JTable();
		applyBtn = new javax.swing.JButton();
		refreshBtn = new javax.swing.JButton();

		channelTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {}, new String[] {
						ConsoleUtil.getLocaleString("Channel_ID"),
						ConsoleUtil.getLocaleString("Channel_Name"),
						ConsoleUtil.getLocaleString("Channel_Type"),
						ConsoleUtil.getLocaleString("Channel_Status") }) {

			boolean[] canEdit = new boolean[] { false, false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(channelTable);

		applyBtn.setText(ConsoleUtil.getLocaleString("Apply"));
		applyBtn.setEnabled(false);
		applyBtn.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				applyBtnActionPerformed(evt);
			}
		});

		refreshBtn.setText(ConsoleUtil.getLocaleString("Refresh"));
		refreshBtn.setEnabled(false);
		refreshBtn.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshBtnActionPerformed(evt);
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
																jScrollPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																366,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				refreshBtn,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				81,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED,
																				204,
																				Short.MAX_VALUE)
																		.add(
																				applyBtn)))
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { applyBtn, refreshBtn },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup().addContainerGap().add(
						jScrollPane1,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180,
						Short.MAX_VALUE).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(
						layout.createParallelGroup(
								org.jdesktop.layout.GroupLayout.BASELINE).add(
								applyBtn).add(refreshBtn)).addContainerGap()));
	}// </editor-fold>

	private void applyBtnActionPerformed(java.awt.event.ActionEvent evt) {
		ConsoleUtil.stopEditingCells(this.channelTable);
		SwingWorker worker = new SwingWorker() {

			public Object construct() {
				parent.getProgressBar().setIndeterminate(true);
				applyBtn.setEnabled(false);
				refreshBtn.setEnabled(false);
				parent.getStopButton().setEnabled(false);

				final ArrayList channelTobeStart = getChannelsTobeStart();
				final ArrayList channelTobeStop = getChannelsTobeStop();
				for (int i = 0; i < channelTobeStart.size(); i++) {
					MediaBean media = (MediaBean) channelTobeStart.get(i);
					parent.getUMSMBean().startMedia(media);
					// WSUtil.startChannel(media);
				}
				for (int i = 0; i < channelTobeStop.size(); i++) {
					MediaBean media = (MediaBean) channelTobeStop.get(i);
					parent.getUMSMBean().stopMedia(media);
				}
				return null;
			}

			public void finished() {
				clearChannels();
				showChannels(parent.getUMSMBean().getMediaList());
				applyBtn.setEnabled(true);
				refreshBtn.setEnabled(true);
				parent.getProgressBar().setIndeterminate(false);
				parent.getStopButton().setEnabled(true);

				parent.getMonitorPanel().removeCompFromContainer(
						parent.getMonitorPanel().getRootChartPanel());
				parent.getMonitorPanel().initActiveChannels();
			}
		};
		worker.start();
	}

	private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {
		refresh2();
	}

	private void refresh2() {
		if (DesktopConsole.getInstance().isServerStarted()) {
			ConsoleUtil.stopEditingCells(this.channelTable);
			this.clearChannels();

			this.showChannels(parent.getUMSMBean().getMediaList());

		}
	}

	public void refresh(List channels) {
		if (DesktopConsole.getInstance().isServerStarted()) {
			ConsoleUtil.stopEditingCells(this.channelTable);
			this.clearChannels();
			this.showChannels(channels);
			reset(true);
		}
	}

	public void clearChannels() {
		DefaultTableModel model = (DefaultTableModel) channelTable.getModel();
		for (int i = model.getRowCount(); i > 0; i--) {
			model.removeRow(i - 1);
		}
		reset(false);
	}

	private ArrayList getChannelsTobeStart() {
		DefaultTableModel model = (DefaultTableModel) channelTable.getModel();
		ArrayList channelsTobeStart = new ArrayList();
		for (int i = 0; i < model.getRowCount(); i++) {
			MediaBean mediaPrevious = (MediaBean) originalChannels.get(i);
			for (int k = 0; k < model.getRowCount(); k++)
				if (mediaPrevious.getMediaStatus() == MediaBean.STATUS_STOPPED
						&& model.getValueAt(k, 3).toString().equals(
								MediaBean.STATUS_RUNNING_String)
						&& model.getValueAt(k, 0).toString().equals(
								mediaPrevious.getMediaID())) {
					channelsTobeStart.add(mediaPrevious);
				}
		}
		return channelsTobeStart;
	}

	private ArrayList getChannelsTobeStop() {
		DefaultTableModel model = (DefaultTableModel) channelTable.getModel();
		ArrayList channelsTobeStop = new ArrayList();
		for (int i = 0; i < model.getRowCount(); i++) {
			MediaBean mediaPrevious = (MediaBean) originalChannels.get(i);
			for (int k = 0; k < model.getRowCount(); k++)
				if (mediaPrevious.getMediaStatus() == MediaBean.STATUS_RUNNING
						&& model.getValueAt(k, 3).toString().equals(
								MediaBean.STATUS_STOPPED_String)
						&& model.getValueAt(k, 0).toString().equals(
								mediaPrevious.getMediaID())) {
					channelsTobeStop.add(mediaPrevious);
				}
		}
		return channelsTobeStop;
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton applyBtn;
	private javax.swing.JTable channelTable;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton refreshBtn;

	// End of variables declaration
	public List getOriginalChannels() {
		return originalChannels;
	}

	public void setOriginalChannels(ArrayList originalChannels) {
		this.originalChannels = originalChannels;
	}

	public JButton getRefreshBtn() {
		return refreshBtn;
	}

	public JButton getApplyBtn() {
		return applyBtn;
	}
}
