package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

/**
 * <p>
 * 标题：SixHasDataUnit.java
 * </p>
 * <p>
 * 描述： 烟草六有结构图数据元类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-16
 * @version 1.0
 */
public class SixHasDataUnit {
	/**
	 * 编号
	 */
	private String id;
	/**
	 * 父节点编号
	 */
	private String parentId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 子节点集
	 */
	private ArrayList children;

	public SixHasDataUnit() {
		init("", "", "");
	}

	/**
	 * 构造函数
	 * 
	 * @param id:String:节点编号
	 * @param parentId:String:父节点编号
	 * @param name:String:节点名称
	 */
	public SixHasDataUnit(String id, String parentId, String name) {
		init(id, parentId, name);
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 初始化函数
	 * @param id:String:节点编号
	 * @param parentId:String:父节点编号
	 * @param name:String:节点名称
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
	 * @功能 获取节点编号
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 设置节点编号
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 获取父节点编号
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 设置父节点编号
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 获取节点名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 设置节点名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 获取子节点集
	 * @return
	 */
	public ArrayList getChildren() {
		return children;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 设置子节点集
	 * @param children
	 */
	public void setChildren(ArrayList children) {
		this.children = children;
	}

	/**
	 * 2009-6-16 Add by ZHM
	 * 
	 * @功能 增加子节点
	 * @param child
	 */
	public void addChildren(String child) {
		this.children.add(child);
	}
}
