package com.nci.ums.channel.outchannel;

import java.io.IOException;

import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Cfg;
import com.huawei.smproxy.SMProxy;
import com.nci.ums.util.Res;

public class SMProxyFactory {

    static SMProxyFactory msProxyFactory;
    static SMProxy proxy;

    private SMProxyFactory() {
        try {
            Args args = new Cfg("hw.xml", false).getArgs("ismg");
            proxy = new SMProxy(args);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("��ʼ����Ϊapi�����ļ���������:" + e.toString());
            Res.log(Res.ERROR, "��ʼ����Ϊapi�����ļ���������:" + e.getMessage());
        }
    }

    public static synchronized SMProxyFactory getInstance() {
        if (msProxyFactory == null) {
            msProxyFactory = new SMProxyFactory();
        }
        return msProxyFactory;
    }

    public static SMProxy createMSProxy() {
        return proxy;
    }
}
