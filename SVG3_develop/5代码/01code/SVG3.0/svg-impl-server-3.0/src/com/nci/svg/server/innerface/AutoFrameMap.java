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
 * ���⣺AutoFrameMap.java
 * </p>
 * <p>
 * ������ �����Զ����ɽṹͼ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-15
 * @version 1.0
 */
public class AutoFrameMap extends OperationServiceModuleAdapter {
	private static final long serialVersionUID = 898991538415318390L;

	public AutoFrameMap(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("�ṹͼ�Զ���ͼ��δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("�ṹͼ�Զ���ͼ��δ�ܻ�ȡ��־��������!");
		}
		log.log(this, LoggerAdapter.DEBUG, "�ṹͼ�Զ���ͼ����ȡ����" + actionName
				+ "���������");

		// ��ȡ�������
		String sysname = (String) getRequestParameter(requestParams, "sysname"); // ҵ��ϵͳ��

		ResultBean rb = new ResultBean();
		if (sysname.equals("sixhas")) {
			// ***********
			// �̲�����ϵͳ
			// ***********
			String type = (String) getRequestParameter(requestParams, "type"); // �ṹͼ����
			String id = (String) getRequestParameter(requestParams, "id"); // ���
			String contextPath = (String) getRequestParameter(requestParams,
					"contextpath"); // �����ĵ�ַ
			String isNew = (String) getRequestParameter(requestParams, "isnew");// ǿ���Զ���ͼ��־
			String content = (String) getRequestParameter(requestParams,
					"content");// �ļ�����
			String isSave = (String) getRequestParameter(requestParams,
					"issave"); // �ļ������־

			if ("true".equalsIgnoreCase(isNew)) {
				// ǿ���Զ���ͼ
				rb = getAutoFrameMap(type, id, contextPath);
			} else if ("save".equalsIgnoreCase(isSave)) {
				// ����svg�ļ������ݿ���
				rb = saveDbFrameMap(type, id, content);
			} else if ("delete".equalsIgnoreCase(isSave)) {
				// ɾ�����ݿ��е�SVG�ļ�
				rb = delSvgFromDb(type, id);
			} else {
				long readB = System.currentTimeMillis();
				// �����ݿ��л�ȡ�ṹͼ
				rb = getDbFrameMap(type, id);
				long readE = System.currentTimeMillis();
				log.log(this, LoggerAdapter.DEBUG, "�ṹͼ�Զ���ͼ����ȡ���ݿ���ͼ���ļ�ʱ�䣺"
						+ (readE - readB));
				if (rb.getReturnFlag() == ResultBean.RETURN_ERROR) {
					// �����ݿ��л�ȡ�ṹͼʧ��
					rb = getAutoFrameMap(type, id, contextPath);
				}
			}
		} else {
			// ***********
			// ָ��ϵͳ��Ч
			// ***********
			return returnErrMsg("ָ����sysname��Ч��");
		}
		return rb;
	}
	
	/**
	 * 2009-7-1
	 * Add by ZHM
	 * @���� �����ݿ���ɾ��ָ��svg�ļ�
	 * @param type:String:�ṹͼ����
	 * @param id:String:���
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
	 * @���� �����޸ĵĽṹͼ�ļ������ݿ���
	 * @param type:String:�ṹͼ����
	 * @param id:String:���
	 * @param content:String:SVG�ļ�����
	 * @return
	 */
	private ResultBean saveDbFrameMap(String type, String id, String content) {
		ResultBean rb = new ResultBean();

//		HashMap requestParams = new HashMap(); // ��ȡ���ݿ��нṹͼ����
//
//		String[] params = new String[] { "saveGraphFile" };
//		requestParams.put("action", params);
//		params = new String[] { type + "_" + id };
//		requestParams.put("filename", params);
//		params = new String[] { "1" };// psms��
//		requestParams.put("businessID", params);
//		params = new String[] { "2" };// ����ͼ
//		requestParams.put("graphType", params);
//		params = new String[] { "flow" };// ����ͼ
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
	 * @���� �����ݿ��л�ȡָ���ṹͼ
	 * @param type:String:����
	 * @param id:String:���
	 * @return
	 */
	private ResultBean getDbFrameMap(String type, String id) {
		ResultBean rb = new ResultBean();

//		HashMap requestParams = new HashMap(); // ��ȡ���ݿ��нṹͼ����
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
	 * @���� �Զ����ɽṹͼ
	 * @param type:String:����
	 * @param id:String:���
	 * @param contextPath:String:�����ĵ�ַ
	 * @return
	 */
	private ResultBean getAutoFrameMap(String type, String id,
			String contextPath) {
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷���ýṹͼ���ݣ�");
			return returnErrMsg("ȡͼ�������޷���ȡ��Ч���ݿ����ӣ�");
		}

		ReadData read = new ReadData(conn, "sixhas");

		if (type == null) {
			return returnErrMsg("�ṹͼ�Զ���ͼȱ��type������");
		}
		if (id == null) {
			return returnErrMsg("�ṹͼ�Զ���ͼȱ��id������");
		}

		long readB = System.currentTimeMillis();
		// ��ȡ�ṹͼ����
		AutoMapResultBean result = read.read(new String[] { type, id });
		long readE = System.currentTimeMillis();
		log.log(this, LoggerAdapter.DEBUG, "�ṹͼ�Զ���ͼ����ȡ����ʱ�䣺" + (readE - readB));
		// �ر����ݿ�����
		controller.getDBManager().close(conn);

		ResultBean rb = new ResultBean();
		if (result.isFlag()) {
			FrameNode data = (FrameNode) result.getMsg();
			CreateFrameMap createFrameMap = new CreateFrameMap(data,
					contextPath);
			result = createFrameMap.createSVG();
			long createE = System.currentTimeMillis();
			log.log(this, LoggerAdapter.DEBUG, "�ṹͼ�Զ���ͼ������SVG�ļ�ʱ��:"
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
