/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ums.demo.socket;

/**
 *
 * @author Qil.Wong
 */
public class SocketConnectionBean {

    private String ip;

    private int port;

    private String infoText;

    private String filePath;

    private boolean isHex;

    private boolean connect;

    private SocketConnection connection;

    public SocketConnectionBean(){}

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the infoText
     */
    public String getInfoText() {
        return infoText;
    }

    /**
     * @param infoText the infoText to set
     */
    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the isHex
     */
    public boolean isIsHex() {
        return isHex;
    }

    /**
     * @param isHex the isHex to set
     */
    public void setIsHex(boolean isHex) {
        this.isHex = isHex;
    }

    /**
     * @return the connect
     */
    public boolean isConnect() {
        return connect;
    }

    /**
     * @param connect the connect to set
     */
    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    /**
     * @return the connection
     */
    public SocketConnection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(SocketConnection connection) {
        this.connection = connection;
    }


}
