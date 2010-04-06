package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMSymbolElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.other.LinkPointManager;
import com.nci.svg.sdk.ui.ComboPanel;
import com.nci.svg.sdk.ui.NCIButtonPanel;
import com.nci.svg.sdk.ui.NciCustomDialog;

import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.actions.popup.PopupSubMenu;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-1-5
 * @功能：图元切换操作模块
 * 
 */
public class SymbolSwtichModule extends ModuleAdapter {
	public final static String MODULE_ID = "6cfa746e-e4ee-42eb-8c78-51db73c8780f";
	/**
	 * 模型标识
	 */
	protected static final String id = "SymbolSwtichModule";
	/**
	 * 菜单图标
	 */
	protected Icon menuIcon;

	/**
	 * 菜单标签
	 */
	protected String menuLabel = "";

	/**
	 * 菜单选项
	 */
	protected JMenuItem optionsMenu;

	/**
	 * add by yux,2009-1-6 选择状态的对话框
	 */
	protected NciCustomDialog statusChoose = null;

	protected ComboPanel comboPanel = null;
	
	/**
	 * add by yux,2009-1-6
	 *待操作的节点
	 */
	private Element actionElement = null;
	/**
	 * add by yux,2009-1-6
	 * 待操作的图元名称
	 */
	private String actionSymbolName = null;

