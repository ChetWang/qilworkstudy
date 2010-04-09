package com.nci.domino.shape.basic;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;

/**
 * 备注图形反射对象
 * 
 * @author Qil.Wong
 * 
 * 
 */
public class WfNoteShapeBasicGenerator {

	public static AbstractShape generateShapeFromWofoBeanBasic(
			WofoBaseBean wofoBean, PaintBoardBasic board) {
		WfNoteShapeBasic noteShape = new WfNoteShapeBasic();
		noteShape.setWofoBean(wofoBean);
		return noteShape;
	}

}
