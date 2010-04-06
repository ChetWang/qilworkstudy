package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>
 * 标题：ResultBean.java
 * </p>
 * <p>
 * 描述： 交互结果实体类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx
 * @时间: 2008-12-24
 * @version 1.0
 */
public class ResultBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7011810584581858498L;
	/**
	 * 返回成功
	 */
	public static final int RETURN_SUCCESS = 0;
	/**
	 * 返回失败
	 */
	public static final int RETURN_ERROR = -1;
	/**
	 * 返回对象中的结果对象
	 */
	public static final int RETURN_STRING = 1;

	
	/**
     * 返回结果，RETURN_SUCCESS表示成功，RETURN_ERROR表示失败
     */
    private int returnFlag = 0;
    /**
     * 失败提示信息
     */
    private String errorText;
    /**
     * 返回结果集类型
     */
    private String returnType;
    /**
     * 返回结果
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
     * 获取返回标志
     * @return RETURN_SUCCESS表示成功，RETURN_ERROR表示失败
     */
	public int getReturnFlag() {
		return returnFlag;
	}
	/**
	 * 设置返回标志
	 * @param returnFlag RETURN_SUCCESS表示成功，RETURN_ERROR表示失败
	 */
	public void setReturnFlag(int returnFlag) {
		this.returnFlag = returnFlag;
	}
	
	/**
	 * 获取失败提示信息
	 * @return String
	 */
	public String getErrorText() {
		return errorText;
	}
	/**
	 * 设置失败提示信息
	 * @param errorText String 
	 */
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
	/**
	 * 获取结果集类型
	 * @return String
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * 设置结果集类型
	 * @param returnType String
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	/**
	 * 获取返回结果
	 * @return Object
	 */
	public Object getReturnObj() {
		return returnObj;
	}
	/**
	 * 设置返回结果
	 * @param returnObj Object
	 */
	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}
    
    
}
