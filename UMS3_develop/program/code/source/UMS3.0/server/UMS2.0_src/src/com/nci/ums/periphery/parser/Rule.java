package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

public interface Rule {
    /**
     * 校验失败，抛出校验异常；成功则返回true
     * count 为循环次数
     */
    public boolean check(Map map, int count) throws ParserException;
}