package com.nci.ums.transmit.common;

/**
 * FSEQ�쳣�����������ݣ�reverseDirection=true����fseqӦ����ǰ�յ������ݵ�fseqֵ���
 * @author Qil.Wong
 *
 */
public class IllegalFSEQException extends Exception{

	private static final long serialVersionUID = -8125356639252511289L;

	public IllegalFSEQException(){
		super("���������ݣ�reverseDirection=true����fseqӦ����ǰ�յ������ݵ�fseqֵ���");
	}
}
