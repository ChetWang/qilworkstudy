package com.nci.ums.sync.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nci.ums.sync.AbstractProcessOrgSync;
import com.nci.ums.sync.AbstractProcessSync;
import com.nci.ums.sync.ldap.LdapOrgResult;
import com.nci.ums.sync.ldap.LdapPerResult;
import com.nci.ums.sync.util.BatchUpdateUtil;
import com.nci.ums.sync.util.SqlUtil;

public class UMSSynOrg extends AbstractProcessOrgSync{
	 public boolean excuteSync(LdapOrgResult result){
	    	JdbcTemplate template       = getJdbcTemplate();
	        BatchUpdateUtil batchUtil   = new BatchUpdateUtil(template);
	        //更新UUM表
	        String[] values     = null;
	        int[] types			= {
    		SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,    //ORG_SYSTEM_CODE
            SqlUtil.STRING
    		}; 
	        values = new String[]{
	        		result.getOrgname(),
	        		result.getParentid(),
	        		result.getOrgid(),
	        		"1",
	        		result.getOrgid()
	        		};
	        int count = template.queryForInt("select count(*) from uum_org where ORG_ID = '"+result.getOrgid()+"'");
	        if(count >0) {
	        	batchUtil.addSql(SqlUtil.getSql("update uum_org set ORG_NAME=?,ORG_PARENT_ID =?,ORG_CODE =?,ORG_SYSTEM_CODE=?" +
                        " where ORG_ID=?",values,types));
            } else {
            	batchUtil.addSql(SqlUtil.getSql("insert into uum_org (ORG_NAME,ORG_PARENT_ID,ORG_CODE,ORG_SYSTEM_CODE,ORG_ID) "
                    + "values(?,?,?,?,?)",values,types));
            }
	        int count2 = template.queryForInt("select count(*) from uum_org where ORG_NAME = '"+result.getParentid()+"'");
	        if(count2==1) {
	        	batchUtil.addSql("update uum_org set ORG_PARENT_ID = (select uum_org.ORG_CODE from uum_org where ORG_NAME = '"+result.getParentid()+"'"+")");
	        } 
	        //更新HR_ORG_BASE表
	        String[] values1     = null;
	        int[] types1			= {
	        		SqlUtil.STRING,
	        		SqlUtil.STRING,
	                SqlUtil.STRING,
	                SqlUtil.INTEGER,
	                SqlUtil.STRING
    		}; 
	        values1 = new String[]{
	        		result.getOrgid(),
	        		result.getOrgname(),
	        		result.getParentid(),
	        		result.getOrgtype(),
	        		result.getOrgid()
	        		};
	        int count1 = template.queryForInt("select count(*) from hr_org_base where ORGCODE = '"+result.getOrgid()+"'");
	        if(count1 >0) {
	        	batchUtil.addSql(SqlUtil.getSql("update hr_org_base set SEQKEY= ?, ORGNAME =?, PARENTID =?, ORGTYPE=?" +
                        " where ORGCODE=?",values1,types1));
            } else {
            	batchUtil.addSql(SqlUtil.getSql("insert into hr_org_base (SEQKEY,ORGNAME,PARENTID,ORGTYPE,ORGCODE) "
                    + "values(?,?,?,?,?)",values1,types1));
            }
	        int count4 = template.queryForInt("select count(*) from hr_org_base where ORGNAME = '"+result.getParentid()+"'");
	        if(count4==1) {
	        	batchUtil.addSql("update hr_org_base set PARENTID = (select hr_org_base.ORGCODE from hr_org_base where ORGNAME = '"+result.getParentid()+"'"+")");
	        }
	        batchUtil.end();
	        return true;
	    }
	
}
