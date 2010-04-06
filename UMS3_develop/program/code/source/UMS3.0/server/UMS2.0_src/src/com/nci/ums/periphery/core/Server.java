package com.nci.ums.periphery.core;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.nci.ums.util.*;
import com.nci.ums.channel.channelmanager.*;
import com.nci.ums.periphery.application.AppServer;
//import com.nci.ums.periphery.application.JMSReceiver;

import com.nci.ums.periphery.application.MonitorServer;


/**
 * This class make a main class to server for other application request
 */
public  class Server  {
    private SendOut sendout;             // media send out thread
    private ThreadGroup threadGroup;     // Control threads of socketreaders
    private Map parserMap;               // request code -> parser class
    private ArrayList umsServers;      //UMS����˼���
    private AppServer appServer;
    private MonitorServer monitorServer;
    
//    private JMSReceiver jmsReceiver;


    public Server() {
        umsServers=new ArrayList();
        init();
    }

    public void start() {
         DBConnect db =null;
         try {
             db = new DBConnect();
             db.executeUpdate("update out_ready set statusFlag=0");
//             db.executeUpdate("update out_ready_v3 set statusFlag=0");
             ResultSet rs = db.executeQuery("select serverport from system_server");
             while(rs.next())
             {
               int port = rs.getInt("serverPort");
               UMSServer umsServer=new UMSServer(port,parserMap);
               umsServer.start();
               umsServers.add(umsServer);
               /*UMSThread umsThread=new UMSThread("UMS �ⲿ�ӿ�ƽ̨","UMS�ⲿ�ӿ��߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",umsServer,"�ڶ˿�"+port+"����");
               Res.getUMSThreads().add(umsThread);*/
             }
             rs.close();
             rs = null;
             
         }catch (Exception e) {
         	e.printStackTrace();
           Res.log(Res.ERROR, "���ܴ����ݿ����System��λ��ʼ��"+e);
           Res.logExceptionTrace(e);
           System.exit(1);
         }finally{
          try{
          if(db!=null)db.close();
          }catch(Exception e){}
      }
        // open sendout media thread
         sendout.start();
        /*UMSThread umsThread=new UMSThread("UMS ����Ϣƽ̨","UMS����Ϣ�����߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",sendout,"");
        Res.getUMSThreads().add(umsThread);*/
       /*umsThread=new UMSThread("UMS �ʼ�����ƽ̨","UMS�ʼ������߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",sendout,"");
       Res.getUMSThreads().add(umsThread);*/
        appServer.start();
        //������ر������
        monitorServer.start();
//        jmsReceiver.start();
    }

    /**
    * This is the polite way to get a Listener to stop accepting
    * connections
    ***/
    public void pleaseStop() {
       sendout.stopThread();
       for(int i=0;i<=umsServers.size();i++)
       {
            UMSServer ums=(UMSServer)umsServers.get(i);
            ums.pleaseStop();
       }
    }

    private void init() {
    	 
        sendout = new SendOut();
        appServer = new AppServer();
        monitorServer=new MonitorServer();
        threadGroup = new ThreadGroup(Server.class.getName());
//        jmsReceiver = new JMSReceiver();
        Properties res = new Properties();          // Empty properties
        try {
            InputStream in = new ServletTemp().getInputStreamFromFile("/resources/parser.props");
            res.load(in); // Try to load properties
        }
        catch (Exception e) {
            System.err.println("------"+"Load parser properties failed.");
            System.exit(0);
        }
        parserMap = res;
    }

    public static void main(String[] args) {
        Server server = new Server();
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