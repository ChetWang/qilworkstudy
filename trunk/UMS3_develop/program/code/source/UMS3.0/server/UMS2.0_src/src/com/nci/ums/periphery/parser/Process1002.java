package com.nci.ums.periphery.parser;

import java.util.*;
import java.sql.*;
import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;
import com.nci.ums.channel.outchannel.Filter;
import com.nci.ums.channel.channelinfo.NCIDataLockFlag;
import com.nci.ums.channel.channelinfo.YDDataLockFlag;
import com.nci.ums.channel.channelinfo.LTDataLockFlag;

/**
 * 1002交易的处理层
 */
public class Process1002 implements Processor {

	private Map map;
	private YDDataLockFlag ydDataLockFlag;
	private LTDataLockFlag ltDataLockFlag;
	private NCIDataLockFlag nciDataLockFlag;
	private SerialNO serialObj = SerialNO.getInstance();

	public String process(Map map, int count, String serial)
			throws ProcessException {
		this.map = map;
		ydDataLockFlag = YDDataLockFlag.getInstance();
		ltDataLockFlag = LTDataLockFlag.getInstance();
		nciDataLockFlag = NCIDataLockFlag.getInstance();
		return processOne(map, count, serial);
	}

	/**
	 * 处理单笔提交交易
	 */
	public String processOne(Map map, int count, String serial)
			throws ProcessException {
		DBConnect db = null;
		int serialNO = 0;
		ResultSet rs = null;
		ProcessException pex = null;
		String ret = "0001";
		// 分切手机号码
		String[] recv = getMobilePhone(value("UMS_TO"));
		String src = "";
		String replyDes = "";
		String insertSQL = "";
		try {
			db = getConn();
			db.transBegin();
			String batchNO = Util.getCurrentTimeStr("yyyyMMddHHmmss");

			StringBuffer LTRecv = new StringBuffer(); // 联通手机
			StringBuffer YDRecv = new StringBuffer(); // 移动手机
			int LTCount = 0;
			int YDCount = 0;

			// 取出待发送表中取出手机号码
			for (int i = 0; i < recv.length; i++) {
				recv[i] = recv[i].trim();
				if (Util.isNumeric(recv[i])) {
					String mediaID = getMediaID(recv[i]);
					if (mediaID.length() > 0) {
						// 移动、联动手机判断
						if (mediaID.trim().equalsIgnoreCase("013")) {
							if (YDCount == 0) {
								YDRecv.append(recv[i]);
							} else {
								YDRecv.append(",").append(recv[i]);
							}
							YDCount = YDCount + 1;
							recv[i] = "";
						} else if (mediaID.trim().equalsIgnoreCase("011")) {
							if (LTCount == 0) {
								LTRecv.append(recv[i]);
							} else {
								LTRecv.append(",").append(recv[i]);
							}
							LTCount = LTCount + 1;
						} // 移动、联动手机判断结束
					} // 是否为联通或移动手机
				}
			} // 取手机号码结束
			// 若手机号码验证不通过
			if (YDRecv.toString().trim().length() == 0
					&& LTRecv.toString().trim().length() == 0) {
				throw new ProcessException("1028", "不存在合法手机号码");
			}

			// 验证消息内容是否非法.若不合法，向out_error插入一条信息，若合法向out_ready写消息
			if (!Filter.filter(value("MSG"))) {
				// 取得应用程序有关服务号、计费内容
				Application app = getSrc(value("APP").trim());
				int fee = 0;
				int feeType = 0;
				if (app != null) {
					src = app.getSpNO();
					fee = app.getFee();
					feeType = app.getFeeType();
				}
				// 向待发送表中增加移动短消息
				String subApp = value("SUBAPP").trim();

				if (YDRecv.toString().trim().length() > 0) {
					// 移动src
					StringBuffer YDsrc = new StringBuffer(MobileCheck
							.getChinaMobileMap().get("UMSCenter_cm").toString());
					YDsrc.append(src);
					if (subApp.length() > 0) {
						YDsrc.append(subApp);
					}
					// 若需回复或回执，获得回复号
					if (value("ACK").trim().equalsIgnoreCase("2")
							|| value("ACK").trim().equalsIgnoreCase("3")) {
						replyDes = serialObj.getReply();
					}
					insertSQL = getOutReady(batchNO, serial, String
							.valueOf(serialNO), replyDes, "013", YDRecv
							.toString().trim(), YDsrc.toString(), feeType, fee);
					Res.log(Res.DEBUG, insertSQL);
					db.executeUpdate(insertSQL);
					ydDataLockFlag.setLockFlag(false);
					nciDataLockFlag.setLockFlag(false);
				}
				if (LTRecv.toString().trim().length() > 0) {
					// 联通src
					StringBuffer LTsrc = new StringBuffer("99881122");
					LTsrc.append(src);
					if (subApp.length() > 0)
						LTsrc.append(subApp);
					if (value("ACK").trim().equalsIgnoreCase("2")
							|| value("ACK").trim().equalsIgnoreCase("3"))
						replyDes = serialObj.getReply();
					serial = serialObj.getSerial();
					insertSQL = getOutReady(batchNO, serial, String
							.valueOf(serialNO), replyDes, "011", LTRecv
							.toString().trim(), LTsrc.toString(), feeType, fee);
					Res.log(1, insertSQL);
					db.executeUpdate(insertSQL);
					ltDataLockFlag.setLockFlag(false);
				}
				serialNO = serialNO + 1;
				// 向待发送表中增加联动短消息
				replyDes = "";

				ret = "0000";
				db.commit();
			} else {
				// 插入一条错误消息
				insertSQL = getOutError(batchNO, serial, String
						.valueOf(serialNO), replyDes, "010", LTRecv.toString()
						.trim(), src);
				// Res.log(Res.ERROR,insertSQL);
				db.executeUpdate(insertSQL);
				db.commit();
				throw new ProcessException("1029", "存在不合法信息");
			}

			// 调试Res.log(Res.DEBUG,insertSQL);
		} catch (SQLException ex) {
			Res.log(Res.ERROR, "sql:" + insertSQL);
			Res.log(Res.ERROR, "处理交易1002出错:" + ex.toString());
			try {
				db.rollback();
				db.close();
			} catch (Exception e) {
			}
			Res.log(Res.ERROR, ex.toString());
			Res.logExceptionTrace(ex);
			throw new ProcessException("1092", Res.getStringFromCode("1092",
					"1001", serial, ex.getMessage()));
		} catch (Exception e) {
			// Res.log(Res.ERROR,"处理1002交易出错"+e);
			if (e instanceof ProcessException) {
				pex = (ProcessException) e;
			}else{
				e.printStackTrace();
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (db != null) {
				try {
					db.close();
				} catch (Exception e) {
				}
			}
		}
		if (pex != null) {
			throw pex;
		}
		return ret;
	}

	private DBConnect getConn() throws ProcessException {
		try {
			DBConnect db = new DBConnect();
			return db;
		} catch (Exception ex) {
			Res.log(Res.ERROR, "连接池出错" + ex);
			// Res.logExceptionTrace(ex);
			throw new ProcessException("1091", Res.getStringFromCode("1091", ex
					.getMessage()));
		}
	}

	private String value(String key) {
		if (map != null) {
			if ("TIMESETFLAG".equalsIgnoreCase(key)) {
				if (Res.getValue(map, key) == null
						|| Res.getValue(map, key).equals("")) {
					return "0";
				}
			}
			return Res.getValue(map, key);
		} else {
			return "";
		}
	}

	private String[] getMobilePhone(String desc) {
		String[] results;
		results = desc.split(",");
		return results;
	}

	private String getMediaID(String desc) {
		String result = "";
		if (desc.length() > 5) {
			desc = desc.substring(0, 3);
			// if (desc.equalsIgnoreCase("130") || desc.equalsIgnoreCase("131")
			// || desc.equalsIgnoreCase("132")
			// || desc.equalsIgnoreCase("133")) {
			// result = "011";
			// }
			Map mobileMap = MobileCheck.getChinaMobileMap();
			String mobileType = (String) mobileMap.get(desc);
			if (mobileType.equalsIgnoreCase("chinamobile")) {
				result = (String) mobileMap.get("chinamobile");
			} else if (mobileType.equalsIgnoreCase("chinaunicom")) {
				result = (String) mobileMap.get("chinaunicom");
			} else if(mobileType.equals("chinatelecom")){
				result = (String) mobileMap.get("chinatelecom");
			}
		}
		return result;
	}

	// 得到合法的信息插入语句
	private String getOutReady(String batchNO, String serialNO,
			String sequenceNO, String replyNO, String mediaID, String desc,
			String src, int feeType, int fee) {
		StringBuffer insertSQL = new StringBuffer("");
		insertSQL
				.append(
						"insert into out_ready(BatchNo,SerialNo,SequenceNo,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime, ack, replyDes,feeType,fee,reply,subApp) values('")
				.append(Util.replaceNULL(batchNO)).append("','").append(
						Util.replaceNULL(serialNO)).append("','").append(
						Util.replaceNULL(sequenceNO)).append("',0,'").append(
						Util.replaceNULL(value("APP"))).append("','").append(
						Util.replaceNULL(value("ID"))).append("','").append(
						Util.replaceNULL(mediaID)).append("','").append(
						Util.replaceNULL(src)).append("','").append(
						Util.replaceNULL(desc)).append("','").append(
						Util.getCurrentTimeStr("yyyyMMdd")).append("','")
				.append(Util.getCurrentTimeStr("HHmmss")).append("',");

		if (value("REP").length() == 0) {
			insertSQL.append("1").append(",0,");
		} else {
			insertSQL.append(value("REP")).append(",0,");
		}

		if (value("PRIORITY").length() == 0) {
			insertSQL.append("0").append(",0,'");
		} else {
			insertSQL.append(value("PRIORITY")).append(",0,'");
		}
		// Res.log(Res.DEBUG,value("MESSAGETYPE"));
		if (value("MESSAGETYPE").trim().equalsIgnoreCase("2")) {
			insertSQL.append("8").append("','").append(getMsg(value("MSG"), 2))
					.append("','");
		} else if (value("MESSAGETYPE").trim().equalsIgnoreCase("1")) {
			insertSQL.append("0").append("','").append(getMsg(value("MSG"), 1))
					.append("','");
		} else if (value("MESSAGETYPE").trim().equalsIgnoreCase("21")) {
			insertSQL.append("21").append("','").append(
					getMsg(value("MSG"), 21)).append("','");
		} else if (value("MESSAGETYPE").trim().equalsIgnoreCase("4")) {
			insertSQL.append("4").append("','").append(getMsg(value("MSG"), 4))
					.append("','");
		} else if (value("MESSAGETYPE").trim().equalsIgnoreCase("0")) {
			insertSQL.append("15").append("','")
					.append(getMsg(value("MSG"), 0)).append("','");
		} else if (value("MESSAGETYPE").trim().equalsIgnoreCase("12")) {
			insertSQL.append("15").append("','").append(
					getMsg(value("MSG"), 12)).append("','");
		} else {
			insertSQL.append("15").append("','")
					.append(getMsg(value("MSG"), 0)).append("','");
		}

		insertSQL.append(value("TIMESETFLAG")).append("','").append(
				value("SETDATE")).append("','").append(value("SETTIME"))
				.append("','").append(value("INVALIDDATE")).append("','")
				.append(value("INVALIDTIME")).append("','")
				.append(value("ACK")).append("','").append(replyNO)
				.append("',").append(feeType).append(",").append(fee).append(
						",'").append(value("REPLY")).append("','").append(
						value("SUBAPP")).append("')");
		return insertSQL.toString();
	}

	private String getMsg(String msg, int msgType) {
		String result = msg.trim();
		if (msgType != 4 && msgType != 21) {
			result = Util.convertHalfToFull(msg);
		}
		/*
		 * Res.log(Res.DEBUG,"msg:"+msg.getBytes().length);
		 * if(msg.getBytes().length>i) { System.arraycopy(msg.getBytes(), 0,
		 * result, 0, i); } Res.log(Res.DEBUG,"msg result:"+result);
		 */
		return result;
	}

	// 得到过滤不通过的非法信息插入语句
	private String getOutError(String batchNO, String serialNO,
			String sequenceNO, String replyNO, String mediaID, String desc,
			String src) {

		StringBuffer insertSQL = new StringBuffer("");

		insertSQL
				.append(
						"insert into out_error(BatchNo,SerialNo,SequenceNo,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime, ack, replyDes,retCode,errMsg) values('")
				.append(Util.replaceNULL(batchNO)).append("','").append(
						Util.replaceNULL(serialNO)).append("','").append(
						Util.replaceNULL(sequenceNO)).append("',0,'").append(
						Util.replaceNULL(value("APP"))).append("','").append(
						Util.replaceNULL(value("ID"))).append("','").append(
						Util.replaceNULL(mediaID)).append("','").append(
						Util.replaceNULL(src)).append("','").append(
						Util.replaceNULL(desc)).append("','").append(
						Util.getCurrentTimeStr("yyyyMMdd")).append("','")
				.append(Util.getCurrentTimeStr("HHmmss")).append("',");

		if (value("REP").length() == 0) {
			insertSQL.append("1").append(",0,");
		} else {
			insertSQL.append(value("REP")).append(",0,");
		}

		if (value("PRIORITY").length() == 0) {
			insertSQL.append("0").append(",0,'");
		} else {
			insertSQL.append(value("PRIORITY")).append(",0,'");
		}

		insertSQL.append("0").append("','").append(
				Util.convertHalfToFull(value("MSG"))).append("','").append(
				value("TIMESETFLAG")).append("','").append(value("SETDATE"))
				.append("','").append(value("SEtTIME")).append("','").append(
						value("INVALIDDATE")).append("','").append(
						value("INVALIDTIME")).append("','")
				.append(value("ACK")).append("','").append(replyNO).append(
						"','").append("0001").append("','").append("存在需过滤信息")
				.append("')");
		return insertSQL.toString();
	}

	// 取得发送方特服号
	public Application getSrc(String appID) throws ProcessException {
		Application app = AppFactory.getInstance().getApplication(appID);
		return app;
	}

	public String getReply(String mediaType) {
		Connection db = null;
		String ret = "";
		try {
			db = DriverManager.getConnection(DataBaseOp.getPoolName());
			CallableStatement cs = db.prepareCall("{ ? = call getReply() }");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.execute();
			ret = cs.getString(1);
			cs.close();
		} catch (Exception e) {
			Res.log(Res.ERROR, "取得回复号出错" + e);
			Res.logExceptionTrace(e);
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (Exception ex) {
				}
			}
		}

		int retLength = ret.length();
		for (int i = 5; i > retLength; i--) {
			ret = "0" + ret;
		}
		Res.log(Res.DEBUG, "取得回复号" + ret);
		return ret;
	}

	/*
	 * public String getReply(String mediaType) { Connection db=null; String ret
	 * = ""; try{ db = DriverManager.getConnection(DataBaseOp.getPoolName());
	 * CallableStatement cs = db.prepareCall("{ ? = call getReply() }");
	 * cs.registerOutParameter(1, Types.VARCHAR); cs.execute(); ret =
	 * cs.getString(1); cs.close(); }catch(Exception e){
	 * Res.log(Res.ERROR,"取得回复号出错"+e); }finally{
	 * if(db!=null)try{db.close();}catch(Exception ex){} }
	 * 
	 * int retLength=ret.length(); for(int i=5;i>retLength;i--) ret="0"+ret;
	 * Res.log(Res.DEBUG,"取得回复号"+ret); return ret; }
	 */
	public static void main(String[] args) {
		Process1002 process = new Process1002();
		System.out.println(process.getReply("YD"));
	}
}