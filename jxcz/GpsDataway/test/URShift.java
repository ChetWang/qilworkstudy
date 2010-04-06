import nci.gps.util.CharCoding;


public class URShift {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int i = -1;
		System.out.println(i >>>= 10);
		long l = -1;
		System.out.println(l >>>= 10);
		byte b = 1;
		System.out.println(b <<= 1);
		System.out.println(b <<= 1);
		
		i = 1000;
		byte[] bs = new byte[2];
		bs[0] = (byte) i;
		bs[1] = (byte) (i >> 8);
		System.out.println(CharCoding.byte2hex(bs));
		
		b = (byte) (bs[0] + bs[1]);
		System.out.println(CharCoding.byte2hex(new byte[]{b}));
		
		String strData = "I am zhouhm";
		byte[] data = strData.getBytes();
		System.out.println(CharCoding.byte2hex(data));
		
		// 68 A1B1C1D1 A01E 68 21 00 0B 4920616D207A686F75686D
		byte[] data2 = { 0x68, (byte) 0xA1, (byte) 0xB1, (byte) 0xC1,
				(byte) 0xD1, (byte) 0xA0, 0x1E, 0x68, 0x21, 0x00, 0x0B, 0x49,
				0x20, 0x61, 0x6D,0x20,0x7A,0x68,0x6F,0x75,0x68,0x6D }; 
		System.out.println(CharCoding.byte2hex(data2));
		int num = 0;
		for(int j=0; j<data2.length; j++){
			num += data2[j];
		}
		b = (byte) num;
		System.out.println(CharCoding.byte2hex(new byte[]{b}));
		
		bs = new byte[]{0x01,0x03};
		i = bs[1]*256 + bs[0];
		System.out.println(Integer.toString(i));
		
		b = (byte) 0xff;
//		System.out.println(CharCoding.byte2hex(new byte[]{(byte) (b<<2)}));
		byte bb = (byte) (b<<2);
		bb >>>= 2;
		System.out.println(CharCoding.byte2hex(new byte[]{bb}));
		
		b = (byte) 0xff;
		bb = (byte) (b & 0x3f);
		System.out.println(CharCoding.byte2hex(new byte[]{bb}));
		
		String st = "¼ÎÐË¾ÖNCI";
		bs = st.getBytes();
		System.out.println(CharCoding.byte2hex(bs));
	}

}
