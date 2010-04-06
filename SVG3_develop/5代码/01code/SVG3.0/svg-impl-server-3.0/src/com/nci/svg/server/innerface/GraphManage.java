package com.nci.svg.server.innerface;

import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * 标题：GraphManage.java
 * </p>
 * <p>
 * 描述：图形文件管理类，提供图形文件存取相关服务
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-23
 * @version 1.0
 */
public class GraphManage extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -160604437537156353L;

	public GraphManage(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("图形管理，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("图形管理，未能获取日志操作对象!");
		}

		log
				.log(this, LoggerAdapter.DEBUG, "图形管理类，获取到‘" + actionName
						+ "’请求命令！");

		if (ActionNames.GET_SUPPORT_FILE_TYPE.equalsIgnoreCase(actionName)) {
			// ************************
			// 获取指定业务系统中图类型清单
			// ************************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getSupportFileType(businessID);
		} else if (ActionNames.GET_GRAPHFILE_LIST.equalsIgnoreCase(actionName)) {
			// ********************
			// 获取指定业务系统文件列表
			// ********************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String fileType = (String) getRequestParameter(requestParams,
					ActionParams.GRAPH_TYPE);
			String busiType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);
			String fileFormat = (String) getRequestParameter(requestParams,
					ActionParams.FILE_FORMAT);
			String[] params = new String[10];
			for (int i = 0, size = 10; i < size; i++) {
				params[i] = (String) getRequestParameter(requestParams,
						ActionParams.PARAM + i);
			}
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR);

			GraphFileBean fileBean = new GraphFileBean();
			fileBean.setBusiType(busiType);
			fileBean.setFileFormat(fileFormat);
			fileBean.setFileType(fileType);
			fileBean.setOperator(operator);
			fileBean.setParams(params);

			rb = getFilesInformation(businessID, fileBean);
		} else if (ActionNames.SAVE_GRAPH_FILE.equalsIgnoreCase(actionName)) {
			// *************************
			// 保存文件到指定业务系统图形表中
			// *************************
			String id = (String) getRequestParameter(requestParams,
					ActionParams.ID);
			String fileName = (String) getRequestParameter(requestParams,
					ActionParams.FILE_NAME);
			String fileType = (String) getRequestParameter(requestParams,
					ActionParams.GRAPH_TYPE);
			String busiType = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_TYPE);
			String fileFormat = (String) getRequestParameter(requestParams,
					ActionParams.FILE_FORMAT);
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR);
			String content = (String) getRequestParameter(requestParams,
					ActionParams.CONTENT);
			String[] params = new String[10];
			for (int i = 0, size = 10; i < size; i++) {
				params[i] = (String) getRequestParameter(requestParams,
						ActionParams.PARAM + i);
			}
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String logs = (String) getRequestParameter(requestParams,
					ActionParams.LOGS);

			GraphFileBean fileBean = new GraphFileBean();
			fileBean.setID(id);
			fileBean.setBusiType(busiType);
			fileBean.setContent(content);
			fileBean.setFileFormat(fileFormat);
			fileBean.setFileName(fileName);
			fileBean.setFileType(fileType);
			fileBean.setOperator(operator);
			fileBean.setParams(params);

			rb = saveGraphFile(businessID, logs, fileBean);
		} else if (ActionNames.GET_GRAPH_FILE.equalsIgnoreCase(actionName)) {
			// *****************************
			// 从指定业务系统图形表中获取图形信息
			// *****************************
			String id = (String) getRequestParameter(requestParams,
					ActionParams.ID);
			String fileName = (String) getRequestParameter(requestParams,
					ActionParams.FILE_NAME);
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);

			GraphFileBean fileBean = new GraphFileBean();
			fileBean.setID(id);
			fileBean.setFileName(fileName);

			rb = getGraphFile(businessID, fileBean);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}

		return rb;
	}

	/**
	 * 获取指定业务系统文件列表
	 * 
	 * @param businessID:String:系统编号
	 * @param graphBusinessType:String:业务类型
	 * @return 返回指定系统下指定业务类型的图文件列表，如果业务类型为空则返回该系统下所有文件清单
	 */
	private ResultBean getFilesInformation(String businessID,
			GraphFileBean fileBean) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getFilesInformation(businessID, fileBean);
	}

	/**
	 * 获取指定业务系统支持的图形文件类型
	 * 
	 * @param bussID:String:业务系统编号
	 * @return:指定系统支持的图形文件类型
	 */
	private ResultBean getSupportFileType(String bussID) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSupportFileType(bussID);
	}

	/**
	 * 获取指定文件内容
	 * 
	 * @param businessID:String:系统编号
	 * @param obj:GraphFileBean:图形文件对象
	 * @return
	 */
	private ResultBean getGraphFile(String businessID, GraphFileBean obj) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.loadGraph(businessID, obj);
	}

	/**
	 * 存储指定文件
	 * 
	 * @param businessID:String:系统编号
	 * @param obj:GraphFileBean:图形文件对象
	 * @return
	 */
	private ResultBean saveGraphFile(String businessID, String logs,
			GraphFileBean obj) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.saveGraph(businessID, logs, obj);
	}
}
