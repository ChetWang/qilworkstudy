package com.nci.svg.district.relate.tree;

import java.awt.Color;
import java.awt.Font;

/**
 * <p>
 * ���⣺DynamicTreeNodeBean.java
 * </p>
 * <p>
 * ������ ���������ʾ���ڵ���Ϣ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-5-1
 * @version 1.0
 */
public class TreeData {

	/**
	 * �ڵ���ʾ����
	 */
	protected Font font;

	/**
	 * �ڵ���ʾ��ɫ
	 */
	protected Color color;

	/**
	 * �ڵ���ʾ����
	 */
	protected String string;

	/**
	 * @���� ���캯��
	 * @param newFont
	 * @param newColor
	 * @param newString
	 */
	public TreeData(Font newFont, Color newColor, String newString) {
		font = newFont;
		color = newColor;
		string = newString;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @���� ���������ʽ
	 * @param newFont
	 */
	public void setFont(Font newFont) {
		font = newFont;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @���� ��ȡ�����ʽ
	 * @return
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @���� ����������ɫ
	 * @param newColor
	 */
	public void setColor(Color newColor) {
		color = newColor;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @���� ��ȡ������ɫ
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @���� ������ʾ��Ϣ
	 * @param newString
	 */
	public void setString(String newString) {
		string = newString;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @���� ��ȡ��ʾ��Ϣ
	 * @return
	 */
	public String string() {
		return string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return string;
	}

}
