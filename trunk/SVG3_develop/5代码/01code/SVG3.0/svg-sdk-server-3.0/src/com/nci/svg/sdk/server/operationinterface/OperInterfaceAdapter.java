package com.nci.svg.sdk.server.operationinterface;

import java.util.HashMap;

import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;


/**
 * <p>
 * ���⣺OperInterfaceAdapter.java
 * </p>
 * <p>
 * ������ ҵ��ϵͳ��Ϣ��ȡ�ӿ�
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-05
 * @version 1.0
 */
public abstract class OperInterfaceAdapter extends OperationServiceModuleAdapter{
	/*	
	 * �ӿڲ����õĽṹ
	 *	<?xml version="1.0" encoding="UTF-8"?>
	 *	<params>
	 *		<param>
	 *			<!-- ������ -->
	 *			<name></name>
	 *			<!-- ����ֵ -->
	 *			<value></value>
	 *		</param>
	 *	</params>
	 *	�������ݵĽṹ
	 *	<?xml version="1.0" encoding="UTF-8"?>
	 *	<results>
	 *		<result>
	 *			<!-- ����ֵ���� -->
	 *			<name></name>
	 *			<!-- ����ֵ -->
	 *			<value></value>
	 *		</result>
	 *	</results>
	*/	
	
	public OperInterfaceAdapter(HashMap parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	public String getModuleType() {
		// TODO Auto-generated method stub
		return ModuleDefines.OPER_INTERFACE_MODULE;
	}

	


}
