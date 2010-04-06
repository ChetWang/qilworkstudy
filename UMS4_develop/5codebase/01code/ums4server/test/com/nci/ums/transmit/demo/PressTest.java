package com.nci.ums.transmit.demo;

import java.io.IOException;
import java.net.UnknownHostException;

import com.nci.ums.transmit.client.TransmitClient;

public class PressTest {

	private int address;

	private int type;
	
	private TransmitClient client;

	public PressTest(int address, int type) {
		this.address = address;
		client = new TransmitClient("192.168.0.56", 10234,
				address, type);
		try {
			client.connect();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public void test(){
//		client.
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
