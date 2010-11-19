package com.creaway.inmemdb.remote.client;

import java.io.IOException;

public class ClientNotConnectedException extends IOException{
	
	public ClientNotConnectedException(){
		super("TCP客户端未连接");
	}

}
