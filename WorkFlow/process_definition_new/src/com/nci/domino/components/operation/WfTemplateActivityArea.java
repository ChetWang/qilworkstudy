package com.nci.domino.components.operation;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoSimpleSet;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.help.BoardOpenListener;
import com.nci.domino.help.Functions;
import com.nci.domino.help.pinyin.PinyinHelper;
import com.nci.domino.shape.WfActivity;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.PaintBoardShapeSelectionListener;

/**
 * ĸ�����������
 * 
 * @author Qil.Wong
 * 
 */
public class WfTemplateActivityArea extends WfOperationSimpleSetArea {

	public WfTemplateActivityArea(WfOperationArea operationArea,
			WofoSimpleSet simpleSetBean) {
		super(operationArea, simpleSetBean);
		simpleSetList.setCellRenderer(new ActListCellRenderer());
		buttonPanel.removeDefaultListeners(buttonPanel.getAddRowBtn());
		buttonPanel.removeDefaultListeners(buttonPanel.getDelRowBtn());
		buttonPanel.getAddRowBtn().setVisible(false);
		buttonPanel.getDelRowBtn().setVisible(false);
		// ÿ��һ����壬����������ͼ��ѡ�����������ͼ��ѡ��Ҫ�뵱ǰlist�еĶ�Ӧ
		operationArea.addBoardOpenListener(new BoardOpenListener() {

			public void boardOpen(PaintBoardBasic board) {
				board
						.addPaintBoardShapeSelectionListener(new PaintBoardShapeSelectionListener() {

							public void shapeSelectionStateChanged(
									List<AbstractShape> currentSelectedShapes,
									List<AbstractShape> changedShapes) {
								simpleSetList.clearSelection();
								simpleSetList.updateUI();
							}
						});
			}
		});
		// �б��ѡ��ҲҪ�ܷ�ӳ��ͼ����
		simpleSetList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				WfTemplateActivityArea.this.operationArea
						.getCurrentPaintBoard().setAllUnselected(false);
//				Object[] os = simpleSetList.getSelectedValues();
//				for (Object o : os) {
//					((WfActivity) o).setSelected(true);
//				}
//				WfTemplateActivityArea.this.operationArea
//						.getCurrentPaintBoard().repaint();
			}
		});
		
		simpleSetList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					showActivityProperties();
				}
			}
		});
	}

	/**
	 * ��ʾ�����
	 */
	private void showActivityProperties() {
		WofoProcessBean process = operationArea.getCurrentProcess();
		PaintBoard board = null;
		if (process != null) {
			board = operationArea.getBoards().get(process.getID());
			if (board != null) {
				((WfActivity) simpleSetList.getSelectedValue())
						.setActivityProperty(board);
			}
		}
	}

	/**
	 * �ڵ����ڰ��л����Ӱ��
	 * 
	 * @param newNode
	 */
	@Override
	public void treeSelectionChanged(TreeSelectionEvent e) {
		clearContent();
		WofoProcessBean process = operationArea.getCurrentProcess();
		PaintBoard board = null;
		if (process != null) {
			board = operationArea.getBoards().get(process.getID());
			if (board != null) {
				List<WfActivity> acts = new ArrayList<WfActivity>();
				for (int i = 0; i < board.getGraphVector().size(); i++) {
					if (board.getGraphVector().get(i) instanceof WfActivity) {
						acts.add((WfActivity) board.getGraphVector().get(i));
					}
				}
				Collections.sort(acts, new MyComparator());
				setValues(acts);
			}
		}
		setEnabled(board != null);
	}

	/**
	 * ���ں���ƴ���ıȽ���
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class MyComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			if (o1 != null && o2 != null) {
				return PinyinHelper.getAllPinyin(o1.toString()).compareTo(
						PinyinHelper.getAllPinyin(o2.toString()));
			}
			return 1;
		}

	}

	/**
	 * ���Ⱦ������list����ʾ����
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class ActListCellRenderer extends DefaultListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			DefaultListCellRenderer c = (DefaultListCellRenderer) super
					.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);
			WfActivity act = (WfActivity) value;
			if (act.isSelected() || isSelected) {
				c.setBackground(list.getSelectionBackground());
				c.setForeground(list.getSelectionForeground());
			} else {
				c.setBackground(list.getBackground());
				c.setForeground(list.getForeground());
			}
			WofoActivityBean actBean = (WofoActivityBean) act.getWofoBean();
			actBean.getActivityIcon();
			c.setIcon(getActivityImage(actBean));
			return c;
		}

		public ImageIcon getActivityImage(WofoActivityBean activityBean) {
			String defaultIcon = "workflow_activity_"
					+ activityBean.getActivityType() + "16.gif";
			if (activityBean.getActivityIcon() != null
					&& !activityBean.getActivityIcon().trim().equals("")) {
				String[] split = activityBean.getActivityIcon().split("\\.");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < split.length; i++) {
					if (i == split.length - 1) {
						sb.append("16");
					}
					if (i > 0) {
						sb.append(".");
					}
					sb.append(split[i]);
				}
				defaultIcon = sb.toString();
			}
			return Functions.getImageIcon(defaultIcon);
		}
	}

}
