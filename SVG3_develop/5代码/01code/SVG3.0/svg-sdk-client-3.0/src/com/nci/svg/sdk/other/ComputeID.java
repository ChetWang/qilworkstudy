/**
 * ������com.nci.svg.other.ComputeID
 * ������:yx
 * �������ڣ�2008-5-5
 * ������:�����������豸ͼԪ��ΨһID��
 * �޸���־��
 */
package com.nci.svg.sdk.other;

import java.util.Date;
/**
 * @author yx
 *
 */
public class ComputeID {
    public static String getTextID()
    {
        String strID = "Text";
        strID += getID();
        return strID;
    }
    
    public static String getLineID()
    {
        String strID = "Line";
        strID += getID();
        return strID;
    }
    
    public static String getSymbolID()
    {
        String strID = "Symbol";
        strID += getID();
        return strID;
    }
    
    public static String getID()
    {
        String strID = new String();
        Date date = new Date();
        strID = strID.format("%d", date.getTime());
        return strID;
        
    }
}
