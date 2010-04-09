package com.nci.domino.beans.desyer.event;

import java.util.List;

import com.nci.domino.beans.desyer.WofoEventBean;

public class WofoEventJavaHandlerBean extends WofoEventBean {

    private String className;
    private String methodName;
    private List params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params = params;
    }
}
