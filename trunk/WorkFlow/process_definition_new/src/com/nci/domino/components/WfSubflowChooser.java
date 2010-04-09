package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.tree.WfTreeCellRenderer;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 流程选择对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfSubflowChooser {
	private JTextField subflowField;

	// 机构部门树所在的弹出菜单
	private JidePopup subflowPopup;

	// 选择按钮
	private AbstractButton subflowBtn;

	private JPanel root;

	private WfEditor editor;

	public WfSubflowChooser(WfEditor editor, JPanel parentPanel) {
		this.root = parentPanel;
		this.editor = editor;
	}

	public void init(int gridx, int gridy, int departFieldLength) {
		subflowBtn = new JideButton(WofoResources.getValueByKey("choose"));
		subflowBtn.setHorizontalTextPosition(AbstractButton.LEFT);
		subflowBtn.setIcon(Functions.getImageIcon("splitbtn.gif"));
		subflowField = new JTextField();

		subflowField.setEditable(false);
		JLabel subflowLabel = new JLabel("子流程：");

		initSubflowPopup(subflowBtn);
		subflowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSubFlowTreePopup(e);
			}
		});
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = gridx;
		cons.insets = new Insets(3, 5, 2, 5);
		cons.gridy = gridy;
		// cons.gridwidth = 0;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 0;
		root.add(subflowLabel, cons);

		cons.gridx = gridx + 1;
		cons.gridwidth = departFieldLength - 1;
		cons.weightx = 1;

		root.add(subflowField, cons);
		// 选择按钮给到第5个，留足够的扩展
		cons.gridx = gridx + departFieldLength;
		cons.weightx = 0;
		cons.gridwidth = 1;
		root.add(subflowBtn, cons);
	}

	private void initSubflowPopup(final AbstractButton btn) {
		subflowPopup = new JidePopup();
		subflowPopup.setMovable(true);
		subflowPopup.getContentPane().setLayout(new BorderLayout());
		subflowPopup.add(createPopupPanel(btn), BorderLayout.CENTER);
	}

	/**
	 * 显示部门机构选择菜单
	 */
	private void showSubFlowTreePopup(ActionEvent e) {
		clearContents();
		AbstractButton btn = (AbstractButton) e.getSource();
		initSubflowPopup(btn);
		subflowPopup.showPopup(btn);
	}

	/**
	 * 清空内容
	 */
	public void clearContents() {
		subflowField.setText("");
		selectedSubNode = null;
	}

	/**
	 * 创建弹出菜单的组件集合
	 * 
	 * @param btn
	 *            触发弹出菜单的JToggleButton对象
	 * @return
	 */
	private JPanel createPopupPanel(final AbstractButton btn) {

		JPanel root = new JPanel(new BorderLayout());
		root.setPreferredSize(new Dimension(250, 300));
		root.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));
		DefaultMutableTreeNode rootNode = editor.getOperationArea().getWfTree()
				.getRootNode();

		DefaultMutableTreeNode clonedNode = (DefaultMutableTreeNode) Functions
				.deepClone(rootNode);
		final JTree tree = new JTree(clonedNode);
		tree.setCellRenderer(new WfTreeCellRenderer());
		root.add(new JideScrollPane(tree), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));

		final JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
		// 点击确定，选择子流程
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subflowPopup.hidePopup();
				subflowField
						.setText(selectedSubNode.getUserObject().toString());
			}
		});

		// 只允许选择流程节点
		tree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {

					public void valueChanged(TreeSelectionEvent e) {
						TreePath path = tree.getSelectionPath();
						if (path != null) {
							selectedSubNode = (DefaultMutableTreeNode) path
									.getLastPathComponent();
							// 只允许选择流程节点,否则“确定”按钮变灰
							okBtn
									.setEnabled(selectedSubNode.getUserObject() instanceof WofoProcessBean);
						} else {
							selectedSubNode = null;
						}
					}
				});
		GridBagConstraints constrain = new GridBagConstraints();
		constrain.gridx = 0;
		constrain.gridy = 0;
		constrain.anchor = GridBagConstraints.WEST;
		JPanel glue = new JPanel();
		constrain.weightx = 1;
		constrain.gridx = 2;
		constrain.anchor = GridBagConstraints.CENTER;
		buttonPanel.add(glue, constrain);
		constrain.gridx = 3;
		constrain.weightx = 0;
		constrain.anchor = GridBagConstraints.EAST;
		buttonPanel.add(okBtn, constrain);
		root.add(buttonPanel, BorderLayout.SOUTH);
		return root;
	}

	private DefaultMutableTreeNode selectedSubNode;

	/**
	 * 获取选择的节点,可能是子流程，也可能是包
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getSelectedSubNode() {
		return selectedSubNode;
	}

}
