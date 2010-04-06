package fr.itris.glips.svgeditor.display.selection;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.selection.ModeObject;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.mode.EditorModeAdapter;
import com.nci.svg.sdk.module.ViewEditModuleAdapter;

import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * the class handling the mouse events on a svg canvas
 * 
 * @author ITRIS, Jordi SUC
 */
public class SelectionListener extends MouseAdapter implements
		MouseMotionListener {

	/**
	 * the zone for the double click action
	 */
	protected static final Point2D zoneForDoubleClick = new Point2D.Double(6, 6);
	/**
	 * the time between two clicks in a mouse released event
	 */
	protected static final long doubleClickThreshold = 300;
	/**
	 * the global selection manager
	 */
	private SelectionInfoManager selectionManager;
	/**
	 * the initial point for the drag action
	 */
	private Point2D initialDragPoint = null;
	/**
	 * the last point onto which a mouse action (but mouse move) has been done
	 */
	private Point2D currentPoint = null;
	/**
	 * whether the drag action has begun or not
	 */
	private boolean dragStarted = false;
	/**
	 * the selection manager
	 */
	private Selection selection;
	/**
	 * the last time a released mouse action happened
	 */
	private long lastMouseReleasedActionTime = 0;
	/**
	 * the point that was clicked when a released mouse action happened
	 */
	private Point2D lastMouseReleasedActionPoint = null;

	private int nSelectionMode = 0;

	private boolean bControl = false;

	private boolean bConnectFlag = false;

	private ModeObject modeObject = null;

	private ModeSelectionEvent modeSelectionEvent = null;

	/**
	 * @return the bControl
	 */
	public boolean isBControl() {
		return bControl;
	}

	/**
	 * the constructor of the class
	 * 
	 * @param selection
	 *            the selection manager
	 */
	public SelectionListener(Selection selection) {

		this.selection = selection;
		modeObject = new ModeObject();
		modeObject.setHandle(selection.getSVGHandle());
		modeSelectionEvent = new ModeSelectionEvent(modeObject);
		selectionManager = selection.getSVGHandle().getEditor()
				.getSelectionManager();
	}

	@Override
	public void mousePressed(MouseEvent evt) {

		evt = convertEvent(evt);

		// getting the current selection mode
		int selectionMode = selectionManager.getSelectionMode();
		// add by yuxiang
		// 支持无需点击框选即可进行框选操作

		if (selectionMode != SelectionInfoManager.NONE_MODE && !isPopUp(evt)) {

			// computing the point for this event
			Point2D point = selection.getSVGHandle().getTransformsManager()
					.getAlignedWithRulersPoint(evt.getPoint(), false);
			currentPoint = point;
			initialDragPoint = point;
			dragStarted = false;

			if (selectionMode == SelectionInfoManager.DRAWING_MODE) {

				selection.drawingAction(point, evt.getModifiers(),
						AbstractShape.DRAWING_MOUSE_PRESSED);

			} else if (selectionMode == SelectionInfoManager.ITEMS_ACTION_MODE) {

				selection.itemsAction(point);

			} else if (selectionMode == SelectionInfoManager.ZONE_MODE) {

				boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
				selection.handleSelectionZone(point,
						Selection.SELECTION_ZONE_MOUSE_PRESSED,
						isMultiSelectionEnabled);

			} else if (selectionMode == SelectionInfoManager.ZOOM_MODE) {

				selection.handleZoomZone(point,
						Selection.SELECTION_ZONE_MOUSE_PRESSED);
			} else if (selectionMode == SelectionInfoManager.MANUAL_TERMINAL_MODE) {
				selection.addTerminalPoint(point);
				selectionManager.setToRegularMode();
			}

		} else if (isPopUp(evt)) {
			ViewEditModuleAdapter viewEditModule = (ViewEditModuleAdapter) selection
					.getSVGHandle().getEditor().getModule(
							ViewEditModuleAdapter.NCI_View_Edit_ModuleID);
			String edit_view_mode = "";
			if (viewEditModule != null)
				edit_view_mode = viewEditModule.getViewEdit_mode();
			if (edit_view_mode.equals(ViewEditModuleAdapter.VIEW_MODE)) {
				// 业务模式根据模型生成右键菜单
				selection.showActionsPopupMenu(evt.getPoint());
				return;
			} else
				selection.getSVGHandle().getEditor().getSVGModuleLoader()
						.getPopupManager().showPopup(selection.getSVGHandle(),
								evt.getPoint());
		}

		// modify by yuxiang,2008-12-03
		// 设置手型鼠标
		if (selection.getSVGHandle().getCanvas().getCursor().getType() == Cursor.DEFAULT_CURSOR
				&& evt.isControlDown()) {
			nSelectionMode = SelectionInfoManager.CANVAS_MOVE_MODE;
			selectionManager.setToCanvasMoveMode();
			selection.handleHandCursor(true);
		} else
			nSelectionMode = SelectionInfoManager.NONE_MODE;
		modeObject.setMouseEvent(evt);
		selection.fireModeSelection(modeSelectionEvent);
		selection.fireMousePressed(modeSelectionEvent);
	}

	@Override
	public void mouseEntered(MouseEvent evt) {// added by
		// wangql，目的是为解决鼠标拖动左侧图元时的鼠标形状，这个过程中mouseMoved不会响应

		if (selection.getSVGHandle().getEditor().getSymbolManager()
				.getMouseStatus() == Constants.MOUSE_PRESSED) {
			selection.getSVGHandle().getScrollPane().getSVGCanvas().setCursor(
					Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

		}
		modeObject.setMouseEvent(evt);
		selection.fireModeSelection(modeSelectionEvent);
	}

	@Override
	public void mouseReleased(MouseEvent evt) {

		bControl = evt.isControlDown();
		evt = convertEvent(evt);

		selection.clearPoints();
		selection.showTerminal(null);
		// getting the current selection mode
		int selectionMode = selectionManager.getSelectionMode();
		// add by yuxiang,2008.12.03
		// 新增平移操作复原鼠标
		if (selectionMode == SelectionInfoManager.CANVAS_MOVE_MODE) {
			selection.handleHandCursor(false);
			selectionMode = SelectionInfoManager.ZONE_MODE;
		}
		if ((nSelectionMode == SelectionInfoManager.ZONE_MODE)
				&& (selectionMode == SelectionInfoManager.REGULAR_MODE)
				&& dragStarted) {
			selectionMode = SelectionInfoManager.ZONE_MODE;
		}

		if (selectionMode != SelectionInfoManager.NONE_MODE
				&& selectionMode != SelectionInfoManager.ITEMS_ACTION_MODE
				&& !isPopUp(evt)) {

			// computing the point for this event
			Point2D point = currentPoint;
			boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
			int selectionSubMode = selection.getSelectionSubMode();
			boolean isDoubleClick = false;

			if (selection.getNLineMode() != -1) {
				// 绘制平行线，垂直线，垂分线
				selection.drawLineAtPoint(point);
			}

			switch (selectionMode) {

			case SelectionInfoManager.REGULAR_MODE:

				if (lastMouseReleasedActionTime != 0
						&& lastMouseReleasedActionPoint != null) {

					long deltaTime = evt.getWhen()
							- lastMouseReleasedActionTime;
					if (point == null)
						break;
					Rectangle2D oldZone = new Rectangle2D.Double(point.getX()
							- zoneForDoubleClick.getX() / 2, point.getY()
							- zoneForDoubleClick.getY() / 2, zoneForDoubleClick
							.getX(), zoneForDoubleClick.getY());

					if (deltaTime < doubleClickThreshold
							&& oldZone.contains(lastMouseReleasedActionPoint)) {

						isDoubleClick = true;
					}
				}

				lastMouseReleasedActionPoint = point;
				lastMouseReleasedActionTime = evt.getWhen();

				if (isDoubleClick) {
					ViewEditModuleAdapter module = (ViewEditModuleAdapter) (selection
							.getSVGHandle().getEditor()
							.getModule(ViewEditModuleAdapter.NCI_View_Edit_ModuleID));
					if (module != null) {
						String mode = module.getViewEdit_mode();
						if (mode.equals(ViewEditModuleAdapter.VIEW_MODE))
							selection.hyperLink(point);
						else if (mode.equals(ViewEditModuleAdapter.EDIT_MODE)) {
							selection.openSVGProperties();
						}
					}
				} else {

					if (!evt.isShiftDown()
							&& selectionSubMode != Selection.REGULAR_SUB_MODE) {

						selection.validateAction(point);

					} else {
						if (point == null)
							break;
						selection.setSelection(point, isMultiSelectionEnabled);
					}
				}

				break;

			case SelectionInfoManager.ZONE_MODE:

				selection.handleSelectionZone(point,
						Selection.SELECTION_ZONE_MOUSE_RELEASED,
						isMultiSelectionEnabled);
				break;

			case SelectionInfoManager.ZOOM_MODE:

				selection.handleZoomZone(point,
						Selection.SELECTION_ZONE_MOUSE_RELEASED);
				break;

			case SelectionInfoManager.DRAWING_MODE:

				// checking if the event denotes a double click action

				if (lastMouseReleasedActionTime != 0
						&& lastMouseReleasedActionPoint != null) {

					long deltaTime = evt.getWhen()
							- lastMouseReleasedActionTime;
					Rectangle2D oldZone = new Rectangle2D.Double(point.getX()
							- zoneForDoubleClick.getX() / 2, point.getY()
							- zoneForDoubleClick.getY() / 2, zoneForDoubleClick
							.getX(), zoneForDoubleClick.getY());

					if (deltaTime < doubleClickThreshold
							&& oldZone.contains(lastMouseReleasedActionPoint)) {

						isDoubleClick = true;
					}
				}

				lastMouseReleasedActionPoint = point;
				lastMouseReleasedActionTime = evt.getWhen();

				// getting the type of the action for the drawing mode
				int type = isDoubleClick ? AbstractShape.DRAWING_MOUSE_DOUBLE_CLICK
						: AbstractShape.DRAWING_MOUSE_RELEASED;

				selection.drawingAction(point, evt.getModifiers(), type);
				break;
			case SelectionInfoManager.SCREEN_CAST_MODE:
				selection.handleSelectionZone(point,
						Selection.SELECTION_ZONE_MOUSE_RELEASED,
						isMultiSelectionEnabled);
				break;
			}

			initialDragPoint = null;
			dragStarted = false;
			currentPoint = null;
		}

		if (selectionMode == SelectionInfoManager.ITEMS_ACTION_MODE) {
			selection.setSelection(currentPoint, false);
			selection.getSVGHandle().getEditor().getSelectionManager()
					.setToRegularMode();
		}
		selection.fireBusinessSelection();
		modeObject.setMouseEvent(evt);
		selection.fireModeSelection(modeSelectionEvent);
		selection.fireMouseReleased(modeSelectionEvent);
		selection.getSVGHandle().getEditor().getSvgSession()
				.refreshCurrentHandleImediately();
		
	}

	public void mouseClicked(MouseEvent e) {
		modeObject.setMouseEvent(e);
		selection.fireMouseClicked(modeSelectionEvent);
	}

	@SuppressWarnings("all")
	/*
	 * 
	 */
	public void mouseDragged(MouseEvent e) {
		// 浏览模式下，除了截屏、区域放大，不允许其他鼠标拖拽操作
		// modify by yuxiang,2008.12.03
		// 新增平移操作控制约束，平移操作在任何时候均有效
		if (selection.getSVGHandle().getEditor().getGCParam(
				SysSetDefines.APP_MODE).equals(
				EditorModeAdapter.SVGTOOL_MODE_VIEW_ONLYVIEW)
				&& (selectionManager.getSelectionMode() != SelectionInfoManager.CANVAS_MOVE_MODE))
			return;
		ViewEditModuleAdapter viewEditModule = (ViewEditModuleAdapter) selection
				.getSVGHandle().getEditor().getModule(
						ViewEditModuleAdapter.NCI_View_Edit_ModuleID);
		if (viewEditModule != null) {

			String edit_view_mode = viewEditModule.getViewEdit_mode();
			if (edit_view_mode.equals(ViewEditModuleAdapter.VIEW_MODE)
					&& (selectionManager.getSelectionMode() != SelectionInfoManager.SCREEN_CAST_MODE)
					&& (selectionManager.getSelectionMode() != SelectionInfoManager.ZOOM_MODE)
					&& (selectionManager.getSelectionMode() != SelectionInfoManager.CANVAS_MOVE_MODE)) {
				return;
			}
		}

		final MouseEvent evt = convertEvent(e);

		int selectionMode = selectionManager.getSelectionMode();

		if (selectionMode != SelectionInfoManager.NONE_MODE
				&& selectionMode != SelectionInfoManager.ITEMS_ACTION_MODE
				&& !isPopUp(evt)) {

			// computing the point for this event
			Point2D point = selection.getSVGHandle().getTransformsManager()
					.getAlignedWithRulersPoint(evt.getPoint(), false);

			currentPoint = point;
			// add by yuxiang
			// 支持无需点击框选即可进行框选操作
			// *
			// if (selectionMode == SelectionInfoManager.REGULAR_MODE
			// && selection.getSelectedElements().size() == 0) {
			// modify by yuxiang,2008.12.03
			// 去除框选，增加平移拖动
			if (selectionMode == SelectionInfoManager.CANVAS_MOVE_MODE) {
				// if
				// (selection.getSVGHandle().getCanvas().getCursor().getType()
				// == Cursor.DEFAULT_CURSOR) {
				//
				// // 如无选中，则开始直接框选，并做框选初始化
				// if (!dragStarted) {
				// boolean isMultiSelectionEnabled =
				// isMultiSelectionEnabled(evt);
				// selection.clearSelection();
				// selection.handleSelectionZone(initialDragPoint,
				// Selection.SELECTION_ZONE_MOUSE_PRESSED,
				// isMultiSelectionEnabled);
				// nSelectionMode = SelectionInfoManager.ZONE_MODE;
				// }
				// }
				selection.moveCanvas(initialDragPoint, currentPoint);
				initialDragPoint = currentPoint;
			}

			if (nSelectionMode == SelectionInfoManager.NONE_MODE) {
				if (selection.getSVGHandle().getCanvas().getCursor().getType() == Cursor.DEFAULT_CURSOR) {

					// 如无选中，则开始直接框选，并做框选初始化
					if (!dragStarted) {
						boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
						selection.clearSelection();
						selection.handleSelectionZone(initialDragPoint,
								Selection.SELECTION_ZONE_MOUSE_PRESSED,
								isMultiSelectionEnabled);
						nSelectionMode = SelectionInfoManager.ZONE_MODE;
					}
				}

			}
			// remove by yuxiang
			// 恢复直线选择命中率
			/*
			 * else if(selectionMode == SelectionInfoManager.REGULAR_MODE &&
			 * !dragStarted) { Rectangle2D rectangle = selection.getBounds();
			 * Point2D scaledPoint =
			 * selection.getSVGHandle().getTransformsManager()
			 * .getScaledPoint(initialDragPoint, true);
			 * //如存在选中的图元，起始点不在图元矩阵内且不在操作点上，仍开始框选，并做框选初始化
			 * if(!rectangle.contains(scaledPoint) &&
			 * selection.getSelectionItem(point) == null) { boolean
			 * isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
			 * selection.handleSelectionZone(initialDragPoint,
			 * Selection.SELECTION_ZONE_MOUSE_PRESSED, isMultiSelectionEnabled);
			 * nSelectionMode = SelectionInfoManager.ZONE_MODE; } }
			 */

			if (nSelectionMode == SelectionInfoManager.ZONE_MODE) {
				selectionMode = SelectionInfoManager.ZONE_MODE;
			}

			// addded by wangql,增加截图功能
			if (selectionMode == SelectionInfoManager.SCREEN_CAST_MODE) {
				if (!dragStarted) {
					boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
					selection.handleSelectionZone(initialDragPoint,
							Selection.SELECTION_ZONE_MOUSE_PRESSED,
							isMultiSelectionEnabled);
				}
			}

			// refreshing the labels displaying the current position of the
			// mouse on the canvas
			selection.refreshSVGFrame(evt.getPoint());

			switch (selectionMode) {

			case SelectionInfoManager.REGULAR_MODE:

				if (!evt.isShiftDown()) {

					selection.doAction(dragStarted ? point : initialDragPoint);

				}
				break;

			case SelectionInfoManager.ZONE_MODE:

				boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
				selection.handleSelectionZone(dragStarted ? point
						: initialDragPoint,
						Selection.SELECTION_ZONE_MOUSE_DRAGGED,
						isMultiSelectionEnabled);
				break;

			case SelectionInfoManager.ZOOM_MODE:

				selection.handleZoomZone(
						dragStarted ? point : initialDragPoint,
						Selection.SELECTION_ZONE_MOUSE_DRAGGED);
				break;

			case SelectionInfoManager.DRAWING_MODE:

				selection
						.drawingAction(dragStarted ? point : initialDragPoint,
								evt.getModifiers(),
								AbstractShape.DRAWING_MOUSE_DRAGGED);
				break;
			case SelectionInfoManager.SCREEN_CAST_MODE:
				boolean multiSelectionEnabled = isMultiSelectionEnabled(evt);
				selection.handleSelectionZone(dragStarted ? point
						: initialDragPoint,
						Selection.SELECTION_ZONE_MOUSE_DRAGGED,
						multiSelectionEnabled);
				break;
			}

			dragStarted = true;

		}
		modeObject.setMouseEvent(evt);
		selection.fireModeSelection(modeSelectionEvent);
	}

	@SuppressWarnings("all")
	public void mouseMoved(MouseEvent evt) {

		// Editor.getEditor().getHandlesManager().getCurrentHandle().getCanvas().
		// setToolTipText("xxx");

		evt = convertEvent(evt);

		if (!isPopUp(evt)) {

			// getting the current selection mode
			int selectionMode = selectionManager.getSelectionMode();

			if (selectionMode != SelectionInfoManager.NONE_MODE) {
				selection.handleCursor(evt.getPoint());

			}

			if (selectionMode == SelectionInfoManager.DRAWING_MODE) {

				// computing the point for this event
				Point2D point = selection.getSVGHandle().getTransformsManager()
						.getAlignedWithRulersPoint(evt.getPoint(), false);
				selection.drawingAction(point, evt.getModifiers(),
						AbstractShape.DRAWING_MOUSE_MOVED);

			}

		}

		Point2D scaledPoint = selection.getSVGHandle().getTransformsManager()
				.getScaledPoint(evt.getPoint(), true);
		Element e = selection.getSVGHandle().getSvgElementsManager().getNodeAt(
				selection.getParentElement(), scaledPoint,
				selection.getSelectionFilter(),selection.getGroupBreaker());
		modeObject.setMouseEvent(evt);
		modeObject.setElementAtMousePoint(e);
		selection.fireModeSelection(modeSelectionEvent);
		selection.fireMouseMove(modeSelectionEvent);
	}

	/**
	 * checks whether this mouse event denotes a popup trigger event or not
	 * 
	 * @param evt
	 *            an event
	 * @return whether this mouse event denotes a popup trigger event or not
	 */
	protected boolean isPopUp(MouseEvent evt) {

		return evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt);
	}

	/**
	 * checks whether this mouse event denotes a multi selection action
	 * 
	 * @param evt
	 *            an event
	 * @return whether this mouse event denotes a multi selection action
	 */
	protected boolean isMultiSelectionEnabled(MouseEvent evt) {

		return evt.isShiftDown();
	}

	/**
	 * converts the provided event to a svg canvas event
	 * 
	 * @param evt
	 *            an event
	 * @return the converted event
	 */
	protected MouseEvent convertEvent(MouseEvent evt) {

		if (!evt.getSource().equals(selection.getSVGHandle().getCanvas())) {

			evt = SwingUtilities.convertMouseEvent((Component) evt.getSource(),
					evt, selection.getSVGHandle().getCanvas());
		}

		return evt;
	}

	/**
	 * 返回
	 * 
	 * @return the bConnectFlag
	 */
	public boolean isBConnectFlag() {
		return bConnectFlag;
	}

}
