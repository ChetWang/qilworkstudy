package com.nci.domino.help.pinyin;

/**
 * ƴ�����������࣬������pinyin4j������ֻʵ��ȡ�ú�������ĸ
 * 
 * @author Qil.Wong
 * 
 */
public class PinyinHelper {

	private PinyinHelper() {
	}

	private static String[] toHanyuPinyinStringArray(char ch) {
		return getUnformattedHanyuPinyinStringArray(ch);
	}

	private static String[] getUnformattedHanyuPinyinStringArray(char ch) {
		return ChineseToPinyinResource.getInstance().getHanyuPinyinStringArray(
				ch);
	}

	/**
	 * ��ȡƴ������ĸ
	 * 
	 * @param str
	 * @return
	 */
	public static String getAllFirstLetter(String str) {
		StringBuffer convert = new StringBuffer();
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert.append(pinyinArray[0].charAt(0));
			} else {
				convert.append(word);
			}
		}
		return convert.toString();
	}

	/**
	 * ��ȡ����ƴ��������ƽ������
	 * 
	 * @param str
	 * @return
	 */
	public static String getAllPinyin(String str) {
		StringBuffer convert = new StringBuffer();
		if (str == null)
			str = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert.append(pinyinArray[0]);
			} else {
				convert.append(word);
			}
		}
		return convert.toString();
	}

}
