package com.nci.domino.help.pinyin;

/**
 * 拼音操作帮助类，剥离自pinyin4j，这里只实现取得汉字首字母
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
	 * 获取拼音首字母
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
	 * 获取完整拼音，包括平仄音调
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
