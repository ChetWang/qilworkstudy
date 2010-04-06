/**
 * <p>Title: SGIPOutChannel.java</p>
 * <p>Description:
 *    SGIPOutChannel外拨渠道类，继承OutChannel
 *    实现联通信息的发送过程
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV 20 2003   张志勇        Created
 * @version 1.0
 */
package com.nci.ums.channel.outchannel;

import java.net.*;
import java.sql.ResultSet;
import java.io.*;

import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.exception.*;

import com.nci.ums.util.*;

import spApi.*;

public class SGIPOutChannel
        extends OutChannel {

    private final static String SGIP_CORPID = "62292";
    private final int SGIP_NODEID = (int) new Long("3057162292").longValue();
    private Socket socket = null;
    private OutputStream out = null;
    private InputStream input = null;
    private MsgInfo[] msgInfos;
    private boolean stop;
    private boolean responseFlag;
    protected QuitLockFlag quitFlag;
    private int submitCount;
    private int responseCount;
    private int msgCount;

    //定时器设为一分钟
    private javax.swing.Timer my_timer;
    private SGIPOutChannel.ResponseOutTime responseOutTime = new SGIPOutChannel.ResponseOutTime();

    public SGIPOutChannel(MediaInfo mediaInfo) {
        super(mediaInfo);
        quitFlag = QuitLockFlag.getInstance();
    }

    protected int getMsgInfos(MsgInfo[] result) {
        int count = 0;
        DBConnect db = null;
        ResultSet rs = null;
        String currentDateTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
        String currentDate = currentDateTime.substring(0, 8);
        String currentTime = currentDateTime.substring(8, 14);

        if ((!ydDataLockFlag.getLockFlag() && mediaInfo.getMediaId().equalsIgnoreCase("010") && !cmppThreadLockFlag.getLockFlag()) || (!ltDataLockFlag.getLockFlag() && mediaInfo.getMediaId().equalsIgnoreCase("011") && !sgipThreadLockFlag.getLockFlag())) {

            if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
                cmppThreadLockFlag.setLockFlag(true);
            } else {
                sgipThreadLockFlag.setLockFlag(true);
            }

            getMsgCounts = getMsgCounts + 1;
            try {
                db = new DBConnect();
                StringBuffer sb = new StringBuffer("select * from out_ready where statusFlag=0 and mediaID=? and doCount<4");
                sb.append(" and ( setDate < ").append(currentDate).
                        append(" or (setDate = ").append(currentDate).
                        append(" and setTime <= ").append(currentTime).append("))").
                        append(" order by serialNO limit 0,1");
                db.prepareStatement(sb.toString());
                db.setString(1, mediaInfo.getMediaId());
                rs = db.executeQuery();
                //填充消息信息
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
                    if (rs.getString("replyDes").length() > 0) {
                        if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
                            msgInfo.setSendID("95598" + rs.getString("replyDes"));
                        } else {
                            msgInfo.setSendID("195598" + rs.getString("replyDes"));
                        }
                    }

                    if (msgInfo.getContentMode() != 4 && msgInfo.getContentMode() != 21) {
                        msgInfo.setContent(Util.convertFullToHalf(rs.getString("content").trim()));
                    } else {
                        msgInfo.setContent(rs.getString("content").trim());
                    }

                    msgInfo.setCount(0);
                    msgInfo.setStatus(2);

                    result[count] = msgInfo;
                    db.executeUpdate("update out_ready set statusFlag=1 where batchNO='" + rs.getString("batchNO") + "' and serialNO=" + rs.getInt("serialNO") + " and sequenceNO=" + rs.getInt("sequenceNO"));
                    count = count + 1;
                    //若消息超过16个,则退出
                    if (count >= 15) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Res.log(Res.ERROR, "1091", e.getMessage());
                Res.logExceptionTrace(e);
            } finally {
                Res.log(Res.DEBUG, "取得消息数量" + count);
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (db != null) {
                        db.close();
                    }
                } catch (Exception e) {
                    Res.log(Res.ERROR, "out channel getMsgs error:" + e);
                    Res.logExceptionTrace(e);
                }
            }
        }

        //解锁
        if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
            cmppThreadLockFlag.setLockFlag(false);
        } else {
            sgipThreadLockFlag.setLockFlag(false);
        }

        //若续三次未取到数据,服务进程处于等待中
        if (count == 0 && getMsgCounts > 3) {
            getMsgCounts = 0;
            if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
                ydDataLockFlag.setLockFlag(true);
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

    public void run() {
        my_timer = new javax.swing.Timer(1000 * 10, responseOutTime); //1分钟后未响应重启
        my_timer.setRepeats(false);
        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
            //建立连接。

            //测试时为备注
            getConnection();

            if (stop) {
                break;
            }
            Res.log(Res.DEBUG, "成功建立连接！");

            //发送消息
            try {
                msgInfos = new MsgInfo[16];
                msgCount = getMsgInfos(msgInfos);
                //发送滑动窗口中消息
                submitCount = 0;
                for (int i = 0; i < msgCount; i++) {
                    //测试时为备注
                    int response = sendMsg(msgInfos[i]);
                    //int response=0;
                    if (response == 0) {
                        submitCount = submitCount + 1;
                    //msgInfos[i].setStatus(1);
                    }
                }
                //等待回应

                responseFlag = false;
                responseCount = 0;
                if (submitCount > 0) {
                    my_timer.start();
                    while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop && !responseFlag) {
                        //测试时为备注
                        readPa();

                    }
                    my_timer.stop();
                    endSubmit(msgInfos, msgCount);
                }
            } catch (Exception e) {
                logout();
                Res.log(Res.DEBUG, "cmppoutchannel run error:" + e);
                endSubmit(msgInfos, msgCount);
            } finally {
                if (msgCount == 0) {
                    sleepTime(1000);
                }
            }
        }
    }

