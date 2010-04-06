package fr.itris.glips.svgeditor;

import java.awt.event.*;
import javax.swing.*;

import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.resources.*;

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
		final EditorAdapter editor = null;
		// creating the parent frame of the editor
		final JFrame mainFrame = new JFrame();
		this.setFrame(mainFrame);
		mainFrame.setTitle("SVG Toolkit");
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
		long start = System.nanoTime();
//		editor.init(mainFrame, fileName, true, true, false, true, null);
		System.out.println("init 被注释了");
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
