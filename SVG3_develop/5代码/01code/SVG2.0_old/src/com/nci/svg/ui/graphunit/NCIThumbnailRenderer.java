package com.nci.svg.ui.graphunit;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class NCIThumbnailRenderer extends JComponent implements ListCellRenderer{

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component c;
		if(value==null)
			c= new JLabel();
		else if (value instanceof NCIThumbnailPanel) {
			
			c = (Component)value;
			
		}else if (value instanceof NCISymbolThumbnail) {
			
			c = (Component)value;
			
		}
		else{
			DefaultListCellRenderer label = new DefaultListCellRenderer();
			label.setText(value.toString());
			c = label;
		}
		if (isSelected) {
			c.setBackground(list.getSelectionBackground());
			c.setForeground(list.getSelectionForeground());
		} else {
			c.setBackground(list.getBackground());
			c.setForeground(list.getForeground());
		}
//		c.repaint();
		return c;
	}
}
