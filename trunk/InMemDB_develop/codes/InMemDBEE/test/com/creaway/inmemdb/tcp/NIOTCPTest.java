package com.creaway.inmemdb.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOTCPTest implements Runnable {
	private ByteBuffer r_buff = ByteBuffer.allocate(1024);
	private ByteBuffer w_buff = ByteBuffer.allocate(1024);
	private static int port = 8848;

	public NIOTCPTest() {
		new Thread(this).start();
	}

	public void run() {
		try {
			// 生成一个侦听端
			ServerSocketChannel ssc = ServerSocketChannel.open();
			// 将侦听端设为异步方式
			ssc.configureBlocking(false);
			// 生成一个信号监视器
			Selector s = Selector.open();
			// 侦听端绑定到一个端口
			ssc.socket().bind(new InetSocketAddress(port));
			// 设置侦听端所选的异步信号OP_ACCEPT
			ssc.register(s, SelectionKey.OP_ACCEPT);

			System.out.println("echo server has been set up ......");

			while (true) {
				try{
				int n = s.select();
				if (n == 0) {// 没有指定的I/O事件发生
					continue;
				}
				Iterator it = s.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					if (key.isAcceptable()) {// 侦听端信号触发
						ServerSocketChannel server = (ServerSocketChannel) key
								.channel();
						// 接受一个新的连接
						SocketChannel sc = server.accept();
						sc.configureBlocking(false);
						// 设置该socket的异步信号OP_READ:当socket可读时， BBS.bitsCN.com网管论坛

						// 触发函数DealwithData();
						sc.register(s, SelectionKey.OP_READ);
					}
					if (key.isReadable()) {// 某socket可读信号
						DealwithData(key);
					}
					it.remove();
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DealwithData(SelectionKey key) throws IOException {
		int count;
		// 由key获取指定socketchannel的引用
		SocketChannel sc = (SocketChannel) key.channel();
		r_buff.clear();
		// 读取数据到r_buff
		while ((count = sc.read(r_buff)) > 0)
			;
		// 确保r_buff可读
		r_buff.flip();

		w_buff.clear();
		// 将r_buff内容拷入w_buff
		w_buff.put(r_buff);
		byte[] b = new byte[w_buff.position()];
		
		w_buff.flip();
		
		w_buff.get(b);
		System.out.println(new String(b));
		// 将数据返回给客户端
		EchoToClient(sc);

		w_buff.clear();
		r_buff.clear();
	}

	public void EchoToClient(SocketChannel sc) throws IOException {
		while (w_buff.hasRemaining())
			sc.write(w_buff);
	}

	public static void main(String args[]) {
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		new NIOTCPTest();
	}
}