package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.org.WofoUnitBean;
import com.nci.domino.components.tree.DepartmentTree;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * ��������ѡ���࣬�ڸö����п���ѡ��ϵͳ����Ļ����Ͳ��ţ�����ʾ�����������
 * 
 * @author Qil.Wong
 * 
 */
public class WfDepartmentChooser {

	private JTextField departField;

	private JList departList;

	// ����������
	private DepartmentTree departmentTree;

	// // �������������ڵĵ����˵�
	private JidePopup departmentPopup;

	// ѡ��ť
	private AbstractButton departBtn;

	private JPanel root;

	private WfEditor editor;

	private JComponent tabPanel;

	private JPanel buttonPanel;

	// ���ȷ������Ҫ�������û�ѡ��ġ����õĲ�����Ϣ
	private List<DefaultMutableTreeNode> selectedUsefulDeparts = new ArrayList<DefaultMutableTreeNode>();

	// ��ǰѡ��Ĳ�����Ϣ
	private List<DefaultMutableTreeNode> selectedDeparts = new ArrayList<DefaultMutableTreeNode>();

	// ��ʱ�Ա�����������;��ʹ��ǰ���
	private List<DefaultMutableTreeNode> tempSelectedDeparts = new ArrayList<DefaultMutableTreeNode>();

	public WfDepartmentChooser(WfEditor editor, JPanel parentPanel) {
		root = parentPanel;
		this.editor = editor;
	}

