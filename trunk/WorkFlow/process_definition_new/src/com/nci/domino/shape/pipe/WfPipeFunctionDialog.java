package com.nci.domino.shape.pipe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoParticipantScopeBean;
import com.nci.domino.beans.org.WofoRoleBean;
import com.nci.domino.beans.org.WofoUnitBean;
import com.nci.domino.beans.org.WofoUserBean;
import com.nci.domino.beans.plugin.pipe.WofoPipeFunctionBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfParticipantChooser;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.WfComboBox.WfComboBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 管道职能对话框
 * 
 * @author Qil.Wong
 * 
 */
public class WfPipeFunctionDialog extends WfDialog {

	public WfPipeFunctionDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 450;
		defaultHeight = 380;
	}

	public void clearContents() {
		super.clearContents();
	}

	public Serializable getInputValues() {
		return super.getInputValues();
	}

	public void setInputValues(Serializable value) {
		super.setInputValues(value);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("职能对象",
				"职能是业务流程所在的一个范围", null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		BasicPanel basic = new BasicPanel();
		customPanels.add(basic);
		setInitFocusedComponent(basic.field);
		return contentTab;
	}

	private class BasicPanel extends WfInputPanel {
		WfParticipantChooser chooser = new WfParticipantChooser(editor);
		JLabel label = new JLabel("名称：");
		JTable participantTable = new JTable();
		WfOverlayableTextField field;

		public BasicPanel() {
			panelName = "作用范围";
			init();
		}

		public String check() {
			if (field.getText().trim().equals("")) {
				return "对象显示名称不能为空！";
			}
			return null;
		}

		private void init() {
			WfTextDocument doc = new WfTextDocument(64);
			doc.setWfDialog(WfPipeFunctionDialog.this);
			field = new WfOverlayableTextField(doc);

			participantTable.setModel(new javax.swing.table.DefaultTableModel(
					new Object[][] {}, new String[] { "参与者类型", "参与者", "范围" }) {
				Class[] types = new Class[] { java.lang.String.class,
						java.lang.String.class, JComboBox.class };

				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {

					return false;
				}
			});
			participantTable.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			participantTable.getColumnModel().getColumn(0).setPreferredWidth(
					120);
			participantTable.getColumnModel().getColumn(1).setPreferredWidth(
					200);
			participantTable.getColumnModel().getColumn(2).setPreferredWidth(
					100);
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 0;
			cons.fill = GridBagConstraints.BOTH;
			cons.insets = new Insets(4, 4, 4, 4);
			add(label, cons);
			cons.gridx = 1;
			cons.weightx = 1;
			add(field.getOverlayableTextField(), cons);

			WfOperControllerButtonPanel buttonPanel = new WfOperControllerButtonPanel(
					participantTable, false);
			cons.gridx = 0;
			cons.gridy = 1;
			cons.gridwidth = 2;
			add(buttonPanel, cons);
			cons.gridx = 0;
			cons.gridy = 2;
			cons.gridwidth = 2;
			cons.weighty = 1;
			add(new JideScrollPane(participantTable), cons);
			buttonPanel.getAddRowBtn().removeActionListener(
					buttonPanel.getAddRowBtn().getActionListeners()[0]);
			buttonPanel.getAddRowBtn().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					chooser.showPopupChooser((JComponent) e.getSource(), 200,
							280);

				}
			});
			chooser.getOkButton().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					addSelectedParticipants();
				}
			});
			Functions.hideColumn(participantTable, 2);
		}

		private void addSelectedParticipants() {
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			List<WofoUnitBean> departs = chooser.getSelectedDepartments();
			List<WofoUserBean> users = chooser.getSelectedStaffs();
			List<WofoRoleBean> roles = chooser.getSelectedRoles();

			WofoParticipantScopeBean p = null;

			// 将选择的部门信息加到表格中
			for (WofoUnitBean unit : departs) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				// p.setActivityId(activity.getActivityId());
				p.setParticipantCode(unit.getUnitCode());
				p.setParticipantId(unit.getUnitId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_UNIT);
				p.setParticipantName(unit.getUnitName());
				// other setters
				// ........
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						new WfComboBean("", "") });
			}
			// 将选择的人员信息加到表格中
			for (WofoUserBean user : users) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				// p.setActivityId(activity.getActivityId());
				p.setParticipantCode(user.getUserCode());
				p.setParticipantId(user.getUserId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_USER);
				p.setParticipantName(user.getUserName());
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						new WfComboBean("", "") });
			}
			// 将选择的角色信息加到表格中
			for (WofoRoleBean r : roles) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				// p.setActivityId(activity.getActivityId());
				p.setParticipantCode(r.getRoleCode());
				p.setParticipantId(r.getRoleId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_ROLE);
				p.setParticipantName(r.getRoleName());
				WfComboBean defaultEffectScope = new WfComboBean(
						WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT,
						WofoResources
								.getValueByKey("participant_scope_"
										+ WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT));
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						defaultEffectScope });
			}
			// FIXME 虚拟角色
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoPipeFunctionBean pipeFBean = (WofoPipeFunctionBean) value;
			pipeFBean.setName(field.getText().trim());
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			for (int i = 0; i < model.getRowCount(); i++) {
				pipeFBean.getRoles().add(model.getValueAt(i, 1));
			}
			return pipeFBean;
		}

		@Override
		public void reset() {
			chooser.reset();
			field.setText("");
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
		}

		@Override
		public void setValues(Serializable value) {
			WofoPipeFunctionBean pipeFBean = (WofoPipeFunctionBean) value;
			field.setText(pipeFBean.getShowText());
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			for (int i = 0; i < pipeFBean.getRoles().size(); i++) {
				WofoParticipantScopeBean scopeBean = (WofoParticipantScopeBean) pipeFBean
						.getRoles().get(i);
				model
						.addRow(new Object[] {
								WofoResources
										.getValueByKey("participant_scope_"
												+ scopeBean
														.getParticipantType()
														.toLowerCase()),
								scopeBean, null });
			}
		}

	}

}
