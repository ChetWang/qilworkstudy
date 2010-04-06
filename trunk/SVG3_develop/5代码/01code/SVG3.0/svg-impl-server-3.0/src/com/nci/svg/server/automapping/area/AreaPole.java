package com.nci.svg.server.automapping.area;

import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * 标题：ElectricPole.java
 * </p>
 * <p>
 * 描述：物理杆塔类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-02-26
 * @version 1.0
 */
public class AreaPole extends BasicProperty {
	/**
	 * @param 线路基础属性，依次为：
	 * @param 杆塔编号、杆塔名字、杆型、杆塔类型、
	 * @param 、高度、档距、转角、挂接杆标志、
	 * @param 挂接杆转角
	 */
	private static String[] basicPropertyName = { "code", "name", "model",
			"kind", "opType", "height", "distance", "angle", "contactTag",
			"contactAngle" };
	/**
	 * 杆塔在图形上的坐标
	 */
	private double[] coordinate;
	/**
	 * 关联的逻辑杆塔数
	 */
	private int multiCir;
	/**
	 * 接入杆标志
	 */
	private boolean isPlugin;
	/**
	 * 逻辑杆标志
	 */
	private boolean isLogic;

	/**
	 * 构造函数
	 */
	public AreaPole() {
		super(basicPropertyName);
		this.coordinate = new double[] { Double.NaN, Double.NaN };
		this.multiCir = -1;
		this.isPlugin = false;
		this.isLogic = false;
	}

	/**
	 * 按照输入的杆塔生成一个新的杆塔对象
	 * 
	 * @param pole
	 */
	public AreaPole(AreaPole pole) {
		super(pole.getPropertNames());
		this.multiCir = pole.getMultiCir();
		this.isPlugin = pole.isPlugin();
		this.isLogic = pole.isLogic();
		setCoordinate(pole.getCoordinate());
		LinkedHashMap map = (LinkedHashMap) pole.getProperties().clone();
		setProperties(map);
	}

	/**
	 * 设置杆塔图形坐标
	 * 
	 * @return double[]
	 */
	public double[] getCoordinate() {
		return coordinate;
	}

	/**
	 * 获取杆塔X轴坐标
	 * 
	 * @return double
	 */
	public double getCoordinateX() {
		if (this.coordinate != null)
			return this.coordinate[0];
		else
			return Double.NaN;
	}

	/**
	 * 获取杆塔Y轴坐标
	 * 
	 * @return double
	 */
	public double getCoordinateY() {
		if (this.coordinate != null)
			return this.coordinate[1];
		else
			return Double.NaN;
	}

	/**
	 * 设置杆塔图形坐标
	 * 
	 * @param coordinate:double[]
	 */
	public void setCoordinate(double[] coordinate) {
		this.coordinate = new double[] { Double.NaN, Double.NaN };
		if (this.coordinate.length >= 2) {
			this.coordinate[0] = coordinate[0];
			this.coordinate[1] = coordinate[1];
		}
	}

	/**
	 * 设置杆塔图形坐标
	 * 
	 * @param x:double:X轴坐标
	 * @param y:double:Y轴坐标
	 */
	public void setCoordinate(double x, double y) {
		if (this.coordinate == null) {
			this.coordinate = new double[] { Double.NaN, Double.NaN };
		}
		this.coordinate[0] = x;
		this.coordinate[1] = y;
	}

	/**
	 * 获取逻辑杆塔关联数
	 * 
	 * @return int
	 */
	public int getMultiCir() {
		return multiCir;
	}

	/**
	 * 设置逻辑杆塔关联数
	 * 
	 * @param multiCir:int
	 */
	public void setMultiCir(int multiCir) {
		this.multiCir = multiCir;
	}

	/**
	 * 获取接入杆标志
	 * 
	 * @return boolean
	 */
	public boolean isPlugin() {
		return isPlugin;
	}

	/**
	 * 设置接入杆标志
	 * 
	 * @param isPlugin:boolean:接入杆标志
	 */
	public void setPlugin(boolean isPlugin) {
		this.isPlugin = isPlugin;
	}

	/**
	 * 获取逻辑杆标志
	 * 
	 * @return boolean
	 */
	public boolean isLogic() {
		return isLogic;
	}

	/**
	 * 设置逻辑杆标志
	 * 
	 * @param isLogic:boolean:逻辑杆标志
	 */
	public void setLogic(boolean isLogic) {
		this.isLogic = isLogic;
	}
}
