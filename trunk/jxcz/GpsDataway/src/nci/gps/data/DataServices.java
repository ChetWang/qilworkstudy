package nci.gps.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import nci.gps.util.TransactionPool;


/**
 * @功能：终端15个数据操作请求服务
 * @时间：2008-07-23
 * @author ZHOUHM
 * 
 */
public class DataServices {
	private String messageKey;	// 数据帧唯一标识
	public DataServices(String messageKey){
		this.messageKey = messageKey;
	}

	/**
	 * 获取代码
	 * @return
	 * 0x01
	 */
	public String selectLSCode() {
		String sql = "select SYMBOL,StandardName,Code,Name from TB_GPS_CZ_LS_DMB";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * 获取部门
	 * @return
	 * 0x02
	 */
	public String selectLSDept() {
		String sql = "select BRANCH_CODE,BRANCH_NAME from TB_GPS_CZ_LS_BM";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * 获取人员
	 * @return
	 * 0x03
	 */
	public String selectLSPerson() {
		String sql = "select WORK_NO,Name,Dept_Code from TB_GPS_CZ_LS_RY";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/***
	 * 获取驾驶员信息
	 * @return
	 * 0x04
	 */
	public String selectDriver() {
		String sql = "select ID,NO,NAME from TB_GPS_CZ_JSYXX";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * 获取车辆信息
	 * @return
	 * 0x05
	 */
	public String selectLSCar() {
		String sql = "select ID,NO, DriverID,DriverName,TerminalID,TerminalNO from TB_GPS_CZ_CLXX";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}
	
	/**
	 * 获取终端信息
	 * @return
	 * 0x11
	 */
	public String selectTerminal(){
		String sql = "SELECT ID,NO,CALLNO,NAME,CLID FROM TB_GPS_CZ_ZDXX";
		DataUtil du = new DataUtil(messageKey);
		return du.getResultSet(sql);
	}

	/**
	 * 获取工单数据,终端还未接收的工单并且是已经派发的工单（LCZT='05'）
	 * @param xml String xml字符串
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
	 * 获取服务器当前时间，精确到分
	 * @return
	 * 0x0C
	 */
	public String getCurrentTime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * 工单接单派工,即更新工单中的派工信息,TB_GPS_CZ_GD
	 * 工单到达现场，修改工单状态
	 * 故障处理完毕
	 * @param xml String xml字符串
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
				flag[i] = "修改工单状态的SQL语句没有生成成功";
		}
		return du.generateXML(keyValue,flag);
	}

	/**
	 * 新增工单信息
	 * @param xml String xml字符串
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
	 * 新增工单处理信息，XML中如果大于0为成功
	 * @param xml String xml字符串
	 * @return 0x09
	 */
	public String createGdclgcInfo(String xml) {
		DataUtil du = new DataUtil(messageKey);
		return du.generateXML("result",du.putSql(du.setGdclgcInfo(xml)));
	}

	/**
	 * 从GPS中新增处理坐标
	 * @param xml String xml字符串
	 * @return 
	 * 0x0A
	 */
	public String createClzbInfo(String xml) {
		DataUtil du = new DataUtil(messageKey);
		return du.generateXML("result",du.putSql(du.setClzbInfo(xml)));
	}
	
	/**
	 * 调用LS接口
	 * @param xml String xml字符串
	 * @return 
	 * 0x0B
	 */
	public String setLsWebServer(String xml) {
//		直接调用LSWebservic	
		DataUtil du = new DataUtil(messageKey);
		String[] flag = null;
		flag = du.setLSInfo(xml);
		String[] keyValue = du.getGdhValue(xml);
		String temp = du.generateXML(keyValue,flag);
		return temp;	
	}
	
	/**
	 * 创建登陆信息
	 * @param xml String xml字符串
	 * @return 返回id，错误就返回0
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
	 * 更新车辆信息
	 * @param clid String 车辆编号
	 * @return 成功>0
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
	 * 退出登陆
	 * @param xml String xml字符串
	 * @return 返回被更新的登陆信息ID和是否成功标志组成的XML
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
				flag[i] = "退出登录，SQL语句没有生成成功";
		}
		return du.generateXML(keyValue,flag);
	}
	
	/**
	 * 更新终端信息
	 * @param xml String xml字符串
	 * @return 返回被更新的登录信息ID和是否成功标志组成的XML
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
				flag[i] = "修改终端信息的SQL语句没有生成成功";
		}
		return du.generateXML(keyValue,flag);
	}
	
	/**
	 * 获取终端收到信息的确定数据帧操作
	 * 将数据库更新commit
	 * @return 数据帧唯一标识
	 * 0xFE
	 */
	public String doConfirm(){
		TransactionPool.getInstance().commit(messageKey, true);
		return null;
	}
}
