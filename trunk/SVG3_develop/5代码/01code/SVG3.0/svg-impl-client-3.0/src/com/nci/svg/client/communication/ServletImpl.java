package com.nci.svg.client.communication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.communication.CommunicationAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.ModuleControllerAdapter;

/**
 * 客户端servlet通讯组件
 * 
 * @author Qil.Wong
 * 
 */
public class ServletImpl extends CommunicationAdapter {

	private static final long serialVersionUID = 1057178890586343621L;

	public final static String MODULE_ID = "c5f2d9d7-61ea-4f3b-ba7f-ba1362a3396f";

	private ResultBean checkResult = null;

	private CommunicationBean checkServletBean = null;

	protected String defaultURL = null;

	/**
	 * 构建ServletImpl对象
	 * 
	 * @param editor
	 *            编辑器主管理组件对象
	 * 
	 */
	public ServletImpl(ModuleControllerAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
		// TODO 构造函数其他功能
	}

	@Override
	public synchronized int checkStatus() {
		try {
			if (checkServletBean == null) {
				checkServletBean = new CommunicationBean(
						ActionNames.CHECK_URL_CONN, null);
			}
			connectFlag = COMMUNICATION_VALID;
			checkResult = this.communicate(checkServletBean);
			if (checkResult != null)
				return COMMUNICATION_VALID;
			else
				return COMMUNICATION_INVALID;
		} finally {
			checkResult = null;
		}
	}

	@Override
	public synchronized ResultBean communicate(CommunicationBean cbean) {
		if (connectFlag == COMMUNICATION_VALID) {
			if (cbean != null) {
				URLConnection conn = sendToServer(cbean);
				if (conn != null)
					return recvFromServer(conn);
			}
		} else if (connectFlag == COMMUNICATION_INVALID) {
			showConnFailedInfo(cbean);
		}
		return null;
	}

	public Object handleOper(int index, Object obj) {
		// TODO 实现ServletImpl通讯组件的业务逻辑处理
		return null;
	}

	/**
	 * 通过传入的url,发起服务器连接,并返回连接结果,设置连接标识
	 * 
	 * @param url
	 *            servlet指定的url
	 * @return Servlet相关的URLConnection对象
	 */
	public URLConnection connectServer(String urlLocation) {
		URL url;
		try {
			url = new URL(urlLocation);
			URLConnection urlConn = url.openConnection();
			return urlConn;
		} catch (MalformedURLException e) {
			((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR,
					e);
		} catch (IOException e) {
			connectionFailed(e);
		}
		return null;
	}

	/**
	 * 将传入的Servlet通讯请求包解析并发送至目标服务器
	 * 
	 * @param bean
	 *            servlet通讯请求
	 * @return Servlet相关的URLConnection对象
	 */
	protected URLConnection sendToServer(CommunicationBean bean) {
		try {
			if (defaultURL == null) {
				defaultURL = (String) ((EditorAdapter) editor)
						.getGCParam(SysSetDefines.APPROOT);
			}
			URLConnection urlConn = connectServer(new StringBuffer().append(
					bean.getURL() == null ? defaultURL : bean.getURL()).append(
					"?").append(CommunicationBean.SERVLET_REQUEST_TYPE_NAME)
					.append("=").append(bean.getActionType()).toString());
			if (urlConn != null) {
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				urlConn.setReadTimeout(Constants.SERVLET_TIME_OUT);
				PrintStream output = new PrintStream(urlConn.getOutputStream());
				String[][] param = bean.getParams();
				StringBuffer rs = new StringBuffer();
				// 增加业务系统编号
				rs.append("&");
				rs.append(URLEncoder.encode(ActionParams.BUSINESS_ID, "UTF-8"))
						.append('=').append(
								URLEncoder.encode(((EditorAdapter) editor)
										.getBusiSysID(), "UTF-8"));
				rs.append("&");
				rs.append(URLEncoder.encode(ActionParams.OPERATOR, "UTF-8"))
						.append('=').append(
								URLEncoder.encode(((EditorAdapter) editor)
										.getSvgSession().getUser(), "UTF-8"));
				if (param != null && param.length > 0) {
					for (int i = 0; i < param.length; i++) {
						if (param[i][0] == null || param[i][1] == null)
							continue;
						rs.append("&");
						rs
								.append(URLEncoder.encode(param[i][0], "UTF-8"))
								.append('=')
								.append(URLEncoder.encode(param[i][1], "UTF-8"));
					}
					
				}
				output.print(rs.toString());
				output.flush();
			}
			return urlConn;
		} catch (IOException e) {
			connectionFailed(e);
		}
		return null;
	}

	/**
	 * 根据传入的连接对象，获取返回信息
	 * 
	 * @param conn
	 *            URLConnection对象
	 * @return 服务端处理的结果对象
	 */
	protected ResultBean recvFromServer(URLConnection conn) {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(conn
					.getInputStream()));
			return (ResultBean) ois.readObject();
		} catch (IOException e) {
			connectionFailed(e);
		} catch (ClassNotFoundException e) {
			((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR,
					e);
		} catch (ClassCastException e) {
			((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.ERROR,
					e);
		}
		return null;
	}

	/**
	 * 网络错误校验
	 * 
	 * @return 错误码
	 */
	public int errorVerify() {
		// TODO 实现网络错误校验
		return 0;
	}

	/**
	 * 网络错误信息获取.根据传入的错误码，寻找并返回相应的错误文本信息
	 * 
	 * @param errorCode
	 * @return 错误信息描述
	 */
	public String getErrorText(int errorCode) {
		// TODO 实现错误码解析
		return null;
	}

	public boolean isKeepConnection() {
		return true;
	}

	protected void showConnFailedInfo(CommunicationBean bean) {
		((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.FATAL,
				"通信模块无法与服务器建立有效连接, 连接无效！" + bean.toString());
	}
}
