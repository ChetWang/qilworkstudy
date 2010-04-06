package com.nci.ums.sync;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.nci.ums.sync.ldap.LdapPerResult;


	public abstract class AbstractProcessSync extends AbstractProcess implements ProcessSync{
	   
	    public boolean doSync(final LdapPerResult result) {
	        TransactionTemplate tt = new TransactionTemplate(getJdbcTransactionManager());//�������ݿ����������ģ��
	        Boolean flag = (Boolean) tt.execute(new TransactionCallback() {
	            public Object doInTransaction(TransactionStatus status) {
	                if(!status.isRollbackOnly()) {//���ǻع�����
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
	    public boolean excuteSync(LdapPerResult result) {
	    	return false;}//
	   
    }

