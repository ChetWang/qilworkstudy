

public class byteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte b = (byte) 0x61;
		int num = (b+256) % 256;
		System.out.println(Integer.toString(num));
		num = 0;
//		num = 
		
		String st = Byte.toString(b);
		System.out.println(st);
	}

}
