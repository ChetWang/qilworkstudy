package com.nci.svg.ui.equip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

import org.w3c.dom.Element;

import chrriis.dj.nativeswing.ui.JWebBrowser;
import chrriis.dj.nativeswing.ui.event.WebBrowserAdapter;
import chrriis.dj.nativeswing.ui.event.WebBrowserEvent;
import chrriis.dj.nativeswing.ui.event.WebBrowserNavigationEvent;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class EquipPropery {

	private JWebBrowser browser = null;
	private JToggleButton frameButton;
	private JMenuItem frameMenuItem;
	private String showFrameLabel = "", hideFrameLabel = "";
	private String id;
	// ADD BY ZHOUHM
	// 调用页面的url地址
	private String strUrl = "";
	// END_ADD
	private JLabel infoLabel = new JLabel();
	private PDialog dialog = null;

	private Editor editor;

	public EquipPropery(String id,Editor edi) {
		this.id = id;
		this.editor = edi;
		// dialog = new PDialog();
		// dialog.setVisible(false);
		showFrameLabel = ResourcesManager.bundle.getString("label_hidden_"
				+ id.toLowerCase());
		hideFrameLabel = ResourcesManager.bundle.getString("label_shown_"
				+ id.toLowerCase());
		frameButton = new JToggleButton();
		ImageIcon toolFrameIcon = ResourcesManager
				.getIcon(id + "Window", false);
		frameButton.setIcon(toolFrameIcon);
		frameMenuItem = new JMenuItem();
		ImageIcon icon = ResourcesManager.getIcon(id, false);

		if (icon != null) {

			// this.setFrameIcon(icon);
			frameMenuItem.setIcon(icon);
		}
		frameMenuItem.setText(showFrameLabel);
		frameButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				if (frameButton.isSelected()) {

					frameMenuItem.setText(hideFrameLabel);
					frameButton.setToolTipText(hideFrameLabel);

				} else {

					frameMenuItem.setText(showFrameLabel);
					frameButton.setToolTipText(showFrameLabel);
				}

				// dialog.setVisible(frameButton.isSelected());
				if (!frameButton.isSelected()) {
					if (dialog != null) {
						dialog.dispose();
					}
				} else {
					if (dialog == null) {
						if (editor.getHandlesManager()
								.getCurrentHandle() != null) {
							Set<Element> selectedElements = editor
									.getHandlesManager().getCurrentHandle()
									.getSelection().getSelectedElements();
							int selectedCounts = 0;
							Element neededElement = null;
							if (selectedElements != null) {
								selectedCounts = selectedElements.size();
								if (selectedCounts == 1) {
									neededElement = selectedElements.iterator()
											.next();
								}
							}

							dialog = new PDialog(neededElement, selectedCounts);
						} else {
							dialog = new PDialog(null, 0);
						}
						dialog.setVisible(true);
					}
				}

			}
		});
		frameMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				frameButton.doClick();
			}
		});

	}

	/**
	 * 获取内部属性浏览器
	 * 
	 * @return
	 */
	public JWebBrowser getBrowser() {
		return browser;
	}

	/**
	 * 获取工具条属性按钮
	 * 
	 * @return
	 */
	public JToggleButton getToolBarButton() {
		return frameButton;
	}

	/**
	 * 获取属性菜单
	 * 
	 * @return
	 */
	public JMenuItem getMenuItem() {
		return frameMenuItem;
	}

	private class PDialog extends JDialog {

		private Element neededElement = null;
		private int selectedCounts = 0;

		public PDialog(Element neededElement, int selectedCounts) {
			// super(Editor.getEditor().findParentFrame());
			super(editor.findParentFrame());
			this.neededElement = neededElement;
			this.selectedCounts = selectedCounts;
			// this.setModal(true);
			if (selectedCounts == 0) {
				iniNoneSelected();
			} else if (selectedCounts > 1) {
				iniMoreSelected();
			} else {
				// iniPropDialog();
				iniWebBrowserDialog();
			}
			this.setTitle(ResourcesManager.bundle.getString(id));

			this.setLocationRelativeTo(editor.getDesktop());
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					if (dialog != null) {
						clearDialog();
						if (frameButton.isSelected()) {
							frameButton.doClick();
						}
					}
				}
			});
		}

		private void iniNoneSelected() {
			infoLabel.setText(ResourcesManager.bundle
					.getString("nci_no_selected_object"));
			this.getContentPane().add(infoLabel);
			this.setSize(150, 100);
		}

		private void iniMoreSelected() {
			infoLabel.setText(ResourcesManager.bundle
					.getString("nci_more_selected_object"));
			this.getContentPane().add(infoLabel);
			this.setSize(150, 100);
		}

		/**
		 * 初始化JWebBrowser的函数
		 */
		private void iniWebBrowserDialog() {
			browser = new JWebBrowser();
			browser.setMenuBarVisible(false);
			browser.setAddressBarVisible(true);

			// 注册获取调用网页返回数据事件
			browser.addWebBrowserListener(new WebBrowserAdapter() {
				@Override
				public void commandReceived(WebBrowserEvent e, String command) {
					System.out.println("得到参数:" + command);
				}

				@Override
				public void urlChanged(WebBrowserNavigationEvent e) {

					super.urlChanged(e);
					JWebBrowser jwb = e.getWebBrowser();
					System.out.println("地址：" + e.getNewURL());
					System.out.println("标题：" + e.getWebBrowser().getTitle());
					System.out.println("得到网页：\n" + jwb.getText());
				}

			});

			if (strUrl != null && !strUrl.equals("")) {
				browser.setURL(strUrl); // 设置调用页面的url

			}
			this.getContentPane().add(browser);
			this.setSize(600, 400);

		}
	}

	public void clearDialog() {
		dialog = null;
	}

	public PDialog getDialog() {
		return dialog;
	}

	// ADD BY ZHOUHM
	// *************
	// 设置URL地址
	// *************
	public void setURL(String strUrl) {
		this.strUrl = strUrl;
	}
	// END_ADD
}
