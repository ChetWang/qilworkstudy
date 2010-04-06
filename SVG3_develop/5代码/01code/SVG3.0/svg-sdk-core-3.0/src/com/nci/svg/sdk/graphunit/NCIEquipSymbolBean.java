/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.sdk.graphunit;

import java.io.Serializable;

import com.nci.svg.sdk.bean.SimpleCodeBean;

/**
 * ͼԪ��ģ�������Bean
 * 
 * @author Qil.Wong
 */
public class NCIEquipSymbolBean implements Serializable {

	private static final long serialVersionUID = -3307779170805877762L;

	public static final String SYMBOL_TYPE_GRAPHUNIT = "graphunit";
	public static final String SYMBOL_TYPE_GRAPHUNIT_CODE = "1";
	public static final String SYMBOL_TYPE_TEMPLATE = "template";
	public static final String SYMBOL_TYPE_TEMPLATE_CODE = "2";
	

	/**
	 * ���࣬��ͼԪ����ģ��
	 */
	private String type;
	/**
	 * ͼԪ���
	 */
	private String id;
	/**
	 * ͼԪ����
	 */
	private String name;

	/**
	 * ģ��ID
	 */
	private String modelID;

	/**
	 * С��������
	 */
	private SimpleCodeBean variety = null;

	/**
	 * ����ʱ��
	 */
	private String createTime;

	/**
	 * �޸�ʱ��
	 */
	private String modifyTime;

	/**
	 * �޸���
	 */
	private String operator;

	/**
	 * svg����
	 */
	private String content;
	/**
	 * ��Ч���
	 */
	private boolean isValid;

	/**
	 * �������
	 */
	private boolean isReleased;

	/**
	 * ģ�����1��ģ��ר��
	 */
	private String param1;

	/**
	 * ģ�����2��ģ��ר��
	 */
	private String param2;

	/**
	 * ģ�����3��ģ��ר��
	 */
	private String param3;
	
	/**
	 * ���캯��
	 */
	public NCIEquipSymbolBean() {
	}

	/**
	 * ���캯��
	 * 
	 * @param id:ͼԪ���
	 * @param variety:ͼԪС�����ֵ
	 * @param name:ͼԪ����
	 * @param content:ͼԪ����
	 * @param createTime:ͼԪ����ʱ��
	 * @param modifyTime:ͼԪ���ά��ʱ��
	 * @param operator:��������
	 * @param isValid:�Ƿ���Ч
	 * @param isReleased:�Ƿ񷢲�
	 * @param type:ͼԪ����
	 */
	public NCIEquipSymbolBean(String id, SimpleCodeBean variety, String name,
			String modelID, String content, String createTime,
			String modifyTime, String operator, boolean isValid,
			boolean isReleased, String type) {
		this.id = id;
		this.variety = variety;
		this.name = name;
		this.modelID = modelID;
		this.content = content;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.operator = operator;
		this.isValid = isValid;
		this.isReleased = isReleased;
		this.type = type;
	}

	/**
	 * ��ȡС��������
	 * @return
	 */
	public SimpleCodeBean getVariety() {
		return variety;
	}

	/**
	 * ����С��������
	 * @param variety
	 */
	public void setVariety(SimpleCodeBean variety) {
		this.variety = variety;
	}

	public String toString() {
		return "[" + variety.getName() + "]" + name;
	}

	/**
	 * ��ȡͼԪ���
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * ����ͼԪ���
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡͼԪ����
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * ����ͼԪ����
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡͼԪ\ģ���svg����
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ����ͼԪ\ģ��svg����
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * ��ȡͼԪ����ʱ��
	 * 
	 * @return
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * ����ͼԪ����ʱ��
	 * 
	 * @param createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * ��ȡͼԪ����޸�ʱ��
	 * 
	 * @return
	 */
	public String getModifyTime() {
		return modifyTime;
	}

	/**
	 * ����ͼԪ����޸�ʱ��
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * ��ȡͼԪ����޸���
	 * 
	 * @return
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * ����ͼԪ����޸���
	 * 
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * ��ȡͼԪ��Ч���
	 * 
	 * @return
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * ����ͼԪ��Ч���
	 * 
	 * @param isValid:��Ч���
	 */
	public void setValidity(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * ��ȡͼԪ�Ƿ񷢲�
	 * 
	 * @return
	 */
	public boolean isReleased() {
		return isReleased;
	}

	/**
	 * ����ͼԪ�Ƿ񷢲�
	 * 
	 * @param isRelease
	 */
	public void setReleased(boolean isRelease) {
		this.isReleased = isRelease;
	}

	/**
	 * ��ȡ���࣬��ͼԪ����ģ��
	 */
	public String getType() {
		return type;
	}

	/**
	 * ���ô��࣬��ͼԪ����ģ��
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}
	

	/**
	 * ��ȡģ��ID
	 * 
	 * @return
	 */
	public String getModelID() {
		return modelID;
	}

	/**
	 * ����ģ��ID
	 * 
	 * @param modelID
	 */
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}

	public NCIEquipSymbolBean cloneSymbolBean() {
		NCIEquipSymbolBean bean = new NCIEquipSymbolBean(id, variety
				.cloneCodeBean(), name, modelID, content, createTime,
				modifyTime, operator, isValid, isReleased, type);
		bean.setParam1(param1);
		bean.setParam2(param2);
		bean.setParam3(param2);
		return bean;
	}


}
