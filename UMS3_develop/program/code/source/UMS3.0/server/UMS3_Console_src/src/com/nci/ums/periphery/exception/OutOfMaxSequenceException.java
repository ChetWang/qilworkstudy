
package com.nci.ums.periphery.exception;

/**
 * <p>Title: DefaultEnterpriseUserInfo.java</p>
 * <p>Description:
 *    While receiver count of one messaege is larger than
 *    the defined MAXSIZE, the message cannot get correct 
 *    <code>SequenceNO</code>, exception will be thrown. 
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.22, since UMS3.0
 * @version 1.0
 */
public class OutOfMaxSequenceException extends Exception{

	public OutOfMaxSequenceException(){
		super();
	}
	
	public OutOfMaxSequenceException(String msg){
		super(msg);
	}
}
