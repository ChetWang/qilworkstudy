package com.nci.svg.sdk.client.selection;

import java.awt.event.MouseEvent;

import org.w3c.dom.Element;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * �����ѡ��ģʽ������ҵ�������չ����������¼��ͻ����¼�
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
