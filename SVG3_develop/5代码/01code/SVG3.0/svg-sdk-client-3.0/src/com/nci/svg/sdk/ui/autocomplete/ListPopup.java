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
 * 自动感知的根据用户输入来进行过滤的弹出列表
 * 
 * @author Qil.Wong
 */
public class ListPopup extends JPopupMenu {

	/**
	 * 弹出列表中嵌套的JList对象
	 */
	private JList list;
	/**
	 * 列表所在的ScroolPane
	 */
	private JScrollPane pane;
	/**
	 * 监听器集合
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
	 * 添加监听器ListSelectionListener
	 * 
	 * @param l
	 *            监听器
	 */
	public void addListSelectionListener(ListSelectionListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * 将指定的位置设置为被选择状态
	 * 
	 * @param index
	 *            指定的位置
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
	 * 获取选择的位置
	 * 
	 * @return 选择的位置
	 */
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	/**
	 * 判断是否选择了
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return list.getSelectedIndex() != -1;
	}

	/**
	 * 将最后一个元素设置为被选择状态
	 */
	public void setLastOneSelected() {
		int count = list.getModel().getSize();
		if (count > 0) {
			list.ensureIndexIsVisible(count - 1);
			list.setSelectedIndex(count - 1);
		}
	}

	/**
	 * 移除列表选择事件ListSelectionListener
	 * 
	 * @param l
	 *            指定的被移除的ListSelectionListener
	 */
	public void removeListSelectionListener(ListSelectionListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	/**
	 * 触发列表值选择变化后的事件
	 * 
	 * @param e
	 *            列表选择事件
	 */
	private void fireValueChanged(ListSelectionEvent e) {
		for (ListSelectionListener l : listeners) {
			l.valueChanged(e);
		}
	}

	/**
	 * 获取弹出列表中的元素个数
	 * 
	 * @return 元素个数
	 */
	public int getItemCount() {
		DefaultListModel model = (DefaultListModel) list.getModel();
		return model.getSize();
	}

	/**
	 * 获取在弹出列表中指定位置的元素对象
	 * 
	 * @param index
	 *            在弹出列表中的位置
	 * @return 元素对象
	 */
	public Object getItem(int index) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		return model.get(index);
	}

	/**
	 * 往弹出列表中添加新的元素
	 * 
	 * @param o
	 *            被添加的新元素
	 */
	public void addItem(Object o) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.addElement(o);
		list.repaint();
	}

	/**
	 * 从弹出列表中将制定的元素移除
	 * 
	 * @param o
	 *            被移除的元素
	 */
	public void removeItem(Object o) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.removeElement(o);
		list.repaint();
	}

	/**
	 * 设置元素集合
	 * 
	 * @param iterable
	 *            可被迭代的元素集合
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
	 * 设置元素集合
	 * 
	 * @param e
	 *            可被枚举的元素集合
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
	 * 设置元素集合
	 * 
	 * @param objects
	 *            元素对象数组
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
	 * 鼠标移到过程中，将鼠标所在的行指定为被选择行
	 * 
	 * @param anEvent
	 *            鼠标事件
	 * @param shouldScroll
	 *            是否该滚动
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
