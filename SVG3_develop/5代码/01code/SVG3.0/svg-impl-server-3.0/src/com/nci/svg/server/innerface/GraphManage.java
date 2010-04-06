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
 * ���⣺GraphManage.java
 * </p>
 * <p>
 * ������ͼ���ļ������࣬�ṩͼ���ļ���ȡ��ط���
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-23
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
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ͼ�ι���δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ͼ�ι���δ�ܻ�ȡ��־��������!");
		}

		log
				.log(this, LoggerAdapter.DEBUG, "ͼ�ι����࣬��ȡ����" + actionName
						+ "���������");

		if (ActionNames.GET_SUPPORT_FILE_TYPE.equalsIgnoreCase(actionName)) {
			// ************************
			// ��ȡָ��ҵ��ϵͳ��ͼ�����嵥
			// ************************
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			rb = getSupportFileType(businessID);
		} else if (ActionNames.GET_GRAPHFILE_LIST.equalsIgnoreCase(actionName)) {
			// ********************
			// ��ȡָ��ҵ��ϵͳ�ļ��б�
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
			// �����ļ���ָ��ҵ��ϵͳͼ�α���
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
			// ��ָ��ҵ��ϵͳͼ�α��л�ȡͼ����Ϣ
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
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}

		return rb;
	}

	/**
	 * ��ȡָ��ҵ��ϵͳ�ļ��б�
	 * 
	 * @param businessID:String:ϵͳ���
	 * @param graphBusinessType:String:ҵ������
	 * @return ����ָ��ϵͳ��ָ��ҵ�����͵�ͼ�ļ��б����ҵ������Ϊ���򷵻ظ�ϵͳ�������ļ��嵥
	 */
	private ResultBean getFilesInformation(String businessID,
			GraphFileBean fileBean) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getFilesInformation(businessID, fileBean);
	}

	/**
	 * ��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ�����
	 * 
	 * @param bussID:String:ҵ��ϵͳ���
	 * @return:ָ��ϵͳ֧�ֵ�ͼ���ļ�����
	 */
	private ResultBean getSupportFileType(String bussID) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSupportFileType(bussID);
	}

	/**
	 * ��ȡָ���ļ�����
	 * 
	 * @param businessID:String:ϵͳ���
	 * @param obj:GraphFileBean:ͼ���ļ�����
	 * @return
	 */
	private ResultBean getGraphFile(String businessID, GraphFileBean obj) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.loadGraph(businessID, obj);
	}

	/**
	 * �洢ָ���ļ�
	 * 
	 * @param businessID:String:ϵͳ���
	 * @param obj:GraphFileBean:ͼ���ļ�����
	 * @return
	 */
	private ResultBean saveGraphFile(String businessID, String logs,
			GraphFileBean obj) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.saveGraph(businessID, logs, obj);
	}
}
