package com.nci.svg.server.automapping.system;

import com.nci.svg.server.automapping.comm.BasicProperty;
import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * ���⣺SystemSubstation.java
 * </p>
 * <p>
 * ������ ϵͳͼ�Զ���ͼ�õĳ�վ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-7-13
 * @version 1.0
 */
/**
 * <p>
 * ���⣺SystemSubstation.java
 * </p>
 * <p>
 * ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-7-13
 * @version 1.0
 */
public class SystemSubstation extends BasicProperty {
	/**
	 * @apram ��·�������ԣ�����Ϊ��
	 * @param ���ơ���š���ѹ�ȼ�������Գ�վ���롢����Գ�վ����
	 * @param Ӧ�ñ�š����糧���͡��������򡢡�
	 * @param ��վ���ͣ�Substation
	 *            ���վ��PowerPlant ���糧��
	 */
	private static String[] basicPropertyName = { "Name", "Id", "VoltageLevel",
			"distance", "angle", "AppCode", "Type", "Dwdm", "zcsx",
			"substationType", "X", "Y" };

	/**
	 * ��������
	 */
	private Point coordinate;
	/**
	 * ϵͳͼ����
	 */
	private Point mapCoord;
	/**
	 * ����ڻ�׼��ĽǶ�
	 */
	private double angle;

	/**
	 * ���캯��
	 */
	public SystemSubstation() {
		super(basicPropertyName);
		this.angle = 0.0;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @���� ��ȡ��վ��������
	 * @return
	 */
	public Point getCoordinate() {
		return coordinate;
	}

	/**
	 * 2009-7-13 Add by ZHM
	 * 
	 * @���� ���ó�վ��������
	 * @param coordinate:Point:��վ��������
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
	 * @���� ���ó�վ��������
	 * @param x:double:X������
	 * @param y:double:Y������
	 */
	public void setCoordinate(double x, double y) {
		this.coordinate = new Point(x, y);
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��ȡ��ԽǶ�
	 * @return
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ������ԽǶ�
	 * @param angle:double:��Խ�
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @���� ��ȡϵͳͼ����
	 * @return
	 */
	public Point getMapCoord() {
		return mapCoord;
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @���� ����ϵͳͼ����
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
	 * @���� ����ϵͳͼ����
	 * @param x:double:X������
	 * @param y:double:Y������
	 */
	public void setMapCoord(double x, double y) {
		this.mapCoord = new Point(x, y);
	}
}
