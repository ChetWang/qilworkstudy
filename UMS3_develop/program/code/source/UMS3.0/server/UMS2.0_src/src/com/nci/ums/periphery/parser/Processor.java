package com.nci.ums.periphery.parser;

import java.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  ����Բ�ͬ�Ľ��״�����д���
 */
public interface Processor {
    /**
     * �ɹ��򷵻سɹ���Ϣ����0000���룬ʧ�����׳�ProcessException�쳣
     */
    public String process(Map map, int count, String serial) throws ProcessException;
}