package com.nci.ums.transmit.common;

/**
 * �Ƿ���ַ�쳣��UMSĬ�ϵĿͻ��˽����ַ��2���ֽڳ��ȣ���0-65535֮��
 * @author Qil.Wong
 *
 */
public class IllegalAdressException extends Exception{

	private static final long serialVersionUID = 8557251087430502168L;

	public IllegalAdressException(int address){
		super("Address value should be at range of 0��65535, current is "+address);
	}
}
