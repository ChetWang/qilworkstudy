package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * 标题：CoordinateCalculate.java
 * </p>
 * <p>
 * 描述： 坐标推算
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-10
 * @version 1.0
 */
public class CoordinateCalculate {
	/**
	 * 结构图数据
	 */
	private FrameNode data;

	/**
	 * 构造函数
	 * 
	 * @param data:FrameNode:结构图数据
	 */
	public CoordinateCalculate(FrameNode data) {
		this.data = data;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @功能 计算结构图各节点位置
	 * @return
	 */
	public AutoMapResultBean calculate() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		// **************
		// 初步计算节点坐标
		// **************
		ArrayList children = data.getChildNodes();
		data = combinBrother(children);
		// ***********
		// 计算连线坐标
		// ***********
		calculateLines(data);

		// ************************
		// 节点整体平移消除节点负数坐标
		// ************************
		double dy = data.getCoordinate().getY();
		data.move(0, Math.abs(dy));

		// ***************
		// 在图形上方预留空白
		// ***************
		double dx = 0;
		if (data.getRight() <= FrameGlobal.MAP_WIDTH) {
			dx = (FrameGlobal.MAP_WIDTH - data.getRight()) / 2;
		}
		data.move(dx, FrameGlobal.MAP_MARGIN);

		resultBean.setMsg(data);
		return resultBean;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @功能 合并兄弟节点
	 * @param nodes:ArrayList:节点集
	 * @return
	 */
	private FrameNode combinBrother(ArrayList nodes) {
		// 获取第一个节点
		FrameNode firNode = (FrameNode) nodes.get(0);
		// 递归第一个节点
		ArrayList firChildNodes = firNode.getChildNodes();
		if (firChildNodes.size() > 0) {
			combinBrother(firChildNodes);
		}
		// 获取父节点
		FrameNode parNode = firNode.getParentNode();
		// 当前最右点位置
		double tempRight = firNode.getRight();
		// 父节点X轴位置
		// double parX = firNode.getCoordinate().getX();
		// 循环合并兄弟节点
		for (int i = 1, size = nodes.size(); i < size; i++) {
			FrameNode child = (FrameNode) nodes.get(i);
			// 递归子节点合并
			ArrayList childNodes = child.getChildNodes();
			if (childNodes.size() > 0) {
				combinBrother(childNodes);
			}

			double dx = tempRight + FrameGlobal.HORIZONTAL_INTERVAL;
			if (child.getModeType() == FrameGlobal.HORIZONTAL_MODE)
				dx += FrameGlobal.HORIZONTAL_NODE_WIDTH;
			else
				dx += FrameGlobal.VERTICAL_NODE_WIDTH;

			double dy = 0;
			child.move(dx, dy);
			tempRight = child.getRight();
			// if (i == size - 1) {
			// // 最后一个节点
			// parX = (parX + child.getCoordinate().getX()) / 2;
			// }
		}
		// 计算出父节点位置
		double childY = firNode.getCoordinate().getY()
				- FrameGlobal.VERTICAL_INTERVAL;
		if (parNode.getModeType() == FrameGlobal.HORIZONTAL_MODE)
			childY -= FrameGlobal.HORIZONTAL_NODE_HEIGHT;
		else
			childY -= FrameGlobal.VERTICAL_NODE_HEIGHT;

		double parX = tempRight / 2;
		// 设置父节点位置
		parNode.setCoordinate(parX, childY);
		// 设置父节点最右点位置
		parNode.setRight(tempRight);
		// ************************
		// 父节点整体平移消除节点负数坐标
		// ************************
		double dy = parNode.getCoordinate().getY();
		parNode.move(0, Math.abs(dy));

		return parNode;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @功能 计算指定节点到其子节点的连线点
	 * @param node:FrameNode:结构图数据节点
	 */
	private void calculateLines(FrameNode node) {
		ArrayList children = node.getChildNodes();
		for (int i = 0, size = children.size(); i < size; i++) {
			FrameNode child = (FrameNode) children.get(i);
			// 计算子节点到本节点间连线点
			calculateLinePoints(child);
			// 递归子节点
			calculateLines(child);
		}
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @功能 计算指定节点到其父节点连线点
	 * @param node
	 */
	private void calculateLinePoints(FrameNode node) {
		// 获取父节点
		FrameNode parNode = node.getParentNode();
		// 获取父节点坐标
		Point parPoint = parNode.getCoordinate();
		// 获取父节点样式
		int parMode = parNode.getModeType();
		// 获取本节点坐标
		Point selfPoint = node.getCoordinate();
		// 获取本节点样式
		int selfMode = node.getModeType();

		// 各点坐标计算需要将节点本身大小考虑在内
		// 第一个点坐标
		double x = parPoint.getX();
		double y = parPoint.getY();
		if (parMode == FrameGlobal.HORIZONTAL_MODE) {
//			x += 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH;
			y += FrameGlobal.HORIZONTAL_NODE_HEIGHT;
		} else {
//			x += 0.5 * FrameGlobal.VERTICAL_NODE_WIDTH;
			y += FrameGlobal.VERTICAL_NODE_HEIGHT;
		}
		Point first = new Point(x, y);
		// 第二点坐标
		y += 0.5 * FrameGlobal.VERTICAL_INTERVAL;
		Point second = new Point(x, y);
		// 最后一点坐标
		x = selfPoint.getX();
		y = selfPoint.getY();
		if(selfMode == FrameGlobal.HORIZONTAL_MODE){
//			x += 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH;
		}else{
//			x += 0.5 * FrameGlobal.VERTICAL_NODE_WIDTH;
		}
		Point last = new Point(x, y);
		// 第三点坐标
		Point third = new Point(last.getX(), second.getY());

		// 将四个坐标填入节点连接线坐标中
		node.appendPoints(first);
		node.appendPoints(second);
		node.appendPoints(third);
		node.appendPoints(last);
	}
}
