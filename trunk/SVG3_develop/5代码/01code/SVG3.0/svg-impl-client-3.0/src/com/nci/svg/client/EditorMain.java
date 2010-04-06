package com.nci.svg.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the enter point of the application
 * 
 * @author ITRIS, Jordi SUC
 */
public class EditorMain {

	/**
	 * the constructor of the class
	 * 
	 * @param fileName
	 *            the name of a svg file
	 */
	public EditorMain(final String fileName) {
		long start = System.nanoTime();
		final Editor editor = new Editor("1");
		// creating the parent frame of the editor
		final JFrame mainFrame = new JFrame();
		this.setFrame(mainFrame);
		mainFrame.setTitle("新世纪SVG图形服务平台");
		// handling the close case
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent evt) {
				editor.exit();
				// NciSvgApplet.isOpened = false;
			}
		});

		// setting the icon
		ImageIcon icon2 = ResourcesManager.getIcon("Editor", false);

		if (icon2 != null && icon2.getImage() != null) {

			mainFrame.setIconImage(icon2.getImage());
		}
		
		editor.init(mainFrame, fileName, true, true, false, true, null);
		System.out.println("初始化整个编辑器耗时：" + (System.nanoTime() - start)
				/ 1000000l + "ms");
		
	}

	/**
	 * the main method
	 * 
	 * @param args
	 *            the array of arguments
	 */
	public static void main(String[] args) {

		String fileName = "";
//		 String fileName="file:/C:/x.svg";

		if (args != null && args.length > 0) {

			fileName = args[0];
		}

		final String ffileName = fileName;

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				new EditorMain(ffileName);
			}
		});
	}

	JFrame f = null;

	public void setFrame(JFrame frame) {
		this.f = frame;
	}

	public JFrame getFrame() {
		return f;
	}

}
