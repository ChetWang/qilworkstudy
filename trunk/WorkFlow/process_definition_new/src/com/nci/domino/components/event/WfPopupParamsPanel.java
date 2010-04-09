package com.nci.domino.components.event;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.text.JTextComponent;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.components.WfArgumentsPanel;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.help.WofoResources;

/**
 * <p>
 * 标题：WfPopupParamsPanel.java
 * </p>
 * <p>
 * 描述： 参数选择弹出窗口类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2010-3-31
 * @version 1.0
 */
public class WfPopupParamsPanel extends JPanel {
	private static final long serialVersionUID = -6690445797453361477L;

	// private WfDialog parentDialog;
	private CheckBoxList argList = new CheckBoxList();
	private JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
	DefaultListModel model = new DefaultListModel();

	public WfPopupParamsPanel(final JidePopup popup, final JComponent comp,
			final boolean multiple, WfDialog parentDialog) {
		// this.parentDialog = parentDialog;

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
				Object[] args = argList.getCheckBoxListSelectedValues();
				
				if (comp instanceof JTextComponent) {
					StringBuffer paramValue = new StringBuffer();
					for (Object o : args) {
						paramValue.append("{").append(o.toString()).append("}");
					}
					JTextComponent textComp = (JTextComponent) comp;
					textComp.setText(textComp.getText() + paramValue.toString());
				}
				
				if(comp instanceof JList){
					JList listComp = (JList) comp;
					DefaultListModel listModel = (DefaultListModel) listComp.getModel();
					for(Object o : args){
						listModel.addElement(o);
					}
				}
			}
		});
		add(btnPanel, BorderLayout.SOUTH);

		WfArgumentsPanel argPanel = null;
		// 取到参数面板
		for (JPanel p : parentDialog.getCustomPanels()) {
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
