package nci.gps.data;

import nci.gps.util.ReadConfig;
import junit.framework.TestCase;

public class DataServicesTest extends TestCase {

	public final void testSelectLSCode() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSCode());
	}

	public final void testSelectLSDept() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSDept());
	}

	public final void testSelectLSPerson() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSPerson());
	}

	public final void testSelectDriver() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectDriver());
	}

	public final void testSelectLSCar() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectLSCar());
	}

	public final void testSelectGdInfo() {
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		// ������������
		String str1 = "";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.selectGdInfo(str1));
	}
	
	public final void testUpdateGdInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">1</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">�㽭ʡ������˾</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">����</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">�㽭ʡ������˾</col> </row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.updateGdInfo(xml));
	}
	
	/**
	 * ����������Ϣ
	 *
	 */
	public final void testCreateGdInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">1</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">�㽭ʡ������˾</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">����</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">�㽭ʡ������˾</col> </row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createGdInfo(xml));
	}
	
	/**
	 * ��������������Ϣ
	 *
	 */
	public final void tstCreateGdclgcInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"ID\">1</col><col name=\"GDID\">12</col><col name=\"LCZT\">05</col><col name=\"KSSJ\">2007-12-18 18:09:53</col> <col name=\"JSSJ\">2007-12-18 18:09:53</col><col name=\"CLBMID\">0330000000000</col>  <col name=\"CLBMMC\">�㽭ʡ������˾</col><col name=\"CLRID\">00181585</col><col name=\"CLRXM\">����</col><col name=\"CLYJ\"></col><col name=\"XYCLBMID\">0330000000000</col><col name=\"XYCLBMMC\">�㽭ʡ������˾</col> </row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createGdclgcInfo(xml));
	}
	
	/**
	 * ��GPS��������������
	 *
	 */
	public final void testCreateClzbInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ResultSet id=\"TB_TMS_WXD_BASEINFO\">  <row id=\"1\"> <col name=\"CLID\">00200000</col>  <col name=\"SJ\">2006-06-23</col>  <col name=\"XZB\">5</col>  <col name=\"YZB\">5</col></row><row id=\"0\"> <col name=\"CLID\">00200000</col>  <col name=\"SJ\">2006-06-23</col>  <col name=\"XZB\">5</col>  <col name=\"YZB\">5</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createClzbInfo(xml));
	}
	
	/**
	 * ������½��Ϣ
	 *
	 */
	public final void testCreateLoginInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml = "<ResultSet><row id=\"1\"><col name=\"ZDID\">1</col><col name=\"CLID\">1</col><col name=\"CZRYID\">010101</col><col name=\"JSYID\">1</col><col name=\"KSSJ\">2007-11-23 13:23:34</col><col name=\"FLAG\">1</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.createLoginInfo(xml));
	}
	
	/**
	 * ���³�����Ϣ
	 *
	 */
	public final void testDeleteLoginInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String clid = "123";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.deleteLoginInfo(clid));
	}
	
	/**
	 * �˳���½
	 *
	 */
	public final void testUpdateLoginInfo(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml = "<ResultSet><row id=\"1\"><col name=\"ID\">12</col><col name=\"JSSJ\">2007-11-23 16:23:34</col><col name=\"FLAG\">0</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.updateLoginInfo(xml));
	}
	
	/**
	 * ��ȡ������ʱ��
	 *
	 */
	public final void testGetCurrentTime(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.getCurrentTime());
	}
	
	/**
	 * ������WebService�ӿ�
	 *
	 */
	public final void testSetLsWebServer(){
		// ������ʼ��ȫ�ֱ���
		ReadConfig.getInstance();
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\"?><ResultSet><row id=\"0\"><col name=\"CS\">2</col><col name=\"DDXCSJ\">2008-04-14 09:19:08</col><col name=\"CLRY\">00181585</col><col name=\"CLBM\">0330000000000</col><col name=\"XGCLBM\">0330000000000</col><col name=\"JDSJ\">2008-11-11 12:00:00</col><col name=\"CLSJ\">2008-08-12 09:48:13</col><col name=\"CLYJ\"></col><col name=\"GDH\">66</col></row></ResultSet>";
		DataServices dataServices = new DataServices("");
		System.out.println(dataServices.setLsWebServer(xml));
	}

}
