/**
 * <p>Title: AppChannel.java</p>
 * <p>Description:
 *    抽象类，提供给各个应用程序拨出渠道类来继承，实现了Send接口。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 17 2003   张志勇        Created
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

    // 全部线程退出标志（共享变量）
    protected QuitLockFlag quitFlag;
    // 本线程退出标志
    protected boolean stop = false;
    private static HashMap appMap;

    /*
     * 构造函数
     */
    public AppServer() {
        quitFlag = QuitLockFlag.getInstance();
        Res.log(Res.INFO, "UMS2.0应用程序转发给外部应用对象产生！");
    }

    /*
     * run函数，提供线程运行时的主要任务 一直循环直到被中断或者退出标志被设置。 每次扫描一次表以后，会sleep一段时间。
     */
    public void run() {
        // 先把退出标志设置为false;
        stop = false;

        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
            try {
                // 裉始化应用程序注册信息表
//                Res.log(Res.DEBUG, "UMS转发给应用程序的处理开始");
                appMap = initAppMap();

                // 扫描需发送至应用程序的信息
                if (appMap != null) {
                    scanTable();
                }
//                Res.log(Res.DEBUG, "UMS转发给应用程序的处理结束");
            } catch (UMSConnectionException e) {
                e.printStackTrace();
            } finally {
                sleepTime(60);
            }
        }
    }

    /*
     * 初始化应用程序注册信息表 以备发送信息时检索
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
            Res.log(Res.ERROR, "初始化应用程序信息出错" + ex);
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
     * 处理函数，主要执行发送数据集中的每一条记录的责任。 先试图和服务器建立连接，如果循环多次建立不上。
     * 返回的int数值为表示这次数据集中发送过程中发送成功的信息的数目。
     */
    public int process(ResultSet rs, Statement stmt) throws UMSConnectionException {

        // test code
        int i = 0; // 用来记录发送成功的记录的条数。
        try {
            while (rs.next()) {
                // 需要读content以及adMsg,subMsg.
                int errCode = sendMsg(rs);
                // 表示这条记录发送成功。
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
            Res.log(Res.ERROR, "8002", "应用程序外拨渠道.");
            Res.logExceptionTrace(e);
            throw e;
        }
        return i;
    }

    /*
     * 数据库操作函数，根据传入的数据集和result来确定要做的数据库操作
     *
     */
    protected void OpData(ResultSet rs, Statement stmt, int result) {
        String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
        String finishDate = currentTime.substring(0, 8);
        String finishTime = currentTime.substring(8, 14);
        StringBuffer insertSQL = new StringBuffer();

        int count = 0;
        try {
            // 表示发送成功。需要修改finishDate,finishTime,count
            // 然后送到ok表中，并在ready表中删除这条记录
            count = rs.getInt("docount");
            if (result == 0) {
                insertSQL.append("insert into in_ok(BatchNo,SerialNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,Content, ack, reply) values('").append(Util.replaceNULL(rs.getString("BatchNo"))).append("',").append(rs.getInt("SerialNo")).append(",'").append(rs.getString("retCode")).append("','").append("发送成功").append("',").append(rs.getString("StatusFlag")).append(",'").append(rs.getString("AppId")).append("','").append(Util.replaceNULL(rs.getString("AppSerialNo"))).append("','").append(rs.getString("MediaId")).append("','").append(rs.getString("SendId")).append("','").append(rs.getString("RecvId")).append("','").append(Util.replaceNULL(rs.getString("SubmitDate"))).append("','").append(Util.replaceNULL(rs.getString("SubmitTime"))).append("','").append(Util.replaceNULL(finishDate)).append("','").append(Util.replaceNULL(finishTime)).append("','").append(rs.getString("Content")).append("',").append(Util.replaceNULL(rs.getString("ack"))).append(",'").append(Util.replaceNULL(rs.getString("reply"))).append("')");

                Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
                // TimerStep t1=new TimerStep("out_ok表中增加一条记录消耗时间");
                stmt.executeUpdate(insertSQL.toString());
                // t1.TimerStop();
                Res.log(Res.DEBUG, "成功向in_ok表中增加一条记录!");
                StringBuffer delSQL = new StringBuffer();
                delSQL = new StringBuffer("delete from in_ready where batchNO='").append(rs.getString("batchNO")).append("' and SerialNo = ").append(rs.getInt("SerialNo"));
                Res.log(Res.DEBUG, "delSQL=" + delSQL);
                stmt.executeUpdate(delSQL.toString());
                Res.log(Res.DEBUG, "成功从in_ready表中删除一条记录！");
            } else {
                // 表示已经超过了设置的数值，不在发送了。
                if (count > 3) {
                    String errMsg = "发送失败次数超过规定次数！";
                    insertSQL.append("insert into in_ok(BatchNo,SerialNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,Content, ack, reply) values('").append(Util.replaceNULL(rs.getString("BatchNo"))).append("',").append(rs.getInt("SerialNo")).append(",'").append("0001").append("','").append(errMsg).append("',").append(rs.getString("StatusFlag")).append(",'").append(rs.getString("AppId")).append("','").append(Util.replaceNULL(rs.getString("AppSerialNo"))).append("','").append(rs.getString("MediaId")).append("','").append(rs.getString("SendId")).append("','").append(rs.getString("RecvId")).append("','").append(Util.replaceNULL(rs.getString("SubmitDate"))).append("','").append(Util.replaceNULL(rs.getString("SubmitTime"))).append("','").append(Util.replaceNULL(finishDate)).append("','").append(Util.replaceNULL(finishTime)).append("','").append(rs.getString("Content")).append("',").append(Util.replaceNULL(rs.getString("ack"))).append(",'").append(Util.replaceNULL(rs.getString("reply"))).append("')");
                    Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
                    stmt.executeUpdate(insertSQL.toString());
                    Res.log(Res.DEBUG, "成功向in_ok表中增加一条记录!");
                    StringBuffer delSQL = new StringBuffer();
                    delSQL = new StringBuffer("delete from in_ready where SerialNo = ").append(rs.getInt("SerialNo"));

                    Res.log(Res.DEBUG, "delSQL=" + delSQL);
                    stmt.executeUpdate(delSQL.toString());
                    Res.log(Res.DEBUG, "成功从in_ready表中删除一条记录！");
                } else {
                    Res.log(Res.DEBUG, "发送不成功！");
                    StringBuffer updateSQL = new StringBuffer("update  in_ready set doCount =").append(count).append(" where SerialNo = ").append(rs.getInt("SerialNo"));
                    Res.log(Res.DEBUG, "updateSQL=" + updateSQL);
                    stmt.executeUpdate(updateSQL.toString());
                    Res.log(Res.DEBUG, "成功从in_ready表中更新一条记录！");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "1091", e.getMessage());
            Res.logExceptionTrace(e);
        }
    }

    /*
     * 扫描数据库表的函数，提供数据集 这个函数主要扫描的是没有失效的，需要发送的信息,不扫描普通信息。 根据参数scanType的不同，做不同的扫描
     * scanType=1 扫描及时信息， scanType=2扫描定时发送信息，
     * 根据渠道号和服务号一起扫描，防止出现已经失效了的服务的信息被扫描出来，并按照流水号来排序。 做完扫描以后，调用process函数，进行处理。
     */
    public void scanTable() throws UMSConnectionException {
        // 从连接池中得到连接
        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
//        StringBuffer sql = null;
//        int i = 0;
//
////        Res.log(Res.INFO, "外发给应用程序数据的扫描！");
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
            // 如果数据集合不为空的时候，才处理。
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
     * 返回线程的状态，如果返回为true 表示现成还在运行中，如果为false,表示线程已经停止。
     */
    public boolean getThreadState() {
        return !stop;
    }

    /*
     * 发送信息的函数
     */
    public int sendMsg(ResultSet rs) throws UMSConnectionException {
        boolean result = false;
        try {
            AppInfo appInfo = (AppInfo) appMap.get(rs.getString("AppID"));
            if (appInfo != null) {
                Send send = null;
                // 构造发给应用程序数据的对象
                if (appInfo.getObject() == null) {
                    // 根据渠道类型创建发送对象，根据要求只处理EMAIL发送
                    if (appInfo.getChannelType() == AppChannelType.EMAIL_TYPE) {
                        send = new EMailAppChannel();
                    }
                    /*
                     * else
                     * if(appInfo.getChannelType()==AppChannelType.SOCKET_TYPE){
                     * send = new SocketAppChannel(); }else{ send =
                     * (Send)createObject(appInfo.getObjectName()); }
                     */

                    // 更新应用程序外发对象
                    appInfo.setObject(send);
                    appMap.remove(appInfo.getAppID());
                    appMap.put(appInfo.getAppID(), appInfo);
                } else {
                    send = (Send) appInfo.getObject();
                }
                // 调用外发对象,非API外发对象需要appInfo参数传递
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
                    // 开始向应用发送，即应用接收消息
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
     * sleep 一段时间
     */
    private void sleepTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime * 1000);
//            Res.log(Res.INFO, "线程睡眠" + sleepTime + "秒!");
        } catch (InterruptedException e) {
//            e.printStackTrace();
            Res.log(Res.INFO, "线程被中断");
            pleaseStop();
        }
    }

    /*
     * 停止这个线程
     */
    public void pleaseStop() {
        this.stop = true; // Set the stop flag
        this.interrupt();
        Res.log(Res.INFO, "应用程序外拨渠道线程退出服务！");
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