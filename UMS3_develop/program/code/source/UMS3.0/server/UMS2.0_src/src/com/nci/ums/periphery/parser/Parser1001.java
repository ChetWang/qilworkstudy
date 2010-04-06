package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  处理应用程序登录UMS,校验密码处理过程
 */
public class Parser1001
    extends ParserIMP {
  private Processor process;

   
public String process(final Map map, final int count, final String serial) throws
      ProcessException {
    String ret="0000";
    process = new Process1001();
    ret=process.process(map, count, serial);
    return ret;
  }

   
protected Item[] initItems() {
    Item[] items1 = {
        new Item("SUBTYPE", 2),
        new Item("APPID", 12),
        new Item("PASSWORD", 20),
    };
    return items1;
  }

   
protected void initRules() {
    rules.addElement(new NotNullRule("%APPID"));
    rules.addElement(new NotNullRule("%PASSWORD"));
  }

  public static void main(String[] args) {
    try {
        StringBuffer sb=new StringBuffer("00002");
       sb.append(Util.getFixedString("111121",6));
      System.out.println(sb.length());
      System.out.println(sb.toString());
      //String ret=new Parser1001().parser(sb.toString(), serial);
       //System.out.println(Res.getMessage(ret, serial ));
    }
    catch (Exception ex) {
      Res.log(Res.ERROR, ex.getMessage());
      Res.logExceptionTrace(ex);
    }
  }
}