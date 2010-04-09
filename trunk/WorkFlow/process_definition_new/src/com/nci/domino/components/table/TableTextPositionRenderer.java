package com.nci.domino.components.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 将默认靠右的数字显示，在表格中靠左显示
 * 
 * @author Qil.Wong
 * 
 */
public class TableTextPositionRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 7045484416429938709L;
	private int position;

	public TableTextPositionRenderer(int position) {
		this.position = position;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) super
				.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
		renderer.setHorizontalAlignment(position);
		return renderer;
	}
}
