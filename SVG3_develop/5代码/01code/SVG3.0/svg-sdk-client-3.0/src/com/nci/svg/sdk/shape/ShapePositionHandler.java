package com.nci.svg.sdk.shape;

import java.util.Set;

import org.w3c.dom.Element;

/**
 * �����ɵ�Ԫ�ص�λ����������ͬ���˽ӿڿ��Զ��������ɵ�Ԫ����SVG�е�λ�ã�������һζ�����ĵ�ĩ������
 * 
 * @author qi
 * 
 */
public interface ShapePositionHandler {

	public void handlePosition(Set<Element> createdElements,
			Element parentElement);
}
