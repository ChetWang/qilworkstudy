
package com.nci.svg.sdk.ui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JDialog;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-6
 * @功能：自定义组合对话框
 *
 */
public class NciCustomDialog extends JDialog {
	private static final long serialVersionUID = 298157702425609486L;

	int index = 0;

	GridBagLayout layout;

	public NciCustomDialog(Frame f, boolean modal) {
		super(f, modal);
		init();
//		this.setResizable(false);
	}

	private void init() {
		layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1.0f };
		// layout.rowWeights = new double[] { 1.0f };
		this.getContentPane().setLayout(layout);
		pack();
	}

	public void addComponent(JComponent comp) {
		// comp.setBorder(new EtchedBorder());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.gridx = 0;
		cons.gridy = index;
		index++;
		cons.insets = new Insets(0, 0, 0, 0);
		cons.weightx = 1.0;
		layout.setConstraints(comp, cons);
		this.getContentPane().add(comp);
		pack();
	}
}
