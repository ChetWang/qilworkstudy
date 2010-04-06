/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.sdk.graphunit;

import java.io.Serializable;

import com.nci.svg.sdk.bean.SimpleCodeBean;

/**
 * 图元或模板的属性Bean
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
	 * 大类，是图元还是模板
	 */
	private String type;
	/**
	 * 图元编号
	 */
	private String id;
	/**
	 * 图元名称
	 */
	private String name;

	/**
	 * 模型ID
	 */
	private String modelID;

	/**
	 * 小类代码对象
	 */
	private SimpleCodeBean variety = null;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 修改时间
	 */
	private String modifyTime;

	/**
	 * 修改人
	 */
	private String operator;

	/**
	 * svg内容
	 */
	private String content;
	/**
	 * 有效标记
	 */
	private boolean isValid;

	/**
	 * 发布标记
	 */
	private boolean isReleased;

	/**
	 * 模板参数1，模板专用
	 */
	private String param1;

	/**
	 * 模板参数2，模板专用
	 */
	private String param2;

	/**
	 * 模板参数3，模板专用
	 */
	private String param3;
	
	/**
	 * 构造函数
	 */
	public NCIEquipSymbolBean() {
	}

	/**
	 * 构造函数
	 * 
	 * @param id:图元编号
	 * @param variety:图元小类代码值
	 * @param name:图元名称
	 * @param content:图元内容
	 * @param createTime:图元创建时间
	 * @param modifyTime:图元最后维护时间
	 * @param operator:最后操作人
	 * @param isValid:是否有效
	 * @param isReleased:是否发布
	 * @param type:图元大类
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
	 * 获取小类代码对象
	 * @return
	 */
	public SimpleCodeBean getVariety() {
		return variety;
	}

	/**
	 * 设置小类代码对象
	 * @param variety
	 */
	public void setVariety(SimpleCodeBean variety) {
		this.variety = variety;
	}

	public String toString() {
		return "[" + variety.getName() + "]" + name;
	}

	/**
	 * 获取图元编号
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置图元编号
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取图元名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置图元名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取图元\模板的svg内容
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置图元\模板svg内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取图元创建时间
	 * 
	 * @return
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * 设置图元创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取图元最后修改时间
	 * 
	 * @return
	 */
	public String getModifyTime() {
		return modifyTime;
	}

	/**
	 * 设置图元最后修改时间
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 获取图元最后修改人
	 * 
	 * @return
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置图元最后修改人
	 * 
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取图元有效标记
	 * 
	 * @return
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * 设置图元有效标记
	 * 
	 * @param isValid:有效标记
	 */
	public void setValidity(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * 获取图元是否发布
	 * 
	 * @return
	 */
	public boolean isReleased() {
		return isReleased;
	}

	/**
	 * 设置图元是否发布
	 * 
	 * @param isRelease
	 */
	public void setReleased(boolean isRelease) {
		this.isReleased = isRelease;
	}

	/**
	 * 获取大类，是图元还是模板
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置大类，是图元还是模板
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
	 * 获取模型ID
	 * 
	 * @return
	 */
	public String getModelID() {
		return modelID;
	}

	/**
	 * 设置模型ID
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
