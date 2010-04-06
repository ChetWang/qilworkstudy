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
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-6
 * @���ܣ�ҵ������ṹ
 *
 */
public abstract class BusinessModuleAdapter extends AbstractShape {

	/**
	 * add by yux,2009-3-11
	 * ��������ƶ�
	 */
	public static final int MOUSE_MOVE = 0;
	/**
	 * add by yux,2009-3-11
	 * ����м�������¹���
	 */
	public static final int MOUSE_WHEEL_DOWN = 1;
	/**
	 * add by yux,2009-3-11
	 * ����м�������Ϲ���
	 */
	public static final int MOUSE_WHEEL_UP = 2;
	
	/**
	 * add by yux,2009-3-11
	 * ��ģ��˵���ӳ��
	 */
	protected LinkedHashMap<String,JMenuItem> mapMenuItems = new LinkedHashMap<String,JMenuItem>();
	/**
	 * add by yux,2009-3-11
	 * ��ģ��˵���ǩ�б�
	 */
	protected LinkedList<String> menuLabels = new LinkedList<String>();
	/**
	 * add by yux,2009-3-11
	 * ��ͼ���ƶ���
	 */
	protected BusinessDrawingHandler drawingHandler = null;
	
	public BusinessModuleAdapter(EditorAdapter editor) {
		super(editor);
		// ���ò˵�������
		setMenuItemInfo();
		//���Ʋ˵���
		createMenuItem();
		//������ͼ����
		drawingHandler = new BusinessDrawingHandler(this);
	}
	
	/**
	 * ���ñ�ģ������Ĳ˵�����Ϣ
	 * ��:
	 * addMenu("���Ƹ���","huizhiganxian");
	 */
	protected abstract void setMenuItemInfo();
	
	/**
	 * ��������Ĳ˵�˳��ţ�ͼ�κ�ѡ�нڵ㼯�ж���ģ���Ƿ���Ҫ��������
	 * @param index �˵���˳���
	 * @param handle ͼ�ξ��
	 * @param selectedElements ѡ�нڵ㼯
	 * @return �������÷���true������������false
	 */
	protected abstract boolean verifyValidity(int index,SVGHandle handle,Set<Element> selectedElements); 

	/**
	 * ��handle����ʱ����ʼ��
	 * @param index ˳���
	 * @param handle ��ǰ�´�����handle
	 */
	protected abstract void initHandle(int index,SVGHandle handle);
	
	/**
	 * ���������˳���
	 * @param index�����������˳��ţ�ִ��ҵ��
	 */
	protected abstract void doAction(int index);
	
	/**
	 * ���Ӳ˵���
	 * @param menuLabel �˵���ǩ������Ϊ��
	 * @param menuID �˵���ţ���Ϊ��
	 * @return ��˵���ǩΪ���򷵻�false�����򷵻�true
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
	 * �����趨�Ĳ˵����������ò˵�
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
			//���ü���
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
	 * ��ȡָ��λ�õĲ˵����,
	 * @param index λ��
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
	 * ����˳��š��������ͼ�ָ������ʼ�����ֹ�㣬�ڴ����path�����Ͻ��л���
	 * @param index ˳���
	 * @param actionType �������ͣ�MOUSE_MOVE��MOUSE_WHEEL_DOWN,MOUSE_WHEEL_UP
	 * @param path �ɱ�����path����
	 * @param beginPoint ��ʼ��
	 * @param endPoint ��ֹ��
	 * @param modifier ��ǰ����״̬
	 *         ͨ��isAltDown��isAltGraphDown��isControlDown��isShiftDown��isMetaDown���Ի�ȡ������Ϣ
	 * @return �����������쳣�򷵻�null
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
	 * ����˳��ż���ͼ�����ݼ�����ָ����handle�����н��нڵ�����
	 * @param handle ��ͼhandle
	 * @param index ˳���
	 * @param shapes ����path����
	 * @param dataList �Զ������ݼ���
	 * @return ���ɵĽڵ� 
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
