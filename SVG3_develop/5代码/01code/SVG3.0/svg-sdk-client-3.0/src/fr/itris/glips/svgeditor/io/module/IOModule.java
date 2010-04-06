package fr.itris.glips.svgeditor.io.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jdesktop.swingworker.SwingWorker;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class of the IO module used to provide widgets for the user to interact
 * with the file system. 输入输出模块，包括文件的打开、保存、转换、图像的关闭、退出等操作。
 * 
 * @author Jordi SUC
 */
public class IOModule extends ModuleAdapter {

	/**
	 * the id of the module
	 */
	protected static final String id = "IOModule";
	/**
	 * the array of the ids of the io manager
	 */
	protected String[] ids = { "FileNew", "FileOpen", "FileOpenPersonal",
			"FileOpenRecent", "FileSave", "FileSaveToServer", "FileSaveAs",
			"FileExport", "FilePrint", "FileClose", "EditorExit",
			"FileOpenStandard","FileProperties" };
	protected String[] accelerators = { "ctrl N", "ctrl O", null, null,
			"ctrl S", null, "ctrl shift S", null, "ctrl P", "ctrl W", "atl F4",
			null,"ctrl shift P" };
	/**
	 * the icons for the square mode
	 */
	protected Icon[] icons, disabledIcons;
	/**
	 * the label for the menu and tool item
	 */
	protected String[] labels;
	/**
	 * the menu items that are displayed in the menu bar
	 */
	protected JMenuItem[] menuItems;
	/**
	 * the open recent menu manager
	 */
	private OpenRecentMenuManager openRecentMenuManager;
	/**
	 * the export menu manager
	 */
	private ExportMenuManager exportMenuManager;
	protected AbstractButton exitButton = null;
	private FileNewMenuManager fileNewMenuManager = null;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public IOModule(EditorAdapter editor) {
		super(editor);
		final IOModule io = this;
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				openRecentMenuManager = new OpenRecentMenuManager(io
						.getEditor());
				exportMenuManager = new ExportMenuManager(io);
				fileNewMenuManager = new FileNewMenuManager(io);
				createMenuItems();
			}
		});

	}

	public String getName() {
		return id;
	}

	/**
	 * creates the menu items
	 */
	protected void createMenuItems() {

		// creating the arrays
		labels = new String[ids.length];
		icons = new Icon[ids.length];
		disabledIcons = new Icon[ids.length];
		menuItems = new JMenuItem[ids.length];
		menuItems[0] = fileNewMenuManager.getMenu();
		// setting the open recent menu
		menuItems[3] = openRecentMenuManager.getMenu();

		// setting the export menu
		menuItems[7] = exportMenuManager.getMenu();

		// creating the items and setting their properties
		ActionListener listener = null;

		for (int i = 0; i < ids.length; i++) {

			final int index = i;

			// getting the labels
			labels[i] = ResourcesManager.bundle.getString(ids[i] + "ItemLabel");

			// getting the icons
			icons[i] = ResourcesManager.getIcon(ids[i], false);
			disabledIcons[i] = ResourcesManager.getIcon(ids[i], true);

			if (i != 0 && i != 3 && i != 7) {

				// creating the menu items
				menuItems[i] = new JMenuItem();

				if (accelerators[i] != null) {

					menuItems[i].setAccelerator(KeyStroke
							.getKeyStroke(accelerators[i]));
				}

				// creating the listener
				listener = new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						doAction(index);
					}
				};

				menuItems[i].addActionListener(listener);
			}

			// setting the properties of the menu items
			menuItems[i].setText(labels[i]);
			menuItems[i].setIcon(icons[i]);
			menuItems[i].setDisabledIcon(disabledIcons[i]);
		}

		exitButton = new JButton();
		exitButton.setIcon(icons[ids.length - 1]);
		exitButton.setDisabledIcon(icons[ids.length - 1]);
		exitButton.setEnabled(true);
		exitButton.setToolTipText(labels[ids.length - 1]);
		ActionListener exitListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Utilities.executeRunnable(new Runnable() {
					public void run() {
						editor.getIOManager().getEditorExitManager().exit();
					}
				});
			}
		};
		exitButton.addActionListener(exitListener);

		// adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager = editor.getHandlesManager();

		svgHandleManager.addHandlesListener(new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {

				handleItemsState();
			}
		});

		handleItemsState();
	}

	/**
	 * handles the tools state
	 */
	protected void handleItemsState() {

		// getting the current handle
		SVGHandle currentHandle = editor.getHandlesManager().getCurrentHandle();

		// for(int i=3; i<7; i++){
		for (int i = 4; i < ids.length ; i++) {
			// if (i != 6) {
			menuItems[i].setEnabled(currentHandle != null);
            if(i == 11)
            	menuItems[11].setEnabled(true);
		}
		if (currentHandle != null) {
			if (currentHandle.getHandleType() != SVGHandle.HANDLE_TYPE_SVG) {
				menuItems[12].setEnabled(false);
			}
		}
		exportMenuManager.handleItemsState(currentHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.ModuleAdapter#getToolItems()
	 */
	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		HashMap<String, AbstractButton> map = new HashMap<String, AbstractButton>();
		map.put(ids[ids.length - 1], exitButton);

		return map;
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();

		for (int i = 0; i < ids.length; i++) {

			if (menuItems[i] != null) {

				map.put(ids[i], menuItems[i]);
			}
		}

		return map;
	}

	/**
	 * executes the action corresponding to the provided index
	 * 
	 * @param index
	 *            the index of an option manager
	 */
	protected void doAction(int index) {

		SVGHandle currentHandle = editor.getHandlesManager().getCurrentHandle();

		switch (index) {

		case 0:

			editor.getIOManager().getFileNewManager().askForNewFileParameters(
					menuItems[index]);
			break;

		case 1:

			editor.getIOManager().getFileOpenManager().askUserForFile(
					menuItems[index]);
			break;
		case 2:

			editor.getIOManager().getFileOpenManager().askForPersonalFile();
			break;

		case 4:
			editor.getIOManager().getFileSaveManager().saveHandleDocument(
					currentHandle, false, menuItems[index]);
			break;
		case 5:
			if (currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG
					|| currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD) {
				editor.getIOManager().getFileSaveManager()
						.saveHandleDocumentToServer(currentHandle,
								menuItems[index]);
			} else if (currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
					|| currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
				editor.getIOManager().getFileSaveManager()
						.saveSymbolDocToServer(currentHandle,
								menuItems[index]);
			} else { // 除了svg和图元、模板之外其他的文件类型
				System.out.println("not defined yet");
			}
			break;
		case 6:
			switch (currentHandle.getHandleType()) {

			case SVGHandle.HANDLE_TYPE_SVG:
				editor.getIOManager().getFileSaveManager().saveHandleDocument(
						currentHandle, true, menuItems[index]);
				break;
			default:
				editor.getIOManager().getFileSaveManager()
						.saveGraphUnitHandleDocument(currentHandle, true,
								menuItems[index]);
				break;
			}
			break;
		case 8:

			editor.getIOManager().getFilePrint().print(currentHandle);
			break;

		case 9:

			editor.getIOManager().getFileCloseManager().closeHandle(
					currentHandle, menuItems[index]);
			break;

		case 10:
			SwingWorker<Object, Object> w = new SwingWorker<Object, Object>() {

				@Override
				protected Object doInBackground() throws Exception {
					editor.getIOManager().getEditorExitManager().exit();
					return null;
				}
			};
			w.execute();
			break;
		case 11: {
			editor.getIOManager().getFileOpenManager().askForStandardFile();
		}
			break;
		case 12:
		{
			if(currentHandle != null && currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG)
			{
				currentHandle.getCanvas().showFileProperties();
			}
			break;
		}
		}

	}
}
