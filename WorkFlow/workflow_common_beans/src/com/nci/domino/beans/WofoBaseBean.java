package com.nci.domino.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.domain.WofoBaseDomain;

/**
 * �������������ͻ���Bean���࣬������Bean������״̬��ȫ��ΨһID
 * 
 * @author denvelope 2010-3-8
 * 
 */
public abstract class WofoBaseBean extends WofoBaseDomain implements
		Serializable {

	public static final int DELETED = -1; // ��ǰBean�Ƿ�ɾ���ı�� DEL
	public static final int CREATED = 1; // ��ǰBean�Ƿ��´����ı�� ADD
	public static final int CHANGED = 2; // ��ǰBean�Ƿ��޸ĵı�� MOD
	public static final int NATURAL = 0; // ��ǰBeanδ���Ķ�

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
	 * �жϵ�ǰBean�Ƿ�Ϊ��ɾ��״̬
	 * 
	 * @return
	 */
	public boolean isDeleted() {
		return state == DELETED;
	}

	/**
	 * ���õ�ǰBeanΪ��ɾ��״̬
	 * 
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		if (deleted) {
			this.state = DELETED;
		}
	}

	/**
	 * �жϵ�ǰBean�Ƿ�Ϊ���޸�״̬
	 * 
	 * @return
	 */
	public boolean isChanged() {
		return state == CHANGED;
	}

	/**
	 * ���õ�ǰBeanΪ���޸�״̬
	 * 
	 * @param changed
	 */
	public void setChanged(boolean changed) {
		if (changed) {
			this.state = CHANGED;
		}
	}

	/**
	 * �жϵ�ǰBean�Ƿ�Ϊ���´���״̬
	 * 
	 * @return
	 */
	public boolean isCreated() {
		return state == CREATED;
	}

	/**
	 * ���õ�ǰBeanΪ���´���״̬
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
	 * ����״̬�仯�¼�����
	 * 
	 * @param l
	 *            �¼���������
	 */
	public void addStateChangedListener(WofoBeanStateChangedListener l) {
		this.stateChangedListeners.add(l);
	}

	/**
	 * ����״̬�仯�¼�
	 * 
	 * @param oldState
	 *            ��״̬
	 * @param newState
	 *            ��״̬
	 */
	public void fireStateChangedListeners(int oldState, int newState) {
		for (int i = 0; i < stateChangedListeners.size(); i++) {
			((WofoBeanStateChangedListener) stateChangedListeners.get(i))
					.wofoBeanStateChanged(oldState, newState);
		}
	}

	/**
	 * Ĭ�ϵ�״̬�仯�������Ķ���
	 * 
	 * @param preiousState
	 * @param newState
	 */
	public void stateChanged(int preiousState, int newState) {
	}

	/**
	 * ����ǰ���¡ǰ�Ķ������ȶԣ����Լ������Щ�������ģ���Щ��ɾ���ģ��Լ�һЩ�û����Զ���Ĺ���
	 * 
	 * @param currentBean
	 * @param originalBean
	 */
	public void saveByCompareToOriginalBean(WofoBaseBean currentBean,
			WofoBaseBean originalBean) {
	}
}
