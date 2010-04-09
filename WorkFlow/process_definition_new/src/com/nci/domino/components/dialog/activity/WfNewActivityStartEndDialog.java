package com.nci.domino.components.dialog.activity;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.help.WofoResources;

/**
 * ���̿�ʼ�ͽ�����ĶԻ���
 * 
 * @author Qil.Wong
 * 
 */
public class WfNewActivityStartEndDialog extends WfActivityDialog {

	private static final long serialVersionUID = 1655756334937094823L;

	private StartEndContentPanel content = new StartEndContentPanel();

	public enum StartEndActivity {
		START, END
	}

	private StartEndActivity startOrEnd = StartEndActivity.END;

	public WfNewActivityStartEndDialog(WfEditor editor, String title,
			boolean modal) {
		super(editor, title, modal);
		defaultWidth = 500;
		defaultHeight = 320;
	}

	public void showWfDialog(int width, int height, Serializable defaultValue) {
		if (getTitle().startsWith("��ʼ")) {
			startOrEnd = StartEndActivity.START;
			getBannerPanel().setTitle("��ʼ����");
			getBannerPanel().setSubtitle("���̿�ʼ����������ҵ�����̵ĵ�һ����ʼ��");

		} else {
			startOrEnd = StartEndActivity.END;
			getBannerPanel().setTitle("��������");
			getBannerPanel().setSubtitle("���̽�������������ҵ�����̵����һ���");

		}
		super.showWfDialog(width, height, defaultValue);
	}

	@Override
	protected String checkInput() {
		if (this.content.nameField.getText().trim().equals("")) {
			return "�������Ʋ���Ϊ��";
		}
		return super.checkInput();
	}

	@Override
	public void clearContents() {
		super.clearContents();
		content.reset();
	}

	@Override
	public Serializable getInputValues() {
		activity = (WofoActivityBean) super.getInputValues();
		content.applyValues(activity);
		return activity;
	}

	@Override
	public void setInputValues(WofoActivityBean value) {
		content.setValues(value);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("���̿�ʼ�",
				"���̿�ʼ�������ҵ�����̵ĵ�һ����ʼ��", null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		customPanels.add(content);
		setInitFocusedComponent(content.nameField);
		return contentTab;
	}

	private class StartEndContentPanel extends WfInputPanel {

		JLabel typeLabel = new JLabel("���ͣ�");
		JTextField typeField = new JTextField();
		JLabel workFlowLabel = new JLabel("ҵ�����̣�");
		JTextField workFlowField = new JTextField();;
		JLabel codeLabel = new JLabel("����Ӣ������");
		WfOverlayableTextField codeField;
		JLabel nameLabel = new JLabel("������������");
		WfOverlayableTextField nameField;
		JLabel descLabel = new JLabel("������");
		JTextArea descField = new JTextArea();

		public StartEndContentPanel() {
			init();
			panelName = "����";
		}		

		private void init() {
			typeField.setEnabled(false);
			descField.setAutoscrolls(true);
			String processCodeFieldTip = "ֻ����Ӣ��\"a-z  A-Z\",\"-\",\"_\",������0-9";
			WfTextDocument codeDoc = new WfTextDocument(38, "[a-z0-9A-Z_-]*",
					processCodeFieldTip);
			codeField = new WfOverlayableTextField(codeDoc);
			WfTextDocument nameDoc = new WfTextDocument(38);
			nameField = new WfOverlayableTextField(nameDoc);
			workFlowField.setEnabled(false);
			codeField.setEnabled(false);
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.insets = new Insets(3, 5, 3, 5);
			cons.anchor = GridBagConstraints.WEST;
			cons.gridx = 0;
			cons.gridy = 0;
			add(typeLabel, cons);
			cons.gridy = 1;
			add(workFlowLabel, cons);
			cons.gridy = 2;
			add(codeLabel, cons);
			cons.gridy = 3;
			add(nameLabel, cons);
			cons.gridy = 4;
			add(descLabel, cons);
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.weightx = 1;
			cons.gridx = 1;
			cons.gridy = 0;
			add(typeField, cons);
			cons.gridy = 1;
			add(workFlowField, cons);
			cons.gridy = 2;
			add(codeField.getOverlayableTextField(), cons);
			cons.gridy = 3;
			add(nameField.getOverlayableTextField(), cons);
			cons.gridy = 4;
			cons.weighty = 1;
			cons.fill = GridBagConstraints.BOTH;
			JideScrollPane scroll = new JideScrollPane(descField);
			add(scroll, cons);
		}

		@Override
		public void reset() {
			switch (startOrEnd) {
			case START:
				typeField.setText("��ʼ");
				codeField.setText("BEGIN");
				// descField.setText("һ�����̵Ŀ�ʼ�");

				break;
			case END:
				typeField.setText("����");
				codeField.setText("END");
				// descField.setText("һ�����̵Ľ����");

				break;
			default:
				break;
			}
			workFlowField.setText("");
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoActivityBean acitvityValue = (WofoActivityBean) value;
			acitvityValue.setActivityName(nameField.getText().trim());
			acitvityValue.setActivityCode(codeField.getText().trim());
			acitvityValue.setDescription(this.descField.getText().trim());
			acitvityValue
					.setActivityStatus(WofoActivityBean.ACTIVITY_STATUS_ONLINE);
			return acitvityValue;
		}

		@Override
		public void setValues(Serializable value) {
			String defaultString = startOrEnd == StartEndActivity.START ? "��ʼ"
					: "����";
			WofoActivityBean acitvityValue = (WofoActivityBean) value;
			this.codeField.setText(acitvityValue.getActivityCode());

			this.descField
					.setText("".equals(acitvityValue.getDescription()) ? defaultString
							: acitvityValue.getDescription());
			this.nameField
					.setText("".equals(acitvityValue.getActivityName()) ? defaultString
							: acitvityValue.getActivityName());
			this.workFlowField.setText(editor.getOperationArea()
					.getCurrentPaintBoard().getProcessBean().getProcessName());
			this.typeField.setText(WofoResources
					.getValueByKey("workflow_activity_"
							+ acitvityValue.getActivityType()));
		}

	}

	public boolean needArguments() {
		return false;
	}
}
