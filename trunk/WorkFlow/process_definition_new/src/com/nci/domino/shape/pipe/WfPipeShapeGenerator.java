package com.nci.domino.shape.pipe;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.plugin.pipe.WofoPipeBaseBean;
import com.nci.domino.shape.basic.AbstractShape;

/**
 * 阶段泳道对象反射对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfPipeShapeGenerator {

	/**
	 * 定义器节点泳道业务对象与图形对象映射生成器
	 * 
	 * @param wofoBean
	 * @param board
	 * @return
	 */
	public static AbstractShape generateShapeFromWofoBean(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WofoPipeBaseBean basePipeBean = (WofoPipeBaseBean) wofoBean;
		WfPipeShape p = new WfPipeShape(basePipeBean.getIndex(), basePipeBean
				.getFormerLength(), basePipeBean.isVertical());
		p.setHeight(basePipeBean.getHeight());
		p.setWidth(basePipeBean.getWidth());
		p.setWofoBean(basePipeBean);
		return p;
	}

}
