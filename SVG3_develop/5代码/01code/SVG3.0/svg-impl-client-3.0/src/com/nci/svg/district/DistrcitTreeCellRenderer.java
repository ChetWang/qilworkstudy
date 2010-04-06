package com.nci.svg.district;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-4-7
 * @功能：
 *
 */
public class DistrcitTreeCellRenderer extends DefaultTreeCellRenderer {

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);   
        DefaultMutableTreeNode   node   =   (DefaultMutableTreeNode)value;  
        try
        {
        if(node.getLevel() == 0)
        {
        	//第一层
//        	this.setIcon(new ImageIcon(""));
        }
        else if(node.getLevel() % 2== 1)
        {
        	//偶数层，目前均用来表示线路
        	this.setIcon(new ImageIcon(new URL(ResourcesManager.getPath("icon/line.ico"))));
        }
        else 
        {
        	//奇数层，目前均用来表示杆塔或计量表
        	this.setIcon(new ImageIcon(new URL(ResourcesManager.getPath("icon/tower.ico"))));
        }
        }
        catch(Exception ex)
        {
        	
        }
        return this;
	}

}
