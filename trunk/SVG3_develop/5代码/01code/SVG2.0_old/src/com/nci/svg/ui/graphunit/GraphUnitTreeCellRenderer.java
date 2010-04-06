package com.nci.svg.ui.graphunit;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.nci.svg.graphunit.NCIEquipSymbolBean;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class GraphUnitTreeCellRenderer extends DefaultTreeCellRenderer{

	private static final long serialVersionUID = 1L;

	private ImageIcon rootIcon = ResourcesManager.getIcon("nci_graphunit_chooser_root_icon", false);
	
	private ImageIcon typeIcon = ResourcesManager.getIcon("nci_graphunit_chooser_type_icon", false);
	
	private ImageIcon graphUnitIcon = ResourcesManager.getIcon("nci_graphunit_chooser_graph_icon", false);

	public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);
		Object userObj = ((DefaultMutableTreeNode)value).getUserObject();
		//父节点，并无实际意义的图元，是图元的分类
		if(userObj instanceof String){
			if(userObj.equals(ResourcesManager.bundle.getString("nci_graphunit_chooser_root"))){
				this.setIcon(rootIcon);
				
			}else{
				this.setIcon(typeIcon);
			}
			this.setText((String)userObj);
		}
		//图元节点
		else if (userObj instanceof NCIEquipSymbolBean){
			this.setIcon(graphUnitIcon);
			this.setText(((NCIEquipSymbolBean)userObj).toString());
		}
		return this;
		
	}
}
