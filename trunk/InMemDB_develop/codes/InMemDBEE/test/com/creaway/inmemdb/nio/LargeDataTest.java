package com.creaway.inmemdb.nio;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.creaway.inmemdb.remote.action.ProcessHandler;
import com.creaway.inmemdb.remote.client.UserMessage;
import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.ProtocolUtils;

public class LargeDataTest {

	public LargeDataTest() throws Exception {
		Selector sel = Selector.open();
		SocketChannel sChannel = SocketChannel.open();
		sChannel.configureBlocking(false);
		sChannel.connect(new InetSocketAddress("localhost", 10017));

		int intrestOps = SelectionKey.OP_READ | SelectionKey.OP_CONNECT;
		SelectionKey key = sChannel.register(sel, intrestOps);
		// Thread.sleep(1000);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(UserMessage.HEAD_LOGIN);

		String user = "qil.wong";

		byte[] pswb = new byte[32];
		String psw = Functions.md5("how sweet");
		System.arraycopy(psw.getBytes(), 0, pswb, 0, psw.getBytes().length);
		buf.put(Functions.int2Bytes(user.getBytes().length));
		buf.put(user.getBytes());
		buf.put(pswb);
		
		buf.flip();
		sChannel.write(buf);
		ByteBuffer fileByteBuffer = ByteBuffer.allocate(150000);
		FileInputStream fin = new FileInputStream("D:/axis2.xml");
		// FileOutputStream fout = new FileOutputStream("outfile.txt");

		FileChannel inc = fin.getChannel();
		// FileChannel outc = fout.getChannel();

		// ByteBuffer bb = ByteBuffer.allocate(1024);
		int size = 0;
		while (true) {
			int ret = inc.read(fileByteBuffer);
			fileByteBuffer.flip();
			if (ret == -1)
				break;
			else
				size = ret;
			// outc.write(bb);

		}
		byte[] b = new byte[size];

		ByteBuffer b1 = ByteBuffer.allocate(250000);
		b1.put(UserMessage.HEAD_DATA);
		b1.put(ProtocolUtils.getCommand(1000));// 功能标记（2字节）
		b1.put(ProtocolUtils.getSerial(2000));// 命令序号（2字节）
		b1.put(ProtocolUtils.getSessionID(3000));// Session ID（4字节）
		b1.put(ProtocolUtils.getDataLength(size));// 数据长度（4字节）

		System.arraycopy(fileByteBuffer.array(), 0, b, 0, size);
		b1.put(b);
		fileByteBuffer.flip();
		b1.flip();
		sChannel.write(b1);
		b1.flip();
		// sChannel.socket().getOutputStream().write(RemoteServer.HEAD_DATA);
		// System.out.println(Functions.byte2hex(b1.array()));
		fileByteBuffer.clear();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new LargeDataTest();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
