package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * 标题：FrameNode.java
 * </p>
 * <p>
 * 描述：结构图节点信息类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-11
 * @version 1.0
 */
public class FrameNode {
	/**
	 * 节点标识
	 */
	private String id;
	/**
	 * 节点显性信息
	 */
	private String name;
	/**
	 * 节点隐性信息
	 */
	private String message;
	/**
	 * 节点超级链接地址
	 */
	private String url;
	/**
	 * 子节点集
	 */
	private ArrayList childNodes;
	/**
	 * 节点最右位置
	 */
	private double right;
	/**
	 * 节点最底位置
	 */
	private double buttom;
	/**
	 * 父节点
	 */
	private FrameNode parentNode;
	/**
	 * 节点坐标
	 */
	private Point coordinate;
	/**
	 * 父节点到本节点连线坐标
	 */
	private ArrayList points;
	/**
	 * 节点样式类型 0:横排，1:竖排
	 */
	private int modeType;

	/**
	 * 构造函数
	 */
	public FrameNode() {
		init("", "", "", "");
	}

	/**
	 * 构造函数
	 * 
	 * @param id:String:编号
	 * @param name:String:显示消息
	 * @param message:String:隐式消息
	 * @param url:String:超级连接
	 */
	public FrameNode(String id, String name, String message, String url) {
		init(id, name, message, url);
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 初始化函数
	 * @param id:String:编号
	 * @param name:String:显示消息
	 * @param message:String:隐式消息
	 * @param url:String:超级连接
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
	 * @功能 获取节点标识
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 设置节点标识
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 获取显性信息
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 设置显性信息
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 获取隐性信息
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 设置隐性信息
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 获取节点超级链接
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 设置节点超级链接
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 获取子节点集
	 * @return
	 */
	public ArrayList getChildNodes() {
		return childNodes;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 获取指定编号的节点
	 * @param id:String:节点编号
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
	 * @功能 删除指定编号节点
	 * @param id:String:节点编号
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
	 * @功能 增加子节点
	 * @param child:FrameNode:子节点
	 * @return
	 */
	public boolean appendChildNode(FrameNode child) {
		// 检查是否有重名的节点
		for (int i = 0, size = childNodes.size(); i < size; i++) {
			FrameNode node = (FrameNode) childNodes.get(i);
			if (node.getId().equals(child.getId())) {
				return false;
			}
		}
		// 增加节点
		child.setParentNode(this);
		childNodes.add(child);
		return true;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 获取父节点
	 * @return
	 */
	public FrameNode getParentNode() {
		return parentNode;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 设置父节点
	 * @param parentNode
	 */
	public void setParentNode(FrameNode parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * 设置节点坐标
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
	 * 设置节点坐标
	 * 
	 * @param x:double:X轴坐标
	 * @param y:double:Y轴坐标
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
	 * @功能 获取节点最右点位置
	 * @return
	 */
	public double getRight() {
		return right;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @功能 设置节点最右点位置
	 * @param right
	 */
	public void setRight(double right) {
		this.right = right;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 获取节点最底部位置
	 * @return
	 */
	public double getButtom() {
		return buttom;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 设置节点最底部位置
	 * @param buttom
	 */
	public void setButtom(double buttom) {
		this.buttom = buttom;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 获取节点坐标
	 * @return
	 */
	public Point getCoordinate() {
		return coordinate;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 获取连线坐标
	 * @return
	 */
	public ArrayList getPoints() {
		return points;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 添加连线坐标
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
	 * @功能 节点坐标平移
	 * @param x:double:X轴平移量
	 * @param y:double:Y轴平移量
	 */
	public void move(double x, double y) {
		// 修改节点坐标
		this.coordinate.movePoint(x, y);
		// 修改最右点位置
		this.right += x;
		// 修改最底部位置
		this.buttom += y;
		// 修改连线坐标
		for (int i = 0, size = points.size(); i < size; i++) {
			Point point = (Point) points.get(i);
			point.movePoint(x, y);
		}
		// 修改子节点坐标
		for (int i = 0, size = childNodes.size(); i < size; i++) {
			FrameNode child = (FrameNode) childNodes.get(i);
			child.move(x, y);
		}
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @功能 获取节点样式类型
	 * @return
	 */
	public int getModeType() {
		return modeType;
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @功能 设置节点样式类型
	 * @param modeType
	 */
	public void setModeType(int modeType) {
		this.modeType = modeType;
	}
}
