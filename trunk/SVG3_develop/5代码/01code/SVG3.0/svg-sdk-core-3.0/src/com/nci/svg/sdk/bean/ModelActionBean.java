
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-18
 * @功能：模型动作实体类
 *
 */
public class ModelActionBean implements Serializable {
	private static final long serialVersionUID = 7828556456435941646L;
	/**
	 * add by yux,2009-1-21
	 * 鼠标移动
	 */
	public static final String TYPE_MOUSE_MOVE = "mMove";
	/**
	 * add by yux,2009-1-18
	 * 左键单击
	 */
	public static final String TYPE_MOUSE_LDOWN = "mLDowm";
	/**
	 * add by yux,2009-1-18
	 * 右键单击
	 */
	public static final String TYPE_MOUSE_RDOWM = "mRDowm";
	/**
	 * add by yux,2009-1-18
	 * 左键双击
	 */
	public static final String TYPE_MOUSE_LDOUBLE = "mLDouble";

    /**
     * add by yux,2009-1-21
     * 初始化加载
     */
    public static final String TYPE_INITLOAD = "initLoad";
    
    /**
     * add by yux,2009-1-21
     * 创建实例
     */
    public static final String TYPE_NEWINSTANCE = "newInst";
    
    /**
     * add by yux,2009-1-21
     * 绑定实例
     */
    public static final String TYPE_RELAINSTANCE = "relaInst";
    
    /**
     * add by yux,2009-1-21
     * 退出事件
     */
    public static final String TYPE_CLOSE = "close";
    
    
    /**
     * add by yux,2009-1-18
     * 动作编号
     */
    private String id = null;
    /**
     * add by yux,2009-1-19
     * 模型编号
     */
    private String modelID = null;
    /**
     * add by yux,2009-1-18
     * 动作类型
     */
    private String type = null;
    /**
     * add by yux,2009-1-18
     * 动作短名
     */
    private String shortName = null;
    /**
     * add by yux,2009-1-18
     * 动作名称
     */
    private String name = null;
    /**
     * add by yux,2009-1-18
     * 动作内容
     */
    private String content = null;
	/**
	 * 返回
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 返回
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 返回
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * 设置
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * 返回
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 返回
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 返回
	 * @return the modelID
	 */
	public String getModelID() {
		return modelID;
	}
	/**
	 * 设置
	 * @param modelID the modelID to set
	 */
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
}
