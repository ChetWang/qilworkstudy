package com.nci.svg.sdk.logntermtask;

import org.jdesktop.swingworker.SwingWorker;

/**
 * ºÄÊ±²Ù×÷µÄjavabean
 * @author Qil.Wong
 *
 */
public class LongtermTask {
	
	private String operationInfo;
	private SwingWorker<Object,Object> worker;
	
	public LongtermTask(){}	
	
	public LongtermTask(String operationInfo,SwingWorker worker){
		this.operationInfo = operationInfo;
		this.worker = worker;
	}

	public String getOperationInfo() {
		return operationInfo;
	}

	public void setOperationInfo(String operationInfo) {
		this.operationInfo = operationInfo;
	}

	public SwingWorker<Object,Object> getSwingWorker() {
		return worker;
	}

	public void setSwingWorker(SwingWorker<Object,Object> worker) {
		this.worker = worker;
	}
	
	

}
