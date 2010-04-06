package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.core.CacheDataHelper;
import com.nci.ums.periphery.exception.*;
/**
 *  1004交易的处理层
 */
public class Process1005 implements Processor {
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
        	String batchNO=value("BATCHNO").trim();
        	String serialNO=value("SERIALNO").trim();
        	CacheDataHelper.response(batchNO,serialNO);
        }catch(Exception e ){
            Res.log(Res.DEBUG,"process1005 error:"+e);
            result="1091";
        }
        return result;
    }



    private String value(String key) {
        if(map != null)
            return Res.getValue(map, key);
        else
            return "";
    }


}