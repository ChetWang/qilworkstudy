// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SocketServer.java

package com.zte.smgw.socket.server;

import com.zte.smgw.socket.iinterface.IReqHandler;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import org.apache.log4j.Logger;

// Referenced classes of package com.zte.smgw.socket.server:
//            ChangeStatus

public class SocketServer implements Runnable {

	public SocketServer(InetAddress hostAddress, int port, Class handlerClass)
			throws IOException {
		log = Logger.getLogger(com.zte.smgw.socket.server.SocketServer.class);
		readBuffer = ByteBuffer.allocate(8192);
		threadNum = 20;
		channelList = new ArrayList();
		pendingChanges = new LinkedList();
		pendingData = new HashMap();
		this.hostAddress = hostAddress;
		this.port = port;
		selector = initSelector();
		try {
			handler = new IReqHandler[threadNum];
			for (int i = 0; i < threadNum; i++)
				handler[i] = (IReqHandler) handlerClass.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}
		serverThread = new Thread(this);
		serverThread.start();
	}

	public void send(SocketChannel socket, byte data[]) {
		log.debug("send to:"
				+ socket.socket().getRemoteSocketAddress().toString() + " "
				+ data.length + " byte data.");
		try {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			buffer.flip();
			socket.write(ByteBuffer.wrap(data));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void run() {
		do
			try {
				synchronized (pendingChanges) {
					for (Iterator changes = pendingChanges.iterator(); changes
							.hasNext();) {
						ChangeStatus change = (ChangeStatus) changes.next();
						switch (change.type) {
						case 2: // '\002'
							SelectionKey key = change.socket.keyFor(selector);
							key.interestOps(change.ops);
							break;
						}
					}

					pendingChanges.clear();
				}
				selector.select();
				for (Iterator selectedKeys = selector.selectedKeys().iterator(); selectedKeys
						.hasNext();) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();
					if (key.isValid())
						if (key.isAcceptable())
							accept(key);
						else if (key.isReadable())
							read(key);
				}

			} catch (Exception e) {
				log.error("SocketServer run exception:", e);
			}
		while (true);
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, 1);
		channelList.add(socketChannel);
		log.info(socketChannel.socket().getRemoteSocketAddress().toString()
				+ " connect success!");
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		log.debug(socketChannel.socket().getRemoteSocketAddress().toString()
				+ " begin to read byte array");
		readBuffer.clear();
		int numRead;
		try {
			numRead = socketChannel.read(readBuffer);
			log.debug("read "
					+ socketChannel.socket().getRemoteSocketAddress()
							.toString() + " " + numRead + " byte ");
		} catch (IOException e) {
			channelList.remove(socketChannel);
			key.cancel();
			socketChannel.close();
			if (e.getMessage().indexOf("远程主机强迫关闭了一个现有的连接") < 0) {
				log.error(socketChannel.socket().getRemoteSocketAddress()
						.toString(), e);
			}
			return;
		}
		if (numRead == -1) {
			channelList.remove(socketChannel);
			key.channel().close();
			key.cancel();
			log.error("this connect is alreay close");
			return;
		} else {
			handleRequest(socketChannel, readBuffer.array(), numRead);
			return;
		}
	}

	private void handleRequest(SocketChannel socketChannel, byte data[],
			int numRead) throws IOException {
		byte rspData[] = new byte[numRead];
		System.arraycopy(data, 0, rspData, 0, numRead);
		curHandlerId %= threadNum;
		int handlerId = curHandlerId;
		curHandlerId++;
		handler[handlerId].Request(this, socketChannel, rspData);
	}

	private Selector initSelector() throws IOException {
		Selector socketSelector = SelectorProvider.provider().openSelector();
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		InetSocketAddress isa = new InetSocketAddress(hostAddress, port);
		serverChannel.socket().bind(isa);
		serverChannel.register(socketSelector, 16);
		log.info(hostAddress.getHostAddress() + ":" + port
				+ " is begin to listen!");
		return socketSelector;
	}

	private Logger log;
	private InetAddress hostAddress;
	private int port;
	private Thread serverThread;
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private ByteBuffer readBuffer;
	private IReqHandler handler[];
	private int curHandlerId;
	private int threadNum;
	private List channelList;
	private List pendingChanges;
	private Map pendingData;
}
