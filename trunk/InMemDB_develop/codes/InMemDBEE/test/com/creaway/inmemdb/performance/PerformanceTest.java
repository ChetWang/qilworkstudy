package com.creaway.inmemdb.performance;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.creaway.inmemdb.core.InMemDBServerH2;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;

public class PerformanceTest {

	private int index;

	private int maxTimes;

	public PerformanceTest(int maxTimes, int index) {
		this.maxTimes = maxTimes;
		this.index = index;
		new InMemDBServerH2().startModule(null);
	}

	public void startTest() {

		// t1 = System.nanoTime();
		// DBLogger.log(DBLogger.WARN, "SLEECT spent:" + (System.nanoTime() -
		// t1) / 1000000
		// + " ms");

	}

	public void test(final int threadCount, final int rowCountPerThread) {
		try {
			Thread.sleep(3000);// 等待集群完全建立关系
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		long t1 = System.nanoTime();

		CountDownLatch cdl = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			new CThread(i, rowCountPerThread * index, cdl).start();
		}
		try {
			cdl.await();
			DBLogger.log(DBLogger.ERROR, "Thread: " + threadCount
					+ ", Total Rows: " + threadCount * rowCountPerThread
					* index + ", spent:" + (System.nanoTime() - t1) / 1000000
					+ " ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (index < maxTimes) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						sleep(1500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					Properties p = System.getProperties();
					String command = p.getProperty("java.home") + "/bin/java";
					String classPath = p.getProperty("java.class.path");
					String mainClass = PerformanceTest.class.getName();
					try {
						Runtime.getRuntime().exec(
								command + " -Xmx"
										+ Runtime.getRuntime().maxMemory()
										+ " -cp " + classPath + " " + mainClass
										+ " threadCount " + threadCount
										+ " rowCountPerThread "
										+ rowCountPerThread + " maxTimes "
										+ maxTimes + " index " + (index + 1));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			String baseURL = "http://sms.api.bz/fetion.php";
			String[][] param = new String[4][2];
			param[0][0] = "username";
			param[0][1] = "13819155409";
			param[1][0] = "password";
			param[1][1] = "qil.wong1303";
			param[2][0] = "sendto";
			param[2][1] = "13819155409";
			param[3][0] = "message";
			param[3][1] = "测试已经完成";
			communicateWithURL(baseURL, param);
		}
		System.exit(0);
	}

	private static class CThread extends Thread {

		private int index;
		CountDownLatch cdl;
		int count;

		public CThread(int index, int count, CountDownLatch cdl) {
			this.index = index;
			this.cdl = cdl;
			this.count = count;
		}

		public void run() {

			System.out.println("TriggerTest: 插入" + count + "数据");
			Connection conn = ConnectionManager.getConnection();
			try {
				Statement st = conn.createStatement();
				for (int i = 0; i < count; i++) {

					st.execute("insert into tttt (ii,aa,bb) values("
							+ (count * index + i) + ",'aaa','bbbb')");

				}
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {

					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				cdl.countDown();
				ConnectionManager.releaseConnection(conn);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int threadCount = 0;
		int rowCountPerThread = 0;
		int maxTimes = 10;
		int index = 1;
		if (args == null || args.length == 0) {
			args = new String[] { "threadCount", "100", "rowCountPerThread",
					"2000", "maxTimes", "10" };
		}
		for (int i = 0; i < args.length; i = i + 2) {
			if (args[i].equals("threadCount")) {
				threadCount = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("rowCountPerThread")) {
				rowCountPerThread = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("maxTimes")) {
				maxTimes = Integer.parseInt(args[i + 1]);
			} else if (args[i].equals("index")) {
				index = Integer.parseInt(args[i + 1]);
			}
		}
		new PerformanceTest(maxTimes, index).test(threadCount,
				rowCountPerThread);
	}

	public static synchronized void communicateWithURL(String baseURL,
			String[][] param) {
		// if (netWorkOK) {
		try {
			URL url = new URL(baseURL);
//			SocketAddress addr = new InetSocketAddress("192.168.6.29", 28080);// 是代理地址:192.9.208.16:3128
//			Proxy typeProxy = new Proxy(Proxy.Type.HTTP, addr);
////			URLConnection conn = url.openConnection(typeProxy);
			
			

			Properties prop = System.getProperties();
			prop.put("http.proxyHost","192.168.6.29");
			prop.put("http.proxyPort", "28080");

			URLConnection rConn = url.openConnection();

			rConn.setDoOutput(true);
			rConn.setDoInput(true);
			rConn.setReadTimeout(3000);
			PrintStream output = new PrintStream(rConn.getOutputStream());
			StringBuffer rs = new StringBuffer();
			if (param != null && param.length > 0) {
				for (int i = 0; i < param.length; i++) {
					if (param[i][0] == null || param[i][1] == null)
						continue;
					if (i != 0) {
						rs.append("&");
					}
					rs.append(URLEncoder.encode(param[i][0], "UTF-8"))
							.append('=')
							.append(URLEncoder.encode(param[i][1], "UTF-8"));
				}
			}
			output.print(rs.toString());
			output.flush();
			byte[] b = new byte[1024];
			rConn.getInputStream().read(b);
			System.out.println(new String(b, "utf8"));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
