/**
 * <p>Title: OutChannel.java</p>
 * <p>Description:
 *    �����࣬�ṩ�������������������̳У�ʵ����ChannelIfc�ӿڡ�
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.channel.outchannel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.CMPPThreadLockFlag;
import com.nci.ums.channel.channelinfo.LCSThreadLockFlag;
import com.nci.ums.channel.channelinfo.LTDataLockFlag;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MM7ThreadLockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelinfo.NCIDataLockFlag;
import com.nci.ums.channel.channelinfo.NCIThreadLockFlag;
import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.channel.channelinfo.SGIPThreadLockFlag;
import com.nci.ums.channel.channelinfo.YDDataLockFlag;
import com.nci.ums.periphery.parser.Process1002;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.Res;
import com.nci.ums.util.SerialNO;
import com.nci.ums.util.Util;

public abstract class OutChannel implements ChannelIfc, Runnable {

	// ȫ���߳��˳���־�����������
	protected QuitLockFlag quitFlag;
	protected int getMsgCounts = 0;
	protected YDDataLockFlag ydDataLockFlag;
	protected LTDataLockFlag ltDataLockFlag;
	protected NCIDataLockFlag nciDataLockFlag;
	protected CMPPThreadLockFlag cmppThreadLockFlag;
	protected SGIPThreadLockFlag sgipThreadLockFlag;
	protected MM7ThreadLockFlag mm7ThreadLockFlag;
	protected NCIThreadLockFlag nciThreadLockFlag;
	protected LCSThreadLockFlag lcsThreadLockFlag;
	// ���߳��˳���־
	protected boolean stop = false;
	// ������Ϣ
	protected MediaInfo mediaInfo;
	// �̱߳���
	protected Thread runner;

	protected boolean startOnce = false;

	// ���Դ��롣

	/*
	 * ���캯��
	 */
	public OutChannel(MediaInfo mediaInfo) {
		quitFlag = QuitLockFlag.getInstance();
		ydDataLockFlag = YDDataLockFlag.getInstance();
		ltDataLockFlag = LTDataLockFlag.getInstance();
		nciDataLockFlag = NCIDataLockFlag.getInstance();
		cmppThreadLockFlag = CMPPThreadLockFlag.getInstance();
		sgipThreadLockFlag = SGIPThreadLockFlag.getInstance();
		mm7ThreadLockFlag = MM7ThreadLockFlag.getInstance();
		nciThreadLockFlag = NCIThreadLockFlag.getInstance();
		lcsThreadLockFlag = LCSThreadLockFlag.getInstance();
		this.mediaInfo = mediaInfo;
		Res.log(Res.INFO, mediaInfo.getMediaName() + "�����̲߳�����");
	}

	protected int getMsgInfos(MsgInfo[] result) {
		int count = 0;
		DBConnect db = null;
		ResultSet rs = null;
		// String currentDateTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		// String currentDate = currentDateTime.substring(0, 8);
		// String currentTime = currentDateTime.substring(8, 14);
		// System.out.println("��Ҫ�����ж�" + !nciDataLockFlag.getLockFlag()
		// + !nciThreadLockFlag.getLockFlag());
		if ((!nciDataLockFlag.getLockFlag()
				&& mediaInfo.getMediaId().equalsIgnoreCase("013") && !nciThreadLockFlag
				.getLockFlag())
				|| (!ydDataLockFlag.getLockFlag()
						&& mediaInfo.getMediaId().equalsIgnoreCase("012") && !mm7ThreadLockFlag
						.getLockFlag())
				|| (!ydDataLockFlag.getLockFlag()
						&& mediaInfo.getMediaId().equalsIgnoreCase("010") && !cmppThreadLockFlag
						.getLockFlag())
				|| (!ltDataLockFlag.getLockFlag()
						&& mediaInfo.getMediaId().equalsIgnoreCase("011") && !sgipThreadLockFlag
						.getLockFlag())) {
			if (mediaInfo.getMediaId().equalsIgnoreCase("013")) {
				nciThreadLockFlag.setLockFlag(true);
			} else if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
				cmppThreadLockFlag.setLockFlag(true);
			} else if (mediaInfo.getMediaId().equalsIgnoreCase("012")) {
				mm7ThreadLockFlag.setLockFlag(true);
			} else {
				sgipThreadLockFlag.setLockFlag(true);
			}
			getMsgCounts = getMsgCounts + 1;
//			Connection conn = null;
			try {
				db = new DBConnect();
				String sql = Util.getDialect().getOutMsgSelectSQL_V2();
//				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				db.prepareStatement(sql);
//				PreparedStatement prep = conn.prepareStatement(sql);
				// Res.log(Res.DEBUG, sql);
				db.setString(1, mediaInfo.getMediaId());
//				prep.setString(1, mediaInfo.getMediaId());
				rs = db.executeQuery();
//				rs = prep.executeQuery();
				// �����Ϣ��Ϣ
				while (rs.next()) {
					MsgInfo msgInfo = new MsgInfo();
					msgInfo.setBatchNO(rs.getString("batchNO"));
					msgInfo.setSerialNO(rs.getInt("serialNO"));
					msgInfo.setSequenceNO(rs.getInt("sequenceNO"));
					msgInfo.setRecvID(rs.getString("recvId"));
					msgInfo.setSendID(rs.getString("sendID"));
					msgInfo.setAck(rs.getInt("ack"));
					msgInfo.setRetCode(0);
					msgInfo.setSubApp(rs.getString("subApp"));

					msgInfo.setAppId(rs.getString("appID"));
					msgInfo.setAppSerialNo(rs.getString("appSerialNO"));
					msgInfo.setBatchMode(rs.getString("batchMode"));
					msgInfo.setContentMode(rs.getInt("contentMode"));
					msgInfo.setInvalidDate(rs.getString("invalidDate"));
					msgInfo.setInvalidTime(rs.getString("invalidTime"));
					msgInfo.setOldSendId(rs.getString("sendID"));
					msgInfo.setMediaId(rs.getString("mediaID"));
					msgInfo.setPriority(rs.getString("priority"));
					msgInfo.setRep(rs.getInt("rep"));
					msgInfo.setReply(rs.getString("reply"));
					msgInfo.setReplyDes(rs.getString("replyDes"));
					msgInfo.setSetDate(rs.getString("setDate"));
					msgInfo.setSetTime(rs.getString("setTime"));
					msgInfo.setSubmitDate(rs.getString("submitDate"));
					msgInfo.setSubmitTime(rs.getString("submitTime"));
					msgInfo.setTimeSetFlag(rs.getString("timeSetFlag"));
					msgInfo.setFeeType(rs.getInt("feeType"));
					msgInfo.setFee(rs.getInt("fee"));
					msgInfo.setFile(getFileList(mediaInfo.getMediaId(), rs
							.getString("batchNO"), rs.getInt("serialNO")));
					if (rs.getString("replyDes") != null
							&& rs.getString("replyDes").length() > 0) {
						if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
							msgInfo.setSendID("08225"
									+ rs.getString("replyDes"));
						} else if (mediaInfo.getMediaId().equalsIgnoreCase(
								"013")) {
							msgInfo.setSendID("088571242"
									+ rs.getString("replyDes"));
						} else {
							msgInfo.setSendID("08225"
									+ rs.getString("replyDes"));
						}
					}

					if (msgInfo.getContentMode() != 4
							&& msgInfo.getContentMode() != 21) {
						msgInfo.setContent(Util.convertFullToHalf(rs.getString(
								"content").trim()));
					} else {
						msgInfo.setContent(rs.getString("content").trim());
					}
					msgInfo.setCount(0);
					msgInfo.setStatus(2);

					result[count] = msgInfo;
					db
							.executeUpdate("update out_ready set statusFlag=1 where batchNO='"
									+ rs.getString("batchNO")
									+ "' and serialNO="
									+ rs.getInt("serialNO")
									+ " and sequenceNO="
									+ rs.getInt("sequenceNO"));
					count = count + 1;
					// ����Ϣ����16��,���˳�
					if (count >= 15) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Res.log(Res.ERROR, "1091", e.getMessage());
			} finally {
				if (count > 0) {
					Res.log(Res.DEBUG, "ȡ����Ϣ����" + count);
				}
				try {
					if (rs != null) {
						rs.close();
					}
					if (db != null) {
						db.close();
					}
//					if(conn!=null){
//						conn.close();
//					}
				} catch (Exception e) {
					Res.log(Res.ERROR, "out channel getMsgs error:" + e);
					Res.logExceptionTrace(e);
				}
			}
		}
		// else{
		// Res.log(Res.INFO, mediaInfo.getMediaId()+"������...");
		// }

		// ����
		if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
			cmppThreadLockFlag.setLockFlag(false);
		}
		if (mediaInfo.getMediaId().equalsIgnoreCase("013")) {
			nciThreadLockFlag.setLockFlag(false);
		}
		if (mediaInfo.getMediaId().equalsIgnoreCase("012")) {
			mm7ThreadLockFlag.setLockFlag(false);
		} else {
			sgipThreadLockFlag.setLockFlag(false);
		}
		// ��������δȡ������,������̴��ڵȴ���
		if (count == 0 && getMsgCounts > 3) {
			getMsgCounts = 0;
			Res.log(Res.DEBUG, mediaInfo.getMediaId() + "����3��δȡ�����ݣ��ȴ�...");
			if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
				ydDataLockFlag.setLockFlag(true);
			} else if (mediaInfo.getMediaId().equalsIgnoreCase("013")) {
				nciDataLockFlag.setLockFlag(true);
			} else {
				ltDataLockFlag.setLockFlag(true);
			}
		} else {
			if (count > 0) {
				getMsgCounts = 0;
			}
		}

		return count;
	}

	protected void OpData(int result, MsgInfo msgInfo, String msgID) {
		String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		String finishDate = currentTime.substring(0, 8);
		String finishTime = currentTime.substring(8, 14);
		StringBuffer insertSQL = new StringBuffer();
		StringBuffer tempSQL = new StringBuffer();
		DBConnect db = null;
		ResultSet rs = null;
		String tableName = currentTime.substring(0, 6);

		int count = 0;
		try {
			db = new DBConnect();
			// db.transBegin();
			// ��ʾ���ͳɹ�����Ҫ�޸�finishDate,finishTime,count
			// Ȼ���͵�ok���У�����ready����ɾ��������¼
			String ret_code = "0000";
			String err_msg = "���ͳɹ�";
			double fee = 0;
			if (result == 40) {
				ret_code = "0040";
				err_msg = "���Ϸ�����";
			} else if (result == 42) {
				ret_code = "0042";
				err_msg = "�޴�Ȩ��";
			} else if (result == -9922) {
				ret_code = "9922";
				err_msg = "�ظ���Ϣ���ͣ�UMS����";
			}

			StringBuffer delSQL = new StringBuffer(
					"delete from out_ready where batchNO='").append(
					msgInfo.getBatchNO()).append("' and SerialNo = ").append(
					msgInfo.getSerialNO());
			delSQL.append(" and  SequenceNo=").append(msgInfo.getSequenceNO());
			// Res.log(Res.DEBUG, "delSQL=" + delSQL);
			db.executeUpdate(delSQL.toString());
			// Res.log(Res.DEBUG, "�ɹ���out_ready����ɾ��һ����¼��");

			insertSQL
					.append("insert into out_ok(BatchNo,SerialNo,SequenceNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime, ack, replyDes,reply,feeType,fee,subApp,msgID) values('");
			tempSQL.append(Util.replaceNULL(msgInfo.getBatchNO())).append("',")
					.append(msgInfo.getSerialNO()).append(",").append(
							msgInfo.getSequenceNO()).append(",'").append(
							ret_code).append("','").append(err_msg)
					.append("',").append("0").append(",'").append(
							msgInfo.getAppId()).append("','").append(
							Util.replaceNULL(msgInfo.getAppSerialNo())).append(
							"','").append(msgInfo.getMediaId()).append("','")
					.append(Util.replaceNULL(msgInfo.getOldSendId())).append(
							"','")
					.append(Util.replaceNULL(msgInfo.getRecvID()))
					.append("','").append(
							Util.replaceNULL(msgInfo.getSubmitDate())).append(
							"','").append(
							Util.replaceNULL(msgInfo.getSubmitTime())).append(
							"','").append(Util.replaceNULL(finishDate)).append(
							"','").append(Util.replaceNULL(finishTime)).append(
							"',").append(msgInfo.getRep()).append(",").append(
							count).append(",").append(msgInfo.getPriority())
					.append(",'").append(
							Util.replaceNULL(msgInfo.getBatchMode())).append(
							"','").append(msgInfo.getContentMode()).append(
							"','").append(
							Util.convertHalfToFull(msgInfo.getContent()))
					.append("','").append(
							Util.replaceNULL(msgInfo.getTimeSetFlag())).append(
							"','").append(
							Util.replaceNULL(msgInfo.getSetDate())).append(
							"','").append(
							Util.replaceNULL(msgInfo.getSetTime())).append(
							"','").append(
							Util.replaceNULL(msgInfo.getInvalidDate())).append(
							"','").append(
							Util.replaceNULL(msgInfo.getInvalidTime())).append(
							"',").append(msgInfo.getAck()).append(",'").append(
							Util.replaceNULL(msgInfo.getReplyDes())).append(
							"','").append(Util.replaceNULL(msgInfo.getReply()))
					.append("',").append(msgInfo.getFeeType()).append(",")
					.append(msgInfo.getFee()).append(",'").append(
							Util.replaceNULL(msgInfo.getSubApp()))
					.append("','").append(Util.replaceNULL(msgID)).append("')");
			// Res.log(Res.DEBUG, insertSQL.toString() + tempSQL.toString());
			db.executeUpdate(insertSQL.toString() + tempSQL.toString());
			// Res.log(Res.DEBUG, "�ɹ���out_ok��������һ����¼!" + msgInfo.getReplyDes());

			if (result == 0 && msgInfo.getAck() != 0) {
				// ��ͨ�����������ֱִ�Ӹ���ִ
				if ((mediaInfo.getMediaId().equalsIgnoreCase("013") || mediaInfo
						.getMediaId().equalsIgnoreCase("ԭ����Ϊ13�����ھ��ò�Ӧ���ڴ�"))
						&& ((msgInfo.getAck() == 1 || msgInfo.getAck() == 3))) {
					// 3
					// ���ڲ��ж�
					if (msgInfo.getReplyDes().length() > 0
							&& msgInfo.getReply().startsWith("1")) {

						HashMap outMsg = new HashMap();
						outMsg.put("APP", msgInfo.getAppId());
						outMsg.put("ID", msgInfo.getAppSerialNo());
						outMsg.put("MSG", msgInfo.getRecvID() + ":�ɹ�����");
						outMsg.put("UMS_TO", msgInfo.getReply());
						outMsg.put("ACK", new String(Integer.toString(0))); // --
						// �ź����޸ģ�Ϊ�����㹫˾������Ҫ
						// outMsg.put("ACK","0");
						outMsg.put("MESSAGETYPE", new String(Integer
								.toString(0)));
						Res.log(Res.DEBUG, "����ת������");
						Process1002 process = new Process1002();
						process.process(outMsg, 1, SerialNO.getInstance()
								.getSerial());
					} else {
						insertSQL = new StringBuffer("");
						insertSQL = insertSQL
								.append(
										"insert into in_ready(BatchNo,SerialNo,sequenceNO,retCode,errMsg,statusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,content,ack,reply,subApp)")
								.append(" values('").append(currentTime)
								.append("',").append(
										SerialNO.getInstance().getSerial())
								.append(",0,'1001','',0,'").append(
										msgInfo.getAppId()).append("','")
								.append(msgInfo.getAppSerialNo()).append("','")
								.append(msgInfo.getMediaId()).append("','")
								.append(msgInfo.getOldSendId()).append("','")
								.append(msgInfo.getRecvID()).append("','")
								.append(finishDate).append("','").append(
										finishTime).append("','','','").append(
										"��ִ:�ɹ����� ").append("',").append(1)
								.append(",'").append(msgInfo.getReply())
								.append("','").append(msgInfo.getSubApp())
								.append("')");
						Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
						db.executeUpdate(insertSQL.toString());
						Res.log(Res.DEBUG, "�ɹ�����һ����ִ��Ϣ!");
						if (msgInfo.getAck() == 1) {
							db
									.executeUpdate("delete from out_reply where replydes='"
											+ msgInfo.getReplyDes() + "'");
						}
						Res.log(Res.DEBUG, "ɾ��out_reply���о���ͬһ�ظ��ŵļ�¼!");
					}
				}

				if (mediaInfo.getMediaId().equalsIgnoreCase("013")
						&& (msgInfo.getAck() == 2)) {
					db.executeUpdate("delete from out_reply where replydes='"
							+ msgInfo.getReplyDes() + "'");
					Res.log(Res.DEBUG, "ɾ��out_reply���о���ͬһ�ظ��ŵļ�¼!");
					insertSQL = new StringBuffer(
							"insert into out_reply(BatchNo,SerialNo,SequenceNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime, ack, replyDes,reply,feeType,fee,subApp,msgID) values('");
					Res.log(Res.DEBUG, "tempSQL=" + tempSQL);
					db.executeUpdate(insertSQL.toString() + tempSQL.toString());
				}
			}

			// db.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (db != null) {
					db.close();
				}
			} catch (Exception e) {
			}
		}
	}

	protected void endSubmit(MsgInfo[] msgInfos, int count) {
		// DBConnect db=null;
		// try {
		// db=new DBConnect();
		// boolean firstErrorFlag=false;
		// for(int i=0;i<count;i++)
		// {
		// if(msgInfos[i].getStatus()==1)
		// {
		// if(i==0)firstErrorFlag=true;
		// if((msgInfos[i].getRetCode()==27||msgInfos[i].getRetCode()==50||msgInfos[i].getRetCode()==93||msgInfos[i].getRetCode()==172||msgInfos[i].getRetCode()==88)&&mediaInfo.getMediaId().equalsIgnoreCase("011"))
		// {
		// //10���Ӻ��ط�
		// String currentTime = Util.addMinute("yyyyMMddHHmmss",2);
		// String setDate = currentTime.substring(0, 8);
		// String setTime = currentTime.substring(8, 14);
		// db.prepareStatement("update out_ready set
		// statusFlag=0,retCode=?,setDate=?,setTime=? where batchNO=? and
		// serialNO=?");
		// db.setInt(1,msgInfos[i].getRetCode());
		// db.setString(2,setDate);
		// db.setString(3,setTime);
		// db.setString(4,msgInfos[i].getBatchNO());
		// db.setInt(5,msgInfos[i].getSerialNO());
		// }else{
		// if(firstErrorFlag&&i>0){
		// db.prepareStatement("update out_ready set statusFlag=0,retCode=?
		// where batchNO=? and serialNO=?");
		// }else{
		// db.prepareStatement("update out_ready set
		// statusFlag=0,doCount=doCount+1,retCode=? where batchNO=? and
		// serialNO=?");
		// }
		// db.setInt(1,msgInfos[i].getRetCode());
		// db.setString(2,msgInfos[i].getBatchNO());
		// db.setInt(3,msgInfos[i].getSerialNO());
		// }
		// db.executeUpdate();
		// }else if(msgInfos[i].getStatus()==2) {
		// db.prepareStatement("update out_ready set statusFlag=0,retCode=?
		// where batchNO=? and serialNO=?");
		// db.setInt(1,msgInfos[i].getRetCode());
		// db.setString(2,msgInfos[i].getBatchNO());
		// db.setInt(3,msgInfos[i].getSerialNO());
		// db.executeUpdate();
		// }
		// }
		// }catch(Exception e)
		// {
		// Res.log(Res.ERROR,"����������Ϣ��������"+e);
		// }finally{
		// if(db!=null)try{db.close();}catch(Exception e){}
		// }
	}

	/*
	 * ���߳̿�ʼ�����ĺ�����
	 */
	public void start() {
		if (!stop) {
			runner = new Thread(this, mediaInfo.getMediaName());
			runner.start();
			Res.log(Res.INFO, mediaInfo.getMediaName() + "�Ⲧ�����߳���������"
					+ Util.getCurrentTimeStr("yyyyMMddHHmmss"));
		} else {
			if (runner != null) {
				stop = false;
				runner.start();
			}
		}

		startOnce = true;
	}

	/*
	 * ֹͣ����߳�
	 */
	public void stop() {
		stop = true;
		Res.log(Res.INFO, mediaInfo.getMediaName() + "�Ⲧ�����߳��˳�����");
	}

	public boolean isStartOnce() {
		return this.startOnce;
	}

	/*
	 * ɨ�����ݿ��ĺ������ṩ���ݼ� ��Ҫɨ����ͨ��Ϣ�ģ����ݼ���Ͷ�ʱ��־����ɨ��ʧЧ����Ϣ
	 * ���������źͷ����һ��ɨ�裬��ֹ�����Ѿ�ʧЧ�˵ķ������Ϣ��ɨ���������������ˮ�������� ����ɨ���Ժ󣬵���process���������д���
	 */
	protected void sleepTime(int sleepTime) {
		try {
			// ���ݵõ�����Ϣ������ȷ��sleep��ʱ�䡣
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Res.log(Res.INFO, "�̱߳��ж�");
			stop();
		}
	}

	/*
	 * �����̵߳�״̬���������Ϊtrue ��ʾ�ֳɻ��������У����Ϊfalse,��ʾ�߳��Ѿ�ֹͣ��
	 */
	public boolean getThreadState() {
		return !stop;
	}

	public void UpdateMsgId(String sBatchNo, int nSerialNo, String nMsgId) {
		String SQL = "UPDATE OUT_READY SET MSGID = '" + nMsgId
				+ "'  WHERE batchno = '" + sBatchNo + "'  AND serialno="
				+ nSerialNo;
		DBConnect db = null;

		try {
			db = new DBConnect();
			db.executeUpdate(SQL);
		} catch (Exception e) {
			System.out.println("�޸�MSGID �������� :" + e);
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Res.log(Res.ERROR, "�޸�MSGID�ر����ݿ����ӳ���!" + e);
				Res.logExceptionTrace(e);
			}
		}
	}

	public List getFileList(String sMedia, String sBatchNo, int sSerialNO) {
		List file = new ArrayList();
		DBConnect db = null;
		ResultSet rs = null;

		StringBuffer sb = new StringBuffer(
				"select targetfilename from  in_out_attachments   where batchno = '"
						+ sBatchNo + "'  and serialno = " + sSerialNO);
		try {
			db = new DBConnect();
			rs = db.executeQuery(sb.toString());

			while (rs.next()) {
				file.add((String) rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println("ȡ�ļ���������: " + e);
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Res.log(Res.ERROR, "ȡ�ļ��ر����ݿ����ӳ���!" + e);
				Res.logExceptionTrace(e);
			}
		}

		return file;
	}

	public LockFlag getDataLockFlag() {
		// TODO Auto-generated method stub
		return null;
	}

}