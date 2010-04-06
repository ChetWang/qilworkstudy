package com.nci.ums.periphery.parser;

/**
 * 封装一个字段的信息
 */
public class Item {
    private String name;         //字段名称
    private int length;          //字段长度
    private boolean isLoopBegin; //是否循环字段起始点

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