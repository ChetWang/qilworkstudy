package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;


public class ParserIMP implements Parser {
    private Item[] items;      // 记录字段信息，用于切词
    private int count;         // 记录循环次数
    public Map map;           // 记录字段名 -> 值 的映射，用于校验
    protected Vector rules;      // 记录校验规则集

    public ParserIMP() {
        map = new HashMap();
        rules = new Vector();
        count = 0;
        init();
    }

    public String parser(byte[] bpars,String serial) throws ParserException {
        // 切词
        String temp=new String("");
        int i = 0;
        int j = 0;
        //Res.log(Res.DEBUG,"parserIMP ok!");
        // 对于循环字段，如果缺少一个，整个将被忽略
        while(i < items.length && (bpars.length - j) >= items[i].length()) {

            try{            	
                temp = new String(bpars, j, items[i].length(),"GB2312");
            }catch(Exception e ){}
            j = j + items[i].length();            
            map.put(items[i].getName(), temp);    
            i++;
        }
        // 校验

        return process(map, count, serial);
    }

    public String process(final Map map, final int count, final String serial)
        throws ProcessException {
        return "0000";
    }

    private void init() {
        items = initItems();
        initRules();
    }

    protected Item[] initItems() {
        return null;
    }

    protected void initRules() {
    }
}