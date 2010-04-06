/*
 * CommandCenter.java
 * 
 * Created on 2007-9-27, 16:53:32
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.service.common;

import com.nci.ums.periphery.exception.OutOfMaxSequenceException;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import java.sql.SQLException;

/**
 *
 * @author Qil.Wong
 */
public interface CommandCenter{

    /**
     * Message transmission before send to database.
     * In this step, message will be attibuted many parameter to ensure message
     * data integrity.
     * @param msgs messages to be transmitted
     * @return String, result of transmission
     * @throws com.nci.ums.periphery.exception.OutOfMaxSequenceException 
     */
    public String transmit(UMSMsg[] msgs) throws OutOfMaxSequenceException;
    
    /**
     * 
     * @param msg 
     * @return 
     */
    public UMSMsg channelPolicier(UMSMsg msg) throws SQLException;
    
    /**
     * 
     * @param msg
     * @return 
     */
    public UMSMsg messagePolicier(BasicMsg basicMsg);
}
