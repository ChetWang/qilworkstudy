/*
 * DefaultSimpleTableSortRenderer.java
 *
 * Created on 2007��12��9��, ����11:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.nci.ums.jmx.desktop.bean.tablesorter;

import java.util.Collections;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.nci.ums.jmx.desktop.bean.SwingWorker;

/**
 * ���TableModel��ʵ����DefaultTableModel���Ϳ�����������������
 * @author Qil.Wong
 */
public class DefaultSimpleTableSortRenderer implements SortController {

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

    public DefaultSimpleTableSortRenderer(javax.swing.JTable sortTable) {
        this.sortTable = sortTable;
    }

    public void initSortHeader() {
        JTableHeader header = sortTable.getTableHeader();
        header.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableColummClicked(evt);
            }
        });
    //����������
        header.setDefaultRenderer(new SortHeaderRenderer(this));
    }

    public void tableColummClicked(final java.awt.event.MouseEvent evt) {
   	
    	try {
            TableColumnModel colModel = sortTable.getColumnModel();
            int index = colModel.getColumnIndexAtX(evt.getX());//��ȡ��������е����
            int modelIndex = colModel.getColumn(index).getModelIndex();
            // ����Ѿ��������������һ����֮�෴������
            if (sortedColumnIndex == index) {
                sortedColumnAscending = !sortedColumnAscending;
            }
            sortedColumnIndex = index;
            //��ģ�ͽ�������
            sortColumn(modelIndex, sortedColumnAscending, evt);
        } catch (ClassCastException cce) {
            System.out.println(cce);
            cce.printStackTrace();
        }
    }

    public void sortColumn(int col, boolean ascending, java.awt.event.MouseEvent evt) {
        DefaultTableModel model = (DefaultTableModel) sortTable.getModel();
        Collections.sort(model.getDataVector(), new ColumnComparator(col, ascending));
    }

    public int getSortColumnIndex() {
        return sortedColumnIndex;
    }

    public boolean getSortedColumnAscending() {
        return sortedColumnAscending;
    }
}


