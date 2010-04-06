/**
 * <p>Title: Util.java</p>
 * <p>Description:
 *    提供公用的函数库
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.util;


public class Application {
  private String appID;
  private String spNO;
  private int fee=0;
  private int feeType=0;

    public Application(String appID,  String spNO,int fee) {
        this.appID = appID;
        this.fee = fee;
        this.spNO = spNO;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getSpNO() {
        return spNO;
    }

    public void setSpNO(String spNO) {
        this.spNO = spNO;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

}