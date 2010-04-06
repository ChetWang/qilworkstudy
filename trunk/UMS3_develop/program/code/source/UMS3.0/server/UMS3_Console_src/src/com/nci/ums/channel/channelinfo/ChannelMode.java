/*
 * ChannelMode.java
 * 
 * Created on 2007-10-8, 17:51:07
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.channel.channelinfo;

/**
 *
 * @author Qil.Wong
 */
public class ChannelMode {
    
    /**
     * Means the channel gets messages by scaning database.
     */
    public static final int CHANNEL_MODE_SCAN = 1;
    /**
     * Means the channel gets messages by event listening.
     */
    public static final int CHANNEL_MODE_LISTENER = 2;

}
