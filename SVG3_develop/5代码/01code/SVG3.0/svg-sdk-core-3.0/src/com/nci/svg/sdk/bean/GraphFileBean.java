package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * 标题：GraphFileBean.java
 * </p>
 * <p>
 * 描述： 图形文件
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-12
 * @version 1.0
 */
public class GraphFileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2385277607467446244L;
	/**
	 * 文件编号
	 */
	private String ID;
	/**
	 * 图文件名
	 */
	private String fileName;
	/**
	 * 图文件类型代码值（个性图/标准图） 
	 */
	private String fileType;
	/**
	 * 文件类型代码名称
	 */
	private String fileTypeName;
	/**
	 * 图业务类型代码值
	 */
	private String busiType;
	/**
	 * 业务图类型代码名
	 */
	private String busiTypeName;
	/**
	 * 图格式代码值
	 */
	private String fileFormat;
	/**
	 * 图格式代码名称
	 */
	private String fileFormatName;
	/**
	 * 最后操作人
	 */
	private String operator;
	/**
	 * 最后修改时间
	 */
	private String modifyTime;
	/**
	 * 文件内容
	 */
	private String content;
	/**
	 * 图形其他参数，共10个
	 */
	private String[] params;
	
	/**
	 * 构造函数
	 */
	public GraphFileBean(){
		params = new String[10];
	}

	/**
	 * 获取文件名
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置文件名
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取文件类型代码值
	 * @return
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * 设置文件类型代码值
	 * @param fileType
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取文件的业务类型代码值
	 * @return
	 */
	public String getBusiType() {
		return busiType;
	}
	/**
	 * 设置文件的业务类型代码值
	 * @param busiType
	 */
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	/**
	 * 获取文件格式代码值
	 * @return
	 */
	public String getFileFormat() {
		return fileFormat;
	}
	/**
	 * 设置文件格式代码值
	 * @param fileFormat
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * 获取最后修改人
	 * @return
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * 设置最后修改人
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取最后修改时间
	 * @return
	 */
	public String getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置最后修改时间
	 * @param modifyTime
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 获取文件内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置文件内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取文件其他参数
	 * @param index:第几个其他参数
	 * @return
	 */
	public String getParams(int index) {
		return params[index];
	}
	/**
	 * 设置文件其他参数
	 * @param index:第几个其他参数
	 * @param param:参数值
	 */
	public void setParams(int index, String param) {
		this.params[index] = param;
	}
	/**
	 * 获取文件其他参数
	 * @return
	 */
	public String[] getParams() {
		return params;
	}
	/**
	 * 设置文件其他参数
	 * @param params
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * 获取业务图类型代码名
	 * @return
	 */
	public String getBusiTypeName() {
		return busiTypeName;
	}
	/**
	 * 设置业务图类型代码名
	 * @param busiTypeName
	 */
	public void setBusiTypeName(String busiTypeName) {
		this.busiTypeName = busiTypeName;
	}

	/**
	 * 获取文件编号
	 * @return
	 */
	public String getID() {
		return ID;
	}

	/**
	 * 设置文件编号
	 * @param id
	 */
	public void setID(String id) {
		ID = id;
	}

	/**
	 * 获取文件类型代码名称
	 * @return
	 */
	public String getFileTypeName() {
		return fileTypeName;
	}

	/**
	 * 设置文件类型代码名称
	 * @param fileTypeName
	 */
	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	/**
	 * 获取图格式代码名称
	 * @return
	 */
	public String getFileFormatName() {
		return fileFormatName;
	}

	/**
	 * 设置图格式代码名称
	 * @param fileFormatName
	 */
	public void setFileFormatName(String fileFormatName) {
		this.fileFormatName = fileFormatName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// TODO Auto-generated method stub
		return fileName;
	}
}
