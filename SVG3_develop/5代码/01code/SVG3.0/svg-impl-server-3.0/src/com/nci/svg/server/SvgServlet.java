/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�Servlet��ͨѶ�����
 *
 */
package com.nci.svg.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;
import com.nci.svg.server.util.Global;

/**
 * @author yx.nci
 * 
 */
public class SvgServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -533775564997626705L;
	
	private static ServerModuleController mainController = null;
//    private static final String CONTENT_TYPE = "text/html; charset=gbk";
//    private static final String REQUEST_ENCODING = "ISO-8859-1";
	/**
	 * ������־����
	 */
	private LoggerAdapter log;
	
	private static boolean bInit = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse) ͨѶ����
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse) ͨѶ����
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		System.out.println("mouduleType='"+req.getParameter("moduleType"));
//        resp.setContentType(CONTENT_TYPE);
//        req.setCharacterEncoding(REQUEST_ENCODING);
		ServiceShuntAdapter shunt = mainController.getServiceShunt();
		if(shunt == null)
			System.out.println("shunt is null");
		ResultBean bean = shunt.shuntService(req);
		
		try {
			if(bean.getReturnFlag()== ResultBean.RETURN_STRING){
				// ��Ҫ�������ؽ�����е��ַ�������
				resp.setContentType("txt/xml; charset=utf-8");
//				resp.setCharacterEncoding("utf-8");
//				System.out.println(bean.getReturnObj());
				resp.getWriter().write((String)bean.getReturnObj());
			}else{
				OutputStream os = null;
				os = resp.getOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(os);
				oos.writeObject(bean);
				oos.close();
			}			
		} catch (Exception ex) {
			log.log(null, LoggerAdapter.ERROR, ex);
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 *      Servlet��ʼ������,����������ڴ˴���ʵ����,���ڴ˵���������������и�����ļ���
	 */
	public void init() throws ServletException {
		if(bInit)
			return;
		bInit = true;
		URL url = getClass().getResource("");
		Global.appRoot = new File(url.getFile()).getParentFile()
				.getParentFile().getParentFile().getParentFile()
				.getParentFile().getParentFile().getAbsolutePath();
		// ʵ�������������
		mainController = new ServerModuleController(null);

		if (mainController == null) {
			System.out.println("�������������ʧ��");
			return;
		}
		int ret = mainController.init();
		if (ret == ServerModuleController.MODULE_INITIALIZE_FAILED) {
			System.out.println("�����������ʼ��ʧ��");
			return;
		}
		// ��ȡ��־����
		log = mainController.getLogger();
		if (log == null) {
			System.out.println("�޷������־���");
		}
		mainController.getLogger().log(null, LoggerAdapter.DEBUG,
				"Global.appRoot=" + Global.appRoot);
		// ��ʼ�����������������
		// ��ӡ���
		ret = mainController.start();
		if (ret == ServerModuleController.MODULE_START_COMPLETE) {
			if (log != null)
				log.log(null, LoggerAdapter.DEBUG, "��������������ɹ�");
			else
				System.out.println("��������������ɹ�");
		} else if (ret == ServerModuleController.MODULE_START_FAILED) {
			if (log != null)
				log.log(null, LoggerAdapter.ERROR, "�������������ʧ��");
			else
				System.out.println("�������������ʧ��");
		}
		super.init();
	}


}
