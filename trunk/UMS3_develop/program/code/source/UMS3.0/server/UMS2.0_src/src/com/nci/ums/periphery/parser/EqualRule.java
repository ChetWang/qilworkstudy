package com.nci.ums.periphery.parser;


import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  字段为某个取值校验
 */
public class EqualRule extends AbstractRule {
    private String value;     //比较取值

    public EqualRule(String checkString, String value) {
        super(checkString);
        this.value = value;
    }

     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        //Res.log(Res.DEBUG, checkString + ": " + args[0]);
        if(!checkString.equalsIgnoreCase(value))
            throw new ParserException("1026", Res.getStringFromCode("1026", args[0], vals[0], value));
        return true;
    }

}