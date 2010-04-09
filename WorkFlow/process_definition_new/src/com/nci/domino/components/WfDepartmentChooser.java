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
 * 机构部门选择类，在该对象中可以选择系统定义的机构和部门，并显示在两个组件上
 * 
 * @author Qil.Wong
 * 
 */
public class WfDepartmentChooser {

	private JTextField departField;

	private JList departList;

	// 机构部门树
	private DepartmentTree departmentTree;

	// // 机构部门树所在的弹出菜单
	private JidePopup departmentPopup;

	// 选择按钮
	private AbstractButton departBtn;

	private JPanel root;

	private WfEditor editor;

	private JComponent tabPanel;

	private JPanel buttonPanel;

	// 点击确定后需要的真正用户选择的、有用的部门信息
	private List<DefaultMutableTreeNode> selectedUsefulDeparts = new ArrayList<DefaultMutableTreeNode>();

	// 当前选择的部门信息
	private List<DefaultMutableTreeNode> selectedDeparts = new ArrayList<DefaultMutableTreeNode>();

	// 临时性变量，迭代用途，使用前清空
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
				+ "：");
		companyLabel
				.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
		JLabel departLabel = new JLabel(WofoResources
				.getValueByKey("department")
				+ "：");
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
		// 选择按钮给到第5个，留足够的扩展
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
	 * 初始化机构数据
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
	 * 显示部门机构选择菜单
	 */
	private void showDepartmentTreePopup(ActionEvent e) {
		final AbstractButton btn = (AbstractButton) e.getSource();
		departmentPopup = new JidePopup();

		departmentPopup.getContentPane().setLayout(new BorderLayout());
		JideTabbedPane tab = new JideTabbedPane();
		tab.addTab("机构", this.tabPanel);
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
	 * 创建弹出菜单的组件集合
	 * 
	 * @param btn
	 *            触发弹出菜单的JToggleButton对象
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
	 * 全选，这里的根节点是隐藏的，不能用简单的根节点选择进行，而是要选择所有下属节点
	 */
	private void selectAll() {
		selectedDeparts.clear();
		departmentTree.getCheckBoxTreeSelectionModel().setSelectionPath(
				new TreePath(departmentTree.getRootNode()));
	}

	/**
	 * 叶节点转换
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
		// 全选操作
		selectAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		JButton deselectAllBtn = new JButton(WofoResources
				.getValueByKey("deselect_all"));
		// 全不选操作
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
	 * 根据部门id找到树上的选择节点
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
	 * 根据传进来的部门id集合，获取到部门节点
	 * 
	 * @param departmentIDs
	 * @return
	 */
	private List<DefaultMutableTreeNode> getLeafNodesByDepartmentID(
			String departmentIDs) {
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		// 遍历一遍树结构，判断userobject的id是否在idSet里
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
	 * 计算选择的节点，精确到叶子节点
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
	 * 树选择动作，用于增加、删除选择的节点
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
	 * 清空内容
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
	 * 设置单位、部门信息，并显示出来
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
	 * 将选择的部门机构信息显示出来
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
				// 设置机构信息
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
				// 设置父机构信息
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
				// 设置部门信息
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
	 * 获取公司机构id，多个公司间用","隔开
	 * 
	 * @return id组合
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
						"未加入的DepartmentChooser识别机构属性：" + unit.getUnitType());
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
	 * 获取部门id，多个部门间用","隔开
	 * 
	 * @return id组合
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
						"未加入的DepartmentChooser识别机构属性：" + unit.getUnitType());
			}
		}
		return sb.toString();
	}

}
