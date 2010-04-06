/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.util;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.apache.batik.dom.svg.SVGDOMImplementation;

/**
 * 
 * @author Qil.Wong
 */
public class Constants {

	/**
	 * ʲô��������û��
	 */
	public static final int MOUSE_NONE = -1;

	/**
	 * ��갴�£�press��״̬
	 */
	public static final int MOUSE_PRESSED = 0;
	/**
	 * ����ɿ���release��״̬
	 */
	public static final int MOUSE_RELEASED = 1;

	/**
	 * �����루entered��״̬
	 */
	public static final int MOUSE_ENTERED = 2;

	/**
	 * ���Ų����exited��״̬
	 */
	public static final int MOUSE_EXITED = 3;

	/**
	 * ͼԪ�������Ŀ��
	 */
	public static final String GRAPH_UNIT_WIDTH_StringValue = "450";
	public static final int GRAPH_UNIT_WIDTH_IntValue = 450;
	/**
	 * ͼԪ�������ĸ߶�
	 */
	public static final String GRAPH_UNIT_HEIGHT_StringValue = "300";
	public static final int GRAPH_UNIT_HEIGHT_IntValue = 300;

	/**
	 * �༭��splict pane�ĳ�ʼ�ָ�λ��
	 */
	public static final int EDITOR_SPLIT_DIVIDERLOCATION = 280;
	/**
	 * �༭��splict pane�ָ��ߵ��������
	 */
	public static final int EDITOR_SPLIT_DIVIDERSIZE_NORMAL = 9;
	/**
	 * �༭��splict pane�ָ��ߵ���С���
	 */
	public static final int EDITOR_SPLIT_DIVIDERSIZE_MIN = 1;

	public final static String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

	/**
	 * svg�༭�����ü�����Ĵ��·��
	 */
	public static final String NCI_SVG_DIR = System.getProperty("user.home")
			+ "/.nci/svg/";

	/**
	 * svg�༭��������������ļ����Ŀ¼
	 */
	public static final String NCI_SVG_LIB_DIR = NCI_SVG_DIR + "lib/";

	/**
	 * svg�༭�����軺����Ŀ¼
	 */
	public static final String NCI_SVG_CACHE_DIR = NCI_SVG_DIR + "cache/";

	/**
	 * symbol�İ汾�ļ�
	 */
	public static final String NCI_SVG_SYMBOL_VERSION_FILE = NCI_SVG_CACHE_DIR
			+ "symbol_version.nci";

	/**
	 * �ü����ļ�
	 */
	public static final String NCI_SVG_CACHE_CLIP_TEMP_CACHE_DIR = NCI_SVG_CACHE_DIR
			+ "clipcahce/";

	/**
	 * svg�༭��ͼԪ������Ŀ¼
	 */
	public static final String NCI_SVG_SYMBOL_CACHE_DIR = NCI_SVG_CACHE_DIR
			+ "symbols/";

	/**
	 * �򿪵�ͼ�εĻ���Ŀ¼
	 */
	public static final String NCI_SVG_DOWNLOADS_CACHE_DIR = NCI_SVG_CACHE_DIR
			+ "downloads/";

	/**
	 * ͼԪ���漰ͼԪ�������е�ͼԪ�汾��Ϣ����ļ���ʽ
	 */
	public static final String NCI_SVG_MOD_FILE_EXTENSION = ".ncimod";

	public static final String NCI_SVG_EXTENDSION = ".svg";

	/**
	 * ��ʾ��Image��ǩ��svg��������ר�õ�ͼ�θ�ʽ����ʾͼԪ
	 */
	public static final String NCI_SVG_Type_Attr = "nciType";
	/**
	 * �豸״̬�������
	 */
	public static final String NCI_SVG_Status = "statusFlag";
	/**
	 * �½����豸���
	 */
	public static final String NCI_SVG_Status_New = "new";
	/**
	 * �����豸��Ϣ��ǣ����ڿ��������豸ʱ���ñ�ǩ��ʶҪ���¿����豸����Ϣ
	 */
	public static final String NCI_SVG_Status_Update = "update";
	/**
	 * �Ѵ��ڵ��豸���
	 */
	public static final String NCI_SVG_Status_Previous = "old";

