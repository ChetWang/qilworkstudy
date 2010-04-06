package com.nci.svg.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NCIGlobal {
	//	
	// /**
	// * web�������ĸ�·��
	// */
	//
	// public static String appRoot = "http://192.168.133.125:8084/cserv";//wql
	// // public static String appRoot =
	// "http://192.168.133.168:8090/cserv";//zhm
	// // public static String appRoot =
	// "http://192.168.133.200:8089/cserv";//yx
	// // public static String appRoot = "http://192.168.0.50:50000/web";//SAP
	//
	//    
	// /**
	// * servlet·��
	// */
	// public static String servletPath = "/sysmanag";
	// /**
	// * ��ʽ�ļ���
	// */
	// public static final String style_file = "common.css";
	//    
	// public static String newDocumentWidth = "1600";
	//    
	// public static String newDocumentHeight = "1200";
	//    
	// public static boolean exportIncludeBackground = true;
	//	
	// public static String showmenu = "TRUE";
	// public static String showtoolbar = "TRUE";
	// public static String showstatusbar = "TRUE";
	//	
	// public static String mode = "0";
	//	
	// /**
	// * �༭ģʽ
	// */
	// public static final String SVGTOOL_MODE_EDIT = "0";
	// /**
	// * ���в��ֲ��������ģʽ
	// */
	// public static final String SVGTOOL_MODE_VIEW_1 = "1";
	// /**
	// * ��������ģʽ�����ʺ���applet��ʹ��
	// */
	// public static final String SVGTOOL_MODE_VIEW_2 = "2";
	//	
	// /**
	// *��ʼ������ɫ,��ʱĬ��Ϊ��ɫ
	// */
	// public static final Color initBackGroudColor = new Color(0,0,0);
	// /**
	// *Ĭ�ϰ����ļ�����
	// */
	// public static String strHelpCommand = "hh svghelp.chm";
	//    
	// public static boolean debug = true;

	/**
	 * Զ��servlet��������ַ
	 */
	public static final String APPROOT_Str = "appRoot";
	/**
	 * servlet����
	 */
	public static final String SERVLETPATH_Str = "servletPath";

	public static final String STYLE_FILE_Str = "style_file";

	public static final String NEW_DOCUMENT_WIDTH_Str = "default_new_doc_width";

	public static final String NEW_DOCUMENT_HEIGHT_Str = "default_new_doc_height";

	public static final String EXPORT_INCLUDE_BACKGROUND_Str = "exportIncludeBackground";

	public static final String SHOW_MENU_Str = "showmenu";

	public static final String SHOW_TOOLBAR_Str = "showtoolbar";

	public static final String SHOW_STATUSBAR_Str = "showstatusbar";

	public static final String APP_MODE_Str = "mode";

	public static final String INIT_BG_COLOR_Str = "initBackGroudColor";

	public static final String STR_HELP_COMMAND_Str = "strHelpCommand";

	private HashMap<String, Object> map_config = new HashMap<String, Object>();

	public NCIGlobal() {
		initParam();
	}

	/**
	 * ���еĳ�ʼ�������ڱ������ڼ���
	 */
	private void initParam() {
		setParam(NEW_DOCUMENT_WIDTH_Str, "2000"); // ��ʼ�����,�ȸ���һ��Ĭ��ֵ
		setParam(NEW_DOCUMENT_HEIGHT_Str, "1400"); // ��ʼ���߶�,�ȸ���һ��Ĭ��ֵ
		setParam(APP_MODE_Str, "0"); // ģʽ
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File("conf/conf.p")));
			setParam(APPROOT_Str, p.getProperty(APPROOT_Str)); // Զ��servlet��������ַ
			setParam("new_equip_url", p.getProperty("new_equip_url"));
			setParam("webflag", p.getProperty("webflag"));
			setParam("codebase", p.getProperty("codebase"));
			setParam(NEW_DOCUMENT_WIDTH_Str, p
					.getProperty("default_new_doc_width")); // ��ʼ�����
			setParam(NEW_DOCUMENT_HEIGHT_Str, p
					.getProperty("default_new_doc_height")); // ��ʼ���߶�
			setParam(APP_MODE_Str, p.getProperty("mode")); // ģʽ
		} catch (IOException ex) {
			System.err.println("conf.p not found. applet ignore");
		}
		setParam(SERVLETPATH_Str, "/ncisvgsysmanage"); // servlet����
		setParam(STYLE_FILE_Str, "common.css");

		setParam(EXPORT_INCLUDE_BACKGROUND_Str, true); // �������ɫ���
		setParam(SHOW_MENU_Str, "TRUE"); // ��ʾ�˵�
		setParam(SHOW_TOOLBAR_Str, "TRUE"); // ��ʾ������
		setParam(SHOW_STATUSBAR_Str, "TRUE"); // ��ʾ״̬��

		setParam(INIT_BG_COLOR_Str, new Color(0, 0, 0)); // ��ʼ��������ɫ
		setParam(STR_HELP_COMMAND_Str, "hh svghelp.chm"); // ���������

	}

	/**
	 * ��������Ĳ���������ȡ����ֵ
	 * 
	 * @param strName��������
	 * @return�����ڵĲ���ֵ���粻�����򷵻�null
	 */
	public Object getParam(String strName) {
		return map_config.get(strName.toLowerCase());
	}

	/**
	 * ��������Ĳ����������ò���ֵ
	 * 
	 * @param strName��������
	 * @param obValue�������õĲ���ֵ
	 */
	public void setParam(String strName, Object obValue) {
		map_config.put(strName.toLowerCase(), obValue);
		return;
	}

	public void putAll(HashMap<String, Object> config) {
		map_config.putAll(config);
		return;
	}

}
