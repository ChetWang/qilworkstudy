/*
 * 创建日期 2006-3-24
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.nci.ums.test;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class TestDB {

    public static String byteArr2HexStr(byte[] arrB)
    throws Exception
{
    int iLen = arrB.length;
    //每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
    StringBuffer sb = new StringBuffer(iLen * 2);
    for (int i = 0; i < iLen; i++)
    {
        int intTmp = arrB[i];
        //把负数转换为正数
        while (intTmp < 0){
            intTmp = intTmp + 256;
        }
        //小于0F的数需要在前面补0
        if (intTmp < 16){
            sb.append("0");
        }
        sb.append(Integer.toString(intTmp, 16));
    }
    return sb.toString();
}


/**
 * 将表示16进制值的字符串转换为byte数组，
 * 和public static String byteArr2HexStr(byte[] arrB)
 * 互为可逆的转换过程
 * @param strIn 需要转换的字符串
 * @return 转换后的byte数组
 * @throws Exception 本方法不处理任何异常，所有异常全部抛出
 * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
 */
public static byte[] hexStr2ByteArr(String strIn)
    throws Exception
{
    byte[] arrB = strIn.getBytes();
    int iLen = arrB.length;

	//两个字符表示一个字节，所以字节数组长度是字符串长度除以2
    byte[] arrOut = new byte[iLen / 2];
    for (int i = 0; i < iLen; i = i + 2)
    {
        String strTmp = new String(arrB, i, 2);
        arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
    }
    return arrOut;
}
	
	
	
/**
 * 将指定byte数组以16进制的形式打印到控制台
 * @param hint String
 * @param b byte[]
 * @return void
 */
public static void printHexString(String hint, byte[] b) {
  System.out.print(hint);
  for (int i = 0; i < b.length; i++) {
    String hex = Integer.toHexString(b[i] & 0xFF);
    if (hex.length() == 1) {
      hex = '0' + hex;
    }
    System.out.print(hex.toUpperCase() + " ");
  }
  System.out.println("");
}

/**
 *
 * @param b byte[]
 * @return String
 */
public static String Bytes2HexString(byte[] b) {
  String ret = "";
  for (int i = 0; i < b.length; i++) {
    String hex = Integer.toHexString(b[i] & 0xFF);
    if (hex.length() == 1) {
      hex = '0' + hex;
    }
    ret += hex.toUpperCase();
  }
  return ret;
}

/**
 * 将两个ASCII字符合成一个字节；
 * 如："EF"--> 0xEF
 * @param src0 byte
 * @param src1 byte
 * @return byte
 */
public static byte uniteBytes(byte src0, byte src1) {
  byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
  _b0 = (byte)(_b0 << 4);
  byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
  byte ret = (byte)(_b0 ^ _b1);
  return ret;
}

/**
 * 将指定字符串src，以每两个字符分割转换为16进制形式
 * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
 * @param src String
 * @return byte[]
 */
public static byte[] HexString2Bytes(String src){
  int length=src.getBytes().length/2;
  byte[] ret = new byte[length];
  byte[] tmp = src.getBytes();
  for(int i=0; i<length; i++){
    ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
  }
  return ret;
}

protected static int Bytes4ToInt(byte abyte0[])
{
    return (0xff & abyte0[0]) << 24 | (0xff & abyte0[1]) << 16 | (0xff & abyte0[2]) << 8 | 0xff & abyte0[3];
}
	
protected static void BytesCopy(byte abyte0[], byte abyte1[], int i, int j, int k)
{
    int i1 = 0;
    for(int l = i; l <= j; l++)
    {
        abyte1[k + i1] = abyte0[l];
        i1++;
    }

}

	private void test(){
        try{
            //NBSmsConnect process = new NBSmsConnect();
            
            /*ResultSet rs=process.executeQuery("select * from SM_DELIVER");
            if(rs.next()){
            	String msg=rs.getString("MSG");
            	System.out.println(hexStr2ByteArr("2401 0043 14060 30e1 00e0")));
            	
            	
            }*/
        	
        	String msg="tesaaass测t";
        	//String content=(Util.getASCII(msg.getBytes(),msg.getBytes().length));
        	//System.out.println(content);
        	String content="240100431406030e101f274e0006333030303133002c4e313633313339414e32392e393435393033453132312e3833363536362b30303030303030303133382e30302a19";
        	System.out.println(new String(convertHexStrToBytes(content),"UTF-16BE"));
        	
 
        }catch(Exception e){
        	e.printStackTrace();
        }
		
	}
	
	
	 public static byte[] convertHexStrToBytes(String srcBuff) {
	    String subBuff;
	    byte ab;
	    try {
	      byte des[]=new byte[srcBuff.length()/2];
	      int j=0;
	      for(int i=0; i<srcBuff.length();i+=2,j++) {
	        subBuff=srcBuff.substring(i,i+2);
	        ab=(byte)Integer.parseInt(subBuff,16);
	        des[j]=ab;
	      }  return des;
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	
    public static void main(String[] args) {
        try{
        	//DBConnect connect=new DBConnect();
            TestDB test=new TestDB();
            test.test();
        }catch(Exception e){
        	e.printStackTrace();
        }

}
	
}