//测试
    private void testResponse() {

        OpData(0, msgInfos[responseCount], "");
        //msgInfos[responseCount].setStatus(1);
        //msgInfos[responseCount].setRetCode(0);
        responseCount = responseCount + 1;
        if (responseCount == submitCount) {
            responseFlag = true;
        }
    }
    //测试
    //读取反馈信息
    public int readPa() {
        int result = 0;
        try {
            SGIP_Command sgip = null;
            SGIP_Command tmp = null;
            sgip = new SGIP_Command();
            tmp = sgip.read(input);
            if (tmp.getCommandID() == SGIP_Command.ID_SGIP_SUBMIT_RESP) {
                SubmitResp submitresp = null;
                submitresp = (SubmitResp) tmp; //强制转换
                submitresp.readbody(); //解包
                //返回的数值是发送的状态。
                MsgInfo msgInfo = getMsgInfo(submitresp.getSeqno_3());
                //Res.log(Res.DEBUG, "接收到联通响应"+submitresp.getSeqno_3());
                if (msgInfo != null) {
                    if (submitresp.getResult() == 0) {
                        Res.log(Res.DEBUG, "发送联通短消息成功！");
                        {
                            OpData(result, msgInfo, "");
                            msgInfo.setStatus(0);
                        }
                    } else if (submitresp.getResult() == 88) {
                        throw new Exception();
                    } else {
                        msgInfo.setStatus(1);
                        msgInfo.setRetCode(submitresp.getResult());
                    //Res.log(Res.DEBUG, "发送联通短消息失败，错误码为:" + submitresp.getResult());
                    }
                }
                responseCount = responseCount + 1;
                if (responseCount == submitCount) {
                    responseFlag = true;
                }
                result = submitresp.getResult();

            }
        } catch (Exception e) {
            responseCount = responseCount + 1;
            if (responseCount == submitCount) {
                responseFlag = true;
            }
            logout();
            Res.log(Res.DEBUG, "SGIPOutChannel出错" + e);
            result = 1;
        }
        return result;
    }

    /* 发送一条信息，返回数值为整数。如果为-1说明服务器返回的包失去次序或者超时，表示需要重新连接。
     * 返回0表示发送成功，返回其他大于0的数值表示发送失败，但不需要重新连接。
     */
    public int sendMsg(MsgInfo msgInfo) throws
            UMSConnectionException {
        String dst_addr = msgInfo.getRecvID();
        String short_msg = msgInfo.getContent();

        String[] temp_dst = dst_addr.split(",");
        StringBuffer sb = new StringBuffer("");

        for (int i = 0; i < temp_dst.length; i++) {
            if (i == 0) {
                sb.append("86").append(temp_dst[i]);
            } else {
                sb.append(",").append("86").append(temp_dst[i]);
            }
        }

        int sm_len = short_msg.length();
        int result = 1;
        Res.log(Res.DEBUG, "mobileNumber=" + temp_dst.length);
        Res.log(Res.DEBUG, "mobile=" + sb.toString());
        try {

            Submit submit = null;
            String chargeNumber = "000000000000000000000";
            int feeType = 0;
            String fee = "0";
            if (msgInfo.getFeeType() != 0) {
                feeType = 2;
                chargeNumber = "";
                //fee=new Double(msgInfo.getFee()*100).toString();
                fee = new Double(msgInfo.getFee()).toString();
            }
            submit = new Submit(SGIP_NODEID, //node id同上
                    msgInfo.getSendID(), //cp_phone  SP的接入号码
                    chargeNumber, //付费号码 表示该条短消息产生的费用由SP支付。
                    temp_dst.length, //接收短消息的手机数
                    sb.toString(), //手机号码前面加86
                    SGIP_CORPID, //cp_id   企业代码
                    "99999", //业务代码     ，由SP定义
                    feeType, //计费类型
                    fee, //短消息收费值   该条短消息的收费值，单位为分，由SP定义
                    "0", //赠送话费
                    1, //代收标志
                    2, //引起MT的原因
                    9, //优先级
                    "", //短消息终止时间
                    "", //011125120000032+短消息定时发送时间
                    2, //状态报告标志
                    1, //GSM协议类型
                    1, //GSM协议类型
                    msgInfo.getContentMode(), //短消息编码格式
                    0, //信息类型
                    sm_len, //短消息长度
                    short_msg); //短消息内容
            submit.write(out); //发送submit
            msgInfo.setStatus(1);
            msgInfo.setSeq(submit.getSeqno_3());
            result = 0;
            Res.log(Res.DEBUG, "成功发送！" + submit.getSeqno_3());
        } catch (SGIP_Exception e) {
            Res.log(Res.INFO, "出现参数设置异常" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            logout();
            Res.log(Res.INFO, "出现异常" + e.getMessage());
        //throw new UMSConnectionException(e);
        }
        //如果服务器返回的包失去了次序，则也返回错误。
        //或者出现了超时等异常。
        //需要重新连接服务器。
        return result;
    }

    private MsgInfo getMsgInfo(int seq) {
        MsgInfo result = null;
        for (int i = 0; i < msgCount; i++) {
            if (msgInfos[i].getSeq() == seq) {
                result = msgInfos[i];
            }
        }
        return result;
    }

    /*
     * 和服务器做连接，如果连不上则做循环，并sleep
     * 和服务器做身份验证，如果验证不成功，重新连接服务器。
     */
    synchronized public void getConnection() {
        boolean flag = false;
        int i = 0;
        while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) &&
                (!flag)) {
            if (socket == null || socket.isClosed()) {
                if (i++ == 0) {
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "开始连接服务器:");
                }
                Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i + "次连接服务器:");
                try {
                    socket = new Socket(mediaInfo.getIp(), mediaInfo.getPort());
                    out = new DataOutputStream(socket.getOutputStream());
                    input = new DataInputStream(socket.getInputStream());

                    //设置超时函数。
                    socket.setSoTimeout(mediaInfo.getSleepTime() * 1000);
                    flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Res.log(Res.INFO, e.getMessage());
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i + "次连接服务器失败！");
                    closeConnection();
                }
                //如果建立了连接，再验证用户的合法性。
                if (flag == true) {
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "第" + i + "次连接服务器成功！");
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "开始和服务器验证！");
                    flag = getBind();
                    if (flag) {
                        Res.log(Res.INFO, mediaInfo.getMediaName() + "liantong connect success和服务器验证成功！");
                    } else {
                        Res.log(Res.INFO, mediaInfo.getMediaName() + "liantong connect fail和服务器验证失败！");
                    }
                }
                //如果验证身份失败，关闭连接。并且sleep一段时间，再与服务器连接并做验证
                if (flag == false) {
                    Res.log(Res.INFO,
                            mediaInfo.getMediaName() + "将间隔" + mediaInfo.getSleepTime() +
                            "秒后，重新连接服务器");
                    closeConnection();
                    //sleep一段时间。
                    try {
                        Thread.sleep(mediaInfo.getSleepTime() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Res.log(Res.INFO, "线程被中断");
                        pleaseStop();
                    }
                }
            } else if (socket != null && socket.isConnected()) {
                try {
                    socket.setKeepAlive(true);
                } catch (Exception e) {
                }
            }
            flag = true;
        }
    }

    public void pleaseStop() {
        stop = true;
    }
    /*
     * 登陆函数，用于和服务器进行身份验证
     * 返回为true表示验证成功，返回false表示验证失败。
     */

    private boolean getBind() {
        Bind command = null;
        SGIP_Command sgip = null;
        SGIP_Command tmp = null;

        sgip = new SGIP_Command();
        //参数分别是节点号码，接入类型，登陆名称，登陆密码。
        command = new Bind(SGIP_NODEID, 1, mediaInfo.getLoginName(),
                mediaInfo.getLoginPassword());
        int err;
        BindResp resp = null;
        //发送bind,不会抛出异常，如果出现错误，则返回错误码。
        err = command.write(out);
        //写套接子错误。
        if (err != 0) {
            Res.log(Res.ERROR, "在给联通服务器发送验证信息时出错，错误代码为" + err);
            return false;
        }

        try {
            tmp = sgip.read(input); //接收sgip消息
            if (sgip.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
                resp = (BindResp) tmp; //强制转换为bindresp
                resp.readbody(); //对消息进行解包          
                if (resp.GetResult() == 0) {
                    Res.log(Res.DEBUG, "接受到bing_resp登陆验证成功");
                    return true;
                } else {
                    Res.log(Res.DEBUG, "接受到bing_resp，但是服务器提示错误，错误码为:" + resp.GetResult());
                }
            } else {
                Res.log(Res.ERROR, "接受到错误的回应包");
            }
        } catch (SGIP_Exception e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "出现参数设置异常" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "出现异常" + e.getMessage());
        }
        //如果接受到的回应包不是bind_resp或者出现网络异常，已经参数错误，则返回false
        return false;
    }

    private void logout() {
        try {
            Unbind();
            Res.log(Res.DEBUG, "正常退出");
        } catch (Exception ee) {
            Res.log(Res.DEBUG, "退出异常");
        } finally {
            closeConnection();
        }
        responseFlag = true;
    }
    /*
     * 退出登陆的函数，用于通知服务器，登陆退出
     * 返回为true表示登陆成功，返回false表示登陆失败。
     */

    private boolean Unbind() {
        Unbind command = null;
        SGIP_Command sgip = null;
        SGIP_Command tmp = null;

        sgip = new SGIP_Command();
        command = new Unbind(SGIP_NODEID);
        int err;
        UnbindResp resp = null;
        err = command.write(out); //发送bind
        //写套接子错误。
        if (err != 0) {
            Res.log(Res.DEBUG, "在给联通服务器发送解除登陆信息时出错，错误代码为" + err);
            return false;
        }
        try {
            tmp = sgip.read(input); //接收sgip消息
            if (sgip.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
                //Res.log(Res.ERROR, "接受到正确的回应包");
                resp = (UnbindResp) tmp; //强制转换为bindresp
                return true;
            } else {
                Res.log(Res.ERROR, "接受到错误的回应包");
            }
        } catch (SGIP_Exception e) {
            e.printStackTrace();
            Res.log(Res.INFO, "出现参数设置异常" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Res.log(Res.INFO, "出现异常" + e.getMessage());
        }
        return false;
    }

    /*
     * 关闭和服务器的连接
     */
    public void closeConnection() {
        if (socket != null && socket.isConnected()) {
            Unbind();
            try {
                out.close();
                input.close();
                socket.close();
                socket = null;
                out = null;
                input = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ResponseOutTime
            implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            logout();
        }

        
        
    

;
    }
}
