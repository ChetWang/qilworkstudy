package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;


public class ParserIMP implements Parser {
    private Item[] items;      // ��¼�ֶ���Ϣ�������д�
    private int count;         // ��¼ѭ������
    public Map map;           // ��¼�ֶ��� -> ֵ ��ӳ�䣬����У��
    protected Vector rules;      // ��¼У�����

    public ParserIMP() {
        map = new HashMap();
        rules = new Vector();
        count = 0;
        init();
    }

    public String parser(byte[] bpars,String serial) throws ParserException {
        // �д�
        String temp=new String("");
        int i = 0;
        int j = 0;
        //Res.log(Res.DEBUG,"parserIMP ok!");
        // ����ѭ���ֶΣ����ȱ��һ����������������
        while(i < items.length && (bpars.length - j) >= items[i].length()) {

            try{            	
                temp = new String(bpars, j, items[i].length(),"GB2312");
            }catch(Exception e ){}
            j = j + items[i].length();            
            map.put(items[i].getName(), temp);    
            i++;
        }
        // У��

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