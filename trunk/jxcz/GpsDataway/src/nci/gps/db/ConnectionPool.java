package nci.gps.db;
import java.util.*;
import java.sql.*;

public class ConnectionPool {		

	private Vector pool=new Vector();//ʹ��Vector�������е����Ӷ���
	private int connectionPoolSize=1;//��ʼ��ʱ�������ӳ��н������Ӷ������Ŀ
	private int connectionPoolMaxSize;//���ӳ��������ܽ��������Ӷ������Ŀ
	private int connectionMaxUseCount;//�������������ܱ�ʹ�õĴ���
	private String driver;//���ӳ�����ʹ�õ�JDBC����������
	private String url;//������ʹ�õ�URL
	private String userName;//������ʹ�õ��û���
	private String password;//������ʹ�õ�����
	
	public ConnectionPool(String driver, String url, String userName, 
			String password, int minConnection, int maxConnection, int connectionMaxUseCount){
		//��ʼ������
		this.driver=driver;
		this.url=url;
		this.userName=userName;
		this.password=password;
		this.connectionPoolSize=minConnection;
		this.connectionPoolMaxSize=maxConnection;
		this.connectionMaxUseCount=connectionMaxUseCount;
		initPool(); //����˽�з���initPool()��ʼ�����ӳ�
	}	
	
	//����initPool�������Ӷ��󣬲���ʼ�����ӳ�
	private void initPool(){
		try{//��װJDBC����
			Class.forName(driver);
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
			return;
		}
		//����connectionPooISize�����е����Ӷ��󣬲������Ǽ���	�����ӳ���ȥ
		while(pool.size()<connectionPoolSize){
			ConnectionObject newConnection=new ConnectionObject(url,userName,password);
			if (newConnection.connection != null)
				pool.add(newConnection);
		}
	}
	
	//�û���ȡ�������ӵĽӿ�
	public Connection getConnection(){
		if (pool==null)
			return null;
		
		//���÷���getFreeConnection��ȡ����
		ConnectionObject connectionObject= getFreeConnection();
		//��û�п������Ӳ������ӳ�û�дﵽ����,����һ���µ�����
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
			//�����Ӷ����״̬����Ϊæ,�����������ӷ��ظ��û�ʹ��
			connectionObject.isUse=true;
			connectionObject.useCount++;
			System.out.println("UserCount:"+connectionObject.useCount);
			return connectionObject.connection;
		}
		
		return null;
	}
	
	/**
	 * �����ӳػ�ȡ�������ӣ�����ָ���ͻ������ܹ��ȴ����ʱ��
	 * @param timeout �Ժ���Ƶĵȴ�ʱ������
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
	
	//�������ӳ�,���ص�һ�����е�����,û���򷵻�null
	public ConnectionObject getFreeConnection(){
		if (pool == null)
			return null;
		for(int i=0;i<pool.size();i++){
			ConnectionObject co=(ConnectionObject) pool.elementAt(i);
			if(co.connection !=null && co.isUse==false) return co;
		}
		return null;
	}
	
	//�û��ͷ����ӵĽӿ�
	public void realse(Connection connection){
		int index=findConnectiOn(connection);
		if(index==-1) return;
		
		ConnectionObject co=(ConnectionObject) pool.elementAt(index);
		//������ʹ�ô�������connectionMaxUseCount���������Ӷ�������ӳ���ɾ����
		//����һ���������ڷ���ʹ�ö���ɵ����ܲ��ȶ�
		if(co.useCount >= connectionMaxUseCount){
			closeConnection(co);
			co = null;
			pool.remove(index);
			
		}else{//�������Ӷ����״̬������Ϊ����
			co.isUse=false;
		}
	}
	
	//���Ҳ������������ӳ��е����
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
	
	//�����������ͷ����е����ӣ�������ӳ�
	public void finalize(){
		for(int i=0;i<pool.size();i++){
			ConnectionObject co=(ConnectionObject) pool.elementAt(i);
			closeConnection(co);
			co=null;
		}
		pool=null;
	}
	
	//�ر����ݿ�����
	public void closeConnection(ConnectionObject co){
		try{
			co.connection.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
		
}