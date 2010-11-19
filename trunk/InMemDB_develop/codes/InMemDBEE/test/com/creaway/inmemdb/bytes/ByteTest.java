package com.creaway.inmemdb.bytes;

import java.math.BigInteger;

import com.creaway.inmemdb.util.Functions;

import sun.security.util.BigInt;

public class ByteTest {

	public static int twoBytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		if (bytes.length >= 2)
			num |= ((bytes[1] << 8) & 0xFF00);
		return num;
	}

	public static int bytesToInt(byte[] bRefArr) {
		int iOutcome = 0;
		for (int i = 0; i < bRefArr.length; i++) {
			iOutcome += (bRefArr[i] & 0xFF) << (8 * i);
		}
		return iOutcome;
	}
	
	  public static byte[] toByteArray(int iSource, int iArrayLen) {
	        byte[] bLocalArr = new byte[iArrayLen];
	        for ( int i = 0; i < iArrayLen; i++) {
	            bLocalArr[i] = (byte)( iSource>>8*i & 0xFF );
	          
	        }
	        return bLocalArr;
	    }  

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		byte[] bs = new byte[2];
//		bs[0] = (byte) 0xFF;
//		bs[1] = (byte) 0xFF;
////		bs[2] = (byte) 0xFF;
////		bs[3] = (byte) 0xFF;
////		bs[4] = (byte) 0x43;
//		int result = bytesToInt(bs);
//		System.out.println(result);
//		int result2 = new BigInt(bs).toInt();
//		System.out.println(result2);
//		byte[] b = new BigInt(result2).toByteArray();
//		System.out.println(toByteArray(result,bs.length));
		
		System.out.println(Functions.md5("testtest"));
	}

}
