package com.nci.domino.shape.basic;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;

/**
 * �����ķ���
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityBasicGenerator {

	/**
	 * ͳһ��ȡ���״̬�»ͼ���󣬴˷������Ƽ�����
	 * 
	 * @param wofoBean
	 * @param board
	 * @return
	 */
	public static AbstractShape generateShapeFromWofoBeanBasic(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WofoActivityBean actBean = (WofoActivityBean) wofoBean;
		WfActivityBasic act = new WfActivityBasic(actBean.getPosX(), actBean
				.getPosY(), actBean.getActivityType());
		act.setWofoBean(actBean);
		return act;
	}

}
