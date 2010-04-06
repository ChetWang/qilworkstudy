package com.nci.ums.test;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.ProcessException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class testclient
 {

    public void process() throws ProcessException {
        DBConnect db = null;
        try {
            db = getConn();
            for(int i=300001;i<400000;i++)
            {
                StringBuffer sb=new StringBuffer("insert into a01(id,leader_code,a0101,a0117,a0132,a0199_1,file_size,create_id,update_id,unused4) values(");
                sb.append(i).append(",'").append("00").append(i).append("','test','11','330000010000',0,0,0,0,0").append(")");
                db.executeUpdate(sb.toString());
            }
        }catch (Exception ex) {
            Res.log(Res.ERROR,"处理交易1002出错:"+ex.toString());
            try {
                db.close();
            } catch (Exception e) { }
        }finally {
            if(db!=null)try { db.close(); } catch (Exception e) { }
        }
    }

    private DBConnect getConn() throws ProcessException {
        try {
            DBConnect db = new DBConnect();
            return db;
        }
        catch (Exception ex) {
            Res.log(Res.ERROR,"连接池出错"+ex);
            throw new ProcessException("1091", Res.getStringFromCode("1091", ex.getMessage()));
        }
    }

    public static void main(String[] args) {
        testclient process = new testclient();
        try{
            process.process();
        }catch(Exception e){}
    }

}
