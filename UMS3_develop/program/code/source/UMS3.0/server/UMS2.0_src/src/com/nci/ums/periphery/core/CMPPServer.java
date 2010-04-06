package com.nci.ums.periphery.core;

import java.net.ServerSocket;
import java.net.Socket;

import com.nci.ums.util.Res;

/**
 * This class make a main class to server for other application request
 */
public class CMPPServer extends Thread {
    private boolean stop=false;                // flag to stop this main thread

    public CMPPServer() {

    }


	public void run() {
        try {
            ServerSocket s = new ServerSocket(10001);
            Res.log(Res.INFO, "CMPPServer started at  10001" );
            while(!stop) {
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
                    new CMPPSocketRead(socket);
                    //new SocketRead(socket, parserMap, timeout);
                }catch (Exception sx) {
                    if(socket != null) socket.close();
                    sx.printStackTrace(System.out);
                    Res.log(Res.ERROR, "UMSServer:"+sx.getMessage());
                    Res.logExceptionTrace(sx);
            }
          }
          s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Res.log(Res.ERROR, "UMSServer:"+ex.getMessage());
            Res.logExceptionTrace(ex);
            System.exit(0);
        }
    }
    /**
    * This is the polite way to get a Listener to stop accepting
    * connections
    ***/
    public void pleaseStop() {
        this.stop = true;              // Set the stop flag
        this.interrupt();
    }

    public static void main(String[] args)
    {
        CMPPServer cmppServer=new CMPPServer();
        cmppServer.start();
    }
}