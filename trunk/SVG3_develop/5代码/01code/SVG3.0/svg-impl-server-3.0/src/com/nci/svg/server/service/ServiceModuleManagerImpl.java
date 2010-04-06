package com.nci.svg.server.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.ServerModuleControllerAdapter;
import com.nci.svg.sdk.server.database.DBSqlIdiomAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.sdk.server.service.ServiceModuleManagerAdapter;
import com.nci.svg.sdk.server.service.ServiceShuntAdapter;
import com.nci.svg.sdk.server.util.ServerUtilities;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�ҵ���������������ʵ����
 * 
 */
public class ServiceModuleManagerImpl extends ServiceModuleManagerAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8616941127521330960L;
	public static final String moduleID = "20fe40c7-f0ea-4d7f-9fd6-99da65bfa00d";
	private HashMap mapModuleInfo = null;
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

	public ServiceModuleManagerImpl(HashMap parameters) {
		super(parameters);
	}

	public String getModuleID() {
		return moduleID;
	}

	public int init() {
		// ��ʼ���������������е�ҵ���������嵥������
		int ret = 0;
		try
		{
			String key = ServerModuleControllerAdapter.class
			.toString();
			System.out.println(key);
			Object obj = getParameter(key);
			System.out.println(obj);
		controller = (com.nci.svg.sdk.server.ServerModuleControllerAdapter)obj ;
		// ��ȡ��־����
		log = controller.getLogger();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return MODULE_INITIALIZE_FAILED;
		}
		// ��ȡ����嵥
		ret = getModuleList();
		if (ret < 0) {
			log.log(this, LoggerAdapter.ERROR, "��ȡҵ���������嵥ʧ�ܣ�");
		}

		// ��������嵥��һ���������������
		if (mapModuleInfo.size() == 0) {
			log.log(this, LoggerAdapter.INFO, "����嵥Ϊ�գ�����ʧ�ܣ�");
			return MODULE_INITIALIZE_COMPLETE;
		}

		// ��ȡ���ش洢·����Ϣ
		String pathName = controller.getCachePath();
		Iterator iterator = mapModuleInfo.values().iterator();
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			ret = downloadModuleJar(bean, pathName);
		}
		return super.init();
	}

	/**
	 * ��ȡҵ���������嵥
	 * 
	 * @return���ɹ�����0��ʧ�ܷ���С��0
	 */
	private int getModuleList() {
		mapModuleInfo = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_shortname,ml_name,ml_kind,ml_type,ml_edition,")
				.append(
						"ml_log_level,ml_log_type,ml_class_name,ml_edition,ml_location FROM t_svg_module_list a ")
				.append(
						"WHERE ml_location ='2' AND ml_kind = '2' AND ml_type = '18'");

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡҵ���������嵥��");
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
				String shortname = rs.getString("ml_shortname");
				mapModuleInfo.put(shortname, bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			controller.getDBManager().close(conn);
			log.log(this, LoggerAdapter.ERROR, e);
			return OPER_ERROR;
		} finally {
			// �ر����ݿ�����
			controller.getDBManager().close(conn);
		}
		if (mapModuleInfo.size() == 0)
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	/**
	 * ���ݴ���������Ϣ�����ݿ����ӣ������ݿ���������ܰ������أ���������jvm��
	 * 
	 * @param bean�������Ϣ
	 * @param pathName���洢·��
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
				return -1;
			// ��ȡ���ݿ�����
			Connection conn = controller.getDBManager().getConnection("svg");
			// ��ȡ���������
			byte[] content = sqlIdiom.getBlob(conn, sql.toString(),
					"ml_content");
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					log.log(this, LoggerAdapter.ERROR, "���ط�������������ر����ݿ����ӳ���");
					log.log(this, LoggerAdapter.ERROR, e1);
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
					log.log(this, LoggerAdapter.ERROR, e);
					// e.printStackTrace();
				}
				return OPER_SUCCESS;
			}
		}

		return OPER_ERROR;
	}

	public int start() {
		// �������е�ҵ��������
		Iterator iterator = mapModuleInfo.values().iterator();
		String className = null;
		while (iterator.hasNext()) {
			ModuleInfoBean bean = (ModuleInfoBean) iterator.next();
			className = bean.getClassName();
			if (className == null || className.length() == 0)
				continue;

			OperationServiceModuleAdapter module = (OperationServiceModuleAdapter) ServerUtilities
					.startModule(className, parameters);
			if (module != null) {
				module.init();
			}
		}
		return super.start();
	}

	public ResultBean configModule(String operType, String shortName) {
		ResultBean bean = null;
		if (operType.equals(OPER_ADD)) {
			bean = addModule(shortName);
		} else if (operType.equals(OPER_MODIFY)) {
			bean = modifyModule(shortName);
		} else if (operType.equals(OPER_DELETE)) {
			
			bean = deleteModule(shortName);
		}
		return bean;
	}
	
	/**
	 * �������
	 * @param shortName���������
	 * @return�����������
	 */
	private ResultBean addModule(String shortName)
	{
		int ret = OPER_ERROR;
		ModuleInfoBean bean = getModuleBean(shortName);
		String pathName = controller.getCachePath();
		if(bean != null)
		{
			ret = downloadModuleJar(bean,pathName);
		}
		return new ResultBean(ret,"",null,null);
	}
	
	/**
	 * ά�����
	 * @param shortName���������
	 * @return��ά�������
	 */
	private ResultBean modifyModule(String shortName)
	{
		int ret = OPER_ERROR;
		ModuleInfoBean newBean = getModuleBean(shortName);
		ModuleInfoBean oldBean = (ModuleInfoBean)mapModuleInfo.get(shortName);
		if(newBean != null || oldBean != null)
		{
			if(newBean.getEdition().equals(oldBean.getEdition()))
			{
				//����汾û�б仯����Ϊֻ�ı��˲�������
				mapModuleInfo.put(shortName, newBean);
				ret = OPER_SUCCESS;
			}
			else
			{
				String pathName = controller.getCachePath();
				controller.getServiceShunt().handleService(
				    ServiceShuntAdapter.HANDLE_DELETE,
				    ServiceShuntAdapter.SERVICE_MODULE, shortName);
				String jarName = pathName + oldBean.getModuleShortName()
				+ oldBean.getEdition() + ".jar";
				//ɾ���ϰ汾��jar�ļ�
				boolean flag = new File(jarName).delete();
				//������jar�ļ�
				downloadModuleJar(newBean,pathName);
				
				mapModuleInfo.put(shortName, newBean);
				
				controller.getServiceShunt().handleService(
					    ServiceShuntAdapter.HANDLE_ADD,
					    ServiceShuntAdapter.SERVICE_MODULE, shortName);
				ret = OPER_SUCCESS;
			}
		}
		return new ResultBean(ret,"",null,null) ;
	}
	
	/**
	 * ɾ�����
	 * @param shortName���������
	 * @return��ɾ�������
	 */
	private ResultBean deleteModule(String shortName)
	{
		return controller.getServiceShunt().handleService(
				ServiceShuntAdapter.HANDLE_DELETE,
				ServiceShuntAdapter.SERVICE_MODULE, shortName);
	}

	/**
	 * �������������ȡ�����Ϣbean
	 * 
	 * @param shortName:�������
	 * @return���ɹ����������Ϣbean��ʧ�ܷ���null
	 */
	private ModuleInfoBean getModuleBean(String shortName) {
		ModuleInfoBean bean = new ModuleInfoBean();
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"SELECT ml_shortname,ml_name,ml_kind,ml_type,ml_edition,")
				.append(
						"ml_log_level,ml_log_type,ml_class_name,ml_edition,ml_location FROM t_svg_module_list a ")
				.append("WHERE ml_shortname = '").append(shortName).append("'");

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		try {
			if (conn == null) {
				log.log(this, LoggerAdapter.ERROR, "�޷���ȡ���ݿ����ӣ��޷���ȡҵ���������嵥��");
				return null;
			}

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				bean.setModuleShortName(rs.getString("ml_shortname"));
				bean.setName(rs.getString("ml_name"));
				bean.setLocation(rs.getString("ml_location"));
				bean.setKind(rs.getString("ml_kind"));
				bean.setType(rs.getString("ml_type"));
				bean.setLogLevel(rs.getString("ml_log_level"));
				bean.setLogType(rs.getString("ml_log_type"));
				bean.setClassName(rs.getString("ml_class_name"));
				bean.setEdition(rs.getString("ml_edition"));
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return null;
		} finally {
			// �ر����ݿ�����
			controller.getDBManager().close(conn);
		}

		return bean;
	}

}
