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
     * �豸ͼԪ����
     */
    private String graphUnitType;
    /**
     * ͼԪID
     */
    private String graphUnitID;
    /**
     * ͼԪ����
     */
    private String graphUnitName;
    /**
     * ͼԪ״̬
     */
    private String graphUnitStatus;
    /**
     * ͼԪ������
     */
    private String graphUnitGroup;
    /**
     * svg����
     */
    private String content;
 
    /**
     * ���캯��
     */
    public NCIEquipSymbolBean() {
    }
    
    /**
     * ���캯��
     * @param graphUnitID	�豸ͼԪ���
     * @param graphUnitType	�豸ͼԪ����
     * @param graphUnitName	�豸ͼԪ����
     * @param graphUnitGroup	�豸ͼԪ���
     * @param graphUnitStatus	�豸ͼԪ״̬
     * @param content	�豸ͼԪ����
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
     * ���캯��
     * @param graphUnitType	�豸ͼԪ����
     * @param node	��Ӧ�Ľڵ�
     */
    public NCIEquipSymbolBean(String graphUnitType) {
        this.graphUnitType = graphUnitType;
    }

    /**
     * ��ȡͼԪ����
     * @return ͼԪ����
     */
    public String getGraphUnitType() {
        return graphUnitType;
    }

    /**
     * ����ͼԪ����
     * @param graphUnitType ͼԪ����
     */
    public void setGraphUnitType(String graphUnitType) {
        this.graphUnitType = graphUnitType;
    }

    public String toString() {
        return "["+graphUnitType+"]"+graphUnitName;
    }

    /**
     * ��ȡͼԪID
     * @return
     */
    public String getGraphUnitID() {
        return graphUnitID;
    }

    /**
     * ����ͼԪID
     * @param graphUnitID
     */
    public void setGraphUnitID(String graphUnitID) {
        this.graphUnitID = graphUnitID;
    }

    /**
     * ��ȡͼԪ����
     * @return
     */
    public String getGraphUnitName() {
        return graphUnitName;
    }

    /**
     * ����ͼԪ����
     * @param graphUnitName
     */
    public void setGraphUnitName(String graphUnitName) {
        this.graphUnitName = graphUnitName;
    }

    /**
     * ��ȡͼԪ״̬
     * @return
     */
    public String getGraphUnitStatus() {
        return graphUnitStatus;
    }

    /**
     * ����ͼԪ״̬
     * @param graphUnitStatus ͼԪ״̬���μ�STATUS_NONE��STATUS_OPEN��STATUS_CLOSED
     */
    public void setGraphUnitStatus(String graphUnitStatus) {
        this.graphUnitStatus = graphUnitStatus;
    }

    /**
     * ��ȡͼԪ������
     * @return
     */
    public String getGraphUnitGroup() {
        return graphUnitGroup;
    }
    /**
     * ����ͼԪ������
     * @param graphUnitGroup
     */
    public void setGraphUnitGroup(String graphUnitGroup) {
        this.graphUnitGroup = graphUnitGroup;
    }
    /**
     * ��ȡͼԪ��svg����
     * @return
     */
	public String getContent() {
		return content;
	}
	/**
	 * ����ͼԪsvg����
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	

}
