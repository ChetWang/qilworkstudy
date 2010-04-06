package com.nci.ums.test;

import java.sql.ResultSet;

import com.nci.ums.periphery.exception.ProcessException;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.Res;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2003-5-13
 * Time: 11:18:55
 * To change this template use Options | File Templates.
 */
public class TestUpdate {

    public void process() throws ProcessException {
        DBConnect db = null;
        try {
            db = getConn();
            ResultSet  rs=db.executeQuery("select * from wcm.AJPE");
            while(rs.next()){
            	System.out.println(rs.getString("XML_CONTENT"));
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
        TestUpdate process = new TestUpdate();
        try{
            process.process();
        }catch(Exception e){}
    }


}
