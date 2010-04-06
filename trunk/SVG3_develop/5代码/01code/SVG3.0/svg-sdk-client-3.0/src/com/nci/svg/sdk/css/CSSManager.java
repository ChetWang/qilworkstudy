package com.nci.svg.sdk.css;

import com.nci.svg.sdk.client.EditorAdapter;

public abstract class CSSManager {
	
	protected EditorAdapter editor;
	
	public CSSManager(EditorAdapter editor){
		this.editor = editor;
		
	}
	
	public EditorAdapter getEditor(){
		return editor;
	}
	
	/**
	 * ���ݲ�ͬ��xml��ȡcss
	 * @param xmlName
	 * @return
	 */
	public abstract String getData(String xmlName);

}
