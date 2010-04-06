package nci.gps.message.jms;

import java.util.HashMap;

import nci.gps.util.ReadConfig;

/**
 * ���͸�JMS��������Ϣ������ֻ����һ��Ӧ�ã������񣩣�ֻ��һ�����⣬ֻ������һ��ʵ��
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
