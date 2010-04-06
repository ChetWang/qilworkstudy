package com.nci.svg.sdk.client.browser;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.NSOption;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.module.DefaultModuleAdapter;

/**
 * @author qi
 *
 */
public class BrowserModule extends ModuleAdapter {

	private static final long serialVersionUID = 6591648031109122932L;

	public static String MODULE_ID = "ef06b699-0638-4643-ab79-b05af6aae167";

	private JWebBrowser dialogBrowser = null;

	private JDialog dialog = null;

	public String getModuleType() {
		return "browser";
	}

	public BrowserModule(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
	}

	@Override
	public int init() {
		NativeInterface.runEventPump();
		NativeInterface.open();
		return DefaultModuleAdapter.MODULE_INITIALIZE_COMPLETE;
	}

	/**
	 * @param url
	 */
	public void showBrowserDialog(String url) {
		// if (dialog == null) {
		Frame parentComp = new JFrame("Xx");
		JDialog dialog = new JDialog(parentComp, false);
		dialog.setSize(800, 600);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// if (dialogBrowser == null) {
		JWebBrowser dialogBrowser = new JWebBrowser(new NSOption[0]);
		dialogBrowser.setMenuBarVisible(false);
		dialogBrowser.setButtonBarVisible(false);
		dialogBrowser.setLocationBarVisible(false);
		// }
		dialog.getContentPane().add(dialogBrowser);
		dialog.setLocationRelativeTo(parentComp);
		// }
		dialogBrowser.navigate(url);
		dialog.setTitle(url);

		dialog.setVisible(true);
	}
	
	public static void main(String[] xxx){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				BrowserModule m = new BrowserModule(null);
				m.init();
				m.showBrowserDialog("www.nci.com.cn");
			}
		});
		
	}

}