	public static final String NCI_SVG_XMLNS = "xmlns:nci";

	public static final String PSR_SVG_XMLNS = "xmlns:PSR";
	/**
	 * ��ʾ��Image��ǩ��svg��������ר�õ�ͼ�θ�ʽ��ֵ
	 */
	public static final String NCI_SVG_Type_GraphUnit_Value = "GraphUnit";

	public static final String NCI_SVG_EquipType_Name = "equipType";

	public static final String NCI_SVG_Type_ConnectedLine_Value = "ConnectedLine";

	public static final String NCI_SVG_Type_ConnectedLine_Name = "powerLine";

	public static final String NCI_FROM_TO_SUBSTATION_COMPONENT_NAME = "dynamic";

	public static final String NCI_SVG_PRODUCER_ATTR = "xmlns:producer";

	public static final String NCI_SVG_PRODUCER_VALUE = "http://www.nci.com.cn/ncird";
	public static final String NCI_SVG_XMLNS_VALUE = "http://www.nci.com.cn";
	public static final String PSR_SVG_XMLNS_VALUE = "http://www.cim.com";

	public static final String NCI_SVG_INNER_IMAGE_ATTR = "imageType";

	public static final String NCI_SVG_INNER_IMAGE_VALUE = "inner";
	/**
	 * Ĭ�ϵĻ�ͼģʽ��ͼ�εĿ��
	 */
	public static final int NCI_SVG_DEFAULT_GRAPHICS_WIDTH = 48;
	/**
	 * Ĭ�ϵĻ�ͼģʽ��ͼ�εĸ߶�
	 */
	public static final int NCI_SVG_DEFAULT_GRAPHICS_HEIGHT = 48;

	/**
	 * Ĭ�ϵıʻ���ɫ
	 */
	public static final String NCI_DEFAULT_STROKE_COLOR = "#000000";

	/**
	 * Ĭ��ͼԪ�������ɫ
	 */
	public static final String NCI_DEFAULT_GRAPHUNIT_FILL = "#000000";

	public static final String NCI_SVG_DOCUMENT_DASHARRAY_ATTR = "stroke-dasharray";
	public static final String NCI_SVG_DOCUMENT_DASHARRAY_DEFAULT_VALUE = "5";
	public static final String NCI_SVG_DOCUMENT_DASHARRAY_NONE_VALUE = "none";

	public static final String NCI_SVG_METADATA = "metadata";
	public static final String NCI_SVG_APPCODE_ATTR = "AppCode";
	public static final String NCI_SVG_SCADAID_ATTR = "ObjectID";
	public static final String NCI_SVG_PSR_OBJREF = "PSR:ObjRef";

	public static final int SERVLET_TIME_OUT = 15 * 1000;

	public static final Color THUMBNAIL_BACKGROUND = new Color(153, 153, 255);

	public static final String GRAPHUNIT_STATUS_NONE = "��";

	/**
	 * ����nciͼ���豸���б������ڲ˵���ʾʱ���豸���ԡ�����Ҫ
	 */
	public static ArrayList<String> nciGraphTypes;

	public static final int SINGLE_GRAPH_FLAG = 0;
	public static final int COMBO_GRAPH_FLAG = 1;

	private static byte[] iniConstantsLock = new byte[0];

	private static boolean iniOK = false;

	static {
		//�������ͬ����������߳��º����ײ�����ͻ
		synchronized (iniConstantsLock) {
			if (!iniOK) {
				nciGraphTypes = new ArrayList<String>();
				nciGraphTypes.add(NCI_SVG_Type_GraphUnit_Value);
				nciGraphTypes.add(NCI_SVG_Type_ConnectedLine_Value);
				File downloadCache = new File(NCI_SVG_DOWNLOADS_CACHE_DIR);
				File clipcahceDir = new File(NCI_SVG_CACHE_CLIP_TEMP_CACHE_DIR);
				Utilities.clearCacheFolder(downloadCache);
				Utilities.clearCacheFolder(clipcahceDir);
				iniOK = true;
			}
		}
	}
}
