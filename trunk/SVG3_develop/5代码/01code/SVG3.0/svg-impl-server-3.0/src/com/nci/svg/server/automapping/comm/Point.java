package com.nci.svg.server.automapping.comm;

/**
 * <p>
 * 标题：Point.java
 * </p>
 * <p>
 * 描述： 坐标点类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-4-2
 * @version 1.0
 */
public class Point {
	/**
	 * X轴坐标
	 */
	private double x;
	/**
	 * Y轴坐标
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
	 * 2009-4-2 Add by ZHM 获取X轴坐标
	 * 
	 * @return double
	 */
	public double getX() {
		return x;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置X轴坐标
	 * 
	 * @param x:double:X轴坐标值
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * 2009-4-2 Add by ZHM 获取Y轴坐标
	 * 
	 * @return double
	 */
	public double getY() {
		return y;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置Y轴坐标
	 * 
	 * @param y:double:Y轴坐标值
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 点移动
	 * @param x:double:X轴移动距离
	 * @param y:double:Y轴移动距离
	 */
	public void movePoint(double x, double y) {
		this.x += x;
		this.y += y;
	}
}
