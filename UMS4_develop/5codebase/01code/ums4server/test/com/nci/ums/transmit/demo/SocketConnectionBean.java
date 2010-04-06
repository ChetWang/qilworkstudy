/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.transmit.demo;

import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.common.message.ControlCode;

/**
 * 
 * @author Qil.Wong
 */
public class SocketConnectionBean {

	private String ip;

	private int port;

	private String infoText;

	private String filePath;

	private boolean hex;

	private boolean connected;

	private TransmitClient client;

	private int subServerType;

	private int clientAddress;
	
	private int forwardAddress;

	private boolean fileTranmit;

	private TransmitSocketPanel bindPanelBean;

	public TransmitClient getClient() {
		return client;
	}

	public void setClient(TransmitClient client) {
		this.client = client;
	}

	public int getSubServerType() {
		return subServerType;
	}

	public void setSubServerType(int subServerType) {
		this.subServerType = subServerType;
	}

	public int getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(int clientAddress) {
		this.clientAddress = clientAddress;
	}

	public SocketConnectionBean() {
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
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
	 * @param port
	 *            the port to set
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
	 * @param infoText
	 *            the infoText to set
	 */
	public void setInfoText(String infoText) {
		this.infoText = infoText;
		bindPanelBean.getInfoArea().setText(infoText);
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		bindPanelBean.getPathField().setText(filePath);
	}

	/**
	 * @return the isHex
	 */
	public boolean isHex() {
		return hex;
	}

	/**
	 * @param isHex
	 *            the isHex to set
	 */
	public void setHex(boolean isHex) {
		this.hex = isHex;
		bindPanelBean.getHexCheckBox().setSelected(isHex);
	}

	/**
	 * @return the connect
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @param connect
	 *            the connect to set
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
		bindPanelBean.getConnectBtn().setEnabled(!connected);
		bindPanelBean.getDisConnectBtn().setEnabled(connected);
		bindPanelBean.getFileChooseBtn().setEnabled(connected);
		bindPanelBean.getFileCheckBox().setEnabled(connected);
		bindPanelBean.getSendBtn().setEnabled(connected);
	}




	public String toString() {
		String t_a = subServerType == ControlCode.DIRECTION_FROM_APPLICATION ? "A"
				: "T";
		return ip + ":" + port + "-[" + clientAddress + "]-" + t_a;
	}

	public TransmitSocketPanel getBindPanelBean() {
		return bindPanelBean;
	}

	public void setBindPanelBean(TransmitSocketPanel bindPanelBean) {
		this.bindPanelBean = bindPanelBean;
	}

	public void appendInfo(String info) {
		String origin = getInfoText();
		if (origin != null && !origin.equals("")) {
			String temp = getInfoText() + "\n" + info;
			setInfoText(temp);
		} else {
			setInfoText(info);
		}

	}

	public void appendException(Exception e) {
		appendInfo(e.getClass().getName() + ":" + e.getMessage());
	}

	public boolean isFileTranmit() {
		return fileTranmit;
	}

	public void setFileTranmit(boolean fileTranmit) {
		this.fileTranmit = fileTranmit;
		bindPanelBean.getFileCheckBox().setSelected(fileTranmit);
		bindPanelBean.getFileChooseBtn().setEnabled(fileTranmit);
		bindPanelBean.getSimpleInputArea().setEditable(!fileTranmit);
		bindPanelBean.getHexCheckBox().setEnabled(!fileTranmit);
	}

	public int getForwardAddress() {
		return forwardAddress;
	}

	public void setForwardAddress(int forwardAddress) {
		this.forwardAddress = forwardAddress;
	}

}
