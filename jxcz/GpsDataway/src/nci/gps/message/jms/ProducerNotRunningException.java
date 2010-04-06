package nci.gps.message.jms;

public class ProducerNotRunningException extends Exception{

	public ProducerNotRunningException(String producerID){
		super(producerID+"消息生产者已停止运行.");
	}
}
