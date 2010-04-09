package com.nci.domino.edit;

import com.nci.domino.PaintBoard;
import com.nci.domino.WfEditor;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.concurrent.WfStartupEndException;
import com.nci.domino.help.Functions;

/**
 * 模式管理器
 * 
 * @author Qil.Wong
 * 
 */
public class ModeManager {

	private String activityType = null;

	private String pipeType = null;

	private int toolMode = ToolMode.TOOL_SELECT_OR_DRAG;

	// 重复操作
	private boolean remanentOperation = false;

	WfEditor editor;

	public ModeManager(final WfEditor editor) {
		this.editor = editor;
		WfRunnable run = new WfRunnable("") {
			public void run() {
				remanentOperation = editor.getToolBar().getButtons().get(
						"remanent_oper").isSelected();
			}
		};
		try {
			editor.getBackgroundManager().enqueueStartupQueue(run);
		} catch (WfStartupEndException e) {
			e.printStackTrace();
		}
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public int getToolMode() {
		return toolMode;
	}

	public void setToolMode(int toolMode) {
		this.toolMode = toolMode;
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		if (board != null) {
			board.setCursor(Functions.getCursor(toolMode));
		}
	}

	/**
	 * 是否运行当前模式重复操作
	 * 
	 * @return
	 */
	public boolean isRemanentOperation() {
		return remanentOperation;
	}

	public void setRemanentOperation(boolean remanentOperation) {
		this.remanentOperation = remanentOperation;
	}

	public String getPipeType() {
		return pipeType;
	}

	public void setPipeType(String pipeType) {
		this.pipeType = pipeType;
	}

}
