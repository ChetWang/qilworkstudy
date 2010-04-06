package nci.gps.util;

public class SerialFSEQ {
	
	private static SerialFSEQ fseq = null;
	
	private int serial = 0;
	
	private int max_serial = 128;
	
	private SerialFSEQ(){}
	
	public static SerialFSEQ getInstance(){
		if(fseq==null){
			fseq = new SerialFSEQ();
		}
		return fseq;
	}

	public synchronized int getSerial(){
		serial++;
		if(serial>=max_serial){
			serial = 1;
		}
		return serial;
	}
}
