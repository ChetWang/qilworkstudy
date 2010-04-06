package com.nci.svg.sdk.module;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JMenuItem;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.shape.BusinessDrawingHandler;
import com.nci.svg.sdk.shape.BusinessDrawingHandler.StoreData;

import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-6
 * @功能：业务组件结构
 *
 */
public abstract class BusinessModuleAdapter extends AbstractShape {

	/**
	 * add by yux,2009-3-11
	 * 鼠标正常移动
	 */
	public static final int MOUSE_MOVE = 0;
	/**
	 * add by yux,2009-3-11
	 * 鼠标中间滚轮向下滚动
	 */
	public static final int MOUSE_WHEEL_DOWN = 1;
	/**
	 * add by yux,2009-3-11
	 * 鼠标中间滚轮向上滚动
	 */
	public static final int MOUSE_WHEEL_UP = 2;
	
	/**
	 * add by yux,2009-3-11
	 * 本模块菜单项映射
	 */
	protected LinkedHashMap<String,JMenuItem> mapMenuItems = new LinkedHashMap<String,JMenuItem>();
	/**
	 * add by yux,2009-3-11
	 * 本模块菜单标签列表
	 */
	protected LinkedList<String> menuLabels = new LinkedList<String>();
	/**
	 * add by yux,2009-3-11
	 * 绘图控制对象
	 */
	protected BusinessDrawingHandler drawingHandler = null;
	
	public BusinessModuleAdapter(EditorAdapter editor) {
		super(editor);
		// 设置菜单项属性
		setMenuItemInfo();
		//绘制菜单项
		createMenuItem();
		//创建绘图对象
		drawingHandler = new BusinessDrawingHandler(this);
	}
	
	/**
	 * 设置本模块所需的菜单项信息
	 * 如:
	 * addMenu("绘制干线","huizhiganxian");
	 */
	protected abstract void setMenuItemInfo();
	
	/**
	 * 根据输入的菜单顺序号，图形和选中节点集判定本模块是否需要发生作用
	 * @param index 菜单的顺序号
	 * @param handle 图形句柄
	 * @param selectedElements 选中节点集
	 * @return 发生作用返回true，不发生返回false
	 */
	protected abstract boolean verifyValidity(int index,SVGHandle handle,Set<Element> selectedElements); 

	/**
	 * 新handle创建时，初始化
	 * @param index 顺序号
	 * @param handle 当前新创建的handle
	 */
	protected abstract void initHandle(int index,SVGHandle handle);
	
	/**
	 * 根据输入的顺序号
	 * @param index：根据输入的顺序号，执行业务
	 */
	protected abstract void doAction(int index);
	
