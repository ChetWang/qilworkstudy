/**
 * <p>Title: LockFlag.java</p>
 * <p>Description:
 *    这个类用来提供锁
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCISystem Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇       Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class LockFlag {
	private boolean flag;

	protected LockFlag() {
		this.flag = false;
	}

	synchronized public void setLockFlag(boolean flag) {
		this.flag = flag;
	}

	synchronized public boolean getLockFlag() {
		return this.flag;
	}

	synchronized public void reverseFlag() {
		this.flag = !this.flag;
	}
}