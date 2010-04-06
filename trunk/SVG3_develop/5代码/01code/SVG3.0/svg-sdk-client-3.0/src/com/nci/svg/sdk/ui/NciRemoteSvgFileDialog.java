/*
 * NciRemoteSvgFileDialog.java
 *
 * Created on 2008年8月20日, 上午9:02
 */

package com.nci.svg.sdk.ui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.GraphFileParamsBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.RemoteUtilities;

/**
 * 
 * @author yx.nci
 */
public class NciRemoteSvgFileDialog extends javax.swing.JDialog {

	private EditorAdapter editor = null;

	/** Creates new form NciRemoteSvgFileDialog */
	public NciRemoteSvgFileDialog(java.awt.Frame parent, boolean modal,
			EditorAdapter editor) {

		super(parent, modal);

		initComponents();
		this.editor = editor;
		this.setLocationRelativeTo(editor.findParentFrame());
	}

	@SuppressWarnings("unchecked")
	public void initData(String filetype, String graphBusinessType,
			String fileformat, String param[]) {
		TreeFile.setModel(null);
		//获取数据
		ArrayList<GraphFileBean> list = null;
		if (filetype == Constants.GRAPHFILE_STANDARD)
			list = RemoteUtilities.getGraphFileList(editor, filetype,
					graphBusinessType, fileformat, param, null);
		else
			list = RemoteUtilities.getGraphFileList(editor, filetype,
					graphBusinessType, fileformat, param, editor
							.getSvgSession().getUser());
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("请选择图");
		DefaultTreeModel graphTreeModel = new DefaultTreeModel(root);
		TreeFile.setModel(graphTreeModel);
		int size = editor.getSupportFileTypeSize();
		String name = null, value = null;
		DefaultMutableTreeNode node = null;
		//增加图类型
		HashMap<String, DefaultMutableTreeNode> mapNode = new HashMap<String, DefaultMutableTreeNode>();
		for (int i = 0; i < size; i++) {
			name = editor.getSupportTypeKeyFromMap(i);
			value = editor.getSupportTypeValueFromMap(i);
			node = new DefaultMutableTreeNode(name);
			root.add(node);
			mapNode.put(value, node);
		}
		ArrayList<GraphFileParamsBean> paramsList = RemoteUtilities
				.getGraphFileParams(editor, "");
		if (paramsList == null || paramsList.size() == 0) {
			if (list != null) {
				for (GraphFileBean bean : list) {
					DefaultMutableTreeNode parentNode = mapNode.get(bean
							.getBusiType());
					if (parentNode != null) {
						node = new DefaultMutableTreeNode(bean.getFileName());
						node.setUserObject(bean);
						parentNode.add(node);
					}
				}
			}
		} else {
			for (GraphFileParamsBean bean : paramsList) {
				// 先构建树各分支节点
				String[] values = new String[10];
				String[] descs = new String[10];
				int[] pos = new int[10];
				DefaultMutableTreeNode parentNode = mapNode.get(bean
						.getGraphType());
				String key = bean.getGraphType();
				if (parentNode == null)
					continue;
				for (int i = 0; i < 10; i++) {
					if (bean.getBean(i).getQueryOrder() != 0) {
						values[bean.getBean(i).getQueryOrder() - 1] = bean
								.getBean(i).getType();
						descs[bean.getBean(i).getQueryOrder() - 1] = bean
								.getBean(i).getDesc();
						pos[bean.getBean(i).getQueryOrder() - 1] = i;
					}
				}

				// 将图信息填充
				if (list != null) {
					for (GraphFileBean fileBean : list) {
						if (fileBean.getBusiType().equals(bean.getGraphType())) {
							key = bean.getGraphType();
							parentNode = mapNode.get(key);
							for (int i = 0; i < 10; i++) {
								if (descs[i] != null && descs[i].length() > 0) {
									key += "_" + fileBean.getParams(pos[i]);
									if (mapNode.get(key) == null) {
										String codeName = editor
												.getSvgSession()
												.getNameFromCode(
														values[i],
														fileBean
																.getParams(pos[i]));
										if (codeName != null) {
											node = new DefaultMutableTreeNode(
													codeName);
											parentNode.add(node);
											mapNode.put(key, node);
											parentNode = mapNode.get(key);
										}
									}
									
								}
							}
							parentNode = mapNode.get(key);
							if (parentNode != null) {
								node = new DefaultMutableTreeNode(fileBean
										.getFileName());
								node.setUserObject(fileBean);
								parentNode.add(node);
							}
						}
					}
				}

			}

		}

		TreeFile.expandRow(0);
	}

	private GraphFileBean fileBean = null;

	private void treeFileValueChanged(javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_graphUnitTreeValueChanged
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) evt.getPath()
				.getLastPathComponent();
		if (node.getUserObject() instanceof GraphFileBean) {
			fileBean = (GraphFileBean) node.getUserObject();
		}

	}

	private void openFile() {
		if(fileBean == null)
			return;
		GraphFileBean bean = RemoteUtilities.getGraphFile(editor, fileBean
				.getID(), fileBean.getFileName());
		if (bean != null) {
			// 打开文件
			editor.getSvgSession().open(bean);
		}
		dispose();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		TreeFile = new javax.swing.JTree();
		ButtonOK = new javax.swing.JButton();
		ButtonCanel = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		TreeFile.setModel(null);
		jScrollPane1.setViewportView(TreeFile);

		ButtonOK.setText("打开");
		ButtonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ButtonOKActionPerformed(evt);
			}
		});

		ButtonCanel.setText("取消");
		ButtonCanel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ButtonCanelActionPerformed(evt);
			}
		});
		TreeFile
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(
							javax.swing.event.TreeSelectionEvent evt) {
						treeFileValueChanged(evt);
					}
				});
		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
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
																layout
																		.createSequentialGroup()
																		.add(
																				jScrollPane1,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				380,
																				Short.MAX_VALUE)
																		.addContainerGap())
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				ButtonOK)
																		.add(
																				18,
																				18,
																				18)
																		.add(
																				ButtonCanel)
																		.add(
																				43,
																				43,
																				43)))));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jScrollPane1,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												243,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(ButtonOK).add(
																ButtonCanel))
										.addContainerGap(12, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void ButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonOKActionPerformed

		openFile();
	}// GEN-LAST:event_ButtonOKActionPerformed

	private void ButtonCanelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonCanelActionPerformed

		dispose();
	}// GEN-LAST:event_ButtonCanelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton ButtonCanel;
	private javax.swing.JButton ButtonOK;
	private javax.swing.JTree TreeFile;
	private javax.swing.JScrollPane jScrollPane1;
	// End of variables declaration//GEN-END:variables

}
