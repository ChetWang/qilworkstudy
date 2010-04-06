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
	 * 如果内容体设置过将抛出IllegalStateException异常
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
				log.error("字符集转换错误", e);
				return this;
			}
		}
		return this;
	}
	
	/**
	 * 设置内容体的字符集
	 * 如果内容体设置过将抛出IllegalStateException异常
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
	 * 如果当前内容体内容类型是文本型，则
	 * 返回当前内容体的字符集，否则
	 * 返回null
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
				log.error("字符集转换错误", e);
			}
		}
		return null;
	}
	/**
	 * 返回内容体类型
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * 返回内容体长度
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
	 * 将消息体内容拷贝后，返回消息体内容。
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
