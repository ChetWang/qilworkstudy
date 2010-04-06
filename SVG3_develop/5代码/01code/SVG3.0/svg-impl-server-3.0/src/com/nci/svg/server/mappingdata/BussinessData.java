package com.nci.svg.server.mappingdata;

import java.util.Map;

/**
 * <p>
 * ���⣺BussinessData.java
 * </p>
 * <p>
 * ������ ����ģ�ͱ�ŵ�ҵ��ϵͳ��ҵ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHANGSF
 * @ʱ��: 2009-04-09
 * @version 1.0
 */
public class BussinessData {
	
	private String system;  //ҵ��ϵͳ���
	
	private String modelId; //ģ�͹����е�ģ��ID
	
	private String modelName;// ҵ��ϵͳ��Ӧģ������
	
	private String field;//ҵ��ϵͳ��Ӧ�ֶ���
	
	private String value;//ֵ
	
	private String codeModelName;
	
	private Map codeModel;//����ģ��

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map getCodeModel() {
		return codeModel;
	}

	public void setCodeModel(Map codeModel) {
		this.codeModel = codeModel;
	}

	public String getCodeModelName() {
		return codeModelName;
	}

	public void setCodeModelName(String codeModelName) {
		this.codeModelName = codeModelName;
	}

}
