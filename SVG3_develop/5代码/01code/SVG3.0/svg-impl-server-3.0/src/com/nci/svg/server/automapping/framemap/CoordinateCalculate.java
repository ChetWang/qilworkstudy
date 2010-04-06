package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * ���⣺CoordinateCalculate.java
 * </p>
 * <p>
 * ������ ��������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-10
 * @version 1.0
 */
public class CoordinateCalculate {
	/**
	 * �ṹͼ����
	 */
	private FrameNode data;

	/**
	 * ���캯��
	 * 
	 * @param data:FrameNode:�ṹͼ����
	 */
	public CoordinateCalculate(FrameNode data) {
		this.data = data;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @���� ����ṹͼ���ڵ�λ��
	 * @return
	 */
	public AutoMapResultBean calculate() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		// **************
		// ��������ڵ�����
		// **************
		ArrayList children = data.getChildNodes();
		data = combinBrother(children);
		// ***********
		// ������������
		// ***********
		calculateLines(data);

		// ************************
		// �ڵ�����ƽ�������ڵ㸺������
		// ************************
		double dy = data.getCoordinate().getY();
		data.move(0, Math.abs(dy));

		// ***************
		// ��ͼ���Ϸ�Ԥ���հ�
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
	 * @���� �ϲ��ֵܽڵ�
	 * @param nodes:ArrayList:�ڵ㼯
	 * @return
	 */
	private FrameNode combinBrother(ArrayList nodes) {
		// ��ȡ��һ���ڵ�
		FrameNode firNode = (FrameNode) nodes.get(0);
		// �ݹ��һ���ڵ�
		ArrayList firChildNodes = firNode.getChildNodes();
		if (firChildNodes.size() > 0) {
			combinBrother(firChildNodes);
		}
		// ��ȡ���ڵ�
		FrameNode parNode = firNode.getParentNode();
		// ��ǰ���ҵ�λ��
		double tempRight = firNode.getRight();
		// ���ڵ�X��λ��
		// double parX = firNode.getCoordinate().getX();
		// ѭ���ϲ��ֵܽڵ�
		for (int i = 1, size = nodes.size(); i < size; i++) {
			FrameNode child = (FrameNode) nodes.get(i);
			// �ݹ��ӽڵ�ϲ�
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
			// // ���һ���ڵ�
			// parX = (parX + child.getCoordinate().getX()) / 2;
			// }
		}
		// ��������ڵ�λ��
		double childY = firNode.getCoordinate().getY()
				- FrameGlobal.VERTICAL_INTERVAL;
		if (parNode.getModeType() == FrameGlobal.HORIZONTAL_MODE)
			childY -= FrameGlobal.HORIZONTAL_NODE_HEIGHT;
		else
			childY -= FrameGlobal.VERTICAL_NODE_HEIGHT;

		double parX = tempRight / 2;
		// ���ø��ڵ�λ��
		parNode.setCoordinate(parX, childY);
		// ���ø��ڵ����ҵ�λ��
		parNode.setRight(tempRight);
		// ************************
		// ���ڵ�����ƽ�������ڵ㸺������
		// ************************
		double dy = parNode.getCoordinate().getY();
		parNode.move(0, Math.abs(dy));

		return parNode;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @���� ����ָ���ڵ㵽���ӽڵ�����ߵ�
	 * @param node:FrameNode:�ṹͼ���ݽڵ�
	 */
	private void calculateLines(FrameNode node) {
		ArrayList children = node.getChildNodes();
		for (int i = 0, size = children.size(); i < size; i++) {
			FrameNode child = (FrameNode) children.get(i);
			// �����ӽڵ㵽���ڵ�����ߵ�
			calculateLinePoints(child);
			// �ݹ��ӽڵ�
			calculateLines(child);
		}
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @���� ����ָ���ڵ㵽�丸�ڵ����ߵ�
	 * @param node
	 */
	private void calculateLinePoints(FrameNode node) {
		// ��ȡ���ڵ�
		FrameNode parNode = node.getParentNode();
		// ��ȡ���ڵ�����
		Point parPoint = parNode.getCoordinate();
		// ��ȡ���ڵ���ʽ
		int parMode = parNode.getModeType();
		// ��ȡ���ڵ�����
		Point selfPoint = node.getCoordinate();
		// ��ȡ���ڵ���ʽ
		int selfMode = node.getModeType();

		// �������������Ҫ���ڵ㱾���С��������
		// ��һ��������
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
		// �ڶ�������
		y += 0.5 * FrameGlobal.VERTICAL_INTERVAL;
		Point second = new Point(x, y);
		// ���һ������
		x = selfPoint.getX();
		y = selfPoint.getY();
		if(selfMode == FrameGlobal.HORIZONTAL_MODE){
//			x += 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH;
		}else{
//			x += 0.5 * FrameGlobal.VERTICAL_NODE_WIDTH;
		}
		Point last = new Point(x, y);
		// ����������
		Point third = new Point(last.getX(), second.getY());

		// ���ĸ���������ڵ�������������
		node.appendPoints(first);
		node.appendPoints(second);
		node.appendPoints(third);
		node.appendPoints(last);
	}
}
