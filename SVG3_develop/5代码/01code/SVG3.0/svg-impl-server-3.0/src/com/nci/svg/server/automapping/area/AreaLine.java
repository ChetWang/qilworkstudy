package com.nci.svg.server.automapping.area;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * ���⣺Line.java
 * </p>
 * <p>
 * ������̨��ͼ�Զ���ͼ�õ���·��
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
public class AreaLine extends BasicProperty {
	/**
	 * @apram ��·�������ԣ�����Ϊ��
	 * @param ��·��š���·���ơ���·�������͡���·��ʼ�������ơ�
	 * @param ��·��ֹ�������ơ���·�ܳ��ȡ���·��ʼ�ǡ����ҽ��м������־��
	 * @param ����·��š�����·�Ľ���������
	 */
	private static String[] basicPropertyName = { "code", "name", "leadModel",
			"from", "to", "distance", "startAngle", "middleTag", "pline",
			"startPole" };
	/**
	 * ��·�¸������У���������������Ϣ
	 */
	private ArrayList poles;
	/**
	 * ֧�߱�־
	 */
	private boolean isBranch;
	/**
	 * �м�����־
	 */
	private boolean isMiddle;

	/**
	 * ���캯��
	 */
	public AreaLine() {
		super(basicPropertyName);
		isBranch = false;
		isMiddle = false;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ArrayList
	 */
	public ArrayList getPoles() {
		return poles;
	}

	/**
	 * ������·�¸�������
	 * 
	 * @param poles:ArrayList:��������
	 */
	public void setPoles(ArrayList poles) {
		this.poles = poles;
	}

	/**
	 * ��ȡ֧�߱�־
	 * 
	 * @return boolean
	 */
	public boolean isBranch() {
		return isBranch;
	}

	/**
	 * ����֧�߱�־
	 * 
	 * @param isBranch:boolean:֧�߱�־
	 */
	public void setBranch(boolean isBranch) {
		this.isBranch = isBranch;
	}

	/**
	 * ��ȡ�м�����־
	 * 
	 * @return boolean
	 */
	public boolean isMiddle() {
		return isMiddle;
	}

	/**
	 * �����м�����־
	 * 
	 * @param isMiddle:boolean:�м�����־
	 */
	public void setMiddle(boolean isMiddle) {
		this.isMiddle = isMiddle;
	}

}
