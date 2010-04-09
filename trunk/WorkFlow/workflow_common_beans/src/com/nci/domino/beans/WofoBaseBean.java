package com.nci.domino.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.domain.WofoBaseDomain;

/**
 * 工作流定义器客户端Bean基类，定义了Bean的四种状态及全局唯一ID
 * 
 * @author denvelope 2010-3-8
 * 
 */
public abstract class WofoBaseBean extends WofoBaseDomain implements
		Serializable {

	public static final int DELETED = -1; // 当前Bean是否被删除的标记 DEL
	public static final int CREATED = 1; // 当前Bean是否新创建的标记 ADD
	public static final int CHANGED = 2; // 当前Bean是否被修改的标记 MOD
	public static final int NATURAL = 0; // 当前Bean未作改动

	protected int state = NATURAL;

	List stateChangedListeners = new ArrayList();

	public WofoBaseBean() {
		stateChangedListeners.add(new WofoBeanStateChangedListener() {
			public void wofoBeanStateChanged(int preiousState, int newState) {
				stateChanged(preiousState, newState);
			}
		});
	}

	public abstract String getID();

	public abstract void setID(String id);

	/**
	 * 判断当前Bean是否为被删除状态
	 * 
	 * @return
	 */
	public boolean isDeleted() {
		return state == DELETED;
	}

	/**
	 * 设置当前Bean为被删除状态
	 * 
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		if (deleted) {
			this.state = DELETED;
		}
	}

	/**
	 * 判断当前Bean是否为被修改状态
	 * 
	 * @return
	 */
	public boolean isChanged() {
		return state == CHANGED;
	}

	/**
	 * 设置当前Bean为被修改状态
	 * 
	 * @param changed
	 */
	public void setChanged(boolean changed) {
		if (changed) {
			this.state = CHANGED;
		}
	}

	/**
	 * 判断当前Bean是否为被新创建状态
	 * 
	 * @return
	 */
	public boolean isCreated() {
		return state == CREATED;
	}

	/**
	 * 设置当前Bean为被新创建状态
	 * 
	 * @param created
	 */
	public void setCreated(boolean created) {
		if (created) {
			this.state = CREATED;
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		int oldState = this.state;
		this.state = state;
		if (oldState != state) {
			fireStateChangedListeners(oldState, state);
		}
	}

	/**
	 * 增加状态变化事件监听
	 * 
	 * @param l
	 *            事件监听对象
	 */
	public void addStateChangedListener(WofoBeanStateChangedListener l) {
		this.stateChangedListeners.add(l);
	}

	/**
	 * 触发状态变化事件
	 * 
	 * @param oldState
	 *            旧状态
	 * @param newState
	 *            新状态
	 */
	public void fireStateChangedListeners(int oldState, int newState) {
		for (int i = 0; i < stateChangedListeners.size(); i++) {
			((WofoBeanStateChangedListener) stateChangedListeners.get(i))
					.wofoBeanStateChanged(oldState, newState);
		}
	}

	/**
	 * 默认的状态变化所触发的动作
	 * 
	 * @param preiousState
	 * @param newState
	 */
	public void stateChanged(int preiousState, int newState) {
	}

	/**
	 * 保存前与克隆前的对象做比对，可以计算出哪些是新增的，哪些是删除的；以及一些用户可自定义的功能
	 * 
	 * @param currentBean
	 * @param originalBean
	 */
	public void saveByCompareToOriginalBean(WofoBaseBean currentBean,
			WofoBaseBean originalBean) {
	}
}
