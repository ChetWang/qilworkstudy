package nci.gps.message.jms;

public class OutofBytesMessageIndexException extends Exception{
	
	private static final long serialVersionUID = -2292696964472263708L;
	/**
	 * һ������֡������ֽ���
	 */
	public static int MAX_ONE_FRAME_BYTES = 64*1024+13;

	public OutofBytesMessageIndexException(){
		super("���͵��ֽڹ��󣬲��ܳ���"+MAX_ONE_FRAME_BYTES+"���ֽ�");
	}
}
