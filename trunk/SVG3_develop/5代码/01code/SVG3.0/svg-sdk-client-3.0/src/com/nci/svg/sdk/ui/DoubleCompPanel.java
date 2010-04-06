package com.nci.svg.sdk.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DoubleCompPanel extends JPanel {

	private JComponent comp1;

	private JComponent comp2;

	public static Dimension labelSize = new Dimension(75, 22);

	public static Dimension compSize = new Dimension(250, 23);

	public DoubleCompPanel(JComponent comp1, JComponent comp2) {
		this.comp1 = comp1;
		this.comp2 = comp2;
		init();
	}

	private void init() {
		if (comp1 instanceof JButton == false)
			comp1.setPreferredSize(labelSize);
		if (comp2 instanceof JButton == false)
			comp2.setPreferredSize(compSize);
		GridBagLayout rootLayout = new GridBagLayout();
		rootLayout.columnWeights = new double[] { 0.0 };
//		rootLayout.rowWeights = new double[] { 1.0 };
		this.setLayout(rootLayout);

		GridBagConstraints comp1Cons = new GridBagConstraints();
		comp1Cons.weightx = 0.3;
		
		comp1Cons.fill = GridBagConstraints.BOTH;		
		comp1Cons.gridy = 0;
		if (comp1 instanceof JButton){
			comp1Cons.gridx = 3;
			comp1Cons.ipady = 40;
		}
		else
			comp1Cons.gridx = 0;
		comp1Cons.anchor = GridBagConstraints.WEST;
		rootLayout.setConstraints(comp1, comp1Cons);

		GridBagConstraints comp2Cons = new GridBagConstraints();
		comp2Cons.weightx = 0.7;
		
		comp2Cons.fill = GridBagConstraints.BOTH;
		if (comp2 instanceof JButton){
			comp2Cons.gridx = 4;
		}
		else
			comp2Cons.gridx = 0;
		comp2Cons.gridy = 0;
		
		comp2Cons.anchor = GridBagConstraints.WEST;
		rootLayout.setConstraints(comp1, comp2Cons);

		this.add(comp1);
		this.add(comp2);
	}

}
