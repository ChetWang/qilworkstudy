package com.nci.svg.sdk.module;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.logger.LoggerAdapter;

import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-6
 * @功能：弹出菜单模块基类
 *
 */
public abstract class BusinessRootModuleAdapter extends ModuleAdapter {

	/**
	 * add by yux,2009-3-11
	 * 支持的模块清单
	 */
	private LinkedList<ModuleAdapter> moduleList = new LinkedList<ModuleAdapter>();
	/**
	 * add by yux,2009-3-11
	 * 菜单编号
	 */
	protected String id = null;
	/**
	 * add by yux,2009-3-11
	 * 菜单标签
	 */
	protected String label = null;
	/**
	 * add by yux,2009-3-11
	 * 菜单对象
	 */
	protected JMenu menuItem = null;
	
	/**
	 * add by yux,2009-3-11
	 * 菜单项标识,如为true表示支持菜单项,为false则表示不支持
	 */
	protected boolean menuFlag = false;
	/**
	 * add by yux,2009-3-11
	 * 工具条标识,如为true表示支持工具条,为false则表示不支持
	 */
	protected boolean toolBarFlag = false;
	/**
	 * add by yux,2009-3-11
	 * 右键弹出菜单标识,如为true表示支持右键弹出菜单项,为false则表示不支持
	 */
	protected boolean popupMenuFlag = false;
	public BusinessRootModuleAdapter(EditorAdapter editor) {
		super(editor);
		//首先设置菜单属性
		setMenuInfo();
		//构建菜单项
		createMenuItem();
		//初始化子模块
		initSonModules();
	}
	
	/**
	 * 设置菜单的编号、文字描述
	 * 菜单编号、文字必须要设置，否则本模块不发生作用
	 * 范例:
	 *    menuID = "TQT";
	 *    menuLabel = "台区图";
	 */
	public abstract void setMenuInfo();

	
	/**
	 * 根据输入的图形和选中节点集判定本模块是否需要发生作用
	 * @param handle 图形句柄
	 * @param selectedElements 选中节点集
	 * @return 发生作用返回true，不发生返回false
	 */
	protected abstract boolean verifyValidity(SVGHandle handle,Set<Element> selectedElements);
	
	/**
	 * 新handle创建时，初始化
	 * @param handle 当前新创建的handle
	 */
	protected abstract void initHandle(SVGHandle handle);
	
	/**
	 * 初始化子模块
	 * 必须填写,填写范例如下:
	 *  MainPathShape shape = new MainPathShape(editor);
	 *  this.addModuleAdapter(shape);
	 */
	protected abstract void initSonModules();

	
	/**
	 * 按顺序加入组件实例，以便于生成菜单、工具条进行管理
	 * @param module 组件实例
	 */
	protected void addModuleAdapter(ModuleAdapter module)
	{
		if(module != null)
			moduleList.add(module);
	}
	
	/**
	 * 创建菜单信息，并设置相应的监听
	 */
	private void createMenuItem()
	{
		//校验数据合法性
		if(id == null || id.length() == 0 || label == null || label.length() == 0)
		{
			editor.getLogger().log(this, LoggerAdapter.ERROR, "菜单编号或菜单名称为空，无法生成");
			return;
		}

		//生成菜单
		menuItem = new JMenu(label);
		menuItem.setEnabled(false);

		//设置监听
		final SelectionChangedListener scl = new SelectionChangedListener() {

			@Override
			public void selectionChanged(Set<Element> selectedElements) {

				SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
				boolean b = verifyValidity(handle,selectedElements);
				menuItem.setEnabled(b);
			}
		};
		editor.getHandlesManager().addHandlesListener(new HandlesListener() {
			@Override
			public void handleCreated(SVGHandle currentHandle) {
				initHandle(currentHandle);
				currentHandle.getSelection().addSelectionChangedListener(scl);
				boolean b = verifyValidity(currentHandle,null);
				menuItem.setEnabled(b);
			}

			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				Set<Element> selectedElements = null;
				if(currentHandle != null)
					selectedElements = currentHandle.getSelection().getSelectedElements();
				boolean b = verifyValidity(currentHandle,selectedElements);
				menuItem.setEnabled(b);
			}

		});
	}
	
	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.client.function.ModuleAdapter#getMenuItems()
	 */
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		if(menuItem == null)
			return null;
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		map.put(id, menuItem);
		//遍历模块实例集合，
		for(ModuleAdapter module : moduleList)
		{
			LinkedHashMap<String, JMenuItem> mapMenu = (LinkedHashMap<String, JMenuItem>)module.getMenuItems();
			if(mapMenu != null || mapMenu.size() > 0)
			{
				Iterator<String> iterator = mapMenu.keySet().iterator();
				while(iterator.hasNext())
				{
					JMenuItem item = mapMenu.get(iterator.next());
					menuItem.add(item);
				}
			}
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see com.nci.svg.sdk.client.function.ModuleAdapter#getToolItems()
	 */
	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		// TODO Auto-generated method stub
		// add by yux,2009-3-6
		return super.getToolItems();
	}

	/**
	 * 返回
	 * @return the menuFlag
	 */
	public boolean isMenuFlag() {
		return menuFlag;
	}

	/**
	 * 设置
	 * @param menuFlag the menuFlag to set
	 */
	public void setMenuFlag(boolean menuFlag) {
		this.menuFlag = menuFlag;
	}

	/**
	 * 返回
	 * @return the toolBarFlag
	 */
	public boolean isToolBarFlag() {
		return toolBarFlag;
	}

	/**
	 * 设置
	 * @param toolBarFlag the toolBarFlag to set
	 */
	public void setToolBarFlag(boolean toolBarFlag) {
		this.toolBarFlag = toolBarFlag;
	}

	/**
	 * 返回
	 * @return the popupMenuFlag
	 */
	public boolean isPopupMenuFlag() {
		return popupMenuFlag;
	}

	/**
	 * 设置
	 * @param popupMenuFlag the popupMenuFlag to set
	 */
	public void setPopupMenuFlag(boolean popupMenuFlag) {
		this.popupMenuFlag = popupMenuFlag;
	}

	
}
