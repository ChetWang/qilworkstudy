
package com.nci.svg.sdk.server.module;

import java.util.HashMap;

import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-11-25
 * @功能：服务器端组件基础抽象类
 *
 */
public abstract class ServerModuleAdapter extends DefaultModuleAdapter {

	protected ServerModuleControllerAdapter controller = null; 
	
	public ServerModuleAdapter(HashMap parameters)
	{
		super(parameters);
	}

}
