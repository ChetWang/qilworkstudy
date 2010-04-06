/*
 * MsgDefinePanel.java
 *
 * Created on 2007��11��6��, ����9:59
 */
package com.ums.demo.panels;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;

import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.AppInfoV3;
import com.ums.db.UMSConn;
import com.ums.demo.util.NumOnlyDocument;
import com.ums.demo.util.UMSDemoUtil;

/**
 * 
 * @author Qil.Wong
 */
public class MsgDefinePanel extends javax.swing.JPanel {

	private int receiverIDType;
	private AppInfoV3 app;

	/** Creates new form MsgDefinePanel */
	public MsgDefinePanel() {
		initComponents();
		// jTabbedPane1.setTabPlacement(JTabbedPane.LEFT);
		jTabbedPane1.addTab("SocketTransmit", new TransmitSocketPanel());
		attachList.setSelectionMode(0);
		this.sendTimesField.setDocument(new NumOnlyDocument());
	}

	public void iniOutMediaChannel() {
		mediaIDCombo.addItem(new String("--Choose--"));
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(UMSConn.getPoolName());
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT * FROM MEDIA WHERE TYPE=1 AND STATUSFLAG =0");
			while (rs.next()) {
				mediaIDCombo.addItem(new MediaBean(rs.getString("media_id"), rs
						.getString("mediaName")));
			}
			rs.close();
		} catch (Exception e) {
			Logger.getLogger(MsgDefinePanel.class.getName()).log(Level.SEVERE,
					null, e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger.getLogger(MsgDefinePanel.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	public void iniFeeServiceNOCombo() {
		feeServiceNOCombo.addItem(new String("--Choose--"));
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(UMSConn.getPoolName());
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT FEESERVICENO FROM FEE_V3");
			while (rs.next()) {
				feeServiceNOCombo.addItem(rs.getString(1));
			}
			rs.close();
		} catch (Exception e) {
			Logger.getLogger(MsgDefinePanel.class.getName()).log(Level.SEVERE,
					null, e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger.getLogger(MsgDefinePanel.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	public BasicMsg getBasicMsg() {
		BasicMsg basic = new BasicMsg();
		basic.setReceivers(this.getReceivers());
		if (ackCheckBox.isSelected()) {
			basic.setAck(BasicMsg.UMSMsg_ACK);
		} else {
			basic.setAck(BasicMsg.UMSMsg_ACK_NO);
		}
		basic.setAppSerialNO(appSerialField.getText());
		basic.setCompanyID(app.getCompanyID());
		if (sendDirectlyCheckBox.isSelected()) {
			basic.setDirectSendFlag(BasicMsg.BASICMSG_DIRECTSEND_YES);
			basic.setMediaID(((MediaBean) mediaIDCombo.getSelectedItem())
					.getMediaID());
		} else {
			basic.setDirectSendFlag(BasicMsg.BASICMSG_DIRECTSEND_NO);
		}
		basic.setMsgAttachment(this.getAttachments());
		basic.setMsgContent(new MsgContent(subjectField.getText(), messageArea
				.getText()));
		if (needReplyCheckBox.isSelected()) {
			basic.setNeedReply(BasicMsg.BASICMSG_NEEDREPLY_YES);
		} else {
			basic.setNeedReply(BasicMsg.BASICMSG_NEEDREPLY_NO);
		}
		basic.setSender(this.getSender());
		basic.setPriority(priorityCombo.getSelectedIndex());
		basic.setReplyDestination(replyDestField.getText());
		basic.setContentMode(this.getMsgCotentMode());
		basic.setServiceID(serviceCombo.getSelectedItem().toString());
		return basic;
	}

	private int getMsgCotentMode() {
		int mode = 15;
		switch (getContentModeCombo().getSelectedIndex()) {
		case 0:
			mode = BasicMsg.BASICMSG_CONTENTE_MODE_GBK;
			break;
		case 1:
			mode = BasicMsg.BASICMSG_CONTENTE_MODE_8859_1;
			break;
		case 2:
			mode = BasicMsg.BASICMSG_CONTENTE_MODE_PDU;
			break;
		case 3:
			mode = BasicMsg.BASICMSG_CONTENTE_MODE_UNICODE_BIG;
			break;
		}

		return mode;
	}

	private Participant getSender() {
		Participant p = new Participant();
		// switch (receiverIDType) {
		// case Participant.PARTICIPANT_ID_EMAIL:
		// p.setIDType(receiverIDType);
		// p.setParticipantID("ums@nci.com.cn");
		// p.setParticipantType(Participant.PARTICIPANT_MSG_FROM);
		// break;
		// case Participant.PARTICIPANT_ID_MOBILE:
		// p.setIDType(receiverIDType);
		// p.setParticipantID("088571242" +
		// serviceCombo.getSelectedItem().toString());
		// p.setParticipantType(Participant.PARTICIPANT_MSG_FROM);
		// break;
		// case Participant.PARTICIPANT_ID_LCS:
		// p.setIDType(receiverIDType);
		// p.setParticipantID("NCI Robot" +
		// serviceCombo.getSelectedItem().toString());
		// p.setParticipantType(Participant.PARTICIPANT_MSG_FROM);
		// break;
		// default:
		// JOptionPane.showConfirmDialog(this, "Please choose address type!",
		// "Sender",
		// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		// break;
		// }
		p.setIDType(receiverIDType);
		p.setParticipantID(serviceCombo.getSelectedItem().toString());
		p.setParticipantType(Participant.PARTICIPANT_MSG_FROM);
		return p;
	}

	private Participant[] getReceivers() {
		String s = this.receiversField.getText().trim();
		ArrayList<Participant> arr = new ArrayList<Participant>();
		while (s.indexOf(";") > 0) {
			Participant p = new Participant();
			p.setParticipantID(s.substring(0, s.indexOf(";")));
			p.setIDType(this.getReceiverIDType());
			p.setParticipantType(Participant.PARTICIPANT_MSG_TO);
			arr.add(p);
			s = s.substring(s.indexOf(";") + 1);
		}
		Participant[] receivers = new Participant[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			receivers[i] = arr.get(i);
		}
		return receivers;
	}

	private MsgAttachment[] getAttachments() {
		MsgAttachment[] atts = new MsgAttachment[attachList.getModel()
				.getSize()];
		for (int i = 0; i < attachList.getModel().getSize(); i++) {
			InputStream in = null;
			try {
				File file = new File(attachList.getModel().getElementAt(i)
						.toString());
				in = new FileInputStream(file);
				byte[] b = new byte[in.available()];
				in.read(b);
				atts[i] = new MsgAttachment(file.getName(), UMSDemoUtil
						.getBase64Encoder().encode(b));
				// atts[i] = new
				// MsgAttachment(UMSDemoUtil.getBase64Encoder().encode(file.getName().getBytes("gb2312")),
				// UMSDemoUtil.getBase64Encoder().encode(b));
			} catch (IOException ex) {
				Logger.getLogger(MsgDefinePanel.class.getName()).log(
						Level.SEVERE, null, ex);
			} finally {
				try {
					in.close();
				} catch (IOException ex) {
					Logger.getLogger(MsgDefinePanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}
		return atts;
	}

	public void iniAppServices() {
		try {
			Properties p = new Properties();
			FileInputStream fis = new FileInputStream(new File(System
					.getProperty("user.dir")
					+ "/conf/services"));
			p.load(fis);
			fis.close();
			Iterator it = p.values().iterator();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			while(it.hasNext()){
				model.addElement(it.next());
			}			
			serviceCombo.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearMsgs() {
		this.receiversField.setText("");
		this.appSerialField.setText("");
		this.priorityCombo.setSelectedIndex(0);
		this.mediaIDCombo.setSelectedIndex(0);
		this.mediaIDCombo.setEnabled(false);
		this.sendDirectlyCheckBox.setSelected(false);
		this.replyDestField.setText("");
		this.needReplyCheckBox.setSelected(false);
		this.feeServiceNOCombo.setSelectedIndex(0);
		this.subjectField.setText("");
		this.messageArea.setText("");
		this.getResultLabel().setText("");
		this.attachList.setModel(new AbstractListModel() {

			public int getSize() {
				return 0;
			}

			public Object getElementAt(int index) {
				return null;
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		receiversField = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		appSerialField = new javax.swing.JTextField();
		sendDirectlyCheckBox = new javax.swing.JCheckBox();
		mediaIDLabel = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		priorityCombo = new javax.swing.JComboBox();
		jLabel6 = new javax.swing.JLabel();
		replyDestField = new javax.swing.JTextField();
		needReplyCheckBox = new javax.swing.JCheckBox();
		jLabel7 = new javax.swing.JLabel();
		ackCheckBox = new javax.swing.JCheckBox();
		serviceCombo = new javax.swing.JComboBox();
		jLabel9 = new javax.swing.JLabel();
		mediaIDCombo = new javax.swing.JComboBox();
		feeServiceNOCombo = new javax.swing.JComboBox();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		attachList = new javax.swing.JList();
		addAttBtn = new javax.swing.JButton();
		removeAttBtn = new javax.swing.JButton();
		receivePanel = new com.ums.demo.panels.ReceivePanel();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		messageArea = new javax.swing.JTextArea();
		jLabel2 = new javax.swing.JLabel();
		subjectField = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		contentModeCombo = new javax.swing.JComboBox();
		sendBtn = new javax.swing.JButton();
		clearBtn = new javax.swing.JButton();
		jLabel8 = new javax.swing.JLabel();
		sendTimesField = new javax.swing.JTextField();
		resultLabel = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();

		jLabel1.setText("To:");

		jLabel4.setText("AppSerial NO.:");

		sendDirectlyCheckBox.setText("Send Directly");
		sendDirectlyCheckBox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						sendDirectlyCheckBoxActionPerformed(evt);
					}
				});

		mediaIDLabel.setText("Out Media ID:");

		jLabel5.setText("Priority:");

		priorityCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Normal", "Urgent" }));

		jLabel6.setText("Reply Dest:");

		needReplyCheckBox.setText("Need Reply");

		jLabel7.setText("Fee Service NO.:");

		ackCheckBox.setText("Need Acknowledge");
		ackCheckBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ackCheckBoxActionPerformed(evt);
			}
		});

		jLabel9.setText("Service ID:");

		mediaIDCombo.setEnabled(false);

		org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout
				.setHorizontalGroup(jPanel3Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel3Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(jLabel7).add(
																jLabel6).add(
																jLabel4).add(
																mediaIDLabel)
														.add(jLabel1))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(feeServiceNOCombo,
																0, 186,
																Short.MAX_VALUE)
														.add(mediaIDCombo, 0,
																186,
																Short.MAX_VALUE)
														.add(
																receiversField,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																186,
																Short.MAX_VALUE)
														.add(
																appSerialField,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																186,
																Short.MAX_VALUE)
														.add(
																replyDestField,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																186,
																Short.MAX_VALUE))
										.add(33, 33, 33)
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(needReplyCheckBox)
														.add(
																jPanel3Layout
																		.createParallelGroup(
																				org.jdesktop.layout.GroupLayout.LEADING,
																				false)
																		.add(
																				sendDirectlyCheckBox)
																		.add(
																				jPanel3Layout
																						.createSequentialGroup()
																						.add(
																								jPanel3Layout
																										.createParallelGroup(
																												org.jdesktop.layout.GroupLayout.TRAILING)
																										.add(
																												jLabel9)
																										.add(
																												jLabel5))
																						.addPreferredGap(
																								org.jdesktop.layout.LayoutStyle.RELATED)
																						.add(
																								jPanel3Layout
																										.createParallelGroup(
																												org.jdesktop.layout.GroupLayout.LEADING)
																										.add(
																												serviceCombo,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																												63,
																												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																										.add(
																												priorityCombo,
																												0,
																												0,
																												Short.MAX_VALUE))))
														.add(ackCheckBox))
										.addContainerGap()));
		jPanel3Layout
				.setVerticalGroup(jPanel3Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel3Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel1)
														.add(
																receiversField,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(
																serviceCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(jLabel9))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel4)
														.add(
																appSerialField,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(jLabel5)
														.add(
																priorityCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(mediaIDLabel)
														.add(
																sendDirectlyCheckBox)
														.add(
																mediaIDCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel6)
														.add(needReplyCheckBox)
														.add(
																replyDestField,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel3Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel7)
														.add(
																ackCheckBox,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																22,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(
																feeServiceNOCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jTabbedPane1.addTab("Basic Message", jPanel3);

		jScrollPane2.setViewportView(attachList);

		addAttBtn.setText("Add");
		addAttBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addAttBtnActionPerformed(evt);
			}
		});

		removeAttBtn.setText("Remove");
		removeAttBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAttBtnActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(
				jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout
				.setHorizontalGroup(jPanel4Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jPanel4Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																jScrollPane2,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																452,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																jPanel4Layout
																		.createSequentialGroup()
																		.add(
																				removeAttBtn)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				addAttBtn)))
										.addContainerGap()));

		jPanel4Layout.linkSize(new java.awt.Component[] { addAttBtn,
				removeAttBtn }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		jPanel4Layout
				.setVerticalGroup(jPanel4Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jScrollPane2,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												97, Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel4Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(removeAttBtn).add(
																addAttBtn))
										.addContainerGap()));

		jTabbedPane1.addTab("Attachments", jPanel4);
		jTabbedPane1.addTab("Received Msg", receivePanel);

		jPanel2.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Type Your Message Here:"));

		messageArea.setColumns(20);
		messageArea.setLineWrap(true);
		messageArea.setRows(5);
		jScrollPane1.setViewportView(messageArea);

		jLabel2.setText("Subject:");

		jLabel3.setText("Content:");

		jLabel10.setText("Encoding:");

		contentModeCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "GBK", "8859-1", "PDU", "UnicodeBig" }));

		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel2Layout
										.createSequentialGroup()
										.add(
												jPanel2Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(jLabel2).add(
																jLabel3))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel2Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																jPanel2Layout
																		.createSequentialGroup()
																		.add(
																				subjectField,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				264,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				jLabel10)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				contentModeCombo,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
														.add(
																jScrollPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																413,
																Short.MAX_VALUE))));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel2Layout
										.createSequentialGroup()
										.add(
												jPanel2Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel2)
														.add(
																contentModeCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(jLabel10)
														.add(
																subjectField,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel2Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																jPanel2Layout
																		.createSequentialGroup()
																		.add(
																				jLabel3)
																		.addContainerGap())
														.add(
																jScrollPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																79,
																Short.MAX_VALUE))));

		sendBtn.setText("Send");

		clearBtn.setText("Clear");
		clearBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearBtnActionPerformed(evt);
			}
		});

		jLabel8.setText("Times:");

		jLabel12.setText("Result:");

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
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
																jPanel2,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																jTabbedPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																477,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																layout
																		.createSequentialGroup()
																		.add(
																				clearBtn,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				65,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				jLabel8)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				sendTimesField,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				59,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED,
																				100,
																				Short.MAX_VALUE)
																		.add(
																				jLabel12)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				resultLabel,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				86,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.UNRELATED)
																		.add(
																				sendBtn)))
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { clearBtn, sendBtn },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.add(
												jTabbedPane1,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												174, Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(
												jPanel2,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(clearBtn)
														.add(sendBtn)
														.add(jLabel8)
														.add(
																sendTimesField,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(
																resultLabel,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																19,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
														.add(jLabel12))
										.addContainerGap()));
	}// </editor-fold>//GEN-END:initComponents

	private void sendDirectlyCheckBoxActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sendDirectlyCheckBoxActionPerformed
		if (sendDirectlyCheckBox.isSelected()) {
			this.mediaIDCombo.setEnabled(true);
		} else {
			this.mediaIDCombo.setEnabled(false);
			this.mediaIDCombo.setSelectedIndex(0);
		}
	}// GEN-LAST:event_sendDirectlyCheckBoxActionPerformed

	private void removeAttBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeAttBtnActionPerformed
		String[] strings = new String[attachList.getModel().getSize()];
		for (int i = 0; i < attachList.getModel().getSize(); i++) {
			strings[i] = attachList.getModel().getElementAt(i).toString();
		}
		final String[] newStrings = new String[attachList.getModel().getSize() - 1];
		for (int i = 0; i < attachList.getSelectedIndex(); i++) {
			newStrings[i] = strings[i];
		}
		for (int i = attachList.getSelectedIndex(); i < attachList.getModel()
				.getSize() - 1; i++) {
			newStrings[i] = strings[i + 1];
		}
		attachList.setModel(new AbstractListModel() {

			public int getSize() {
				return newStrings.length;
			}

			public Object getElementAt(int index) {
				return newStrings[index];
			}
		});
	}// GEN-LAST:event_removeAttBtnActionPerformed

	private void addAttBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addAttBtnActionPerformed
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(this);

		File[] files = chooser.getSelectedFiles();
		final String[] strings = new String[attachList.getModel().getSize()
				+ files.length];
		for (int i = 0; i < attachList.getModel().getSize(); i++) {
			strings[i] = attachList.getModel().getElementAt(i).toString();
		}
		for (int i = 0; i < files.length; i++) {
			strings[attachList.getModel().getSize() + i] = files[i]
					.getAbsolutePath();
		}
		attachList.setModel(new AbstractListModel() {

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int index) {
				return strings[index];
			}
		});

	}// GEN-LAST:event_addAttBtnActionPerformed

	private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_clearBtnActionPerformed
		clearMsgs();
	}// GEN-LAST:event_clearBtnActionPerformed

	private void ackCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ackCheckBoxActionPerformed
		// if(ackCheckBox.isSelected()){
		// ackField.setVisible(true);
		// ackLabel.setVisible(true);
		// }else{
		// ackField.setVisible(false);
		// ackLabel.setVisible(false);
		// }
	}// GEN-LAST:event_ackCheckBoxActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox ackCheckBox;
	private javax.swing.JButton addAttBtn;
	private javax.swing.JTextField appSerialField;
	private javax.swing.JList attachList;
	private javax.swing.JButton clearBtn;
	private javax.swing.JComboBox contentModeCombo;
	private javax.swing.JComboBox feeServiceNOCombo;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JComboBox mediaIDCombo;
	private javax.swing.JLabel mediaIDLabel;
	private javax.swing.JTextArea messageArea;
	private javax.swing.JCheckBox needReplyCheckBox;
	private javax.swing.JComboBox priorityCombo;
	private com.ums.demo.panels.ReceivePanel receivePanel;
	private javax.swing.JTextField receiversField;
	private javax.swing.JButton removeAttBtn;
	private javax.swing.JTextField replyDestField;
	private javax.swing.JLabel resultLabel;
	private javax.swing.JButton sendBtn;
	private javax.swing.JCheckBox sendDirectlyCheckBox;
	private javax.swing.JTextField sendTimesField;
	private javax.swing.JComboBox serviceCombo;
	private javax.swing.JTextField subjectField;

	// End of variables declaration//GEN-END:variables
	public ReceivePanel getReceivePanel() {
		return receivePanel;
	}

	public javax.swing.JTextField getReceiversField() {
		return receiversField;
	}

	public void setReceiversField(javax.swing.JTextField receiversField) {
		this.receiversField = receiversField;
	}

	public int getReceiverIDType() {
		return receiverIDType;
	}

	public void setReceiverIDType(int receiverIDType) {
		this.receiverIDType = receiverIDType;
	}

	public javax.swing.JButton getSendBtn() {
		return sendBtn;
	}

	public void setSendBtn(javax.swing.JButton sendBtn) {
		this.sendBtn = sendBtn;
	}

	public AppInfoV3 getApp() {
		return app;
	}

	public void setApp(AppInfoV3 app) {
		this.app = app;
	}

	public javax.swing.JTextField getSendTimesField() {
		return sendTimesField;
	}

	public void setSendTimesField(javax.swing.JTextField sendTimesField) {
		this.sendTimesField = sendTimesField;
	}

	public javax.swing.JComboBox getContentModeCombo() {
		return contentModeCombo;
	}

	public void setContentModeCombo(javax.swing.JComboBox contentModeCombo) {
		this.contentModeCombo = contentModeCombo;
	}

	public javax.swing.JLabel getResultLabel() {
		return resultLabel;
	}

	public void setResultLabel(javax.swing.JLabel resultLabel) {
		this.resultLabel = resultLabel;
	}
}

class MediaBean {

	private String mediaName;
	private String mediaID;

	public MediaBean() {
	}

	public MediaBean(String mediaID, String mediaName) {
		this.mediaName = mediaName;
		this.mediaID = mediaID;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaID() {
		return mediaID;
	}

	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}

	@Override
	public String toString() {
		return mediaName;
	}
}
