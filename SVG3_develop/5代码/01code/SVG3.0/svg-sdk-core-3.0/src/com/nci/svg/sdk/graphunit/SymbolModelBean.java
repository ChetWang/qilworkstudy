package com.nci.svg.sdk.graphunit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ͼԪ��ģ���ģ�Ͷ�����Ҫ��������Ϊid��name����������������params��
 * @author Qil.Wong
 *
 */
public class SymbolModelBean implements Serializable {


	private static final long serialVersionUID = -7957701238142514500L;

	/**
	 * ģ��ID
	 */
	private String id = null;

	/**
	 * ģ������
	 */
	private String name = null;
	
	/**
	 * ��ģ��ID
	 */
	private String parentId = null;
	
	/**
	 * ģ��״̬����
	 */
	private List statusList = null;

	/**
	 * ģ��������������״̬��λ�õȡ�key�ǲ������ͣ�value�ǲ���ֵ
	 */
	private Map params = null;
	
	/**
	 * ģ�Ͷ���,keyΪ�������ƣ�valueΪ��������bean
	 */
	private Map actions = null;

	public SymbolModelBean() {

	}

	/**
	 * ͼԪ��ģ���ģ�Ͷ���
	 * @param id ģ��ID
	 * @param name ģ������
	 * @param status ģ��״̬����
	 * @param params ģ��������������״̬��λ�õȡ�key�ǲ������ͣ�value�ǲ���ֵ��
	 */
	public SymbolModelBean(String id, String name, String parentId,List status, Map params) {
		this.id = id;
		this.name = name;
		this.parentId=parentId;
		this.statusList = status;
		this.params = params;
	}

	/**
	 * ��ȡģ��ID
	 * @return ģ��ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * ����ģ��ID
	 * @param id ģ��ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡģ������ 
	 * @return ģ������
	 */
	public String getName() {
		return name;
	}

	/**
	 * ����ģ������
	 * @param name ģ������
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ���������ļ���HashMap
	 * @return ��������
	 */
	public Map getParams() {
		return params;
	}

	/**
	 * ������������
	 * @param params ��������
	 */
	public void setParams(Map params) {
		this.params = params;
	}

	/**
	 * ��ȡģ��״̬����
	 * @return ģ��״̬����
	 */
	public List getStatusList() {
		return statusList;
	}

	/**
	 * ����ģ��״̬����
	 * @param status ģ��״̬����
	 */
	public void setStatusList(List statusList) {
		this.statusList = statusList;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Map getActions() {
		return actions;
	}

	public void setActions(Map actions) {
		this.actions = actions;
	}
	
	
	public String toString(){
		return name;
	}

}
