package com.nci.svg.ui;

import javax.swing.JPanel;

import fr.itris.glips.svgeditor.Editor;

public abstract class EditorPanel extends JPanel{
	
	protected Editor editor;
	
	public EditorPanel(Editor editor){
		super();
		this.editor = editor;
	}
	
}
