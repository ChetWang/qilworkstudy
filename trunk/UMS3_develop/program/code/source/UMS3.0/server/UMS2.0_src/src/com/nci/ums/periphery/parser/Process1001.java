package com.nci.ums.periphery.parser;

import java.util.*;
import java.sql.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  1001交易的处理层
 */
public class Process1001 implements Processor {
    private Map map;

    public String process(Map map, int count, String serial) throws ProcessException {
        this.map = map;
        return processOne(map, count, serial);
    }

    /**
     *  处理应用程序登录
     */
    public String processOne(Map map, int count, String serial) throws ProcessException {
        String result="1041";
        DBConnect db =null;
        ProcessException pex = null;
        ResultSet rs = null;
        try {
                db = getConn();
                rs=db.executeQuery("select * from application where appID='"+ value("APPID").trim()+"' and password='"+value("PASSWORD").trim()+"'");
                if(rs.next()){
                    result= "0000";
                    
                }else{
                    result="1041";
                }
                Res.log(Res.DEBUG,"处理1001交易成功"+value("APPID").trim());
        }catch (Exception ex) {
            Res.log(Res.ERROR,"处理1001交易出错"+ex);
            Res.logExceptionTrace(ex);
            pex = new ProcessException("1041", Res.getStringFromCode("1041"));
        }finally {
            if(rs != null) try { rs.close(); } catch (Exception e) { }
            if(db != null)try { db.close(); } catch (Exception e) { }
        }
        if(pex != null)
            throw pex;
        return result;
    }

    private DBConnect getConn() throws ProcessException {
        try {
            DBConnect db = new DBConnect();
            return db;
        }catch (Exception ex) {
            Res.log(Res.ERROR,"连接池出错"+ex);
            Res.logExceptionTrace(ex);
            throw new ProcessException("1091", Res.getStringFromCode("1091", ex.getMessage()));
        }
    }

    private String value(String key) {
        if(map != null)
            return Res.getValue(map, key);
        else
            return "";
    }


}