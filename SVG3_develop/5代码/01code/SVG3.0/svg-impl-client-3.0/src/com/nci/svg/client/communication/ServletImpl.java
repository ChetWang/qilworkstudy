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
 * �ͻ���servletͨѶ���
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
	 * ����ServletImpl����
	 * 
	 * @param editor
	 *            �༭���������������
	 * 
	 */
	public ServletImpl(ModuleControllerAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
		// TODO ���캯����������
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
		// TODO ʵ��ServletImplͨѶ�����ҵ���߼�����
		return null;
	}

	/**
	 * ͨ�������url,�������������,���������ӽ��,�������ӱ�ʶ
	 * 
	 * @param url
	 *            servletָ����url
	 * @return Servlet��ص�URLConnection����
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
	 * �������ServletͨѶ�����������������Ŀ�������
	 * 
	 * @param bean
	 *            servletͨѶ����
	 * @return Servlet��ص�URLConnection����
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
				// ����ҵ��ϵͳ���
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
	 * ���ݴ�������Ӷ��󣬻�ȡ������Ϣ
	 * 
	 * @param conn
	 *            URLConnection����
	 * @return ����˴���Ľ������
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
	 * �������У��
	 * 
	 * @return ������
	 */
	public int errorVerify() {
		// TODO ʵ���������У��
		return 0;
	}

	/**
	 * ���������Ϣ��ȡ.���ݴ���Ĵ����룬Ѱ�Ҳ�������Ӧ�Ĵ����ı���Ϣ
	 * 
	 * @param errorCode
	 * @return ������Ϣ����
	 */
	public String getErrorText(int errorCode) {
		// TODO ʵ�ִ��������
		return null;
	}

	public boolean isKeepConnection() {
		return true;
	}

	protected void showConnFailedInfo(CommunicationBean bean) {
		((EditorAdapter) editor).getLogger().log(this, LoggerAdapter.FATAL,
				"ͨ��ģ���޷��������������Ч����, ������Ч��" + bean.toString());
	}
}
