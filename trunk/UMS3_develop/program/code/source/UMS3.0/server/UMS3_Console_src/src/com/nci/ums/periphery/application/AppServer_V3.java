package com.nci.ums.periphery.application;

import java.util.HashMap;

import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.util.Res;

public class AppServer_V3 extends Thread{

    // ȫ���߳��˳���־�����������
    protected QuitLockFlag quitFlag;
    // ���߳��˳���־
    protected boolean stop = false;
    private static HashMap appMap;
    
    //ums2.0�汾�У�AppServer����Ҫ�����ǽ���Ϣ���ݸ�EmailӦ�ó���
    //��UMS3.0��,email�Ѿ���ȫ�Զ�����������Ϊһ���൱��Ҫ���������Ͻ�UMS�С�
    //���ԣ���3.0�У�����Ҫ����ת�����ơ�
    /**
     * @deprecated
     */
    public AppServer_V3() {
        quitFlag = QuitLockFlag.getInstance();
        Res.log(Res.INFO, "UMS3.0Ӧ�ó����Ⲧ�������������");
    }

    
}
