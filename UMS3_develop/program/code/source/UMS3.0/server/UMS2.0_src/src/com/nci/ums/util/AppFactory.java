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


import java.sql.*;
import java.util.HashMap;

public class AppFactory {

  private static HashMap applications;
  private static AppFactory appObj;

    static {
        if(applications==null)
        {
            applications=new HashMap();
        }

    }
    /*
     * 工厂方法得到唯一实例
     */

   public static AppFactory getInstance()
   {
        if(appObj==null)
        {
            return new AppFactory();
        }else
        {
            return appObj;
        }
   }


    public  Application getApplication(String appID)
    {
        Application app=(Application)applications.get(appID);
        return app;
    }

    public  int getSize()
    {
        return applications.size();
    }

    public void refresh()
    {
        ResultSet rs=null;
        DBConnect db = null;
        try{
            applications=new HashMap();
            db=new DBConnect();
            rs=db.executeQuery("select * from application") ;
            while(rs.next()){
                String appID=rs.getString("appid");
                Application app=new Application(appID,rs.getString("spNO"),rs.getInt("fee"));
                app.setFeeType(rs.getInt("feeType"));
                applications.put(appID,app);
            }
        }catch(Exception e){
            Res.log(Res.ERROR,"get applications error"+e);
            Res.logExceptionTrace(e);
        }finally{
            if(rs!=null) try{rs.close();}catch(SQLException ex){}
            if(db!=null)try { db.close(); } catch (Exception e1) {}
        }
//        Res.log(Res.DEBUG,"size:"+applications.size());
    }

     public  static void main(String[] args)
    {
         AppFactory.getInstance().refresh();
         AppFactory.getInstance().refresh();
         AppFactory.getInstance().refresh();
         Res.log(Res.DEBUG,"size:"+AppFactory.getInstance().getSize());
    }
}