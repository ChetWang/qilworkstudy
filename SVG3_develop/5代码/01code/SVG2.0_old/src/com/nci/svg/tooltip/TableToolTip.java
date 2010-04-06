/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.tooltip;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolTip;

/**
 *
 * @author Qil.Wong
 */
public class TableToolTip extends JToolTip {

    JTable table;
    JPanel panel = new JPanel();

    public TableToolTip(JTable table) {
        this.table = table;
    }

    public TableToolTip() {
    }

    public void setTipTable(JTable table) {
        this.table = table;
        panel.add(table);
    }

    public void paint(Graphics g) {
        if (table != null) {
//            icon.paintIcon(this, g, 0, 0);

            panel.paint(g);
        }
    }

    public Dimension getPreferredSize() {
        if (table == null)
            return super.getPreferredSize();
        else
            return new Dimension(table.getWidth(), table.getHeight());
    }
}
