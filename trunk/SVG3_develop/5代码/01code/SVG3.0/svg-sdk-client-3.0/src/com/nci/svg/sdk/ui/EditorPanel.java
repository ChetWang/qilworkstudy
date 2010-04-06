package com.nci.svg.sdk.ui;

import javax.swing.JPanel;

import com.nci.svg.sdk.client.EditorAdapter;


public abstract class EditorPanel extends JPanel{
	
	protected EditorAdapter editor;
	
	public EditorPanel(EditorAdapter editor){
		super();
		this.editor = editor;
	}
	
}
