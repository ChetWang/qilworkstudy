package com.nci.svg.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.server.util.Global;

public class MainServletTest extends HttpServlet {
	private static final long serialVersionUID = -3937397938899252529L;
	private HashMap actions;
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";
	private static final String REQUEST_ENCODING = "ISO-8859-1";

	public static ServerModuleController mainController = null;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		req.setCharacterEncoding(REQUEST_ENCODING);

		PrintWriter out = new PrintWriter(System.out);
		out = resp.getWriter();

		HashMap parameters = new HashMap();
		parameters.put(ServerModuleControllerAdapter.class.toString(),
				mainController);
		String action = req.getParameter("action").toUpperCase();

		Integer index = (Integer) actions.get(action);
		if (index != null) {
			switch (index.intValue()) {
			case 1: {
//				CodeSearch cs = new CodeSearch(parameters);
//				ResultBean rb = cs.handleOper("",req.getParameterMap());
//				System.out.println("return flag:" + rb.getReturnFlag());
//				out.println("return flag:" + rb.getReturnFlag());
			}
				break;
			case 2: 
			case 3: {
//				ClientUpgrade cu = new ClientUpgrade(parameters);
//				ResultBean rb = cu.handleOper("",req.getParameterMap());
//				System.out.println("return flag:" + rb.getReturnFlag());
//				out.println("return flag:" + rb.getReturnFlag());
			}
				break;
			case 4:
			case 5:
			case 6:
//				BusinessModuleSearch bms = new BusinessModuleSearch(parameters);
//				ResultBean rb = bms.handleOper("",req.getParameterMap());
//				System.out.println("return flag:" + rb.getReturnFlag());
//				out.println("return flag:" + rb.getReturnFlag());
				break;
			default:
				break;
			}
		}

		out.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 *      Servlet初始化函数,主管理组件在此处被实例化,并在此调用主管理组件进行各组件的加载
	 */
	public void init() throws ServletException {
		Global.appRoot = getServletContext().getRealPath("/");
		// 实例化主管理组件
		mainController = new ServerModuleController(null);
		if (mainController == null) {
			System.out.println("创建主管理组件失败");
			return;
		}
		int ret = mainController.init();
		if (ret == mainController.MODULE_INITIALIZE_FAILED) {
			System.out.println("主管理组件初始化失败");
			return;
		}

		// 设置请求命令
		actions = new HashMap();
		actions.put("getSvgCodes".toUpperCase(), new Integer(1));
		actions.put("getUpgradeModule".toUpperCase(), new Integer(2));
		actions.put("downloadUpgradeModule".toUpperCase(), new Integer(3));
		actions.put("getModuleList".toUpperCase(), new Integer(4));
		actions.put("getModuleParams".toUpperCase(), new Integer(5));
		actions.put("getModuleActions".toUpperCase(), new Integer(6));

		super.init();
	}

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

}
