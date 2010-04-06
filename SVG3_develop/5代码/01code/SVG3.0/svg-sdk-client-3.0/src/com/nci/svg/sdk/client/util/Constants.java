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
	public static final int EDITOR_SPLIT_DIVIDERSIZE_NORMAL = 6;
	/**她起床上课,所以买了
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
	 * svg编辑器所需缓存存放目录
	 */
	public static final String NCI_SVG_CACHE_DIR = NCI_SVG_DIR + "cache/";

	public static final String NCI_SVG_LOGGER_DIR = NCI_SVG_DIR + "log/";

	/**
	 * add by yux,2009-1-7 代码数据的本地持久化文件
	 */
	public static final String NCI_SVG_LOCAL_CODES = NCI_SVG_DIR
			+ "local/codes.nci";
	
	/**
	 * add by yux,2009-1-19
	 * 公共数据的本地持久化文件
	 */
	public static final String NCI_SVG_LOCAL_GLOBAL = NCI_SVG_DIR
	+ "local/global.nci";
	/**
	 * add by yux,2009-1-7 图元数据的本地持久化文件
	 */
	// public static final String NCI_SVG_LOCAL_GRAPHUNIT = NCI_SVG_DIR +
	// "local/graphunits.nci";
	// /**
	// * add by yux,2009-1-7
	// * 模板数据的本地持久化文件
	// */
	// public static final String NCI_SVG_LOCAL_TEMPLATE = NCI_SVG_DIR +
	// "local/templates.nci";
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
	 * 表示图元或模板---graphunit，symbol
	 */
	public static final String NCI_SVG_Type_Attr = "nciType";
	/**
	 * 图元模板状态标记名称
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

	/**
	 * 表示该Image标签或svg是新世纪专用的图形格式的值
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
	 * 默认的绘图模式下图形的宽度
	 */
	public static final double NCI_SVG_DEFAULT_GRAPHICS_WIDTH = 100.0d;
	/**
	 * 默认的绘图模式下图形的高度
	 */
	public static final double NCI_SVG_DEFAULT_GRAPHICS_HEIGHT = 100.0d;

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

	public static final Color THUMBNAIL_RELEASED_BACKGROUND = new Color(
			0x9999ef);// new Color(153, 153, 255);

	public static final Color THUMBNAIL_PERSONAL_BACKGROUND = new Color(
			0xe999eb);// new Color(233, 133, 235);

	public static final String GRAPHUNIT_STATUS_NONE = "无";

	/**
	 * "模板"的中文名称
	 */
	public static final String SINO_NAMED_TEMPLATE = "模板";

	/**
	 * ”图元“中文名称
	 */
	public static final String SINO_NAMED_GRAPHUNIT = "图元";

	/**
	 * 属于nci图形设备的列表，用于在菜单显示时“设备属性”的需要
	 */
	public static ArrayList<String> nciGraphTypes;

	public static final int SINGLE_GRAPH_FLAG = 0;
	public static final int COMBO_GRAPH_FLAG = 1;

	/**
	 * 图元\模板类型标签名
	 */
	public static final String SYMBOL_TYPE = "symbol_type";

	/**
	 * add by yux,2009-1-7 nci自定义的类型属性名
	 */
	public static final String NCI_TYPE = "nci_type";
	/**
	 * 图元\模板状态标签名
	 */
	public static final String SYMBOL_STATUS = "symbol_status";

	public static final String SYMBOL_ID = "symbol_id";

	public static final String SYMBOL_DEFAULT_STATUS = "symbol_default_status";

	private static byte[] iniConstantsLock = new byte[0];

	private static boolean iniOK = false;

	/**
	 * add by yux,2009-1-5 图元与状态间分隔符
	 */
	public static final String SYMBOL_STATUS_SEP = "_#@sss@#_";
	public static final String SYMBOL_DATE_SEP = "@sds@";
	/**
	 * add by yux,2009-1-5 图层后缀
	 */
	public static final String LAYER_SUFFIX = "_layer";

	/**
	 * add by yux,2009-1-5 连接线
	 */
	public static final String LAYER_PATH = "link_layer";
	public static final String LAYER_PATH_NAME = "连线层";

	/**
	 * add by yux,2009-1-5 文件描述图层
	 */
	public static final String LAYER_TEXT = "text_layer";
	public static final String LAYER_TEXT_NAME = "文字描述层";
	/**
	 * add by yux,2009-1-5 其他图层
	 */
	public static final String LAYER_OTHER = "other_layer";
	public static final String LAYER_OTHER_NAME = "其他层";
	/**
	 * add by yux,2008.12.30 标准图标识
	 */
	public static String GRAPHFILE_STANDARD = "1";

	/**
	 * add by yux,2008-12-30 个性图标识
	 */
	public static String GRAPHFILE_PERSONAL = "2";

	/**
	 * add by yux,2008-12-30 svg文件格式
	 */
	public static String FILE_FORMAT_SVG = "svg";

	/**
	 * add by yux,2008-12-30 jpg文件格式
	 * 
	 */
	public static String FILE_FORMAT_JPG = "jpg";

	/**
	 * add by yux,2009-1-9 系统架包
	 */
	public static String MODULE_TYPE_SYSTEM = "0";
	/**
	 * add by yux,2009-1-9 平台模块
	 */
	public static String MODULE_TYPE_PALTROOF = "1";

	/**
	 * add by yux,2009-1-9 业务组件
	 */
	public static String MODULE_TYPE_BUSINESS = "2";
	
	public static HashMap<String,Color> mapColor = new HashMap<String,Color>();
	static {
		// 这里必须同步，否则多线程下很容易产生冲突
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
