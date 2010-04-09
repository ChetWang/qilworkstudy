package com.nci.domino.concurrent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Timer;

import com.nci.domino.WfEditor;

/**
 * ��̨����������
 * 
 * @author Qil.Wong
 * 
 */
public class WfBackgroundRunner {

	private WfEditor editor;

	private LinkedBlockingQueue<WfRunnable> startupQueue = new LinkedBlockingQueue<WfRunnable>();

	private LinkedBlockingQueue<WfSwingWorker<?, ?>> operTimeQueue = new LinkedBlockingQueue<WfSwingWorker<?, ?>>();

	// �����׶κ�̨�̴߳����
	private boolean startupAlive = true;

	private boolean operTimeAlive = true;

	public WfBackgroundRunner(WfEditor editor) {
		this.editor = editor;
		new StartupThread().start();
		new OpertimeBackgroundThread().start();
	}

	public WfEditor getEditor() {
		return editor;
	}

	/**
	 * �ڳ��������׶�ִ�к�̨�߳�
	 * 
	 * @param run
	 */
	public synchronized void enqueueStartupQueue(WfRunnable run)
			throws WfStartupEndException {
		if (startupAlive) {
			startupQueue.add(run);
		} else {
			throw new WfStartupEndException();
		}
	}

	/**
	 * �ڳ������н׶�ִ�к�̨�߳�
	 * 
	 * @param worker
	 */
	public synchronized void enqueueOpertimeQueue(WfSwingWorker<?, ?> worker) {
		operTimeQueue.add(worker);
	}

	/**
	 * ���������׶������ĺ�̨����ִ���̣߳������60��
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class StartupThread extends Thread {

		public StartupThread() {
			setName("�����׶κ�̨�߳�");
			Timer liveT = new Timer(30 * 1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startupAlive = false;
					startupQueue.add(new WfRunnable("") {
						public void run() {
							System.out.println("�����׶κ�̨�߳��˳�");
						}
					});
				}
			});
			liveT.start();
		}

		public void run() {
			while (startupAlive) {
				try {
					WfRunnable run = startupQueue.take();
					if (run != null) {
						if (run.getTipInfo() != null
								&& !run.getTipInfo().trim().equals("")) {
							editor.getStatusBar().startShowBusyInfo(
									run.getTipInfo());
						}
						run.run();
						editor.getStatusBar().stopShowInfo(run.getTipInfo());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �������й����еĺ�ִ̨���߳�
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class OpertimeBackgroundThread extends Thread {

		public OpertimeBackgroundThread() {
			setName("�����׶κ�̨�߳�");
		}

		public void run() {
			while (operTimeAlive) {
				try {
					final WfSwingWorker<?, ?> worker = operTimeQueue.take();
					editor.getStatusBar()
							.startShowBusyInfo(worker.getTipInfo());
					if (worker != null) {
						worker.setDoneListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								editor.getStatusBar().stopShowInfo(
										worker.getTipInfo());
							}
						});
						worker.execute();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
