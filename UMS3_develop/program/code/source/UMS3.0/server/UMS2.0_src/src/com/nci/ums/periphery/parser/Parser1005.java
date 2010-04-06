package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

/**
 *  处理应用程序登录UMS,校验密码处理过程
 */
public class Parser1005
    extends ParserIMP {
  private Processor process;

   
public String process(final Map map, final int count, final String serial) throws
      ProcessException {
    String ret="0000";
    process = new Process1005();
    ret=process.process(map, count, serial);
    return ret;
  }

   
protected Item[] initItems() {
    Item[] items1 = {
        new Item("SUBTYPE", 2),
        new Item("BATCHNO", 14),
        new Item("SERIALNO", 8)
    };
    return items1;
  }

   
protected void initRules() {
  }

}