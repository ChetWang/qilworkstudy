package com.nci.domino.importexport;

import com.nci.domino.PaintBoard;
import com.nci.domino.WfEditor;

/**
 * �������̵ĵ��뵼��
 * @author Qil.Wong
 *
 */
public class WofoProcessImportExport extends WfFileExport {

	public WofoProcessImportExport(WfEditor editor, String acceptExtention,
			String description) {
		super(editor, acceptExtention, description);
	}

	@Override
	public void export(String fileName) {
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		if (board != null) {
			board.preSave();
		}
		editor.saveLocal(fileName, editor.getOperationArea().getWfTree()
				.getSelectedNode());
	}

	/**
	 * ���뵥����������
	 * @param fileName
	 */
	public void importFromLocal(String fileName) {
		//FIXME ���뵥����������
	}

}
