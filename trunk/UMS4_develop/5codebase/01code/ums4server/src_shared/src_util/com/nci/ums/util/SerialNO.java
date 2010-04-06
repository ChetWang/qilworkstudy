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

public class SerialNO {

    private int serialNO = 0;
    static private SerialNO serialObj;

    /*
     * 工厂方法得到唯一实例
     */
    static synchronized public SerialNO getInstance() {
        if (serialObj == null) {
            serialObj = new SerialNO();
        }
        return serialObj;
    }

    public synchronized String getSerial() {
        if (serialNO < 1000000) {
            serialNO = serialNO + 1;
        } else {
            serialNO = 1;
        }


        return (new Integer(serialNO)).toString();
    }

    
}