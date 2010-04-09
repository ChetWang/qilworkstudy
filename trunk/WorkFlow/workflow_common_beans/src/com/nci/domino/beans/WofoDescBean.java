package com.nci.domino.beans;

/**
 * 属于描述性的对象，与流程运行无关，集中保存为一个blob的xml
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WofoDescBean extends WofoBaseBean {

	protected String id;

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}
}
