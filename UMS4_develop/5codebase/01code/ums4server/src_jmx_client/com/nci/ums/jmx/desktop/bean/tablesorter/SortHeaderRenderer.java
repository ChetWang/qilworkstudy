/*
 * SortHeaderRenderer.java
 *
 * Created on 2007年12月8日, 上午10:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.nci.ums.jmx.desktop.bean.tablesorter;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * 渲染器，用于表头的渲染
 * @author Qil.Wong
 */
public class SortHeaderRenderer extends DefaultTableCellRenderer{
    
	private static final long serialVersionUID = 6248542573955683301L;

	SortController d;
	
    public SortHeaderRenderer() {
    }
    
    
    public SortHeaderRenderer(SortController d){
        this.d = d;
    }
    
    public Component getTableCellRendererComponent(JTable jtable,
            Object obj, boolean flag, boolean flag1, int i, int j) {
        if (jtable != null) {
            JTableHeader jtableheader = jtable.getTableHeader();
            if (jtableheader != null) {
                setForeground(jtableheader.getForeground());
                setBackground(jtableheader.getBackground());
                setFont(jtableheader.getFont());
            }
        }
        setText(obj != null ? obj.toString() : "");
        int k = jtable.convertColumnIndexToModel(j);
        if (k == d.getSortColumnIndex()) {
            setIcon(d.getSortedColumnAscending() ? SortController.upIcon : SortController.downIcon);
        } else {
            setIcon(null);
        }
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        this.setHorizontalAlignment(0);
        this.setHorizontalTextPosition(2);
        return this;
    }
}
