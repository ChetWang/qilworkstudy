package com.nci.domino.shape.basic;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.plugin.pipe.WofoPipeBaseBean;

/**
 * 阶段泳道对象反射对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfPipeShapeBasicGenerator {

	/**
	 * 监控状态下泳道业务对象对应的图形对象生成器，不推荐修改方法名
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
