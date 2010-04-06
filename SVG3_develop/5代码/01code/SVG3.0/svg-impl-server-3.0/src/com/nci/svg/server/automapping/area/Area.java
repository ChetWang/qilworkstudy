package com.nci.svg.server.automapping.area;

import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * ���⣺Area.java
 * </p>
 * <p>
 * ������̨����
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
public class Area extends BasicProperty {
	/**
	 * @param ̨���������ԣ�����Ϊ��
	 * @param ̨����š�̨�����ơ���ѹ�ȼ���
	 * @param ��˾���롢����Ӫҵ�����롢С������
	 */
	private static String[] basicPropertyName = { "code", "name", "basevol",
			"company", "serviceStation", "village" };
	/**
	 * ̨����ѹ��
	 */
	private AreaTransformer transformer;
	/**
	 * �����������
	 */
	private LinkedHashMap electricPoleList;
	/**
	 * ��·����
	 */
	private LinkedHashMap linesList;

	public Area() {
		super(basicPropertyName);
		electricPoleList = new LinkedHashMap();
	}

	/**
	 * ��ȡ��ѹ������
	 * 
	 * @return Transformer
	 */
	public AreaTransformer getTransformer() {
		return transformer;
	}

	/**
	 * ���ñ�ѹ������
	 * 
	 * @param transformer:Transformer:��ѹ��
	 */
	public void setTransformer(AreaTransformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * ��ȡ�����������
	 * 
	 * @return ArrayList
	 */
	public LinkedHashMap getElectricPoleList() {
		return electricPoleList;
	}

	/**
	 * ���������������
	 * 
	 * @param electricPoleList:ArrayList:�����������
	 */
	public void setElectricPoleList(LinkedHashMap electricPoleList) {
		this.electricPoleList = electricPoleList;
	}

	/**
	 * ��ȡ��·����
	 * 
	 * @return ArrayList
	 */
	public LinkedHashMap getLinesList() {
		return linesList;
	}

	/**
	 * ������·����
	 * 
	 * @param linesList:ArrayList:��·����
	 */
	public void setLinesList(LinkedHashMap linesList) {
		this.linesList = linesList;
	}

}
