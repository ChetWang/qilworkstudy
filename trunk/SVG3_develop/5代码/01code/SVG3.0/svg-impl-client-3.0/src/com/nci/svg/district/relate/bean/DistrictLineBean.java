package com.nci.svg.district.relate.bean;

import java.util.LinkedList;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictLineBean extends BasicProperty {
	public static final String MAIN_LINE = "����";
	public static final String LATERAL_LINE = "֧��";
	public static final String SERVICE_LINE = "�ӻ���";
	/**
	 * @apram ��·�������ԣ�����Ϊ��
	 * @param ��·��š���·�����롢��·���ơ���·�ϼ���·��š�
	 * @param �ϼ���·֧�Ӹ˻�·��š������·��š�
	 */
	private static String[] basicPropertyName = { "bl_objid", "bl_ddm",
			"bl_name", "bl_sjxl", "bl_zjgid", "bl_contact" };
	/**
	 * ��·�»�·���У���������������Ϣ
	 */
	private LinkedList<String> poles;
	/**
	 * �м�����־
	 */
	private boolean isMiddle;
	/**
	 * ����·���
	 */
	private LinkedList<String> subLines;
	/**
	 * ��·����
	 */
	private String lineType;
	/**
	 * ����·���Ӹ�˳���
	 */
	private String position;

	/**
	 * ���캯��
	 */
	public DistrictLineBean() {
		super(basicPropertyName);
		isMiddle = false;
		subLines = new LinkedList<String>();
		lineType = MAIN_LINE;
		position = "";
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @���� ��ȡ����·���Ӹ�˳���
	 * @return
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * 2009-4-20 Add by ZHM
	 * 
	 * @���� ���ø���·���Ӹ�˳���
	 * @param position
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ��ȡ��·����
	 * @return
	 */
	public String getLineType() {
		return lineType;
	}

	/**
	 * 2009-4-17 Add by ZHM
	 * 
	 * @���� ������·����
	 * @param lineType
	 */
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public boolean isMiddle() {
		return isMiddle;
	}

	public void setMiddle(boolean isMiddle) {
		this.isMiddle = isMiddle;
	}

	/**
	 * 2009-4-16 Add by ZHM ��ȡ����·���
	 * 
	 * @return
	 */
	public LinkedList<String> getSubLines() {
		return subLines;
	}

	/**
	 * 2009-4-16 Add by ZHM ����һ������·
	 * 
	 * @param subLineID
	 */
	public void setSubLines(String subLineID) {
		this.subLines.add(subLineID);
	}

	/**
	 * 2009-4-16 Add by ZHM ��������·���
	 * 
	 * @param subLines
	 */
	public void setSubLines(LinkedList<String> subLines) {
		this.subLines = subLines;
	}

	/**
	 * ����
	 * 
	 * @return the poles
	 */
	public LinkedList<String> getPoles() {
		return poles;
	}

	/**
	 * ����
	 * 
	 * @param poles
	 */
	public void setPoles(LinkedList<String> poles) {
		this.poles = poles;
	}

}
