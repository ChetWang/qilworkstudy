package com.creaway.inmemdb.tcp;

import java.util.concurrent.CountDownLatch;

import javax.sql.RowSet;

import com.creaway.inmemdb.remote.client.SyncTcpClient;

/**
 * 同步客户端测试用例
 * @author Qil.Wong
 *
 */
public class SyncTcpClientTest {

	public void test() {
		int concurrent = 1;
		final CountDownLatch cdl = new CountDownLatch(concurrent);
		for (int i = 0; i < concurrent; i++) {
			final int index = i;
			Thread t = new Thread("Concurrent Sync TCP test " + i) {

				public void run() {
					try {
						SyncTcpClient client = new SyncTcpClient();
						client.connect("192.168.6.29", 10017, "creaway",
								"wql");
						RowSet row = client.query("select * from tttt where ii<100 order by ii");

						while (row.next()) {
							System.out.println("THREAD " + index + "    "
									+ row.getInt(1));
						}
						System.out.println("THREAD " + index + "    finished");
						row.close();
						client.close(false);
						cdl.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
			
		}
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String... strings) {
		new SyncTcpClientTest().test();
	}

}
