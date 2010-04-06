package nci.gps.message.jms;

public class NoSubjectException extends Exception{
	
	public NoSubjectException(){
		super("没有对应的主题.");
	}

}
