package com.creaway.inmemdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class IOToolkit {

	/**
	 * 将ByteBuffer中的数据全部发送出去
	 * 
	 * @param sc
	 * @param bb
	 * @throws IOException
	 */
	public static void nioCompleteWrite(SocketChannel sc, ByteBuffer bb)
			throws IOException {
		synchronized (sc) {
			while (bb.hasRemaining()) {
				sc.write(bb);
			}
		}
	}

	/**
	 * 将指定协议数据完整读取过来，这个ByteBuffer对象必须严格定义协议数据大小
	 * 
	 * @param sc
	 * @param bf
	 */
	public static void nioReadFull(SocketChannel sc, ByteBuffer bf)
			throws IOException {
		synchronized (sc) {
			// int readLen = 0;
			// int sum = 0;
			// int zeroReadTime = 0;
			// while (sum < bf.capacity()) {
			// readLen = sc.read(bf);
			//
			// if (readLen == 0) {
			// zeroReadTime++; //
			// 用作数据流间断等待,为避免错误的流，这里设置一个MAX_ZERO_READ_TIMES门槛，防止死循环
			// try {
			// Thread.sleep(10);
			// } catch (InterruptedException e) {
			// }
			// if (zeroReadTime > MAX_ZERO_READ_TIMES) {
			// DBLogger.log(DBLogger.ERROR,
			// "MAX_ZERO_READ_TIMES OCCURED!");
			// break;
			// }
			// }
			// sum += readLen;
			// }
			while (bf.hasRemaining()) {
				sc.read(bf);
			}
		}
	}

	/**
	 * 将数据读进指定字节
	 * 
	 * @param b
	 * @throws IOException
	 */
	public static void readFull(InputStream is, byte[] b) throws IOException {
		synchronized (is) {
			int sumLen = 0;// 初始长度
			int len = 0;
			while ((len = is.read(b, sumLen, b.length - sumLen)) > 0
					&& sumLen <= b.length) { // 要搞清楚这个循环的原理，了解一下InputStream的read模式吧
				sumLen += len;
			}
		}
	}

}
