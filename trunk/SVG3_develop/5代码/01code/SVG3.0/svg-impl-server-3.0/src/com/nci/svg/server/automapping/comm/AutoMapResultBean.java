package com.nci.svg.server.automapping.comm;

/**
 * <p>
 * ���⣺AutoMapResultBean.java
 * </p>
 * <p>
 * �������Զ���ͼ������Ϣ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-03
 * @version 1.0
 */
public class AutoMapResultBean {
	/**
	 * �ɹ���־
	 */
	private boolean flag;
	/**
	 * ������Ϣ
	 */
	private String errMsg;
	/**
	 * ��Ϣ
	 */
	private Object msg;
	
	/**
	 * ���캯��
	 */
	public AutoMapResultBean(){
		flag = true;
	}

	/**
	 * ��ȡ�ɹ���־
	 * 
	 * @return String
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * ���óɹ���־
	 * 
	 * @param flag:boolean:�ɹ���־
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return String
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * ���ô�����Ϣ
	 * 
	 * @param errMsg:String:������Ϣ
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * ��ȡ��Ϣ
	 * 
	 * @return Object
	 */
	public Object getMsg() {
		return msg;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param msg:Object:��Ϣ
	 */
	public void setMsg(Object msg) {
		this.msg = msg;
	}

}
