package com.creaway.inmemdb.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.creaway.inmemdb.remote.action.ProcessHandler;
import com.creaway.inmemdb.remote.client.UserMessage;
import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.ProtocolUtils;

public class ClassicIOTest {

	public ClassicIOTest() throws Exception {
		Socket socket = new Socket("localhost", 10017);
		OutputStream oos = socket.getOutputStream();
		String user = "qil.wong";
		new InThread(socket.getInputStream()).start();
		// byte[] pswb = new byte[32];
//		String psw = Functions.md5("xxss");
//		// System.arraycopy(psw.getBytes(), 0, pswb, 0, psw.getBytes().length);
//		oos.write(UserMessage.HEAD_LOGIN);
//		oos.write(Functions.int2Bytes(user.getBytes().length));
//		oos.write(user.getBytes());
//		oos.write(psw.getBytes());
		for(int i=0;i<100000;i++){
			oos.write(i);
		}
		// writeFile(oos);
		remoteSelect(oos);
	}

	private void remoteSelect(OutputStream oos) throws IOException {
		oos.write(UserMessage.HEAD_DATA);
		oos.write(ProtocolUtils.getCommand(2002));// 功能标记（2字节）
		oos.write(ProtocolUtils.getSerial(2000));// 命令序号（2字节）
		oos.write(ProtocolUtils.getSessionID(3000));// Session ID（4字节）
//		byte[] sql = "SELECT count(*) as qqqqqqqqqqqqq FROM TTTT".getBytes();
		byte[] sql = "delete FROM TTTT".getBytes();

		oos.write(ProtocolUtils.getDataLength(sql.length));// 数据长度（4字节）
		oos.write(sql);
	}

	private void writeFile(OutputStream oos) throws IOException {
		ByteBuffer fileByteBuffer = ByteBuffer.allocate(150000);
		FileInputStream fin = new FileInputStream("D:/axis2.xml");
		// FileOutputStream fout = new FileOutputStream("outfile.txt");

		FileChannel inc = fin.getChannel();
		// FileChannel outc = fout.getChannel();

		// ByteBuffer bb = ByteBuffer.allocate(1024);
		int size = 0;
		while (true) {
			int ret = inc.read(fileByteBuffer);

			if (ret == -1)
				break;
			else
				size = ret;
			// outc.write(bb);

		}
		byte[] b = new byte[size];
		fileByteBuffer.flip();
		fileByteBuffer.get(b, 0, size);

		// ByteBuffer b1 = ByteBuffer.allocate(250000);
		oos.write(UserMessage.HEAD_DATA);
		oos.write(ProtocolUtils.getCommand(1001));// 功能标记（2字节）
		oos.write(ProtocolUtils.getSerial(2000));// 命令序号（2字节）
		oos.write(ProtocolUtils.getSessionID(3000));// Session ID（4字节）
		oos.write(ProtocolUtils.getDataLength(size));// 数据长度（4字节）
		oos.write(b);
	}

	private class InThread extends Thread {

		private InputStream is;

		private InThread(InputStream is) {
			this.is = is;
		}

		public void run() {
			while (true) {
				try {
					byte[] temp = new byte[1024];
					int len = is.read(temp);
					ProtocolUtils.getCommand(temp);
					System.out
							.println("received:  " + new String(temp, 0, len));
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						new ClassicIOTest();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		Thread.sleep(Integer.MAX_VALUE);
	}

}
