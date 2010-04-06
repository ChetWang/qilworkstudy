package com.nci.svg.sdk.client.util;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.GraphFileParamsBean;
import com.nci.svg.sdk.bean.RelaIndunormSymbolBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-30
 * @���ܣ�Զ�̷��ʹ�����
 * 
 */
public class RemoteUtilities {
	/**
	 * add by yux,2008.12.30 ��ȡָ��ҵ��ϵͳ��ͼ�����ļ������б�
	 * 
	 * @param editor:��ǰ�༭��
	 * @param fileType��ͼ���ͣ���ͼ����Ϊ��ʱ�����ظ�ҵ��ϵͳ�������ļ������б�
	 * @return��ArrayList<GraphFileParamsBean>
	 */
	public static ArrayList<GraphFileParamsBean> getGraphFileParams(
			EditorAdapter editor, String fileType) {
		String[][] params = new String[2][2];
		params[0][0] = ActionParams.BUSINESS_TYPE;
		
		params[0][1] = fileType;
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_GRAPHFILE_PARAMS,
						params));
		if (bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return (ArrayList<GraphFileParamsBean>) bean.getReturnObj();
	}

	/**
	 * add by yux,2008-12-30 ����ָ�����ļ�id��name��ȡָ��ҵ��ϵͳ��ͼ���ļ�
	 * 
	 * @param editor
	 * @param id��ͼid
	 * @param name��ͼname
	 * @return��GraphFileBean
	 */
	public static GraphFileBean getGraphFile(EditorAdapter editor, String id,
			String name) {
		if ((id == null || id.length() == 0)
				&& (name == null || name.length() == 0))
			return null;
		String[][] params = new String[2][2];
		params[0][0] = ActionParams.ID;
		params[0][1] = id;
		params[1][0] = ActionParams.FILE_NAME;
		params[1][1] = name;
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_GRAPH_FILE,
						params));
		if (bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return (GraphFileBean) bean.getReturnObj();
	}

	public static ArrayList<GraphFileBean> getGraphFileList(
			EditorAdapter editor, String filetype, String graphBusinessType,
			String fileformat, String[] param, String operator) {
		String[][] params = new String[13][2];
		params[0][0] = ActionParams.GRAPH_TYPE;
		params[0][1] = filetype;
		params[1][0] = ActionParams.BUSINESS_TYPE;
		params[1][1] = graphBusinessType;
		params[2][0] = ActionParams.FILE_FORMAT;
		params[2][1] = fileformat;

		for (int i = 0; i < 10; i++) {
			params[3 + i][0] = ActionParams.PARAM + i;
			if (param != null && param.length > i)
				params[3 + i][1] = param[i];
			else
				params[3 + i][1] = "";
		}


		ResultBean resultBean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_GRAPHFILE_LIST,
						params));
		if (resultBean == null
				|| resultBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return null;
		}
		return (ArrayList<GraphFileBean>) resultBean.getReturnObj();
	}

	/**
	 * add by yux,2009-1-4
	 * ����������ڵ�
	 * @param editor���༭��
	 * @param graphType��ͼ����
	 * @param symbolType��ͼԪ����
	 * @param symbolID��ͼԪ���
	 * @param element��ͼԪ�ڵ�
	 */
	public static void appendMetadataElement(EditorAdapter editor, String graphType,
			String symbolType, String symbolID, Element element) {
		Document doc = element.getOwnerDocument();
		Element metadataElement = doc.createElement("metadata");
		element.appendChild(metadataElement);
		ArrayList<RelaIndunormSymbolBean> list = editor.getIndunormManager()
				.getIndunormSymbolRela(graphType, symbolType, symbolID);
		String type = null,metadata = null,field = null;
		Element el = null;
		if(list != null)
		{
			for(RelaIndunormSymbolBean bean : list)
			{
				if(!bean.getTypeBean().getShortName().equals(type) || !bean.getMetadataBean().getShortName().equals(metadata))
				{
					el = doc.createElement(bean.getTypeBean().getShortName()+":"+bean.getMetadataBean().getShortName());
					type = bean.getTypeBean().getShortName();
					metadata = bean.getMetadataBean().getShortName();
					metadataElement.appendChild(el);
				}
				
				el.setAttribute(bean.getFieldBean().getShortName(), "");
			}
		}
		return;
	}
}
