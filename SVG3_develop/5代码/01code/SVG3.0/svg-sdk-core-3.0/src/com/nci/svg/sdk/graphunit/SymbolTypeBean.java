package com.nci.svg.sdk.graphunit;

import java.io.Serializable;

import com.nci.svg.sdk.bean.SimpleCodeBean;

/**
 * Ϊ����ͬ���͵�symbol��ͼԪ��ģ�壩�趨��javabean
 * 
 * @author Qil.Wong
 * 
 */
public class SymbolTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111875963101503496L;
	/**
	 * ͼԪ���ָ࣬����ͼԪ����ģ��
	 */
	private String symbolType;

	/**
	 * ͼԪС�����BEAN
	 */
	private SimpleCodeBean variety = null;

	public SymbolTypeBean() {

	}

	/**
	 * ���캯��
	 * 
	 * @param symbolType:ͼԪ����
	 * @param vairety:ͼԪ�������ƣ��絶բ�����أ�
	 */
	public SymbolTypeBean(String symbolType, SimpleCodeBean vairety) {
		if (symbolType == null) {
			this.symbolType = "";
		} else
			this.symbolType = symbolType;
		this.variety = vairety;
		// if (symbolSubCode == null) {
		// this.symbolSubCode = "";
		// } else
		// this.symbolSubCode = symbolSubCode;
		// if (symbolSubName == null) {
		// this.symbolSubName = "";
		// } else
		// this.symbolSubName = symbolSubName;
	}

	/**
	 * ��ȡͼԪ���� ͼԪ����ģ��
	 * 
	 * @return
	 */
	public String getSymbolType() {
		return symbolType;
	}

	/**
	 * ����ͼԪ���� ͼԪ����ģ��
	 * 
	 * @param symbolType:ͼԪ����
	 */
	public void setSymbolType(String symbolType) {
		if (symbolType == null)
			symbolType = "";
		this.symbolType = symbolType;
	}

	public int hashCode() {
		int hash = -1;
		if (symbolType == null && variety != null) {
			hash = variety.hashCode();
		} else if (symbolType != null && variety == null) {
			hash = symbolType.hashCode();
		} else if (symbolType == null && variety == null) {
			hash = 0;
		} else {
			hash = symbolType.hashCode() * variety.hashCode();
		}
		return hash;
	}

	public boolean equals(Object o) {
		boolean flag = false;
		if (o instanceof SymbolTypeBean) {
			try {
				SymbolTypeBean other = (SymbolTypeBean) o;
				flag = this.symbolType == other.getSymbolType() ? true
						: this.symbolType.equals(other.getSymbolType());
				if (flag) {
					flag = this.variety == other.getVariety() ? true
							: this.variety.equals(other.getVariety());
				}
			} catch (Exception e) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * ��ȡС�����
	 * 
	 * @return
	 */
	public SimpleCodeBean getVariety() {
		return variety;
	}

	/**
	 * ����С�����
	 * 
	 * @param variety
	 */
	public void setVariety(SimpleCodeBean variety) {
		this.variety = variety;
	}

	public String toString() {
		return variety.getName();
	}

}
