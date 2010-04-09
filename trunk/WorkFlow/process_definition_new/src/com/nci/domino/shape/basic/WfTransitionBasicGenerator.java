package com.nci.domino.shape.basic;

import java.awt.geom.Point2D;
import java.util.List;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoTransitionBean;
import com.nci.domino.help.Functions;

/**
 * 迁移的反射对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfTransitionBasicGenerator {

	/**
	 * 统一获取监控下的迁移的对象，此方法不推荐改名
	 * @param wofoBean
	 * @param board
	 * @return
	 */
	public static AbstractShape generateShapeFromWofoBeanBasic(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WofoTransitionBean transBean = (WofoTransitionBean) wofoBean;
		WfActivityBasic prevActivity = (WfActivityBasic) board
				.getShapeById(transBean.getPreviousActivityId());
		WfActivityBasic nextActivity = (WfActivityBasic) board
				.getShapeById(transBean.getNextActivityId());
		WfTransitionBasic tra = new WfTransitionBasic(prevActivity,
				nextActivity);
		parse(tra, transBean);
		return tra;
	}

	/**
	 * 将迁移业务参数赋值到迁移图形中去
	 * @param tra
	 * @param transBean
	 */
	private static void parse(WfTransitionBasic tra,
			WofoTransitionBean transBean) {
		tra.setWofoBean(transBean);
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
