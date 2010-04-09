package com.nci.domino.shape.pipe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.plugin.pipe.WofoPipeStageBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.dialog.WfDialog;

/**
 * 阶段输入对话框
 * 
 * @author Qil.Wong
 * 
 */
public class WfPipeStageDialog extends WfDialog {

	public WfPipeStageDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 350;
		defaultHeight = 280;
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("阶段", "阶段是业务流程生效所处的时间段",
				null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		BasicPanel basic = new BasicPanel();
		customPanels.add(basic);
		setInitFocusedComponent(basic.nameField);
		return contentTab;
	}

	private class BasicPanel extends WfInputPanel {
		JLabel nameLabel = new JLabel("名称：");
		JLabel descLabel = new JLabel("描述：");
		WfOverlayableTextField nameField;
		JTextArea descArea;

		public BasicPanel() {
			init();
			panelName = "属性";
		}

		private void init() {
			WfTextDocument nameDoc = new WfTextDocument(64);
			nameDoc.setWfDialog(WfPipeStageDialog.this);
			nameField = new WfOverlayableTextField(nameDoc);
			WfTextDocument descDoc = new WfTextDocument(200);
			descDoc.setWfDialog(WfPipeStageDialog.this);
			descArea = new JTextArea(descDoc);
			descArea.setLineWrap(true);
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 0;
			cons.fill = GridBagConstraints.BOTH;
			cons.insets = new Insets(4, 4, 4, 4);
			add(nameLabel, cons);
			cons.gridx = 1;
			cons.weightx = 1;
			add(nameField.getOverlayableTextField(), cons);
			cons.gridx = 0;
			cons.gridy = 1;
			cons.weightx = 0;
			add(descLabel, cons);
			cons.gridx = 1;
			cons.weightx = 1;
			cons.weighty = 1;
			add(new JideScrollPane(descArea), cons);
		}

		public String check() {
			if (nameField.getText().trim().equals("")) {
				return "显示名称不能为空！";
			}
			return null;
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoPipeStageBean pipeSBean = (WofoPipeStageBean) value;
			pipeSBean.setName(nameField.getText().trim());
			pipeSBean.setDesc(descArea.getText().trim());
			return pipeSBean;
		}

		@Override
		public void reset() {
			nameField.setText("");
			descArea.setText("");
		}

		@Override
		public void setValues(Serializable value) {
			WofoPipeStageBean pipeSBean = (WofoPipeStageBean) value;
			nameField.setText(pipeSBean.getName());
			descArea.setText(pipeSBean.getDesc());
		}

	}

}
