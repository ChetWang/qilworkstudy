package nci.gps.util;

public class Util {
	
	/**
	 * 使用空格将字符串补齐
	 * @param msg 要补齐的字符串
	 * @param len 返回字符串的长度
	 * @return
	 */
	public String getFixedString(String msg, int len) {
		StringBuffer sb = new StringBuffer(msg);
		for (int i = msg.getBytes().length + 1; i <= len; i++)
			sb.append(" ");
		return sb.toString();
	}
}
