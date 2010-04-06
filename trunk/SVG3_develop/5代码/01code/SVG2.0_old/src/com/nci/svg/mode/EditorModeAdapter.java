package com.nci.svg.mode;

import java.util.HashSet;

import org.w3c.dom.Document;

import fr.itris.glips.svgeditor.Editor;

public abstract class EditorModeAdapter {

	protected Editor editor;

	protected String mode;

	protected boolean createOutlookPane;

	protected boolean showPopupMenu;

	protected boolean createJFileChooser;
	
	protected boolean showRuler;
	
	protected boolean showGrid;
	
	protected boolean showToolbar;
	
	protected boolean showMenubar;
	
	protected boolean internalFrameCloseable;
	
	protected HashSet<String> disabledProperties = new HashSet<String>();

	/**
	 * 编辑模式
	 */
	public static final String SVGTOOL_MODE_EDIT = "0";
	/**
	 * 能有部分操作的浏览模式
	 */
	public static final String SVGTOOL_MODE_VIEW_PARTVIEW = "1";
	/**
	 * 纯粹的浏览模式
	 */
	public static final String SVGTOOL_MODE_VIEW_ONLYVIEW = "2";

	/**
	 * 构造函数，构造具体的模式适配器
	 * 
	 * @param editor
	 *            Editor对象
	 * @param mode
	 *            模式类型
	 * @param createOutlookPane
	 *            是否需要创建图元面板
	 * @param showPopupMenu
	 *            是否需要能响应邮件菜单
	 */
	public EditorModeAdapter(Editor editor, String mode) {
		this.editor = editor;
		this.mode = mode;
	}

	/**
	 * 初始化模式，该方法必须要在所有模块生成之后才能调用
	 */
	public abstract void initMode();

	public abstract Document getModuleDocument();

	/**
	 * 获取svg编辑器模式
	 * 
	 * @return
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * 获取svg编辑器Editor对象
	 * 
	 * @return 当前Editor对象
	 */
	public Editor getEditor() {
		return editor;
	}

	/**
	 * 判断是否需要创建OutlookPane的图元面板
	 * 
	 * @return
	 */
	public boolean isOutlookPaneCreat() {
		return createOutlookPane;
	}

	/**
	 * 是否需要显示弹出菜单
	 * 
	 * @return
	 */
	public boolean isShowPopupMenu() {
		return showPopupMenu;
	}
	/**
	 * 是否需要创建JFileChooer，一般情况这在需要打开本地磁盘目录是才需要
	 * @return true为需要创建，false不需要
	 */
	public boolean isJFileChooserCreate() {
		return createJFileChooser;
	}
	/**
	 * 是否需要显示标尺
	 * @return true为需要创建，false不需要
	 */
	public boolean isRulerShown(){
		return showRuler;
	}
	/**
	 * 是否需要显示网格
	 * @return true为需要显示，false不需要
	 */
	public boolean isGridShown(){
		return showGrid;
	}
	
	/**
	 * 是否需要显示菜单栏
	 * @return true为需要显示，false不需要
	 */
	public boolean isMenubarShown(){
		return showMenubar;
	}
	
	/**
	 * 是否需要显示工具栏
	 * @return true为需要显示，false不需要
	 */
	public boolean isToolbarShown(){
		return showToolbar;
	}
	
	/**
	 * 内部窗口是否可关闭
	 * @return true为可关闭，false不可关闭
	 */
	public boolean isSVGFrameCloseable(){
		return internalFrameCloseable;
	}
	
	/**
	 * 判断哪个功能类型是被禁止的
	 * @param property 功能类型名称,都将转为小写名称去查询
	 * @return true为该功能被禁，false是该功能仍照旧
	 */
	public boolean hasDisabledProperty(String property){
		return disabledProperties.contains(property.toLowerCase());
	}

}
