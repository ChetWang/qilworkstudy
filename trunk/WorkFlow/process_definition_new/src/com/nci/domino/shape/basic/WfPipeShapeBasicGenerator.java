package com.nci.domino.shape.basic;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.plugin.pipe.WofoPipeBaseBean;

/**
 * �׶�Ӿ�����������
 * 
 * @author Qil.Wong
 * 
 */
public class WfPipeShapeBasicGenerator {

	/**
	 * ���״̬��Ӿ��ҵ������Ӧ��ͼ�ζ��������������Ƽ��޸ķ�����
	 * 
	 * @param wofoBean
	 * @param board
	 * @return
	 */
	public static AbstractShape generateShapeFromWofoBeanBasic(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WofoPipeBaseBean basePipeBean = (WofoPipeBaseBean) wofoBean;
		WfPipeShapeBasic p = new WfPipeShapeBasic(basePipeBean.getIndex(),
				basePipeBean.getFormerLength(), basePipeBean.isVertical());
		p.setHeight(basePipeBean.getHeight());
		p.setWidth(basePipeBean.getWidth());
		p.setWofoBean(basePipeBean);
		return p;
	}

}
