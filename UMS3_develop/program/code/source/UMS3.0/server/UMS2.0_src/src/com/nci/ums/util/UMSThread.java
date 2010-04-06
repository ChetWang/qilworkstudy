/**
 * <p>Title: Util.java</p>
 * <p>Description:
 *    线程表
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.util;

public class UMSThread{
    private int ID;
    private String type;
    private String name;
    private Object threadObject;
    private String startTime;
    private String status;
    private String memo;

    public UMSThread(String type,  String name, String startTime, String status, Object threadObject,String memo) {
        this.type = type;
        this.memo = memo;
        this.name = name;
        this.startTime = startTime;
        this.status = status;
        this.threadObject = threadObject;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getThreadObject() {
        return threadObject;
    }

    public void setThreadObject(Object threadObject) {
        this.threadObject = threadObject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}