package com.nci.domino.beans.desyer.event;

import com.nci.domino.beans.desyer.WofoEventBean;

public class WofoEventSQLHandlerBean extends WofoEventBean {
	private static final long serialVersionUID = 9018428288087927185L;

	private String sqlText;

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }
}
