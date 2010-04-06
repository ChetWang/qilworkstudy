package com.nci.ums.periphery.parser;

import java.io.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  �ļ�����У��
 */
public class FileNotFind extends AbstractRule {

    // checkString ��¼�ļ�
    public FileNotFind(String checkString) {
        super(checkString);
    }

     
	protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {
        String temp = checkString.replaceAll(" ", "");
        File file = new File(temp);
        if(!file.exists())
            throw new ParserException("1027", Res.getStringFromCode("1027", temp));
        return true;
    }

}