
package com.nci.svg.sdk.server.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ��������˻���ʵ����
 *
 */
public class CacheObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2328672378296290567L;

	/**
	 * list��������
	 */
	public static final String LIST_FLAG = "ArrayList";
	
	/**
	 * map��������
	 */
	public static final String MAP_FLAG = "Map";
	/**
	 * String��������
	 */
	public static final String STRING_FLAG = "String";
    /**
     * ��������
     */
    private String cacheType;
    
    /**
     * ����洢��ʽ,Ŀǰ֧������LIST��MAP��String
     */
    private String cacheFlag;
    
    /**
     * �б���洢
     */
    private List cacheList;
    
     /**
     * �ֵ仺��洢
     */
    private Map cacheMap;
    
    /**
     * �洢�ַ���
     */
    private String cacheString;
    
    /**
     * ��ȡ��������
     * @return
     */
	public String getCacheType() {
		return cacheType;
	}
	/**
	 * ���û�������
	 * @param cacheType:��������
	 */
	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}
	
	/**
	 * ��ȡ�����б�
	 * @return
	 */
	public List getCacheList() {
		return cacheList;
	}
	/**
	 * ���û����б�
	 * @param cacheList
	 */
	public void setCacheList(List cacheList) {
		this.cacheList = cacheList;
	}
	
	/**
	 * ��ȡ�����ֵ�
	 * @return
	 */
	public Map getCacheMap() {
		return cacheMap;
	}
	/**
	 * ���û����ֵ�
	 * @param cacheMap
	 */
	public void setCacheMap(HashMap cacheMap) {
		this.cacheMap = cacheMap;
	}
	
	/**
	 * ��ȡ����洢��ʽ
	 * @return LIST��MAP��String
	 */
	public String getCacheFlag() {
		return cacheFlag;
	}
	
	/**
	 * ���û���洢��ʽ
	 * @param cacheFlag:LIST��MAP��String
	 */
	public void setCacheFlag(String cacheFlag) {
		this.cacheFlag = cacheFlag;
	}
	
	/**
	 * ��ȡ����洢�ַ���
	 * @return
	 */
	public String getCacheString() {
		return cacheString;
	}
	
	/**
	 * ���û���洢�ַ���
	 * @param cacheString
	 */
	public void setCacheString(String cacheString) {
		this.cacheString = cacheString;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return cacheType;
	}
}
