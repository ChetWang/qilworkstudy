/**
 * <p>Title: Util.java</p>
 * <p>Description:
 *    提供公用的函数库
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;

public class SerialNO {

  private  int serialNO=0;
  private  int replyNO=0;
  private   String replyRemark;
  static private SerialNO serialObj;

    /*
     * 工厂方法得到唯一实例
     */
    static synchronized public SerialNO getInstance()
    {
      if (serialObj == null) {
        serialObj = new SerialNO();
      }
      return serialObj;
    }


    public  synchronized String getSerial()
    {
        if(serialNO<1000000)
            serialNO=serialNO+1;
        else
            serialNO=1;


        return (new Integer(serialNO)).toString();
    }

    public synchronized  String getReply()
    {
        if(replyRemark==null)
            replyRemark=getReplyRemark();
        if(replyNO<10000){
            replyNO=replyNO+1;
        }else{
            replyRemark=getReplyRemark();
            replyNO=1;
        }
        StringBuffer sb=new StringBuffer("0");
        sb.append(replyRemark);
        String result=(new Integer(replyNO)).toString();
        int retLength=result.length();
        for(int i=4;i>retLength;i--)
                sb.append("0");
        sb.append(result);
        Res.log(Res.DEBUG,"取得回复号"+result);

        return sb.toString();
    }


    public  String getReplyRemark()
    {
    		int srialNO=1;
            DBConnect db=null;
            ResultSet rs=null;
            try{
            	db=new DBConnect();            	
            	rs=db.executeQuery("select * from serial where serialType='ReplyNO'");
            	if(rs.next()){
            		serialNO=rs.getInt("serialNO");
            		if(serialNO>=9)
            			serialNO=1;
            		else
            			serialNO=serialNO+1;            		
            		
            	}
            	StringBuffer sb=new StringBuffer("update serial set serialNO=");
				sb.append(serialNO).append(" where serialType='ReplyNO'");
            	db.executeUpdate(sb.toString());
            }catch(Exception e){
                Res.log(Res.ERROR,"取得回复标识号出错"+e);
            }finally{
            	if(rs!=null)try{rs.close();}catch(Exception ex){}
                if(db!=null)try{db.close();}catch(Exception ex){}
            }
           return new Integer(serialNO).toString();

    }

   public  String getPY()
    {
            Connection db=null;
            String ret = "1";
            try{
            	System.out.println((new Date()).getTime());
            	db = DriverManager.getConnection(DataBaseOp.getPoolName());
            	CallableStatement cs = db.prepareCall("{call sp_get_py(?,?) }");
                //注意在汉字中有空格数字字母标点符号会出错
                //性能不是很好，需一秒钟
            	cs.setString(1,"张志勇");
                cs.setString(2,"");
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.registerOutParameter(2, Types.VARCHAR);
                cs.execute();
                ret = cs.getString(2);
                System.out.println((new Date()).getTime());
                cs.close();
            }catch(Exception e){
                Res.log(Res.ERROR,"取汉字首字母时出错"+e);
            }finally{
                if(db!=null)try{db.close();}catch(Exception ex){}
            }
           return ret;

    }

    public static void main(String[] args)
    {
        System.out.println(SerialNO.getInstance().getPY());
    }
}