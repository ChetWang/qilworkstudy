package com.nci.svg.sdk.graphunit;

import java.io.Serializable;

import com.nci.svg.sdk.bean.SimpleCodeBean;

/**
 * 为区别不同类型的symbol（图元、模板）设定的javabean
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
	 * 图元大类，指名是图元还是模板
	 */
	private String symbolType;

	/**
	 * 图元小类代码BEAN
	 */
	private SimpleCodeBean variety = null;

	public SymbolTypeBean() {

	}

	/**
	 * 构造函数
	 * 
	 * @param symbolType:图元大类
	 * @param vairety:图元类型名称，如刀闸？开关？
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
	 * 获取图元大类 图元还是模板
	 * 
	 * @return
	 */
	public String getSymbolType() {
		return symbolType;
	}

	/**
	 * 设置图元大类 图元还是模板
	 * 
	 * @param symbolType:图元大类
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
	 * 获取小类对象
	 * 
	 * @return
	 */
	public SimpleCodeBean getVariety() {
		return variety;
	}

	/**
	 * 设置小类对象
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
