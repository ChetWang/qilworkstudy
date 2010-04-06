package com.nci.svg.sdk.mode;

import java.util.ArrayList;
import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;


public abstract class EditorModeAdapter {

	protected EditorAdapter editor;

	protected String mode;

	protected boolean createOutlookPane;
	
	protected boolean createPropertyPane;

	protected boolean showPopupMenu;

	protected boolean createJFileChooser;
	
	protected boolean showRuler;
	
	protected boolean showGrid;
	
	protected boolean showToolbar;
	
	protected boolean showMenubar;
	
	protected boolean internalFrameCloseable;
	
	protected boolean graphunit_has_model = true;
	
	protected boolean F = true;
	
	protected HashSet<String> disabledProperties = new HashSet<String>();
	
	protected ArrayList<EditorModeListener> modeListeners = new ArrayList<EditorModeListener>();

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
	 *            �Ƿ���Ҫ����Ӧ�Ҽ��˵�
	 */
	public EditorModeAdapter(EditorAdapter editor, String mode) {
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
	public EditorAdapter getEditor() {
		return editor;
	}

	/**
	 * �ж��Ƿ���Ҫ����OutlookPane��ͼԪ���
	 * 
	 * @return
	 */
	public boolean isOutlookPaneCreate() {
		return createOutlookPane;
	}
	
	public boolean isPropertyPaneCreate(){
		return createPropertyPane;
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
	 * �Ƿ�ͼԪ��Ӧģ��
	 * @return
	 */
	public boolean isGraphUnitHasModel(){
		return this.graphunit_has_model;
	}
	
	/**
	 * �ж��ĸ����������Ǳ���ֹ��
	 * @param property ������������,����תΪСд����ȥ��ѯ
	 * @return trueΪ�ù��ܱ�����false�Ǹù������վ�
	 */
	public boolean hasDisabledProperty(String property){
		return disabledProperties.contains(property.toLowerCase());
	}
	
	/**
	 * ע��ģʽ�仯�����¼�
	 * @param listener �����¼�
	 */
	public void addModeListener(EditorModeListener listener){
		modeListeners.add(listener);
	}
	
	/**
	 * ģʽ�仯��������ע���¼�
	 */
	public void notifyModeChanged(){
		for(EditorModeListener listener: modeListeners){
			listener.modeChanged();
		}
	}
	
	public Element modifyUseElement(Element useElement){
		return useElement;
	}

}
