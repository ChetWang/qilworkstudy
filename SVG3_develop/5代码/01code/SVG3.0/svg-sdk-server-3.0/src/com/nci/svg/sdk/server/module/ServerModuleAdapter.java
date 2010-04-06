
package com.nci.svg.sdk.server.module;

import java.util.HashMap;

import com.nci.svg.sdk.module.DefaultModuleAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-25
 * @���ܣ����������������������
 *
 */
public abstract class ServerModuleAdapter extends DefaultModuleAdapter {

	protected ServerModuleControllerAdapter controller = null; 
	
	public ServerModuleAdapter(HashMap parameters)
	{
		super(parameters);
	}

}
