package com.nci.ums.periphery.core;

import java.io.*;
import java.net.*;


import com.nci.ums.util.*;

/**
 * 允许一个应用的一个socket有多个请求，一个请求可以分多次断续发送
 */
public class CMPPSocketRead extends Thread  {
    private int timeout=600;                 // Set server socket timeout
    private Socket socket;               // socket for reading
    private DataInput  in;           // input stream
    private DataOutput out;
    private boolean stop=false;


    public CMPPSocketRead(Socket socket)
            throws SocketException, IOException{
        this.socket = socket;
        initSocket(this.socket);
        start();
    }

    public void run() {
        byte[] sb=new byte[1024];
        int length = -10;
        int index = 0;
        int k=0;
        int recvNUM=0;

        try {
                while(!stop) {
                    sb[k] = in.readByte();
                     index++;
                     k++;
                    if(index == 5) {
                        sb[index]=0x0;
                        String lenStr = new String(sb, 0,5, "GBK");
                        Res.log(Res.DEBUG, "New request  length: " + lenStr);
                        lenStr = lenStr.trim();
                        length = Integer.parseInt(lenStr);
                        k=0;
                    }
                    if(index - 5 == length) {
                        sb[index]=0x0;
                        String lenStr = new String(sb, 0,length, "GB2312");
                        Res.log(Res.DEBUG, "New request : " + lenStr);
                        out.writeBytes(Res.getMessage("0000"));
                        recvNUM=recvNUM+1;
                        index = 0;
                        length = -10;
                        k=0;

                    }
                }
                /*if(k==0){
                    length=in.readInt();
                    Res.log(Res.DEBUG, "New request  length: " + length);
                }else {
                    sb[index]=in.readByte();
                    index++;
                }
                k++;
                if(index  == length-4) {
                    sb[index]=0x0;
                    String lenStr = new String(sb, 0,length, "GB2312");
                    Res.log(Res.DEBUG, "New request : " + lenStr);
                    Res.log(Res.DEBUG, "New request  byte: " );
                    recvNUM=recvNUM+1;
                    index = 0;
                    length = -10;
                    k=0;

                }

            }     */
        }catch (NumberFormatException ex) {
            Res.log(Res.ERROR, ex.getMessage());
            try{
                //out.writeBytes(Res.getMessage("1003", Res.getStringFromCode("1003")));
            }catch(Exception e){}
        }catch (SocketTimeoutException ex) {
            Res.log(Res.DEBUG, ex.getMessage());
        }catch (IOException ex) {
            Res.log(Res.ERROR, ex.getMessage());
        }finally{
            try {
                socket.close();
            } catch (IOException ex) { }
        }
    }

    private void initSocket(Socket s)
            throws SocketException, IOException{
        Res.log(Res.DEBUG,"初始化端口");
        //s.setSoTimeout(timeout);
        in = new DataInputStream(s.getInputStream());
        out = new DataOutputStream(s.getOutputStream());
    }

    public boolean getThreadState()
    {
        return !stop;
    }

    public void pleaseStop()
    {
        stop=true;
    }

}