/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.graphunit;

import org.w3c.dom.Node;

/**
 * 仅仅是简单的针对图元类型的javabean，表示是那个图元类型，以及可能代表的xml 节点（仅在NCISingleGraphUnitManager中有效）
 * 
 * @author Qil.Wong
 */
public class NCIGraphUnitTypeBean {

	private String type;
	private Node node;

	public NCIGraphUnitTypeBean() {

	}

	public NCIGraphUnitTypeBean(String type) {
		this.type = type;
	}

	public NCIGraphUnitTypeBean(String type, Node node) {
		this.type = type;
		this.node = node;
	}

	public String getGraphUnitType() {
		return type;
	}

	public void setGraphUnitType(String type) {
		this.type = type;
	}

	public Node getGraphUnitNode() {
		return node;
	}

	public void setGraphUnitNode(Node node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return type;
	}

}
