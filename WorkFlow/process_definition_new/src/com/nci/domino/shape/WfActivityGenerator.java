package com.nci.domino.shape;

import com.nci.domino.GlobalConstants;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.shape.basic.AbstractShape;

/**
 * 活动对象的反射
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityGenerator {

	/**
	 * 统一获取监控定义器活动图对象，此方法不推荐改名
	 * 
	 * @param wofoBean
	 * @param board
	 * @return
	 */
	public static AbstractShape generateShapeFromWofoBean(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WofoActivityBean actBean = (WofoActivityBean) wofoBean;
		WfActivity act = new WfActivity(actBean.getPosX(), actBean.getPosY(),
				actBean.getActivityType());
		act.setWofoBean(actBean);
		act
				.setWidth(actBean.getWidth() <= 0 ? WfActivity.DEFAULT_CENTERSHAPE_WIDTH
						: actBean.getWidth());
		act
				.setHeight(actBean.getHeight() <= 0 ? WfActivity.DEFAULT_CENTERSHAPE_HEIGHT
						: actBean.getHeight());
		return act;
	}

}
