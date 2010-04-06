/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.v3.service.client;

import java.io.IOException;

/**
 *
 * @author Qil.Wong
 */
public class UMSDisconnectException extends IOException{
    
    public UMSDisconnectException(){
        super("UMS Server Disconected");
    }
}
