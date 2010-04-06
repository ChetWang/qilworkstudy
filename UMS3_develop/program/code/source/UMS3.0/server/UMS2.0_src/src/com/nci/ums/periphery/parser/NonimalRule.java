package com.nci.ums.periphery.parser;


import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  字段为一个取值集合校验
 */
public class NonimalRule extends AbstractRule {
    private String[] nonimals; //字段的限定取值集合

    public NonimalRule(String checkString, String[] nonimals) {
        super(checkString);
        this.nonimals = nonimals;
    }

     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        //Res.log(Res.DEBUG, checkString + ": " + args[0]);
        if(checkString == null || checkString.length() <= 0)
            return true;
        for(int i = 0; i < nonimals.length; i++) {
            if(nonimals[i].equalsIgnoreCase(checkString))
                return true;
        }
        error(args[0], vals[0], getNonimals());
        return true;
    }

    private void error(String arg1, String arg2, String arg3) throws ParserException {
        throw new ParserException("1023", Res.getStringFromCode("1023",arg1,arg2,arg3));
    }

    private String getNonimals() {
        String ret = "{ ";
        for(int i = 0; i < nonimals.length; i++) {
            ret = ret + nonimals[i];
            if(i == nonimals.length -1)
                ret = ret + " }";
            else
                ret = ret + ", ";
        }
        return ret;
    }

}