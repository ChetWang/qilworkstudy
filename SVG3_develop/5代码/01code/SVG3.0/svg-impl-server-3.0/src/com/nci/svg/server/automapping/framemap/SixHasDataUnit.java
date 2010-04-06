package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

/**
 * <p>
 * ���⣺SixHasDataUnit.java
 * </p>
 * <p>
 * ������ �̲����нṹͼ����Ԫ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-16
 * @version 1.0
 */
public class SixHasDataUnit {
	/**
	 * ���
	 */
	private String id;
	/**
	 * ���ڵ���
	 */
	private String parentId;
	/**
	 * ����
	 */
	private String name;
	/**
	 * �ӽڵ㼯
	 */
	private ArrayList children;

	public SixHasDataUnit() {
		init("", "", "");
	}

	/**
	 * ���캯��
	 * 
	 * @param id:String:�ڵ���
	 * @param parentId:String:���ڵ���
	 * @param name:String:�ڵ�����
	 */
	public SixHasDataUnit(String id, String parentId, String name) {
		init(id, parentId, name);
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ʼ������
	 * @param id:String:�ڵ���
	 * @param parentId:String:���ڵ���
	 * @param name:String:�ڵ�����
	 */
	private void init(String id, String parentId, String name) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.children = new ArrayList();
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ���
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ���ýڵ���
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡ���ڵ���
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ���ø��ڵ���
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ�����
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ���ýڵ�����
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� ��ȡ�ӽڵ㼯
	 * @return
	 */
	public ArrayList getChildren() {
		return children;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� �����ӽڵ㼯
	 * @param children
	 */
	public void setChildren(ArrayList children) {
		this.children = children;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @���� �����ӽڵ�
	 * @param child
	 */
	public void addChildren(String child) {
		this.children.add(child);
	}
}
