package com.nci.ums.transmit.common;

import java.net.Socket;

public abstract class SocketCallBackRunnable extends CallBackRunnable {

	protected Socket socket;

	public SocketCallBackRunnable(Socket socket) {
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}

}
