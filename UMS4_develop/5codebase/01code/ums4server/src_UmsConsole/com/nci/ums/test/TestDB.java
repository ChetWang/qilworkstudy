/*
 * �������� 2006-3-24
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.nci.ums.test;

/**
 * @author Administrator
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class TestDB {

    public static String byteArr2HexStr(byte[] arrB)
    throws Exception
{
    int iLen = arrB.length;
    //ÿ��byte�������ַ����ܱ�ʾ�������ַ����ĳ��������鳤�ȵ�����
    StringBuffer sb = new StringBuffer(iLen * 2);
    for (int i = 0; i < iLen; i++)
    {
        int intTmp = arrB[i];
        //�Ѹ���ת��Ϊ����
        while (intTmp < 0){
            intTmp = intTmp + 256;
        }
        //С��0F������Ҫ��ǰ�油0
        if (intTmp < 16){
            sb.append("0");
        }
        sb.append(Integer.toString(intTmp, 16));
    }
    return sb.toString();
}


/**
 * ����ʾ16����ֵ���ַ���ת��Ϊbyte���飬
 * ��public static String byteArr2HexStr(byte[] arrB)
 * ��Ϊ�����ת������
 * @param strIn ��Ҫת�����ַ���
 * @return ת�����byte����
 * @throws Exception �������������κ��쳣�������쳣ȫ���׳�
 * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
 */
public static byte[] hexStr2ByteArr(String strIn)
    throws Exception
{
    byte[] arrB = strIn.getBytes();
    int iLen = arrB.length;

	//�����ַ���ʾһ���ֽڣ������ֽ����鳤�����ַ������ȳ���2
    byte[] arrOut = new byte[iLen / 2];
    for (int i = 0; i < iLen; i = i + 2)
    {
        String strTmp = new String(arrB, i, 2);
        arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
    }
    return arrOut;
}
	
	
	
/**
 * ��ָ��byte������16���Ƶ���ʽ��ӡ������̨
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
 * ������ASCII�ַ��ϳ�һ���ֽڣ�
 * �磺"EF"--> 0xEF
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
 * ��ָ���ַ���src����ÿ�����ַ��ָ�ת��Ϊ16������ʽ
 * �磺"2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
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
        	
        	String msg="tesaaass��t";
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
