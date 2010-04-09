package com.nci.domino.shape.note;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.plugin.note.WofoNoteBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.dialog.WfDialog;

/**
 * 备注信息窗口
 * 
 * @author Qil.Wong
 * 
 */
public class WfNoteInputDialog extends WfDialog {

	public WfNoteInputDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 320;
		defaultHeight = 260;
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("备注信息",
				"对流程某个阶段或活动进行备注说明", null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {	
		BasicPanel basic = new BasicPanel();
		customPanels.add(basic);
		setInitFocusedComponent(basic.area);
		return contentTab;
	}

	public void okActionPerformed(ActionEvent e) {
		if (getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			getInputValues();
		}
	}

	/**
	 * 内容输入界面
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class BasicPanel extends WfInputPanel {

		JTextArea area;

		public BasicPanel() {
			init();
			panelName = "备注";
		}

		private void init() {
			area = new JTextArea();
			WfTextDocument doc = new WfTextDocument(1280);
			doc.setWfDialog(WfNoteInputDialog.this);
			area.setDocument(doc);
			setLayout(new BorderLayout());
			JideScrollPane scroll = new JideScrollPane(area);
			add(scroll, BorderLayout.CENTER);
			area.setLineWrap(true);
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoNoteBean bean = (WofoNoteBean) value;
			bean.setValue(area.getText().trim());
			return bean;
		}

		@Override
		public void reset() {
			area.setText("");
		}

		@Override
		public void setValues(Serializable value) {
			WofoNoteBean bean = (WofoNoteBean) value;
			area.setText(bean.getValue() == null ? "" : bean.getValue());
		}

	}

}
