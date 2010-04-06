package com.nci.ums.periphery.parser;


import org.apache.regexp.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  �ֶ�Ϊ��У��
 */
public class NotNullRule extends AbstractRule {
    private static RE re;                            // У��������ʽ

    public NotNullRule(String checkString) {
        super(checkString);
        try {
            re = new RE("[^\\s]+");
        }
        catch (Exception ex) {
            Res.log(Res.ERROR, "Create not null regular express failed." + ex.getMessage());
            Res.logExceptionTrace(ex);
        }
    }

     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        //Res.log(Res.DEBUG, checkString + ": " + args[0]);
        if(!re.match(checkString))
            throw new ParserException("1021", Res.getStringFromCode("1021", args[0]));
        return true;
    }

}