/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.graphunit;

import java.io.Serializable;

import org.w3c.dom.Node;

/**
 *
 * @author Qil.Wong
 */
public class NCIEquipSymbolBean implements Serializable{


	private static final long serialVersionUID = -3307779170805877762L;
	/**
     * 设备图元类型
     */
    private String graphUnitType;
    /**
     * 图元ID
     */
    private String graphUnitID;
    /**
     * 图元名称
     */
    private String graphUnitName;
    /**
     * 图元状态
     */
    private String graphUnitStatus;
    /**
     * 图元所在组
     */
    private String graphUnitGroup;
    /**
     * svg内容
     */
    private String content;
 
    /**
     * 构造函数
     */
    public NCIEquipSymbolBean() {
    }
    
    /**
     * 构造函数
     * @param graphUnitID	设备图元编号
     * @param graphUnitType	设备图元类型
     * @param graphUnitName	设备图元名称
     * @param graphUnitGroup	设备图元组号
     * @param graphUnitStatus	设备图元状态
     * @param content	设备图元内容
     */
    public NCIEquipSymbolBean(String graphUnitID, String graphUnitType,
			String graphUnitName, String graphUnitGroup,
			String graphUnitStatus, String content) {
    	this.graphUnitID = graphUnitID;
    	this.graphUnitGroup = graphUnitGroup;
    	this.graphUnitType = graphUnitType;
    	this.graphUnitName = graphUnitName;
    	this.graphUnitGroup = graphUnitGroup;
    	this.graphUnitStatus = graphUnitStatus;
    	this.content = content;
	}

    /**
     * 构造函数
     * @param graphUnitType	设备图元类型
     * @param node	对应的节点
     */
    public NCIEquipSymbolBean(String graphUnitType) {
        this.graphUnitType = graphUnitType;
    }

    /**
     * 获取图元类型
     * @return 图元类型
     */
    public String getGraphUnitType() {
        return graphUnitType;
    }

    /**
     * 设置图元类型
     * @param graphUnitType 图元类型
     */
    public void setGraphUnitType(String graphUnitType) {
        this.graphUnitType = graphUnitType;
    }

    public String toString() {
        return "["+graphUnitType+"]"+graphUnitName;
    }

    /**
     * 获取图元ID
     * @return
     */
    public String getGraphUnitID() {
        return graphUnitID;
    }

    /**
     * 设置图元ID
     * @param graphUnitID
     */
    public void setGraphUnitID(String graphUnitID) {
        this.graphUnitID = graphUnitID;
    }

    /**
     * 获取图元名称
     * @return
     */
    public String getGraphUnitName() {
        return graphUnitName;
    }

    /**
     * 设置图元名称
     * @param graphUnitName
     */
    public void setGraphUnitName(String graphUnitName) {
        this.graphUnitName = graphUnitName;
    }

    /**
     * 获取图元状态
     * @return
     */
    public String getGraphUnitStatus() {
        return graphUnitStatus;
    }

    /**
     * 设置图元状态
     * @param graphUnitStatus 图元状态，参见STATUS_NONE，STATUS_OPEN，STATUS_CLOSED
     */
    public void setGraphUnitStatus(String graphUnitStatus) {
        this.graphUnitStatus = graphUnitStatus;
    }

    /**
     * 获取图元所在组
     * @return
     */
    public String getGraphUnitGroup() {
        return graphUnitGroup;
    }
    /**
     * 设置图元所在组
     * @param graphUnitGroup
     */
    public void setGraphUnitGroup(String graphUnitGroup) {
        this.graphUnitGroup = graphUnitGroup;
    }
    /**
     * 获取图元的svg内容
     * @return
     */
	public String getContent() {
		return content;
	}
	/**
	 * 设置图元svg内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	

}
