package fr.itris.glips.svgeditor.display.canvas.grid;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class handling the grid parameters
 * 
 * @author Jordi SUC
 */
public class GridParametersManager {

	/**
	 * the id for the grid enablement preference
	 */
	private static final String GRID_ENABLED_PREF_ID = "GridEnabled";
	/**
	 * the id for the horizontal distance preference
	 */
	private static final String HORIZONTAL_DISTANCE_PREF_ID = "GridHorizontalDistance";
	/**
	 * the id for the vertical distance preference
	 */
	private static final String VERTICAL_DISTANCE_PREF_ID = "GridVerticalDistance";
	/**
	 * the id for the color preference
	 */
	private static final String COLOR_PREF_ID = "GridColor";
	/**
	 * the id for the stroke preference
	 */
	private static final String STROKE_PREF_ID = "GridStroke";
	/**
	 * the grid parameters dialog
	 */
	private GridParametersDialog gridParametersDialog;
	/**
	 * the default value for the grid enabled boolean
	 */
	protected static final boolean defaultGridEnabled = false;
	/**
	 * the default distances for the grid
	 */
	protected static final double defaultHorizontalDistance = 50,
			defaultVerticalDistance = 50;
	protected static final double NCIGraphUnitHorizontalDistance = 50,
			NCIGraphUnitVerticalDistance = 50;
	/**
	 * the default color of the grid
	 */
	protected static final Color defaultGridColor = new Color(200, 200, 200);
	/**
	 * the default stroke
	 */
	protected static final BasicStroke defaultGridStroke = new BasicStroke(1,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
			new float[] { 2, 3 }, 0);
	/**
	 * the default string representation of the dashes for the stroke
	 */
	protected static final String defaultStrokeDashesValues = "2 3";
	/**
	 * the handles manager
	 */
	private HandlesManager handlesManager;
	/**
	 * whether the grid is enabled or not
	 */
	private boolean gridEnabled = true;
	/**
	 * the horizontal distance for the grid
	 */
	private double horizontalDistance = defaultHorizontalDistance;
	/**
	 * the vertical distance for the grid
	 */
	private double verticalDistance = defaultVerticalDistance;
	/**
	 * the color of the grid
	 */
	private Color gridColor = defaultGridColor;
	/**
	 * the stroke of the grid
	 */
	private BasicStroke gridStroke = defaultGridStroke;
	/**
	 * the string representation of the dashes for the stroke
	 */
	private String strokeDashesValues = "2 3";

	/**
	 * the constructor of the class
	 * 
	 * @param handlesManager
	 *            the handles manager
	 */
	public GridParametersManager(HandlesManager handlesManager) {

		this.handlesManager = handlesManager;

		initializeParameters();

		// creating the dialog used for setting the parameters
		if (handlesManager.getEditor().getParent() instanceof Frame) {

			gridParametersDialog = new GridParametersDialog(this,
					(Frame) handlesManager.getEditor().getParent(),
					handlesManager.getEditor());

		} else if (handlesManager.getEditor().getParent() instanceof JDialog) {

			gridParametersDialog = new GridParametersDialog(this,
					(JDialog) handlesManager.getEditor().getParent(),
					handlesManager.getEditor());
		}
	}

	/**
	 * initializes the grid parameters
	 */
	protected void initializeParameters() {

		// getting the parameters from the preference store
		try {
			if (handlesManager.getEditor().getModeManager().isGridShown()) {
				gridEnabled = Boolean.parseBoolean(PreferencesStore
						.getPreference(null, GRID_ENABLED_PREF_ID));
			}else{
				gridEnabled = false;
			}

		} catch (Exception ex) {
			gridEnabled = defaultGridEnabled;
		}

		try {
			horizontalDistance = Double.parseDouble(PreferencesStore
					.getPreference(null, HORIZONTAL_DISTANCE_PREF_ID));
		} catch (Exception ex) {
			horizontalDistance = defaultHorizontalDistance;
		}

		try {
			verticalDistance = Double.parseDouble(PreferencesStore
					.getPreference(null, VERTICAL_DISTANCE_PREF_ID));
		} catch (Exception ex) {
			verticalDistance = defaultVerticalDistance;
		}

		String colorString = PreferencesStore
				.getPreference(null, COLOR_PREF_ID);

		if (colorString == null || gridColor == null) {
			gridColor = Editor.getColorChooser().getColor(null, colorString);
			gridColor = defaultGridColor;
		}

		// computing the grid stroke
		String dashesStr = PreferencesStore.getPreference(null, STROKE_PREF_ID);
		handleDashes(dashesStr);
	}

