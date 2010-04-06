package com.nci.ums.periphery.core;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.nci.ums.util.*;
import com.nci.ums.periphery.application.AppServer;
import com.nci.ums.periphery.application.MonitorServer;

/**
 * This class make a main class to server for other application request
 */
public class SocketServer  {
    private ThreadGroup threadGroup;     // Control threads of socketreaders
    private Map parserMap;               // request code -> parser class
    private ArrayList umsServers;      //UMS服务端监听
    private EmailServer emailServer;
    private AppServer appServer;
    private MonitorServer monitorServer;

    public SocketServer() {
        umsServers=new ArrayList();
        init();
    }


    public void start() {
        int port = 10000;
        DBConnect db=null;
         try {
           db = new DBConnect();
           ResultSet rs = db.executeQuery("select serverport from system");
           while(rs.next())
           {
             port = rs.getInt("serverPort");
             UMSServer umsServer=new UMSServer(port,parserMap);
             umsServer.start();
             umsServers.add(umsServer);
             /*UMSThread umsThread=new UMSThread("UMS 外部接口平台","UMS外部接口线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",umsServer,"在端口"+port+"监听");
             Res.getUMSThreads().add(umsThread);*/
           }
           rs.close();
           rs = null;
         }catch (Exception e) {
           Res.log(Res.ERROR, "不能打开数据库或者System表位初始化");
           Res.logExceptionTrace(e);
//           System.exit(1);
         } finally{
             if(db!=null){
                 try{db.close();}catch(Exception e){}
             }
         }

         // open sendout media thread
        emailServer.start();
       /*umsThread=new UMSThread("UMS 邮件服务平台","UMS邮件接收线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",sendout,"");
       Res.getUMSThreads().add(umsThread);*/
        appServer.start();
        //启动监控报告服务
        monitorServer.start();

    }

    /**
    * This is the polite way to get a Listener to stop accepting
    * connections
    ***/
    public void pleaseStop() {
       for(int i=0;i<=umsServers.size();i++)
       {
            UMSServer ums=(UMSServer)umsServers.get(i);
            ums.pleaseStop();
       }
    }

    private void init() {
        emailServer = new EmailServer();
        appServer = new AppServer();
        monitorServer=new MonitorServer();
        threadGroup = new ThreadGroup(Server.class.getName());
        Properties res = new Properties();          // Empty properties
        try {
            InputStream in = new ServletTemp().getInputStreamFromFile("/resources/parser.props");
            res.load(in); // Try to load properties
        }
        catch (Exception e) {
            Res.log(Res.ERROR,"Load parser properties failed."+e);
            Res.logExceptionTrace(e);
//            System.exit(0);
        }
        parserMap = res;
    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        if(args.length>0)
        {
            if(args[0].equalsIgnoreCase("START"))
                server.start();
            else
                server.pleaseStop();
        }else{
            server.start();
        }

    }
}