/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.sdk.client.util;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.batik.dom.svg.SVGDOMImplementation;

import com.nci.svg.sdk.bean.ModelActionBean;

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
	public static final int EDITOR_SPLIT_DIVIDERSIZE_NORMAL = 6;
	/**�����Ͽ�,��������
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
	 * svg�༭�����軺����Ŀ¼
	 */
	public static final String NCI_SVG_CACHE_DIR = NCI_SVG_DIR + "cache/";

	public static final String NCI_SVG_LOGGER_DIR = NCI_SVG_DIR + "log/";

	/**
	 * add by yux,2009-1-7 �������ݵı��س־û��ļ�
	 */
	public static final String NCI_SVG_LOCAL_CODES = NCI_SVG_DIR
			+ "local/codes.nci";
	
	/**
	 * add by yux,2009-1-19
	 * �������ݵı��س־û��ļ�
	 */
	public static final String NCI_SVG_LOCAL_GLOBAL = NCI_SVG_DIR
	+ "local/global.nci";
	/**
	 * add by yux,2009-1-7 ͼԪ���ݵı��س־û��ļ�
	 */
	// public static final String NCI_SVG_LOCAL_GRAPHUNIT = NCI_SVG_DIR +
	// "local/graphunits.nci";
	// /**
	// * add by yux,2009-1-7
	// * ģ�����ݵı��س־û��ļ�
	// */
	// public static final String NCI_SVG_LOCAL_TEMPLATE = NCI_SVG_DIR +
	// "local/templates.nci";
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
	 * ��ʾͼԪ��ģ��---graphunit��symbol
	 */
	public static final String NCI_SVG_Type_Attr = "nciType";
	/**
	 * ͼԪģ��״̬�������
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

	/**
	 * ��ʾ��Image��ǩ��svg��������ר�õ�ͼ�θ�ʽ��ֵ
	 */
	public static final String NCI_SVG_Type_GraphUnit_Value = "GraphUnit";

	public static final String NCI_SVG_EquipType_Name = "equipType";

	public static final String NCI_SVG_Type_ConnectedLine_Value = "ConnectedLine";

	public static final String NCI_SVG_Type_ConnectedLine_Name = "powerLine";

	public static final String NCI_FROM_TO_SUBSTATION_COMPONENT_NAME = "dynamic";

	public static final String NCI_SVG_XMLNS_VALUE = "http://www.nci.com.cn";

	public static final String NCI_SVG_INNER_IMAGE_ATTR = "imageType";

	public static final String NCI_SVG_INNER_IMAGE_VALUE = "inner";
	/**
	 * Ĭ�ϵĻ�ͼģʽ��ͼ�εĿ��
	 */
	public static final double NCI_SVG_DEFAULT_GRAPHICS_WIDTH = 100.0d;
	/**
	 * Ĭ�ϵĻ�ͼģʽ��ͼ�εĸ߶�
	 */
	public static final double NCI_SVG_DEFAULT_GRAPHICS_HEIGHT = 100.0d;

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

	public static final Color THUMBNAIL_RELEASED_BACKGROUND = new Color(
			0x9999ef);// new Color(153, 153, 255);

	public static final Color THUMBNAIL_PERSONAL_BACKGROUND = new Color(
			0xe999eb);// new Color(233, 133, 235);

	public static final String GRAPHUNIT_STATUS_NONE = "��";

	/**
	 * "ģ��"����������
	 */
	public static final String SINO_NAMED_TEMPLATE = "ģ��";

	/**
	 * ��ͼԪ����������
	 */
	public static final String SINO_NAMED_GRAPHUNIT = "ͼԪ";

	/**
	 * ����nciͼ���豸���б������ڲ˵���ʾʱ���豸���ԡ�����Ҫ
	 */
	public static ArrayList<String> nciGraphTypes;

	public static final int SINGLE_GRAPH_FLAG = 0;
	public static final int COMBO_GRAPH_FLAG = 1;

	/**
	 * ͼԪ\ģ�����ͱ�ǩ��
	 */
	public static final String SYMBOL_TYPE = "symbol_type";

	/**
	 * add by yux,2009-1-7 nci�Զ��������������
	 */
	public static final String NCI_TYPE = "nci_type";
	/**
	 * ͼԪ\ģ��״̬��ǩ��
	 */
	public static final String SYMBOL_STATUS = "symbol_status";

	public static final String SYMBOL_ID = "symbol_id";

	public static final String SYMBOL_DEFAULT_STATUS = "symbol_default_status";

	private static byte[] iniConstantsLock = new byte[0];

	private static boolean iniOK = false;

	/**
	 * add by yux,2009-1-5 ͼԪ��״̬��ָ���
	 */
	public static final String SYMBOL_STATUS_SEP = "_#@sss@#_";
	public static final String SYMBOL_DATE_SEP = "@sds@";
	/**
	 * add by yux,2009-1-5 ͼ���׺
	 */
	public static final String LAYER_SUFFIX = "_layer";

	/**
	 * add by yux,2009-1-5 ������
	 */
	public static final String LAYER_PATH = "link_layer";
	public static final String LAYER_PATH_NAME = "���߲�";

	/**
	 * add by yux,2009-1-5 �ļ�����ͼ��
	 */
	public static final String LAYER_TEXT = "text_layer";
	public static final String LAYER_TEXT_NAME = "����������";
	/**
	 * add by yux,2009-1-5 ����ͼ��
	 */
	public static final String LAYER_OTHER = "other_layer";
	public static final String LAYER_OTHER_NAME = "������";
	/**
	 * add by yux,2008.12.30 ��׼ͼ��ʶ
	 */
	public static String GRAPHFILE_STANDARD = "1";

	/**
	 * add by yux,2008-12-30 ����ͼ��ʶ
	 */
	public static String GRAPHFILE_PERSONAL = "2";

	/**
	 * add by yux,2008-12-30 svg�ļ���ʽ
	 */
	public static String FILE_FORMAT_SVG = "svg";

	/**
	 * add by yux,2008-12-30 jpg�ļ���ʽ
	 * 
	 */
	public static String FILE_FORMAT_JPG = "jpg";

	/**
	 * add by yux,2009-1-9 ϵͳ�ܰ�
	 */
	public static String MODULE_TYPE_SYSTEM = "0";
	/**
	 * add by yux,2009-1-9 ƽ̨ģ��
	 */
	public static String MODULE_TYPE_PALTROOF = "1";

	/**
	 * add by yux,2009-1-9 ҵ�����
	 */
	public static String MODULE_TYPE_BUSINESS = "2";
	
	public static HashMap<String,Color> mapColor = new HashMap<String,Color>();
	static {
		// �������ͬ����������߳��º����ײ�����ͻ
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
	
	static
	{
		mapColor.put(ModelActionBean.TYPE_NEWINSTANCE, new Color(255,0,0));
		mapColor.put(ModelActionBean.TYPE_RELAINSTANCE, new Color(0,255,0));
		mapColor.put(ModelActionBean.TYPE_MOUSE_RDOWM, new Color(0,0,255));
	}
	
	public static Color getColor(String key)
	{
		return mapColor.get(key);
	}
}
