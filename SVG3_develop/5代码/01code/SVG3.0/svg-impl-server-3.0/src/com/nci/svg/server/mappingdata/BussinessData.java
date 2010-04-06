package com.nci.svg.server.mappingdata;

import java.util.Map;

/**
 * <p>
 * 标题：BussinessData.java
 * </p>
 * <p>
 * 描述： 根据模型编号到业务系统的业务数据
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHANGSF
 * @时间: 2009-04-09
 * @version 1.0
 */
public class BussinessData {
	
	private String system;  //业务系统编号
	
	private String modelId; //模型管理中的模型ID
	
	private String modelName;// 业务系统对应模型名称
	
	private String field;//业务系统对应字段名
	
	private String value;//值
	
	private String codeModelName;
	
	private Map codeModel;//代码模型

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
