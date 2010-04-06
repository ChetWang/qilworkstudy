package com.nci.svg.logntermtask;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingUtilities;

import org.jdesktop.swingworker.SwingWorker;

import fr.itris.glips.svgeditor.Editor;

/**
 * ��ʱ����������
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
	 * ��ʱ��������Ҫ��status bar����ʾ�����Ϣ
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
	 * ��ʾ��ʱ����������Ϣ��������ʲô�����ȡ�
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
										.setIndeterminate(true);// ���û�����񣬵�һ������ʼʱ��Ҫ����������
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
					if (longtermTasks.size() > 1)// �������ͬʱ���е�ʱ��˯�߶�㣬�����û�����
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
									false);// ֻҪ����һ��������ִ�У��������Ͳ���ͣ,ֻ��ȫ������������ͣ
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
