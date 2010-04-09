package com.nci.domino.concurrent;

public abstract class WfRunnable implements Runnable {

	private String tipInfo;

	public WfRunnable(String tipInfo) {
		this.tipInfo = tipInfo;
	}

	public String getTipInfo() {
		return tipInfo;
	}

}
