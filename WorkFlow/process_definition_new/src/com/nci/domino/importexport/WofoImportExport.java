package com.nci.domino.importexport;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;

import com.nci.domino.PaintBoard;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoProcessBean;

public class WofoImportExport extends WfFileExport {

	public WofoImportExport(WfEditor editor, String acceptExtention,
			String description) {
		super(editor, acceptExtention, description);
	}

	@Override
	public void export(String fileName) {
		Map<String, PaintBoard> boardMap = editor.getOperationArea()
				.getBoards();
		Iterator<PaintBoard> boards = boardMap.values().iterator();
		while (boards.hasNext()) {
			boards.next().preSave();
		}
		DefaultTreeModel model = editor.getOperationArea().getWfTree()
				.getWfTreeModel();
		editor.saveLocal(fileName, (Serializable) model.getRoot());
	}

	public void importFromLocal(String fileName) {
		editor.importFromLocal(fileName);
	}

}
