package com.nci.domino.beans.desyer.event;

import java.util.List;

import com.nci.domino.beans.desyer.WofoEventBean;

public class WofoEventWebServiceHandlerBean extends WofoEventBean {

    private String serviceAddress; // 地址
    private String serviceName; // 服务名
    private String operationName; // 操作名
    private String username; // 登入名
    private String password; // 登入密码
    private List params; // 参数

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params = params;
    }
}
