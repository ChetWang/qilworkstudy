package nci.gps.message.jms;

public class ProducerNotRunningException extends Exception{

	public ProducerNotRunningException(String producerID){
		super(producerID+"��Ϣ��������ֹͣ����.");
	}
}
