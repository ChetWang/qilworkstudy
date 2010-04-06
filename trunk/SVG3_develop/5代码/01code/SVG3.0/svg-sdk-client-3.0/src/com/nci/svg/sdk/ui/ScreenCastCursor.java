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
 * 自定义的截图模式下的鼠标形状
 * 
 * @author Qil.Wong
 * 
 */
public class ScreenCastCursor extends Cursor {

	private static final long serialVersionUID = -7185789076519025650L;
	
	/**
	 * 鼠标显示的图像
	 */
	private static Image image;
	
	/**
	 * 初始化完毕标签，true为已经初始化完毕，false表示还未初始化
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
	 * 获取截图模式下的鼠标Cursor对象
	 * @return 截图模式下的鼠标Cursor对象
	 * @throws AWTException
	 * @throws HeadlessException
	 */
	public synchronized static Cursor getScreenCastCursor()
			throws AWTException, HeadlessException {

		return Toolkit.getDefaultToolkit().createCustomCursor(image,
				new Point(12, 12), "screenCastCursor");
	}

}
