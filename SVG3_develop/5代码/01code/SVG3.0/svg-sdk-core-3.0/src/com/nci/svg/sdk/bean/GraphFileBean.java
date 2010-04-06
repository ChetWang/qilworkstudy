package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * ���⣺GraphFileBean.java
 * </p>
 * <p>
 * ������ ͼ���ļ�
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-12
 * @version 1.0
 */
public class GraphFileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2385277607467446244L;
	/**
	 * �ļ����
	 */
	private String ID;
	/**
	 * ͼ�ļ���
	 */
	private String fileName;
	/**
	 * ͼ�ļ����ʹ���ֵ������ͼ/��׼ͼ�� 
	 */
	private String fileType;
	/**
	 * �ļ����ʹ�������
	 */
	private String fileTypeName;
	/**
	 * ͼҵ�����ʹ���ֵ
	 */
	private String busiType;
	/**
	 * ҵ��ͼ���ʹ�����
	 */
	private String busiTypeName;
	/**
	 * ͼ��ʽ����ֵ
	 */
	private String fileFormat;
	/**
	 * ͼ��ʽ��������
	 */
	private String fileFormatName;
	/**
	 * ��������
	 */
	private String operator;
	/**
	 * ����޸�ʱ��
	 */
	private String modifyTime;
	/**
	 * �ļ�����
	 */
	private String content;
	/**
	 * ͼ��������������10��
	 */
	private String[] params;
	
	/**
	 * ���캯��
	 */
	public GraphFileBean(){
		params = new String[10];
	}

	/**
	 * ��ȡ�ļ���
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * �����ļ���
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * ��ȡ�ļ����ʹ���ֵ
	 * @return
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * �����ļ����ʹ���ֵ
	 * @param fileType
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * ��ȡ�ļ���ҵ�����ʹ���ֵ
	 * @return
	 */
	public String getBusiType() {
		return busiType;
	}
	/**
	 * �����ļ���ҵ�����ʹ���ֵ
	 * @param busiType
	 */
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	/**
	 * ��ȡ�ļ���ʽ����ֵ
	 * @return
	 */
	public String getFileFormat() {
		return fileFormat;
	}
	/**
	 * �����ļ���ʽ����ֵ
	 * @param fileFormat
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * ��ȡ����޸���
	 * @return
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * ��������޸���
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * ��ȡ����޸�ʱ��
	 * @return
	 */
	public String getModifyTime() {
		return modifyTime;
	}
	/**
	 * ��������޸�ʱ��
	 * @param modifyTime
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * ��ȡ�ļ�����
	 * @return
	 */
	public String getContent() {
		return content;
	}
	/**
	 * �����ļ�����
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * ��ȡ�ļ���������
	 * @param index:�ڼ�����������
	 * @return
	 */
	public String getParams(int index) {
		return params[index];
	}
	/**
	 * �����ļ���������
	 * @param index:�ڼ�����������
	 * @param param:����ֵ
	 */
	public void setParams(int index, String param) {
		this.params[index] = param;
	}
	/**
	 * ��ȡ�ļ���������
	 * @return
	 */
	public String[] getParams() {
		return params;
	}
	/**
	 * �����ļ���������
	 * @param params
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * ��ȡҵ��ͼ���ʹ�����
	 * @return
	 */
	public String getBusiTypeName() {
		return busiTypeName;
	}
	/**
	 * ����ҵ��ͼ���ʹ�����
	 * @param busiTypeName
	 */
	public void setBusiTypeName(String busiTypeName) {
		this.busiTypeName = busiTypeName;
	}

	/**
	 * ��ȡ�ļ����
	 * @return
	 */
	public String getID() {
		return ID;
	}

	/**
	 * �����ļ����
	 * @param id
	 */
	public void setID(String id) {
		ID = id;
	}

	/**
	 * ��ȡ�ļ����ʹ�������
	 * @return
	 */
	public String getFileTypeName() {
		return fileTypeName;
	}

	/**
	 * �����ļ����ʹ�������
	 * @param fileTypeName
	 */
	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	/**
	 * ��ȡͼ��ʽ��������
	 * @return
	 */
	public String getFileFormatName() {
		return fileFormatName;
	}

	/**
	 * ����ͼ��ʽ��������
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
