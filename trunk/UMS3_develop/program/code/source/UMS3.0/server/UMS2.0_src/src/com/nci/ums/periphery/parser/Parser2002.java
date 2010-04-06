package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.exception.*;

/**
 *  处理1002交易，处理单笔外拨请求
 */
public class Parser2002
    extends ParserIMP {
  private Processor process;

   
public String process(final Map map, final int count, final String serial) throws
      ProcessException {
      process = new Process1002();
      String ret = "0000";
      ret = process.process(map, count, serial);

      // 返回成功信息
      return ret;
  }

   
protected Item[] initItems() {
    Item[] items1 = {
        new Item("SUBTYPE", 2),
        new Item("APP", 12),
        new Item("ID", 35),
        new Item("MESSAGETYPE", 3),
        new Item("UMS_TO", 255),
        new Item("MSG", 150),
        new Item("ACK", 1),
        new Item("REPLY", 30),
        new Item("PRIORITY", 2),
        new Item("REP", 2)

    };
    return items1;
  }

   
protected void initRules() {
    /*rules.addElement(new NotNullRule("%SUBTYPE"));
    rules.addElement(new NotNullRule("%MESSAGETYPE"));*/

    rules.addElement(new NotNullRule("%APP"));
    rules.addElement(new NotNullRule("%UMS_TO"));
    rules.addElement(new NotNullRule("%MSG"));

  }

  public static void main(String[] args) {
    String serial = "000001";
    try {
      // 批量外拨
      serial = SerialNO.getInstance().getSerial();
        byte[] bPars=new byte[1024];

       System.out.println(serial);
      StringBuffer sb=new StringBuffer("");
      sb.append(Util.getFixedString("01002",14)).append(Util.getFixedString("001",20)).append(Util.getFixedString("1",3)).append(Util.getFixedString("13575471065",255)).append(Util.getFixedString("请回复",150)).append("1").append("0").append(Util.getFixedString("",8)).append(Util.getFixedString("",6)).append(Util.getFixedString("",8)).append(Util.getFixedString("",6)).append(Util.getFixedString("1",2)).append(Util.getFixedString("1",2));

       bPars=Util.getMaxByteString(sb.toString());
      System.out.println(sb.toString()+":"+sb.toString().length());
      System.out.println(new Parser1002().parser(bPars, serial));
    }
    catch (Exception ex) {
      System.out.println("错误error:"+ex.getMessage());
    }
  }

}