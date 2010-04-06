/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author ZHOUHM
 * @ʱ�䣺2008-09-28
 * @���ܣ����ݿ����ӳػ����࣬�����������ݿ����ӳػ�����Ϣ
 *
 */
package com.nci.svg.server.bean;

public class ConnBean {
	private String name;	// ���ӳ�����
	private String dbType;    //���ݿ�����
	private String username;	// ���ݿ��¼�û���
	private String password;	// ���ݿ��¼����
	private String jdbcurl;		// ���ݿ��ַ
	private int max;		// ���ӳ����������
	private long wait;		// ���ӳ���ȴ�ʱ��
	private String driver;	// ���ݿ����ӳ�����������
	
	/**
	 * �����ӳ�����
	 * @return String ���ӳ�����
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * �������ӳ�����
	 * @param name String ���ӳ�����
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * ��ȡ���ӳص�¼�û���
	 * @return String �û���
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * �������ӳص�¼�û���
	 * @param username String ��¼�û���
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * ��ȡ���ӳص�¼����
	 * @return String ��¼����
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * �������ӳص�¼����
	 * @param password String ��¼����
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * ��ȡ���ӳ����ӵ�ַ
	 * @return String ���ӵ�ַ
	 */
	public String getJdbcurl() {
		return jdbcurl;
	}
	
	/**
	 * �������ӳ����ӵ�ַ
	 * @param jdbcurl String ���ӵ�ַ
	 */
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	
	/**
	 * ��ȡ���ӳ����������
	 * @return integer ���������
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * �������ӳ����������
	 * @param max integer ���������
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * ��ȡ���ӳصȴ�ʱ��
	 * @return long �ȴ�ʱ��
	 */
	public long getWait() {
		return wait;
	}
	
	/**
	 * �������ӳصȴ�ʱ��
	 * @param wait long �ȴ�ʱ��
	 */
	public void setWait(long wait) {
		this.wait = wait;
	}
	
	/**
	 * ��ȡ���ӳ���������
	 * @return String ��������
	 */
	public String getDriver() {
		return driver;
	}
	
	/**
	 * �������ӳ���������
	 * @param driver String ��������
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * ��ȡ���ݿ�����
	 * @return String ���ݿ�����
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * �������ݿ�����
	 * @param dbType ���ݿ�����
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
