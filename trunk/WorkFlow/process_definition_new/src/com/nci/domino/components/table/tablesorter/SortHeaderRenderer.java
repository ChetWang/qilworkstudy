package com.nci.domino.components.table.tablesorter;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * 渲染器，用于表头的渲染
 * 
 * @author Qil.Wong
 */
public class SortHeaderRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 6248542573955683301L;

	SortController d;

	Border defaultBorder = (Border) UIManager
			.getBorder("TableHeader.cellBorder");


	public SortHeaderRenderer() {
	}

	public SortHeaderRenderer(SortController d) {
		this.d = d;
		setBorder(defaultBorder);
	}

	public Component getTableCellRendererComponent(JTable jtable, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (jtable != null) {
			JTableHeader jtableheader = jtable.getTableHeader();
			if (jtableheader != null) {
				setForeground(jtableheader.getForeground());
				setBackground(jtableheader.getBackground());
				setFont(jtableheader.getFont());
			}
		}
		setText(obj != null ? obj.toString() : "");
		int k = jtable.convertColumnIndexToModel(column);
		if (k == d.getSortColumnIndex()) {
			setIcon(d.getSortedColumnAscending() ? SortController.upIcon
					: SortController.downIcon);
		} else {
			setIcon(null);
		}
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.LEFT);
		return this;
	}
}
