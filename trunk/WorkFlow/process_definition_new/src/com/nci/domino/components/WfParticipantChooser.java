package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.org.WofoRoleBean;
import com.nci.domino.beans.org.WofoUnitBean;
import com.nci.domino.beans.org.WofoUserBean;
import com.nci.domino.beans.org.WofoVirtualRoleBean;
import com.nci.domino.components.tree.DepartmentTree;
import com.nci.domino.help.Functions;

public class WfParticipantChooser {

	// ������
	private DepartmentTree deparTree;

	// ��Ա��
	private DepartmentTree staffTree;

	// ��ɫ�б�
	private CheckBoxList roleList;

	// �����ɫ�б�
	private CheckBoxList vRoleList;

	private JidePopup popup;

	protected WfEditor editor;

	AbstractButton okBtn = new JButton("ȷ��");

	AbstractButton selectAllBtn = new JButton("ȫѡ");

	AbstractButton deselectAllBtn = new JButton("ȫ��ѡ");

	JideTabbedPane tab;

	/**
	 * ѡ��Ļ����ڵ�
	 */
	private List<DefaultMutableTreeNode> selectedDepartmentNodes = new ArrayList<DefaultMutableTreeNode>();

	/**
	 * ѡ�����Ա�ڵ�
	 */
	private List<DefaultMutableTreeNode> selectedStaffNodes = new ArrayList<DefaultMutableTreeNode>();

	/**
	 * ѡ��Ľ�ɫ�ڵ�
	 */
	private List<WofoRoleBean> selectedRoles = new ArrayList<WofoRoleBean>();

	/**
	 * ѡ��������ɫ�ڵ�
	 */
	private List<WofoVirtualRoleBean> selectedVRoles = new ArrayList<WofoVirtualRoleBean>();

	// ��ʱ�Դ洢�������ݵ�ȫ�ֱ�����ÿ���ⲿ�����ĵ�������մ˱���
	private List effectedUnits = new ArrayList();

	private JPanel btnPanel;

	/**
	 * �ɼ���������ǣ�˳��Ϊ��������Ա����ɫ�������ɫ
	 */
	boolean[] shownParticipantTypes;

	private JPanel contentPanel;

	/**
	 * ���캯��
	 * 
	 * @param editor
	 *            �༭������
	 */
	public WfParticipantChooser(WfEditor editor) {
		this(editor, new boolean[] { true, true, true, true });
	}

	/**
	 * ���캯��
	 * 
	 * @param editor
	 *            �༭������
	 * @param shownParticipantTypes
	 *            ��ʾ�������˳��Ϊ��������Ա����ɫ�������ɫ
	 */
	public WfParticipantChooser(WfEditor editor, boolean[] shownParticipantTypes) {
		this.editor = editor;
		this.shownParticipantTypes = shownParticipantTypes;
		initComponent();
		initData();
	}

