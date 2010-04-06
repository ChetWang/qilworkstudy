package nci.gps.db;

import java.sql.Connection;

import nci.gps.util.gpsGlobal;


public class DBConnHelper {
	private static Connection con = null;
	private static ConnectionPool cp;
	private static int connectionPoolSize=1;//��ʼ��ʱ�������ӳ��н������Ӷ������Ŀ
	private static int connectionPoolMaxSize=100;//���ӳ��������ܽ��������Ӷ������Ŀ
	private static int connectionMaxUseCount=20;//�������������ܱ�ʹ�õĴ���
	
	/**
	 * @function �������ӳ�
	 * @author
	 * @date
	 */
	public static void init(){
		 if (cp == null) {
			String url = gpsGlobal.DB_URL;
			String user = gpsGlobal.DB_USER;
			String password = gpsGlobal.DB_PASSWORD;
			String driver = gpsGlobal.DB_DRIVER;
			cp = new ConnectionPool(driver, url, user, password,
					connectionPoolSize, connectionPoolMaxSize,
					connectionMaxUseCount);
		}   
	}
	/**
	 * @function ��ȡ����
	 * @author
	 * @date
	 */
	public static Connection getConn() {
		try {
            //Class.forName(driver);
            //con=DriverManager.getConnection(url, user, password);
        	init();
        	con = cp.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;

	}
	/**
	 * @function �ͷ�����
	 * @author
	 * @date
	 */
	public static void realse(Connection connection){
		cp.realse(connection);
	}
	/**
	 * @function �ͷ����е����ӣ�������ӳ�
	 * @author
	 * @date
	 */
	public static void finalizeConn(){
		cp.finalize();
	}
}
