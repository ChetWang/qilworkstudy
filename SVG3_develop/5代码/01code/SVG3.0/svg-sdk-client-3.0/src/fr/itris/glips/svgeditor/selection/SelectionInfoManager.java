package fr.itris.glips.svgeditor.selection;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JToggleButton;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.svgeditor.selection.DrawingShapeChangeListener;
import com.nci.svg.sdk.ui.terminal.TerminalDialog;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * the selection manager storing information for all the svg documents about the
 * selections
 * 
 * @author ITRIS, Jordi SUC
 */
public class SelectionInfoManager {

	/**
	 * nothing should be done by the selection managers when in this mode
	 */
	public static final int NONE_MODE = 0;

	/**
	 * the regular selection type
	 */
	public static final int REGULAR_MODE = 1;

	/**
	 * the zone selection type
	 */
	public static final int ZONE_MODE = 2;

	/**
	 * the zoom selection type
	 */
	public static final int ZOOM_MODE = 3;

	/**
	 * the drawing selection type
	 */
	public static final int DRAWING_MODE = 4;

	/**
	 * the selection items action mode, i.e. the mode that enables to execute
	 * actions above selection items
	 */
	public static final int ITEMS_ACTION_MODE = 5;

	/**
	 * add by yuxiang,2008.12.03 平移操作用
	 */
	public static final int CANVAS_MOVE_MODE = 20;

	public static final int SCREEN_CAST_MODE = 110;

	/**
	 * add by yux,2009-1-6 手工拓扑点模式
	 */
	public static final int MANUAL_TERMINAL_MODE = 120;
	/**
	 * the selection mode
	 */
	private int selectionMode = REGULAR_MODE;

	/**
	 * the tool button corresponding to the regular mode and the regular sub
	 * mode
	 */
	private JToggleButton regularButton;

	/**
	 * the current shape manager for the drawing mode
	 */
	private fr.itris.glips.svgeditor.shape.AbstractShape drawingShape;

	private EditorAdapter editor;

	private TerminalDialog terminalmodule = null;

	private List<ActionListener> setToRegularModeListeners = null;

	private List<DrawingShapeChangeListener> drawingShapeChangeListeners = new Vector<DrawingShapeChangeListener>();

	public SelectionInfoManager(EditorAdapter editor) {
		this.editor = editor;
		setToRegularModeListeners = new Vector<ActionListener>();
	}

	public void addSetToRegularListener(ActionListener l) {
		setToRegularModeListeners.add(l);
	}

	/**
	 * sets the tool button corresponding to the regular mode and the regular
	 * sub mode
	 * 
	 * @param regularButton
	 *            a tool button
	 */
	public void setRegularButton(JToggleButton regularButton) {

		this.regularButton = regularButton;
	}

	/**
	 * sets the new selection mode.设置选择模式，并指定选择的图元
	 * 
	 * @param selectionMode
	 *            the selection mode
	 * @param drawingShape
	 *            shape module or null if the mode is not the drawing mode
	 */
	public void setSelectionMode(int selectionMode,
			fr.itris.glips.svgeditor.shape.AbstractShape drawingShape) {

		this.selectionMode = selectionMode;
		setDrawingShape(drawingShape);
		this.terminalmodule = null;

		// setting the sub mode of each svg selection object to the regular sub
		// mode
		Set<SVGHandle> handles = new HashSet<SVGHandle>(editor
				.getHandlesManager().getHandles());

		for (SVGHandle handle : handles) {

			handle.getSelection().selectionModeChanged();
			handle.getSelection().setSelectionSubMode(
					Selection.REGULAR_SUB_MODE);
		}
		fireModeChanged(new ActionEvent(this, selectionMode, null));
	}

	public void fireModeChanged(ActionEvent e) {
		for (ActionListener lis : modeChangeActions) {
			lis.actionPerformed(e);
		}
	}

	private List<ActionListener> modeChangeActions = new Vector<ActionListener>();

	public void addModeChangeListener(ActionListener lis) {
		modeChangeActions.add(lis);
	}

