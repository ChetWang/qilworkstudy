package com.nci.ums.sync.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.nci.ums.sync.AbstractProcessSync;
import com.nci.ums.sync.ldap.LdapPerResult;
import com.nci.ums.sync.util.BatchUpdateUtil;
import com.nci.ums.sync.util.SqlUtil;

public class UMSSynPer extends AbstractProcessSync{
	 public boolean excuteSync(LdapPerResult result){
	    	JdbcTemplate template       = getJdbcTemplate();
	        BatchUpdateUtil batchUtil   = new BatchUpdateUtil(template);
	        //更新UUM表
	        String[] values     = null;
	        int[] types			= {
    		SqlUtil.STRING,
    		SqlUtil.STRING,
    		SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.INTEGER,
            SqlUtil.STRING,
            SqlUtil.STRING
    		}; 
	        values = new String[]{
	        		result.getUsername(),
	        		result.getEmail(),
	        		result.getPhone(),
	        		result.getSex(),
	        		result.getOrgid(),
	        		result.getPassword(),
	        		result.getLoginname(),
	        		"1",
	        		"ums",
	        		result.getSeqkey(),
	        		};
	        int count = template.queryForInt("select count(*) from uum_user where uum_user.user_id = '"+result.getSeqkey()+"'");
	        if(count >0) {
	        	batchUtil.addSql(SqlUtil.getSql("update uum_user set user_name=?,USER_EMAIL =?,USER_MOBILE =?,USER_SEX=?,USER_ORG_ID=?,USER_LOGIN_PASSWD=?,USER_LOGIN_NAME=?,USER_STATUS=?,USER_SYSTEM_CODE=?" +
                        " where user_id=?",values,types));
            } else {
            	batchUtil.addSql(SqlUtil.getSql("insert into uum_user (user_name,USER_EMAIL,USER_MOBILE,USER_SEX,USER_ORG_ID,USER_LOGIN_PASSWD,USER_LOGIN_NAME,USER_STATUS,USER_SYSTEM_CODE,user_id) "
                    + "values(?,?,?,?,?,?,?,?,?,?)",values,types));
            }
	        //更新HR_HI_PERSON表
	        String[] values1     = null;
	        int[] types1			= {
	        SqlUtil.STRING,
    		SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING,
            SqlUtil.STRING
    		}; 
	        values1 = new String[]{
	        		result.getSeqkey(),
	        		result.getUsername(),
	        		result.getSex(),
	        		result.getOrgid(),
                    "UMS",
	        		result.getSeqkey()
	        		};
	        int count1 = template.queryForInt("select count(*) from hr_hi_person where PSNNUM = '"+result.getSeqkey()+"'");
	        if(count1 >0) {
	        	batchUtil.addSql(SqlUtil.getSql("update hr_hi_person set SEQKEY= ?, NAME =?, SEX =?, ORGCODE=?,PERSONTYPE=?" +
                        " where PSNNUM=?",values1,types1));
            } else {
            	batchUtil.addSql(SqlUtil.getSql("insert into hr_hi_person (SEQKEY,NAME,SEX,ORGCODE,PERSONTYPE,PSNNUM) "
                    + "values(?,?,?,?,?,?)",values1,types1));
            }
	        batchUtil.end();
	        return true;
	    }
	
}
