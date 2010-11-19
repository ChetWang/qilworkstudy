package com.creaway.inmemdb.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPTest {

	public TCPTest() throws Exception {
		ServerSocket server = new ServerSocket(3333);
		while (true) {
			Socket socket = server.accept();
			new ServerHandler(socket).start();
		}
	}

	public class ServerHandler extends Thread {

		Socket socket;

		public ServerHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out
					.println("Server side of client port:" + socket.getPort());
		}
	}

	public static class ClientHandler extends Thread {
		public void run() {
			try {
				
				Socket client = new Socket("localhost", 7777);
				System.out.println("Client Side connect port:"
						+ client.getPort());
				for (int i = 0; i < 5; i++) {
					client.getOutputStream().write("hello".getBytes());
					Thread.sleep(1000);
				}
				Thread.sleep(Integer.MAX_VALUE);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] xxx) {
		new ClientHandler().start();
	}

}
