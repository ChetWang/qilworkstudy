package com.nci.domino.shape.basic;

import java.awt.Graphics2D;
import java.io.Serializable;

import com.nci.domino.PaintBoardBasic;

/**
 * @author 陈新中
 * 基本形状接口 该接口定义了绘制方法和是否在图形上和是否落在一个四边形范围内这三个方法 
 *
 */
public interface BasicShape extends Serializable {
	/**
	 * 画图形
	 * @param g2    Graphics2D对象 相当于一块画布
	 * @param pb    PaintBoard对象 是Graphics2D所在的panel
	 */
	public void drawShape(Graphics2D g2, PaintBoardBasic pb);

	/**
	 * 判断一个点是否在该图形范围内
	 * @param wx    坐标x
	 * @param wy    坐标y
	 * @param e     分辨率e  仅对线形对象有效
	 * @return      布尔值
	 */
	public boolean isOnRange(double wx, double wy, double e);

	/**
	 * 判断该图形是否被包在指定的凸四边形范围内
	 * @param p1   p1 p2 p3 p4是凸四边形的四个顶点  按照逆时针或者顺时针
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return   返回布尔值
	 */
	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4);

	
}