/*
 * DefaultSimpleTableSortRenderer.java
 *
 * Created on 2007年12月9日, 上午11:21
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
 * 如果TableModel的实例是DefaultTableModel，就可以用这个类进行排序
 * @author Qil.Wong
 */
public class DefaultSimpleTableSortRenderer implements SortController {

    /**
     * 选择要求排序的列
     */
    public int sortedColumnIndex = -1;
    /**
     * 默认排序为升序
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
    //设置三角形
        header.setDefaultRenderer(new SortHeaderRenderer(this));
    }

    public void tableColummClicked(final java.awt.event.MouseEvent evt) {
   	
    	try {
            TableColumnModel colModel = sortTable.getColumnModel();
            int index = colModel.getColumnIndexAtX(evt.getX());//获取鼠标点击的列的序号
            int modelIndex = colModel.getColumn(index).getModelIndex();
            // 如果已经排序过，触发另一个与之相反的排序
            if (sortedColumnIndex == index) {
                sortedColumnAscending = !sortedColumnAscending;
            }
            sortedColumnIndex = index;
            //对模型进行排序
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


