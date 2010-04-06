package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

/**
 *  处理1004交易，处理接收消息报文
 */
public class Parser3004
    extends ParserIMP {
  private Processor process;

   
public String process(final Map map, final int count, final String serial) throws
      ProcessException {
      process = new Process3004();
      String ret = "0000";
      ret = process.process(map, count, serial);

      // 返回成功信息
      return ret;
  }

   
protected Item[] initItems() {
    Item[] items1 = {
        new Item("SUBTYPE", 2),
        new Item("APP", 12),
        new Item("SUBAPP", 3)

    };
    return items1;
  }

   
protected void initRules() {

    rules.addElement(new NotNullRule("%APP"));

  }


}