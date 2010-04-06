/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：Servlet版通讯组件类
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
	 * 操作日志对象
	 */
	private LoggerAdapter log;
	
	private static boolean bInit = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse) 通讯管理
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse) 通讯管理
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
				// 需要仅仅返回结果集中的字符串对象
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
	 *      Servlet初始化函数,主管理组件在此处被实例化,并在此调用主管理组件进行各组件的加载
	 */
	public void init() throws ServletException {
		if(bInit)
			return;
		bInit = true;
		URL url = getClass().getResource("");
		Global.appRoot = new File(url.getFile()).getParentFile()
				.getParentFile().getParentFile().getParentFile()
				.getParentFile().getParentFile().getAbsolutePath();
		// 实例化主管理组件
		mainController = new ServerModuleController(null);

		if (mainController == null) {
			System.out.println("创建主管理组件失败");
			return;
		}
		int ret = mainController.init();
		if (ret == ServerModuleController.MODULE_INITIALIZE_FAILED) {
			System.out.println("主管理组件初始化失败");
			return;
		}
		// 获取日志对象
		log = mainController.getLogger();
		if (log == null) {
			System.out.println("无法获得日志组件");
		}
		mainController.getLogger().log(null, LoggerAdapter.DEBUG,
				"Global.appRoot=" + Global.appRoot);
		// 初始化并启动主管理组件
		// 打印结果
		ret = mainController.start();
		if (ret == ServerModuleController.MODULE_START_COMPLETE) {
			if (log != null)
				log.log(null, LoggerAdapter.DEBUG, "主管理组件启动成功");
			else
				System.out.println("主管理组件启动成功");
		} else if (ret == ServerModuleController.MODULE_START_FAILED) {
			if (log != null)
				log.log(null, LoggerAdapter.ERROR, "主管理组件启动失败");
			else
				System.out.println("主管理组件启动失败");
		}
		super.init();
	}


}
