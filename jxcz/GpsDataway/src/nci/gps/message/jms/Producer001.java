package nci.gps.message.jms;

import java.util.HashMap;

import nci.gps.util.ReadConfig;

/**
 * 发送给JMS服务器消息，由于只发给一个应用（主服务），只有一个主题，只需生成一个实例
 * 
 * @author Qil.Wong
 * 
 */
public class Producer001 extends AbstractProducer {


	private static Producer001 producer;
	private Producer001() throws NoSubjectException{
		super();
	}
	
	public static Producer001 getInstance() throws NoSubjectException{
		if(producer == null)
			producer = new Producer001();
		return producer;
	}

	public String getProducerID() {
		return "producer001";
//		return null;
	}

	public String getServerSubject() {
		return (String) ((HashMap) ReadConfig.getInstance().getParamsHash()
				.get("messageServer".toUpperCase())).get("SUBJECT");
	}

}
