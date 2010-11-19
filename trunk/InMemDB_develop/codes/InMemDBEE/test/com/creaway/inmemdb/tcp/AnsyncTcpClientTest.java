package com.creaway.inmemdb.tcp;

import java.io.IOException;

import com.creaway.inmemdb.remote.client.AsyncDataListener;
import com.creaway.inmemdb.remote.client.AsyncTcpClient;
import com.creaway.inmemdb.remote.client.IllegalClientException;
import com.creaway.inmemdb.trigger.InMemDBTrigger;

public class AnsyncTcpClientTest {

	public void test() throws IOException, IllegalClientException {
		// 在resource/nio-processors.xml和triggers.xml下都应注册1001命令的processor和触发器
		for (int i = 0; i < 500; i++) {
			AsyncTcpClient client = new AsyncTcpClient();
			client.connect("192.168.6.29", 10017, "creaway", "wql");
			
			client.addAnsyncDataListener(new AsyncDataListener(7010,
					new byte[] { InMemDBTrigger.TYPE_INSERT, 0, 0 }) {

				@Override
				public void dataRecieved(byte[] data) {
					System.out.println("received: " + new String(data));
				}
			});
		}
	}

	public static void main(String... strings) throws Exception {
		new AnsyncTcpClientTest().test();
	}

}
