package nci.gps.messages;

import nci.gps.TransactorServer;
import nci.gps.message.CommonPackage;
import nci.gps.util.MsgLogger;

public class ServerHeartBeatTool extends Thread {

	private TransactorServer server;
	private boolean running = true;
	private static String heartBeatSerial = "heartbeat";

	public ServerHeartBeatTool(TransactorServer server) {
		this.server = server;
	}

	public void run() {
		byte[] heartBeatBytes = CommonPackage.getHeartBeatPackage();
		while (running) {
			if (server.isLogin()) {
				if (!server.getMsgPool().getToFrontMsgMap().containsKey(
						heartBeatSerial)) {
					MsgLogger.log(MsgLogger.INFO, "·¢ËÍÐÄÌøÖ¡.");
					server.getMsgPool().getToFrontMsgMap().put(heartBeatSerial,
							heartBeatBytes);
				}
			}
			try {
				sleep(35 * 1000);
			} catch (InterruptedException e) {
			}
		}
	}

	public void stopHeartBeat() {
		running = false;
	}

}