	/**
	 * creates the stroke corresponding to the provided dashes string. 虚线的处理
	 * 
	 * @param dashesString
	 *            the string representation of dashes
	 */
	protected void handleDashes(String dashesString) {

		if (dashesString != null) {

			// getting the array of the dash factors
			float[] dashes = DashChooserWidget.getDashes(dashesString);

			if (dashes.length > 0) {

				strokeDashesValues = dashesString;
				gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL, 0, dashes, 0);

			} else {

				gridStroke = null;
				strokeDashesValues = "";
			}

		} else {

			gridStroke = defaultGridStroke;
			strokeDashesValues = defaultStrokeDashesValues;
		}
	}

	/**
	 * launches the dialog used to modify the parameters
	 * 
	 * @param relativeComponent
	 *            the component relatively to which the dialog should be shown
	 */
	public void launchDialog(JComponent relativeComponent) {

		gridParametersDialog.showDialog(relativeComponent);

		if (gridParametersDialog.isCorrectValues()) {

			// updating the parameters of the grid
			double hDist = gridParametersDialog.getHorizontalDistance();
			double vDist = gridParametersDialog.getVerticalDistance();
			Color color = gridParametersDialog.getColor();
			String dashesStr = gridParametersDialog.getDashes();

			if (hDist > 0 && vDist > 0 && color != null) {

				horizontalDistance = hDist;
				verticalDistance = vDist;
				gridColor = color;

				if (dashesStr == null) {

					dashesStr = "";
				}
				// 处理虚线
				handleDashes(dashesStr);
				// 刷新网格
				updateGrids();
			}
		}
	}

	/**
	 * provides all the grids with their new parameters
	 */
	protected void updateGrids() {

		for (SVGHandle handle : handlesManager.getHandles()) {
			// if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG)//added
			// by wangql，只是svg编辑窗口才受影响，而图元管理窗口不会受影响
			handle.getCanvas().getGridManager().refresh();
		}

		updatePreferences();
	}

	/**
	 * updates the preferences values
	 */
	protected void updatePreferences() {

		PreferencesStore.setPreference(null, GRID_ENABLED_PREF_ID, Boolean
				.toString(gridEnabled));

		PreferencesStore.setPreference(null, HORIZONTAL_DISTANCE_PREF_ID,
				FormatStore.format(horizontalDistance));

		PreferencesStore.setPreference(null, VERTICAL_DISTANCE_PREF_ID,
				FormatStore.format(verticalDistance));

		PreferencesStore.setPreference(null, COLOR_PREF_ID, handlesManager
				.getEditor().getColorChooser().getColorString(gridColor));

		PreferencesStore
				.setPreference(null, STROKE_PREF_ID, strokeDashesValues);
	}

	/**
	 * @return whether the grid should be displayed
	 */
	public boolean isGridEnabled() {
		if(!handlesManager.getEditor().getModeManager().isGridShown()){
			return false;
		}
		return gridEnabled;
	}

	/**
	 * sets whether the grid should be displayed
	 * 
	 * @param gridEnabled
	 *            whether the grid should be displayed
	 */
	public void setGridEnabled(boolean gridEnabled) {

		this.gridEnabled = gridEnabled;
		updateGrids();
	}

	/**
	 * @return the grid color
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * @return the grid stroke
	 */
	public BasicStroke getGridStroke() {
		return gridStroke;
	}

	/**
	 * @return the string representation of the dashes for the stroke
	 */
	public String getStrokeDashesValues() {
		return strokeDashesValues;
	}

	/**
	 * @return the horizontal distance
	 */
	public double getHorizontalDistance() {
		return horizontalDistance;
	}

	/**
	 * @return the vertical distance
	 */
	public double getVerticalDistance() {
		return verticalDistance;
	}

	/**
	 * 获取网格的水平间距，added by wangql。
	 * 
	 * @param type
	 *            SVGHandle类型。如果是图元管理（HANDLE_TYPE_GRAPH_UNIT），则返回固定的值。
	 * @return
	 */
	public double getHorizontalDistance(int type) {
		switch (type) {
		case SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD: {
			return NCIGraphUnitHorizontalDistance;
		}
		case SVGHandle.HANDLE_TYPE_SVG: {
			return horizontalDistance;
		}
		default:
			return horizontalDistance;
		}
	}

	/**
	 * 获取网格的垂直间距，added by wangql。
	 * 
	 * @param type
	 *            SVGHandle类型。如果是图元管理（HANDLE_TYPE_GRAPH_UNIT），则返回固定的值。
	 * @return
	 */
	public double getVerticalDistance(int type) {
		switch (type) {
		case SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD: {
			return NCIGraphUnitVerticalDistance;
		}
		case SVGHandle.HANDLE_TYPE_SVG: {
			return verticalDistance;
		}
		default:
			return verticalDistance;
		}
	}
}
