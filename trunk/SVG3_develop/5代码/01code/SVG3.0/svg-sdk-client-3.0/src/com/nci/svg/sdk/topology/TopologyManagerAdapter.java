package com.nci.svg.sdk.topology;

import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.shape.GraphUnitImageShape;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-5
 * @功能：
 *
 */
public abstract class TopologyManagerAdapter implements TopologyManagerIF {


	/**
	 * the drawing mouse pressed action
	 */
	public static final int DRAWING_MOUSE_PRESSED = 0;
	/**
	 * the drawing mouse released action
	 */
	public static final int DRAWING_MOUSE_RELEASED = 1;
	/**
	 * the drawing mouse dragged action
	 */
	public static final int DRAWING_MOUSE_DRAGGED = 2;
	/**
	 * the drawing mouse move action
	 */
	public static final int DRAWING_MOUSE_MOVED = 3;
	/**
	 * the drawing mouse double click action
	 */
	public static final int DRAWING_MOUSE_DOUBLE_CLICK = 4;
	/**
	 * the drawing mouse end action event
	 */
	public static final int DRAWING_END = 5;
	
	public static final int DRAWING_MOUSE_RIGHT = 10;

	/**
	 * add by yux,2009-4-9
	 * 
	 */
	protected EditorAdapter editor = null;
	/**
	 * add by yux,2009-4-9
	 *
	 */
	protected boolean businessModeFlag = false;
	/**
	 * add by yux,2009-4-9
	 *
	 */
	protected SVGHandle handle = null;


	public TopologyManagerAdapter(EditorAdapter editor,SVGHandle handle) {
		this.editor = editor;
		this.handle = handle;
	}

	
	
	public boolean isBusinessModeFlag() {
		return businessModeFlag;
	}

	public void setBusinessModeFlag(boolean businessModeFlag) {
		this.businessModeFlag = businessModeFlag;
	}
}
