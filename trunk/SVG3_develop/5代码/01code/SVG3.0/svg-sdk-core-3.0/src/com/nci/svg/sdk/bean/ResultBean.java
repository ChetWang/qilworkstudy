package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * ���⣺ResultBean.java
 * </p>
 * <p>
 * ������ �������ʵ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx
 * @ʱ��: 2008-12-24
 * @version 1.0
 */
public class ResultBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7011810584581858498L;
	/**
	 * ���سɹ�
	 */
	public static final int RETURN_SUCCESS = 0;
	/**
	 * ����ʧ��
	 */
	public static final int RETURN_ERROR = -1;
	/**
	 * ���ض����еĽ������
	 */
	public static final int RETURN_STRING = 1;

	
	/**
     * ���ؽ����RETURN_SUCCESS��ʾ�ɹ���RETURN_ERROR��ʾʧ��
     */
    private int returnFlag = 0;
    /**
     * ʧ����ʾ��Ϣ
     */
    private String errorText;
    /**
     * ���ؽ��������
     */
    private String returnType;
    /**
     * ���ؽ��
     */
    private Object returnObj=null;
    
    public ResultBean()
    {
    	
    }

    public ResultBean(int flag,String errorText,String type,Object returnObj)
    {
    	this.returnFlag = flag;
    	this.errorText = errorText;
    	this.returnType = type;
    	this.returnObj = returnObj;
    }
    
    /**
     * ��ȡ���ر�־
     * @return RETURN_SUCCESS��ʾ�ɹ���RETURN_ERROR��ʾʧ��
     */
	public int getReturnFlag() {
		return returnFlag;
	}
	/**
	 * ���÷��ر�־
	 * @param returnFlag RETURN_SUCCESS��ʾ�ɹ���RETURN_ERROR��ʾʧ��
	 */
	public void setReturnFlag(int returnFlag) {
		this.returnFlag = returnFlag;
	}
	
	/**
	 * ��ȡʧ����ʾ��Ϣ
	 * @return String
	 */
	public String getErrorText() {
		return errorText;
	}
	/**
	 * ����ʧ����ʾ��Ϣ
	 * @param errorText String 
	 */
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
	/**
	 * ��ȡ���������
	 * @return String
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * ���ý��������
	 * @param returnType String
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	/**
	 * ��ȡ���ؽ��
	 * @return Object
	 */
	public Object getReturnObj() {
		return returnObj;
	}
	/**
	 * ���÷��ؽ��
	 * @param returnObj Object
	 */
	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}
    
    
}
