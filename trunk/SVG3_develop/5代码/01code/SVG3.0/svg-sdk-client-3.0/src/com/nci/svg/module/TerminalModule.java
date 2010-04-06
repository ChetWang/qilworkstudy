/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-3
 * @���ܣ����˵������
 *
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.ui.terminal.TerminalDialog;

import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author yx.nci
 *
 */
public class TerminalModule extends ModuleAdapter {
	/**
	 * ģ�ͱ�ʶ
	 */
	protected static final String id = "TerminalModule";
	/**
	 * �˵�ͼ��
	 */
	protected Icon menuIcon;

	/**
	 * �˵���ǩ
	 */
	protected String menuLabel = "";

	/**
	 * �˵�ѡ��
	 */
	protected JMenuItem optionsMenu;
	
	/**
	 * ���ĵ�
	 */
	public static final String CENTER_POINT = "centerPoint";
	
	/**
	 * �����޵�
	 */
	public static final String FOURWAY_POINT = "fourwayPoint";
	
	/**
	 * ���Ҽ��޵�
	 */
	public static final String LEFTANDRIGHT_POINT = "leftAndRightPoint";
	
	/**
	 * ���¼��޵�
	 */
	public static final String UPANDDOWN_POINT = "upAndDownPoint";
	
	/**
	 * б�����޵�
	 */
	public static final String INCLINE_FOURWAY_POINT= "inclineFourwayPoint";
	
	/**
	 * add by yux,2009-1-7
	 * �ֹ�ѡ�����˵�
	 */
	public static final String MANUAL_TERMINAL= "manual";
	
	/**
	 * add by yux,2009-1-7
	 * �����������˵㶨��ڵ�����
	 */
	public static final String DEFS_TERMINIAL_NCINAME= "nci:nci-terminal";
	
	/**
	 * add by yux,2009-1-7
	 * ���˵�����
	 */
	public static final String DEFS_TERMINIAL_NAME= "terminal-name";
	
	/**
	 * add by yux,2009-1-7
	 * ���˵���������
	 */
	public static final String DEFS_TERMINIAL_NOTE = "terminal-note";
	
	/**
	 * add by yux,2009-1-7
	 * �������˵�
	 */
	public static final String CENTER_TERMINAL_NAME = "nci_c";
	
	/**
	 * add by yux,2009-1-7
	 * �Ҳ����˵�
	 */
	public static final String EAST_TERMINAL_NAME = "nci_e";
	
	/**
	 * add by yux,2009-1-7
	 * ������˵�
	 */
	public static final String WEST_TERMINAL_NAME = "nci_w";
	
	/**
	 * add by yux,2009-1-7
	 * �ϲ����˵�
	 */
	public static final String NORTH_TERMINAL_NAME = "nci_n";
	
	/**
	 * add by yux,2009-1-7
	 * �²����˵�
	 */
	public static final String SOUTH_TERMINAL_NAME = "nci_s";
	
	/**
	 * add by yux,2009-1-7
	 * ���ϲ����˵�
	 */
	public static final String EASTNORTH_TERMINAL_NAME = "nci_en";
	
	/**
	 * add by yux,2009-1-7
	 * ���ϲ����˵�
	 */
	public static final String WESTNORTH_TERMINAL_NAME = "nci_wn";
	
	/**
	 * add by yux,2009-1-7
	 * ���²����˵�
	 */
	public static final String EASTSOUTH_TERMINAL_NAME = "nci_es";
	
	/**
	 * add by yux,2009-1-7
	 * ���²����˵�
	 */
	public static final String WESTSOUTH_TERMINAL_NAME = "nci_ws";
	
	/**
	 * add by yux,2009-1-7
	 * ���˵�����
	 */
	public static final String NCI_TYPE_TERMINAL = "terminal";
	public TerminalModule(EditorAdapter editor)
	{
	   super(editor);	
	   createMenuItems();
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		map.put(id, optionsMenu);

		return map;
	}
	
	/**
	 * �������˵����˵���
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
		//���Ӵ����¼�
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

		svgHandleManager.addHandlesListener(new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				if(currentHandle == null)
				{
					optionsMenu.setEnabled(false);
				}
				else
				{
					if(currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
						|| currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE)
					    optionsMenu.setEnabled(true);
					else
						optionsMenu.setEnabled(false);
				}

			}
		});

	}
	
	/**
	 * ִ�в�������ʾ���˵����Ի���
	 */
	protected void doAction()
	{
		TerminalDialog dialog = new TerminalDialog(editor,editor.findParentFrame(),false);
		dialog.setTitle(menuLabel);
		dialog.setSize(350,280);
		dialog.setLocationRelativeTo(editor.findParentFrame());
		dialog.setVisible(true);
	}

}
