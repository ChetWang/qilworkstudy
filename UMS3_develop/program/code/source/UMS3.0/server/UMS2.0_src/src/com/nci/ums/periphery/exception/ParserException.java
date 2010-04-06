package com.nci.ums.periphery.exception;

public class ParserException extends Exception {
    private String errorCode;   // �������
    private String message;     // ������ϸ��Ϣ

    public ParserException(String errorCode, String message) {
        super(errorCode + message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() { return errorCode; }
    public String getMsg() { return message; }

}