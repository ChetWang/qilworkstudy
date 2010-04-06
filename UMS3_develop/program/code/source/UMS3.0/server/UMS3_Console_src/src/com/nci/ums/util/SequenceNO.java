
package com.nci.ums.util;

import com.nci.ums.periphery.exception.OutOfMaxSequenceException;

/**
 * <p>Title: SequenceNO.java</p>
 * <p>Description:
 *     UMS server may get more than one UMSMsg Object, every UMSMsg Object has
 *     unique batchNO + SerialNO; And each submessage in UMSMsg for one 
 *     receiver will have a unique number, this unique number in submessage called
 *     sequence number.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.26,Since UMS3.0
 * @version 1.0 
 */
public class SequenceNO {

	static private SequenceNO sequenceNO;
	
	private int[] sequenceNOArray;
	
	private int flag = 1;
	
	public static final int MAXSIZE = 10000000;
	
	private SequenceNO(){}
	
	public static synchronized SequenceNO getInstance(){
		if(sequenceNO==null)
			sequenceNO = new SequenceNO();
		return sequenceNO;
	}
	
	public static int[] getSequenceNO(int arraySize) throws OutOfMaxSequenceException{
		if (arraySize > MAXSIZE)
			throw new OutOfMaxSequenceException("Max Array Size is:"+MAXSIZE);
		int[] sequenceNO = new int[arraySize];
		if(arraySize>1){
			for(int i=0;i<arraySize-1;i++){
				sequenceNO[i]=i+1;
			}
		}else if(arraySize==1){
			sequenceNO[arraySize-1]=0;
		}else {
			sequenceNO= null;
		}
		return sequenceNO;
	}
	
	/**
	 * get the sequence number array for UMSMsg , each submessage for one 
	 * receiver will have a unique number.
	 * @param arraySize the count of receivers of one unique message
	 * @return sequence number array
	 * @throws OutOfMaxSequenceException 
	 */
	public synchronized int[] getSequenceNOArray(int arraySize) throws OutOfMaxSequenceException{
		sequenceNOArray = new int[arraySize];
		if (arraySize > MAXSIZE)
			throw new OutOfMaxSequenceException("Max Array Size is:"+MAXSIZE);
		if(flag>MAXSIZE-arraySize){
			flag = 1;
		}
		for(int i = 0;i < arraySize;i++){
			sequenceNOArray[i] = flag;
			flag++;
		}		
		return sequenceNOArray;
	}
	
	
}
