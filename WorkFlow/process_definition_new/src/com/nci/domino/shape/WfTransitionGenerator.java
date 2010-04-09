package com.nci.domino.shape;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoTransitionBean;
import com.nci.domino.help.Functions;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.WfActivityBasic;
import com.nci.domino.shape.basic.WfTransitionBasic;

/**
 * 迁移的反射对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfTransitionGenerator {

	/**
	 * 统一获取定义器下的迁移的对象，此方法不推荐改名
	 * 
	 * @param wofoBean
	 * @param board
	 * @return
	 */
	public static AbstractShape generateShapeFromWofoBean(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WofoTransitionBean transBean = (WofoTransitionBean) wofoBean;
		// 没有关联2个活动的迁移是无效迁移
		if (transBean.getPreviousActivityId() == null
				|| transBean.getPreviousActivityId().equals("")) {
			Logger.getLogger(WfTransitionGenerator.class.getName())
					.log(Level.INFO,
							"迁移" + transBean.getID() + "的prviousActivityID为空");
			return null;
		}
		if (transBean.getNextActivityId() == null
				|| transBean.getNextActivityId().equals("")) {
			Logger.getLogger(WfTransitionGenerator.class.getName()).log(
					Level.INFO, "迁移" + transBean.getID() + "的nextActivityID为空");
			return null;
		}
		WfActivityBasic prevActivity = (WfActivityBasic) board
				.getShapeById(transBean.getPreviousActivityId());
		WfActivityBasic nextActivity = (WfActivityBasic) board
				.getShapeById(transBean.getNextActivityId());
		WfTransitionBasic tra = new WfTransition(prevActivity, nextActivity);
		parse(tra, transBean);
		return tra;
	}

	/**
	 * 将迁移业务参数赋值到迁移图形中去
	 * 
	 * @param tra
	 * @param transBean
	 */
	private static void parse(WfTransitionBasic tra,
			WofoTransitionBean transBean) {
		tra.setTransitionBean(transBean);
		String wordPos = transBean.getWordsPos();
		double[] pos = new double[] { 0, 0 };
		if (wordPos != null && !wordPos.trim().equals("")) {
			String[] stringPos = wordPos.split(",");
			pos[0] = Double.valueOf(stringPos[0]);
			pos[1] = Double.valueOf(stringPos[1]);
		}
		tra.getTextShape().setValue(transBean.getTransitionName());
		// tra.getStringShape().x = pos[0];
		tra.getTextShape().setX(pos[0]);
		// tra.getStringShape().y = pos[1];
		tra.getTextShape().setY(pos[1]);
		String anchors = transBean.getAnchors();
		List<Point2D> cornerPoints = Functions.parseCornerPoints(anchors);
		tra.getCornerPoints().clear();
		tra.getCornerPoints().addAll(cornerPoints);
	}
}
