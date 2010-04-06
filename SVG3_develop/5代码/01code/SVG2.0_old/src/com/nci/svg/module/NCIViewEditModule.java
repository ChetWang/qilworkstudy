package com.nci.svg.module;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.xml.xpath.XPathExpressionException;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class NCIViewEditModule extends ModuleAdapter {

	private JToggleButton btn;

	public static final String NCI_View_Edit_ModuleID = "view_edit";

	public static final String VIEW_MODE = "view";

	public static final String EDIT_MODE = "edit";

	public static final String BOTH_VIEW_EDIT_MODE = "view-edit";

	private ImageIcon viewIcon;

	private ImageIcon editIcon;

	private String ve_mode = EDIT_MODE;

	public NCIViewEditModule(Editor editor) {
		super(editor);
		btn = new JToggleButton();
		btn.setEnabled(true);
		viewIcon = ResourcesManager.getIcon("nci_view", false);
		editIcon = ResourcesManager.getIcon("nci_edit", false);
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		btn.setToolTipText(ResourcesManager.bundle
				.getString("nci_View_Edit_SetView_tooltip"));

		btn.setIcon(viewIcon);
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				setViewEdit(btn.isSelected() ? VIEW_MODE : EDIT_MODE);
			}

		});
		HashMap<String, AbstractButton> toolItems = new HashMap<String, AbstractButton>();
		toolItems.put(NCI_View_Edit_ModuleID, btn);
		return toolItems;
	}

	public String getName() {
		return NCI_View_Edit_ModuleID;
	}

	public String getViewEdit_mode() {
		return ve_mode;
	}

	/**
	 * 模式切换时，工具条的变化控制
	 * 
	 * @param ve_mode
	 *            具体的模式，
	 * @throws XPathExpressionException
	 */
	private void handleToolBar(String ve_mode) throws XPathExpressionException {
		boolean isViewMode = ve_mode.equals(VIEW_MODE);
		Document toolDoc = ResourcesManager.getXMLDocument("tool.xml");
		btn.setIcon(isViewMode ? editIcon : viewIcon);
		btn.setSelected(isViewMode);
		btn.setToolTipText(isViewMode ? ResourcesManager.bundle
				.getString("nci_View_Edit_SetEdit_tooltip")
				: ResourcesManager.bundle
						.getString("nci_View_Edit_SetView_tooltip"));
		NodeList modeElements = Utilities.findNodes("//*[@mode]", toolDoc
				.getDocumentElement());
		for (int i = 0; i < modeElements.getLength(); i++) {
			Element btnEle = (Element) modeElements.item(i);
			AbstractButton toolBtn = editor.getToolBarManager().getToolItems()
					.get(btnEle.getAttribute("name"));
			if (toolBtn != null) {
				String mode = btnEle.getAttribute("mode");
				if (mode.equals(VIEW_MODE)) {
					toolBtn.setVisible(isViewMode);
				} else if (mode.equals(EDIT_MODE)) {
					toolBtn.setVisible(!isViewMode);
				}
			}

		}
		Component[] comps = ((JToolBar) editor.getToolBarManager()
				.getToolsBar()).getComponents();
		for (int n = 0; n < comps.length - 1; n++) {
			if (comps[n] instanceof JToolBar) {
				Component[] compChildren = ((JToolBar) comps[n])
						.getComponents();
				boolean flag = true;
				for (int i = 0; i < compChildren.length; i++) {
					if (compChildren[i].isVisible()) {
						flag = false;
						break;
					}
				}
				comps[n].setVisible(!flag);
			}
		}
		handlePlatte(ve_mode);
	}

	/**
	 * 控制组件面板
	 */
	private void handlePlatte(String ve_mode) {
		NCICustomGraphModule cgModule = (NCICustomGraphModule) editor
				.getModule("NciSvgCustomUnit");
		if (ve_mode.equals(VIEW_MODE)) {
			cgModule.hidePlatte();
		} else if (ve_mode.equals(EDIT_MODE)) {
			cgModule.showPlatte();
		}
	}

	/**
	 * 模式切换时，菜单条的变化控制
	 * 
	 * @param ve_mode
	 *            具体的模式，
	 * @throws XPathExpressionException
	 */
	private void handleMenuBar(String ve_mode) throws XPathExpressionException {
		boolean isViewMode = ve_mode.equals(VIEW_MODE);
		Document toolDoc = ResourcesManager.getXMLDocument("menu.xml");
		NodeList modeElements = Utilities.findNodes("//*[@mode]", toolDoc
				.getDocumentElement());
		Element menuEle = null;
		JComponent menuObj = null;
		String mode = null;

		for (int i = 0; i < modeElements.getLength(); i++) {
			menuEle = (Element) modeElements.item(i);
			if (menuEle.getNodeName().equals("menu")) {
				menuObj = editor.getMenuBar().getMenu(
						menuEle.getAttribute("name"));

			} else if (menuEle.getNodeName().equals("menuitem")) {
				menuObj = editor.getMenuBar().getMenuItems().get(
						menuEle.getAttribute("name"));
			}
			mode = menuEle.getAttribute("mode");
			if (menuObj == null)
				continue;
			if (mode.equals(VIEW_MODE)) {
				menuObj.setVisible(isViewMode);
			} else if (mode.equals(EDIT_MODE)) {
				menuObj.setVisible(!isViewMode);
			}
		}
		Iterator<JMenu> it = editor.getMenuBar().getMenus().values().iterator();
		while (it.hasNext()) {
			this.handleSeparators(it.next().getMenuComponents());
		}
	}

	/**
	 * 模式切换时，弹出菜单的变化控制
	 * 
	 * @param ve_mode
	 *            具体的模式，
	 * @throws XPathExpressionException
	 */
	private void handlePopupMenu(String ve_mode)
			throws XPathExpressionException {
		boolean isViewMode = ve_mode.equals(VIEW_MODE);
		Map<String, PopupItem> popupItems = editor.getPopupManager()
				.getPopupItems();
		NodeList modeElements = Utilities.findNodes("//*[@mode]",
				ResourcesManager.getXMLDocument("popup.xml")
						.getDocumentElement());
		for (int i = 0; i < modeElements.getLength(); i++) {
			Element popupEle = (Element) modeElements.item(i);
			if (popupEle != null) {
				PopupItem pop = popupItems.get(popupEle.getAttribute("name"));
				String mode = popupEle.getAttribute("mode");
				LinkedList<Element> nodes = new LinkedList<Element>();
				if (mode.equals(VIEW_MODE)) {
					pop.getPopupItem(nodes).setVisible(isViewMode);

				} else if (mode.equals(EDIT_MODE)) {
					pop.getPopupItem(nodes).setVisible(!isViewMode);
				}
			}
		}
	}

	/**
	 * 设置模式
	 * 
	 * @param ve_mode
	 *            参见VIEW_MODE、EDIT_MODE、BOTH_VIEW_EDIT_MODE
	 */
	public void setViewEdit(String mode) {
		this.ve_mode = mode;
		SwingWorker worker = new SwingWorker(){

			@Override
			protected Object doInBackground() throws Exception {
				getEditor().getSelectionManager().setToRegularMode();
				try {
					handleToolBar(ve_mode);
					handleMenuBar(ve_mode);
					handlePopupMenu(ve_mode);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
		};
		worker.execute();		
	}

	/**
	 * 处理菜单中的分隔符
	 * 
	 * @param menuComps
	 *            指定菜单中的所有子组件集合
	 */
	public void handleSeparators(Component[] menuComps) {
		ArrayList<Component> visibleComponents = new ArrayList<Component>();
		for (int i = 0; i < menuComps.length - 1; i++) {
			boolean allMenuInvisible = true;
			int k = 1;
			boolean isJMenu = menuComps[i] instanceof JMenuItem;
			while (true) {
				if (!isJMenu) {
					Component next = menuComps[i + k];
					boolean isNextJMenu = next instanceof JMenuItem;
					if (next.isVisible() && isNextJMenu) {
						allMenuInvisible = false;
						break;
					}
					if (!isNextJMenu)
						break;
				} else {
					break;
				}
				k++;
				if ((i + k) > menuComps.length - 1)
					break;
			}
			if (!isJMenu) {
				menuComps[i].setVisible(!allMenuInvisible);
			}
			if (menuComps[i].isVisible())
				visibleComponents.add(menuComps[i]);

		}
		// 最后一个不能落下，上面的循环只是循环到倒数第二个
		if (menuComps.length > 0)
			if (menuComps[menuComps.length - 1].isVisible())
				visibleComponents.add(menuComps[menuComps.length - 1]);
		// 前面的处理只是将中间的Separator隐藏，接下来要处理两端的Separator
		if (visibleComponents.size() > 0) {
			if (visibleComponents.get(0) instanceof JPopupMenu.Separator) {
				visibleComponents.get(0).setVisible(false);
			}
			if (visibleComponents.get(visibleComponents.size() - 1) instanceof JPopupMenu.Separator) {
				visibleComponents.get(visibleComponents.size() - 1).setVisible(
						false);
			}
		}
	}
	
	public JToggleButton getToggleButton(){
		return btn;
	}

}
