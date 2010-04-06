package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * ���⣺FrameNode.java
 * </p>
 * <p>
 * �������ṹͼ�ڵ���Ϣ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-11
 * @version 1.0
 */
public class FrameNode {
	/**
	 * �ڵ��ʶ
	 */
	private String id;
	/**
	 * �ڵ�������Ϣ
	 */
	private String name;
	/**
	 * �ڵ�������Ϣ
	 */
	private String message;
	/**
	 * �ڵ㳬�����ӵ�ַ
	 */
	private String url;
	/**
	 * �ӽڵ㼯
	 */
	private ArrayList childNodes;
	/**
	 * �ڵ�����λ��
	 */
	private double right;
	/**
	 * �ڵ����λ��
	 */
	private double buttom;
	/**
	 * ���ڵ�
	 */
	private FrameNode parentNode;
	/**
	 * �ڵ�����
	 */
	private Point coordinate;
	/**
	 * ���ڵ㵽���ڵ���������
	 */
	private ArrayList points;
	/**
	 * �ڵ���ʽ���� 0:���ţ�1:����
	 */
	private int modeType;

	/**
	 * ���캯��
	 */
	public FrameNode() {
		init("", "", "", "");
	}

	/**
	 * ���캯��
	 * 
	 * @param id:String:���
	 * @param name:String:��ʾ��Ϣ
	 * @param message:String:��ʽ��Ϣ
	 * @param url:String:��������
	 */
	public FrameNode(String id, String name, String message, String url) {
		init(id, name, message, url);
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ��ʼ������
	 * @param id:String:���
	 * @param name:String:��ʾ��Ϣ
	 * @param message:String:��ʽ��Ϣ
	 * @param url:String:��������
	 */
	private void init(String id, String name, String message, String url) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.url = url;
		this.childNodes = new ArrayList();
		this.parentNode = null;
		this.points = new ArrayList();
		this.coordinate = new Point();
		this.right = 0;
		this.buttom = 0;
		this.modeType = FrameGlobal.HORIZONTAL_MODE;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ��ʶ
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ���ýڵ��ʶ
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ��ȡ������Ϣ
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ����������Ϣ
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ��ȡ������Ϣ
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ����������Ϣ
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ㳬������
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ���ýڵ㳬������
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ��ȡ�ӽڵ㼯
	 * @return
	 */
	public ArrayList getChildNodes() {
		return childNodes;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ��ȡָ����ŵĽڵ�
	 * @param id:String:�ڵ���
	 * @return
	 */
	public FrameNode getChildNode(String id) {
		FrameNode node = null;
		for (int i = 0, size = childNodes.size(); i < size; i++) {
			FrameNode child = (FrameNode) childNodes.get(i);
			if (id.equals(child.getId())) {
				node = child;
				break;
			}
		}
		return node;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ɾ��ָ����Žڵ�
	 * @param id:String:�ڵ���
	 * @return
	 */
	public boolean removeChildNode(String id) {
		for (int i = 0, size = childNodes.size(); i < size; i++) {
			FrameNode child = (FrameNode) childNodes.get(i);
			if (id.equals(child.getId())) {
				childNodes.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� �����ӽڵ�
	 * @param child:FrameNode:�ӽڵ�
	 * @return
	 */
	public boolean appendChildNode(FrameNode child) {
		// ����Ƿ��������Ľڵ�
		for (int i = 0, size = childNodes.size(); i < size; i++) {
			FrameNode node = (FrameNode) childNodes.get(i);
			if (node.getId().equals(child.getId())) {
				return false;
			}
		}
		// ���ӽڵ�
		child.setParentNode(this);
		childNodes.add(child);
		return true;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ��ȡ���ڵ�
	 * @return
	 */
	public FrameNode getParentNode() {
		return parentNode;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ���ø��ڵ�
	 * @param parentNode
	 */
	public void setParentNode(FrameNode parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * ���ýڵ�����
	 * 
	 * @param coordinate:double[]
	 */
	public void setCoordinate(double[] coordinate) {
		this.coordinate = new Point();
		if (coordinate.length >= 2) {
			this.coordinate.setX(coordinate[0]);
			this.coordinate.setY(coordinate[1]);
		}
	}

	/**
	 * ���ýڵ�����
	 * 
	 * @param x:double:X������
	 * @param y:double:Y������
	 */
	public void setCoordinate(double x, double y) {
		if (this.coordinate == null) {
			this.coordinate = new Point();
		}
		this.coordinate.setX(x);
		this.coordinate.setY(y);
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ����ҵ�λ��
	 * @return
	 */
	public double getRight() {
		return right;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @���� ���ýڵ����ҵ�λ��
	 * @param right
	 */
	public void setRight(double right) {
		this.right = right;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ���ײ�λ��
	 * @return
	 */
	public double getButtom() {
		return buttom;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ���ýڵ���ײ�λ��
	 * @param buttom
	 */
	public void setButtom(double buttom) {
		this.buttom = buttom;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ�����
	 * @return
	 */
	public Point getCoordinate() {
		return coordinate;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ��ȡ��������
	 * @return
	 */
	public ArrayList getPoints() {
		return points;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� �����������
	 * @param point
	 * @return
	 */
	public boolean appendPoints(Point point) {
		if (point != null) {
			Point p = new Point(point);
			this.points.add(p);
		}
		return false;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� �ڵ�����ƽ��
	 * @param x:double:X��ƽ����
	 * @param y:double:Y��ƽ����
	 */
	public void move(double x, double y) {
		// �޸Ľڵ�����
		this.coordinate.movePoint(x, y);
		// �޸����ҵ�λ��
		this.right += x;
		// �޸���ײ�λ��
		this.buttom += y;
		// �޸���������
		for (int i = 0, size = points.size(); i < size; i++) {
			Point point = (Point) points.get(i);
			point.movePoint(x, y);
		}
		// �޸��ӽڵ�����
		for (int i = 0, size = childNodes.size(); i < size; i++) {
			FrameNode child = (FrameNode) childNodes.get(i);
			child.move(x, y);
		}
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @���� ��ȡ�ڵ���ʽ����
	 * @return
	 */
	public int getModeType() {
		return modeType;
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @���� ���ýڵ���ʽ����
	 * @param modeType
	 */
	public void setModeType(int modeType) {
		this.modeType = modeType;
	}
}
