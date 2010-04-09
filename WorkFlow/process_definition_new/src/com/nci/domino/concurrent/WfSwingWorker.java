package com.nci.domino.concurrent;

import java.awt.event.ActionListener;

import com.jidesoft.utils.SwingWorker;

/**
 * 当前程序的EDT外后台处理对象，所有后台的都在SwingWorker中的doInBackGround中执行，得到结果后再将结果反馈给EDT线程
 * 
 * @author Qil.Wong
 * 
 * @param <T>
 * @param <V>
 */
public abstract class WfSwingWorker<T, V> extends SwingWorker<T, V> {

	private String tipInfo;

	private ActionListener doneListener;

	private ActionListener cancelListener;

	public WfSwingWorker(String tipInfo) {
		this.tipInfo = tipInfo;
	}

	public String getTipInfo() {
		return tipInfo;
	}

	public void done() {
		try {
			if (doneListener != null) {
				doneListener.actionPerformed(null);
			}
		} finally {
			if (!isCancelled()) {
				wfDone();
			}
		}
	}

	/**
	 * 取消当前执行的后台进程
	 * 
	 * @param mayInterruptIfRunning
	 */
	public void cancelWorking(boolean mayInterruptIfRunning) {
		try {
			if (cancelListener != null) {
				cancelListener.actionPerformed(null);
			}
		} finally {
			cancel(mayInterruptIfRunning);
		}
	}

	/**
	 * 工作流组件执行完后台任务后的动作
	 */
	public abstract void wfDone();

	public ActionListener getDoneListener() {
		return doneListener;
	}

	public void setDoneListener(ActionListener doneListener) {
		this.doneListener = doneListener;
	}

	public ActionListener getCancelListener() {
		return cancelListener;
	}

	public void setCancelListener(ActionListener cancelListener) {
		this.cancelListener = cancelListener;
	}
}
