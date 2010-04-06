package com.nci.ums.periphery.core;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.nci.ums.channel.outchannel.MsgInfo;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

public class CacheDataHelper {

    private static HashMap appMsgData = new HashMap();
    private static HashMap appLoginInfo = new HashMap();
    private static HashMap waitResponseMsgs = new HashMap();
    private static HashMap ipLoginInfo = new HashMap();

    public static synchronized MsgInfo getMsg(String appID, String subAppID) {

        MsgInfo result = null;

        if (subAppID == null) {
            subAppID = "";
        }
        StringBuffer appName = new StringBuffer(appID).append(subAppID);
        List msgs = (List) appMsgData.get(appName.toString());
        //�Ӵ�����map�л�õ�һ����Ϣ
        if (msgs != null) {
            if (msgs.size() > 0) {
                result = (MsgInfo) msgs.get(0);
            }
        } else {
            //��һ�ε�¼��ֱ�Ӵ����ݿ��л�ȡ
            getNextMsgs(appID, subAppID);
            msgs = (List) appMsgData.get(appName.toString());
            //�Ӵ�����map�л�õ�һ����Ϣ
            if (msgs != null) {
                if (msgs.size() > 0) {
                    result = (MsgInfo) msgs.get(0);
                }
            }
        }
        //���û����Ϣ
        if (result == null) {
            //�ж��Ƿ�Ҫ�����ݿ��ж�ȡ
            if (checkTimeInvalid(appID, subAppID)) {
                getNextMsgs(appID, subAppID);
            }
        } else {
            //�������Ϣ������Ϣ���ز�����Ϣ������ظ�map
            if (msgs.size() > 0) {
                msgs.remove(0);
            }
            waitResponseMsgs.put(result.getBatchNO() + result.getSerialNO(), result);
        }
        return result;
    }

    public static synchronized void response(String batchNO, String serialNO) {
        MsgInfo msgInfo = getResponseMsg(batchNO, serialNO);
        if (msgInfo != null) {
            OpData(msgInfo);
        }
    }

    private static synchronized void OpData(MsgInfo msgInfo) {
        String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
        String finishDate = currentTime.substring(0, 8);
        String finishTime = currentTime.substring(8, 14);
        StringBuffer insertSQL = new StringBuffer();
        //��ĸ�����
        //StringBuffer tableName=new StringBuffer("in_ok_").append(currentTime.substring(0, 6));
        //������sql���

        DBConnect db = null;
        try {
            //��ʾ���ͳɹ�����Ҫ�޸�finishDate,finishTime,count
            //Ȼ���͵�ok���У�����ready����ɾ��������¼
            db = getConn();
            db.transBegin();

            /*int day=Integer.valueOf(currentTime.substring(6,8)).intValue();
            //���ɱ���������ÿ��10������ʼ�ջ��
            if((day>0&&day<=10)){
            tableName.append("01");
            }else if(day>10&&day<=20){
            tableName.append("11");
            }else{
            tableName.append("21");
            }
             */
            //��ʱ�Ļ�
            //tableName=new StringBuffer("in_ok");
            insertSQL.append("insert into in_ok(BatchNo,SerialNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,Content, ack, reply,msgType) values(\'").append(Util.replaceNULL(msgInfo.getBatchNO())).append("\',").append(msgInfo.getSerialNO()).append(",\'").append(msgInfo.getRetCode()).append("\',\'").append("���ͳɹ�").append("\',0").append(",\'").append(msgInfo.getAppId()).append("\',\'").append(Util.replaceNULL(msgInfo.getAppSerialNo())).append("\',\'").append(msgInfo.getMediaId()).append("\',\'").append(msgInfo.getSendID()).append("\',\'").append(msgInfo.getRecvID()).append("\',\'").append(Util.replaceNULL(msgInfo.getSubmitDate())).append("\',\'").append(Util.replaceNULL(msgInfo.getSubmitTime())).append("\',\'").append(Util.replaceNULL(finishDate)).append("\',\'").append(Util.replaceNULL(finishTime)).append("\',\'").append(msgInfo.getContent()).append("\',").append(Util.replaceNULL(Integer.toString(msgInfo.getAck()))).append(",\'").append(Util.replaceNULL(msgInfo.getReply())).append("\',").append(Util.replaceNULL(Integer.toString(msgInfo.getContentMode()))).append(")");

            //Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
            //TimerStep t1=new TimerStep("out_ok��������һ����¼����ʱ��");
            db.executeUpdate(insertSQL.toString());
            //t1.TimerStop();
            //Res.log(Res.DEBUG, "�ɹ���in_ok��������һ����¼!");
            StringBuffer delSQL = new StringBuffer();
            delSQL = new StringBuffer("delete from in_ready where batchNO='").append(msgInfo.getBatchNO()).append("' and SerialNo = ").append(msgInfo.getSerialNO());
            //Res.log(Res.DEBUG, "delSQL=" + delSQL);
            db.executeUpdate(delSQL.toString());
            //Res.log(Res.DEBUG, "�ɹ���in_ready����ɾ��һ����¼��");
            db.commit();
        } catch (Exception e) {
            try {
                db.rollback();
            } catch (Exception ex) {
            }
            Res.log(Res.ERROR, "ɾ������" + e);
            Res.logExceptionTrace(e);
        } finally {
            if (db != null) {
                try {
                    db.close();
                    db = null;
                } catch (Exception ex) {
                }
            }
        }
    }

