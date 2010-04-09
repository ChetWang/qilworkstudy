package com.nci.domino.beans.desyer.event;

import java.util.List;

import com.nci.domino.beans.desyer.WofoEventBean;

public class WofoEventProcedureHandlerBean extends WofoEventBean {

    private String procedure;
    private List params;

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params = params;
    }
}
