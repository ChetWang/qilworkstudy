package com.nci.ums.periphery.parser;


import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  字段时间校验
 */
public class TimeRule extends AbstractRule {

    public TimeRule(String checkString) {
        super(checkString);
    }

    /**
     *  校验是否为有效的HHMMSS时间格式
     */
     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        //Res.log(Res.DEBUG, checkString + ": " + args[0]);
        if(checkString == null || checkString.length() <= 0)
            return true;
        int hour, minute, second;
        hour = number(checkString.substring(0,2), args, vals);
        minute = number(checkString.substring(2,4), args, vals);
        second = number(checkString.substring(4), args, vals);

        if(hour < 0 || hour > 23 || minute < 0 || minute > 59 ||
           second < 0 || second > 59)
            throw new ParserException("1025", Res.getStringFromCode("1025", args[0], vals[0]));

        return true;
    }

    private int number(String num, String[] args, String[] vals)
            throws ParserException {
        try {
            int value = Integer.parseInt(num);
            return value;
        }
        catch (NumberFormatException ex) {
            throw new ParserException("1022", Res.getStringFromCode("1022", args[0], vals[0]));
        }
    }
}