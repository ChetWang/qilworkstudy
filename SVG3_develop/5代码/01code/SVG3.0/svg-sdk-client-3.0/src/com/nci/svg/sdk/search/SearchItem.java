package com.nci.svg.sdk.search;

public interface SearchItem {
    
	/**
	 * 获取相关搜索对象的搜索表达式。
	 * @param searchContent 搜索内容
	 * @param caseSensitive 是否大小写区分。true为大小写区分，false为大小写不区分。
	 * @param wholeWord 是否是整字搜索。true为整字搜索，false为模糊搜索。
	 * @return 对象的搜索表达式
	 */
    public StringBuffer getExpr(String searchContent, boolean caseSensitive, boolean wholeWord);
    
}
