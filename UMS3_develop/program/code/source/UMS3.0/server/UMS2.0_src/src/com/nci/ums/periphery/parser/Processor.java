package com.nci.ums.periphery.parser;

import java.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  负责对不同的交易代码进行处理
 */
public interface Processor {
    /**
     * 成功则返回成功信息或者0000代码，失败则抛出ProcessException异常
     */
    public String process(Map map, int count, String serial) throws ProcessException;
}