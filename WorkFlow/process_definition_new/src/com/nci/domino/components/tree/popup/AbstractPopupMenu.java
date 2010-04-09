package com.nci.domino.components.tree.popup;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JList;
import javax.swing.JMenuItem;

import com.jidesoft.swing.JidePopupMenu;
import com.nci.domino.components.tree.WfTree;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * �������������Ҽ��˵�����
 * 
 * @author Qil.Wong
 * 
 */
public abstract class AbstractPopupMenu extends JidePopupMenu implements
		ActionListener {

	private static final long serialVersionUID = -6575862380934494199L;
	protected static final String SEPARATOR = "SEPARATOR";

	protected abstract List<Enum> getCommands();

	// protected

	protected WfTree tree;

	protected JList list;

	public AbstractPopupMenu(WfTree tree) {
		this.tree = tree;
		init();
	}

	public AbstractPopupMenu(JList list) {
		this.list = list;
		init();
	}

	/**
	 * ��ʼ�������˵�
	 */
	protected void init() {
		List<Enum> enums = getCommands();
		for (int i = 0; i < enums.size(); i++) {
			String command = enums.get(i).name();
			if (command.indexOf(SEPARATOR) >= 0) {
				addSeparator();
			} else {
				JMenuItem menu = new JMenuItem(WofoResources
						.getValueByKey(command), Functions.getImageIcon(command
						+ ".gif"));
				menu.addActionListener(this);
				menu.setActionCommand(command);
				add(menu);
			}
		}
	}

	public WfTree getWfTree() {
		return tree;
	}

	public JList getWfList() {
		return list;
	}

	/**
	 * ���ݽڵ����Ӳ˵���Ч��
	 */
	public void checkMenus() {
		// Ĭ�ϲ�����飬��������ȥʵ��
	}

	public void show(Component invoker, int x, int y) {
		checkMenus();
		super.show(invoker, x, y);
	}

}
