/*
 * @(#)RemoteServer.java	1.0  09/19/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.remote;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.creaway.inmemdb.core.DBModule;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.InMemDBThreadFactory;
import com.creaway.inmemdb.remote.action.ProcessHandler;
import com.creaway.inmemdb.remote.action.Processor;
import com.creaway.inmemdb.remote.client.UserMessage;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.FileWatchDog;
import com.creaway.inmemdb.util.Functions;
import com.creaway.inmemdb.util.IOToolkit;

/**
 * TCP连接数据库的服务端处理对象类
 * 
 * @author Qil.Wong
 * 
 */
public class RemoteTcpServer implements DBModule, Runnable {

	/**
	 * 模块启动标签
	 */
	private boolean start = false;

	private ServerSocketChannel socketServerChannel = null;

	private Selector selector = null;

	/**
	 * Channel是否已经登录的标签，放在selectionkey的attachment object下
	 */
	public static final String LOGIN_ATTACH_KEY = "login";

	private Map<Integer, Processor<?>> processors = new ConcurrentHashMap<Integer, Processor<?>>();


	/**
	 * 是否进行用户认证
	 */
	private boolean userAuth = true;

	/**
	 * 是否进行ip认证
	 */
	private boolean ipAuth = true;

	/**
	 * 用户认证下的权限信息
	 */
	private Map<String, String> nioUsers = new ConcurrentHashMap<String, String>();

	/**
	 * ip认证下的允许的ip列表
	 */
	private List<String> nioAllowedIps = new CopyOnWriteArrayList<String>();

	private static String AUTH_CONFIG = RemoteTcpServer.class.getResource(
			"/resources/nio-auth.xml").getFile();

	private static String PROCESSOR_CONFIG = RemoteTcpServer.class.getResource(
			"/resources/nio-processors.xml").getFile();

	public RemoteTcpServer() {
		FileWatchDog fwd_user = new FileWatchDog(AUTH_CONFIG) {
			@Override
			protected void doOnChange() {
				loadAuth();
			}
		};
		fwd_user.setName("nio auth watch dog");
		fwd_user.setDelay(5000);
		fwd_user.start();
		FileWatchDog fwd_processor = new FileWatchDog(getClass().getResource(
				"/resources/nio-processors.xml").getFile()) {
			@Override
			protected void doOnChange() {
				initProcessors();
			}
		};
		fwd_processor.setName("nio processors watch dog");
		fwd_processor.setDelay(5000);
		fwd_processor.start();
	}

