package com.nci.svg.sdk.client.selection;

import java.awt.event.MouseEvent;

import org.w3c.dom.Element;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * 抽象的选择模式，便于业务操作扩展。包括鼠标事件和滑轮事件
 * 
 * @author qi
 * 
 */
public class ModeObject {

	private SVGHandle handle = null;

	private MouseEvent mouseEvent = null;
	
	private Element elementAtMousePoint = null;

	public ModeObject() {
	}

	public ModeObject(SVGHandle handle) {
		this.handle = handle;
	}

	public MouseEvent getMouseEvent() {
		return mouseEvent;
	}

	public void setMouseEvent(MouseEvent mouseEvent) {
		this.mouseEvent = mouseEvent;
	}

	public SVGHandle getHandle() {
		return handle;
	}

	public void setHandle(SVGHandle handle) {
		this.handle = handle;
	}

	public Element getElementAtMousePoint() {
		return elementAtMousePoint;
	}

	public void setElementAtMousePoint(Element elementAtMousePoint) {
		this.elementAtMousePoint = elementAtMousePoint;
	}
	
	

}
