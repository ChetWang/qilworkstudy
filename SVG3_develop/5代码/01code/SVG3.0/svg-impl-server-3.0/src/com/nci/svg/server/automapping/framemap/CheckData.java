package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * ���⣺CheckData.java
 * </p>
 * <p>
 * ������ �ṹͼ���ݼ�У
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
public class CheckData {
	/**
	 * �ṹͼ����
	 */
	private FrameNode data;
	/**
	 * ����ڵ���
	 */
	private int[] layerNumbers;
	/**
	 * ������ʼ��
	 */
	private int hvFlg;

	/**
	 * ���캯��
	 * 
	 * @param data:FrameNode:�ṹͼ����
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
	 * @���� �ṹͼ���ݼ��
	 * @return
	 */
	public AutoMapResultBean check() {
		// �����ڵ��Ƿ�Ϸ�
		AutoMapResultBean result = checkNode(data);
		// ��ȡ������ʼ��
		getHVFlag(data);
		// ���ýڵ���ʽ����
		setNodeModeType(data, 0);

		return result;
	}

	/**
	 * 2009-6-11 Add by ZHM
	 * 
	 * @���� ���ָ���ڵ��Ƿ�Ϸ�
	 * @param node
	 * @return
	 */
	private AutoMapResultBean checkNode(FrameNode node) {
		AutoMapResultBean result = new AutoMapResultBean();
		// ***************
		// ����ӽڵ��Ƿ�Ϸ�
		// ***************
		ArrayList nodeList = node.getChildNodes();
		for (int i = 0, size = nodeList.size(); i < size; i++) {
			Object subObject = nodeList.get(i);
			if (subObject instanceof FrameNode) {
				FrameNode subNode = (FrameNode) subObject;
				result = checkNode(subNode);
				if (!result.isFlag()) {
					// �ӽڵ����ݷǷ�
					return result;
				}
			} else {
				// �ṹͼ�����д��ڷǽڵ�����
				result.setFlag(false);
				result.setErrMsg("�ṹͼ�����д��ڷǽڵ�����");
				return result;
			}
		}
		// **************************
		// ���node�����нڵ��ʶ�Ƿ����
		// **************************
		String id = node.getId();
		if (id == null || id.length() <= 0) {
			result.setFlag(false);
			result.setErrMsg("�ṹͼ�����д��ڱ�ʶ�����ڽڵ㣡");
		}
		return result;
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @���� �������ýڵ�����ģʽ
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
		// �������ڵ���
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
	 * @���� ��ȡ�ڵ����ڵ���
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
