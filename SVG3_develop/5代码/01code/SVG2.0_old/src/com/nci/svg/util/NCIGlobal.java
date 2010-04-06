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
	// * web服务器的根路径
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
	// * servlet路径
	// */
	// public static String servletPath = "/sysmanag";
	// /**
	// * 样式文件名
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
	// * 编辑模式
	// */
	// public static final String SVGTOOL_MODE_EDIT = "0";
	// /**
	// * 能有部分操作的浏览模式
	// */
	// public static final String SVGTOOL_MODE_VIEW_1 = "1";
	// /**
	// * 纯粹的浏览模式，仅适合在applet下使用
	// */
	// public static final String SVGTOOL_MODE_VIEW_2 = "2";
	//	
	// /**
	// *初始化背景色,暂时默认为黑色
	// */
	// public static final Color initBackGroudColor = new Color(0,0,0);
	// /**
	// *默认帮助文件名字
	// */
	// public static String strHelpCommand = "hh svghelp.chm";
	//    
	// public static boolean debug = true;

	/**
	 * 远程servlet服务器地址
	 */
	public static final String APPROOT_Str = "appRoot";
	/**
	 * servlet服务
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
	 * 所有的初始化参数在本函数内加载
	 */
	private void initParam() {
		setParam(NEW_DOCUMENT_WIDTH_Str, "2000"); // 初始化宽度,先给定一个默认值
		setParam(NEW_DOCUMENT_HEIGHT_Str, "1400"); // 初始化高度,先给定一个默认值
		setParam(APP_MODE_Str, "0"); // 模式
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File("conf/conf.p")));
			setParam(APPROOT_Str, p.getProperty(APPROOT_Str)); // 远程servlet服务器地址
			setParam("new_equip_url", p.getProperty("new_equip_url"));
			setParam("webflag", p.getProperty("webflag"));
			setParam("codebase", p.getProperty("codebase"));
			setParam(NEW_DOCUMENT_WIDTH_Str, p
					.getProperty("default_new_doc_width")); // 初始化宽度
			setParam(NEW_DOCUMENT_HEIGHT_Str, p
					.getProperty("default_new_doc_height")); // 初始化高度
			setParam(APP_MODE_Str, p.getProperty("mode")); // 模式
		} catch (IOException ex) {
			System.err.println("conf.p not found. applet ignore");
		}
		setParam(SERVLETPATH_Str, "/ncisvgsysmanage"); // servlet服务
		setParam(STYLE_FILE_Str, "common.css");

		setParam(EXPORT_INCLUDE_BACKGROUND_Str, true); // 输出背景色标记
		setParam(SHOW_MENU_Str, "TRUE"); // 显示菜单
		setParam(SHOW_TOOLBAR_Str, "TRUE"); // 显示工具条
		setParam(SHOW_STATUSBAR_Str, "TRUE"); // 显示状态条

		setParam(INIT_BG_COLOR_Str, new Color(0, 0, 0)); // 初始化背景颜色
		setParam(STR_HELP_COMMAND_Str, "hh svghelp.chm"); // 帮助打开语句

	}

	/**
	 * 根据输入的参数名，获取参数值
	 * 
	 * @param strName：参数名
	 * @return：存在的参数值，如不存在则返回null
	 */
	public Object getParam(String strName) {
		return map_config.get(strName.toLowerCase());
	}

	/**
	 * 根据输入的参数名，设置参数值
	 * 
	 * @param strName：参数名
	 * @param obValue：待设置的参数值
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
