package com.nci.svg.server.automapping.area;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * ���⣺Transformer.java
 * </p>
 * <p>
 * ������̨��ͼ�Զ���ͼ�õı�ѹ����
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
public class AreaTransformer extends BasicProperty {
	
	/**
	 * @param ��ѹ���������ԣ�����Ϊ��
	 * @param ��ѹ����š���ѹ�����ơ���ѹ�ȼ�����ѹ������
	 */
	private static String[] basicPropertyName = { "code", "name", "voltage",
			"capacity" };
	
	/**
	 * ���캯��
	 */
	public AreaTransformer(){
		super(basicPropertyName);
	}
}
