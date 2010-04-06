package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.framemap.CreateFrameMap;
import com.nci.svg.server.automapping.framemap.FrameMapDbManage;
import com.nci.svg.server.automapping.framemap.FrameNode;
import com.nci.svg.server.automapping.framemap.ReadData;

/**
 * <p>
 * 标题：AutoFrameMap.java
 * </p>
 * <p>
 * 描述： 处理自动生成结构图请求
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-15
 * @version 1.0
 */
public class AutoFrameMap extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = 898991538415318390L;

	public AutoFrameMap(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("结构图自动成图，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("结构图自动成图，未能获取日志操作对象!");
		}
		log.log(this, LoggerAdapter.DEBUG, "结构图自动成图，获取到‘" + actionName
				+ "’请求命令！");

		// 获取请求参数
		String sysname = (String) getRequestParameter(requestParams, "sysname"); // 业务系统名

		ResultBean rb = new ResultBean();
		if (sysname.equals("sixhas")) {
			// ***********
			// 烟草六有系统
			// ***********
			String type = (String) getRequestParameter(requestParams, "type"); // 结构图类型
			String id = (String) getRequestParameter(requestParams, "id"); // 编号
			String contextPath = (String) getRequestParameter(requestParams,
					"contextpath"); // 上下文地址
			String isNew = (String) getRequestParameter(requestParams, "isnew");// 强行自动成图标志
			String content = (String) getRequestParameter(requestParams,
					"content");// 文件内容
			String isSave = (String) getRequestParameter(requestParams,
					"issave"); // 文件保存标志

			if ("true".equalsIgnoreCase(isNew)) {
				// 强行自动成图
				rb = getAutoFrameMap(type, id, contextPath);
			} else if ("save".equalsIgnoreCase(isSave)) {
				// 保存svg文件到数据库中
				rb = saveDbFrameMap(type, id, content);
			} else if ("delete".equalsIgnoreCase(isSave)) {
				// 删除数据库中的SVG文件
				rb = delSvgFromDb(type, id);
			} else {
				long readB = System.currentTimeMillis();
				// 从数据库中获取结构图
				rb = getDbFrameMap(type, id);
				long readE = System.currentTimeMillis();
				log.log(this, LoggerAdapter.DEBUG, "结构图自动成图，获取数据库中图形文件时间："
						+ (readE - readB));
				if (rb.getReturnFlag() == ResultBean.RETURN_ERROR) {
					// 从数据库中获取结构图失败
					rb = getAutoFrameMap(type, id, contextPath);
				}
			}
		} else {
			// ***********
			// 指定系统无效
			// ***********
			return returnErrMsg("指定的sysname无效！");
		}
		return rb;
	}
	
	/**
	 * 2009-7-1
	 * Add by ZHM
	 * @功能 从数据库中删除指定svg文件
	 * @param type:String:结构图类型
	 * @param id:String:编号
	 * @return
	 */
	private ResultBean delSvgFromDb(String type, String id){
		ResultBean rb = new ResultBean();
		GraphFileBean fileBean = new GraphFileBean();
		fileBean.setFileName(type + "_" + id);
		FrameMapDbManage fm = new FrameMapDbManage(controller);
		rb = fm.delGraph("1", fileBean);
		return rb;
	}

	/**
	 * 2009-6-29 Add by ZHM
	 * 
	 * @功能 保存修改的结构图文件到数据库中
	 * @param type:String:结构图类型
	 * @param id:String:编号
	 * @param content:String:SVG文件内容
	 * @return
	 */
	private ResultBean saveDbFrameMap(String type, String id, String content) {
		ResultBean rb = new ResultBean();

//		HashMap requestParams = new HashMap(); // 获取数据库中结构图参数
//
//		String[] params = new String[] { "saveGraphFile" };
//		requestParams.put("action", params);
//		params = new String[] { type + "_" + id };
//		requestParams.put("filename", params);
//		params = new String[] { "1" };// psms库
//		requestParams.put("businessID", params);
//		params = new String[] { "2" };// 个性图
//		requestParams.put("graphType", params);
//		params = new String[] { "flow" };// 流程图
//		requestParams.put("graphBusinessType", params);
//		params = new String[] { "svg" };
//		requestParams.put("fileFormat", params);
//		params = new String[] { "sys" };
//		requestParams.put("operator", params);
//		params = new String[] { content };
//		requestParams.put("content", params);
//
//		GraphManage gm = new GraphManage(parameters);
//		rb = gm.handleOper(ActionNames.SAVE_GRAPH_FILE, requestParams);
		
		GraphFileBean fileBean = new GraphFileBean();
		fileBean.setFileName(type + "_" + id);
		fileBean.setContent(content);
		fileBean.setFileFormat("svg");
		fileBean.setFileType("flow");
		fileBean.setOperator("sys");
		
		FrameMapDbManage fm = new FrameMapDbManage(controller);
		rb = fm.saveGraph("1", fileBean);
		
		return rb;
	}

	/**
	 * 2009-6-29 Add by ZHM
	 * 
	 * @功能 从数据库中获取指定结构图
	 * @param type:String:类型
	 * @param id:String:编号
	 * @return
	 */
	private ResultBean getDbFrameMap(String type, String id) {
		ResultBean rb = new ResultBean();

//		HashMap requestParams = new HashMap(); // 获取数据库中结构图参数
//		String[] params = new String[] { "getSymbolsVersion" };
//		requestParams.put("action", params);
//		params = new String[] { type + "_" + id };
//		requestParams.put("filename", params);
//		params = new String[] { "1" };
//		requestParams.put("businessID", params);
//
//		GraphManage gm = new GraphManage(parameters);
//		rb = gm.handleOper(ActionNames.GET_GRAPH_FILE, requestParams);
		
		GraphFileBean obj = new GraphFileBean();
		obj.setFileName(type + "_" + id);
		FrameMapDbManage fm = new FrameMapDbManage(controller);
		rb = fm.loadGraph("1", obj);
		
		if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
			GraphFileBean fileBean = (GraphFileBean) rb.getReturnObj();
			String content = fileBean.getContent();
			rb.setReturnFlag(ResultBean.RETURN_STRING);
			rb.setReturnObj(content);
		}
		return rb;
	}

	/**
	 * 2009-6-29 Add by ZHM
	 * 
	 * @功能 自动生成结构图
	 * @param type:String:类型
	 * @param id:String:编号
	 * @param contextPath:String:上下文地址
	 * @return
	 */
	private ResultBean getAutoFrameMap(String type, String id,
			String contextPath) {
		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得结构图数据！");
			return returnErrMsg("取图操作，无法获取有效数据库连接！");
		}

		ReadData read = new ReadData(conn, "sixhas");

		if (type == null) {
			return returnErrMsg("结构图自动成图缺少type参数！");
		}
		if (id == null) {
			return returnErrMsg("结构图自动成图缺少id参数！");
		}

		long readB = System.currentTimeMillis();
		// 获取结构图数据
		AutoMapResultBean result = read.read(new String[] { type, id });
		long readE = System.currentTimeMillis();
		log.log(this, LoggerAdapter.DEBUG, "结构图自动成图，获取数据时间：" + (readE - readB));
		// 关闭数据库连接
		controller.getDBManager().close(conn);

		ResultBean rb = new ResultBean();
		if (result.isFlag()) {
			FrameNode data = (FrameNode) result.getMsg();
			CreateFrameMap createFrameMap = new CreateFrameMap(data,
					contextPath);
			result = createFrameMap.createSVG();
			long createE = System.currentTimeMillis();
			log.log(this, LoggerAdapter.DEBUG, "结构图自动成图，生成SVG文件时间:"
					+ (createE - readE));
			if (result.isFlag()) {
				rb.setReturnFlag(ResultBean.RETURN_STRING);
//				System.out.println(result.getMsg());
				rb.setReturnObj(result.getMsg());
			}
		} else {
			return returnErrMsg(result.getErrMsg());
		}

		return rb;
	}

}
