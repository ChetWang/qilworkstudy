package com.nci.svg.server.automapping.system;

import com.nci.svg.server.automapping.comm.BasicProperty;
import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * 标题：SystemSubstation.java
 * </p>
 * <p>
 * 描述： 系统图自动成图用的厂站类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-7-13
 * @version 1.0
 */
/**
 * <p>
 * 标题：SystemSubstation.java
 * </p>
 * <p>
 * 描述：
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-7-13
 * @version 1.0
 */
public class SystemSubstation extends BasicProperty {
	/**
	 * @apram 线路基础属性，依次为：
	 * @param 名称、编号、电压等级、与相对厂站距离、与相对厂站方向
	 * @param 应用编号、发电厂类型、所属区域、、
	 * @param 厂站类型（Substation
	 *            变电站；PowerPlant 发电厂）
	 */
	private static String[] basicPropertyName = { "Name", "Id", "VoltageLevel",
			"distance", "angle", "AppCode", "Type", "Dwdm", "zcsx",
			"substationType", "X", "Y" };

	/**
	 * 地理坐标
	 */
	private Point coordinate;
	/**
	 * 系统图坐标
	 */
	private Point mapCoord;
	/**
	 * 相对于基准点的角度
	 */
	private double angle;

	/**
	 * 构造函数
	 */
	public SystemSubstation() {
		super(basicPropertyName);
		this.angle = 0.0;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 获取厂站地理坐标
	 * @return
	 */
	public Point getCoordinate() {
		return coordinate;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 设置厂站地理坐标
	 * @param coordinate:Point:厂站地理坐标
	 */
	public void setCoordinate(Point coordinate) {
		if (coordinate != null)
			this.coordinate = new Point(coordinate);
		else
			this.coordinate = new Point();
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @功能 设置厂站地理坐标
	 * @param x:double:X轴坐标
	 * @param y:double:Y轴坐标
	 */
	public void setCoordinate(double x, double y) {
		this.coordinate = new Point(x, y);
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 获取相对角度
	 * @return
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 设置相对角度
	 * @param angle:double:相对角
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 获取系统图坐标
	 * @return
	 */
	public Point getMapCoord() {
		return mapCoord;
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 设置系统图坐标
	 * @param mapCoord
	 */
	public void setMapCoord(Point mapCoord) {
		if (mapCoord != null)
			this.mapCoord = new Point(mapCoord);
		else
			this.mapCoord = new Point();
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 设置系统图坐标
	 * @param x:double:X轴坐标
	 * @param y:double:Y轴坐标
	 */
	public void setMapCoord(double x, double y) {
		this.mapCoord = new Point(x, y);
	}
}
