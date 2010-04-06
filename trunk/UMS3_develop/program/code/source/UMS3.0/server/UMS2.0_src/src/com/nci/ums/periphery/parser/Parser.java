package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

/**
 *  ����Բ�ͬ�Ľ��״�����н������ҵ�����Ӧ�Ĵ��������д���
 */
public interface Parser {
    /**
     * �ɹ��򷵻سɹ���Ϣ����0000���룬ʧ�����׳�ParserException�쳣
     */
    public String parser(byte[] parserString, String serial) throws ParserException;

    /**
     * ����process����д���CISֱ�ӵ��ã������¿��߳�
     */
    public String process(final Map map, final int count, final String serial)
        throws ProcessException ;
}