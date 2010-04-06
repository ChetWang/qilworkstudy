package com.nci.svg.logntermtask;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingUtilities;

import org.jdesktop.swingworker.SwingWorker;

import fr.itris.glips.svgeditor.Editor;

/**
 * 耗时操作管理器
 * 
 * @author Qil.Wong
 * 
 */
public class LongtermTaskManager extends Thread {

	private ConcurrentHashMap<Integer, LongtermTask> longtermTasks = new ConcurrentHashMap<Integer,LongtermTask>();

	private boolean isFinished = true;

	boolean showingFlag = false;

	private static LongtermTaskManager manager = null;

	private Editor editor;
	
	private int taskSerial = 0;

	private LongtermTaskManager(Editor editor) {
		this.editor = editor;
		setName("SVG LongtermTask Manager");
		start();
	}

	public static synchronized LongtermTaskManager getInstance(Editor editor) {
		if (manager == null) {
			manager = new LongtermTaskManager(editor);
		}
		return manager;
	}

	public void run() {
		while (true) {
			if (showingFlag) {
				notifyShowing();
			} else {
				try {
					sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 耗时操作，需要在status bar中显示相关信息
	 */
	public synchronized void addAndStartLongtermTask(LongtermTask task) {

		longtermTasks.put(new Integer(taskSerial), task);
		taskSerial++;
		task.getSwingWorker().execute();
		showingFlag = true;
//		if (isFinished) {
//			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//				@Override
//				public Void doInBackground() {
//					isFinished = false;
//					// notifyShowing();
//					showingFlag = true;
//					return null;
//				}
//
//				@Override
//				public void done() {
//					isFinished = true;
//				}
//			};
//			worker.execute();
//		}

	}

	/**
	 * 显示耗时操作各类信息，包括干什么，进度。
	 */
	private void notifyShowing() {

		while (showingFlag) {
			Iterator<Integer> it = longtermTasks.keySet().iterator();
			// for (LongtermOperationTask task : longtermTasks) {
			while (it.hasNext()) {
				Integer key  = it.next();
				final LongtermTask task = longtermTasks.get(key);
				if (!task.getSwingWorker().isDone()) {
					if (!editor.getStatusBar().getProgressBar()
							.isIndeterminate()) {
						SwingUtilities.invokeLater(new Runnable(){
							public void run(){
								editor.getStatusBar().getProgressBar().setVisible(true);
								editor.getStatusBar().getOperationInfoLabel()
										.setVisible(true);
								editor.getStatusBar().getProgressBar()
										.setIndeterminate(true);// 如果没有任务，第一个任务开始时就要滚动进度条
							}
						});
						
					}
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							editor.getStatusBar().getOperationInfoLabel().setText(
									task.getOperationInfo());
						}
					});
				} else {
					longtermTasks.remove(key);
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							editor.getStatusBar().getOperationInfoLabel().setText("");
						}
					});
					
				}
				try {
					if (longtermTasks.size() > 1)// 多个任务同时进行的时候睡眠多点，便于用户体验
						sleep(300);
					else
						sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (longtermTasks.isEmpty()) {
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							editor.getStatusBar().getProgressBar().setIndeterminate(
									false);// 只要还有一个任务在执行，进度条就不能停,只有全部任务结束后才停
							editor.getStatusBar().getProgressBar().setVisible(false);
							editor.getStatusBar().getOperationInfoLabel().setVisible(
									false);
//							editor.getStatusBar().getOperationInfoLabel().setText("");
						}
					});
					
					showingFlag = false;
				}
			}
		}
	}

}
