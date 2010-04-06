package com.nci.svg.sdk.topology;

import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.shape.GraphUnitImageShape;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-5
 * @功能：图形拓扑管理及解析接口
 *
 */
public interface TopologyManagerIF {
    /**
     * 建立图形拓扑结构
     * @return 建立成功返回true,失败返回false
     */
    public boolean createTopology();
    
    /**
     * 在指定位置构建图元节点
     * @param bounds 目标位置
     * @param doc 文档对象
     * @param symboldoc 图元对象
     * @param name 图元名称
     * @param symbolID 图元编号
     * @param shape 图元操作对象
     * @return 成功则返回本次生成的图元节点,失败则返回null
     */
    public abstract Element createElementAsGraphUnit(
			Rectangle2D bounds, Document doc, Document symboldoc, String name,
			String symbolID, GraphUnitImageShape shape);

	/**
	 * 在指定位置构建模板节点
	 * @param bounds 目标位置
	 * @param doc 文档对象
	 * @param symboldoc 模板对象
     * @param name 图元名称
     * @param symbolID 图元编号
     * @param shape 图元操作对象
     * @return 成功则返回本次生成的图元节点,失败则返回null
	 */
	public Element createElementAsTemplate(
			Rectangle2D bounds, Document doc, Document symboldoc, String name,
			String symbolID, GraphUnitImageShape shape);

	/**
	 * 获取拓扑树model
	 * @return 拓扑树model
	 */
	public TreeModel getTreeModel();
	
	/**
	 * 获取拓扑管理对象
	 * @return 拓扑管理对象,如不存在则返回null
	 *         每个拓扑对象均允许根据实际情况返回
	 */
	public Object getTopologyObject();
	
	/**
	 * 获取拓扑树鼠标监听
	 * @return 拓扑树鼠标监听对象
	 */
	public MouseListener getTreeMouseListener();
	
	/**
	 * 获取拓扑树扩展节点定义
	 * @return 拓扑树扩展节点定义
	 */
	public DefaultTreeCellRenderer getTreeCellRenderer();

	/**
	 * 根据指定的鼠标位置获取鼠标形状
	 * @param point 鼠标坐标
	 * @return 计算后的鼠标坐标,无变化则返回null
	 */
	public Cursor getCursor(Point2D point);
	
	/**
	 * 触发鼠标事件
	 * @param mouseEventType 鼠标事件类型
	 * @param initPoint 起始点
	 * @param curPoint 当前点
	 * @return 事件操作结果
	 */
	public boolean nodifyMouseEvent(int mouseEventType,Point2D initPoint,Point2D curPoint);
    
}
