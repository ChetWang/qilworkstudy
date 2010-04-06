package com.nci.svg.server.operinterface;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceAdapter;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceManagerAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-25
 * @���ܣ�ҵ��ӿڹ�����
 * 
 */
public class OperInterfaceManagerImpl extends OperInterfaceManagerAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4602101711028754855L;
	private HashMap mapModuleInfo = null;
	/**
	 * ��־����
	 */
	private static LoggerAdapter log;
	
	/**
	 * ҵ��ӿڶ�Ӧ�嵥
	 */
	private HashMap mapInterface = null;
	
	private static final String SEP_NAME = "_$@nci@$_";

	public OperInterfaceManagerImpl(HashMap parameters) {
		super(parameters);
	}

	public int init() {
		// ��ȡҵ��ӿ�����嵥
		controller = (ServerModuleControllerAdapter) getParameter(ServerModuleControllerAdapter.class
				.toString());
		// ��ȡ��־��������
		log = controller.getLogger();
		// ��ȡҵ��ӿ�����嵥
		int ret = getOperInterfaceList();
		if (ret == OPER_ERROR) {
			log.log(this, LoggerAdapter.ERROR, "��ȡҵ��ӿ�����嵥ʧ��");
		}

		// ��������嵥��һ���������������
		if (mapModuleInfo.size() == 0) {
			log.log(this, LoggerAdapter.INFO, "����嵥Ϊ�գ�����ʧ��");
			return MODULE_INITIALIZE_COMPLETE;
		}

		// ��ȡ���ش洢·����Ϣ
		String pathName = controller.getCachePath();
		Iterator iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}
		
		ret = getOperInterfaceServices();
		if (ret == OPER_ERROR) {
			log.log(this, LoggerAdapter.ERROR, "��ȡҵ��ӿ��嵥ʧ��");
		}
		return super.init();
	}

	/**
	 * ���ݴ���������Ϣ�����ݿ����ӣ������ݿ���������ܰ������أ���������jvm��
	 * 
	 * @param bean�������Ϣ
	 * @param conn�����ݿ�����
	 * @return�����������ɹ�����0.ʧ�ܷ���<0
	 */
	protected int downloadModuleJar(ModuleInfoBean bean, String pathName) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_content FROM t_svg_module_list WHERE ml_shortname = '")
				.append(bean.getModuleShortName()).append("'");

		String type = controller.getDBManager().getDBType("SVG");
		if (type != null) {
			// ͨ������ת�����ȡblob�ֶ�
			DBSqlIdiomAdapter sqlIdiom = controller.getDBManager()
					.getDBSqlIdiom(type);
			if (sqlIdiom == null)
				return OPER_ERROR;
			// ��ȡ���ݿ�����
			Connection conn = controller.getDBManager().getConnection("svg");
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���������ܰ���");
				return OPER_ERROR;
			}
			// ��ȡ�������
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
					"ml_content");
			// �ر����ݿ�����
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.log(this, LoggerAdapter.ERROR, "��������ܰ����ر����ݿ�����ʧ�ܣ�");
					log.log(this, LoggerAdapter.ERROR, e);
					return OPER_ERROR;
				}
			}
			if (content != null) {
				String jarName = pathName + bean.getModuleShortName()
						+ bean.getEdition() + ".jar";
				try {
					FileOutputStream fos = new FileOutputStream(new File(
							jarName));
					fos.write(content, 0, content.length);
					fos.close();
					File localJarFile = new File(jarName);
					if (localJarFile.exists()) {
						controller.addClassPath(localJarFile);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return OPER_SUCCESS;
			}
		}
		return OPER_ERROR;
	}

	public OperInterfaceAdapter getOperationModule(String bussinessID, String actionName) {
		String className = getModuleClass(bussinessID, actionName);
		if (className == null || className.length() == 0) {
			log.log(this, LoggerAdapter.ERROR, "��ȡ��Ӧ��ҵ��ӿ�ʵ���಻���ڣ�");
			return null;
		}
		OperInterfaceAdapter operInterface = (OperInterfaceAdapter) ServerUtilities
		              .startModule(className, parameters);
		return operInterface;
	}
	
	/**
	 * ��ȡҵ��ӿ�ʵ������
	 * 
	 * @param bussinessID:ҵ��ϵͳ���
	 * @param actionName:����������
	 * @return
	 */
	protected String getModuleClass(String bussinessID, String actionName) {
		String name = bussinessID.toUpperCase() + SEP_NAME + actionName.toUpperCase();

		return (String)mapInterface.get(name);
	}

	/**
	 * ���ݴ�������ݿ����ӣ���ȡҵ��ӿ�����嵥
	 * 
	 * @return
	 */
	protected int getOperInterfaceList() {
		mapModuleInfo = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ml_shortname, ml_name,").append(
				" ml_kind, ml_type, ml_edition,").append(
				" ml_log_level, ml_log_type, ml_class_name,").append(
				"ml_edition,ml_location FROM t_svg_module_list a ").append(
				"WHERE ml_location ='2' AND ml_kind = '2'").append(
				" AND ml_type = '22'");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			
			if (conn == null) {
				System.out.println("�޷���ȡ���ݿ����ӣ��޷���ȡҵ��ӿ�����嵥��");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				ModuleInfoBean bean = new ModuleInfoBean();
				bean.setModuleShortName(rs.getString("ml_shortname"));
				bean.setName(rs.getString("ml_name"));
				bean.setLocation(rs.getString("ml_location"));
				bean.setKind(rs.getString("ml_kind"));
				bean.setType(rs.getString("ml_type"));
				bean.setLogLevel(rs.getString("ml_log_level"));
				bean.setLogType(rs.getString("ml_log_type"));
				bean.setClassName(rs.getString("ml_class_name"));
				bean.setEdition(rs.getString("ml_edition"));
				String name = rs.getString("ml_shortname");
				mapModuleInfo.put(name, bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return OPER_ERROR;
		}
		finally
		{
			controller.getDBManager().close(conn);
		}

		if (mapModuleInfo.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}
	
	/**
	 * ���ݴ�������ݿ����ӣ���ȡҵ��ӿ�����嵥
	 * 
	 * @return
	 */
	protected int getOperInterfaceServices() {
		mapInterface = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ms_service_class,ms_service_name,ms_buss_type FROM t_svg_module_service,t_svg_module_list").append(
		" WHERE ms_validity='1' AND ml_shortname= ms_short_name AND ml_location='2' ")
		.append("AND ml_kind='2' AND ml_type= '22' AND ms_buss_type is not null");
		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			
			if (conn == null) {
				System.out.println("�޷���ȡ���ݿ����ӣ��޷���ȡҵ��ӿ�����嵥��");
				return OPER_ERROR;
			}
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				String name = rs.getString("ms_service_name");
				String className = rs.getString("ms_service_class");
				String bussType = rs.getString("ms_buss_type");
				name = bussType.toUpperCase() + SEP_NAME +name.toUpperCase();
				mapInterface.put(name, className);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return OPER_ERROR;
		}
		finally
		{
			controller.getDBManager().close(conn);
		}

		return OPER_SUCCESS;
	}

	public ResultBean OperInterfaceMultiplexing(String bussinessID,
			String action, Map requestParams) {
		OperInterfaceAdapter operInterface = getOperationModule(bussinessID, action);
		if(operInterface == null)
		{
			ResultBean bean = new ResultBean(ResultBean.RETURN_ERROR,"�޷���ȡҵ��ӿ����",null,null);
			return bean;
		}
		return operInterface.handleOper(action, requestParams);
	}


}
