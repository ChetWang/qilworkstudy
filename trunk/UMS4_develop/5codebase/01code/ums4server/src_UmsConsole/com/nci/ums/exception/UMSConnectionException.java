package com.nci.ums.exception;

public class UMSConnectionException extends Exception {

  public UMSConnectionException() {
    super();
  }
  public UMSConnectionException(String msg) {
    super(msg);
  }
  public UMSConnectionException(Throwable e) {
    super(e);
  }
  public UMSConnectionException(String msg,Throwable e) {
    super(msg,e);
  }
}