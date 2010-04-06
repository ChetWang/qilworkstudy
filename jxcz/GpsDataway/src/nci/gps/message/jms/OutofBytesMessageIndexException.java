package nci.gps.message.jms;

public class OutofBytesMessageIndexException extends Exception{
	
	private static final long serialVersionUID = -2292696964472263708L;
	/**
	 * 一个数据帧的最大字节数
	 */
	public static int MAX_ONE_FRAME_BYTES = 64*1024+13;

	public OutofBytesMessageIndexException(){
		super("发送的字节过大，不能超过"+MAX_ONE_FRAME_BYTES+"个字节");
	}
}