    public static IpLogin getIpLogin(String ip) {
        IpLogin result = (IpLogin) ipLoginInfo.get(ip);
        return result;
    }

    public static void setIpLogin(String ip) {
        IpLogin ipLogin = new IpLogin();
        ipLogin.setLoginTime(Util.getUTCTime());
        ipLogin.setStatus(true);
        ipLoginInfo.put(ip, ipLogin);
    }

    public static void removeIpLogin(String ip) {
        ipLoginInfo.remove(ip);
    }

    private static boolean checkTimeInvalid(String appID, String subAppID) {
        boolean result = true;
        StringBuffer appName = new StringBuffer(appID);
        if (subAppID != null && subAppID.length() > 0) {
            appName.append(subAppID);
        }

        AppLogin appLogin = (AppLogin) appLoginInfo.get(appName.toString());
        if (appLogin != null) {
            //�����ͣ�ȴ�ʱ���Ƿ�ʧЧ����û��ʧЧ���򲻴����ݿ��л�ȡ
            //System.out.println(Util.getUTCTime()+":"+appLogin.getLostTime());
            if (appLogin.getLostTime() != 0 && Util.getUTCTime() < appLogin.getLostTime() + 2) {
                result = false;
                //appLogin.setLostTime(0);
            } else {
                appLogin.setLostTime(0);
            }
        } else {
            appLogin = new AppLogin();
            appLogin.setAppID(appID);
            appLogin.setSubAppID("");
            appLogin.setLostTime(0);
            appLoginInfo.put(appName.toString(), appLogin);
        }
        return result;
    }

