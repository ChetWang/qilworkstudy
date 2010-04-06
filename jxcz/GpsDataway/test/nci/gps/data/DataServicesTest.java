package nci.gps.data;

import nci.gps.util.ReadConfig;
import junit.framework.TestCase;

public class DataServicesTest extends TestCase {

	public final void testSelectLSCode() {
		// 启动初始化全局变量
		ReadConfig.getInstance();
		// 调用数据请求
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSCode());
	}

	public final void testSelectLSDept() {
		// 启动初始化全局变量
		ReadConfig.getInstance();
		// 调用数据请求
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSDept());
	}

	public final void testSelectLSPerson() {
		// 启动初始化全局变量
		ReadConfig.getInstance();
		// 调用数据请求
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSPerson());
	}

	public final void testSelectDriver() {
		// 启动初始化全局变量
		ReadConfig.getInstance();
		// 调用数据请求
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectDriver());
	}

	public final void testSelectLSCar() {
		// 启动初始化全局变量
		ReadConfig.getInstance();
		// 调用数据请求
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSCar());
	}

	public final void testSelectGdInfo() {
		// 启动初始化全局变量
		ReadConfig.getInstance();
		// 调用数据请求
		String str1 = "";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectGdInfo(str1));
	}
	
	public final void testUpdateGdInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">1</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">浙江省电力公司</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">陈理</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">浙江省电力公司</col> </row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.updateGdInfo(xml));
	}
	
	/**
	 * 新增工单信息
	 *
	 */
	public final void testCreateGdInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">1</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">浙江省电力公司</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">陈理</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">浙江省电力公司</col> </row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createGdInfo(xml));
	}
	
	/**
	 * 新增工单处理信息
	 *
	 */
	public final void tstCreateGdclgcInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">1</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">浙江省电力公司</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">陈理</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">浙江省电力公司</col> </row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createGdclgcInfo(xml));
	}
	
	/**
	 * 从GPS中新增处理坐标
	 *
	 */
	public final void testCreateClzbInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ResultSet id=\"TB_TMS_WXD_BASEINFO\">  <row id=\"1\"> <col name=\"CLID\">00200000</col>  <col name=\"SJ\">2006-06-23</col>  <col name=\"XZB\">5</col>  <col name=\"YZB\">5</col></row><row id=\"0\"> <col name=\"CLID\">00200000</col>  <col name=\"SJ\">2006-06-23</col>  <col name=\"XZB\">5</col>  <col name=\"YZB\">5</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createClzbInfo(xml));
	}
	
	/**
	 * 创建登陆信息
	 *
	 */
	public final void testCreateLoginInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml = "<ResultSet><row id=\"1\"><col name=\"ZDID\">1</col><col name=\"CLID\">1</col><col name=\"CZRYID\">010101</col><col name=\"JSYID\">1</col><col name=\"KSSJ\">2007-11-23 13:23:34</col><col name=\"FLAG\">1</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createLoginInfo(xml));
	}
	
	/**
	 * 更新车辆信息
	 *
	 */
	public final void testDeleteLoginInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String clid = "123";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.deleteLoginInfo(clid));
	}
	
	/**
	 * 退出登陆
	 *
	 */
	public final void testUpdateLoginInfo(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml = "<ResultSet><row id=\"1\"><col name=\"ID\">12</col><col name=\"JSSJ\">2007-11-23 16:23:34</col><col name=\"FLAG\">0</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.updateLoginInfo(xml));
	}
	
	/**
	 * 获取服务器时间
	 *
	 */
	public final void testGetCurrentTime(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.getCurrentTime());
	}
	
	/**
	 * 调郎新WebService接口
	 *
	 */
	public final void testSetLsWebServer(){
		// 启动初始化全局变量
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"CS\">2</col><col name=\"DDXCSJ\">2008-04-14 09:19:08</col><col name=\"CLRY\">00181585</col><col name=\"CLBM\">0330000000000</col><col name=\"XGCLBM\">0330000000000</col><col name=\"JDSJ\">2008-11-11 12:00:00</col><col name=\"CLSJ\">2008-08-12 09:48:13</col><col name=\"CLYJ\"></col><col name=\"GDH\">66</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.setLsWebServer(xml));
	}

}
