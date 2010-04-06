package com.nci.ums.jmx.desktop.bean;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.Toolkit;

public class NumOnlyDocument extends PlainDocument {

	private boolean haveDot = false;
	private int length = 0;
	private int maxLength = -1;

	public NumOnlyDocument() {

	}

	public NumOnlyDocument(int maxLength) {
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
			if (offset == 0) {
				if (!(number[i] >= '0' && number[i] <= '9' || number[i] == '.'
						|| number[i] == '-' || number[i] == '+')) {
					if (offset == length - 1) {
						remove(offset + i, 1);
					} else {
						return;
					}

				} else {
					length++;
				}
			} else {
				if (this.getText(0, this.getLength()).indexOf(".") == -1) {
					haveDot = false;
				}
				if (!haveDot) {
					if (!(number[i] >= '0' && number[i] <= '9' || number[i] == '.')) {
						if (offset == length - 1) {
							remove(offset + i, 1);
						} else {
							return;
						}
					} else {
						if (number[i] == '.') {
							haveDot = true;
						}
						length++;
					}
				} else {
					if (!(number[i] >= '0' && number[i] <= '9')) {
						if (offset == length - 1) {
							remove(offset + i, 1);
						} else {
							Toolkit.getDefaultToolkit().beep();
							return;
						}
					} else {
						length++;
					}
				}
			}
		}
		super.insertString(offset, new String(number), attrSet);
	}
}
