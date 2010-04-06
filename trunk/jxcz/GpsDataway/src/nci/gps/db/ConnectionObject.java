package nci.gps.db;
import java.sql.*;

public class ConnectionObject{ 
	Connection connection;//数据库的物理连接
	boolean isUse;//此连接当前是否被使用
	int useCount;//此连接被使用的次数
	
	public ConnectionObject
		(String url,String userName,String	password){
		//创建物理连接
		try{
			if (userName !=null){
				connection=DriverManager.getConnection(url,userName,password);
			}else{
				connection=DriverManager.getConnection(url);				
			}
		}catch(SQLException ex){
			
		}
		isUse=false;//初始化连接状态为空闲
		useCount=0;//数据化使用次数为0
	}
			
}


