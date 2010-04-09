package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.message.MessageHandler;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.beans.desyer.WofoMessageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.org.WofoRoleBean;
import com.nci.domino.beans.org.WofoUnitBean;
import com.nci.domino.beans.org.WofoUserBean;
import com.nci.domino.beans.org.WofoVirtualRoleBean;
import com.nci.domino.components.WfComboBox.WfComboBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * ��Ϣ�������
 * 
 * @author Qil.Wong
 * 
 */
public class WfMessagePanel extends WfDialogPluginPanel {

	/**
	 * ������,ÿһ�еĵ�һ�д�ŵĶ����а���WofoMessageBean�����Ƿ�װ����MySenceObj����
	 */
	protected JTable sceneTable;

	private Document sceneDoc;

	public WfMessagePanel(WfDialog dialog) {
		super(dialog);
		panelName = "��Ϣ";
		init();
	}

	private void init() {
		sceneDoc = Functions.getXMLDocument(getClass().getResource(
				"/resources/message_scene.xml"));
		sceneTable = new JTable();
		sceneTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2
						&& evt.getButton() == MouseEvent.BUTTON1) {
					modifyScene();
				}
			}
		});
		sceneTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		sceneTable.setModel(new javax.swing.table.DefaultTableModel(null,
				new String[] { "����", "���ն���", "��Ϣ����", "����Ϣ����", "֪ͨ��������" }) {
			Class[] types = new Class[] { String.class, String.class,
					String.class, String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});
		WfOperControllerButtonPanel buttonPanel = new WfOperControllerButtonPanel(
				sceneTable, false);
		buttonPanel.getDelRowBtn().setText("�Ƴ�����");
		buttonPanel.getAddRowBtn().setText("��������");
		buttonPanel.getUpRowBtn().setVisible(false);
		buttonPanel.getDownRowBtn().setVisible(false);
		buttonPanel.getAddRowBtn().removeActionListener(
				buttonPanel.getAddRowBtn().getActionListeners()[0]);
		buttonPanel.getAddRowBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WofoMessageBean message = new WofoMessageBean(Functions
						.getUID());
				showAddSceneDialog(message);
			}
		});
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.insets = new Insets(4, 4, 4, 4);
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1;
		add(buttonPanel, cons);

		cons.gridy = 1;
		cons.weighty = 1;
		add(new JideScrollPane(sceneTable), cons);
	}

	/**
	 * ��ʾ�³����Ի���
	 * 
	 * @param message
	 *            ��Ϣ����
	 */
	private void showAddSceneDialog(WofoMessageBean message) {
		WfDialog d = new WfSceneInputDialog(dialog.getEditor(), "��Ϣ����", true);
		d.showWfDialog(message);
		if (d.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			d.getInputValues();
			insertOneRow(sceneTable.getModel().getRowCount(), message);
		}
	}

	/**
	 * �����еĳ����Ͻ����޸Ķ���
	 */
	private void modifyScene() {
		int row = sceneTable.getSelectedRow();
		DefaultTableModel model = (DefaultTableModel) sceneTable.getModel();

		MySceneObj scene = (MySceneObj) model.getValueAt(row, 0);
		WfDialog d = new WfSceneInputDialog(dialog.getEditor(), "��Ϣ����", true);
		d.showWfDialog(scene.message);
		if (d.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			d.getInputValues();
			model.removeRow(row);
			insertOneRow(row, scene.message);
		}
	}

	@Override
	public void reset() {
		DefaultTableModel model = (DefaultTableModel) sceneTable.getModel();
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}

	@Override
	public Serializable applyValues(Serializable value) {
		MessageHandler messageHandler = (MessageHandler) value;
		List<WofoMessageBean> messages = messageHandler.getMessages();
		DefaultTableModel model = (DefaultTableModel) sceneTable.getModel();
		messages.clear();
		for (int i = 0; i < model.getRowCount(); i++) {
			messages.add(((MySceneObj) model.getValueAt(i, 0)).message);
		}
		return value;
	}

	@Override
	public void setValues(Serializable value) {
		MessageHandler messageHandler = (MessageHandler) value;
		List<WofoMessageBean> messages = messageHandler.getMessages();
		for (int i = 0; i < messages.size(); i++) {
			WofoMessageBean message = messages.get(i);
			insertOneRow(i, message);
		}
	}

	/**
	 * �ڳ�����������һ��
	 * 
	 * @param message
	 */
	private void insertOneRow(int row, WofoMessageBean message) {
		DefaultTableModel model = (DefaultTableModel) sceneTable.getModel();
		model.insertRow(row, new Object[] { new MySceneObj(message),
				message.getAddressee(), message.getMessageTitle(),
				message.getMessageContent(), new MyNotifyObj(message) });
	}

	/**
	 * �������ԶԻ���
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class WfSceneInputDialog extends WfDialog {

		/**
		 * �����������
		 */
		WfComboBox sceneTypeCombo = new WfComboBox(false);
		/**
		 * ��Ϣ�������
		 */
		JTextField titleField = new JTextField();
		AbstractButton titleParamBtn = new WfSplitBtn("����");
		AbstractButton contentParamBtn = new WfSplitBtn("����");
		AbstractButton participantBtn = new WfSplitBtn("��Ա");
		/**
		 * ��Ϣ�������
		 */
		JTextArea contentArea = new JTextArea();
		/**
		 * �����������
		 */
		List<JCheckBox> typeCheckList = new ArrayList<JCheckBox>();
		/**
		 * ���ն����б�
		 */
		JList receiversList = new JList(new DefaultListModel());
		/**
		 * ����ѡ�񵯳��˵�
		 */
		private JidePopup argSelectPopup;

		/**
		 * ��������Աѡ����
		 */
		WfParticipantChooser chooser = new WfParticipantChooser(dialog
				.getEditor());

		public WfSceneInputDialog(WfEditor editor, String title, boolean modal) {
			super(editor, title, modal);
			defaultWidth = 400;
			defaultHeight = 350;
			setLocationRelativeTo(dialog);
		}

		public void clearContents() {
			super.clearContents();
			sceneTypeCombo.setSelectedIndex(0);
			titleField.setText("");
			contentArea.setText("");
			for (JCheckBox c : typeCheckList) {
				c.setSelected(false);
			}
			DefaultListModel model = (DefaultListModel) receiversList
					.getModel();
			model.clear();
		}

		public Serializable getInputValues() {
			WofoMessageBean message = (WofoMessageBean) super.getInputValues();
			message.getAddressee().clear();
			DefaultListModel receiversListModel = (DefaultListModel) receiversList
					.getModel();
			for (int i = 0; i < receiversListModel.size(); i++) {
				message.getAddressee().add(receiversListModel.getElementAt(i));
			}
			message.setMessageContent(contentArea.getText().trim());
			message.setMessageTitle(titleField.getText().trim());
			StringBuffer notifyTypes = new StringBuffer();
			for (JCheckBox c : typeCheckList) {
				if (c.isSelected()) {
					if (notifyTypes.length() != 0) {
						notifyTypes.append(",");
					}
					notifyTypes.append(((WfComboBean) c
							.getClientProperty("WfComboBean")).id);
				}
			}
			message.setNotifyType(notifyTypes.toString());
			message.setSceneType(((WfComboBean) sceneTypeCombo
					.getSelectedItem()).id);
			// ���������Ķ����id������
			message.setProcessObjId(((WofoBaseBean) (defalutValue)).getID());
			if (defalutValue instanceof WofoProcessBean) {
				message.setProcessObjType(WofoBaseBean.OBJ_TYPE_PROCESS);
			} else if (defalutValue instanceof WofoActivityBean) {
				message.setProcessObjType(WofoBaseBean.OBJ_TYPE_ACTIVITY);
			}
			return message;
		}

		public void setInputValues(Serializable value) {
			super.setInputValues(value);
			WofoMessageBean message = (WofoMessageBean) value;
			sceneTypeCombo.setSelectedItemByID(message.getSceneType());
			titleField.setText(message.getMessageTitle());
			contentArea.setText(message.getMessageContent());
			String[] notifyTypes = message.getNotifyType() == null ? null
					: message.getNotifyType().split(",");
			if (notifyTypes != null) {
				for (String type : notifyTypes) {
					for (JCheckBox c : typeCheckList) {
						// ÿ��JCheckList�ж���ǰ������һ��ClientProperty����WfComboBean�������ڴ�����JCheckBox������WfComboBean����
						WfComboBean comboBean = (WfComboBean) c
								.getClientProperty("WfComboBean");
						if (type.equals(comboBean.id)) {
							c.setSelected(true);
						}
					}
				}
			}
			DefaultListModel receiversListModel = (DefaultListModel) receiversList
					.getModel();
			for (int i = 0; i < message.getAddressee().size(); i++) {
				receiversListModel.addElement(message.getAddressee().get(i));
			}
		}

		/**
		 * ��ȡ������������������
		 * 
		 * @return
		 */
		private JPanel createSceneContentPanel() {
			// ��ȡ���ã�������Ϣ�����ĳ�������Ϣ�ķ�������

			NodeList scenes = sceneDoc.getDocumentElement()
					.getElementsByTagName("scene");

			for (int i = 0; i < scenes.getLength(); i++) {
				Element e = (Element) scenes.item(i);
				sceneTypeCombo.getWfComboModel().addElement(
						new WfComboBean(e.getAttribute("id"), e
								.getAttribute("value")));
			}
			NodeList types = sceneDoc.getDocumentElement()
					.getElementsByTagName("type");
			for (int i = 0; i < types.getLength(); i++) {
				Element e = (Element) types.item(i);
				WfComboBean b = new WfComboBean(e.getAttribute("id"), e
						.getAttribute("value"));
				JCheckBox c = new JCheckBox(b.value.toString());
				c.putClientProperty("WfComboBean", b);
				typeCheckList.add(c);
			}

			WfTextDocument messageTitleDoc = new WfTextDocument(256);
			WfTextDocument messageContentDoc = new WfTextDocument(1024);
			titleField.setDocument(messageTitleDoc);
			contentArea.setDocument(messageContentDoc);
			messageTitleDoc.setWfDialog(this);
			messageContentDoc.setWfDialog(this);
			// ��������ѡ���ı�
			combineArgsTextComp(titleParamBtn, titleField);
			combineArgsTextComp(contentParamBtn, contentArea);
			participantBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					showReceiverChooser();
				}
			});
			// ���湹��
			JPanel root = new JPanel(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.fill = GridBagConstraints.BOTH;
			cons.insets = new Insets(4, 4, 4, 4);
			cons.gridx = 0;
			cons.gridy = 0;
			root.add(new JLabel("�������ͣ�"), cons);
			cons.weightx = 1;
			cons.gridx = 1;
			root.add(sceneTypeCombo, cons);

			cons.gridx = 0;
			cons.gridy = 1;
			cons.weightx = 0;
			root.add(new JLabel("��Ϣ���⣺"), cons);
			cons.weightx = 1;
			cons.gridx = 1;
			root.add(titleField, cons);
			cons.weightx = 0;
			cons.gridx = 2;
			root.add(titleParamBtn, cons);

			cons.gridx = 0;
			cons.gridy = 2;
			cons.weightx = 0;
			root.add(new JLabel("��Ϣ���ݣ�"), cons);
			cons.weightx = 1;
			cons.weighty = 1;
			cons.gridx = 1;
			cons.gridheight = 3;
			root.add(new JideScrollPane(contentArea), cons);
			cons.weightx = 0;
			cons.gridx = 2;
			cons.gridheight = 1;
			cons.weighty = 0;
			root.add(contentParamBtn, cons);

			cons.gridx = 0;
			cons.gridy = 5;
			root.add(new JLabel("֪ͨ���ͣ�"), cons);
			JPanel messageTypePanel = new JPanel(new GridBagLayout());
			GridBagConstraints typeCons = new GridBagConstraints();
			typeCons.fill = GridBagConstraints.WEST;
			typeCons.anchor = GridBagConstraints.WEST;
			for (int i = 0; i < typeCheckList.size(); i++) {
				typeCons.gridx = i;
				messageTypePanel.add(typeCheckList.get(i), typeCons);
			}
			typeCons.gridx = typeCons.gridx + 1;
			typeCons.weightx = 1;
			messageTypePanel.add(new JPanel(), typeCons);
			cons.weightx = 1;
			cons.gridx = 1;
			root.add(messageTypePanel, cons);

			// ���ն���
			cons.gridx = 0;
			cons.gridy = 6;
			cons.weightx = 0;
			cons.weighty = 0;
			root.add(new JLabel("���ն���"), cons);
			cons.gridx = 1;
			cons.weightx = 1;
			cons.weighty = 1;
			cons.gridheight = 4;
			root.add(new JideScrollPane(receiversList), cons);
			cons.gridx = 2;
			cons.weightx = 0;
			cons.weighty = 0;
			cons.gridheight = 1;
			root.add(participantBtn, cons);

			root.setBorder(new EtchedBorder());

			chooser.getOkButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearReceivers();
					addSelectedReceivers(chooser.getSelectedDepartments());
					addSelectedReceivers(chooser.getSelectedRoles());
					addSelectedReceivers(chooser.getSelectedStaffs());
					addSelectedReceivers(chooser.getSelectedVRoles());
				}
			});
			receiversList.registerKeyboardAction(new AbstractAction(
					"remove receivers") {
				public void actionPerformed(ActionEvent e) {
					int[] indecies = receiversList.getSelectedIndices();
					DefaultListModel model = (DefaultListModel) receiversList
							.getModel();
					for (int i = indecies.length - 1; i >= 0; i--) {
						model.remove(indecies[i]);
					}
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
					JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			return root;
		}

		/**
		 * ��ʾ��Աѡ�����
		 */
		private void showReceiverChooser() {
			DefaultListModel model = (DefaultListModel) receiversList
					.getModel();
			List<WofoUnitBean> units = new ArrayList<WofoUnitBean>();
			List<WofoUserBean> users = new ArrayList<WofoUserBean>();
			List<WofoRoleBean> roles = new ArrayList<WofoRoleBean>();
			// �����ɫ
			List<WofoVirtualRoleBean> vRoles = new ArrayList<WofoVirtualRoleBean>();

			for (int i = 0; i < model.getSize(); i++) {
				Object o = model.getElementAt(i);
				if (o instanceof WofoUnitBean) {
					units.add((WofoUnitBean) o);
				} else if (o instanceof WofoUserBean) {
					users.add((WofoUserBean) o);
				} else if (o instanceof WofoRoleBean) {
					WofoRoleBean role = (WofoRoleBean) o;
					roles.add(role);
				} else if (o instanceof WofoVirtualRoleBean) {
					vRoles.add((WofoVirtualRoleBean) o);
				}
			}
			chooser.setSelectedDepartements(units);
			chooser.setSelectedRoles(roles);
			chooser.setSelectedStaffs(users);
			chooser.setSelectedVRoles(vRoles);
			chooser.showPopupChooser(-1, -1, participantBtn, 190, 270, false);
		}

		/**
		 * ��ս����߶���
		 */
		private void clearReceivers() {
			DefaultListModel model = (DefaultListModel) receiversList
					.getModel();
			model.clear();
		}

		/**
		 * ��ӽ����߶���
		 * 
		 * @param receivers
		 */
		@SuppressWarnings("unchecked")
		private void addSelectedReceivers(List receivers) {
			DefaultListModel model = (DefaultListModel) receiversList
					.getModel();
			for (Object o : receivers) {
				model.addElement(o);
			}
		}

		/**
		 * ������ѡ��ť���ı���������������Ӳ�����ѡ��Ĳ������Զ���װĳ���ʽ��ӵ��ı������
		 * 
		 * @param btn
		 * @param textComp
		 */
		private void combineArgsTextComp(final AbstractButton btn,
				final JTextComponent textComp) {
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					argSelectPopup = new JidePopup();
					PopupPanel pPanel = new PopupPanel(argSelectPopup,
							textComp, true);
					argSelectPopup.setPreferredSize(new Dimension(150, 200));
					argSelectPopup.getContentPane().setLayout(
							new BorderLayout());
					argSelectPopup.getContentPane().add(pPanel,
							BorderLayout.CENTER);
					argSelectPopup.showPopup(btn);
				}
			});
		}

		@Override
		public JComponent createBannerPanel() {
			WfBannerPanel headerPanel1 = new WfBannerPanel("��Ϣ���ɳ���",
					"��ĳ��ָ���ĳ����£����Զ�����Ϣ��ʽ����Ϣ�����߶���", null);
			return headerPanel1.getGlassBanner();
		}

		@Override
		public JComponent createContentPanel() {
			return createSceneContentPanel();
		}
	}

	/**
	 * ����ѡ�񵯳��˵��Ļ������
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class PopupPanel extends JPanel {
		private CheckBoxList argList = new CheckBoxList();
		private JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
		DefaultListModel model = new DefaultListModel();

		public PopupPanel(final JidePopup popup, final JTextComponent textComp,
				final boolean multiple) {
			argList.setModel(model);
			if (!multiple) {
				argList.getCheckBoxListSelectionModel().setSelectionMode(
						ListSelectionModel.SINGLE_SELECTION);
			}
			JideScrollPane scroll = new JideScrollPane(argList);
			setLayout(new BorderLayout());
			add(scroll, BorderLayout.CENTER);
			JPanel btnPanel = new JPanel(new BorderLayout());
			btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			btnPanel.add(okBtn, BorderLayout.EAST);
			okBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					popup.hidePopup();
					StringBuffer paramValue = new StringBuffer();
					Object[] args = argList.getCheckBoxListSelectedValues();
					for (Object o : args) {
						paramValue.append("{").append(o.toString()).append("}");
					}
					textComp
							.setText(textComp.getText() + paramValue.toString());
				}
			});
			add(btnPanel, BorderLayout.SOUTH);

			WfArgumentsPanel argPanel = null;
			// ȡ���������
			for (JPanel p : dialog.getCustomPanels()) {
				if (p instanceof WfArgumentsPanel) {
					argPanel = (WfArgumentsPanel) p;
					break;
				}
			}
			if (argPanel != null) {
				List<WofoArgumentsBean> args = argPanel.getArguments(null);
				model.clear();
				for (WofoArgumentsBean arg : args) {
					model.addElement(arg);
				}
			}
		}
	}

	/**
	 * ����������ͷ�İ�ť
	 * 
	 * @author Qil.Wong
	 * 
	 */
	public static class WfSplitBtn extends JideButton {
		public WfSplitBtn(String text) {
			setHorizontalTextPosition(AbstractButton.LEFT);
			setIcon(Functions.getImageIcon("splitbtn.gif"));
			setFocusable(false);
			setText(text);
		}
	}

	/**
	 * �Զ��峡������Ŀ�����ڱ�����ܸ��õ���ʾ����
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class MySceneObj {
		private WofoMessageBean message;

		public MySceneObj(WofoMessageBean message) {
			this.message = message;
		}

		public String toString() {
			Element e = (Element) Functions.findNode(
					"/message/scenes/scene[@id='" + message.getSceneType()
							+ "']", sceneDoc.getDocumentElement());
			return e.getAttribute("value");
		}
	}

	private class MyNotifyObj {
		private WofoMessageBean message;

		public MyNotifyObj(WofoMessageBean message) {
			this.message = message;
		}

		public String toString() {
			if (message.getNotifyType() == null
					|| message.getNotifyType().trim().equals("")) {
				return "";
			}
			String[] ids = message.getNotifyType().split(",");
			StringBuffer sb = new StringBuffer();
			for (String id : ids) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				Element e = (Element) Functions.findNode(
						"/message/sendTypes/type[@id='" + id + "']", sceneDoc
								.getDocumentElement());
				sb.append(e.getAttribute("value"));
			}
			return sb.toString();
		}
	}

}
