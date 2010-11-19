// $Id$
package com.creaway.inmemdb.developworks.nio;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class MultiPortEcho {
	private int ports[];
	private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

	public MultiPortEcho(int ports[]) throws IOException {
		this.ports = ports;

		go();
	}

	
	private void go() throws IOException {
		// Create a new selector
		try {
			final Selector selector = Selector.open();

			// Open a listener on each port, and register each one
			// with the selector
			for (int i = 0; i < ports.length; ++i) {
				ServerSocketChannel ssc = ServerSocketChannel.open();
				ssc.configureBlocking(false);
				ServerSocket ss = ssc.socket();
				InetSocketAddress address = new InetSocketAddress(ports[i]);
				ss.bind(address);

				SelectionKey key = ssc.register(selector,
						SelectionKey.OP_ACCEPT);

				System.out.println("Going to listen on " + ports[i]);
			}
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					for(SelectionKey k:selector.keys()){
						System.out.println(k.channel());
						if(k.channel() instanceof SocketChannel){
							SocketChannel s = (SocketChannel)k.channel();
//							s.
							try {
								s.write(echoBuffer);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			},1000,1000);
			while (true) {
				int num = selector.select();

				Set selectedKeys = selector.selectedKeys();
				Iterator it = selectedKeys.iterator();

				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();

					if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
						// Accept the new connection
						ServerSocketChannel ssc = (ServerSocketChannel) key
								.channel();
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						// Add the new connection to the selector
						SelectionKey newKey = sc.register(selector,
								SelectionKey.OP_READ);
						it.remove();

						System.out.println("Got connection from " + sc);
					} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
						// Read the data
						SocketChannel sc = (SocketChannel) key.channel();
						
						// Echo data
						int bytesEchoed = 0;
						boolean valid = true;
						while (valid) {
							echoBuffer.clear();
							try {
								int r = sc.read(echoBuffer);

								if (r <= 0) {
									break;
								}

								echoBuffer.flip();

								sc.write(echoBuffer);
								bytesEchoed += r;
							} catch (IOException e) {
								if (e.getMessage()
										.toLowerCase()
										.indexOf(
												"An existing connection was forcibly"
														.toLowerCase()) >= 0) {
									System.err.println(e.getMessage());
									valid = false;
									key.cancel();
								} else {
									throw e;
								}
							}

						}
						if (bytesEchoed > 0) {
							System.out.println("Echoed " + bytesEchoed
									+ " from " + sc);
						}
						it.remove();
					}

				}

				// System.out.println( "going to clear" );
				// selectedKeys.clear();
				// System.out.println( "cleared" );
			}
		} catch (IOException e) {
			if (e.getMessage().indexOf("An existing connection was forcibly") >= 0) {
				e.printStackTrace();
			} else {
				throw e;
			}
		}
	}
	
	Timer timer = new Timer("Check Channels", true);
	

	static public void main(String args[]) throws Exception {

		int ports[] = new int[] { 7777, 7778, 7779 };

		new MultiPortEcho(ports);
	}
}
