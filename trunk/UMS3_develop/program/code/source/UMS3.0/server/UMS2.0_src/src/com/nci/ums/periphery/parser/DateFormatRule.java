package com.nci.ums.periphery.parser;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  字段日期校验
 */
public class DateFormatRule extends AbstractRule {

    public DateFormatRule(String checkString) {
        super(checkString);
    }

    /**
     *  校验是否为有效的YYYYMMDD日期格式
     */
     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        //Res.log(Res.DEBUG, checkString + ": " + args[0]);
        if(checkString == null || checkString.length() <= 0)
            return true;
        int year, month, day;
        year = number(checkString.substring(0,4), args, vals);
        month = number(checkString.substring(4,6), args, vals);
        day = number(checkString.substring(6), args, vals);

        if(month < 1 || month > 12 || day < 1 || day > 31)
            throw new ParserException("1022", Res.getStringFromCode("1022", args[0], vals[0]));
        if (!((year % 4)==0) && (month==2) && (day==29))
            throw new ParserException("1022", Res.getStringFromCode("1022", args[0], vals[0]));
        if ((month<=7) && ((month % 2)==0) && (day>=31))
            throw new ParserException("1022", Res.getStringFromCode("1022", args[0], vals[0]));
        if ((month>=8) && ((month % 2)==1) && (day>=31))
            throw new ParserException("1022", Res.getStringFromCode("1022", args[0], vals[0]));
        if ((month==2) && (day==30))
            throw new ParserException("1022", Res.getStringFromCode("1022", args[0], vals[0]));

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