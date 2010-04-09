package com.nci.domino.components.event;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoEventBean;
import com.nci.domino.beans.desyer.event.WofoEventJavaHandlerBean;
import com.nci.domino.beans.desyer.event.WofoEventProcedureHandlerBean;
import com.nci.domino.beans.desyer.event.WofoEventSQLHandlerBean;
import com.nci.domino.beans.desyer.event.WofoEventWebServiceHandlerBean;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;

/**
 * <p>
 * ���⣺WfEventPanel.java
 * </p>
 * <p>
 * ������ ���̻���¼�������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2010-3-30
 * @version 1.0
 */
public class WfEventPanel extends WfDialogPluginPanel {
	private static final long serialVersionUID = 8708432761954038679L;
	private JTable eventTable;
	/**
	 * �¼��������ť
	 */
	private WfOperControllerButtonPanel eventsButtons;

	public WfEventPanel(WfDialog dialog) {
		super(dialog);
		panelName = "��¼�����";
		initComponents();
	}

	/**
	 * @���� ��ʼ�����������˵�
	 * 
	 * @Add by ZHM 2010-3-30
	 */
	private void initPopMenu() {
		/**
		 * sql��䵯���˵�
		 */
		JMenuItem sqlMenuItem;
		/**
		 * �洢���̵����˵�
		 */
		JMenuItem processMenuItem;
		/**
		 * Java���򵯳��˵�
		 */
		JMenuItem javaMenuItem;
		/**
		 * Webservice�����˵�
		 */
		JMenuItem webMenuItem;
		// �����˵��¼�
		ActionListener popuMenuAction = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String menuName = ((JMenuItem) e.getSource()).getText();
				WfDialog addDialog = null;
				WofoEventBean bean = null;
				if ("SQL���".equals(menuName)) {
					// ����SQL��䴰��
					addDialog = new WfSqlEventDialog(dialog.getEditor(),
							"SQL�ű�����", true, dialog);
					bean = new WofoEventSQLHandlerBean();
				} else if ("�洢����".equals(menuName)) {
					// ���ô洢���̴���
					addDialog = new WfProcedureEventDialog(dialog.getEditor(),
							"�洢�����¼�����", true, dialog);
					bean = new WofoEventProcedureHandlerBean();
				} else if ("Java����".equals(menuName)) {
					// ����Java���򴰿�
					addDialog = new WfJavaEventDialog(dialog.getEditor(),
							"Java�����¼�����", true, dialog);
					bean = new WofoEventJavaHandlerBean();
				} else if ("WebService".equals(menuName)) {
					// ����WebService����
					addDialog = new WfWebservicesEventdialog(
							dialog.getEditor(), "Java�����¼�����", true, dialog);
					bean = new WofoEventWebServiceHandlerBean();
				}
				addDialog.showWfDialog(bean);
				if (addDialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
					addDialog.getInputValues();
					insertOneRow(eventTable.getRowCount(), bean);
				}
			}

		};
		sqlMenuItem = new JMenuItem("SQL���");
		sqlMenuItem.addActionListener(popuMenuAction);
		processMenuItem = new JMenuItem("�洢����");
		processMenuItem.addActionListener(popuMenuAction);
		javaMenuItem = new JMenuItem("Java����");
		javaMenuItem.addActionListener(popuMenuAction);
		webMenuItem = new JMenuItem("WebService");
		webMenuItem.addActionListener(popuMenuAction);

		JPopupMenu popup = new JPopupMenu();
		popup.add(sqlMenuItem);
		popup.add(processMenuItem);
		popup.add(javaMenuItem);
		popup.add(webMenuItem);
		popup.show(eventsButtons.getAddRowBtn(), eventsButtons.getAddRowBtn()
				.getSize().width / 2,
				eventsButtons.getAddRowBtn().getSize().height / 2);
	}

	/**
	 * @���� �����ʼ������
	 * 
	 * @Add by ZHM 2010-3-30
	 */
	private void initComponents() {
		eventTable = new JTable();
		eventTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2
						&& evt.getButton() == MouseEvent.BUTTON1) {
					modifyEventBand();
				}
			}
		});
		eventTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		eventTable.setModel(new javax.swing.table.DefaultTableModel(null,
				new String[] { "�¼�����", "�¼�����", "��������", "ִ�з�ʽ", "��ע" }) {
			private static final long serialVersionUID = 4630753515549558647L;
			Class[] types = new Class[] { String.class, String.class,
					String.class, String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});

		eventsButtons = new WfOperControllerButtonPanel(eventTable, false);
		eventsButtons.getDelRowBtn().setText("ɾ���¼�");
		eventsButtons.getAddRowBtn().setText("����¼�");
		// buttonPanel.getUpRowBtn().setVisible(false);
		// buttonPanel.getDownRowBtn().setVisible(false);
		eventsButtons.getAddRowBtn().removeActionListener(
				eventsButtons.getAddRowBtn().getActionListeners()[0]);
		eventsButtons.getAddRowBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initPopMenu();
			}
		});

		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		add(eventsButtons, gridBagConstraints);

		gridBagConstraints.gridy = 1;
		gridBagConstraints.weighty = 1;
		add(new JideScrollPane(eventTable), gridBagConstraints);
	}

	/**
	 * @���� �޸��¼���
	 * 
	 * @Add by ZHM 2010-3-30
	 */
	protected void modifyEventBand() {
		int rowIndex = eventTable.getSelectedRow();
		DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
		WofoEventBean bean = (WofoEventBean) model.getValueAt(rowIndex,
				0);
		WfDialog bandDialog = null;
		if (bean instanceof WofoEventSQLHandlerBean) {
			// SQL�¼�����
			bandDialog = new WfSqlEventDialog(dialog.getEditor(),
					"SQL�ű�����", true, dialog);
		} else if (bean instanceof WofoEventProcedureHandlerBean) {
			// ���ô洢���̴���
			bandDialog = new WfProcedureEventDialog(dialog.getEditor(),
					"�洢�����¼�����", true, dialog);
		} else if (bean instanceof WofoEventJavaHandlerBean) {
			// ����Java���򴰿�
			bandDialog = new WfJavaEventDialog(dialog.getEditor(),
					"Java�����¼�����", true, dialog);
		} else if (bean instanceof WofoEventWebServiceHandlerBean) {
			// ����WebService����
			bandDialog = new WfWebservicesEventdialog(dialog.getEditor(),
					"Java�����¼�����", true, dialog);
		}
		bandDialog.showWfDialog(bean);
		if (bandDialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			bandDialog.getInputValues();
			model.removeRow(rowIndex);
			insertOneRow(rowIndex, bean);
		}
	}

	@SuppressWarnings("unchecked")
	public Serializable applyValues(Serializable value) {
		WofoActivityBean activityBean = (WofoActivityBean) value;
		DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
		ArrayList<WofoEventBean> list = (ArrayList<WofoEventBean>) activityBean
				.getEvents();
		list.clear();
		for (int i = 0; i < model.getRowCount(); i++) {
			list.add((WofoEventBean) model.getValueAt(i, 0));
		}
		return activityBean;
	}

	@Override
	public void reset() {
		DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValues(Serializable value) {
		WofoActivityBean activityBean = (WofoActivityBean) value;
		ArrayList<WofoEventBean> listenerBeans = (ArrayList<WofoEventBean>) activityBean
				.getEvents();
		for (int i = 0, size = listenerBeans.size(); i < size; i++) {
			WofoEventBean bean = listenerBeans.get(i);
			insertOneRow(i, bean);
		}
	}

	/**
	 * @���� �ڼ�����������һ������
	 * @param rowIndex
	 *            int ���ӵ�λ��
	 * @param bean
	 *            WofoEventListenerBean ���ӵĶ���
	 * 
	 * @Add by ZHM 2010-3-30
	 */
	private void insertOneRow(int rowIndex, WofoEventBean bean) {
		DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
		// String name = bean.getEventName();
		String type = bean.getListenerType();
		String mode = bean.getPerformMode();
		String remark = bean.getDescription();
		String performMode = bean.getPerformMode();
		model.insertRow(rowIndex, new Object[] { bean, type, mode, performMode,
				remark });
	}

}