	public void init(int gridx, int gridy, int departFieldLength) {
		departBtn = new JideButton(WofoResources.getValueByKey("choose"));
		departBtn.setHorizontalTextPosition(AbstractButton.LEFT);
		departBtn.setIcon(Functions.getImageIcon("splitbtn.gif"));
		departField = new JTextField();
		departList = new JList();
		DefaultListModel listModel = new DefaultListModel();
		departList.setModel(listModel);
		departList.setEnabled(false);
		JScrollPane departScroll = new JideScrollPane(departList);
		departField.setEditable(false);
		JLabel companyLabel = new JLabel(WofoResources.getValueByKey("company")
				+ "��");
		companyLabel
				.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
		JLabel departLabel = new JLabel(WofoResources
				.getValueByKey("department")
				+ "��");
		departLabel
				.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
		departBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDepartmentTreePopup(e);
			}
		});
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = gridx;
		cons.insets = new Insets(3, 5, 2, 5);
		cons.gridy = gridy;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 0;
		root.add(companyLabel, cons);

		cons.gridx = gridx + 1;
		cons.gridwidth = departFieldLength - 1;
		cons.weightx = 1;

		root.add(departField, cons);
		// ѡ��ť������5�������㹻����չ
		cons.gridx = gridx + departFieldLength;
		cons.weightx = 0;
		cons.gridwidth = 1;
		root.add(departBtn, cons);

		cons.gridx = gridx;
		cons.gridy = gridy + 1;
		cons.gridwidth = 1;
		root.add(departLabel, cons);

		cons.gridx = gridx + 1;
		cons.gridy = gridy + 1;
		cons.gridheight = 1;
		cons.gridwidth = departFieldLength;
		cons.weightx = 1.0f;
		cons.weighty = 1.0f;
		cons.fill = GridBagConstraints.BOTH;
		root.add(departScroll, cons);
		tabPanel = createContentPanel();
		initButtonPanel();
		initData();
	}

	/**
	 * ��ʼ����������
	 */
	private void initData() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) editor
				.getCache().waitWhileNullGet(WofoActions.GET_DEPT_TREE);
		departmentTree.setRootNode(root);
		departmentTree.expandRow(0);
	}

	private void setDefaultData() {
		TreePath[] path = new TreePath[selectedUsefulDeparts.size()];
		for (int i = 0; i < path.length; i++) {
			path[i] = new TreePath(selectedUsefulDeparts.get(i).getPath());
		}
		departmentTree.getCheckBoxTreeSelectionModel().setSelectionPaths(path);
	}

	/**
	 * ��ʾ���Ż���ѡ��˵�
	 */
	private void showDepartmentTreePopup(ActionEvent e) {
		final AbstractButton btn = (AbstractButton) e.getSource();
		departmentPopup = new JidePopup();

		departmentPopup.getContentPane().setLayout(new BorderLayout());
		JideTabbedPane tab = new JideTabbedPane();
		tab.addTab("����", this.tabPanel);
		departmentPopup.getContentPane().add(tab, BorderLayout.CENTER);
		departmentPopup.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		departmentPopup.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				setDefaultData();
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				btn.setSelected(false);
				departmentTree.reset();
			}
		});
		departmentPopup.updateUI();
		departmentPopup.setMovable(true);
		departmentPopup.setPreferredPopupSize(new Dimension(230, 310));
		departmentPopup.showPopup(btn);
	}

	/**
	 * ���������˵����������
	 * 
	 * @param btn
	 *            ���������˵���JToggleButton����
	 * @return
	 */
	private JComponent createContentPanel() {
		departmentTree = new DepartmentTree(editor);
		departmentTree.setRootVisible(false);
		departmentTree.getCheckBoxTreeSelectionModel()
				.addTreeSelectionListener(new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						treeSelected(e);
					}
				});
		JPanel root = new JPanel(new BorderLayout());
		root.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		root.add(departmentTree.getOverlayScroll(), BorderLayout.CENTER);
		return root;
	}

	/**
	 * ȫѡ������ĸ��ڵ������صģ������ü򵥵ĸ��ڵ�ѡ����У�����Ҫѡ�����������ڵ�
	 */
	private void selectAll() {
		selectedDeparts.clear();
		departmentTree.getCheckBoxTreeSelectionModel().setSelectionPath(
				new TreePath(departmentTree.getRootNode()));
	}

	/**
	 * Ҷ�ڵ�ת��
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
			tempSelectedDeparts.add(node);
		}
	}

	private void initButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		JButton selectAllBtn = new JButton(WofoResources
				.getValueByKey("select_all"));
		// ȫѡ����
		selectAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		JButton deselectAllBtn = new JButton(WofoResources
				.getValueByKey("deselect_all"));
		// ȫ��ѡ����
		deselectAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				departmentTree.reset();
				selectedDeparts.clear();
			}
		});
		JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				departmentPopup.hidePopup();
				showSelectedDepartmentValue();
			}
		});
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints constrain = new GridBagConstraints();
		constrain.gridx = 0;
		constrain.gridy = 0;
		buttonPanel.add(selectAllBtn, constrain);
		constrain.gridx = 1;
		buttonPanel.add(deselectAllBtn, constrain);
		JPanel glue = new JPanel();
		constrain.weightx = 1;
		constrain.gridx = 2;
		buttonPanel.add(glue, constrain);
		constrain.gridx = 3;
		constrain.weightx = 0;
		buttonPanel.add(okBtn, constrain);
	}

	/**
	 * ���ݲ���id�ҵ����ϵ�ѡ��ڵ�
	 * 
	 * @param departmentID
	 */
	private void parseDepartmentObjByID(DefaultMutableTreeNode node,
			Set<String> idSet, List<DefaultMutableTreeNode> nodes) {
		if (!node.isLeaf()) {
			int childCount = node.getChildCount();
			for (int i = 0; i < childCount; i++) {
				parseDepartmentObjByID((DefaultMutableTreeNode) node
						.getChildAt(i), idSet, nodes);
			}
		} else {
			WofoUnitBean unit = (WofoUnitBean) node.getUserObject();
			if (idSet.contains(unit.getUnitId())) {
				nodes.add(node);
			}
		}
	}

	/**
	 * ���ݴ������Ĳ���id���ϣ���ȡ�����Žڵ�
	 * 
	 * @param departmentIDs
	 * @return
	 */
	private List<DefaultMutableTreeNode> getLeafNodesByDepartmentID(
			String departmentIDs) {
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		// ����һ�����ṹ���ж�userobject��id�Ƿ���idSet��
		if (departmentIDs != null && !departmentIDs.trim().equals("")) {
			String[] departIDs = departmentIDs.split(",");
			Set<String> idSet = new HashSet<String>();
			for (String s : departIDs) {
				idSet.add(s);
			}
			parseDepartmentObjByID(departmentTree.getRootNode(), idSet, nodes);
		}

		return nodes;
	}

	/**
	 * ����ѡ��Ľڵ㣬��ȷ��Ҷ�ӽڵ�
	 * 
	 * @return
	 */
	public List<DefaultMutableTreeNode> getSelectedNodes() {
		tempSelectedDeparts.clear();
		List<DefaultMutableTreeNode> temp = new ArrayList<DefaultMutableTreeNode>();
		for (DefaultMutableTreeNode node : selectedDeparts) {
			parseLeafObj(node);
		}
		temp.addAll(tempSelectedDeparts);
		return temp;
	}

	/**
	 * ��ѡ�������������ӡ�ɾ��ѡ��Ľڵ�
	 * 
	 * @param e
	 */
	protected void treeSelected(TreeSelectionEvent e) {
		TreePath[] paths = e.getPaths();
		for (TreePath path : paths) {
			DefaultMutableTreeNode evtNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();

			if (e.isAddedPath()) {
				if (!selectedDeparts.contains(evtNode)) {
					selectedDeparts.add(evtNode);
				}
			} else {
				selectedDeparts.remove(evtNode);
			}
		}
	}

	/**
	 * �������
	 */
	public void clearContents() {
		departField.setText("");
		departList.setModel(new DefaultListModel());
		departList.updateUI();
	}

	public JTextField getDepartField() {
		return departField;
	}

	public void setDepartField(JTextField departField) {
		this.departField = departField;
	}

	public DepartmentTree getDepartmentTree() {
		return departmentTree;
	}

	public AbstractButton getDepartBtn() {
		return departBtn;
	}

	/**
	 * ���õ�λ��������Ϣ������ʾ����
	 * 
	 * @param company
	 * @param depart
	 */
	public void setDepartmentValue(String company, String departmentIDs) {
		selectedDeparts.clear();
		tempSelectedDeparts.clear();
		List<DefaultMutableTreeNode> leafNodes = getLeafNodesByDepartmentID(departmentIDs);
		selectedDeparts.clear();
		selectedDeparts.addAll(leafNodes);
		showSelectedDepartmentValue();
	}

	/**
	 * ��ѡ��Ĳ��Ż�����Ϣ��ʾ����
	 */
	private void showSelectedDepartmentValue() {
		clearContents();
		selectedUsefulDeparts = getSelectedNodes();
		StringBuffer comanyBuffer = new StringBuffer();
		boolean comanyStart = false;
		Set<WofoUnitBean> existedCompany = new HashSet<WofoUnitBean>();
		DefaultListModel listModel = (DefaultListModel) this.departList
				.getModel();
		for (DefaultMutableTreeNode node : selectedUsefulDeparts) {
			if (node == departmentTree.getRootNode()) {
				continue;
			}
			WofoUnitBean unit = (WofoUnitBean) node.getUserObject();
			if (unit.getUnitType().equals(WofoUnitBean.TYPE_COMPANY)) {
				// ���û�����Ϣ
				if (comanyStart) {
					comanyBuffer.append(",");
				}
				if (!comanyStart) {
					comanyStart = true;
				}
				comanyBuffer.append(unit.getUnitName());
				existedCompany.add(unit);
				listModel.addElement(unit.getUnitName());
			} else if (unit.getUnitType().equals(WofoUnitBean.TYPE_DEPARTMENT)) {
				WofoUnitBean parentUnit = (WofoUnitBean) ((DefaultMutableTreeNode) node
						.getParent()).getUserObject();
				// ���ø�������Ϣ
				if (!existedCompany.contains(parentUnit)) {
					existedCompany.add(parentUnit);
					if (comanyStart) {
						comanyBuffer.append(",");
					}
					if (!comanyStart) {
						comanyStart = true;
					}
					comanyBuffer.append(parentUnit.getUnitName());
					listModel.addElement(parentUnit.getUnitName());
				}
				// ���ò�����Ϣ
				listModel.addElement("      " + unit.getUnitName());
			}

		}
		departField.setText(comanyBuffer.toString());
		departmentTree.reset();
	}

	public JList getDepartList() {
		return departList;
	}

	/**
	 * ��ȡ��˾����id�������˾����","����
	 * 
	 * @return id���
	 */
	public String getCompanyID() {
		StringBuffer sb = new StringBuffer();
		boolean start = false;
		Set<WofoUnitBean> units = new HashSet<WofoUnitBean>();
		for (int i = 0; i < selectedUsefulDeparts.size(); i++) {
			WofoUnitBean unit = (WofoUnitBean) selectedUsefulDeparts.get(i)
					.getUserObject();
			if (unit.getUnitType().equals(WofoUnitBean.TYPE_DEPARTMENT)) {
				DefaultMutableTreeNode companyNode = (DefaultMutableTreeNode) selectedUsefulDeparts
						.get(i).getParent();
				units.add((WofoUnitBean) companyNode.getUserObject());
			} else {
				Logger.getLogger(this.getClass().getName()).log(Level.INFO,
						"δ�����DepartmentChooserʶ��������ԣ�" + unit.getUnitType());
			}
		}
		for (WofoUnitBean unit : units) {
			if (unit.getUnitType().equals(WofoUnitBean.TYPE_COMPANY)) {
				if (start) {
					sb.append(",");
				} else {
					start = true;
				}
				sb.append(unit.getUnitId());
			}
		}
		return sb.toString();
	}

	/**
	 * ��ȡ����id��������ż���","����
	 * 
	 * @return id���
	 */
	public String getDepartmentID() {
		StringBuffer sb = new StringBuffer();
		boolean start = false;
		for (int i = 0; i < selectedUsefulDeparts.size(); i++) {
			WofoUnitBean unit = (WofoUnitBean) selectedUsefulDeparts.get(i)
					.getUserObject();
			if (unit.getUnitType().equals(WofoUnitBean.TYPE_DEPARTMENT)) {
				if (start) {
					sb.append(",");
				} else {
					start = true;
				}
				sb.append(unit.getUnitId());
			} else {
				Logger.getLogger(this.getClass().getName()).log(Level.INFO,
						"δ�����DepartmentChooserʶ��������ԣ�" + unit.getUnitType());
			}
		}
		return sb.toString();
	}

}
