/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：TODO
 *
 */
package com.nci.svg.client.indunorm;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.sdk.bean.RelaIndunormDescBean;
import com.nci.svg.sdk.bean.RelaIndunormGraphBean;
import com.nci.svg.sdk.bean.RelaIndunormSymbolBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.module.IndunormModuleAdapter;

/**
 * @author yx.nci
 *
 */
public class IndunormManager extends IndunormModuleAdapter {

	public IndunormManager(EditorAdapter editor) {
		super(editor);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.IndunormModuleAdapter#getIndunormDescRela(java.lang.String)
	 */
	@Override
	public ArrayList<RelaIndunormDescBean> getIndunormDescRela(String graphType) {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.BUSINESS_TYPE;
		params[0][1] = graphType;
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_INDUNORM_DESC_RELA, params));
		if(bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return (ArrayList<RelaIndunormDescBean>)bean.getReturnObj();
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.IndunormModuleAdapter#getIndunormGraphRela(java.lang.String)
	 */
	@Override
	public ArrayList<RelaIndunormGraphBean> getIndunormGraphRela(String graphType) {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.BUSINESS_TYPE;
		params[0][1] = graphType;
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_INDUNORM_GRAPHTYPE_RELA, params));
		if(bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return (ArrayList<RelaIndunormGraphBean>)bean.getReturnObj();
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.IndunormModuleAdapter#getIndunormSymbolRela(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ArrayList<RelaIndunormSymbolBean> getIndunormSymbolRela(
			String graphType, String symbolType, String symbolID) {
		String[][] params = new String[3][2];
		params[0][0] = ActionParams.BUSINESS_TYPE;
		params[0][1] = graphType;
		params[1][0] = ActionParams.SYMBOL_TYPE;
		params[1][1] = symbolType;
		params[2][0] = ActionParams.SYMBOL_ID;
		params[2][1] = symbolID;
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_INDUNORM_FIELD_SYMBOL_RELA, params));
		if(bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return (ArrayList<RelaIndunormSymbolBean>)bean.getReturnObj();
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.module.IndunormModuleAdapter#getIndunormList()
	 */
	@Override
	public HashMap<String,SimpleIndunormBean> getIndunormList() {
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_SIMPLE_INDUNORM_LIST, null));
		if(bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return (HashMap<String,SimpleIndunormBean>)bean.getReturnObj();
	}

}
