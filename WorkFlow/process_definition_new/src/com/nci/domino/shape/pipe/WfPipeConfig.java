package com.nci.domino.shape.pipe;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.domino.beans.plugin.pipe.WofoPipeBaseBean;
import com.nci.domino.components.dialog.WfDialog;

/**
 * 管道设计配置类
 * 
 * @author Qil.Wong
 * 
 */
@SuppressWarnings("unchecked")
public class WfPipeConfig {

	/**
	 * 管道默认宽度
	 */
	public static double defaultPipeWidth;

	/**
	 * 管道默认高度
	 */
	public static double defaultPipeHeight;

	/**
	 * 垂直管道对象输入窗口类
	 */
	public static Class<WfDialog> verticalPipeInput;

	/**
	 * 水平管道对象输入窗口类
	 */
	public static Class<WfDialog> horizontalPipeInput;

	/**
	 * 水平管道默认显示值
	 */
	public static String verticalDefaultText;
	/**
	 * 垂直管道默认显示值
	 */
	public static String horizontalDefaultText;

	public static Class<WofoPipeBaseBean> verticalBeanClass;

	public static Class<WofoPipeBaseBean> horizontalBeanClass;

	static {
		try {
			InputStream is = WfPipeConfig.class
					.getResourceAsStream("wfpipe.xml");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document pipeDoc = docBuilder.parse(is);
			is.close();
			Element root = pipeDoc.getDocumentElement();
			defaultPipeWidth = Double.parseDouble(root.getAttribute("width"));
			defaultPipeHeight = Double.parseDouble(root.getAttribute("height"));
			Element vertical = (Element) root.getElementsByTagName("vertical")
					.item(0);
			Element horizontal = (Element) root.getElementsByTagName(
					"horizontal").item(0);
			verticalDefaultText = vertical.getAttribute("defaultValue");
			horizontalDefaultText = horizontal.getAttribute("defaultValue");
			verticalPipeInput = (Class<WfDialog>) Class.forName(vertical
					.getAttribute("inputDialog"));
			horizontalPipeInput = (Class<WfDialog>) Class.forName(horizontal
					.getAttribute("inputDialog"));
			verticalBeanClass = (Class<WofoPipeBaseBean>) Class.forName(vertical.getAttribute("bean"));
			horizontalBeanClass = (Class<WofoPipeBaseBean>) Class
					.forName(horizontal.getAttribute("bean"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
