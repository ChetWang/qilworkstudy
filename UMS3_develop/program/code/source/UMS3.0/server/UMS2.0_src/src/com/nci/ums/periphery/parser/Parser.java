package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

/**
 *  负责对不同的交易代码进行解析，且调用相应的处理程序进行处理
 */
public interface Parser {
    /**
     * 成功则返回成功信息或者0000代码，失败则抛出ParserException异常
     */
    public String parser(byte[] parserString, String serial) throws ParserException;

    /**
     * 调用process层进行处理，CIS直接调用，其他新开线程
     */
    public String process(final Map map, final int count, final String serial)
        throws ProcessException ;
}