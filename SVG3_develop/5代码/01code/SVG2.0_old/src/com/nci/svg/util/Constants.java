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
	 * 什么鼠标操作都没有
	 */
	public static final int MOUSE_NONE = -1;

	/**
	 * 鼠标按下（press）状态
	 */
	public static final int MOUSE_PRESSED = 0;
	/**
	 * 鼠标松开（release）状态
	 */
	public static final int MOUSE_RELEASED = 1;

	/**
	 * 鼠标进入（entered）状态
	 */
	public static final int MOUSE_ENTERED = 2;

	/**
	 * 鼠标挪开（exited）状态
	 */
	public static final int MOUSE_EXITED = 3;

	/**
	 * 图元管理画布的宽度
	 */
	public static final String GRAPH_UNIT_WIDTH_StringValue = "450";
	public static final int GRAPH_UNIT_WIDTH_IntValue = 450;
	/**
	 * 图元管理画布的高度
	 */
	public static final String GRAPH_UNIT_HEIGHT_StringValue = "300";
	public static final int GRAPH_UNIT_HEIGHT_IntValue = 300;

	/**
	 * 编辑器splict pane的初始分割位置
	 */
	public static final int EDITOR_SPLIT_DIVIDERLOCATION = 280;
	/**
	 * 编辑器splict pane分割线的正常宽度
	 */
	public static final int EDITOR_SPLIT_DIVIDERSIZE_NORMAL = 9;
	/**
	 * 编辑器splict pane分割线的最小宽度
	 */
	public static final int EDITOR_SPLIT_DIVIDERSIZE_MIN = 1;

	public final static String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

	/**
	 * svg编辑器设置及缓存的存放路径
	 */
	public static final String NCI_SVG_DIR = System.getProperty("user.home")
			+ "/.nci/svg/";

	/**
	 * svg编辑器所需第三方库文件存放目录
	 */
	public static final String NCI_SVG_LIB_DIR = NCI_SVG_DIR + "lib/";

	/**
	 * svg编辑器所需缓存存放目录
	 */
	public static final String NCI_SVG_CACHE_DIR = NCI_SVG_DIR + "cache/";

	/**
	 * symbol的版本文件
	 */
	public static final String NCI_SVG_SYMBOL_VERSION_FILE = NCI_SVG_CACHE_DIR
			+ "symbol_version.nci";

	/**
	 * 裁剪的文件
	 */
	public static final String NCI_SVG_CACHE_CLIP_TEMP_CACHE_DIR = NCI_SVG_CACHE_DIR
			+ "clipcahce/";

	/**
	 * svg编辑器图元缓存存放目录
	 */
	public static final String NCI_SVG_SYMBOL_CACHE_DIR = NCI_SVG_CACHE_DIR
			+ "symbols/";

	/**
	 * 打开的图形的缓存目录
	 */
	public static final String NCI_SVG_DOWNLOADS_CACHE_DIR = NCI_SVG_CACHE_DIR
			+ "downloads/";

	/**
	 * 图元缓存及图元服务器中的图元版本信息存放文件格式
	 */
	public static final String NCI_SVG_MOD_FILE_EXTENSION = ".ncimod";

	public static final String NCI_SVG_EXTENDSION = ".svg";

	/**
	 * 表示该Image标签或svg是新世纪专用的图形格式，表示图元
	 */
	public static final String NCI_SVG_Type_Attr = "nciType";
	/**
	 * 设备状态标记名称
	 */
	public static final String NCI_SVG_Status = "statusFlag";
	/**
	 * 新建的设备标记
	 */
	public static final String NCI_SVG_Status_New = "new";
	/**
	 * 更新设备信息标记，即在库中已有设备时，该标签标识要更新库中设备的信息
	 */
	public static final String NCI_SVG_Status_Update = "update";
	/**
	 * 已存在的设备标记
	 */
	public static final String NCI_SVG_Status_Previous = "old";

	public static final String NCI_SVG_XMLNS = "xmlns:nci";

	public static final String PSR_SVG_XMLNS = "xmlns:PSR";
	/**
	 * 表示该Image标签或svg是新世纪专用的图形格式的值
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
	 * 默认的绘图模式下图形的宽度
	 */
	public static final int NCI_SVG_DEFAULT_GRAPHICS_WIDTH = 48;
	/**
	 * 默认的绘图模式下图形的高度
	 */
	public static final int NCI_SVG_DEFAULT_GRAPHICS_HEIGHT = 48;

	/**
	 * 默认的笔画颜色
	 */
	public static final String NCI_DEFAULT_STROKE_COLOR = "#000000";

	/**
	 * 默认图元的填充颜色
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

	public static final String GRAPHUNIT_STATUS_NONE = "无";

	/**
	 * 属于nci图形设备的列表，用于在菜单显示时“设备属性”的需要
	 */
	public static ArrayList<String> nciGraphTypes;

	public static final int SINGLE_GRAPH_FLAG = 0;
	public static final int COMBO_GRAPH_FLAG = 1;

	private static byte[] iniConstantsLock = new byte[0];

	private static boolean iniOK = false;

	static {
		//这里必须同步，否则多线程下很容易产生冲突
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
