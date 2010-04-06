package nci.gps.db;
import java.util.*;
import java.sql.*;

public class ConnectionPool {		

	private Vector pool=new Vector();//使用Vector保存所有的连接对象
	private int connectionPoolSize=1;//初始化时，在连接池中建立连接对象的数目
	private int connectionPoolMaxSize;//连接池中至多能建立的连接对象的数目
	private int connectionMaxUseCount;//物理连接至多能被使用的次数
	private String driver;//连接程序所使用的JDBC驱动程序名
	private String url;//连接所使用的URL
	private String userName;//连接所使用的用户名
	private String password;//连接所使用的密码
	
	public ConnectionPool(String driver, String url, String userName, 
			String password, int minConnection, int maxConnection, int connectionMaxUseCount){
		//初始化属性
		this.driver=driver;
		this.url=url;
		this.userName=userName;
		this.password=password;
		this.connectionPoolSize=minConnection;
		this.connectionPoolMaxSize=maxConnection;
		this.connectionMaxUseCount=connectionMaxUseCount;
		initPool(); //调用私有方法initPool()初始化连接池
	}	
	
	//方法initPool创建连接对象，并初始化连接池
	private void initPool(){
		try{//安装JDBC驱动
			Class.forName(driver);
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
			return;
		}
		//创建connectionPooISize个空闲的连接对象，并把它们加入	到连接池中去
		while(pool.size()<connectionPoolSize){
			ConnectionObject newConnection=new ConnectionObject(url,userName,password);
			if (newConnection.connection != null)
				pool.add(newConnection);
		}
	}
	
	//用户获取物理连接的接口
	public Connection getConnection(){
		if (pool==null)
			return null;
		
		//调用方法getFreeConnection获取连接
		ConnectionObject connectionObject= getFreeConnection();
		//若没有空闲连接并且连接池没有达到上限,创建一个新的连接
		if(connectionObject==null&&pool.size()<this.connectionPoolMaxSize){
			ConnectionObject co=new ConnectionObject(url, userName,password);
			if(co.connection==null)
				return null;
			pool.addElement(co);
			connectionObject=getFreeConnection();
		}
		System.out.println("PoolCount:"+pool.size());
		if(connectionObject!=null){
			try {
				if(connectionObject.connection.isClosed()){
//					this.realse(connectionObject.connection);
					pool.remove(connectionObject.connection);
					return getConnection();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//将连接对象的状态设置为忙,并把物理连接返回给用户使用
			connectionObject.isUse=true;
			connectionObject.useCount++;
			System.out.println("UserCount:"+connectionObject.useCount);
			return connectionObject.connection;
		}
		
		return null;
	}
	
	/**
	 * 从连接池获取可用连接，可以指定客户程序能够等待的最长时间
	 * @param timeout 以毫秒计的等待时间限制
	 */
	public Connection getConnection(long timeout){
		long startTime=new java.util.Date().getTime();
		Connection con;
		while((con=getConnection())== null){
			try{
				wait(timeout);
			}catch(InterruptedException e){}
			if((new java.util.Date().getTime()-startTime)>=timeout){
				return null;
			}
		}		
		return con;
	}
	
	//查找连接池,返回第一个空闲的连接,没有则返回null
	public ConnectionObject getFreeConnection(){
		if (pool == null)
			return null;
		for(int i=0;i<pool.size();i++){
			ConnectionObject co=(ConnectionObject) pool.elementAt(i);
			if(co.connection !=null && co.isUse==false) return co;
		}
		return null;
	}
	
	//用户释放连接的接口
	public void realse(Connection connection){
		int index=findConnectiOn(connection);
		if(index==-1) return;
		
		ConnectionObject co=(ConnectionObject) pool.elementAt(index);
		//若连接使用次数超过connectionMaxUseCount，将此连接对象从连接池中删除。
		//避免一个连接由于反复使用而造成的性能不稳定
		if(co.useCount >= connectionMaxUseCount){
			closeConnection(co);
			co = null;
			pool.remove(index);
			
		}else{//将此连接对象的状态重新置为空闲
			co.isUse=false;
		}
	}
	
	//查找参数对象在连接池中的序号
	public int findConnectiOn(Connection connection){
		ConnectionObject co;
		if (pool==null)
			return -1;
		for(int i=0; i<pool.size(); i++){
			co=(ConnectionObject)pool.elementAt(i);
			if(co.connection==connection)return i;
		}
		return -1;
	}
	
	//析构函数，释放所有的连接，清空连接池
	public void finalize(){
		for(int i=0;i<pool.size();i++){
			ConnectionObject co=(ConnectionObject) pool.elementAt(i);
			closeConnection(co);
			co=null;
		}
		pool=null;
	}
	
	//关闭数据库连接
	public void closeConnection(ConnectionObject co){
		try{
			co.connection.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
		
}