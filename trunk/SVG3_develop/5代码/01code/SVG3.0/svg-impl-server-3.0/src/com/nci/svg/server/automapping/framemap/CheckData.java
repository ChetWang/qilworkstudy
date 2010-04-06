package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * 标题：CheckData.java
 * </p>
 * <p>
 * 描述： 结构图数据检校
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
public class CheckData {
	/**
	 * 结构图数据
	 */
	private FrameNode data;
	/**
	 * 各层节点数
	 */
	private int[] layerNumbers;
	/**
	 * 竖排起始层
	 */
	private int hvFlg;

	/**
	 * 构造函数
	 * 
	 * @param data:FrameNode:结构图数据
	 */
	public CheckData(FrameNode data) {
		this.data = data;

		this.hvFlg = -1;
		this.layerNumbers = new int[20];
		for (int i = 0, size = layerNumbers.length; i < size; i++) {
			layerNumbers[i] = 0;
		}
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 结构图数据检查
	 * @return
	 */
	public AutoMapResultBean check() {
		// 检查各节点是否合法
		AutoMapResultBean result = checkNode(data);
		// 获取竖排起始层
		getHVFlag(data);
		// 设置节点样式类型
		setNodeModeType(data, 0);

		return result;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @功能 检查指定节点是否合法
	 * @param node
	 * @return
	 */
	private AutoMapResultBean checkNode(FrameNode node) {
		AutoMapResultBean result = new AutoMapResultBean();
		// ***************
		// 检查子节点是否合法
		// ***************
		ArrayList nodeList = node.getChildNodes();
		for (int i = 0, size = nodeList.size(); i < size; i++) {
			Object subObject = nodeList.get(i);
			if (subObject instanceof FrameNode) {
				FrameNode subNode = (FrameNode) subObject;
				result = checkNode(subNode);
				if (!result.isFlag()) {
					// 子节点数据非法
					return result;
				}
			} else {
				// 结构图数据中存在非节点数据
				result.setFlag(false);
				result.setErrMsg("结构图数据中存在非节点数据");
				return result;
			}
		}
		// **************************
		// 检查node对象中节点标识是否存在
		// **************************
		String id = node.getId();
		if (id == null || id.length() <= 0) {
			result.setFlag(false);
			result.setErrMsg("结构图数据中存在标识不存在节点！");
		}
		return result;
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @功能 检查和设置节点排列模式
	 * @param node
	 */
	private void setNodeModeType(FrameNode node, int layerIndex) {
		if (hvFlg <= 0)
			return;
		if (layerIndex >= hvFlg) {
			node.setModeType(FrameGlobal.VERTICAL_MODE);
		}
		layerIndex++;
		ArrayList children = node.getChildNodes();
		for (int i = 0, size = children.size(); i < size; i++) {
			FrameNode child = (FrameNode) children.get(i);
			setNodeModeType(child, layerIndex);
		}
	}

	private void getHVFlag(FrameNode node) {
		// 计算各层节点数
		getLayerNumber(node, 0);
		for (int i = 0, size = layerNumbers.length; i < size
				&& layerNumbers[i] > 0; i++) {
			if (layerNumbers[i] > FrameGlobal.H_V_FLAG) {
				hvFlg = i;
				break;
			}
		}
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @功能 获取节点各层节点数
	 * @param node
	 * @return
	 */
	private void getLayerNumber(FrameNode node, int layerIndex) {
		if (layerIndex == 0) {
			layerNumbers[0] = 1;
		}
		ArrayList children = node.getChildNodes();
		int number = children.size();
		layerNumbers[layerIndex + 1] += number;
		layerIndex++;
		for (int i = 0; i < number; i++) {
			FrameNode child = (FrameNode) children.get(i);
			getLayerNumber(child, layerIndex);
		}
	}
}
