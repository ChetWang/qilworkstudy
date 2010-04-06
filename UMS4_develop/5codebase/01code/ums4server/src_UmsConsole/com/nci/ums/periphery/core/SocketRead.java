package com.nci.ums.periphery.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;

import com.nci.ums.util.Res;

/**
 * 允许一个应用的一个socket有多个请求，一个请求可以分多次断续发送
 */
public class SocketRead extends Thread {

    private int timeout; // Set server socket timeout
    private Socket socket; // socket for reading
    private DataInputStream in; // input stream
    private DataOutputStream out; // output stream
    //private PrintWriter out;
    private Map parserMap;
    private boolean stop = false;

    public SocketRead(Socket socket, Map parserMap) throws SocketException, IOException {
        this.timeout = 5 * 60 * 1000;
        this.socket = socket;
        this.parserMap = parserMap;
        initSocket(this.socket);
        start();
    }

    public SocketRead(Socket socket, Map parserMap, int timeout) throws SocketException, IOException {
        this.timeout = timeout;
        this.socket = socket;
        this.parserMap = parserMap;
        initSocket(this.socket);
        start();
    }

    public void run() {
//        ParserController controller = new ParserController(out, parserMap);
        String ip = socket.getInetAddress().getHostAddress();
//        controller.setIp(ip);
        byte[] sb = new byte[1024];
        int length = -10;
        int index = 0;
        int k = 0;
        int recvNUM = 0;

        try {
            while (!stop) {
//            	try{
//            	Object o = ois.readObject();
//            	}catch(Exception e){
//            		
//            	}
                sb[k] = in.readByte();
                index++;
                k++;
                if (index == 5) {
                    sb[index] = 0x0;
                    String lenStr = new String(sb, 0, 5, "GBK");
                    //Res.log(Res.DEBUG, "New request length : " + lenStr);
                    lenStr = lenStr.trim();
                    length = Integer.parseInt(lenStr);
                    k = 0;
                }
                if (index - 5 == length) {
                    sb[index] = 0x0;
//                    String lenStr = new String(sb, 0, length, "GBK");
//                    Res.log(Res.DEBUG, "New request : " + lenStr);
//                    controller.parse(sb, k + 1);
                    recvNUM = recvNUM + 1;
                    index = 0;
                    length = -10;
                    k = 0;
                }
            }
        } catch (NumberFormatException ex) {
            Res.log(Res.ERROR, ex.getMessage());
            try {
                //out.writeBytes(Res.getMessage("1003", Res.getStringFromCode("1003")));
                out.writeBytes(Res.getMessage("1003", Res.getStringFromCode("1003")));
                out.flush();
            } catch (Exception e) {
            }
        } catch (SocketTimeoutException ex) {
            Res.log(Res.DEBUG, ex.getMessage());
        } catch (IOException ex) {
            CacheDataHelper.removeIpLogin(ip);
            Res.log(Res.DEBUG, "IOException:" + ex.getClass().getName()+":"+ex.getMessage());
            //Res.logExceptionTrace(ex);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
            	Res.logExceptionTrace(ex);
            }
        }
    }

    private void initSocket(Socket s) throws SocketException, IOException {
        Res.log(Res.DEBUG, "初始化端口");
        s.setSoTimeout(timeout);
        in = new DataInputStream(s.getInputStream());
        out = new DataOutputStream(s.getOutputStream());
        //out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
    }

    public boolean getThreadState() {
        return !stop;
    }

    public void pleaseStop() {
        stop = true;
    }
}