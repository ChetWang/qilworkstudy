package com.creaway.inmemdb.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.creaway.inmemdb.remote.client.UserMessage;
import com.creaway.inmemdb.util.Functions;

public class LoginTest {

	public LoginTest() throws Exception {
		SocketChannel sChannel = SocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.connect(new InetSocketAddress("localhost", 10017));
		sChannel.finishConnect();
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		buf.put(UserMessage.HEAD_LOGIN);

		String user = "qil.wong";

		byte[] pswb = new byte[32];
		String psw = Functions.md5("what a shit");
		System.arraycopy(psw.getBytes(), 0, pswb, 0, psw.getBytes().length);
		buf.put(pswb);
		buf.put(user.getBytes());
		buf.flip();

		int numBytesWritten = sChannel.write(buf);
		Thread.sleep(Integer.MAX_VALUE);

	}

	public static void main(String[] x) throws Exception {
		new LoginTest();
	}

}
