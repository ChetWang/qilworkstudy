package com.nci.domino.shape.basic;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;

/**
 * 活动对象的反射
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityBasicGenerator {

	/**
	 * 统一获取监控状态下活动图对象，此方法不推荐改名
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
