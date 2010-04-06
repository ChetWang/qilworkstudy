/*
 * ListPopup.java
 *
 * Created on 2007-6-21, 22:14:33
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.sdk.ui.autocomplete;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

import com.nci.svg.sdk.client.util.Utilities;

/**
 * �Զ���֪�ĸ����û����������й��˵ĵ����б�
 * 
 * @author Qil.Wong
 */
public class ListPopup extends JPopupMenu {

	/**
	 * �����б���Ƕ�׵�JList����
	 */
	private JList list;
	/**
	 * �б����ڵ�ScroolPane
	 */
	private JScrollPane pane;
	/**
	 * ����������
	 */
	private ArrayList<ListSelectionListener> listeners = new ArrayList<ListSelectionListener>();

	public ListPopup() {
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				init();
			}
		});
	}

	private void init() {
		setLayout(new BorderLayout());
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new DefaultListModel());
		pane = new JScrollPane(list);
		pane.setBorder(null);
		add(pane, BorderLayout.CENTER);
		list.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent evt) {
				if (evt.getSource() == list) {
					Point location = evt.getPoint();
					Rectangle r = new Rectangle();
					list.computeVisibleRect(r);
					if (r.contains(location)) {
						updateListBoxSelectionForEvent(evt, false);
					}
				}
			}
		});
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent evt) {
				if (list.getSelectedIndex() != -1) {
					fireValueChanged(new ListSelectionEvent(list, list
							.getSelectedIndex(), list.getSelectedIndex(), true));
				}
			}
		});
	}

	/**
	 * ��Ӽ�����ListSelectionListener
	 * 
	 * @param l
	 *            ������
	 */
	public void addListSelectionListener(ListSelectionListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * ��ָ����λ������Ϊ��ѡ��״̬
	 * 
	 * @param index
	 *            ָ����λ��
	 */
	public void setSelectedIndex(int index) {
		if (index >= list.getModel().getSize()) {
			index = 0;
		}
		if (index < 0) {
			index = list.getModel().getSize() - 1;
		}
		list.ensureIndexIsVisible(index);
		list.setSelectedIndex(index);
	}

	public Object getSelectedValue() {
		return list.getSelectedValue();
	}

	/**
	 * ��ȡѡ���λ��
	 * 
	 * @return ѡ���λ��
	 */
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	/**
	 * �ж��Ƿ�ѡ����
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return list.getSelectedIndex() != -1;
	}

	/**
	 * �����һ��Ԫ������Ϊ��ѡ��״̬
	 */
	public void setLastOneSelected() {
		int count = list.getModel().getSize();
		if (count > 0) {
			list.ensureIndexIsVisible(count - 1);
			list.setSelectedIndex(count - 1);
		}
	}

	/**
	 * �Ƴ��б�ѡ���¼�ListSelectionListener
	 * 
	 * @param l
	 *            ָ���ı��Ƴ���ListSelectionListener
	 */
	public void removeListSelectionListener(ListSelectionListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	/**
	 * �����б�ֵѡ��仯����¼�
	 * 
	 * @param e
	 *            �б�ѡ���¼�
	 */
	private void fireValueChanged(ListSelectionEvent e) {
		for (ListSelectionListener l : listeners) {
			l.valueChanged(e);
		}
	}

	/**
	 * ��ȡ�����б��е�Ԫ�ظ���
	 * 
	 * @return Ԫ�ظ���
	 */
	public int getItemCount() {
		DefaultListModel model = (DefaultListModel) list.getModel();
		return model.getSize();
	}

	/**
	 * ��ȡ�ڵ����б���ָ��λ�õ�Ԫ�ض���
	 * 
	 * @param index
	 *            �ڵ����б��е�λ��
	 * @return Ԫ�ض���
	 */
	public Object getItem(int index) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		return model.get(index);
	}

	/**
	 * �������б�������µ�Ԫ��
	 * 
	 * @param o
	 *            ����ӵ���Ԫ��
	 */
	public void addItem(Object o) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.addElement(o);
		list.repaint();
	}

	/**
	 * �ӵ����б��н��ƶ���Ԫ���Ƴ�
	 * 
	 * @param o
	 *            ���Ƴ���Ԫ��
	 */
	public void removeItem(Object o) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.removeElement(o);
		list.repaint();
	}

	/**
	 * ����Ԫ�ؼ���
	 * 
	 * @param iterable
	 *            �ɱ�������Ԫ�ؼ���
	 */
	public void setList(Iterable iterable) {
		DefaultListModel model = new DefaultListModel();
		for (Object o : iterable) {
			model.addElement(o);
		}
		list.setModel(model);
		list.repaint();
	}

	/**
	 * ����Ԫ�ؼ���
	 * 
	 * @param e
	 *            �ɱ�ö�ٵ�Ԫ�ؼ���
	 */
	public void setList(Enumeration e) {
		DefaultListModel model = new DefaultListModel();
		while (e.hasMoreElements()) {
			model.addElement(e.nextElement());
		}
		list.setModel(model);
		list.repaint();
	}

	/**
	 * ����Ԫ�ؼ���
	 * 
	 * @param objects
	 *            Ԫ�ض�������
	 */
	public void setList(Object... objects) {
		DefaultListModel model = new DefaultListModel();
		for (Object o : objects) {
			model.addElement(o);
		}
		list.setModel(model);
		list.repaint();
	}

	/**
	 * ����Ƶ������У���������ڵ���ָ��Ϊ��ѡ����
	 * 
	 * @param anEvent
	 *            ����¼�
	 * @param shouldScroll
	 *            �Ƿ�ù���
	 */
	protected void updateListBoxSelectionForEvent(MouseEvent anEvent,
			boolean shouldScroll) {

		Point location = anEvent.getPoint();
		if (list == null) {
			return;
		}
		int index = list.locationToIndex(location);
		if (index == -1) {
			if (location.y < 0) {
				index = 0;
			} else {
				index = list.getModel().getSize() - 1;
			}
		}
		if (list.getSelectedIndex() != index) {
			list.setSelectedIndex(index);
			if (shouldScroll) {
				list.ensureIndexIsVisible(index);
			}
		}
	}
}