    /**
     * @param appID
     */
    private static synchronized void getNextMsgs(String appID, String subAppID) {
        // TODO Auto-generated method stub
        ResultSet rs = null;
        String result = "0001";
        DBConnect db = null;
        try {
            StringBuffer appName = new StringBuffer(appID).append(subAppID);
            db = getConn();
            List msgs = new ArrayList();
            StringBuffer sql = new StringBuffer(Util.getDialect().getCacheData_NextMsgSQL_V2());
            if (subAppID != null && subAppID.length() > 0) {
                sql.append(appID).append("' and subApp='").append(subAppID).append("' order by serialNO");
            } else {
                sql.append(appID).append("' order by serialNO");
            }
            rs = db.executeQuery(sql.toString());
            while (rs.next()) {
                MsgInfo msgInfo = new MsgInfo();
                msgInfo.setBatchNO(rs.getString("batchNO"));
                msgInfo.setSerialNO(rs.getInt("serialNO"));
                msgInfo.setRetCode(rs.getInt("retCode"));
                msgInfo.setAppId(rs.getString("appid"));
                msgInfo.setAppSerialNo(rs.getString("AppSerialNo"));
                msgInfo.setMediaId(rs.getString("mediaID"));
                msgInfo.setSendID(rs.getString("sendID"));
                msgInfo.setRecvID(rs.getString("recvID"));
                msgInfo.setSubmitDate(rs.getString("submitDate"));
                msgInfo.setSubmitTime(rs.getString("submitTime"));
                msgInfo.setContent(rs.getString("content"));
                msgInfo.setAck(rs.getInt("ack"));
                msgInfo.setReply(rs.getString("reply"));
                msgInfo.setContentMode(rs.getInt("msgType"));
                msgInfo.setSubApp(rs.getString("subApp"));
                msgs.add(msgInfo);
            }
            if (msgs.size() == 0) {
                AppLogin appLogin = (AppLogin) appLoginInfo.get(appName.toString());
                if (appLogin == null) {
                    appLogin = new AppLogin();
                    appLogin.setAppID(appID);
                    appLogin.setSubAppID(subAppID);
                }
                appLogin.setLostTime(Util.getUTCTime());
                appLoginInfo.put(appName.toString(), appLogin);
            } else {
                AppLogin appLogin = (AppLogin) appLoginInfo.get(appName.toString());
                if (appLogin == null) {
                    appLogin = new AppLogin();
                    appLogin.setAppID(appID);
                    appLogin.setSubAppID(subAppID);
                }
                appLogin.setLostTime(0);
                appMsgData.put(appName.toString(), msgs);
            }
        } catch (Exception e) {
            Res.log(Res.ERROR, e.getMessage());
            Res.logExceptionTrace(e);
        } finally {
            if (db != null) {
                try {
                    db.close();
                    db = null;
                } catch (Exception ex) {
                }
            }
        }
    }

    private static synchronized MsgInfo getResponseMsg(String batchNO, String serialNO) {
        // TODO Auto-generated method stub
        StringBuffer keyID = new StringBuffer(batchNO).append(serialNO);
        MsgInfo msgInfo = (MsgInfo) waitResponseMsgs.get(keyID.toString());
        DBConnect db = null;
        if (msgInfo != null) {
            if (waitResponseMsgs.size() > 1000) {
                waitResponseMsgs.clear();
            } else {
                waitResponseMsgs.remove(keyID);
            }
        } else {
            ResultSet rs = null;
            String result = "0001";
            try {
                db = getConn();
                List msgs = new ArrayList();
                StringBuffer sql = new StringBuffer("select * from in_ready where batchNO='").append(batchNO).append("' and serialNO=").append(serialNO).append(" limit 0,1");
                //StringBuffer sql=new StringBuffer("select * from in_ready where batchNO='").append(batchNO).append("' and serialNO=").append(serialNO);
                rs = db.executeQuery(sql.toString());
                if (rs.next()) {
                    msgInfo = new MsgInfo();
                    msgInfo.setBatchNO(rs.getString("batchNO"));
                    msgInfo.setSerialNO(rs.getInt("serialNO"));
                    msgInfo.setRetCode(rs.getInt("retCode"));
                    msgInfo.setAppId(rs.getString("appid"));
                    msgInfo.setAppSerialNo(rs.getString("AppSerialNo"));
                    msgInfo.setMediaId(rs.getString("mediaID"));
                    msgInfo.setSendID(rs.getString("sendID"));
                    msgInfo.setRecvID(rs.getString("recvID"));
                    msgInfo.setSubmitDate(rs.getString("submitDate"));
                    msgInfo.setSubmitTime(rs.getString("submitTime"));
                    msgInfo.setContent(rs.getString("content"));
                    msgInfo.setAck(rs.getInt("ack"));
                    msgInfo.setReply(rs.getString("reply"));
                    msgInfo.setContentMode(rs.getInt("msgType"));
                    msgInfo.setSubApp(rs.getString("subApp"));
                }
            } catch (Exception e) {
                Res.log(Res.ERROR, e.getMessage());
                Res.logExceptionTrace(e);
            } finally {
                if (db != null) {
                    try {
                        db.close();
                        db = null;
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return msgInfo;
    }

    private static synchronized DBConnect getConn() {
        try {
            DBConnect db = new DBConnect();
            return db;
        } catch (Exception ex) {
            Res.log(Res.ERROR, "���ӳس���" + ex);
            Res.logExceptionTrace(ex);
            return null;
        }
    }

}