/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import org.jdesktop.swingworker.SwingWorker;

import com.nci.svg.logntermtask.LongtermTask;

import fr.itris.glips.svgeditor.Editor;

/**
 * 
 * @author Qil.Wong
 */
public class NCIStatusBar extends JPanel {

	private Editor editor;

	private javax.swing.JLabel operationInfoLabel;
	private javax.swing.JComponent emptyComp1;
	private javax.swing.JComponent emptyComp2;
	private javax.swing.JProgressBar progressBar;


	public NCIStatusBar(Editor editor) {
		super();
		this.editor = editor;
		initComponents();
	}

	private void initComponents() {
		operationInfoLabel = new javax.swing.JLabel();
		operationInfoLabel.setVisible(false);
		progressBar = new javax.swing.JProgressBar();
		progressBar.setVisible(false);
		emptyComp1 = new javax.swing.JLabel();
		emptyComp2 = new javax.swing.JLabel();
		GridBagConstraints gbc_space1 = new GridBagConstraints();
		gbc_space1.gridx = 0;
		gbc_space1.gridy = 0;
		gbc_space1.insets = new Insets(5, 5, 5, 5);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridx = 2;
		gbc_label.gridy = 0;
		gbc_label.insets = new Insets(5, 5, 5, 20);
		GridBagConstraints gbc_progress = new GridBagConstraints();
		gbc_progress.gridx = 3;
		gbc_progress.gridy = 0;
		gbc_progress.insets = new Insets(5, 5, 5, 5);
		GridBagLayout gbl_panel1 = new GridBagLayout();
		this.setLayout(gbl_panel1);
		operationInfoLabel.setText("Condition");
		gbl_panel1.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0 };// À­Éý·ù¶È
		gbl_panel1.rowWeights = new double[] { 1.0f };
		gbl_panel1.setConstraints(emptyComp1, gbc_space1);
		gbl_panel1.setConstraints(emptyComp2, gbc_space1);
		gbl_panel1.setConstraints(operationInfoLabel, gbc_label);
		gbl_panel1.setConstraints(progressBar, gbc_progress);
		add(emptyComp1);
		add(emptyComp2);
		add(operationInfoLabel);
		add(progressBar);
	}

	public Editor getEditor() {
		return editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public javax.swing.JLabel getOperationInfoLabel() {
		return operationInfoLabel;
	}

	public void setOperationInfoLabel(javax.swing.JLabel operationInfoLabel) {
		this.operationInfoLabel = operationInfoLabel;
	}

	public javax.swing.JComponent getFirstEmptyComponent() {
		return emptyComp1;
	}

	public void setSecondEmptyComponent(javax.swing.JComponent emptyComp2) {
		this.emptyComp2 = emptyComp2;
	}

	public javax.swing.JComponent getSecondEmptyComponent() {
		return emptyComp2;
	}

	public void setFirstEmptyComponent(javax.swing.JComponent emptyComp1) {
		this.emptyComp1 = emptyComp1;
	}

	public javax.swing.JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(javax.swing.JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

}
