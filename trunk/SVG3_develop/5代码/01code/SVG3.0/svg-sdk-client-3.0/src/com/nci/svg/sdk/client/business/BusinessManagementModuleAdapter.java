package com.nci.svg.sdk.client.business;

import com.nci.svg.sdk.module.DefaultModuleAdapter;

/**
 * @author yx.nci
 * 业务组件管理器基类
 */
public abstract class BusinessManagementModuleAdapter extends
		DefaultModuleAdapter {

	public String getModuleType() {
		return "business module management";
	}

}
