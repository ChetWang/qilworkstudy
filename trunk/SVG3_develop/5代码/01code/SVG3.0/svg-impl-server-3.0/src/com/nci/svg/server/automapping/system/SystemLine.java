package com.nci.svg.server.automapping.system;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * ���⣺SystemLine.java
 * </p>
 * <p>
 * ������ϵͳͼ�Զ���ͼ�õ���·��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-10
 * @version 1.0
 */
public class SystemLine extends BasicProperty {
	/**
	 * @apram ��·�������ԣ�����Ϊ��
	 * @param ���ơ���š���ʼ��վ��š���ʼ��վ���ơ���ֹ��վ��š�
	 * @param ��ֹ��վ���ơ���ѹ�ȼ���Ӧ�ñ�š���������
	 */
	private static String[] basicPropertyName = { "Name", "Id",
			"StartSubId", "startEquipmentName", "EndSubId",
			"endEquipmentName", "VoltageLevel", "AppCode", "Dwdm", "zcsx" };

	/**
	 * ���캯��
	 */
	public SystemLine() {
		super(basicPropertyName);
	}
}
