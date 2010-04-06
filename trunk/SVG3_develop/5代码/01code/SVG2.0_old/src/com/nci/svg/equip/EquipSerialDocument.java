package com.nci.svg.equip;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 设备的编号只允许数字加英文
 * @author Qil.Wong
 *
 */
public class EquipSerialDocument extends PlainDocument {

	private int length = 0;
	private int maxLength = -1;

	public EquipSerialDocument() {

	}

	public EquipSerialDocument(int maxLength) {
		this.maxLength = maxLength;
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
			}
		}
		char[] number = str.toCharArray();
		for (int i = 0; i < number.length; i++) {
			if (!((number[i] >= '0' && number[i] <= '9') || (number[i] >= 'a'
					&& number[i] <= 'z') || (number[i] >= 'A' && number[i] <= 'Z'))) {
				if (offset == length - 1) {
					remove(offset + i, 1);
				} else {
					return;
				}
			} else {
				length++;
			}
		}
		super.insertString(offset, new String(number), attrSet);
	}
}
