package fr.itris.glips.svgeditor.display.canvas.rulers;

import javax.swing.JMenuItem;

import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class handling the grid parameters
 * 
 * @author Jordi SUC
 */
public class RulersParametersManager {

	/**
	 * the id for the rulers enablement preference
	 */
	private static final String RULERS_ENABLED_PREF_ID = "RulersEnabled";

	/**
	 * the id for the horizontal distance preference
	 */
	private static final String ALIGN_WITH_RULERS_PREF_ID = "AlignWithRulers";

	/**
	 * the default value for the rulers enabled boolean
	 */
	protected static final boolean defaultRulersEnabled = true;

	/**
	 * the default value for the align with rulers boolean
	 */
	protected static final boolean defaultAlignWithRulers = false;

	/**
	 * the handles manager
	 */
	private HandlesManager handlesManager;

	/**
	 * whether the rulers are enabled or not
	 */
	private boolean rulersEnabled = false;

	/**
	 * whether to align mouse to rulers when using painting tools
	 */
	protected boolean alignWithRulers = false;

	/**
	 * the constructor of the class
	 * 
	 * @param handlesManager
	 *            the handles manager
	 */
	public RulersParametersManager(HandlesManager handlesManager) {

		this.handlesManager = handlesManager;

		initializeParameters();
	}

	/**
	 * initializes the parameters
	 */
	protected void initializeParameters() {

		// getting the parameters from the preference store
		try {
			if (handlesManager.getEditor().getModeManager().isRulerShown()) {
				rulersEnabled = Boolean.parseBoolean(PreferencesStore
						.getPreference(null, RULERS_ENABLED_PREF_ID));
			} else {
				rulersEnabled = false;
			}
		} catch (Exception ex) {
			rulersEnabled = defaultRulersEnabled;
		}

		try {
			alignWithRulers = Boolean.parseBoolean(PreferencesStore
					.getPreference(null, ALIGN_WITH_RULERS_PREF_ID));
		} catch (Exception ex) {
			alignWithRulers = defaultAlignWithRulers;
		}
	}

	/**
	 * provides all the rulers with their new parameters
	 */
	protected void updateRulers() {

		for (SVGHandle handle : handlesManager.getHandles()) {
			// if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG)
			handle.getScrollPane().updateRulers();
		}

		updatePreferences();
	}

	/**
	 * updates the preferences values
	 */
	protected void updatePreferences() {

		PreferencesStore.setPreference(null, RULERS_ENABLED_PREF_ID, Boolean
				.toString(rulersEnabled));
		PreferencesStore.setPreference(null, ALIGN_WITH_RULERS_PREF_ID, Boolean
				.toString(alignWithRulers));
	}

	/**
	 * @return whether the rulers are enabled or not
	 */
	public boolean areRulersEnabled() {
		if (!handlesManager.getEditor().getModeManager().isRulerShown()) {
			return false;
		}
		return rulersEnabled;
	}

	/**
	 * sets whether the rulers are enabled or not
	 * 
	 * @param rulersEnabled
	 *            whether the rulers are enabled or not
	 */
	public void setRulersEnabled(boolean rulersEnabled) {

		this.rulersEnabled = rulersEnabled;
		try {
			JMenuItem menuItem = ((RulersModule) handlesManager.getEditor()
					.getModule("Rulers")).getDisplayRulersMenuItem();
			if (handlesManager.getEditor().getHandlesManager()
					.getRulersParametersHandler().areRulersEnabled()) {
				menuItem.setText(ResourcesManager.bundle
						.getString("RulersShown"));
			} else {
				menuItem.setText(ResourcesManager.bundle
						.getString("RulersHidden"));
			}
		} catch (NullPointerException e) {

		}
		updatePreferences();
		updateRulers();
	}

	/**
	 * @return whether to align mouse to rulers when using painting tools
	 */
	public boolean alignWithRulers() {
		return alignWithRulers;
	}

	/**
	 * sets whether to align mouse to rulers when using painting tools
	 * 
	 * @param alignWithRulers
	 *            whether to align mouse to rulers when using painting tools
	 */
	public void setAlignWithRulers(boolean alignWithRulers) {

		this.alignWithRulers = alignWithRulers;
		updatePreferences();
	}
}
