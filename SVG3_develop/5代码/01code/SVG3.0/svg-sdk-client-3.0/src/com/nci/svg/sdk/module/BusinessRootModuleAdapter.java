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
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-6
 * @���ܣ������˵�ģ�����
 *
 */
public abstract class BusinessRootModuleAdapter extends ModuleAdapter {

	/**
	 * add by yux,2009-3-11
	 * ֧�ֵ�ģ���嵥
	 */
	private LinkedList<ModuleAdapter> moduleList = new LinkedList<ModuleAdapter>();
	/**
	 * add by yux,2009-3-11
	 * �˵����
	 */
	protected String id = null;
	/**
	 * add by yux,2009-3-11
	 * �˵���ǩ
	 */
	protected String label = null;
	/**
	 * add by yux,2009-3-11
	 * �˵�����
	 */
	protected JMenu menuItem = null;
	
	/**
	 * add by yux,2009-3-11
	 * �˵����ʶ,��Ϊtrue��ʾ֧�ֲ˵���,Ϊfalse���ʾ��֧��
	 */
	protected boolean menuFlag = false;
	/**
	 * add by yux,2009-3-11
	 * ��������ʶ,��Ϊtrue��ʾ֧�ֹ�����,Ϊfalse���ʾ��֧��
	 */
	protected boolean toolBarFlag = false;
	/**
	 * add by yux,2009-3-11
	 * �Ҽ������˵���ʶ,��Ϊtrue��ʾ֧���Ҽ������˵���,Ϊfalse���ʾ��֧��
	 */
	protected boolean popupMenuFlag = false;
	public BusinessRootModuleAdapter(EditorAdapter editor) {
		super(editor);
		//�������ò˵�����
		setMenuInfo();
		//�����˵���
		createMenuItem();
		//��ʼ����ģ��
		initSonModules();
	}
	
	/**
	 * ���ò˵��ı�š���������
	 * �˵���š����ֱ���Ҫ���ã�����ģ�鲻��������
	 * ����:
	 *    menuID = "TQT";
	 *    menuLabel = "̨��ͼ";
	 */
	public abstract void setMenuInfo();

	
	/**
	 * ���������ͼ�κ�ѡ�нڵ㼯�ж���ģ���Ƿ���Ҫ��������
	 * @param handle ͼ�ξ��
	 * @param selectedElements ѡ�нڵ㼯
	 * @return �������÷���true������������false
	 */
	protected abstract boolean verifyValidity(SVGHandle handle,Set<Element> selectedElements);
	
	/**
	 * ��handle����ʱ����ʼ��
	 * @param handle ��ǰ�´�����handle
	 */
	protected abstract void initHandle(SVGHandle handle);
	
	/**
	 * ��ʼ����ģ��
	 * ������д,��д��������:
	 *  MainPathShape shape = new MainPathShape(editor);
	 *  this.addModuleAdapter(shape);
	 */
	protected abstract void initSonModules();

	
	/**
	 * ��˳��������ʵ�����Ա������ɲ˵������������й���
	 * @param module ���ʵ��
	 */
	protected void addModuleAdapter(ModuleAdapter module)
	{
		if(module != null)
			moduleList.add(module);
	}
	
	/**
	 * �����˵���Ϣ����������Ӧ�ļ���
	 */
	private void createMenuItem()
	{
		//У�����ݺϷ���
		if(id == null || id.length() == 0 || label == null || label.length() == 0)
		{
			editor.getLogger().log(this, LoggerAdapter.ERROR, "�˵���Ż�˵�����Ϊ�գ��޷�����");
			return;
		}

		//���ɲ˵�
		menuItem = new JMenu(label);
		menuItem.setEnabled(false);

		//���ü���
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
		//����ģ��ʵ�����ϣ�
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
	 * ����
	 * @return the menuFlag
	 */
	public boolean isMenuFlag() {
		return menuFlag;
	}

	/**
	 * ����
	 * @param menuFlag the menuFlag to set
	 */
	public void setMenuFlag(boolean menuFlag) {
		this.menuFlag = menuFlag;
	}

	/**
	 * ����
	 * @return the toolBarFlag
	 */
	public boolean isToolBarFlag() {
		return toolBarFlag;
	}

	/**
	 * ����
	 * @param toolBarFlag the toolBarFlag to set
	 */
	public void setToolBarFlag(boolean toolBarFlag) {
		this.toolBarFlag = toolBarFlag;
	}

	/**
	 * ����
	 * @return the popupMenuFlag
	 */
	public boolean isPopupMenuFlag() {
		return popupMenuFlag;
	}

	/**
	 * ����
	 * @param popupMenuFlag the popupMenuFlag to set
	 */
	public void setPopupMenuFlag(boolean popupMenuFlag) {
		this.popupMenuFlag = popupMenuFlag;
	}

	
}
