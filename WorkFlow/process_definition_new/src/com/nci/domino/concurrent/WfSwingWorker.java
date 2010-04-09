package com.nci.domino.concurrent;

import java.awt.event.ActionListener;

import com.jidesoft.utils.SwingWorker;

/**
 * ��ǰ�����EDT���̨����������к�̨�Ķ���SwingWorker�е�doInBackGround��ִ�У��õ�������ٽ����������EDT�߳�
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
	 * ȡ����ǰִ�еĺ�̨����
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
	 * ���������ִ�����̨�����Ķ���
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
