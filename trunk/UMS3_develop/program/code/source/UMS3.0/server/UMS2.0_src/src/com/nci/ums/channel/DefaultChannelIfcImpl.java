package com.nci.ums.channel;

import com.nci.ums.channel.channelinfo.LockFlag;

public class DefaultChannelIfcImpl implements ChannelIfc{

	public boolean getThreadState() {
		return false;
	}

	public boolean isStartOnce() {
		return false;
	}

	public void start() {
	
	}

	public void stop() {
	
	}

	public LockFlag getDataLockFlag() {
		return null;
	}
	
	

}
