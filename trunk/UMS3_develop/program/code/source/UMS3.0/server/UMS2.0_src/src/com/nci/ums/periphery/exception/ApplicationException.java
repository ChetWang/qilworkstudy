package com.nci.ums.periphery.exception;

public class ApplicationException extends Exception {
    private String errorCode;   // ´íÎó´úÂë
    private String message;     // ´íÎóÏêÏ¸ÏûÏ¢

    public ApplicationException(String errorCode, String message) {
        super(errorCode + message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() { return errorCode; }
    public String getMsg() { return message; }

}