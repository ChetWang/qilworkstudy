package com.nci.svg.sdk.ui.graphunit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class SymbolNameDocument extends PlainDocument {
	
   private static final long serialVersionUID = -654710710859465612L;
   
	/**
	 * symbol名称的最大长度
	 */
	public static final int SYMBOL_NAME_MAX_LENGTH = 64;

	public void insertString(int offset, String str,
			AttributeSet attrSet) throws BadLocationException {
		if (str == null) {
			return;
		}
		if (SYMBOL_NAME_MAX_LENGTH > -1) {
			int i = SYMBOL_NAME_MAX_LENGTH
					- getText(0, getLength()).getBytes().length;
			if (str.getBytes().length > i) {
				str = "";
			}
		}
		super.insertString(offset, str, attrSet);
	}

}
