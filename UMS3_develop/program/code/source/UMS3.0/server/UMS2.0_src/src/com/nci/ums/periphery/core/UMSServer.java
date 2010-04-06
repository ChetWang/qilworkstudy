package com.nci.ums.periphery.core;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.util.Res;

/**
 * This class make a main class to server for other application request
 */
public class UMSServer extends Thread {
    private int timeout;                 // Set socket timeout
    private boolean stop=false;                // flag to stop this main thread
    private int port;
    private Map parserMap;
    private QuitLockFlag quitLockFlag;
    ServerSocket s;
    private int startFinishedFlag = 0;
    public static final int UMSSERVER_SOCKET_NOT_STARTED = 0;
    public static final int UMSSERVER_SOCKET_STARTED = 1;
    public static final int UMSSERVER_SOCKET_BIND = 2;

    public UMSServer(int port,Map parserMap) {
        timeout = 5*60*1000;
        this.port=port;
        this.parserMap=parserMap;
        quitLockFlag=QuitLockFlag.getInstance();
        quitLockFlag.setLockFlag(false);
    }

    public UMSServer(int timeout,int port,Map parserMap) {
        this.timeout = timeout;
        this.port=port;
        this.parserMap=parserMap;
        quitLockFlag=QuitLockFlag.getInstance();
        quitLockFlag.setLockFlag(false);
    }

	public void run() {
        try {
            s = new ServerSocket(port);
            Res.log(Res.INFO, "UMSServer Socket接入端口为：" + port);
            startFinishedFlag = UMSSERVER_SOCKET_STARTED;
            while(!stop&&!quitLockFlag.getLockFlag()) {
                Socket socket = null;
                try {                	
                    socket = s.accept();
                    //check allow ip
                    /*String ip = socket.getInetAddress().getHostAddress();
                    if(!Res.getAllowIP().contains(ip)) {
                      Res.log(Res.INFO, "Not allowed ip " + ip);
                      socket.close();
                      socket = null;
                      continue;
                    } */

                    Res.log(Res.DEBUG, "New connection : " + socket.getInetAddress().getHostAddress());
                    new SocketRead(socket, parserMap, timeout);
                    //new SocketRead(socket, parserMap, timeout);
                }catch (Exception sx) {
                    if(socket != null) socket.close();
//                    sx.printStackTrace(System.out);
                    Res.log(Res.ERROR, "UMSServer:"+sx.getMessage());
//                    Res.logExceptionTrace(sx);
            }
          }       
        } catch (Exception ex) {            
        	if(ex instanceof BindException){
        		startFinishedFlag = UMSSERVER_SOCKET_BIND;
        	}
            Res.log(Res.ERROR, "UMSServer:"+ex.getMessage());
            Res.logExceptionTrace(ex);
//            System.exit(0);
        }
    }
    /**
    * This is the polite way to get a Listener to stop accepting
    * connections
    ***/
    public void pleaseStop() {
    	try {
    		Res.log(Res.INFO, "关闭UMS Socket接收端口:"+s.getLocalPort());
			s.close();
		} catch (Exception e) {
//			e.printStackTrace();
		}
    	s=null;
        this.stop = true;              // Set the stop flag
        this.interrupt();
    }
    
    public int  getStartFinishedFlag(){
    	return startFinishedFlag;
    }

}