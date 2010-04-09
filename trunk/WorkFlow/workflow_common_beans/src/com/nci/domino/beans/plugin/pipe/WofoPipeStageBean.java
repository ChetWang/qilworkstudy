package com.nci.domino.beans.plugin.pipe;

/**
 * Ó¾µÀ½×¶Î¶ÔÏó
 * 
 * @author Qil.Wong
 * 
 */
public class WofoPipeStageBean extends WofoPipeBaseBean {

	private String name;
	private String desc;

	public WofoPipeStageBean() {
	}
	
	public WofoPipeStageBean(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		showText = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return name;
	}

	public void setShowText(String t) {
		super.setShowText(t);
		name = t;
	}

}
