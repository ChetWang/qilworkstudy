package com.nci.domino.components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.nci.domino.components.dialog.WfDialog;

/**
 * �ı���ʽ��Document������textfield��textarea
 * 
 * @author Qil.Wong
 * 
 */
public class WfTextDocument extends PlainDocument {

	private static final long serialVersionUID = -3345757826961552042L;

	private int maxLength = -1;

	private String regExpr;

	private WfDialog wfDialog;

	private String regTip;

	public WfTextDocument() {

	}

	public WfTextDocument(int maxLength) {
		this.maxLength = maxLength;
	}

	public WfTextDocument(int maxLength, String regExpr, String regTip) {
		this.maxLength = maxLength;
		this.regExpr = regExpr;
		this.regTip = regTip;
	}

	public void insertString(int offset, String str, AttributeSet attrSet)
			throws BadLocationException {
		if (str == null) {
			return;
		}
		if (maxLength > -1) {
			int i = maxLength - getText(0, getLength()).getBytes().length;
			if (str.getBytes().length > i) {
				str = "";
				if (wfDialog != null) {
					wfDialog.getBannerPanel().setTip(
							"�ܳ��Ȳ��ܳ���" + maxLength + "���ַ���" + maxLength / 2
									+ "������");
				}
			}
		}
		if (regExpr != null && !regExpr.trim().equals("") && !str.equals("")
				&& !str.matches(regExpr)) {
			str = "";
			if (wfDialog != null) {
				wfDialog.getBannerPanel().setTip(regTip);
			}
		}
		super.insertString(offset, str, attrSet);
	}

	public String getRegExpr() {
		return regExpr;
	}

	public void setRegExpr(String regExpr) {
		this.regExpr = regExpr;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public WfDialog getWfDialog() {
		return wfDialog;
	}

	/**
	 * ���������Ի����Ա�������ʾ��Ϣ�����ڶԻ���ͷ��ʾ����
	 * @param wfDialog
	 */
	public void setWfDialog(WfDialog wfDialog) {
		this.wfDialog = wfDialog;
	}
}
