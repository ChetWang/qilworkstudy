package com.nci.ums.sync;

import com.nci.ums.sync.ldap.LdapOrgResult;
import com.nci.ums.sync.ldap.LdapPerResult;



public interface ProcessSyncOrg {
	public boolean doSync(LdapOrgResult result) ;
}
