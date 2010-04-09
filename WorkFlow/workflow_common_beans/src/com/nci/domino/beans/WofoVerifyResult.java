package com.nci.domino.beans;

import java.io.Serializable;

public class WofoVerifyResult implements Serializable {

    private boolean passed; // �Ƿ�ͨ��
    private String result; // У����
    private String suggestion; // �޸Ľ���
    
    public WofoVerifyResult() {
        //
    }
    
    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
