package nci.gps.db;
import java.sql.*;

public class ConnectionObject{ 
	Connection connection;//���ݿ����������
	boolean isUse;//�����ӵ�ǰ�Ƿ�ʹ��
	int useCount;//�����ӱ�ʹ�õĴ���
	
	public ConnectionObject
		(String url,String userName,String	password){
		//������������
		try{
			if (userName !=null){
				connection=DriverManager.getConnection(url,userName,password);
			}else{
				connection=DriverManager.getConnection(url);				
			}
		}catch(SQLException ex){
			
		}
		isUse=false;//��ʼ������״̬Ϊ����
		useCount=0;//���ݻ�ʹ�ô���Ϊ0
	}
			
}


