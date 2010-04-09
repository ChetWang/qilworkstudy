/*
 * DefaultSimpleTableSortRenderer.java
 *
 * Created on 2007��12��9��, ����11:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.nci.domino.components.table.tablesorter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * ���TableModel��ʵ����DefaultTableModel���Ϳ�����������������
 * 
 * @author Qil.Wong
 */
public class DefaultSimpleTableSortor implements SortController {

	/**
	 * ѡ��Ҫ���������
	 */
	public int sortedColumnIndex = -1;
	/**
	 * Ĭ������Ϊ����
	 */
	public boolean sortedColumnAscending = true;
	/**
	 * Creates a new instance of DefaultSimpleTableSortRenderer
	 */
	public javax.swing.JTable sortTable;

	public DefaultSimpleTableSortor(javax.swing.JTable sortTable) {
		this.sortTable = sortTable;
	}

	/**
	 * ��ʼ����ͷ��
	 */
	public void initSortHeader() {
		final JTableHeader header = sortTable.getTableHeader();

		// ����������
		final SortHeaderRenderer renderer = new SortHeaderRenderer(this);
		header.setDefaultRenderer(renderer);
		final Border defaultBorder = UIManager
				.getBorder("TableHeader.cellBorder");
		header.setBorder(defaultBorder);
		header.updateUI();
		header.addMouseListener(new MouseAdapter() {
			Border bevelBorder = new SoftBevelBorder(BevelBorder.LOWERED);

			DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header
					.getDefaultRenderer();

			public void mousePressed(MouseEvent evt) {
				if (evt.getButton() == MouseEvent.BUTTON1)
					renderer.setBorder(bevelBorder);
			}

			public void mouseReleased(MouseEvent evt) {
				if (evt.getButton() == MouseEvent.BUTTON1) {
					renderer.setBorder(defaultBorder);
					tableColummClicked(evt);
				}
			}
		});
	}

	public void tableColummClicked(final java.awt.event.MouseEvent evt) {
		try {
			TableColumnModel colModel = sortTable.getColumnModel();
			// sortTable.get
			int index = colModel.getColumnIndexAtX(evt.getX());// ��ȡ��������е����
			int modelIndex = colModel.getColumn(index).getModelIndex();
			// ����Ѿ��������������һ����֮�෴������
			if (sortedColumnIndex == modelIndex) {
				sortedColumnAscending = !sortedColumnAscending;
			}
			sortedColumnIndex = modelIndex;
			// ��ģ�ͽ�������
			sortColumn(modelIndex, sortedColumnAscending, evt);
		} catch (ClassCastException cce) {
			System.out.println(cce);
			cce.printStackTrace();
		}
	}

	public void sortColumn(int col, boolean ascending,
			java.awt.event.MouseEvent evt) {
		DefaultTableModel model = (DefaultTableModel) sortTable.getModel();
		Collections.sort(model.getDataVector(), new ColumnComparator(col,
				ascending));
	}

	public int getSortColumnIndex() {
		return sortedColumnIndex;
	}

	public boolean getSortedColumnAscending() {
		return sortedColumnAscending;
	}
}
