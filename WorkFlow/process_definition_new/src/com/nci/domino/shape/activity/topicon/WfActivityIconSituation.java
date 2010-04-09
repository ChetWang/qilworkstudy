package com.nci.domino.shape.activity.topicon;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.w3c.dom.Element;

import com.nci.domino.help.Functions;

/**
 * �Сͼ�곡������ÿ��������Ӧһ�����ơ����͡���������ͼ�����ɶ���
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityIconSituation {

	/**
	 * ��������
	 */
	private String situationName;

	/**
	 * ��������
	 */
	private String type;

	/**
	 * ������������
	 */
	private WfActivityIconActinListener iconAction;

	/**
	 * ͼ�����ɶ���
	 */
	private WfActivityIconCreator iconCreator;

	public WfActivityIconSituation(Element configEle,boolean resizeable) {
		try {
			situationName = configEle.getAttribute("situation");
			type = configEle.getAttribute("type");
			if (type.equals("class")) {
				String clsName = configEle.getAttribute("path");
				iconCreator = (WfActivityIconCreator) Class.forName(clsName)
						.newInstance();
			} else if (type.equals("image")) {
				iconCreator = new WfActivityImageIconCreator();
				String iconPath = configEle.getAttribute("path");
				BufferedImage image = ImageIO.read(getClass().getResource(iconPath));
				((WfActivityImageIconCreator) iconCreator).setImage(image);
			}
			iconAction = (WfActivityIconActinListener) Class.forName(
					configEle.getAttribute("action")).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSituationName() {
		return situationName;
	}

	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WfActivityIconActinListener getIconAction() {
		return iconAction;
	}

	public void setIconAction(WfActivityIconActinListener iconAction) {
		this.iconAction = iconAction;
	}

	public WfActivityIconCreator getIconCreator() {
		return iconCreator;
	}

	public void setIconCreator(WfActivityIconCreator iconCreator) {
		this.iconCreator = iconCreator;
	}

}
