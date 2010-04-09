package com.nci.domino.shape;

import java.awt.geom.Point2D;

import com.nci.domino.shape.basic.WfTransitionBasic;

/**
 * 迁移线中心点对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfTransitionCenter {

	private Point2D centerPoint;

	private WfTransitionBasic trans;
	
	private int centerIndex;

	public WfTransitionCenter() {

	}

	public WfTransitionCenter(Point2D centerPoint, WfTransition trans) {
		this.centerPoint = centerPoint;
		this.trans = trans;
	}

	public Point2D getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Point2D centerPoint) {
		this.centerPoint = centerPoint;
	}

	public WfTransitionBasic getTrans() {
		return trans;
	}

	public void setTrans(WfTransitionBasic trans) {
		this.trans = trans;
	}

	public void clear() {
		centerPoint = null;
		trans = null;

	}

	public int getCenterIndex() {
		return centerIndex;
	}

	public void setCenterIndex(int centerIndex) {
		this.centerIndex = centerIndex;
	}

	public boolean isEmpty() {
		return centerPoint == null;
	}

}
