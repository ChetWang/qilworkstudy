package com.nci.svg.sdk.search;

public interface SearchItem {
    
	/**
	 * ��ȡ�������������������ʽ��
	 * @param searchContent ��������
	 * @param caseSensitive �Ƿ��Сд���֡�trueΪ��Сд���֣�falseΪ��Сд�����֡�
	 * @param wholeWord �Ƿ�������������trueΪ����������falseΪģ��������
	 * @return ������������ʽ
	 */
    public StringBuffer getExpr(String searchContent, boolean caseSensitive, boolean wholeWord);
    
}
