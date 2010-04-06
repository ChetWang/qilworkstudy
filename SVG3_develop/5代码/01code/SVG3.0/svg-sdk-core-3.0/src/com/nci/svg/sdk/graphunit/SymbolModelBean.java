package com.nci.svg.sdk.graphunit;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 图元或模板的模型对象，主要两个参数为id和name，其他参数都放在params中
 * @author Qil.Wong
 *
 */
public class SymbolModelBean implements Serializable {


	private static final long serialVersionUID = -7957701238142514500L;

	/**
	 * 模型ID
	 */
	private String id = null;

	/**
	 * 模型名称
	 */
	private String name = null;
	
	/**
	 * 父模型ID
	 */
	private String parentId = null;
	
	/**
	 * 模型状态集合
	 */
	private List statusList = null;

	/**
	 * 模型其他参数，如状态、位置等。key是参数类型，value是参数值
	 */
	private Map params = null;
	
	/**
	 * 模型动作,key为动作名称，value为动作内容bean
	 */
	private Map actions = null;

	public SymbolModelBean() {

	}

	/**
	 * 图元或模板的模型对象
	 * @param id 模型ID
	 * @param name 模型名称
	 * @param status 模型状态集合
	 * @param params 模型其他参数，如状态、位置等。key是参数类型，value是参数值。
	 */
	public SymbolModelBean(String id, String name, String parentId,List status, Map params) {
		this.id = id;
		this.name = name;
		this.parentId=parentId;
		this.statusList = status;
		this.params = params;
	}

	/**
	 * 获取模型ID
	 * @return 模型ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置模型ID
	 * @param id 模型ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取模型名称 
	 * @return 模型名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置模型名称
	 * @param name 模型名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取其他参数的集合HashMap
	 * @return 其他参数
	 */
	public Map getParams() {
		return params;
	}

	/**
	 * 设置其他参数
	 * @param params 其他参数
	 */
	public void setParams(Map params) {
		this.params = params;
	}

	/**
	 * 获取模型状态集合
	 * @return 模型状态集合
	 */
	public List getStatusList() {
		return statusList;
	}

	/**
	 * 设置模型状态集合
	 * @param status 模型状态集合
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
