package com.nci.svg.server.automapping.area;

import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.BasicProperty;

/**
 * <p>
 * ���⣺ElectricPole.java
 * </p>
 * <p>
 * ���������������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-02-26
 * @version 1.0
 */
public class AreaPole extends BasicProperty {
	/**
	 * @param ��·�������ԣ�����Ϊ��
	 * @param ������š��������֡����͡��������͡�
	 * @param ���߶ȡ����ࡢת�ǡ��ҽӸ˱�־��
	 * @param �ҽӸ�ת��
	 */
	private static String[] basicPropertyName = { "code", "name", "model",
			"kind", "opType", "height", "distance", "angle", "contactTag",
			"contactAngle" };
	/**
	 * ������ͼ���ϵ�����
	 */
	private double[] coordinate;
	/**
	 * �������߼�������
	 */
	private int multiCir;
	/**
	 * ����˱�־
	 */
	private boolean isPlugin;
	/**
	 * �߼��˱�־
	 */
	private boolean isLogic;

	/**
	 * ���캯��
	 */
	public AreaPole() {
		super(basicPropertyName);
		this.coordinate = new double[] { Double.NaN, Double.NaN };
		this.multiCir = -1;
		this.isPlugin = false;
		this.isLogic = false;
	}

	/**
	 * ��������ĸ�������һ���µĸ�������
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
	 * ���ø���ͼ������
	 * 
	 * @return double[]
	 */
	public double[] getCoordinate() {
		return coordinate;
	}

	/**
	 * ��ȡ����X������
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
	 * ��ȡ����Y������
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
	 * ���ø���ͼ������
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
	 * ���ø���ͼ������
	 * 
	 * @param x:double:X������
	 * @param y:double:Y������
	 */
	public void setCoordinate(double x, double y) {
		if (this.coordinate == null) {
			this.coordinate = new double[] { Double.NaN, Double.NaN };
		}
		this.coordinate[0] = x;
		this.coordinate[1] = y;
	}

	/**
	 * ��ȡ�߼�����������
	 * 
	 * @return int
	 */
	public int getMultiCir() {
		return multiCir;
	}

	/**
	 * �����߼�����������
	 * 
	 * @param multiCir:int
	 */
	public void setMultiCir(int multiCir) {
		this.multiCir = multiCir;
	}

	/**
	 * ��ȡ����˱�־
	 * 
	 * @return boolean
	 */
	public boolean isPlugin() {
		return isPlugin;
	}

	/**
	 * ���ý���˱�־
	 * 
	 * @param isPlugin:boolean:����˱�־
	 */
	public void setPlugin(boolean isPlugin) {
		this.isPlugin = isPlugin;
	}

	/**
	 * ��ȡ�߼��˱�־
	 * 
	 * @return boolean
	 */
	public boolean isLogic() {
		return isLogic;
	}

	/**
	 * �����߼��˱�־
	 * 
	 * @param isLogic:boolean:�߼��˱�־
	 */
	public void setLogic(boolean isLogic) {
		this.isLogic = isLogic;
	}
}
