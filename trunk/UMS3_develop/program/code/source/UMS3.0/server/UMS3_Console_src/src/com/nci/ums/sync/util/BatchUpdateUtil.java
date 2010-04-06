package com.nci.ums.sync.util;

import java.util.Vector;
import org.springframework.jdbc.core.JdbcTemplate;


public class BatchUpdateUtil {

    private JdbcTemplate template = null;
    private String[] sql    = null;
    private Vector list     = new Vector();
    private int i           = 0;
    public BatchUpdateUtil(JdbcTemplate template) {
        this.template   = template;
        int capacity    = 100;
        capacity = 100;
        sql = new String[capacity];
    }
    
    public void addSql(String sql1) {
        //System.out.println(sql1);
        if(i >= sql.length) {
            template.batchUpdate(sql);
            i = 0;
        }
        sql[i] = sql1;
        i++;
    }
    /**
     * 
     * @param filter 按照此参数过滤sql
     * @param sql
     */
    public void addSql(String filter,String sql) {
        if(filter == null || "".equals(filter)) return;
        if(!list.contains(filter)) {
            list.add(filter);
            addSql(sql);
        }
    }
    
    public void end() {
        String[] lastSql = new String[i];
        System.arraycopy(sql, 0, lastSql, 0, lastSql.length);
        template.batchUpdate(lastSql);
    }
}
