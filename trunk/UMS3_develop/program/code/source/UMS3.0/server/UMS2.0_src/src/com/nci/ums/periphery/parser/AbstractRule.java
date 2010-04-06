package com.nci.ums.periphery.parser;

import java.util.*;


import com.nci.ums.periphery.exception.*;

/**
 *  У�������
 */
public abstract class AbstractRule implements Rule {
    private static final String suffix = "\\$i";   // ѭ���ֶ�
    /**
     *  У���ַ��������԰���ѭ��У��,��ʽΪ
     *           %FieldName + suffix
     */
    private String checkString;

    public AbstractRule(String checkString) {
        this.checkString = checkString;
    }

    /**f
     *  ��������ڣ����ÿ��ַ����������
     */
    public boolean check(Map map, int count) throws ParserException {
        if(count == 0) count = 1;             //����У��һ��
        for(int i = 0; i < count; i++) {
            String[] args = new String[10];   //��¼��������,���10��
            String[] vals = new String[10];   //��¼����ȡֵ,���10��
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