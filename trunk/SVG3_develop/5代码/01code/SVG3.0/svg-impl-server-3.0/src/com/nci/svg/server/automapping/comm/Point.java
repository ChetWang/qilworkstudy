package com.nci.svg.server.automapping.comm;

/**
 * <p>
 * ���⣺Point.java
 * </p>
 * <p>
 * ������ �������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-4-2
 * @version 1.0
 */
public class Point {
	/**
	 * X������
	 */
	private double x;
	/**
	 * Y������
	 */
	private double y;

	public Point() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point point) {
		this.x = point.getX();
		this.y = point.getY();
	}

	/**
	 * 2009-4-2 Add by ZHM ��ȡX������
	 * 
	 * @return double
	 */
	public double getX() {
		return x;
	}

	/**
	 * 2009-4-2 Add by ZHM ����X������
	 * 
	 * @param x:double:X������ֵ
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * 2009-4-2 Add by ZHM ��ȡY������
	 * 
	 * @return double
	 */
	public double getY() {
		return y;
	}

	/**
	 * 2009-4-2 Add by ZHM ����Y������
	 * 
	 * @param y:double:Y������ֵ
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ���ƶ�
	 * @param x:double:X���ƶ�����
	 * @param y:double:Y���ƶ�����
	 */
	public void movePoint(double x, double y) {
		this.x += x;
		this.y += y;
	}
}
