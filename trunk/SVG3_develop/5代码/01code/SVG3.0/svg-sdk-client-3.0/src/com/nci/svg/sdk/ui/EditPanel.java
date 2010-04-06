/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditPanel.java
 *
 * Created on 2008-12-26, 16:29:12
 */

package com.nci.svg.sdk.ui;

import java.awt.FlowLayout;

/**
 * 
 * @author yx.nci
 */
public class EditPanel extends javax.swing.JPanel {

	public EditPanel() {
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new FlowLayout());
		showText = new javax.swing.JLabel();
		editText = new javax.swing.JTextField();
		this.add(new DoubleCompPanel(showText, editText));

	}


	private javax.swing.JTextField editText;
	private javax.swing.JLabel showText;

	public javax.swing.JTextField getEditText() {
		return editText;
	}

	/**
	 * 
	 * @return the showText
	 */
	public javax.swing.JLabel getShowText() {
		return showText;
	}

}
