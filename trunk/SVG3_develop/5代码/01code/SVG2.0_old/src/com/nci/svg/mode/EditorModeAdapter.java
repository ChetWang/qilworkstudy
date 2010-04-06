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
	 * �༭ģʽ
	 */
	public static final String SVGTOOL_MODE_EDIT = "0";
	/**
	 * ���в��ֲ��������ģʽ
	 */
	public static final String SVGTOOL_MODE_VIEW_PARTVIEW = "1";
	/**
	 * ��������ģʽ
	 */
	public static final String SVGTOOL_MODE_VIEW_ONLYVIEW = "2";

	/**
	 * ���캯������������ģʽ������
	 * 
	 * @param editor
	 *            Editor����
	 * @param mode
	 *            ģʽ����
	 * @param createOutlookPane
	 *            �Ƿ���Ҫ����ͼԪ���
	 * @param showPopupMenu
	 *            �Ƿ���Ҫ����Ӧ�ʼ��˵�
	 */
	public EditorModeAdapter(Editor editor, String mode) {
		this.editor = editor;
		this.mode = mode;
	}

	/**
	 * ��ʼ��ģʽ���÷�������Ҫ������ģ������֮����ܵ���
	 */
	public abstract void initMode();

	public abstract Document getModuleDocument();

	/**
	 * ��ȡsvg�༭��ģʽ
	 * 
	 * @return
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * ��ȡsvg�༭��Editor����
	 * 
	 * @return ��ǰEditor����
	 */
	public Editor getEditor() {
		return editor;
	}

	/**
	 * �ж��Ƿ���Ҫ����OutlookPane��ͼԪ���
	 * 
	 * @return
	 */
	public boolean isOutlookPaneCreat() {
		return createOutlookPane;
	}

	/**
	 * �Ƿ���Ҫ��ʾ�����˵�
	 * 
	 * @return
	 */
	public boolean isShowPopupMenu() {
		return showPopupMenu;
	}
	/**
	 * �Ƿ���Ҫ����JFileChooer��һ�����������Ҫ�򿪱��ش���Ŀ¼�ǲ���Ҫ
	 * @return trueΪ��Ҫ������false����Ҫ
	 */
	public boolean isJFileChooserCreate() {
		return createJFileChooser;
	}
	/**
	 * �Ƿ���Ҫ��ʾ���
	 * @return trueΪ��Ҫ������false����Ҫ
	 */
	public boolean isRulerShown(){
		return showRuler;
	}
	/**
	 * �Ƿ���Ҫ��ʾ����
	 * @return trueΪ��Ҫ��ʾ��false����Ҫ
	 */
	public boolean isGridShown(){
		return showGrid;
	}
	
	/**
	 * �Ƿ���Ҫ��ʾ�˵���
	 * @return trueΪ��Ҫ��ʾ��false����Ҫ
	 */
	public boolean isMenubarShown(){
		return showMenubar;
	}
	
	/**
	 * �Ƿ���Ҫ��ʾ������
	 * @return trueΪ��Ҫ��ʾ��false����Ҫ
	 */
	public boolean isToolbarShown(){
		return showToolbar;
	}
	
	/**
	 * �ڲ������Ƿ�ɹر�
	 * @return trueΪ�ɹرգ�false���ɹر�
	 */
	public boolean isSVGFrameCloseable(){
		return internalFrameCloseable;
	}
	
	/**
	 * �ж��ĸ����������Ǳ���ֹ��
	 * @param property ������������,����תΪСд����ȥ��ѯ
	 * @return trueΪ�ù��ܱ�����false�Ǹù������վ�
	 */
	public boolean hasDisabledProperty(String property){
		return disabledProperties.contains(property.toLowerCase());
	}

}
