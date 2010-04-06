package com.nci.svg.sdk.graphunit;

import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.XMLPrint;

public class SymbolVersionParser {

	public static final String ROOT = "versions";

	public static final String SYMBOL_TYPE_BEAN = "typebean";

	public static final String SYMBOL_TYPE = "type";

	public static final String SYMBOL_VARIETY_NAME = "vn";

	public static final String SYMBOL = "symbol";

	public static final String SYMBOL_VARIETY_CODE = "vc";

	public static final String SYMBOL_NAME = "name";

	public static final String SYMBOL_VERSION = "ver";

	private SymbolVersionParser() {

	}

	/**
	 * 将client传过来的特殊的用户名字符串解析成各个OwnerVersionBean
	 * 
	 * @param ownersStr
	 * @return
	 */
	public static OwnerVersionBean[] parseOwnerVersion(String ownersStr) {
		String[] owners = ownersStr.split(OwnerVersionBean.OWNER_SEP);
		OwnerVersionBean[] beans = new OwnerVersionBean[owners.length];
		for (int i = 0; i < owners.length; i++) {
			beans[i] = new OwnerVersionBean(owners[i], "");
		}
		return beans;
	}

	/**
	 * 创建Symbol大版本信息
	 * 
	 * @param releasedVersion
	 *            已发布的版本
	 * @param personalVersion
	 *            个人symbol的版本
	 * @return 合成的版本表达式
	 */
	public static String createSymbolVersion(OwnerVersionBean[] ovBean) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ovBean.length; i++) {
			sb.append(ovBean[i].toString());
			if (i != ovBean.length - 1) {
				sb.append(OwnerVersionBean.VERSION_SEP);
			}
		}
		return sb.toString();
	}

	/**
	 * 将各用户名组成特殊的字符串以传给servlet
	 * 
	 * @param owners
	 * @return
	 */
	public static String createOwnersStr(String[] owners) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < owners.length; i++) {
			sb.append(owners[i]);
			if (i != owners.length - 1) {
				sb.append(OwnerVersionBean.OWNER_SEP);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取Symbol的版本
	 * 
	 * @param version
	 *            合成的版本表达式
	 * @return Symbol的版本
	 */
	public static String parseVersion(String allVersionStr, String owner) {
		String[] ovStrs = allVersionStr.split(OwnerVersionBean.VERSION_SEP);
		for (int i = 0; i < ovStrs.length; i++) {
			String ownerTemp = ovStrs[i].substring(0, ovStrs[i]
					.indexOf(OwnerVersionBean.BEAN_STR_SEP));
			if (ownerTemp.equals(owner)) {
				String versionWithSep = ovStrs[i].substring(ovStrs[i]
						.indexOf(OwnerVersionBean.BEAN_STR_SEP));
				return versionWithSep.substring(OwnerVersionBean.BEAN_STR_SEP.length());
			}
		}
		return null;
	}


	/**
	 * 将所有的图元和模板的版本信息提取出来转成xml document
	 * 
	 * @param allSymbols
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static String createVersionDoc(Map allSymbols)
			throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document versionDoc = factory.newDocumentBuilder().newDocument();
		Element root = versionDoc.createElement(ROOT);
		versionDoc.appendChild(root);
		Iterator it = allSymbols.keySet().iterator();
		while (it.hasNext()) {
			SymbolTypeBean key = (SymbolTypeBean) it.next();
			Element typeBeanEle = versionDoc.createElement(SYMBOL_TYPE_BEAN);
			typeBeanEle.setAttribute(SYMBOL_TYPE, key.getSymbolType());
			typeBeanEle.setAttribute(SYMBOL_VARIETY_NAME, key.getVariety()
					.getName());
			typeBeanEle.setAttribute(SYMBOL_VARIETY_CODE, key.getVariety()
					.getCode());
			root.appendChild(typeBeanEle);
			Map childMap = (Map) allSymbols.get(key);
			Iterator it2 = childMap.values().iterator();
			while (it2.hasNext()) {
				Element symbolEle = versionDoc.createElement(SYMBOL);
				NCIEquipSymbolBean bean = (NCIEquipSymbolBean) it2.next();
				symbolEle.setAttribute(SYMBOL_NAME, bean.getName());
				symbolEle.setAttribute(SYMBOL_VERSION,
						bean.getModifyTime() == null ? "" : bean
								.getModifyTime());
				typeBeanEle.appendChild(symbolEle);
			}
		}
		return XMLPrint.printNode(versionDoc, false);
	}

}
