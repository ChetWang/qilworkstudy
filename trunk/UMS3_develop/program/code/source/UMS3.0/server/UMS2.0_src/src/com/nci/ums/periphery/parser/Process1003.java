package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  1001���׵Ĵ����
 */
public class Process1003 implements Processor {
    private Map map;

    public String process(Map map, int count, String serial) throws ProcessException {
        this.map = map;
        return processOne(map, count, serial);
    }

    /**
     *  ����Ӧ�ó����¼
     */
    public String processOne(Map map, int count, String serial) throws ProcessException {
        String result="0000";
        return result;
    }

    private String value(String key) {
        if(map != null)
            return Res.getValue(map, key);
        else
            return "";
    }


}