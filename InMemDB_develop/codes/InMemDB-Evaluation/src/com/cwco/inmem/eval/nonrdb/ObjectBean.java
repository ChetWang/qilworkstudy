package com.cwco.inmem.eval.nonrdb;

import java.io.Serializable;

public class ObjectBean implements Serializable{

	private int index;
	private String[] value;

	public ObjectBean() {
	}

	public ObjectBean(int index, String[] value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String[] getValue() {
		return value;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

}
