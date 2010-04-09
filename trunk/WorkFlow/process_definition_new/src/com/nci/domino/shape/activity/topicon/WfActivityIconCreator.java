package com.nci.domino.shape.activity.topicon;

import java.awt.Graphics2D;

import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * ����Ͻ�ͼ�����ɽӿ�
 * 
 * @author Qil.Wong
 * 
 */
public interface WfActivityIconCreator {

	/**
	 * ����ͼ��
	 * 
	 * @param g
	 *            ԴPaindboard�����Graphics2D
	 * @param activity
	 *            ָ���Ļ����
	 */
	public void paint(Graphics2D g, WfActivityBasic activity);
}
