package nci.gps.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class ClientUtil {

	/**
	 * 无入参调用
	 * 
	 * @return
	 */
	public static OMElement getQueryOMElement() {
		return getQueryOMElement(null, null, null);
	}

	/**
	 * 标准QUERY查询
	 * 
	 * @param parm
	 * @param parmValue
	 * @return
	 */
	public static OMElement getQueryOMElement(String[] parm, String[] parmValue) {
		return getQueryOMElement(parm, parmValue, "aa_ceshi");
	}

	/**
	 * 扩展查询，funName为服务器端提供方法名称
	 * 
	 * @param parm
	 * @param parmValue
	 * @param funName
	 * @return
	 */
	public static OMElement getQueryOMElement(String[] parm,
			String[] parmValue, String funName) {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://com.longshine.zjyx/yx_cc", "");
		OMElement method = fac.createOMElement(funName, omNs);
		OMElement value = fac.createOMElement("input", omNs);
		if (parm != null && parmValue != null) {
			for (int i = 0; i < parm.length; i++) {
				if (parm[i] != null && parmValue[i] != null) {
					OMElement temp = fac.createOMElement(parm[i], omNs);
					temp.addChild(fac.createOMText(temp, parmValue[i]));
					value.addChild(temp);
				}
			}
		}
		method.addChild(value);
//		System.out.println(method);
		return method;
	}

	public static OMElement getUpdateOMElement() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://example1.org/example1", "example1");
		OMElement method = fac.createOMElement("update", omNs);
		OMElement value = fac.createOMElement("Text", omNs);
		value.addChild(fac.createOMText(value, "Axis2 Ping String "));
		method.addChild(value);
		return method;
	}

	public static OMElement getDataChangeOMElement() {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://com.longshine.zjyx/yx_cc", "");
		OMElement method = fac.createOMElement("dataExchange", omNs);
		OMElement value = fac.createOMElement("input", omNs);
		method.addChild(value);
		return method;
	}
}
