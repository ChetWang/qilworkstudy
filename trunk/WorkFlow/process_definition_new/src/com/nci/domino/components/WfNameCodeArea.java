package com.nci.domino.components;

import java.awt.GridBagConstraints;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.help.pinyin.PinyinHelper;
import com.nci.domino.shape.WfActivity;
import com.nci.domino.shape.basic.AbstractShape;

/**
 * 流程各类属性定义中有关名称，编码的组件，将二者合成一个对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfNameCodeArea {

	private JLabel nameLabel;

	private JLabel codeLabel;

	private WfOverlayableTextField codeField;

	public WfOverlayableTextField nameField;

	private WfDialog dialog;

	public WfNameCodeArea(String nameLabelText, String codeLabelText,
			WfDialog dialog) {
		this.dialog = dialog;
		nameLabel = new JLabel(nameLabelText);
		codeLabel = new JLabel(codeLabelText);
		String processCodeFieldTip = "只允许英文\"a-z  A-Z\",\"-\",\"_\",及数字0-9";
		WfTextDocument codeDoc = new WfTextDocument(38, "[a-z0-9A-Z_-]*",
				processCodeFieldTip);
		codeDoc.setWfDialog(dialog);
		codeField = new WfOverlayableTextField(codeDoc);

		WfTextDocument nameDoc = new WfTextDocument(38);
		nameDoc.setWfDialog(dialog);
		nameDoc.addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e) {
				parsePinyin();
			}

			public void insertUpdate(DocumentEvent e) {
				parsePinyin();
			}

			public void changedUpdate(DocumentEvent e) {
				parsePinyin();
			}
		});
		nameField = new WfOverlayableTextField(nameDoc);
		dialog.setInitFocusedComponent(nameField);
	}

	/**
	 * 初始化界面，仅对GridbagLayout有效
	 * 
	 * @param gridx
	 * @param gridy
	 * @param fieldLength
	 * @param containter
	 * @param cons
	 */
	public void init(int gridx, int gridy, int fieldLength, JPanel containter,
			GridBagConstraints cons) {
		cons.gridx = gridx;
		cons.gridy = gridy;
		cons.gridwidth = 1;
		containter.add(nameLabel, cons);

		cons.gridx = gridx + 1;
		cons.gridwidth = fieldLength;
		containter.add(nameField.getOverlayableTextField(), cons);

		cons.gridx = gridx;
		cons.gridy = gridy + 1;
		cons.gridwidth = 1;
		containter.add(codeLabel, cons);

		cons.gridx = gridx + 1;
		cons.gridwidth = fieldLength;
		containter.add(codeField.getOverlayableTextField(), cons);
	}

	public String checkDuplicateProcessCode() {
		if (reseting)
			return null;
		return null;
	}

	/**
	 * 在单个流程中检查输入活动的代码是否重复
	 * 
	 * @return
	 */
	public String checkDuplicateActivityCode() {
		if (reseting)
			return null;
		final String currentTypedCode = getCode();
		List<AbstractShape> existShapes = dialog.getEditor().getOperationArea()
				.getCurrentPaintBoard().getGraphVector();
		WofoActivityBean currentActivity = (WofoActivityBean) dialog
				.getDefaultValue();
		boolean flag = false;
		String name = "";
		for (AbstractShape s : existShapes) {
			if (s instanceof WfActivity) {
				WofoActivityBean actBean = (WofoActivityBean) ((WfActivity) s)
						.getWofoBean();
				if (!currentActivity.getActivityId().equals(
						actBean.getActivityId())
						&& currentTypedCode.equals(actBean.getActivityCode())) {
					flag = true;
					name = actBean.getActivityName();
					break;
				}
			}
		}
		if (flag) {
			codeField.requestFocus();
			codeField.selectAll();
			return "\"" + currentTypedCode + "\"已经存在，属于\"" + name + "\"，请改名！";
		}
		return null;
	}

	/**
	 * 将名称首字母自动转换成编码
	 */
	private void parsePinyin() {
		codeField.setText(PinyinHelper.getAllFirstLetter(getName())
				.toUpperCase());
	}

	/**
	 * 获取中文名称
	 * 
	 * @return
	 */
	public String getName() {
		return nameField.getText().trim();
	}

	/**
	 * 获取编码（英文名称）
	 * 
	 * @return
	 */
	public String getCode() {
		return codeField.getText().trim();
	}

	/**
	 * 设置中文名称,英文编码
	 * 
	 * @param name
	 */
	public void setNameCode(String name, String code) {
		nameField.setText(name);
		codeField.setText(code);
	}

	boolean reseting = false;

	/**
	 * 清空
	 */
	public void reset() {
		reseting = true;
		nameField.setText("");
		codeField.setText("");
		reseting = false;
	}

}
