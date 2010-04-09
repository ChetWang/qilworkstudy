package com.nci.domino.components.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfDepartmentChooser;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * �½����ĶԻ���
 * 
 * @author Qil.Wong
 * 
 */
public class WfNewPackageDialog extends WfDialog {

	private static final long serialVersionUID = -4582422485310829743L;

	public WfNewPackageDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 590;
		defaultHeight = 430;
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("������������Ϣ",
				"���̰��Ǹ�������Ϣ������ܳ�.\n������������������������Ҫָ��ѡ��������������Ϣ.", Functions
						.getImageIcon("41.gif"));
		return headerPanel1.getGlassBanner();
	}

	private WfOverlayableTextField newPackgeNameField;

	// �����ı��ؼ�
	private JTextArea descriptionArea;

	private WfDepartmentChooser departChooser;

//	private WofoPackageBean packageBean;

	@Override
	public JComponent createContentPanel() {
		descriptionArea = new JTextArea();
		descriptionArea.setAutoscrolls(true);
		WfTextDocument doc = new WfTextDocument(500);
		doc.setWfDialog(this);
		descriptionArea.setDocument(doc);

		JScrollPane descScroll = new JideScrollPane(descriptionArea);

		WfTextDocument doc2 = new WfTextDocument(38);
		doc2.setWfDialog(this);
		newPackgeNameField = new WfOverlayableTextField(doc2);

		JLabel processSetNameLabel = new JLabel(WofoResources
				.getValueByKey("package_name"));
		processSetNameLabel
				.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);

		JLabel descLabel = new JLabel(WofoResources
				.getValueByKey("package_desc"));
		descLabel.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);

		JPanel root = new JPanel(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = 0;
		cons.gridy = 0;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.insets = new Insets(5, 5, 5, 5);
		root.add(processSetNameLabel, cons);

		cons.gridx = 1;
		cons.gridwidth = 5;
		cons.weightx = 1;
		root.add(newPackgeNameField.getOverlayableTextField(), cons);

		departChooser = new WfDepartmentChooser(editor, root);
		departChooser.init(0, 1, 5);
		// departChooser�ĸ߶�ռ2�����ӣ����������gridyҪ��3��ʼ
		cons.gridx = 0;
		cons.gridy = 3;
		cons.weightx = 0;
		cons.gridwidth = 1;
		root.add(descLabel, cons);

		cons.gridx = 1;
		cons.gridy = 3;
		cons.gridwidth = 5;
		cons.weightx = 1.0f;
		cons.weighty = 1.0f;
		cons.fill = GridBagConstraints.BOTH;
		root.add(descScroll, cons);

		root.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentTab.addTab("����", root);
		return contentTab;
	}

	@Override
	public void clearContents() {
		super.clearContents();
		if (departChooser != null && departChooser.getDepartmentTree() != null)
			departChooser.getDepartmentTree().reset();
		departChooser.clearContents();
		this.newPackgeNameField.setText("");
		this.descriptionArea.setText("");
	}


	@Override
	public WofoPackageBean getInputValues() {
		defalutValue = super.getInputValues();
		WofoPackageBean packageBean = (WofoPackageBean)defalutValue;
		packageBean.setPackageName(newPackgeNameField.getText().trim());
		packageBean.setCreateDate(new Date());
		packageBean.setCreatorId(editor.getUserID());
		packageBean.setEffectDepartmentId(departChooser.getDepartmentID());
		packageBean.setEffectUnitId(departChooser.getCompanyID());
		packageBean.setDescription(descriptionArea.getText().trim());
		packageBean.setDisplayOrder(editor.getOperationArea().getWfTree()
				.getSelectedNode().getChildCount() + 1);
		return packageBean;
	}

	@Override
	public void setInputValues(Serializable value) {
		if (value instanceof WofoPackageBean) {
			defalutValue = value;
			WofoPackageBean packageBean = (WofoPackageBean)defalutValue;
			newPackgeNameField.setText(packageBean.getPackageName());
			descriptionArea.setText(packageBean.getDescription());
			departChooser.setDepartmentValue(packageBean.getEffectUnitId(),
					packageBean.getEffectDepartmentId());
		} else {
			new IllegalInputTypeException(WofoPackageBean.class, value
					.getClass()).printStackTrace();
		}
	}

	@Override
	protected String checkInput() {
		if (newPackgeNameField.getText().trim().equals("")) {
			return "\n����ָ��������";
		}
		return super.checkInput();
	}

}
