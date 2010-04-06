package com.nci.ums.sync;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.nci.ums.sync.ldap.LdapOrgResult;


	public abstract class AbstractProcessOrgSync extends AbstractProcess implements ProcessSyncOrg{
	   
	    public boolean doSync(final LdapOrgResult result) {
	        TransactionTemplate tt = new TransactionTemplate(getJdbcTransactionManager());//申明数据库操作事务处理模版
	        Boolean flag = (Boolean) tt.execute(new TransactionCallback() {
	            public Object doInTransaction(TransactionStatus status) {
	                if(!status.isRollbackOnly()) {//不是回滚事务
	                    boolean f = excuteSync(result);
	                    if(!f)status.setRollbackOnly();
	                    return new Boolean(f);
	                }
	                return new Boolean(false);
	            }
	        });
	        return flag.booleanValue();
	    }
	    /**
	     * 
	     * @param sapClass
	     * @param result
	     * @return
	     */
	    public boolean excuteSync(LdapOrgResult result) {
	    	return false;}//
	
	}

