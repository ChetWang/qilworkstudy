/**
 * <p>Title: AppChannel.java</p>
 * <p>Description:
 *    �����࣬�ṩ������Ӧ�ó��򲦳����������̳У�ʵ����Send�ӿڡ�
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 17 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.periphery.application;

import java.sql.*;
import java.util.HashMap;
import com.nci.ums.exception.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.ParserException;

public class AppServer extends Thread {

    // ȫ���߳��˳���־�����������
    protected QuitLockFlag quitFlag;
    // ���߳��˳���־
    protected boolean stop = false;
    private static HashMap appMap;

    /*
     * ���캯��
     */
    public AppServer() {
        quitFlag = QuitLockFlag.getInstance();
        Res.log(Res.INFO, "UMS2.0Ӧ�ó���ת�����ⲿӦ�ö��������");
    }

    /*
     * run�������ṩ�߳�����ʱ����Ҫ���� һֱѭ��ֱ�����жϻ����˳���־�����á� ÿ��ɨ��һ�α��Ժ󣬻�sleepһ��ʱ�䡣
     */
    public void run() {
        // �Ȱ��˳���־����Ϊfalse;
        stop = false;

        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
            try {
                // ��ʼ��Ӧ�ó���ע����Ϣ��
//                Res.log(Res.DEBUG, "UMSת����Ӧ�ó���Ĵ���ʼ");
                appMap = initAppMap();

                // ɨ���跢����Ӧ�ó������Ϣ
                if (appMap != null) {
                    scanTable();
                }
//                Res.log(Res.DEBUG, "UMSת����Ӧ�ó���Ĵ������");
            } catch (UMSConnectionException e) {
                e.printStackTrace();
            } finally {
                sleepTime(60);
            }
        }
    }

    /*
     * ��ʼ��Ӧ�ó���ע����Ϣ�� �Ա�������Ϣʱ����
     */
    private HashMap initAppMap() {
        DBConnect db = null;
        ResultSet rs = null;
        HashMap result = new HashMap();
        try {
            db = new DBConnect();
            db.prepareStatement("select * from application");
            rs = db.executeQuery();
            while (rs.next()) {
                AppInfo appInfo = new AppInfo();
                appInfo.setAppID(rs.getString("appID"));
                appInfo.setAppName(rs.getString("appName"));
                appInfo.setChannelType(rs.getInt("channelType"));
                appInfo.setIp(rs.getString("ip"));
                appInfo.setPort(rs.getInt("port"));
                appInfo.setLoginName(rs.getString("loginName"));
                appInfo.setLoginPwd(rs.getString("loginPwd"));
                appInfo.setObjectName(rs.getString("object"));
                appInfo.setSpNO(rs.getString("spNO"));
                appInfo.setEmail(rs.getString("email"));
                result.put(appInfo.getAppID(), appInfo);
            }
        } catch (Exception ex) {
            Res.log(Res.ERROR, "��ʼ��Ӧ�ó�����Ϣ����" + ex);
            Res.logExceptionTrace(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /*
     * ����������Ҫִ�з������ݼ��е�ÿһ����¼�����Ρ� ����ͼ�ͷ������������ӣ����ѭ����ν������ϡ�
     * ���ص�int��ֵΪ��ʾ������ݼ��з��͹����з��ͳɹ�����Ϣ����Ŀ��
     */
    public int process(ResultSet rs, Statement stmt) throws UMSConnectionException {

        // test code
        int i = 0; // ������¼���ͳɹ��ļ�¼��������
        try {
            while (rs.next()) {
                // ��Ҫ��content�Լ�adMsg,subMsg.
                int errCode = sendMsg(rs);
                // ��ʾ������¼���ͳɹ���
                if (errCode == 0) {
                    OpData(rs, stmt, errCode);
                    i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "1091", e.getMessage());
            Res.logExceptionTrace(e);
        } catch (UMSConnectionException e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "8002", "Ӧ�ó����Ⲧ����.");
            Res.logExceptionTrace(e);
            throw e;
        }
        return i;
    }

    /*
     * ���ݿ�������������ݴ�������ݼ���result��ȷ��Ҫ�������ݿ����
     *
     */
    protected void OpData(ResultSet rs, Statement stmt, int result) {
        String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
        String finishDate = currentTime.substring(0, 8);
        String finishTime = currentTime.substring(8, 14);
        StringBuffer insertSQL = new StringBuffer();

        int count = 0;
        try {
            // ��ʾ���ͳɹ�����Ҫ�޸�finishDate,finishTime,count
            // Ȼ���͵�ok���У�����ready����ɾ��������¼
            count = rs.getInt("docount");
            if (result == 0) {
                insertSQL.append("insert into in_ok(BatchNo,SerialNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,Content, ack, reply) values('").append(Util.replaceNULL(rs.getString("BatchNo"))).append("',").append(rs.getInt("SerialNo")).append(",'").append(rs.getString("retCode")).append("','").append("���ͳɹ�").append("',").append(rs.getString("StatusFlag")).append(",'").append(rs.getString("AppId")).append("','").append(Util.replaceNULL(rs.getString("AppSerialNo"))).append("','").append(rs.getString("MediaId")).append("','").append(rs.getString("SendId")).append("','").append(rs.getString("RecvId")).append("','").append(Util.replaceNULL(rs.getString("SubmitDate"))).append("','").append(Util.replaceNULL(rs.getString("SubmitTime"))).append("','").append(Util.replaceNULL(finishDate)).append("','").append(Util.replaceNULL(finishTime)).append("','").append(rs.getString("Content")).append("',").append(Util.replaceNULL(rs.getString("ack"))).append(",'").append(Util.replaceNULL(rs.getString("reply"))).append("')");

                Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
                // TimerStep t1=new TimerStep("out_ok��������һ����¼����ʱ��");
                stmt.executeUpdate(insertSQL.toString());
                // t1.TimerStop();
                Res.log(Res.DEBUG, "�ɹ���in_ok��������һ����¼!");
                StringBuffer delSQL = new StringBuffer();
                delSQL = new StringBuffer("delete from in_ready where batchNO='").append(rs.getString("batchNO")).append("' and SerialNo = ").append(rs.getInt("SerialNo"));
                Res.log(Res.DEBUG, "delSQL=" + delSQL);
                stmt.executeUpdate(delSQL.toString());
                Res.log(Res.DEBUG, "�ɹ���in_ready����ɾ��һ����¼��");
            } else {
                // ��ʾ�Ѿ����������õ���ֵ�����ڷ����ˡ�
                if (count > 3) {
                    String errMsg = "����ʧ�ܴ��������涨������";
                    insertSQL.append("insert into in_ok(BatchNo,SerialNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,Content, ack, reply) values('").append(Util.replaceNULL(rs.getString("BatchNo"))).append("',").append(rs.getInt("SerialNo")).append(",'").append("0001").append("','").append(errMsg).append("',").append(rs.getString("StatusFlag")).append(",'").append(rs.getString("AppId")).append("','").append(Util.replaceNULL(rs.getString("AppSerialNo"))).append("','").append(rs.getString("MediaId")).append("','").append(rs.getString("SendId")).append("','").append(rs.getString("RecvId")).append("','").append(Util.replaceNULL(rs.getString("SubmitDate"))).append("','").append(Util.replaceNULL(rs.getString("SubmitTime"))).append("','").append(Util.replaceNULL(finishDate)).append("','").append(Util.replaceNULL(finishTime)).append("','").append(rs.getString("Content")).append("',").append(Util.replaceNULL(rs.getString("ack"))).append(",'").append(Util.replaceNULL(rs.getString("reply"))).append("')");
                    Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
                    stmt.executeUpdate(insertSQL.toString());
                    Res.log(Res.DEBUG, "�ɹ���in_ok��������һ����¼!");
                    StringBuffer delSQL = new StringBuffer();
                    delSQL = new StringBuffer("delete from in_ready where SerialNo = ").append(rs.getInt("SerialNo"));

                    Res.log(Res.DEBUG, "delSQL=" + delSQL);
                    stmt.executeUpdate(delSQL.toString());
                    Res.log(Res.DEBUG, "�ɹ���in_ready����ɾ��һ����¼��");
                } else {
                    Res.log(Res.DEBUG, "���Ͳ��ɹ���");
                    StringBuffer updateSQL = new StringBuffer("update  in_ready set doCount =").append(count).append(" where SerialNo = ").append(rs.getInt("SerialNo"));
                    Res.log(Res.DEBUG, "updateSQL=" + updateSQL);
                    stmt.executeUpdate(updateSQL.toString());
                    Res.log(Res.DEBUG, "�ɹ���in_ready���и���һ����¼��");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "1091", e.getMessage());
            Res.logExceptionTrace(e);
        }
    }

    /*
     * ɨ�����ݿ��ĺ������ṩ���ݼ� ���������Ҫɨ�����û��ʧЧ�ģ���Ҫ���͵���Ϣ,��ɨ����ͨ��Ϣ�� ���ݲ���scanType�Ĳ�ͬ������ͬ��ɨ��
     * scanType=1 ɨ�輰ʱ��Ϣ�� scanType=2ɨ�趨ʱ������Ϣ��
     * ���������źͷ����һ��ɨ�裬��ֹ�����Ѿ�ʧЧ�˵ķ������Ϣ��ɨ���������������ˮ�������� ����ɨ���Ժ󣬵���process���������д���
     */
    public void scanTable() throws UMSConnectionException {
        // �����ӳ��еõ�����
        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
//        StringBuffer sql = null;
//        int i = 0;
//
////        Res.log(Res.INFO, "�ⷢ��Ӧ�ó������ݵ�ɨ�裡");
//        int dataBaseType = Util.getDataBaseType();
//        if (dataBaseType == Util.UMS_DATABASE_ORACLE) {
//            sql = new StringBuffer("select * from in_ready where statusFlag = 0 and rownum<=15");
//        } else if (dataBaseType == Util.UMS_DATABASE_SQLSERVER) {
//            sql = new StringBuffer("select top 15 * from in_ready where statusFlag = 0");
//        }
        // sql = new StringBuffer("select * from in_ready where statusFlag =
        // 0");
//        Res.log(Res.DEBUG, "sql=" + sql);

        try {
            conn = DriverManager.getConnection(DataBaseOp.getPoolName());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            rs = stmt.executeQuery(sql.toString());
            rs = stmt.executeQuery(Util.getDialect().getAppServerMsgProcessSQL_V2());
            // ������ݼ��ϲ�Ϊ�յ�ʱ�򣬲Ŵ���
            if (rs.next()) {
                stmt2 = conn.createStatement();
                rs.beforeFirst();
                process(rs, stmt2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "1091", e.getMessage());
            Res.logExceptionTrace(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Res.log(Res.ERROR, "1091", e.getMessage());
                Res.logExceptionTrace(e);
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Res.log(Res.ERROR, "1091", e.getMessage());
                Res.logExceptionTrace(e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Res.log(Res.ERROR, "1091", e.getMessage());
                Res.logExceptionTrace(e);
            }
        }
    }

    /*
     * �����̵߳�״̬���������Ϊtrue ��ʾ�ֳɻ��������У����Ϊfalse,��ʾ�߳��Ѿ�ֹͣ��
     */
    public boolean getThreadState() {
        return !stop;
    }

    /*
     * ������Ϣ�ĺ���
     */
    public int sendMsg(ResultSet rs) throws UMSConnectionException {
        boolean result = false;
        try {
            AppInfo appInfo = (AppInfo) appMap.get(rs.getString("AppID"));
            if (appInfo != null) {
                Send send = null;
                // ���췢��Ӧ�ó������ݵĶ���
                if (appInfo.getObject() == null) {
                    // �����������ʹ������Ͷ��󣬸���Ҫ��ֻ����EMAIL����
                    if (appInfo.getChannelType() == AppChannelType.EMAIL_TYPE) {
                        send = new EMailAppChannel();
                    }
                    /*
                     * else
                     * if(appInfo.getChannelType()==AppChannelType.SOCKET_TYPE){
                     * send = new SocketAppChannel(); }else{ send =
                     * (Send)createObject(appInfo.getObjectName()); }
                     */

                    // ����Ӧ�ó����ⷢ����
                    appInfo.setObject(send);
                    appMap.remove(appInfo.getAppID());
                    appMap.put(appInfo.getAppID(), appInfo);
                } else {
                    send = (Send) appInfo.getObject();
                }
                // �����ⷢ����,��API�ⷢ������ҪappInfo��������
                if (send != null) {
                    /*
                     * if(appInfo.getChannelType()==AppChannelType.API_TYPE){
                     * send.initInterface();
                     * result=send.receiveMessage(appInfo.getLoginName(),appInfo.getLoginPwd(),rs.getString("retCode"),rs.getString("appSerialNO"),rs.getInt("ack"),rs.getString("recvID"),rs.getString("sendID"),rs.getString("submitDate"),rs.getString("submitTime"),rs.getString("content"));
                     * }else{
                     */
                    if (rs.getString("reply").indexOf("@") > 0) {
                        appInfo.setEmail(rs.getString("reply"));
                    }
                    send.initInterface(appInfo);
                    // ��ʼ��Ӧ�÷��ͣ���Ӧ�ý�����Ϣ
                    result = send.receiveMessage(appInfo.getLoginName(), appInfo.getLoginPwd(), rs.getString("retCode"), rs.getString("appSerialNO"), rs.getInt("ack"), rs.getString("recvID"), rs.getString("sendID"), rs.getString("submitDate"), rs.getString("submitTime"), rs.getString("content"));
                    // }
                }
            }
        } catch (Exception e) {
            Res.log(Res.ERROR, "sendMsg error:" + e);
            Res.logExceptionTrace(e);
            throw new UMSConnectionException();
        }

        if (result) {
            return 0;
        } else {
            return 1;
        }
    }

    private Object createObject(String className) throws ParserException {
        Object object = null;
        try {
            Class classDefinition = Class.forName(className);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            throw new ParserException("1012", Res.getStringFromCode("1012"));
        } catch (IllegalAccessException e) {
            throw new ParserException("1012", Res.getStringFromCode("1012"));
        } catch (ClassNotFoundException e) {
            throw new ParserException("1012", Res.getStringFromCode("1012"));
        }
        return object;
    }

    /*
     * sleep һ��ʱ��
     */
    private void sleepTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime * 1000);
//            Res.log(Res.INFO, "�߳�˯��" + sleepTime + "��!");
        } catch (InterruptedException e) {
//            e.printStackTrace();
            Res.log(Res.INFO, "�̱߳��ж�");
            pleaseStop();
        }
    }

    /*
     * ֹͣ����߳�
     */
    public void pleaseStop() {
        this.stop = true; // Set the stop flag
        this.interrupt();
        Res.log(Res.INFO, "Ӧ�ó����Ⲧ�����߳��˳�����");
    }

    public static void setAppInfo(AppInfo appInfo) {
        appMap.remove(appInfo.getAppID());
        appMap.put(appInfo.getAppID(), appInfo);
    }

    public static void main(String[] args) {
        AppServer appServer = new AppServer();
        appServer.start();
    }

}