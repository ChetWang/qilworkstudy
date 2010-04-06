package com.nci.nbport.ums;


import java.sql.*;


/**
 *  一个数据库使用的wrapper类
 */
public class NBSmsConnect
{

    private Connection conn;
    private Statement stmt;
    private PreparedStatement prepstmt;

    void init() throws SQLException
    {
        conn = DriverManager.getConnection(NBSmsDb.getPoolName());
    }

    public NBSmsConnect()
        throws Exception
    {
        conn = null;
        stmt = null;
        prepstmt = null;
        init();
        stmt = conn.createStatement();
    }

    public NBSmsConnect(int i, int j)
        throws Exception
    {
        conn = null;
        stmt = null;
        prepstmt = null;
        init();
        stmt = conn.createStatement(i, j);
    }

    public NBSmsConnect(String s)
        throws Exception
    {
        conn = null;
        stmt = null;
        prepstmt = null;
        init();
        prepareStatement(s);
    }

    public NBSmsConnect(String s, int i, int j)
        throws Exception
    {
        conn = null;
        stmt = null;
        prepstmt = null;
        init();
        prepareStatement(s, i, j);
    }

    public Connection getConnection()
    {
        return conn;
    }

    // 事务控制
    public void transBegin()
        throws SQLException
    {
        conn.setAutoCommit(false);
    }

    public void commit()
        throws SQLException
    {
        conn.commit();
        conn.setAutoCommit(true);
    }

    public void rollback()
        throws SQLException
    {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    public void prepareStatement(String s)
        throws SQLException
    {
        prepstmt = conn.prepareStatement(s);
    }

    public void prepareStatement(String s, int i, int j)
        throws SQLException
    {
        prepstmt = conn.prepareStatement(s, i, j);
    }

    public void setString(int i, String s)
        throws SQLException
    {
        prepstmt.setString(i, s);
    }

    public void setInt(int i, int j)
        throws SQLException
    {
        prepstmt.setInt(i, j);
    }

    public void setBoolean(int i, boolean flag)
        throws SQLException
    {
        prepstmt.setBoolean(i, flag);
    }

    public void setTimestamp(int i, Timestamp timestamp)
    throws SQLException
	{
    	prepstmt.setTimestamp(i, timestamp);
	}

    public void setDate(int i, Date date)
        throws SQLException
    {
        prepstmt.setDate(i, date);
        
    }

    public void setLong(int i, long l)
        throws SQLException
    {
        prepstmt.setLong(i, l);
    }

    public void setFloat(int i, float f)
        throws SQLException
    {
        prepstmt.setFloat(i, f);
    }

    public void setBytes(int i, byte abyte0[])
        throws SQLException
    {
        prepstmt.setBytes(i, abyte0);
    }

    public void clearParameters()
        throws SQLException
    {
        prepstmt.clearParameters();
        prepstmt.close();
        prepstmt = null;
    }

    public PreparedStatement getPreparedStatement()
    {
        return prepstmt;
    }

    public Statement getStatement()
    {
        return stmt;
    }

    public ResultSet executeQuery(String s)
        throws SQLException
    {
        if(stmt != null)
            return stmt.executeQuery(s);
        else
            return null;
    }

    public ResultSet executeQuery()
        throws SQLException
    {
        if(prepstmt != null)
            return prepstmt.executeQuery();
        else
            return null;
    }

    public void executeUpdate(String s)
        throws SQLException
    {
        if(stmt != null)
            stmt.executeUpdate(s);
    }

    public void executeUpdate()
        throws SQLException
    {
        if(prepstmt != null)
            prepstmt.executeUpdate();
    }

    public void close()
        throws Exception
    {
        if(stmt != null)
        {
            stmt.close();
            stmt = null;
        }
        if(prepstmt != null)
        {
            prepstmt.close();
            prepstmt = null;
        }
        if(conn != null)
        {
            conn.close();
            conn = null;
        }
    }

    public String getSerial()
            throws SQLException
    {
        String ret = "";
        CallableStatement cs = conn.prepareCall("{ ? = call getSerial() }");
        cs.registerOutParameter(1, Types.VARCHAR);
        cs.execute();
        ret = cs.getString(1);
        cs.close();
        return ret;
    }







    public static void main(String[] args) {
            try{
                NBSmsConnect process = new NBSmsConnect();
                ResultSet rs=process.executeQuery("select * from SM_DELIVER");
                if(rs.next()){
                	String msg=rs.getString("MSG");
                	System.out.println(msg);
                }
                System.out.println(process.getSerial());
            }catch(Exception e){
            	e.printStackTrace();
            }

    }

}
