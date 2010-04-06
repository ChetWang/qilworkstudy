package com.nci.ums.periphery.application;


import com.nci.ums.periphery.exception.*;

/**
 *  负责UMS给不同的应用系统发送消息的接口
 */
public interface Send {
    /**
     * 接口初始化
     */
    public boolean initInterface() throws ApplicationException;

    public boolean initInterface(AppInfo appInfo) throws ApplicationException;

    /**
     * 调用process层进行处理，CIS直接调用，其他新开线程
     */
    public boolean receiveMessage(String loginName,
                        String loginPwd,
                        String retCode,
                        String msgID,
                        int ack,
						String receive,
						String from,
						String receiveDate,
                        String receiveTime,
						String content)throws ApplicationException ;
}