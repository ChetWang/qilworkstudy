package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

public interface Rule {
    /**
     * У��ʧ�ܣ��׳�У���쳣���ɹ��򷵻�true
     * count Ϊѭ������
     */
    public boolean check(Map map, int count) throws ParserException;
}