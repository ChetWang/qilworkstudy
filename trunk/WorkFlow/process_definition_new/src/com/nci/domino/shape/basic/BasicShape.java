package com.nci.domino.shape.basic;

import java.awt.Graphics2D;
import java.io.Serializable;

import com.nci.domino.PaintBoardBasic;

/**
 * @author ������
 * ������״�ӿ� �ýӿڶ����˻��Ʒ������Ƿ���ͼ���Ϻ��Ƿ�����һ���ı��η�Χ������������ 
 *
 */
public interface BasicShape extends Serializable {
	/**
	 * ��ͼ��
	 * @param g2    Graphics2D���� �൱��һ�黭��
	 * @param pb    PaintBoard���� ��Graphics2D���ڵ�panel
	 */
	public void drawShape(Graphics2D g2, PaintBoardBasic pb);

	/**
	 * �ж�һ�����Ƿ��ڸ�ͼ�η�Χ��
	 * @param wx    ����x
	 * @param wy    ����y
	 * @param e     �ֱ���e  �������ζ�����Ч
	 * @return      ����ֵ
	 */
	public boolean isOnRange(double wx, double wy, double e);

	/**
	 * �жϸ�ͼ���Ƿ񱻰���ָ����͹�ı��η�Χ��
	 * @param p1   p1 p2 p3 p4��͹�ı��ε��ĸ�����  ������ʱ�����˳ʱ��
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return   ���ز���ֵ
	 */
	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4);

	
}