	public SymbolSwtichModule(EditorAdapter editor) {
		super(editor);
		createMenuItems();
		statusChoose = new NciCustomDialog(editor.findParentFrame(), true);
		statusChoose.setTitle("请选择需要的状态");

		comboPanel = new ComboPanel();
		comboPanel.getShowText().setText("可选状态");

		statusChoose.addComponent(comboPanel);

		NCIButtonPanel buttonPanel = new NCIButtonPanel();
		buttonPanel.getButtonOK().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swtichAction();
				statusChoose.setVisible(false);
			}
		});

		buttonPanel.getButtonCancel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusChoose.setVisible(false);
			}
		});

		statusChoose.addComponent(buttonPanel);
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.client.function.ModuleAdapter#getPopupItems()
	 */
	@Override
	public Collection<PopupItem> getPopupItems() {
		LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();
			PopupItem item = new PopupItem(editor,id, menuLabel, id) {
				@Override
				public JMenuItem getPopupItem(LinkedList<Element> nodes) {
					menuItem.setEnabled(false);
					if (nodes.size() == 1) {
						Element element = nodes.get(0);
						if (element instanceof SVGOMUseElement)
						{
							menuItem
							.addActionListener(new ActionListener() {
								public void actionPerformed(
										ActionEvent e) {
									doAction();
								}
							});
							menuItem.setEnabled(true);
						}
					}
					return super.getPopupItem(nodes);
				}
			};

		popupItems.add(item);
		return popupItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.function.ModuleAdapter#getMenuItems()
	 */
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		map.put(id, optionsMenu);

		return map;
	}

	/**
	 * 创建刷新管理菜单项
	 */
	protected void createMenuItems() {
		// creating the menu//
		// getting the menu label
		menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");

		// getting the menu icons
		menuIcon = ResourcesManager.getIcon(id, false);

		// creating the menu
		optionsMenu = new JMenuItem(menuLabel);
		optionsMenu.setIcon(menuIcon);
		// 增加处理事件
		ActionListener listener = null;
		listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAction();
			}
		};
		optionsMenu.addActionListener(listener);
		optionsMenu.setEnabled(false);

		// adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager = editor.getHandlesManager();

		svgHandleManager.addHandlesListener(new SymbolSwitchHandlesListener()); 

	}

	/**
	 * 执行全图图元刷新
	 */
	protected void doAction() {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null) {
			return;
		}
		Set<Element> sets = handle.getSelection().getSelectedElements();
		if (sets.size() != 1) {
			return;
		} else {
			Element element = sets.iterator().next();
			if (element instanceof SVGOMUseElement) {

				String href = ((SVGOMUseElement) element).getHref()
						.getBaseVal().toString();
				if (href == null)
					return;
				href = href.substring(1);
				String str[] = href.split(Constants.SYMBOL_STATUS_SEP);
				if (str.length == 1)
					return;

				//遍历该图元所有状态，并以对话框的形式显示选择
				String xpathExpr = "//*[contains(@id ,'"+ str[0] + Constants.SYMBOL_STATUS_SEP + "')]";
				NodeList list = null;
				try {
					list = Utilities.findNodes(xpathExpr, handle.getCanvas()
							.getDocument().getDocumentElement());
				} catch (XPathExpressionException e) {
					list = null;
				}
				String id = null;
				if (list != null) {
					int length = list.getLength();
					ArrayList<String> statusList = new ArrayList<String>();
					for (int i = 0; i < length; i++) {
						if (list.item(i) instanceof SVGOMSymbolElement) {
							id = ((SVGOMSymbolElement) (list.item(i)))
									.getAttribute("id");
							String str2[] = id
									.split(Constants.SYMBOL_STATUS_SEP);
							if (str2.length == 2) {
								statusList.add(str2[1]);
							}
						}
					}
					comboPanel.setSonComboData(statusList);
					actionElement = element;
					actionSymbolName = str[0];
					statusChoose.setLocationRelativeTo(editor
							.findParentFrame());
					statusChoose.setVisible(true);
				}
			}
		}
	}

	/**
	 * add by yux,2009-1-6
	 * 图元切换操作
	 * @return：切换结果
	 */
	public boolean swtichAction() {
        SimpleCodeBean bean = (SimpleCodeBean)comboPanel.getSonCombo().getSelectedItem();
        if(bean == null)
        {
        	return false;
        }
        
        if(actionElement == null || actionSymbolName == null)
        	return false;
        editor.getSvgSession().refreshHandle(actionElement);
        String href = "#" + actionSymbolName + Constants.SYMBOL_STATUS_SEP + bean.getCode();
        actionElement.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",href);
        editor.getSvgSession().refreshHandle(actionElement);
        editor.getSvgSession().refreshCurrentHandleImediately();
        //图线跟随变化
        Set<Element> sets = new HashSet<Element>();
        sets.add(actionElement);
        editor.getHandlesManager().getCurrentHandle()
        .getCanvas().getLpManager().symbolAction(LinkPointManager.SYMBOL_ACTION_MODIFY, sets);
        return true;
	}

	/**
	 * add by yux,2009-1-6
	 * 设置菜单显示状态
	 * @param enable：显示状态
	 */
	protected void setEnabled(boolean enable) {
		optionsMenu.setEnabled(enable);
	}

	protected class SymbolSwitchHandlesListener extends HandlesListener {

		/**
		 * the selection listener for the current handle
		 */
		private SelectionChangedListener selectionChangedListener;

		/**
		 * the last handle
		 */
		private SVGHandle lastHandle;

		@Override
		public void handleChanged(SVGHandle currentHandle,
				Set<SVGHandle> handles) {

			if (currentHandle != null) {

				// removing the last selection changed listener
				if (selectionChangedListener != null && lastHandle != null
						&& lastHandle.getSelection() != null) {

					lastHandle.getSelection().removeSelectionChangedListener(
							selectionChangedListener);
				}

				selectionChanged(currentHandle);

				// adding a new selection changed listener
				selectionChangedListener = new SelectionChangedListener() {

					@Override
					public void selectionChanged(Set<Element> selectedElements) {

						SymbolSwitchHandlesListener.this.selectionChanged(editor
								.getHandlesManager().getCurrentHandle());
					}
				};

				currentHandle.getSelection().addSelectionChangedListener(
						selectionChangedListener);

			} else {

				selectionChangedListener = null;
				setEnabled(false);
			}

			this.lastHandle = currentHandle;
		}

		/**
		 * called when the selection of the provided handle has changed
		 * 
		 * @param currentHandle
		 *            the current svg handle
		 */
		protected void selectionChanged(SVGHandle currentHandle) {

			if (currentHandle != null) {

				// getting the set of the selected elements
				Set<Element> selectedElements = currentHandle.getSelection()
						.getSelectedElements();

				setEnabled(false);
				Set<Element> sets = currentHandle.getSelection()
						.getSelectedElements();
				if (sets.size() != 1) {
					setEnabled(false);
				} else {
					Element element = sets.iterator().next();
					if (element instanceof SVGOMUseElement)
						setEnabled(true);
					else
						setEnabled(false);
				}

			}
		}
	}
}
