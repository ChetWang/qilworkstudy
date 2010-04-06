package com.nci.ums.sync;

import com.nci.ums.sync.ldap.LdapPerResult;



public interface ProcessSync {
	public boolean doSync(LdapPerResult result) ;
}
