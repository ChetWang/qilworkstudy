package com.nci.svg.district.relate.bean;

import java.util.LinkedList;

/**
 * <p>
 * ���⣺DistrictTreeNodeBean.java
 * </p>
 * <p>
 * ������̨��ͼ���豸���ڵ���
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-4-17
 * @version 1.0
 */
public class DistrictTreeNodeBean {
	/**
	 * �ڵ��Ӧ���豸���
	 */
	private String id;
	/**
	 * ��Ӧ���豸����
	 */
	private String type;
	/**
	 * �ӽڵ���
	 */
	private LinkedList<DistrictTreeNodeBean> subNode;
	/**
	 * ����·������λ��
	 */
	private String subNodePosition;
	/**
	 * �ڵ���豸���� 0�豸��š�1�豸����
	 */
	private LinkedList<String[]> equips;

	/**
	 * ���캯��
	 */
	public DistrictTreeNodeBean() {
		this.id = "";
		this.type = "";
		this.subNode = new LinkedList<DistrictTreeNodeBean>();
		this.equips = new LinkedList<String[]>();
	}

	/**
	 * 2009-4-20
	 * Add by ZHM
	 * @���� ��ȡ����·����λ��
	 * @return
	 */
	public String getSubNodePosition() {
		return subNodePosition;
	}
	
	/**
	 * 2009-4-20
	 * Add by ZHM
	 * @���� ����֧������λ��
	 * @param subNodePosition
	 */
	public void setSubNodePosition(String subNodePosition){
		this.subNodePosition = subNodePosition;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ��豸���
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ���ýڵ��豸���
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ��豸����
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ���ýڵ��豸����
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ��ȡ�ӽڵ㼯
	 * @return
	 */
	public LinkedList<DistrictTreeNodeBean> getSubNode() {
		return subNode;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� �����ӽڵ㵽�ڵ㼯��
	 * @param subNode
	 */
	public void setSubNode(DistrictTreeNodeBean subNode) {
		if (subNode != null) {
			this.subNode.add(subNode);
		}
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� �����ӽڵ㼯
	 * @param subNode
	 */
	public void setSubNode(LinkedList<DistrictTreeNodeBean> subNode) {
		this.subNode = subNode;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ���豸��
	 * @return
	 */
	public LinkedList<String[]> getEquips() {
		return equips;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� �����豸��Ϣ���ڵ��豸����
	 * @param equip
	 */
	public void setEquips(String[] equip) {
		if (equip.length == 2) {
			this.equips.add(equip);
		}
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ���ýڵ���豸��
	 * @param equips
	 */
	public void setEquips(LinkedList<String[]> equips) {
		this.equips = equips;
	}
}
