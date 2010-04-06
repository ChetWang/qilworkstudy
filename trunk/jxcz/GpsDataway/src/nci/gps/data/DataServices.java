package nci.gps.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import nci.gps.util.TransactionPool;


/**
 * @���ܣ��ն�15�����ݲ����������
 * @ʱ�䣺2008-07-23
 * @author ZHOUHM
 * 
 */
public class DataServices {
	private String messageKey;	// ����֡Ψһ��ʶ
	public DataServices(String messageKey){
		this.messageKey = messageKey;
	}

	/**
	 * ��ȡ����
	 * @return
	 * 0x01
	 */
	public String selectLSCode() {
		String sql = "select SYMBOL,StandardName,Code,Name from TB_GPS_CZ_LS_DMB";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * ��ȡ����
	 * @return
	 * 0x02
	 */
	public String selectLSDept() {
		String sql = "select BRANCH_CODE,BRANCH_NAME from TB_GPS_CZ_LS_BM";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * ��ȡ��Ա
	 * @return
	 * 0x03
	 */
	public String selectLSPerson() {
		String sql = "select WORK_NO,Name,Dept_Code from TB_GPS_CZ_LS_RY";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/***
	 * ��ȡ��ʻԱ��Ϣ
	 * @return
	 * 0x04
	 */
	public String selectDriver() {
		String sql = "select ID,NO,NAME from TB_GPS_CZ_JSYXX";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * ��ȡ������Ϣ
	 * @return
	 * 0x05
	 */
	public String selectLSCar() {
		String sql = "select ID,NO, DriverID,DriverName,TerminalID,TerminalNO from TB_GPS_CZ_CLXX";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}
	
	/**
	 * ��ȡ�ն���Ϣ
	 * @return
	 * 0x11
	 */
	public String selectTerminal(){
		String sql = "SELECT ID,NO,CALLNO,NAME,CLID FROM TB_GPS_CZ_ZDXX";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * ��ȡ��������,�ն˻�δ���յĹ����������Ѿ��ɷ��Ĺ�����LCZT='05'��
	 * @param xml String xml�ַ���
	 * @return
	 * 0x06
	 */
	public String selectGdInfo(String str1) {
		DataUtil du = new DataUtil(messageKey);
		String sql = "select ID,GDH,GDLX,SLSJ,LXRXM,LXDZ,LXDH,SLNR,HH,FZBMID,FZBMMC,CLID from TB_GPS_CZ_GD where LCZT='05'";
		if (str1 != null && !str1.equals("")) {
			sql = sql + " and clid='" + str1 + "'";
		}
		return du.getResultSet(sql);
	}
	
	/**
	 * ��ȡ��������ǰʱ�䣬��ȷ����
	 * @return
	 * 0x0C
	 */
	public String getCurrentTime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * �����ӵ��ɹ�,�����¹����е��ɹ���Ϣ,TB_GPS_CZ_GD
	 * ���������ֳ����޸Ĺ���״̬
	 * ���ϴ������
	 * @param xml String xml�ַ���
	 * @return
	 * 0x07 
	 */
	public String updateGdInfo(String xml){
		DataUtil du = new DataUtil(messageKey);
		String[] sql = du.setGdInfo(xml);
		String[] keyValue = du.getKeyValue(xml);
		String[] flag = new String[sql.length];
		for(int i=0;i<sql.length;i++){
			if(!sql[i].equals("")&&sql[i]!=null)
				flag[i] = du.putSql(sql[i]);
			else 
				flag[i] = "�޸Ĺ���״̬��SQL���û�����ɳɹ�";
		}
		return du.generateXML(keyValue,flag);
	}

	/**
	 * ����������Ϣ
	 * @param xml String xml�ַ���
	 * @return
	 * 0x08
	 */
	public String createGdInfo(String xml) {
		DataUtil du = new DataUtil(messageKey);
		String[] sql = du.setNewGdInfo(xml);
		String[] keyValue = du.getKeyValue(xml);
		String[] flag = new String[sql.length];
		for(int i=0;i<sql.length;i++){
			flag[i] = du.putSql(sql[i]);
		}
		return du.generateXML(keyValue,flag);
	}

	/**
	 * ��������������Ϣ��XML���������0Ϊ�ɹ�
	 * @param xml String xml�ַ���
	 * @return 0x09
	 */
	public String createGdclgcInfo(String xml) {
		DataUtil du = new DataUtil(messageKey);
		return du.generateXML("result",du.putSql(du.setGdclgcInfo(xml)));
	}

	/**
	 * ��GPS��������������
	 * @param xml String xml�ַ���
	 * @return 
	 * 0x0A
	 */
	public String createClzbInfo(String xml) {
		DataUtil du = new DataUtil(messageKey);
		return du.generateXML("result",du.putSql(du.setClzbInfo(xml)));
	}
	
	/**
	 * ����LS�ӿ�
	 * @param xml String xml�ַ���
	 * @return 
	 * 0x0B
	 */
	public String setLsWebServer(String xml) {
//		ֱ�ӵ���LSWebservic	
		DataUtil du = new DataUtil(messageKey);
		String[] flag = null;
		flag = du.setLSInfo(xml);
		String[] keyValue = du.getGdhValue(xml);
		String temp = du.generateXML(keyValue,flag);
		return temp;	
	}
	
	/**
	 * ������½��Ϣ
	 * @param xml String xml�ַ���
	 * @return ����id������ͷ���0
	 * 0x0D
	 */
	public String createLoginInfo(String xml) {
		if (xml != null && xml.length() != 0) {
			DataUtil du = new DataUtil(messageKey);
			String value = "" + new Date().getTime();
			String returnValue = du.putSql(du.setZddlxxInfo(xml, value.trim()));
			if (returnValue.equals("1"))
				returnValue = value;
			else
				returnValue = "0";
			return returnValue;
		} else {
			return null;
		}
	}
	
	/**
	 * ���³�����Ϣ
	 * @param clid String �������
	 * @return �ɹ�>0
	 * 0x0E
	 */
	public String deleteLoginInfo(String clid) {
		DataUtil du = new DataUtil(messageKey);
		String sql = null;
		if(clid!=null&&!clid.equals(""))
			sql = "update TB_GPS_CZ_ZDDLXX set flag='0' where flag='1' and clid='"+clid+"'";
		else
			sql = "update TB_GPS_CZ_ZDDLXX set flag='0' where flag='1'";
		return du.putSql(sql);
	}
	
	/**
	 * �˳���½
	 * @param xml String xml�ַ���
	 * @return ���ر����µĵ�½��ϢID���Ƿ�ɹ���־��ɵ�XML
	 * 0x0F
	 */
	public String updateLoginInfo(String xml) {
		DataUtil du = new DataUtil(messageKey);
		String[] sql = du.updateZddlxxInfo(xml);
		String[] keyValue = du.getKeyValue(xml);
		String[] flag = new String[sql.length];
		for(int i=0;i<sql.length;i++){
			if(!sql[i].equals("")&&sql[i]!=null)
				flag[i] = du.putSql(sql[i]);
			else 
				flag[i] = "�˳���¼��SQL���û�����ɳɹ�";
		}
		return du.generateXML(keyValue,flag);
	}
	
	/**
	 * �����ն���Ϣ
	 * @param xml String xml�ַ���
	 * @return ���ر����µĵ�¼��ϢID���Ƿ�ɹ���־��ɵ�XML
	 * 0x10
	 */
	public String updateTerminal(String xml){
		DataUtil du = new DataUtil(messageKey);
//		String[] sql = null;
		String[] sql = du.updateZdsxInfo(xml);
		String[] keyValue = du.getKeyValue(xml);
		String[] flag = new String[sql.length];
		for(int i=0;i<sql.length;i++){
			if(!sql[i].equals("")&&sql[i]!=null)
				flag[i] = du.putSql(sql[i]);
			else 
				flag[i] = "�޸��ն���Ϣ��SQL���û�����ɳɹ�";
		}
		return du.generateXML(keyValue,flag);
	}
	
	/**
	 * ��ȡ�ն��յ���Ϣ��ȷ������֡����
	 * �����ݿ����commit
	 * @return ����֡Ψһ��ʶ
	 * 0xFE
	 */
	public String doConfirm(){
		TransactionPool.getInstance().commit(messageKey, true);
		return null;
	}
}
