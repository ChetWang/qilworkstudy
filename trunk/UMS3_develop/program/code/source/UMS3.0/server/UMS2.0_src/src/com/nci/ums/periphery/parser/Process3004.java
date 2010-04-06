package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.util.*;
import com.nci.ums.channel.outchannel.MsgInfo;
import com.nci.ums.periphery.core.CacheDataHelper;
import com.nci.ums.periphery.exception.*;
/**
 *  1004交易的处理层
 */
public class Process3004 implements Processor {
    private Map map;


    public String process(Map map, int count, String serial) throws ProcessException {
        this.map = map;
        return processOne(map, count, serial);
    }

    /**
     *  处理单笔回复
     */
    public String processOne(Map map, int count, String serial) throws ProcessException {
        String result="0001";

        try{
            String subApp=value("SUBAPP").trim();
            String appID=value("APP").trim();
        	MsgInfo msgInfo=CacheDataHelper.getMsg(appID,subApp);
        	StringBuffer sb=new StringBuffer();
        	if(msgInfo!=null){
                sb.append("200100").append(Util.getFixedString(msgInfo.getBatchNO(),14)).append(Util.getFixedString(Integer.toString(msgInfo.getSerialNO()),8)).
					append(Util.getFixedString(Integer.toString(msgInfo.getRetCode()),4)).append(Util.getFixedString(msgInfo.getAppSerialNo(),35)).append(Integer.toString(msgInfo.getAck())).
					append(Util.getFixedString(msgInfo.getRecvID(),15)).
					append(Util.getFixedString(msgInfo.getSendID(),15)).
					append(Util.getFixedString(msgInfo.getSubmitDate(),8)).
					append(Util.getFixedString(msgInfo.getSubmitTime(),6)).
                	append(Util.getFixedString(Integer.toString(msgInfo.getContentMode()),3)).
					append(Util.getFixedString(msgInfo.getSubApp(),3));

                if(msgInfo.getContentMode()!=4&&msgInfo.getContentMode()!=21)
                	sb.append(Util.getFixedString(Util.convertFullToHalf(msgInfo.getContent()),280));
                else
                	sb.append(Util.getFixedString(msgInfo.getContent(),280));
                        
            }else{
                sb.append("1162");
            }

            result=sb.toString();
        }catch(Exception e ){
        	e.printStackTrace();
            result="1091";
            Res.log(Res.ERROR,"process3004 error:"+e);
            Res.logExceptionTrace(e);
        }
        return result;
    }

    private DBConnect getConn() throws ProcessException {
        try {
            DBConnect db = new DBConnect();
            return db;
        }
        catch (Exception ex) {
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