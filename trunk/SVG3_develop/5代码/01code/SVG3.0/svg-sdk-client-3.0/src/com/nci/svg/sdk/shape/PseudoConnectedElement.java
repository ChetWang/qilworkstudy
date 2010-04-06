package com.nci.svg.sdk.shape;

import org.w3c.dom.Element;

/**
 * 鼠标在连接线移过去的时候断定可以连接的图元
 * @author qi
 *
 */
public class PseudoConnectedElement {
	
	private Element connEle;
	
	private String position;
	
	public PseudoConnectedElement(){}
	
	public PseudoConnectedElement(Element connEle,String position){
		this.connEle = connEle;
		this.position = position;
	}

	public Element getConnEle() {
		return connEle;
	}

	public String getPosition() {
		return position;
	}

	public void setConnEle(Element connEle) {
		this.connEle = connEle;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	

}
