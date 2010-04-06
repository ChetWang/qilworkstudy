package com.nci.ums.transmit.db;

import junit.framework.TestCase;

public class RecordMessageTest extends TestCase {

	public RecordMessageTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRecordSuccessMessage() {
		for(int i=0;i<100; i++){
			System.out.print(i+":");
			testSaveMessage();
		}
	}
	
	private void testSaveMessage(){
//		String source = "1001";
//		String target = "1003";
//		String receiveTime = "2009-08-26 14:31:22";
//		String sentTime = "2009-08-26 14:31:33";
//		byte[] content = { 0x68, (byte) 0xE9, 0x03, (byte) 0xF1, 0x03, 0x63,
//				0x00, 0x68, (byte) 0xA1, 0x00, 0x00, (byte) 0xB4, 0x16 };
//		long startTime = System.currentTimeMillis();
//		RecordMessage rm = new RecordMessage();
//		boolean saveFlag = rm.recordSuccessMessage(source, target, receiveTime,
//				sentTime, content);
//		long stopTime = System.currentTimeMillis();
//		System.out.println("saveFlag:" + saveFlag);
//		System.out.println("保存花费时间为：" + (stopTime - startTime));
	}

}
