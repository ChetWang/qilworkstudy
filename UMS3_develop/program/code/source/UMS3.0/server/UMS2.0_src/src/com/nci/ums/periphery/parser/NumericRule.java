package com.nci.ums.periphery.parser;


import org.apache.regexp.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  �ֶ�Ϊ��ֵУ��
 */
public class NumericRule extends AbstractRule {
    private static RE re;                            // У��������ʽ

    public NumericRule(String checkString) {
        super(checkString);
        try {
            re = new RE("\\D");
        }
        catch (Exception ex) {
            Res.log(Res.ERROR, "Create not null regular express failed." + ex.getMessage());
            Res.logExceptionTrace(ex);
        }
    }

     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        //Res.log(Res.DEBUG, checkString + ": " + args[0]);
        if(checkString == null || checkString.length() <= 0)
            return true;
        if(re.match(checkString))
            throw new ParserException("1024", Res.getStringFromCode("1024", args[0], vals[0]));
        return true;
    }

}