package com.nci.ums.periphery.application;


import com.nci.ums.periphery.exception.*;

/**
 *  ����UMS����ͬ��Ӧ��ϵͳ������Ϣ�Ľӿ�
 */
public interface Send {
    /**
     * �ӿڳ�ʼ��
     */
    public boolean initInterface() throws ApplicationException;

    public boolean initInterface(AppInfo appInfo) throws ApplicationException;

    /**
     * ����process����д���CISֱ�ӵ��ã������¿��߳�
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