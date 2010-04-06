package com.nci.svg.server.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.sdk.server.service.ServiceInfoBean;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�Servlet��ҵ�����·��ʵ����
 * 
 */
public class ServiceShuntImpl extends ServiceShuntAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 701401696505158466L;

	/**
	 * �������������
	 */
	public final static String REQUEST_TYPE_NAME = "action";

	private HashMap mapService = null;
	/**
	 * ����ʧ��
	 */
	private final static int OPER_ERROR = -1;
	/**
	 * �����ɹ�
	 */
	private final static int OPER_SUCCESS = 0;
	/**
	 * ������־����
	 */
	private static LoggerAdapter log;

	public byte[] lock = new byte[0];

	/**
	 * ���ɺ���
	 * 
	 * @param parameters
	 */
	public ServiceShuntImpl(HashMap parameters) {
		super(parameters);
	}

	/**
	 * ������Ĳ���ת����Servlet��ʽ
	 */
	public ResultBean shuntService(Object request) {
		return shuntService((HttpServletRequest) request);

	}

	/**
	 * ҵ��·�ɴ������ݴ�������������ȡ��Ӧ������ţ��ַ�����ҵ��������
	 * 
	 * @param request�������
	 * @param response�����ذ�
	 */
	public ResultBean shuntService(HttpServletRequest request) {
		String action = request.getParameter(REQUEST_TYPE_NAME).toUpperCase();
		ServiceInfoBean bean = (ServiceInfoBean) mapService.get(action.toUpperCase());
		ResultBean resultBean = null;
		try {

			// �����ڸ÷���ţ���ֱ�ӷ���
			if (bean == null || bean.isStartFlag() == false) {
				resultBean = new ResultBean();
				resultBean.setReturnFlag(OPER_ERROR);
				resultBean.setErrorText("�÷�����ʱδ�ṩ");
				return resultBean;
			}

			// ���������ṩ��ʵ���������ɴ���
			OperationServiceModuleAdapter service = (OperationServiceModuleAdapter) ServerUtilities
					.startModule(bean.getClassName(), parameters);
			Map paramMap = request.getParameterMap();
			if (service != null) {
				synchronized (lock) {
					bean.increaseCount();
				}
				resultBean = service.handleOper(action, paramMap);
				synchronized (lock) {
					bean.decreaseCount();
				}
				if (resultBean == null) {
					resultBean = new ResultBean();
					resultBean.setReturnFlag(OPER_ERROR);
					resultBean.setErrorText("�÷���������");
				}
			} else {
				resultBean = new ResultBean();
				resultBean.setReturnFlag(OPER_ERROR);
				resultBean.setErrorText("�÷������ʵ������");
			}

			return resultBean;

		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
			e.printStackTrace();
			resultBean = null;
		}
		return resultBean;

	}

	public String getModuleID() {
		// TODO Auto-generated method stub
		return null;
	}

	public int init() {
		// ��ȡ�������������
		controller = (ServerModuleControllerAdapter) getParameter(ServerModuleControllerAdapter.class
				.toString());
		// ��ȡ��־��������
		log = controller.getLogger();
		// ��ȡҵ������嵥
		int ret = getServiceList();
		if (ret < 0) {
			log.log(this, LoggerAdapter.ERROR, "��ȡҵ�����ʧ��");
			return MODULE_INITIALIZE_COMPLETE;
		}
		return super.init();
	}

	public int start() {
		// У����Ч����������������еķ�����λΪ����״̬
		Iterator iterator = mapService.values().iterator();
		while (iterator.hasNext()) {
			ServiceInfoBean bean = (ServiceInfoBean) iterator.next();
			bean.setStartFlag(true);
		}
		return super.start();
	}

	/**
	 * ��ȡҵ������嵥
	 * 
	 * @return���ɹ�����0��ʧ�ܷ���<0
	 */
	protected int getServiceList() {
		mapService = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.ml_shortname, b.ms_service_name,").append(
				" b.ms_service_class, b.ms_validity").append(
				" FROM t_svg_module_list a,t_svg_module_service b ").append(
				"WHERE a.ml_shortname = b.ms_short_name AND").append(
				" b.ms_validity = '1' AND ml_location ='2'").append(
				" AND b.ms_buss_type is null");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡ�����б�");
			return OPER_ERROR;
		}
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ServiceInfoBean bean = new ServiceInfoBean();
				bean.setShortName(rs.getString("ml_shortname"));
				bean.setServiceName(rs.getString("ms_service_name"));
				bean.setClassName(rs.getString("ms_service_class"));

				String validity = (String) rs.getString("ms_validity");
				if (validity.equals("1")) {
					bean.setStartFlag(true);
				} else {
					bean.setStartFlag(false);
				}
				mapService.put(rs.getString("ms_service_name").toUpperCase(),
						bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		} finally {
			controller.getDBManager().close(conn);
		}
		if (mapService.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	public ResultBean serviceMultiplexing(String action, Map requestParams) {
		ServiceInfoBean bean = (ServiceInfoBean) mapService.get(action.toUpperCase());
		if (bean == null)
			return null;
		try {
			// ���������ṩ��ʵ���������ɴ���
			OperationServiceModuleAdapter service = (OperationServiceModuleAdapter) ServerUtilities
					.startModule(bean.getClassName(), parameters);
			return service.handleOper(action, requestParams);

		} catch (Exception e) {
			log.log(this, LoggerAdapter.ERROR, e);
		}
		return null;
	}

	public ResultBean getServiceStatus(String serviceType, String name) {
		// TODO Auto-generated method stub
		HashMap map = new HashMap();
		ResultBean bean = null;
		// ��ȡ����action����״̬
		if (serviceType.equals(SERVICE_ACTION)) {
			ServiceInfoBean sbean = (ServiceInfoBean) mapService.get(name);
			if (sbean == null) {
				bean = new ResultBean(ResultBean.RETURN_ERROR, "�޴˷���", null, null);
			} else {
				map.put(name, sbean.getStartFlag());
				bean = new ResultBean(ResultBean.RETURN_SUCCESS, null, "HashMap", map);
			}
		} else if (serviceType.equals(SERVICE_MODULE)) {
			// ��ȡһ�����ɷ������״̬
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				ServiceInfoBean sbean = (ServiceInfoBean) iterator.next();
				if (sbean.getShortName().equals(name)) {
					map.put(sbean.getServiceName(), sbean.getStartFlag());
				}
			}
			bean = new ResultBean(OPER_SUCCESS, null, "HashMap", map);
		} else {
			// ��ȡ���з����״̬
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				ServiceInfoBean sbean = (ServiceInfoBean) iterator.next();
				map.put(sbean.getServiceName(), sbean.getStartFlag());
			}
			bean = new ResultBean(OPER_SUCCESS, null, "HashMap", map);
		}
		return bean;
	}

	public ResultBean handleService(String handleType, String serviceType,
			String name) {
		int ret = OPER_SUCCESS;
		ResultBean bean = null;
		if (handleType.equals(HANDLE_START)) {
			ret = handleServiceStatus(serviceType, name, true);
		} else if (handleType.equals(HANDLE_STOP)) {
			ret = handleServiceStatus(serviceType, name, false);
		} else if (handleType.equals(HANDLE_ADD)) {
			ret = addService(serviceType, name);
		} else if (handleType.equals(HANDLE_MODIFY)) {
			ret = modifyService(serviceType, name);
		} else if (handleType.equals(HANDLE_DELETE)) {
			ret = deleteService(serviceType, name);
		}
		bean = new ResultBean(ret, null, null, null);
		return bean;
	}

	/**
	 * ���ݴ���Ĳ��������÷���״̬
	 * 
	 * @param serviceType���������ͣ�SERVICE_MODULE��SERVICE_ACTION��SERVICE_ALL
	 * @param name����Ӧ������
	 * @param flag��true��ʾ������false��ʾֹͣ
	 * @return�����ý��
	 */
	public int handleServiceStatus(String serviceType, String name, boolean flag) {
		ServiceInfoBean sbean = null;
		if (serviceType.equals(SERVICE_ACTION)) {
			sbean = (ServiceInfoBean) mapService.get(name);
			if (sbean == null)
				return OPER_ERROR;
			else
				sbean.setStartFlag(flag);
		} else if (serviceType.equals(SERVICE_MODULE)) {
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				sbean = (ServiceInfoBean) iterator.next();
				if (sbean.getShortName().equals(name)) {
					sbean.setStartFlag(flag);
				}
			}
		} else if (serviceType.equals(SERVICE_ALL)) {
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				sbean = (ServiceInfoBean) iterator.next();
				sbean.setStartFlag(flag);
			}
		}
		return OPER_SUCCESS;
	}

	/**
	 * ���ӷ���
	 * 
	 * @param serviceType���������ͣ�SERVICE_MODULE��SERVICE_ACTION��SERVICE_ALL
	 * @param name����Ӧ������
	 * @return�����ӽ��
	 */
	private int addService(String serviceType, String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.ml_shortname, b.ms_service_name,").append(
				" b.ms_service_class").append(
				" FROM t_svg_module_list a,t_svg_module_service b ").append(
				"WHERE a.ml_shortname = b.ms_short_name AND").append(
				" b.ms_validity = '1' AND ml_location ='2'").append(
				" AND b.ms_buss_type is null ");
		if (serviceType.equals(SERVICE_MODULE)) {
			sql.append("AND b.ms_short_name = '").append(name).append("'");
		} else if (serviceType.equals(SERVICE_ACTION)) {
			sql.append("AND b.ms_service_name = '").append(name).append("'");
		} else {
			return OPER_ERROR;
		}

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		try {

			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡ�����б�");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ServiceInfoBean bean = new ServiceInfoBean();
				bean.setShortName(rs.getString("ml_shortname"));
				bean.setServiceName(rs.getString("ms_service_name"));
				bean.setClassName(rs.getString("ms_service_class"));
				bean.setStartFlag(false);
				mapService.put(rs.getString("ms_service_name").toUpperCase(),
						bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		} finally {
			controller.getDBManager().close(conn);
		}
		handleServiceStatus(serviceType, name, true);
		return OPER_SUCCESS;
	}

	/**
	 * ά������
	 * 
	 * @param serviceType���������ͣ�SERVICE_MODULE��SERVICE_ACTION��SERVICE_ALL
	 * @param name����Ӧ������
	 * @return��ά�����
	 */
	private int modifyService(String serviceType, String name) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.ml_shortname, b.ms_service_name,").append(
				" b.ms_service_class").append(
				" FROM t_svg_module_list a,t_svg_module_service b ").append(
				"WHERE a.ml_shortname = b.ms_short_name AND").append(
				" b.ms_validity = '1' AND ml_location ='2'").append(
				" AND b.ms_buss_type is null ");
		if (serviceType.equals(SERVICE_MODULE)) {
			sql.append("AND b.ms_short_name = '").append(name).append("'");
		} else if (serviceType.equals(SERVICE_ACTION)) {
			sql.append("AND b.ms_service_name = '").append(name).append("'");
		} else {
			return OPER_ERROR;
		}
		handleServiceStatus(serviceType, name, false);
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		try {

			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡ�����б�");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ServiceInfoBean bean = new ServiceInfoBean();
				bean.setShortName(rs.getString("ml_shortname"));
				bean.setServiceName(rs.getString("ms_service_name"));
				bean.setClassName(rs.getString("ms_service_class"));
				bean.setStartFlag(false);
				mapService.put(rs.getString("ms_service_name").toUpperCase(),
						bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			// e.printStackTrace();
			return OPER_ERROR;
		} finally {
			controller.getDBManager().close(conn);
		}
		handleServiceStatus(serviceType, name, true);
		return OPER_SUCCESS;
	}

	/**
	 * ɾ������
	 * 
	 * @param serviceType���������ͣ�SERVICE_MODULE��SERVICE_ACTION��SERVICE_ALL
	 * @param name����Ӧ������
	 * @return��ɾ�����
	 */
	private int deleteService(String serviceType, String name) {
		handleServiceStatus(serviceType, name, false);
		ServiceInfoBean sbean = null;
		if (serviceType.equals(SERVICE_ACTION)) {
			sbean = (ServiceInfoBean) mapService.get(name);
			if (sbean == null)
				return OPER_ERROR;
			else
				mapService.remove(name);
		} else if (serviceType.equals(SERVICE_MODULE)) {
			Iterator iterator = mapService.values().iterator();
			while (iterator.hasNext()) {
				sbean = (ServiceInfoBean) iterator.next();
				if (sbean.getShortName().equals(name)) {
					// FIXME:Ŀǰȱ���޵����ж�
					mapService.remove(sbean.getServiceName());
				}
			}
		} else {
			return OPER_ERROR;
		}
		return OPER_SUCCESS;
	}

}
