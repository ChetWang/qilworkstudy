package com.nci.ums.periphery.application;

import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.util.JMSUtil;
import com.nci.ums.util.Res;
import com.nci.ums.v3.service.impl.activemq.ActiveMQJMS;

public class JMSReceiver extends Thread {

    // 全部线程退出标志（共享变量）
    protected QuitLockFlag quitFlag;
    // 本线程退出标志
    protected boolean stop = false;
    protected ActiveMQJMS activeMQ;

    public JMSReceiver() {
        quitFlag = QuitLockFlag.getInstance();
        activeMQ = new ActiveMQJMS(JMSUtil.getJMSConnBean());
        Res.log(Res.INFO, "JMS监听对象产生！");
    }

    public void run() {
        stop = false;
        while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
            if (activeMQ == null) {
                activeMQ = new ActiveMQJMS(JMSUtil.getJMSConnBean());
                activeMQ.startConsumeListening();
                
                Res.log(Res.INFO, "ActiveMQ 重新初始化，开始监听！");
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Res.log(Res.ERROR, "ActiveMQj监听失败！");
                Res.logExceptionTrace(e);
            }
        }
    }
}