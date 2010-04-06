package com.nci.ums.transmit.common;

/**
 * �û�����������쳣������UMS�Է�װ�����Ĭ�������ֽڳ��ȡ� ����û�����UMS�ṩ�������װ��������������ܴ��쳣���ơ�
 * 
 * @author Qil.Wong
 * 
 */
public class IllegalCommandException extends Exception {

	private static final long serialVersionUID = -5196759859271537157L;

	public IllegalCommandException(int userCommand) {
		super("Address value should be at range of 0��65535, current is "
				+ userCommand);
	}
}
