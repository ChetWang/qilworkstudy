package com.nci.domino.shape.basic;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;

/**
 * ��עͼ�η������
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
