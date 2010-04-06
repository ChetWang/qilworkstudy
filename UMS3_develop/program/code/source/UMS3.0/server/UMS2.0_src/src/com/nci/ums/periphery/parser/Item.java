package com.nci.ums.periphery.parser;

/**
 * ��װһ���ֶε���Ϣ
 */
public class Item {
    private String name;         //�ֶ�����
    private int length;          //�ֶγ���
    private boolean isLoopBegin; //�Ƿ�ѭ���ֶ���ʼ��

    public Item(String name, int length) {
        this.name = name;
        this.length = length;
        this.isLoopBegin = false;
    }

    public Item(String name, int length, boolean isLoopBegin) {
        this.name = name;
        this.length = length;
        this.isLoopBegin = isLoopBegin;
    }

    public String getName() { return name; }
    public int length() { return length; }
    public boolean isLoopBegin() { return isLoopBegin; }
}