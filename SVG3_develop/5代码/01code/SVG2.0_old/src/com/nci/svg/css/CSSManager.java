package com.nci.svg.css;

import fr.itris.glips.svgeditor.Editor;

public abstract class CSSManager {
	
	protected Editor editor;
	
	public CSSManager(Editor editor){
		this.editor = editor;
	}
	
	public Editor getEditor(){
		return editor;
	}
	
	/**
	 * 根据不同的xml获取css
	 * @param xmlName
	 * @return
	 */
	public abstract String getData(String xmlName);

}
