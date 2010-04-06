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
	 * ���ݲ�ͬ��xml��ȡcss
	 * @param xmlName
	 * @return
	 */
	public abstract String getData(String xmlName);

}
