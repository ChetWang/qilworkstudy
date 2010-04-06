/**
 * <p>Title: CMPPOutChannel.java</p>
 * <p>Description:
 *    CMPPOutChannel外拨渠道类，继承OutChannel
 *    实现移动信息的发送过程
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.periphery.application;

import com.nci.ums.periphery.exception.ApplicationException;
import com.nci.ums.util.Util;
import com.nci.ums.util.Res;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

public class SocketAppChannel
        implements Send {
    
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private Socket socket;
    private AppInfo appInfo;
    
    public boolean initInterface() throws ApplicationException {
        return true;
    }
    
    
    public boolean initInterface(AppInfo appInfo) throws ApplicationException {
        boolean result=true;
        try{
            this.appInfo=appInfo;
            if(appInfo.getSocket()==null) {
                socket = new Socket(appInfo.getIp(), appInfo.getPort());
                this.appInfo.setSocket(socket);
                AppServer.setAppInfo(this.appInfo);
            }else{
                socket=appInfo.getSocket();
            }
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }catch(Exception e){
            result=false;
            throw new ApplicationException("1041","网络连接错");
        }
        return result;
    }
    
    public boolean receiveMessage(String loginName,String loginPwd,String retCode,String msgID,int ack,String receive,String from,String receiveDate,String receiveTime,String content)throws ApplicationException {
        boolean result=false;
        StringBuffer sb=new StringBuffer("100100");
        sb.append(Util.getFixedString(loginName,12)).append(loginPwd);
        String responseStr="";
        try{
            //发出登录应用程序报文
            if(!appInfo.isPassed()){
                //Res.log(Res.DEBUG,"send password");
                out.writeBytes(Res.getMessage(sb.toString()));
                responseStr=socketRead(in);
                //Res.log(Res.DEBUG,"result:"+responseStr);
                if(responseStr.length()>=4&&responseStr.substring(0,4).equals("0000")) {
                    appInfo.setPassed(true);
                    AppServer.setAppInfo(this.appInfo);
                }
            }
            
            if(appInfo.isPassed()){
                //给应用程序传信息
                sb=new StringBuffer("100200");
                sb.append(Util.getFixedString(retCode,4)).append(Util.getFixedString(msgID,35)).append(ack).
                        append(Util.getFixedString(receive,15)).
                        append(Util.getFixedString(from,15)).
                        append(Util.getFixedString(receiveDate,8)).
                        append(Util.getFixedString(receiveTime,6)).
                        append(Util.getFixedString(content,160));
                
                out.writeBytes(Res.getMessage(sb.toString()));
                responseStr=socketRead(in);
                if(responseStr.length()>=4&&responseStr.substring(0,4).equals("0000"))
                    result=true;
            }
        }catch(Exception e){
            throw new ApplicationException("1041","网络连接错");
        }finally{
            /*try{
                socket.close();
            }catch(Exception e){}*/
        }
        
        
        return result;
    }
    
    private String socketRead(DataInputStream in) {
        String result="";
        byte[] sb=new byte[1024];
        int length = -10;
        int index = 0;
        int k=0;
        try{
            while(true) {
                sb[k] = in.readByte();
                index++;
                //Res.log(Res.DEBUG,"length:"+index);
                k++;
                if(index == 5) {
                    //get length of request and clear sb
                    sb[index]=0x0;
                    String lenStr = new String(sb, 0,5, "GBK");
                    //Res.log(Res.DEBUG,"result String length:"+lenStr);
                    lenStr = lenStr.trim();
                    length = Integer.parseInt(lenStr);
                    //Res.log(Res.DEBUG,"result int length:"+length);
                    k=0;
                }
                //get all request string and parse it, then clear all for next request
                if(index - 5 == length) {
                    //process curent request
                    sb[index]=0x0;
                    result=new String(sb,0,length,"GBK");
                    length = -10;
                    index = 0;
                    k=0;
                    //Res.log(Res.DEBUG,"result String:"+result);
                    break;
                }
            }
        }catch (Exception ex) {
            result="000100Socket通讯出错!";
            Res.log(Res.ERROR,"socket error:"+ex);
            Res.logExceptionTrace(ex);
        }
        return result;
    }
    
}