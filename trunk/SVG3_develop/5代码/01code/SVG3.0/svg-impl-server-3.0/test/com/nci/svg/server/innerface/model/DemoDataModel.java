package com.nci.svg.server.innerface.model;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.automapping.comm.AutoMapComm;
import com.nci.svg.server.util.Global;

/**
 * <p>
 * 标题：MedelDataModel.java
 * </p>
 * <p>
 * 描述： 业务模型模拟数据提供类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-4-8
 * @version 1.0
 */
public class DemoDataModel extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = -6415988448599922732L;

	public DemoDataModel(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("代码查询功能，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("代码查询功能，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "代码查询功能，获取到‘" + actionName
				+ "’请求命令！");
		ResultBean rb = new ResultBean();
		if (actionName.equalsIgnoreCase(ActionNames.GET_MODEL_DEMO_DATA)) {
			String company = (String) getRequestParameter(requestParams, "company");
			rb = getCompanyDemo(company);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_AREA_DEMO_DATA)) {
			String filename = (String) getRequestParameter(requestParams,
					"filename");
			rb = getDemo(filename);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}

		return rb;
	}
	
	private ResultBean getCompanyDemo(String company){
		String pathname = Global.appRoot + "/WEB-INF/classes/demoModelData.xml";
		if (company != null && company.length() > 0) {
			pathname = Global.appRoot + "/WEB-INF/classes/" + company + ".xml";
		}
		String dataString = AutoMapComm.readDataString(pathname, true);
		ResultBean rb = new ResultBean(ResultBean.RETURN_SUCCESS, null,
				"String", dataString);
		return rb;
	}

	private ResultBean getDemo(String area) {
		String pathname = Global.appRoot + "/WEB-INF/classes/demoAreaData.xml";
		if (area != null && area.length() > 0) {
			pathname = Global.appRoot + "/WEB-INF/classes/" + area + ".xml";
		}
		String dataString = AutoMapComm.readDataString(pathname, true);
		ResultBean rb = new ResultBean(ResultBean.RETURN_SUCCESS, null,
				"String", dataString);
		return rb;
	}

}
