package com.nci.svg.sdk.ui;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import fr.itris.glips.svgeditor.resources.ResourcesManager;
/**
 * �Զ���Ľ�ͼģʽ�µ������״
 * 
 * @author Qil.Wong
 * 
 */
public class ScreenCastCursor extends Cursor {

	private static final long serialVersionUID = -7185789076519025650L;
	
	/**
	 * �����ʾ��ͼ��
	 */
	private static Image image;
	
	/**
	 * ��ʼ����ϱ�ǩ��trueΪ�Ѿ���ʼ����ϣ�false��ʾ��δ��ʼ��
	 */
	private static boolean inited = false;

	static {
		if (!inited) {
			image = ResourcesManager.getIcon("nci_screencast_icon", false).getImage();			
			inited = true;
		}
	}

	public ScreenCastCursor(int n) {
		super(n);
	}

	/**
	 * ��ȡ��ͼģʽ�µ����Cursor����
	 * @return ��ͼģʽ�µ����Cursor����
	 * @throws AWTException
	 * @throws HeadlessException
	 */
	public synchronized static Cursor getScreenCastCursor()
			throws AWTException, HeadlessException {

		return Toolkit.getDefaultToolkit().createCustomCursor(image,
				new Point(12, 12), "screenCastCursor");
	}

}