	/**
	 * 加载权限信息
	 * 
	 * @param filename
	 */
	private void loadAuth() {
		DBLogger.log(DBLogger.INFO, "加载远程交互权限配置信息");
		nioUsers.clear();
		nioAllowedIps.clear();
		Document doc = Functions.getXMLDocument(AUTH_CONFIG);
		Element root = doc.getDocumentElement();
		userAuth = root.getAttribute("user_auth").trim().toLowerCase()
				.equals("true");
		ipAuth = root.getAttribute("ip_auth").trim().toLowerCase()
				.equals("true");
		if (userAuth) {
			NodeList nodes = doc.getElementsByTagName("user");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element e = (Element) nodes.item(i);
				nioUsers.put(e.getAttribute("name"),
						Functions.md5(e.getAttribute("password")));
			}
		}
		if (ipAuth) {
			NodeList nodes = doc.getElementsByTagName("ip");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element e = (Element) nodes.item(i);
				nioAllowedIps.add(e.getAttribute("address").trim());
			}
		}
	}



	/**
	 * 加载远程命令处理对象
	 * 
	 * @param filename
	 */
	private void initProcessors() {
		try {
			DBLogger.log(DBLogger.INFO, "加载远程交互处理对象Processors");
			processors.clear();
			Document doc = Functions.getXMLDocument(PROCESSOR_CONFIG);
			NodeList nodes = doc.getElementsByTagName("processor");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element e = (Element) nodes.item(i);
				String className = e.getAttribute("class");
				int command = Integer.parseInt(e.getAttribute("command"));
				Processor<?> p = (Processor<?>) Class.forName(className)
						.newInstance();
				p.setCommand(command);
				processors.put(command, p);
			}
		} catch (Exception e1) {
			DBLogger.log(DBLogger.ERROR, "", e1);
		}
	}

	@Override
	public void startModule(Map<?, ?> params) {
		if (!start) {
			start = true;
			try {
				DBLogger.log(DBLogger.INFO, "启动远程访问模块，开始创建远程接口支持对象");
				loadAuth();
				initProcessors();
				selector = Selector.open();
				socketServerChannel = ServerSocketChannel.open();
				socketServerChannel.configureBlocking(false);
				ServerSocket ss = socketServerChannel.socket();
				int port = Integer.parseInt(InMemDBServer.getInstance()
						.getSystemProperties().getProperty("nio_tcp_port"));
				InetSocketAddress address = new InetSocketAddress(port);
				ss.bind(address);
				socketServerChannel.register(selector, SelectionKey.OP_ACCEPT);
				DBLogger.log(DBLogger.INFO, "远程TCP连接交互就绪，开放端口：" + port);
			} catch (IOException e) {
				DBLogger.log(DBLogger.ERROR, "", e);
			}
			new Thread(this, "inmemdb tcp server").start();
		}
	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		if (start) {
			start = false;
			try {
				selector.close();
				socketServerChannel.close();
			} catch (IOException e) {
				DBLogger.log(DBLogger.ERROR, "", e);
			}
			processors.clear();
		}
	}

	public void run() {
		try {
			while (start) {
				selector.select();
				if (selector.isOpen() == false)
					break;
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
						// Accept the new connection
						ServerSocketChannel ssc = (ServerSocketChannel) key
								.channel();
						SocketChannel sc = ssc.accept();
						String ip = sc.socket().getInetAddress()
								.getHostAddress();
						if (!isIpAllowed(ip)) {
							ByteBuffer ip_Failed = ByteBuffer.wrap(new byte[] {
									UserMessage.HEAD_LOGIN_RESULT,
									UserMessage.LOGIN_OUT_FAILED_ILLEGAL_IP });
							IOToolkit.nioCompleteWrite(sc, ip_Failed);
							DBLogger.log(DBLogger.WARN, "非法IP " + sc);
							sc.close();
						} else {
							sc.configureBlocking(false);
							// 往 selector中添加新的连接
							Map<Object, Object> att = new HashMap<Object, Object>();
							att.put(LOGIN_ATTACH_KEY, "false");
							sc.register(selector, SelectionKey.OP_READ, att);
							DBLogger.log(DBLogger.INFO, "新TCP连接接入：" + sc);
						}
					} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
						// 开始读数据
						// threadPool.execute(new ProcessHandler(this, key));
						// 频繁的数据交互不能每次都开线程，否则执行顺序可能混乱
						new ProcessHandler(this, key).run();
					}
				}
			}

		} catch (Exception e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
	}

	public boolean isChannelLogin(SelectionKey key) {
		Map<?, ?> att = (Map<?, ?>) key.attachment();
		if (att == null)
			return false;
		return att.get(LOGIN_ATTACH_KEY).toString().toLowerCase().trim()
				.equals("true");
	}


	public Map<Integer, Processor<?>> getProcessors() {
		return processors;
	}

	/**
	 * 登陆时的身份验证
	 * 
	 * @param name
	 * @param md5psw
	 * @return
	 */
	public boolean checkUserLogin(String user, String pswmd5) {
		if (!userAuth)
			return true;
		String psw = nioUsers.get(user);
		if (psw != null) {
			return psw.equals(pswmd5);
		}
		return false;
	}

	/**
	 * 判断指定的ip是否合法
	 * 
	 * @param ip
	 * @return
	 */
	public boolean isIpAllowed(String ip) {
		if (!ipAuth) {
			return true;
		}
		return nioAllowedIps.contains(ip);
	}

}
