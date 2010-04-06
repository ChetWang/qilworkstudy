package com.nci.svg.district.relate.bean;

import java.util.HashMap;

import com.nci.svg.district.relate.BasicProperty;

public class DistrictAreaBean extends BasicProperty {
	/**
	 * @apram ̨���������ԣ�����Ϊ��
	 * @param ̨�����,
	 *            ̨������
	 */
	private static String[] basicPropertyName = { "sd_objid", "sd_name" };

	/**
	 * ����б�
	 */
	private HashMap<String, DistrictTransformerBean> transformers;
	/**
	 * ������б�
	 */
	private HashMap<String, DistrictBoxBean> boxes;
	/**
	 * ��·�б�
	 */
	private HashMap<String, DistrictLineBean> lines;
	/**
	 * �����б�
	 */
	private HashMap<String, DistrictPoleBean> poles;

	/**
	 * ���캯��
	 */
	public DistrictAreaBean() {
		super(basicPropertyName);
		transformers = new HashMap<String, DistrictTransformerBean>();
		boxes = new HashMap<String, DistrictBoxBean>();
		lines = new HashMap<String, DistrictLineBean>();
		poles = new HashMap<String, DistrictPoleBean>();
	}

	/**
	 * 2009-4-10 Add by ZHM ��ȡ��������
	 * 
	 * @return
	 */
	public HashMap<String, DistrictTransformerBean> getTransformers() {
		return transformers;
	}

	/**
	 * 2009-4-10 Add by ZHM ������������
	 * 
	 * @param transformers
	 */
	public void setTransformers(
			HashMap<String, DistrictTransformerBean> transformers) {
		this.transformers = transformers;
	}

	/**
	 * 2009-4-10 Add by ZHM ��ȡ����������
	 * 
	 * @return
	 */
	public HashMap<String, DistrictBoxBean> getBoxs() {
		return boxes;
	}

	/**
	 * 2009-4-10 Add by ZHM ��������������
	 * 
	 * @param boxs
	 */
	public void setBoxs(HashMap<String, DistrictBoxBean> boxs) {
		this.boxes = boxs;
	}

	/**
	 * 2009-4-10 Add by ZHM ��ȡ��·������
	 * 
	 * @return
	 */
	public HashMap<String, DistrictLineBean> getLine() {
		return lines;
	}

	/**
	 * 2009-4-10 Add by ZHM ������·������
	 * 
	 * @param line
	 */
	public void setLine(HashMap<String, DistrictLineBean> lines) {
		this.lines = lines;
	}

	/**
	 * ����
	 * 
	 * @return poles
	 */
	public HashMap<String, DistrictPoleBean> getPoles() {
		return poles;
	}

	/**
	 * ����
	 * 
	 * @param poles
	 */
	public void setPoles(HashMap<String, DistrictPoleBean> poles) {
		this.poles = poles;
	}
}
