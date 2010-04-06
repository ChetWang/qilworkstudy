package com.nci.svg.ui;

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
 * 自定义的截图鼠标
 * 
 * @author Qil.Wong
 * 
 */
public class ScreenCastCursor extends Cursor {

	private static final long serialVersionUID = -7185789076519025650L;
	private static Image image;
	private static boolean inited = false;

	static {
		if (!inited) {
//			String path = ResourcesManager
//					.getPath("icons/screencast_cursor.png");
//			System.out.println("cursor path:"+path);
//			String ab = "";
//			try {
//				ab = new File(new URI(path)).getPath();
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("ab:"+ab);
			image = ResourcesManager.getIcon("nci_screencast_icon", false).getImage();
			
			inited = true;
		}
	}

	public ScreenCastCursor(int n) {
		super(n);
	}

	public synchronized static Cursor getScreenCastCursor()
			throws AWTException, HeadlessException {

		return Toolkit.getDefaultToolkit().createCustomCursor(image,
				new Point(12, 12), "screenCastCursor");
	}

}
