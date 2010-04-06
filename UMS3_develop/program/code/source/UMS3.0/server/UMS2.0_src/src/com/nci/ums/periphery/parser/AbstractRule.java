package com.nci.ums.periphery.parser;

import java.util.*;


import com.nci.ums.periphery.exception.*;

/**
 *  校验抽象类
 */
public abstract class AbstractRule implements Rule {
    private static final String suffix = "\\$i";   // 循环字段
    /**
     *  校验字符串，可以包含循环校验,格式为
     *           %FieldName + suffix
     */
    private String checkString;

    public AbstractRule(String checkString) {
        this.checkString = checkString;
    }

    /**f
     *  如果不存在，则用空字符串“”替代
     */
    public boolean check(Map map, int count) throws ParserException {
        if(count == 0) count = 1;             //至少校验一次
        for(int i = 0; i < count; i++) {
            String[] args = new String[10];   //记录参数名称,最多10个
            String[] vals = new String[10];   //记录参数取值,最多10个
            String temp;
            // replace $i
            if(checkString.indexOf("$i") == -1) {
                temp = checkString;
            } else {
                temp = checkString.replaceAll(suffix, "" + i);
            }
            int begin = 0;
            int end = 0;
            int j = 0;
            while((begin = temp.indexOf('%', begin)) != -1) {
                end = begin;
                while(++end < temp.length() && temp.charAt(end) != ' ') ;
                String arg = temp.substring(begin, end);
                Object valueOfarg = map.get(arg.substring(1));
                String rep = "";
                if(valueOfarg != null) {
                  rep = ((String) valueOfarg).trim();
                  if(rep == null) rep = "";
                }
                args[j] = arg.substring(1);
                vals[j++] = rep;
                temp = temp.replaceFirst(arg, rep);
            }
            String[] argsTemp = new String[j];
            String[] valsTemp = new String[j];
            System.arraycopy(args, 0, argsTemp, 0, j);
            System.arraycopy(vals, 0, valsTemp, 0, j);
            checkOneRule(temp, argsTemp, valsTemp);
        }
        return true;
    }

    protected boolean checkOneRule(String checkString, String[] args, String[] vals)
           throws ParserException {

        return true;
    }

    private void init(Map map, int count) {

    }
}