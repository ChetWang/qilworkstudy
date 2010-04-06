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
 * 通讯组件基类，用于定义通讯组件的基础功能；所有SVG平台的客户端通讯组件都要提供基类定义的功能。
 * 
 * @author Qil.Wong
 * @since 3.0
 * 
 */
public abstract class CommunicationAdapter extends DefaultModuleAdapter
		implements ActionListener {

	/**
	 * 定时器，用来定时判断通讯组件连接的有效性
	 */
	protected Timer timer = null;

	/**
	 * 心跳线程，定时器通过这个心跳线程来实现连接有效性的判断
	 */
	protected SwingWorker<Integer, Integer> heartBeat = null;

	/**
	 * 连接状态标记
	 * 
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 */
	protected int connectFlag = 0;

	/**
	 * 有效连接值
	 */
	public final static int COMMUNICATION_VALID = 0;

	/**
	 * 无效连接值
	 */
	public final static int COMMUNICATION_INVALID = -1;

	/**
	 * 主管理组件基类对象，通过该对象可直接访问主管理对象提供的对外接口
	 */
	protected ModuleControllerAdapter editor;

	protected boolean startFlag;

	

	/**
	 * 唯一的通讯组件构造函数
	 * 
	 * @param editor
	 *            编辑器主管理组件对象
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
				// 第一次一定要在初始化前进行测试
				connectFlag = checkStatus();
				timer = new Timer(10 * 1000, this); // 定时去检测网络连接的有效性
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
	 * 检查通讯组件的状态
	 * 
	 * @return 状态，可用或不可用
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 */
	public abstract int checkStatus();

	/**
	 * 开始和服务器通讯
	 * 
	 * @param obj
	 *            传递给服务器的参数数据结构
	 * @return ResultBean，服务器处理的结果
	 * @see ResultBean
	 */
	public abstract ResultBean communicate(CommunicationBean obj);

	/**
	 * 获取通讯组件的连接状态标记
	 * 
	 * @return int，连接状态
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 */
	public int getConnectFlag() {
		return connectFlag;
	}

	/**
	 * 设置通讯组件的连接状态
	 * 
	 * @param flag
	 *            新的连接状态
	 * @see CommunicationAdapter#COMMUNICATION_VALID
	 * @see CommunicationAdapter#COMMUNICATION_INVALID
	 */
	public void setConnectFlag(int flag) {
		this.connectFlag = flag;
	}

	private final CommunicationAdapter comm = this;

	private boolean isCheckingStatus = false;

	/**
	 * 定时器响应，心跳执行
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
				// LoggerAdapter.DEBUG, "检测连接状态...");
				return checkStatus();
			}

			public void done() {
				try {
					connectFlag = get();
					isCheckingStatus = false;
					// if(connectFlag == COMMUNICATION_INVALID){
					// ((EditorAdapter) editor).getLogger().log(comm,
					// LoggerAdapter.DEBUG, "无法建立连接");
					// }else if(connectFlag == COMMUNICATION_VALID){
					// ((EditorAdapter) editor).getLogger().log(comm,
					// LoggerAdapter.DEBUG, "连接有效");
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
	 * 将通讯模块设置为连接无效，并记录异常
	 * 
	 * @param comm
	 *            通讯模块对象
	 * @param e
	 */
	protected void connectionFailed(Object e) {
		((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR, e);
		connectFlag = COMMUNICATION_INVALID;
	}

	/**
	 * 显示连接失败的提示
	 */
	protected void showConnFailedInfo(CommunicationBean bean) {
		JOptionPane.showConfirmDialog(((EditorAdapter) editor)
				.findParentFrame(), "通信模块无法与服务器建立有效连接！" + bean.toString(),
				"连接无效", JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION);
	}

	public abstract boolean isKeepConnection();

}
