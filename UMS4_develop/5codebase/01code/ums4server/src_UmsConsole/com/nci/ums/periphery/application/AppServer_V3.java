package com.nci.ums.periphery.application;

import java.util.HashMap;

import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.util.Res;

public class AppServer_V3 extends Thread{

    // 全部线程退出标志（共享变量）
    protected QuitLockFlag quitFlag;
    // 本线程退出标志
    protected boolean stop = false;
    private static HashMap appMap;
    
    //ums2.0版本中，AppServer的主要任务是将消息传递给Email应用程序，
    //在UMS3.0中,email已经完全自动化处理，并作为一个相当重要的渠道整合进UMS中。
    //所以，在3.0中，不需要这类转发机制。
    /**
     * @deprecated
     */
    public AppServer_V3() {
        quitFlag = QuitLockFlag.getInstance();
        Res.log(Res.INFO, "UMS3.0应用程序外拨渠道对象产生！");
    }

    
}