	private void initComponent() {
		// ��������ť�Ѿ����Ƽ�ʹ��
		selectAllBtn.setVisible(false);
		deselectAllBtn.setVisible(false);
		// ��������ɼ�
		if (shownParticipantTypes[0]) {
			deparTree = new DepartmentTree(editor);
			deparTree.setRootVisible(false);
			deparTree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(
					new TreeSelectionListener() {
						public void valueChanged(TreeSelectionEvent e) {
							treeSelected(e, selectedDepartmentNodes);
						}
					});
		}
		// �����Ա�ɼ�
		if (shownParticipantTypes[1]) {
			staffTree = new DepartmentTree(editor);
			staffTree.setRootVisible(false);
			staffTree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(
					new TreeSelectionListener() {
						public void valueChanged(TreeSelectionEvent e) {
							treeSelected(e, selectedStaffNodes);
						}
					});
		}
		// �����ɫ�ɼ�
		if (shownParticipantTypes[2]) {
			roleList = new CheckBoxList();
			roleList = new CheckBoxList(new DefaultListModel());
			roleList.getCheckBoxListSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							roleSelect(e, roleList, selectedRoles);
						}
					});
		}
		// ��������ɫ�ɼ�
		if (shownParticipantTypes[3]) {
			vRoleList = new CheckBoxList();
			vRoleList = new CheckBoxList(new DefaultListModel());
			vRoleList.getCheckBoxListSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							roleSelect(e, vRoleList, selectedVRoles);
						}
					});
		}
		btnPanel = new JPanel(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = 3;
		cons.gridy = 0;
		cons.anchor = GridBagConstraints.EAST;
		btnPanel.add(okBtn, cons);
		cons.gridx = 0;
		btnPanel.add(selectAllBtn, cons);
		cons.gridx = 1;
		btnPanel.add(deselectAllBtn, cons);
		cons.gridx = 2;
		cons.weightx = 1;
		btnPanel.add(new JPanel(), cons);// ��glue��

		btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2));

		okBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				popup.hidePopup();
			}
		});
		selectAllBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				selectAll();
			}
		});
		deselectAllBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				deselectAll();
			}
		});
		contentPanel = new JPanel();
		tab = new JideTabbedPane();// Ϊ�˷�ֹpopupmenu���ϱ������jide��һ��bug
		if (shownParticipantTypes[0])
			tab.addTab("����", null, new JideScrollPane(deparTree), null);
		if (shownParticipantTypes[1])
			tab.addTab("��Ա", null, new JideScrollPane(staffTree), null);
		if (shownParticipantTypes[2])
			tab.addTab("��ɫ", null, new JideScrollPane(roleList), null);
		if (shownParticipantTypes[3])
			tab.addTab("�����ɫ", null, new JideScrollPane(vRoleList), null);
		popup = new JidePopup();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(tab, BorderLayout.CENTER);
		contentPanel.add(btnPanel, BorderLayout.SOUTH);
	}

	/**
	 * ȫѡ��ֻȫѡ��ǰtab��ʾ���������
	 */
	private void selectAll() {
		JScrollPane scroll = (JScrollPane) tab.getSelectedComponent();
		Component c = scroll.getViewport().getView();
		if (c instanceof DepartmentTree) {
			DepartmentTree departmentTree = (DepartmentTree) c;
			departmentTree.getCheckBoxTreeSelectionModel().setSelectionPath(
					new TreePath(departmentTree.getModel().getRoot()));
		} else if (c instanceof CheckBoxList) {
			CheckBoxList list = (CheckBoxList) c;
			list.selectAll();
		}
	}

	/**
	 * ȡ��ȫѡ��ֻȡ����ǰtab��ʾ���������
	 */
	private void deselectAll() {
		JScrollPane scroll = (JScrollPane) tab.getSelectedComponent();
		Component c = scroll.getViewport().getView();
		if (c instanceof DepartmentTree) {
			DepartmentTree departmentTree = (DepartmentTree) c;
			departmentTree.getCheckBoxTreeSelectionModel().clearSelection();
		} else if (c instanceof CheckBoxList) {
			CheckBoxList list = (CheckBoxList) c;
			list.selectNone();
		}
	}

	private void roleSelect(ListSelectionEvent e, CheckBoxList roleJList,
			List roles) {
		roles.clear();
		if (!e.getValueIsAdjusting()) {
			int[] selected = roleJList.getCheckBoxListSelectedIndices();
			for (int i = 0; i < selected.length; i++) {
				roles.add(roleJList.getModel().getElementAt(selected[i]));
			}
		}
	}

	/**
	 * �м�ڵ�ѡ���ת����Ҫ���м�ڵ��Ҷ�ӽڵ�ȫ������ȥ
	 * 
	 * @param node
	 */
	private void parseLeafObj(DefaultMutableTreeNode node) {
		if (!node.isLeaf()) {
			int childCount = node.getChildCount();
			for (int i = 0; i < childCount; i++) {
				parseLeafObj((DefaultMutableTreeNode) node.getChildAt(i));
			}
		} else {
			effectedUnits.add(node.getUserObject());
		}
	}

	/**
	 * �ڵ�ѡ���¼�
	 * 
	 * @param e
	 * @param selectList
	 */
	private void treeSelected(TreeSelectionEvent e, List selectList) {
		TreePath[] paths = e.getPaths();
		for (TreePath path : paths) {
			DefaultMutableTreeNode evtNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (e.isAddedPath()) {
				selectList.add(evtNode);
			} else {
				selectList.remove(evtNode);
			}
		}
	}

	/**
	 * ��ʼ��������Ա����
	 */
	protected void initData() {
		String url = editor.getServletPath();
		WofoNetBean param = new WofoNetBean(WofoActions.GET_UNIT_TREE, editor
				.getUserID(), "");
		// ��������ɼ�
		if (shownParticipantTypes[0]) {
			DefaultMutableTreeNode deptNode = (DefaultMutableTreeNode) editor
					.getCache().nowaitWhileNullGet(WofoActions.GET_UNIT_TREE);
			if (deptNode == null) {
				WofoNetBean o = Functions.getReturnNetBean(url, param);

				deptNode = (DefaultMutableTreeNode) o.getParam();
				editor.getCache().cache(WofoActions.GET_UNIT_TREE, deptNode);
			} else {
				// deptNode.getParent()
			}
			deparTree.setRootNode(deptNode);
			deparTree.expandRow(0);
		}
		// �����Ա�ɼ�
		if (shownParticipantTypes[1]) {
			DefaultMutableTreeNode staffNode = (DefaultMutableTreeNode) editor
					.getCache().nowaitWhileNullGet(WofoActions.GET_ORG_TREE);
			if (staffNode == null) {
				param.setActionName(WofoActions.GET_ORG_TREE);
				WofoNetBean o = Functions.getReturnNetBean(url, param);
				staffNode = (DefaultMutableTreeNode) o.getParam();
				editor.getCache().cache(WofoActions.GET_ORG_TREE, staffNode);
			}
			staffTree.setRootNode(staffNode);
			staffTree.expandRow(0);
		}
		// �����ɫ�ǿɼ���
		if (shownParticipantTypes[2]) {
			List<WofoRoleBean> roles = (List<WofoRoleBean>) editor.getCache()
					.nowaitWhileNullGet(WofoActions.GET_ROLES);
			if (roles == null) {
				param.setActionName(WofoActions.GET_ROLES);
				WofoNetBean o = Functions.getReturnNetBean(url, param);
				roles = (List<WofoRoleBean>) o.getParam();
				editor.getCache().cache(WofoActions.GET_ROLES, roles);
			}
			DefaultListModel roleModel = (DefaultListModel) roleList.getModel();
			for (WofoRoleBean r : roles) {
				roleModel.addElement(r);
			}
		}

		// ��������ɫ�ǿɼ���
		if (shownParticipantTypes[3]) {
			List<WofoVirtualRoleBean> roles = (List<WofoVirtualRoleBean>) editor
					.getCache().nowaitWhileNullGet(
							WofoActions.GET_VIRTUAL_ROLES);
			if (roles == null) {
				param.setActionName(WofoActions.GET_VIRTUAL_ROLES);
				WofoNetBean o = Functions.getReturnNetBean(url, param);
				roles = (List<WofoVirtualRoleBean>) o.getParam();
				editor.getCache().cache(WofoActions.GET_VIRTUAL_ROLES, roles);
			}
			DefaultListModel vRoleModel = (DefaultListModel) vRoleList
					.getModel();
			for (WofoVirtualRoleBean r : roles) {
				vRoleModel.addElement(r);
			}
		}
	}

	/**
	 * ��ʾ�������ڣ�ʹ��Ĭ�ϵ�x,y,��͸�
	 * 
	 * @param relative
	 *            ������
	 */
	public void showPopupChooser(JComponent relative) {
		showPopupChooser(relative, -1, -1);
	}

	/**
	 * ��ʾѡ�����ĵ�������,ʹ��Ĭ�ϵ�x��y����
	 * 
	 * @param relative
	 *            ������
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public void showPopupChooser(JComponent relative, int width, int height) {
		showPopupChooser(-1, -1, relative, width, height, false);
	}

	/**
	 * ��ʾѡ�����ĵ�������
	 * 
	 * @param x
	 *            ������x����
	 * @param y
	 *            ������y����
	 * @param relative
	 *            ������
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public void showPopupChooser(int x, int y, Component relative, int width,
			int height, boolean reset) {
		if (reset) {
			reset();
		}
		tab.setSelectedIndex(0);
		popup = new JidePopup();
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(contentPanel);
		popup.setMovable(true);
		if (width != -1) {
			popup.setPreferredSize(new Dimension(width, height));
		}
		if (relative == null) {
			popup.showPopup();
		} else if (x == -1) {
			popup.showPopup(relative);
		} else {
			popup.showPopup(x, y, relative);
		}
	}

	/**
	 * ���
	 */
	public void reset() {
		if (shownParticipantTypes[0])
			deparTree.reset();
		if (shownParticipantTypes[1])
			staffTree.reset();
		if (shownParticipantTypes[2]) {
			roleList.clearCheckBoxListSelection();
			roleList.clearSelection();
		}
		if (shownParticipantTypes[3]) {
			vRoleList.clearCheckBoxListSelection();
			vRoleList.clearSelection();
		}
		selectedDepartmentNodes.clear();
		selectedStaffNodes.clear();
		selectedRoles.clear();
		selectedVRoles.clear();
		tab.setSelectedIndex(0);
	}

	/**
	 * ��ȡѡ��Ĳ���
	 * 
	 * @return
	 */
	public List<WofoUnitBean> getSelectedDepartments() {
		effectedUnits.clear();
		List<WofoUnitBean> temp = new ArrayList<WofoUnitBean>();
		for (DefaultMutableTreeNode node : selectedDepartmentNodes) {
			parseLeafObj(node);
		}
		temp.addAll(effectedUnits);
		return temp;
	}

	/**
	 * ��������Ϣ����������Ϊѡ��״̬
	 * 
	 * @param departements
	 */
	public void setSelectedDepartements(List<WofoUnitBean> departements) {
		deparTree.reset();
		List<String> ids = new ArrayList<String>();
		for (WofoUnitBean unit : departements) {
			ids.add(unit.getID());
		}
		List<DefaultMutableTreeNode> selectedNodes = new ArrayList<DefaultMutableTreeNode>();
		iteratToSetNodeSelect(deparTree.getRootNode(), ids, selectedNodes);
		TreePath[] path = new TreePath[selectedNodes.size()];
		for (int i = 0; i < path.length; i++) {
			path[i] = new TreePath(selectedNodes.get(i).getPath());
		}
		deparTree.getCheckBoxTreeSelectionModel().setSelectionPaths(path);
	}

	/**
	 * ����Ա��Ϣ����������Ϊѡ��״̬
	 * 
	 * @param staffs
	 */
	public void setSelectedStaffs(List<WofoUserBean> staffs) {
		staffTree.reset();
		List<String> ids = new ArrayList<String>();
		for (WofoUserBean user : staffs) {
			ids.add(user.getID());
		}
		List<DefaultMutableTreeNode> selectedNodes = new ArrayList<DefaultMutableTreeNode>();
		iteratToSetNodeSelect(staffTree.getRootNode(), ids, selectedNodes);
		TreePath[] path = new TreePath[selectedNodes.size()];
		for (int i = 0; i < path.length; i++) {
			path[i] = new TreePath(selectedNodes.get(i).getPath());
		}
		staffTree.getCheckBoxTreeSelectionModel().setSelectionPaths(path);
	}

	/**
	 * ����ɫ��Ϣ���б�������Ϊѡ��״̬
	 * 
	 * @param roles
	 */
	public void setSelectedRoles(List<WofoRoleBean> roles) {
		roleList.clearCheckBoxListSelection();
		roleList.clearSelection();
		int[] indices = new int[roles.size()];
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < roles.size(); i++) {
			ids.add(roles.get(i).getID());
		}
		DefaultListModel listModel = (DefaultListModel) roleList.getModel();
		for (int i = 0; i < listModel.getSize(); i++) {
			WofoRoleBean r = (WofoRoleBean) listModel.getElementAt(i);
			if (ids.contains(r.getID())) {
				indices[ids.indexOf(r.getID())] = i;
			}
		}
		roleList.setCheckBoxListSelectedIndices(indices);
	}

	/**
	 * �������ɫ��Ϣ���б�������Ϊѡ��״̬
	 * 
	 * @param vRoles
	 */
	public void setSelectedVRoles(List<WofoVirtualRoleBean> vRoles) {
		vRoleList.clearCheckBoxListSelection();
		vRoleList.clearSelection();
		int[] indices = new int[vRoles.size()];
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < vRoles.size(); i++) {
			ids.add(vRoles.get(i).getID());
		}
		DefaultListModel listModel = (DefaultListModel) vRoleList.getModel();
		for (int i = 0; i < listModel.getSize(); i++) {
			WofoVirtualRoleBean r = (WofoVirtualRoleBean) listModel
					.getElementAt(i);
			if (ids.contains(r.getID())) {
				indices[ids.indexOf(r.getID())] = i;
			}
		}
		vRoleList.setCheckBoxListSelectedIndices(indices);
	}

	/**
	 * ͨ���������ڵ㣬�����������Ľڵ�ѡ��
	 */
	private void iteratToSetNodeSelect(DefaultMutableTreeNode node,
			List<String> beanIDs, List<DefaultMutableTreeNode> selectedNodes) {
		if (node.isLeaf()) {
			WofoBaseBean bean = (WofoBaseBean) node.getUserObject();
			if (beanIDs.contains(bean.getID())) {
				selectedNodes.add(node);
			}
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
						.getChildAt(i);
				iteratToSetNodeSelect(child, beanIDs, selectedNodes);
			}
		}
	}

	/**
	 * ��ȡѡ�����Ա
	 * 
	 * @return
	 */
	public List<WofoUserBean> getSelectedStaffs() {
		effectedUnits.clear();
		List<WofoUserBean> temp = new ArrayList<WofoUserBean>();
		for (DefaultMutableTreeNode node : selectedStaffNodes) {
			parseLeafObj(node);
		}
		temp.addAll(effectedUnits);
		return temp;
	}

	/**
	 * ��ȡѡ��Ľ�ɫ
	 * 
	 * @return
	 */
	public List<WofoRoleBean> getSelectedRoles() {
		List<WofoRoleBean> temp = new ArrayList<WofoRoleBean>();
		temp.addAll(selectedRoles);
		return temp;
	}

	/**
	 * ��ȡѡ��������ɫ
	 * 
	 * @return
	 */
	public List<WofoVirtualRoleBean> getSelectedVRoles() {
		List<WofoVirtualRoleBean> temp = new ArrayList<WofoVirtualRoleBean>();
		temp.addAll(selectedVRoles);
		return temp;
	}

	/**
	 * ��ȡȷ����ť
	 * 
	 * @return
	 */
	public AbstractButton getOkButton() {
		return okBtn;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public JPanel getContentPanel() {
		return contentPanel;
	}

	public void setContentPanel(JPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

	public JPanel getBtnPanel() {
		return btnPanel;
	}

	public JideTabbedPane getTab() {
		return tab;
	}

}
