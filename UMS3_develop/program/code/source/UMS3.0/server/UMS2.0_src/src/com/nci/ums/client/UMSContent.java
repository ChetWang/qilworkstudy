package com.nci.ums.client;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UMSContent implements Serializable{
	private static final long serialVersionUID = 5636790259199575178L;
	private byte[] content;
	private String charset;
	private String type;
	private boolean freeze;
	private int length;
	
	protected static Log log = LogFactory.getLog(UMSContent.class);
	
	public UMSContent() {
		super();
		this.freeze 	= false;
		this.charset 	= CHARSET_GBK;
		this.type		= TYPE_TEXT;
		this.length		= 0;
		this.content 	= new byte[1024];
	}
	
	/**
	 * ������������ù����׳�IllegalStateException�쳣
	 * @param context
	 * @return
	 */
	public UMSContent setText(String context) {
		if( freeze ) {
			throw new IllegalStateException("Content is setted");
		}
		if( context != null ) {
			try {
				byte[] temp = context.getBytes(charset);
				setContent(temp,TYPE_TEXT);
			} catch (UnsupportedEncodingException e) {
				log.error("�ַ���ת������", e);
				return this;
			}
		}
		return this;
	}
	
	/**
	 * ������������ַ���
	 * ������������ù����׳�IllegalStateException�쳣
	 * @param charset
	 * @return
	 */
	public UMSContent setCharset(String charset) {
		if( freeze ) {
			throw new IllegalStateException("Content is setted");
		}
		if( charset != null && "".equals(charset)) {
			this.charset = charset;
		}
		return this;
	}
	
	/**
	 * �����ǰ�����������������ı��ͣ���
	 * ���ص�ǰ��������ַ���������
	 * ����null
	 * @return
	 */
	public String getCharset() {
		if(TYPE_TEXT.equals(type)) {
			return this.charset;
		}
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public String getString() {
		if(TYPE_TEXT.equals(type)) {
			try {
				return new String(content,charset);
			} catch (UnsupportedEncodingException e) {
				log.error("�ַ���ת������", e);
			}
		}
		return null;
	}
	/**
	 * ��������������
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * ���������峤��
	 * @return
	 */
	public int getLength() {
		return this.length;
	}
	/**
	 * 
	 * @param bytes 
	 * @return
	 */
	public UMSContent setContent(byte[] bytes,String type) {
		if( freeze ) {
			throw new IllegalStateException("Content is setted");
		}
		if( bytes != null && type != null && !"".equals(type) ) {
			System.arraycopy(bytes, 0, content, 0, bytes.length);
			this.type 		= type;
			this.length 	= bytes.length;
			this.freeze		= true;
		}
		return this;
	}
	
	/**
	 * ����Ϣ�����ݿ����󣬷�����Ϣ�����ݡ�
	 * @return
	 */
	public byte[] getBytes() {
		byte[] temp = new byte[1024];
		System.arraycopy(content, 0, temp, 0, content.length);
		return temp;
	}
	
	public static final String CHARSET_GBK 	= "GBK";
	
	public static final String TYPE_TEXT		= "com.nci.ums.content.type.text";
	public static final String TYPE_JPG		= "com.nci.ums.content.type.jpg";
	public static final String TYPE_ZIP		= "com.nci.ums.content.type.zip";
}