	/**
	 * @return the current shape module, used to draw shapes
	 */
	public fr.itris.glips.svgeditor.shape.AbstractShape getDrawingShape() {
		return drawingShape;
	}

	/**
	 * @return the current selection mode
	 */
	public int getSelectionMode() {
		return selectionMode;
	}

	/**
	 * sets the selection mode to none
	 */
	public void setToNoneMode() {

		// setting the mode to NONE and recording the cancel runnable
		this.selectionMode = NONE_MODE;
		setDrawingShape(null);
		this.terminalmodule = null;

		// setting the sub mode of each svg selection object to the regular sub
		// mode
		Set<SVGHandle> handles = new HashSet<SVGHandle>(editor
				.getHandlesManager().getHandles());

		for (SVGHandle handle : handles) {

			handle.getSelection().setSelectionSubMode(
					Selection.REGULAR_SUB_MODE);
		}
		fireModeChanged(new ActionEvent(this, selectionMode, null));
	}

	/**
	 * sets the new mode to regular
	 */
	public void setToRegularMode() {

		// setting the mode to REGULAR and removing the cancel runnable
		this.selectionMode = REGULAR_MODE;
		setDrawingShape(null);
		this.terminalmodule = null;

		// setting the sub mode of each svg selection object to the regular sub
		// mode
		Set<SVGHandle> handles = new HashSet<SVGHandle>(editor
				.getHandlesManager().getHandles());

		for (SVGHandle handle : handles) {

			handle.getSelection().setSelectionSubMode(
					Selection.REGULAR_SUB_MODE);
		}
		// added by wangql
		editor.getSymbolSession().clearSelectedBtnGroup();

		regularButton.setSelected(true);
		fireModeChanged(new ActionEvent(this, selectionMode, null));
		fireSetToRegularMode(new ActionEvent(this, 1, "regular"));
	}

	private void fireSetToRegularMode(ActionEvent e) {
		for (ActionListener l : setToRegularModeListeners) {
			l.actionPerformed(e);
		}
	}

	/**
	 * add by yuxiang,2008.12.03 设置成平移模式
	 */
	public void setToCanvasMoveMode() {
		this.selectionMode = CANVAS_MOVE_MODE;
		setDrawingShape(null);
		this.terminalmodule = null;
	}

	/**
	 * add by yux,2009-1-6 设置成手动设置拓扑点的方式
	 */
	public void setToManualTerminalMode(TerminalDialog terminalmodule) {
		this.selectionMode = MANUAL_TERMINAL_MODE;
		setDrawingShape(null);
		this.terminalmodule = terminalmodule;
	}

	/**
	 * 截图的位置
	 */
	private Rectangle screenCastRect;

	/**
	 * 设置要截图的位置
	 * 
	 * @param rect
	 */
	public void setScreenCastRect(Rectangle rect) {
		screenCastRect = rect;
	}

	/**
	 * 获取截图的位置
	 * 
	 * @return
	 */
	public Rectangle getScreenCastRect() {
		return screenCastRect;
	}

	/**
	 * 返回
	 * 
	 * @return the terminalmodule
	 */
	public TerminalDialog getTerminalmodule() {
		return terminalmodule;
	}

	public void setDrawingShape(
			fr.itris.glips.svgeditor.shape.AbstractShape drawingShape) {
		try {
			fireDrawingShapeChanged(this.drawingShape, drawingShape);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.drawingShape = drawingShape;
	}

	public void addDrawingShapeChangeListener(DrawingShapeChangeListener lis) {
		drawingShapeChangeListeners.add(lis);
	}

	public void removeDrawingShapeChangeListener(DrawingShapeChangeListener lis) {
		drawingShapeChangeListeners.remove(lis);
	}

	protected void fireDrawingShapeChanged(AbstractShape previousShape,
			AbstractShape currentShape) {
		for (DrawingShapeChangeListener lis : drawingShapeChangeListeners) {
			lis.drawingShapeChanged(previousShape, currentShape);
		}
	}
}
