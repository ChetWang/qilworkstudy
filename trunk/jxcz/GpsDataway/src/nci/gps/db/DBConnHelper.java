package nci.gps.db;

import java.sql.Connection;

import nci.gps.util.gpsGlobal;


public class DBConnHelper {
	private static Connection con = null;
	private static ConnectionPool cp;
	private static int connectionPoolSize=1;//初始化时，在连接池中建立连接对象的数目
	private static int connectionPoolMaxSize=100;//连接池中至多能建立的连接对象的数目
	private static int connectionMaxUseCount=20;//物理连接至多能被使用的次数
	
	/**
	 * @function 创建连接池
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
	 * @function 获取连接
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
	 * @function 释放连接
	 * @author
	 * @date
	 */
	public static void realse(Connection connection){
		cp.realse(connection);
	}
	/**
	 * @function 释放所有的连接，清空连接池
	 * @author
	 * @date
	 */
	public static void finalizeConn(){
		cp.finalize();
	}
}