	/**
	 * 增加菜单项
	 * @param menuLabel 菜单标签，不可为空
	 * @param menuID 菜单编号，可为空
	 * @return 如菜单标签为空则返回false，否则返回true
	 */
	public int addMenu(String menuLabel)
	{
		if(menuLabel == null || menuLabel.length() == 0)
			return -1;
		if(menuLabels.add(menuLabel) == false)
			return -1;

	    return menuLabels.size() -1;
	}
	
	
	/**
	 * 根据设定的菜单项属性设置菜单
	 */
	protected void createMenuItem()
	{
		if(menuLabels == null)
			return;
		int len = menuLabels.size();
		String menuLabel = null;
		for(int i = 0; i < len;i++)
		{
			menuLabel = menuLabels.get(i);
			if(menuLabel == null || menuLabel.length() == 0)
				continue;
			final JMenuItem menuItem = new JMenuItem(menuLabel);
			menuItem.setEnabled(false);
			mapMenuItems.put(getMenuID(i), menuItem);
			final int index = i;
			menuItem.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent e) {
					doAction(index);
				}
				
			});
			//设置监听
			final SelectionChangedListener scl = new SelectionChangedListener() {

				@Override
				public void selectionChanged(Set<Element> selectedElements) {

					SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
					boolean b = verifyValidity(index,handle,selectedElements);
					menuItem.setEnabled(b);
				}
			};
			editor.getHandlesManager().addHandlesListener(new HandlesListener() {
				@Override
				public void handleCreated(SVGHandle currentHandle) {
					initHandle(index,currentHandle);
					currentHandle.getSelection().addSelectionChangedListener(scl);
					boolean b = verifyValidity(index,currentHandle,null);
					menuItem.setEnabled(b);
				}

				public void handleChanged(SVGHandle currentHandle,
						Set<SVGHandle> handles) {
					Set<Element> selectedElements = null;
					if(currentHandle != null)
						selectedElements = currentHandle.getSelection().getSelectedElements();
					boolean b = verifyValidity(index,currentHandle,selectedElements);
					menuItem.setEnabled(b);
				}

			});
		}
	}
	
	/**
	 * 获取指定位置的菜单编号,
	 * @param index 位置
	 * @return 
	 */
	protected String getMenuID(int index)
	{
		String menuID = String.valueOf(index);
		return menuID;
	}
	
	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#getLevelCount()
	 */
	@Override
	public int getLevelCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#getSelectionItems(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, int)
	 */
	@Override
	public Set<SelectionItem> getSelectionItems(SVGHandle handle,
			Set<Element> elements, int level) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#resize(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.AffineTransform)
	 */
	@Override
	public UndoRedoAction resize(SVGHandle handle, Set<Element> elementSet,
			AffineTransform transform) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#rotate(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, double)
	 */
	@Override
	public UndoRedoAction rotate(SVGHandle handle, Set<Element> elementSet,
			Point2D centerPoint, double angle) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#showAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	@Override
	public CanvasPainter showAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D currentPoint) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#showTranslateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	@Override
	public CanvasPainter showTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#getMenuItems()
	 */
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		return mapMenuItems;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#getShape(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element, boolean)
	 */
	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#setShape(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element, java.awt.Shape)
	 */
	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {
		
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#skew(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, double, boolean)
	 */
	@Override
	public UndoRedoAction skew(SVGHandle handle, Set<Element> elementSet,
			Point2D centerPoint, double skewFactor, boolean isHorizontal) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#translate(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, boolean)
	 */
	@Override
	public UndoRedoAction translate(SVGHandle handle, Set<Element> elementSet,
			Point2D translationFactors, boolean refresh) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#validateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	@Override
	public UndoRedoAction validateAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D lastPoint) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#validateTranslateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
		return null;
	}
	
	/**
	 * 根据顺序号、动作类型及指定的起始点和终止点，在传入的path对象上进行绘制
	 * @param index 顺序号
	 * @param actionType 动作类型：MOUSE_MOVE，MOUSE_WHEEL_DOWN,MOUSE_WHEEL_UP
	 * @param path 可被绘制path对象
	 * @param beginPoint 起始点
	 * @param endPoint 终止点
	 * @param modifier 当前键盘状态
	 *         通过isAltDown，isAltGraphDown，isControlDown，isShiftDown，isMetaDown可以获取按键信息
	 * @return 结果，如存在异常则返回null
	 */
	public abstract StoreData draw(int index,int actionType,ExtendedGeneralPath path,Point2D beginPoint,Point2D endPoint,int modifier);
	
	public void setListenersFalse()
	{
		editor.getHandlesManager().getCurrentHandle().getScrollPane()
		    .setListenersEnabled(false);
	}

	/* (non-Javadoc)
	 * @see fr.itris.glips.svgeditor.shape.AbstractShape#notifyDrawingAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.awt.geom.Point2D, int, int)
	 */
	@Override
	public void notifyDrawingAction(SVGHandle handle, Point2D point,
			int modifier, int type) {
		switch (type) {

		case DRAWING_MOUSE_PRESSED:

			drawingHandler.mousePressed(handle, point,modifier);
			
			break;

		case DRAWING_MOUSE_RELEASED:

			drawingHandler.mouseReleased(handle, point,modifier);
			break;

		case DRAWING_MOUSE_DRAGGED:

     		drawingHandler.mouseDragged(handle, point,modifier);
			break;

		case DRAWING_MOUSE_MOVED:

			drawingHandler.mouseMoved(handle, point,modifier);
			break;

		case DRAWING_MOUSE_DOUBLE_CLICK:

			drawingHandler.mouseDoubleClicked(handle, point,modifier);
			resetDrawing();
			break;

		case DRAWING_END:

			drawingHandler.reset(handle);
			editor.getHandlesManager().getCurrentHandle().getScrollPane()
					.setListenersEnabled(true);
			break;
		case DRAWING_MOUSE_WHEEL_DOWN: {
			drawingHandler.mouseWheelDown(handle, point,modifier);
			break;
		}
		case DRAWING_MOUSE_WHEEL_UP: {
			drawingHandler.mouseWheelUp(handle, point,modifier);
			break;
		}
		}
	}
	
	/**
	 * 根据顺序号及绘图的数据集，在指定的handle对象中进行节点生成
	 * @param handle 绘图handle
	 * @param index 顺序号
	 * @param shapes 过程path集合
	 * @param dataList 自定义数据集合
	 * @return 生成的节点 
	 */
	public abstract Element createElement(SVGHandle handle,int index,LinkedList<ExtendedGeneralPath> shapes,LinkedList<StoreData> dataList);
	 /**
     * Returns whether or not the Shift modifier is down on this event.
     */
    public boolean isShiftDown(int modifiers) {
        return (modifiers & InputEvent.SHIFT_MASK) != 0;
    }

    /**
     * Returns whether or not the Control modifier is down on this event.
     */
    public boolean isControlDown(int modifiers) {
        return (modifiers & InputEvent.CTRL_MASK) != 0;
    }

    /**
     * Returns whether or not the Meta modifier is down on this event.
     */
    public boolean isMetaDown(int modifiers) {
        return (modifiers & InputEvent.META_MASK) != 0;
    }

    /**
     * Returns whether or not the Alt modifier is down on this event.
     */
    public boolean isAltDown(int modifiers) {
        return (modifiers & InputEvent.ALT_MASK) != 0;
    }

    /**
     * Returns whether or not the AltGraph modifier is down on this event.
     */
    public boolean isAltGraphDown(int modifiers) {
        return (modifiers & InputEvent.ALT_GRAPH_MASK) != 0;
    }
}
