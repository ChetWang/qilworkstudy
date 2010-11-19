package com.creaway.inmemdb.api.datapush;

import java.nio.channels.SelectionKey;
import java.util.List;

import com.creaway.inmemdb.datapush.InMemDBCommandPushTrigger;

/**
 * 内存数据库数据推送服务处理接口，所有的本地监听、远程监听都通过这个对象实例进行管理
 * 
 * @author Qil.Wong
 * 
 */
public interface PushServiceIfc {

	/**
	 * 添加本地数据推送监听
	 * 
	 * @param command
	 *            数据推送时对应的注册命令
	 * @param triggerType
	 *            在何种情况下进行推送的类型
	 * @param pal
	 *            推送时的动作对象
	 * @return boolean，true为注册成功；false为没有当前对应的命令和类型，注册失败
	 */
	public boolean addPushCommandListener(int command, int triggerType,
			PushActionListener pal);

	/**
	 * 移除本地注册的推送事件监听
	 * 
	 * @param command
	 * @param triggerType
	 */
	public void removePushCommandListener(int command, int triggerType,
			PushActionListener pal);


	/**
	 * 添加远程数据推送监听
	 * 
	 * @param command
	 *            数据推送时对应的注册命令
	 * @param triggerType
	 *            在何种情况下进行推送的类型
	 * @param key
	 *            对应的远程Socket通道
	 * @param l
	 *            推送时的动作对象
	 * @return boolean，true为注册成功；false为没有当前对应的命令和类型，注册失败
	 */
	public boolean addRemoteSocketChannelListener(int command, int triggerType,
			SelectionKey key, RemotePushActionListener l);
	
	/**
	 * 移除远程数据推送监听
	 * 
	 * @param command
	 *            数据推送时对应的注册命令
	 * @param triggerType
	 *            在何种情况下进行推送的类型
	 * @param key
	 *            对应的远程Socket通道
	 * @param l
	 *            推送时的动作对象
	 * @return boolean，true为注册成功；false为没有当前对应的命令和类型，注册失败
	 */
	public void removeRemoteSocketChannelListener(int command, int triggerType,
			SelectionKey key, RemotePushActionListener l);

	/**
	 * 添加命令对应的触发器
	 * 
	 * @param command
	 * @param triggerType
	 * @param trigger
	 */
	public void addCommandTrigger(int command, int triggerType,
			InMemDBCommandPushTrigger trigger);

	/**
	 * 移除与命令相对的触发器
	 * 
	 * @param command
	 * @param triggerType
	 */
	public void removeCommandTrigger(int command, int triggerType);

	/**
	 * 关闭推送服务
	 */
	public void close();
	
	/**
	 * 获取缓存的监听，用户在模块重启、配置改变是热部署
	 * @return
	 */
	public List<TmpRegisteredListenerObj> getTempRegisteredListeners();

}
