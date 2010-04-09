package com.nci.domino.edit;

/**
 * 工具条动作模式
 * 
 * @author Qil.Wong
 * 
 */
public interface ToolMode {
	/**
	 * 选择或拖放模式
	 */
	public static final int TOOL_SELECT_OR_DRAG = 0;
	/**
	 * 绘活动模式
	 */
	public static final int TOOL_DRAW_ACTIVITY = 1;
	/**
	 * 绘连接线模式
	 */
	public static final int TOOL_LINKLINE = 2;
	/**
	 * 平移模式
	 */
	public static final int TOOL_TRANSLATE = 3;
	/**
	 * 管道模式
	 */
	public static final int Tool_DRAW_PIPE = 4;

	/**
	 * 绘制备注模式
	 */
	public static final int Tool_DRAW_NOTES = 5;

	/**
	 * 监控模式
	 */
	public static final int TOOL_VIEW = 6;

}
