package com.nci.domino.concurrent;

/**
 * 启动阶段结束异常
 * @author Qil.Wong
 *
 */
public class WfStartupEndException extends Exception{
	
	public WfStartupEndException(){
		super("启动阶段已经结束!");
	}

}
