package fr.itris.glips.svgeditor.selection;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JToggleButton;

import com.nci.svg.util.NCISymbolSession;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;

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

	public static final int SCREEN_CAST_MODE = 110;
	
	
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
	
	
	private Editor editor;
	
	public SelectionInfoManager(Editor editor){
		this.editor = editor;
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
		this.drawingShape = drawingShape;

		// setting the sub mode of each svg selection object to the regular sub
		// mode
		Set<SVGHandle> handles = new HashSet<SVGHandle>(editor
				.getHandlesManager().getHandles());

		for (SVGHandle handle : handles) {

			handle.getSelection().selectionModeChanged();
			handle.getSelection().setSelectionSubMode(
					Selection.REGULAR_SUB_MODE);
		}

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
		this.drawingShape = null;

		// setting the sub mode of each svg selection object to the regular sub
		// mode
		Set<SVGHandle> handles = new HashSet<SVGHandle>(editor
				.getHandlesManager().getHandles());

		for (SVGHandle handle : handles) {

			handle.getSelection().setSelectionSubMode(
					Selection.REGULAR_SUB_MODE);
		}
	}

	/**
	 * sets the new mode to regular
	 */
	public void setToRegularMode() {

		// setting the mode to REGULAR and removing the cancel runnable
		this.selectionMode = REGULAR_MODE;
		this.drawingShape = null;

		// setting the sub mode of each svg selection object to the regular sub
		// mode
		Set<SVGHandle> handles = new HashSet<SVGHandle>(editor
				.getHandlesManager().getHandles());

		for (SVGHandle handle : handles) {

			handle.getSelection().setSelectionSubMode(
					Selection.REGULAR_SUB_MODE);
		}

		regularButton.setSelected(true);
		// added by wangql
//		NCISymbolSession.clearSelectedThumbnail();
		editor.getSymbolSession().clearSelectedThumbnail();

	}
	/**
	 * 截图的位置
	 */
	private Rectangle screenCastRect;
	/**
	 * 设置要截图的位置
	 * @param rect
	 */
	public void setScreenCastRect(Rectangle rect){
		screenCastRect = rect;
	}
	/**
	 * 获取截图的位置
	 * @return
	 */
	public Rectangle getScreenCastRect(){
		return screenCastRect;
	}
}
