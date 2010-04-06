package fr.itris.glips.svgeditor;

import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.actions.menubar.EditorMenuBar;
import fr.itris.glips.svgeditor.actions.popup.PopupManager;
import fr.itris.glips.svgeditor.actions.toolbar.ToolBarManager;

public class SwingCompManager {
	private EditorAdapter editor;
	/**
	 * the menu bar
	 */
	private EditorMenuBar menubar;

	/**
	 * the popup manager
	 */
	private PopupManager popupManager;

	/**
	 * the color manager
	 */
	private ColorManager colorManager;

	/**
	 * the toolBar manager
	 */
	private ToolBarManager toolBarManager;
	
	/**
	 * the resource image manager
	 */
	private ResourceImageManager resourceImageManager;

	public SwingCompManager(EditorAdapter editor) {
		this.editor = editor;
	}

	public void init() {
		// the menu bar
		menubar = new EditorMenuBar(editor);

		// the color manager
		colorManager = new ColorManager(editor);

		// the resource image manager
		resourceImageManager = new ResourceImageManager(editor);
		// the popup menu manager
		popupManager = new PopupManager(editor);

		// the toolBar manager
		toolBarManager = new ToolBarManager(editor);

		// initializes the menu bar
		menubar.init();
	}
}
