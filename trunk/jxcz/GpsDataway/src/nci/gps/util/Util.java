package nci.gps.util;

public class Util {
	
	/**
	 * ʹ�ÿո��ַ�������
	 * @param msg Ҫ������ַ���
	 * @param len �����ַ����ĳ���
	 * @return
	 */
	public String getFixedString(String msg, int len) {
		StringBuffer sb = new StringBuffer(msg);
		for (int i = msg.getBytes().length + 1; i <= len; i++)
			sb.append(" ");
		return sb.toString();
	}
}
