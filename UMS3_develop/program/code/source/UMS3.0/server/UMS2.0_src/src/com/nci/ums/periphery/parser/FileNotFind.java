package com.nci.ums.periphery.parser;

import java.io.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  文件存在校验
 */
public class FileNotFind extends AbstractRule {

    // checkString 记录文件
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