package com.nci.domino.concurrent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Timer;

import com.nci.domino.WfEditor;

/**
 * 后台动作管理器
 * 
 * @author Qil.Wong
 * 
 */
public class WfBackgroundRunner {

	private WfEditor editor;

	private LinkedBlockingQueue<WfRunnable> startupQueue = new LinkedBlockingQueue<WfRunnable>();

	private LinkedBlockingQueue<WfSwingWorker<?, ?>> operTimeQueue = new LinkedBlockingQueue<WfSwingWorker<?, ?>>();

	// 启动阶段后台线程存活标记
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
	 * 在程序启动阶段执行后台线程
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
	 * 在程序运行阶段执行后台线程
	 * 
	 * @param worker
	 */
	public synchronized void enqueueOpertimeQueue(WfSwingWorker<?, ?> worker) {
		operTimeQueue.add(worker);
	}

	/**
	 * 程序启动阶段启动的后台任务执行线程，存活期60秒
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class StartupThread extends Thread {

		public StartupThread() {
			setName("启动阶段后台线程");
			Timer liveT = new Timer(30 * 1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startupAlive = false;
					startupQueue.add(new WfRunnable("") {
						public void run() {
							System.out.println("启动阶段后台线程退出");
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
	 * 程序运行过程中的后台执行线程
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class OpertimeBackgroundThread extends Thread {

		public OpertimeBackgroundThread() {
			setName("操作阶段后台线程");
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
