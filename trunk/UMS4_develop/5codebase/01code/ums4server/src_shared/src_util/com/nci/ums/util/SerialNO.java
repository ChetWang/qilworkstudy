/**
 * <p>Title: Util.java</p>
 * <p>Description:
 *    �ṩ���õĺ�����
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   ��־��        Created
 * @version 1.0
 */
package com.nci.ums.util;

public class SerialNO {

    private int serialNO = 0;
    static private SerialNO serialObj;

    /*
     * ���������õ�Ψһʵ��
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