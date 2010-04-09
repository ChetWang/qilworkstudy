package com.nci.domino.shape.note;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.shape.basic.AbstractShape;

/**
 * 备注图形反射对象
 * 
 * @author Qil.Wong
 * 
 * 
 */
public class WfNoteShapeGenerator {

	public static AbstractShape generateShapeFromWofoBean(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WfNoteShape noteShape = new WfNoteShape();
		
		noteShape.setWofoBean(wofoBean);
		return noteShape;
	}

}
