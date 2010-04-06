package com.nci.svg.sdk.client.communication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.jdesktop.swingworker.SwingWorker;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.module.ModuleControllerAdapter;
import com.nci.svg.sdk.module.ModuleInitializeFailedException;
import com.nci.svg.sdk.module.ModuleStartFailedException;

/**
 * ͨѶ������࣬���ڶ���ͨѶ����Ļ������ܣ�����SVGƽ̨�Ŀͻ���ͨѶ�����Ҫ�ṩ���ඨ��Ĺ��ܡ�
 * 
 * @author Qil.Wong
 * @since 3.0
 * 
 */
public abstract class CommunicationAdapter extends DefaultModuleAdapter
		implements ActionListener {

	/**
	 * ��ʱ����������ʱ�ж�ͨѶ������ӵ���Ч��
	 */
	protected Timer timer = null;

	/**
	 * �����̣߳���ʱ��ͨ����������߳���ʵ��������Ч�Ե��ж�
	 */
	protected SwingWorker<Integer, Integer> heartBeat = null;

	/**
	 * ����״̬���
	 * 
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 */
	protected int connectFlag = 0;

	/**
	 * ��Ч����ֵ
	 */
	public final static int COMMUNICATION_VALID = 0;

	/**
	 * ��Ч����ֵ
	 */
	public final static int COMMUNICATION_INVALID = -1;

	/**
	 * ����������������ͨ���ö����ֱ�ӷ�������������ṩ�Ķ���ӿ�
	 */
	protected ModuleControllerAdapter editor;

	protected boolean startFlag;

	

	/**
	 * Ψһ��ͨѶ������캯��
	 * 
	 * @param editor
	 *            �༭���������������
	 */
	public CommunicationAdapter(ModuleControllerAdapter editor) {
		super();
		this.editor = editor;
		
		int result = init();
		if (result != MODULE_INITIALIZE_COMPLETE) {
			((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR,
					new ModuleInitializeFailedException(this));
		}
		result = start();
		if (result != MODULE_START_COMPLETE) {
			((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR,
					new ModuleStartFailedException(this));
		}
	}

	public int init() {
		try {
			if (isKeepConnection()) {
				// ��һ��һ��Ҫ�ڳ�ʼ��ǰ���в���
				connectFlag = checkStatus();
				timer = new Timer(10 * 1000, this); // ��ʱȥ����������ӵ���Ч��
				timer.start();
			}
		} catch (Exception e) {
			((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR,
					e);
			return MODULE_INITIALIZE_FAILED;
		}
		return MODULE_INITIALIZE_COMPLETE;
	}

	/**
	 * ���ͨѶ�����״̬
	 * 
	 * @return ״̬�����û򲻿���
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 */
	public abstract int checkStatus();

	/**
	 * ��ʼ�ͷ�����ͨѶ
	 * 
	 * @param obj
	 *            ���ݸ��������Ĳ������ݽṹ
	 * @return ResultBean������������Ľ��
	 * @see ResultBean
	 */
	public abstract ResultBean communicate(CommunicationBean obj);

	/**
	 * ��ȡͨѶ���������״̬���
	 * 
	 * @return int������״̬
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 */
	public int getConnectFlag() {
		return connectFlag;
	}

	/**
	 * ����ͨѶ���������״̬
	 * 
	 * @param flag
	 *            �µ�����״̬
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 */
	public void setConnectFlag(int flag) {
		this.connectFlag = flag;
	}

	private final CommunicationAdapter comm = this;

	private boolean isCheckingStatus = false;

	/**
	 * ��ʱ����Ӧ������ִ��
	 */
	public void actionPerformed(ActionEvent e) {
		if (isCheckingStatus) {
			return;
		}
		isCheckingStatus = true;

		heartBeat = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				// ((EditorAdapter) editor).getLogger().log(comm,
				// LoggerAdapter.DEBUG, "�������״̬...");
				return checkStatus();
			}

			public void done() {
				try {
					connectFlag = get();
					isCheckingStatus = false;
					// if(connectFlag == COMMUNICATION_INVALID){
					// ((EditorAdapter) editor).getLogger().log(comm,
					// LoggerAdapter.DEBUG, "�޷���������");
					// }else if(connectFlag == COMMUNICATION_VALID){
					// ((EditorAdapter) editor).getLogger().log(comm,
					// LoggerAdapter.DEBUG, "������Ч");
					// }
				} catch (InterruptedException e) {
					((EditorAdapter) editor).getLogger().log(comm,
							LoggerAdapter.ERROR, e);
				} catch (ExecutionException e) {
					((EditorAdapter) editor).getLogger().log(comm,
							LoggerAdapter.ERROR, e);
				}
			}

		};
		heartBeat.execute();
	}

	public String getModuleType() {
		return "Communication";
	}

	/**
	 * ��ͨѶģ������Ϊ������Ч������¼�쳣
	 * 
	 * @param comm
	 *            ͨѶģ�����
	 * @param e
	 */
	protected void connectionFailed(Object e) {
		((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR, e);
		connectFlag = COMMUNICATION_INVALID;
	}

	/**
	 * ��ʾ����ʧ�ܵ���ʾ
	 */
	protected void showConnFailedInfo(CommunicationBean bean) {
		JOptionPane.showConfirmDialog(((EditorAdapter) editor)
				.findParentFrame(), "ͨ��ģ���޷��������������Ч���ӣ�" + bean.toString(),
				"������Ч", JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION);
	}

	public abstract boolean isKeepConnection();

}
