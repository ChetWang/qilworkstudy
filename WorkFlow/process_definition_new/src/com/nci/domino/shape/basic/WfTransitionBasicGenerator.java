package com.nci.domino.shape.basic;

import java.awt.geom.Point2D;
import java.util.List;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoTransitionBean;
import com.nci.domino.help.Functions;

/**
 * Ǩ�Ƶķ������
 * 
 * @author Qil.Wong
 * 
 */
public class WfTransitionBasicGenerator {

	/**
	 * ͳһ��ȡ����µ�Ǩ�ƵĶ��󣬴˷������Ƽ�����
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
	 * ��Ǩ��ҵ�������ֵ��Ǩ��ͼ����ȥ
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
