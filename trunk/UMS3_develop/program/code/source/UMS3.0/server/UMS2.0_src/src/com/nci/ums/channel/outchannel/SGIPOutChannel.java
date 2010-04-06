/**
 * <p>Title: SGIPOutChannel.java</p>
 * <p>Description:
 *    SGIPOutChannel�Ⲧ�����࣬�̳�OutChannel
 *    ʵ����ͨ��Ϣ�ķ��͹���
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV 20 2003   ��־��        Created
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

    //��ʱ����Ϊһ����
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
                //�����Ϣ��Ϣ
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
                    //����Ϣ����16��,���˳�
                    if (count >= 15) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Res.log(Res.ERROR, "1091", e.getMessage());
                Res.logExceptionTrace(e);
            } finally {
                Res.log(Res.DEBUG, "ȡ����Ϣ����" + count);
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

        //����
        if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
            cmppThreadLockFlag.setLockFlag(false);
        } else {
            sgipThreadLockFlag.setLockFlag(false);
        }

        //��������δȡ������,������̴��ڵȴ���
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
        my_timer = new javax.swing.Timer(1000 * 10, responseOutTime); //1���Ӻ�δ��Ӧ����
        my_timer.setRepeats(false);
        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
            //�������ӡ�

            //����ʱΪ��ע
            getConnection();

            if (stop) {
                break;
            }
            Res.log(Res.DEBUG, "�ɹ��������ӣ�");

            //������Ϣ
            try {
                msgInfos = new MsgInfo[16];
                msgCount = getMsgInfos(msgInfos);
                //���ͻ�����������Ϣ
                submitCount = 0;
                for (int i = 0; i < msgCount; i++) {
                    //����ʱΪ��ע
                    int response = sendMsg(msgInfos[i]);
                    //int response=0;
                    if (response == 0) {
                        submitCount = submitCount + 1;
                    //msgInfos[i].setStatus(1);
                    }
                }
                //�ȴ���Ӧ

                responseFlag = false;
                responseCount = 0;
                if (submitCount > 0) {
                    my_timer.start();
                    while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop && !responseFlag) {
                        //����ʱΪ��ע
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

//����
    private void testResponse() {

        OpData(0, msgInfos[responseCount], "");
        //msgInfos[responseCount].setStatus(1);
        //msgInfos[responseCount].setRetCode(0);
        responseCount = responseCount + 1;
        if (responseCount == submitCount) {
            responseFlag = true;
        }
    }
    //����
    //��ȡ������Ϣ
    public int readPa() {
        int result = 0;
        try {
            SGIP_Command sgip = null;
            SGIP_Command tmp = null;
            sgip = new SGIP_Command();
            tmp = sgip.read(input);
            if (tmp.getCommandID() == SGIP_Command.ID_SGIP_SUBMIT_RESP) {
                SubmitResp submitresp = null;
                submitresp = (SubmitResp) tmp; //ǿ��ת��
                submitresp.readbody(); //���
                //���ص���ֵ�Ƿ��͵�״̬��
                MsgInfo msgInfo = getMsgInfo(submitresp.getSeqno_3());
                //Res.log(Res.DEBUG, "���յ���ͨ��Ӧ"+submitresp.getSeqno_3());
                if (msgInfo != null) {
                    if (submitresp.getResult() == 0) {
                        Res.log(Res.DEBUG, "������ͨ����Ϣ�ɹ���");
                        {
                            OpData(result, msgInfo, "");
                            msgInfo.setStatus(0);
                        }
                    } else if (submitresp.getResult() == 88) {
                        throw new Exception();
                    } else {
                        msgInfo.setStatus(1);
                        msgInfo.setRetCode(submitresp.getResult());
                    //Res.log(Res.DEBUG, "������ͨ����Ϣʧ�ܣ�������Ϊ:" + submitresp.getResult());
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
            Res.log(Res.DEBUG, "SGIPOutChannel����" + e);
            result = 1;
        }
        return result;
    }

    /* ����һ����Ϣ��������ֵΪ���������Ϊ-1˵�����������صİ�ʧȥ������߳�ʱ����ʾ��Ҫ�������ӡ�
     * ����0��ʾ���ͳɹ���������������0����ֵ��ʾ����ʧ�ܣ�������Ҫ�������ӡ�
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
            submit = new Submit(SGIP_NODEID, //node idͬ��
                    msgInfo.getSendID(), //cp_phone  SP�Ľ������
                    chargeNumber, //���Ѻ��� ��ʾ��������Ϣ�����ķ�����SP֧����
                    temp_dst.length, //���ն���Ϣ���ֻ���
                    sb.toString(), //�ֻ�����ǰ���86
                    SGIP_CORPID, //cp_id   ��ҵ����
                    "99999", //ҵ�����     ����SP����
                    feeType, //�Ʒ�����
                    fee, //����Ϣ�շ�ֵ   ��������Ϣ���շ�ֵ����λΪ�֣���SP����
                    "0", //���ͻ���
                    1, //���ձ�־
                    2, //����MT��ԭ��
                    9, //���ȼ�
                    "", //����Ϣ��ֹʱ��
                    "", //011125120000032+����Ϣ��ʱ����ʱ��
                    2, //״̬�����־
                    1, //GSMЭ������
                    1, //GSMЭ������
                    msgInfo.getContentMode(), //����Ϣ�����ʽ
                    0, //��Ϣ����
                    sm_len, //����Ϣ����
                    short_msg); //����Ϣ����
            submit.write(out); //����submit
            msgInfo.setStatus(1);
            msgInfo.setSeq(submit.getSeqno_3());
            result = 0;
            Res.log(Res.DEBUG, "�ɹ����ͣ�" + submit.getSeqno_3());
        } catch (SGIP_Exception e) {
            Res.log(Res.INFO, "���ֲ��������쳣" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            logout();
            Res.log(Res.INFO, "�����쳣" + e.getMessage());
        //throw new UMSConnectionException(e);
        }
        //������������صİ�ʧȥ�˴�����Ҳ���ش���
        //���߳����˳�ʱ���쳣��
        //��Ҫ�������ӷ�������
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
     * �ͷ����������ӣ��������������ѭ������sleep
     * �ͷ������������֤�������֤���ɹ����������ӷ�������
     */
    synchronized public void getConnection() {
        boolean flag = false;
        int i = 0;
        while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) &&
                (!flag)) {
            if (socket == null || socket.isClosed()) {
                if (i++ == 0) {
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��ʼ���ӷ�����:");
                }
                Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i + "�����ӷ�����:");
                try {
                    socket = new Socket(mediaInfo.getIp(), mediaInfo.getPort());
                    out = new DataOutputStream(socket.getOutputStream());
                    input = new DataInputStream(socket.getInputStream());

                    //���ó�ʱ������
                    socket.setSoTimeout(mediaInfo.getSleepTime() * 1000);
                    flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Res.log(Res.INFO, e.getMessage());
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i + "�����ӷ�����ʧ�ܣ�");
                    closeConnection();
                }
                //������������ӣ�����֤�û��ĺϷ��ԡ�
                if (flag == true) {
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i + "�����ӷ������ɹ���");
                    Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��ʼ�ͷ�������֤��");
                    flag = getBind();
                    if (flag) {
                        Res.log(Res.INFO, mediaInfo.getMediaName() + "liantong connect success�ͷ�������֤�ɹ���");
                    } else {
                        Res.log(Res.INFO, mediaInfo.getMediaName() + "liantong connect fail�ͷ�������֤ʧ�ܣ�");
                    }
                }
                //�����֤���ʧ�ܣ��ر����ӡ�����sleepһ��ʱ�䣬������������Ӳ�����֤
                if (flag == false) {
                    Res.log(Res.INFO,
                            mediaInfo.getMediaName() + "�����" + mediaInfo.getSleepTime() +
                            "����������ӷ�����");
                    closeConnection();
                    //sleepһ��ʱ�䡣
                    try {
                        Thread.sleep(mediaInfo.getSleepTime() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Res.log(Res.INFO, "�̱߳��ж�");
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
     * ��½���������ںͷ��������������֤
     * ����Ϊtrue��ʾ��֤�ɹ�������false��ʾ��֤ʧ�ܡ�
     */

    private boolean getBind() {
        Bind command = null;
        SGIP_Command sgip = null;
        SGIP_Command tmp = null;

        sgip = new SGIP_Command();
        //�����ֱ��ǽڵ���룬�������ͣ���½���ƣ���½���롣
        command = new Bind(SGIP_NODEID, 1, mediaInfo.getLoginName(),
                mediaInfo.getLoginPassword());
        int err;
        BindResp resp = null;
        //����bind,�����׳��쳣��������ִ����򷵻ش����롣
        err = command.write(out);
        //д�׽��Ӵ���
        if (err != 0) {
            Res.log(Res.ERROR, "�ڸ���ͨ������������֤��Ϣʱ�����������Ϊ" + err);
            return false;
        }

        try {
            tmp = sgip.read(input); //����sgip��Ϣ
            if (sgip.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
                resp = (BindResp) tmp; //ǿ��ת��Ϊbindresp
                resp.readbody(); //����Ϣ���н��          
                if (resp.GetResult() == 0) {
                    Res.log(Res.DEBUG, "���ܵ�bing_resp��½��֤�ɹ�");
                    return true;
                } else {
                    Res.log(Res.DEBUG, "���ܵ�bing_resp�����Ƿ�������ʾ���󣬴�����Ϊ:" + resp.GetResult());
                }
            } else {
                Res.log(Res.ERROR, "���ܵ�����Ļ�Ӧ��");
            }
        } catch (SGIP_Exception e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "���ֲ��������쳣" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "�����쳣" + e.getMessage());
        }
        //������ܵ��Ļ�Ӧ������bind_resp���߳��������쳣���Ѿ����������򷵻�false
        return false;
    }

    private void logout() {
        try {
            Unbind();
            Res.log(Res.DEBUG, "�����˳�");
        } catch (Exception ee) {
            Res.log(Res.DEBUG, "�˳��쳣");
        } finally {
            closeConnection();
        }
        responseFlag = true;
    }
    /*
     * �˳���½�ĺ���������֪ͨ����������½�˳�
     * ����Ϊtrue��ʾ��½�ɹ�������false��ʾ��½ʧ�ܡ�
     */

    private boolean Unbind() {
        Unbind command = null;
        SGIP_Command sgip = null;
        SGIP_Command tmp = null;

        sgip = new SGIP_Command();
        command = new Unbind(SGIP_NODEID);
        int err;
        UnbindResp resp = null;
        err = command.write(out); //����bind
        //д�׽��Ӵ���
        if (err != 0) {
            Res.log(Res.DEBUG, "�ڸ���ͨ���������ͽ����½��Ϣʱ�����������Ϊ" + err);
            return false;
        }
        try {
            tmp = sgip.read(input); //����sgip��Ϣ
            if (sgip.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
                //Res.log(Res.ERROR, "���ܵ���ȷ�Ļ�Ӧ��");
                resp = (UnbindResp) tmp; //ǿ��ת��Ϊbindresp
                return true;
            } else {
                Res.log(Res.ERROR, "���ܵ�����Ļ�Ӧ��");
            }
        } catch (SGIP_Exception e) {
            e.printStackTrace();
            Res.log(Res.INFO, "���ֲ��������쳣" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Res.log(Res.INFO, "�����쳣" + e.getMessage());
        }
        return false;
    }

    /*
     * �رպͷ�����������
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
