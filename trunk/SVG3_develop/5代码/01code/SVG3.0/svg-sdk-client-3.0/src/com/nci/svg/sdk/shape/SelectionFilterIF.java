package com.nci.svg.sdk.shape;

import org.w3c.dom.Element;

public interface SelectionFilterIF {

	/**
	 * ѡ��ڵ�ʱ����λ���ص��£�svgĬ����ѡ��xml�ṹ�е����ϲ��Ԫ�أ�����Ҫѡ���Ĭ�ϵĽڵ㣬��Ҫ���й����жϡ�
	 * 
	 * @param shapeElement
	 *            ���жϵĽڵ�
	 * @return trueΪ����������ȷ��Ϊѡ��Ľڵ㣻falseΪ�����ϣ�����������ж�
	 */
	public boolean filterElement(Element shapeElement);

}
