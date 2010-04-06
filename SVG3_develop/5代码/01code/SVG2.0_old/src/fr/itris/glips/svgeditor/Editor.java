package fr.itris.glips.svgeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DragSource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.nci.svg.css.CSSManager;
import com.nci.svg.graphunit.AbstractNCIGraphUnitManager;
import com.nci.svg.graphunit.NCIGraphUnitTypeBean;
import com.nci.svg.layer.LayerSelectionManager;
import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;
import com.nci.svg.mode.EditorModeAdapter;
import com.nci.svg.module.NCIViewEditModule;
import com.nci.svg.search.NCISvgSearchManager;
import com.nci.svg.tooltip.CanvasToolTipManager;
import com.nci.svg.ui.NCIStatusBar;
import com.nci.svg.ui.equip.MetaDataManager;
import com.nci.svg.ui.graphunit.NCIGraphUnitPanel;
import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.ui.outlook.OutlookPane;
import com.nci.svg.ui.outlook.VTextIcon;
import com.nci.svg.util.Constants;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.NCISVGSession;
import com.nci.svg.util.NCISymbolSession;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.actions.clipboard.ClipboardManager;
import fr.itris.glips.svgeditor.actions.menubar.EditorMenuBar;
import fr.itris.glips.svgeditor.actions.popup.PopupManager;
import fr.itris.glips.svgeditor.actions.toolbar.ToolBarManager;
import fr.itris.glips.svgeditor.colorchooser.ColorChooser;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.options.ClosePathModeManager;
import fr.itris.glips.svgeditor.options.ConstraintLinesModeManager;
import fr.itris.glips.svgeditor.options.RemanentModeManager;
import fr.itris.glips.svgeditor.options.SquareModeManager;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * The main class of the editor
 * 
 * @author
 */
public class Editor {

	/**
	 * whether the editor handles the rtda animations
	 */
	public static boolean isRtdaAnimationsVersion = false;
	/**
	 * the parent container
	 */
	private Container parentContainer;
	/**
	 * the desktop pane
	 */
	private JDesktopPane desktop;
	/**
	 * the svg handles manager
	 */
	private HandlesManager svgHandlesManager;
	/**
	 * the module loader
	 */
	private ModuleManager moduleManager;
	/**
	 * the square mode manager
	 */
	private SquareModeManager squareModeManager;
	/**
	 * the remanent mode manager
	 */
	private RemanentModeManager remanentModeManager;
	/**
	 * the close path mode manager
	 */
	private ClosePathModeManager closePathModeManager;
	/**
	 * the constraint line mode manager
	 */
	private ConstraintLinesModeManager constraintLinesModeManager;
	/**
	 * the new clipboard manager
	 */
	private ClipboardManager clipboardManager;
	/**
	 * the io manager
	 */
	private IOManager ioManager;
	/**
	 * the class that gives the resources
	 */
	private ResourcesManager resourcesManager;
	/**
	 * the toolkit object
	 */
	private EditorToolkit toolkit;
	/**
	 * tells whether the mutli windowing method has to be used or not
	 */
	private boolean isMultiWindow = false;
	/**
	 * the map associating a name to a rectangle representing bounds
	 */
	private final Map<String, Rectangle> widgetBounds = new HashMap<String, Rectangle>();
	/**
	 * the svg selection manager
	 */
	private fr.itris.glips.svgeditor.selection.SelectionInfoManager selectionManager;
	/**
	 * the drag source
	 */
	private DragSource dragSource = DragSource.getDefaultDragSource();
	/**
	 * the color chooser of the editor
	 */
	private static ColorChooser colorChooser;
	/**
	 * whether the quit action is disabled
	 */
	private boolean isQuitActionDisabled = false;
	/**
	 * whether the JVM will be exited when the user requires to exit from the
	 * editor
	 */
	private boolean canExitFromJVM = false;
	/**
	 * the set of the runnables that should be run when the editor is exiting
	 */
	private HashSet<Runnable> disposeRunnables = new HashSet<Runnable>();
	/**
	 * the map of the name spaces that should be checked
	 */
	public HashMap<String, String> requiredNameSpaces = new HashMap<String, String>();
	/**
	 * 符号类型的集合
	 */
	ArrayList<NCIGraphUnitTypeBean> symboTypeList;
	/**
	 * 图元集合，key是类型,内部LinkedHashMap key是图元名称
	 */
	private LinkedHashMap<String, LinkedHashMap<String, NCIThumbnailPanel>> thumnailsMap = null;
	/**
	 * outlookpane区域的图元大类面板，根据图元类型区分，key是图元类型
	 */
	private HashMap<String, NCIGraphUnitPanel> outlookGraphUnitPanelMap = new HashMap<String, NCIGraphUnitPanel>();
	/**
	 * the svg editor
	 */
	private Editor editor;
	/**
	 * svg编辑器session
	 */
	private NCISVGSession svgSession;

	private CSSManager cssManager = null;
	/**
	 * 图元session
	 */
	private NCISymbolSession symbolSession;
	private JSplitPane split = new JSplitPane();
	/**
	 * 编辑器状态栏
	 */
	private NCIStatusBar statusBar;
	/**
	 * 图元管理器
	 */
	private AbstractNCIGraphUnitManager graphUnitManager;
	/**
	 * 搜索管理器
	 */
	private NCISvgSearchManager searchManager;
	private NCIGlobal gConfig = new NCIGlobal();
	/**
	 * 编辑器的模式管理适配器
	 */
	private EditorModeAdapter modeManager;
	/**
	 * 编辑器图层管理器
	 */
	private LayerSelectionManager layerSelectionManager;
	/**
	 * ToolTip管理器
	 */
	private com.nci.svg.tooltip.CanvasToolTipManager toolTipManager;

	private MetaDataManager metaDataManager;

	/**
	 * 支持的文件类型格式映射表
	 */
	private Map<String, String> map_SupportFileType = null;

	private boolean applet = false;

	/**
	 * 正在打开图形的lock
	 */
	private ReentrantLock settingFileLock = new ReentrantLock();

	/**
	 * @return the map_SupportFileType
	 */
	public Map<String, String> getMap_SupportFileType() {
		return map_SupportFileType;
	}

	/**
	 * The constructor of the class
	 */
	public Editor() {
		long start = System.nanoTime();

		if (editor != null) {
			System.out.println("editor is not null");
			return;
		}
		editor = this;
		setUIFont(new javax.swing.plaf.FontUIResource("Dialog",
				Font.TRUETYPE_FONT, 12));
		System.out.println("实例化Editor对象耗时：" + (System.nanoTime() - start)
				/ 1000000l + "ms");
	}

	/**
	 * 设置字体
	 * 
	 * @param f
	 */
	private void setUIFont(javax.swing.plaf.FontUIResource f) {

		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements()) {

			Object key = keys.nextElement();

			Object value = UIManager.get(key);

			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);

		}

	}

	private void iniNativeSwingAndLAF() {
		// try {
		// UIManager
		// .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		// } catch (Exception e) {
		//
		// }
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// try {
		// NativeInterfaceHandler.init();
		//
		// } catch (Exception e) {
		//
		// }
		// }
		// });

	}

	private void iniComp_background() {
		new Thread() {
			public void run() {
				colorChooser = new ColorChooser(editor);
			}
		}.start();
	}

	/**
	 * initializing the editor
	 * 
	 * @param parent
	 *            the parent container for the application
	 * @param fileToBeLoaded
	 *            the file to be directly loaded
	 * @param showSplash
	 *            whether the splash screen should be shown or not
	 * @param displayFrame
	 *            whether or not to show the frame
	 * @param quitDisabled
	 *            whether the quit action is disabled
	 * @param exitFromJVM
	 *            whether the JVM will be exited when the user requires to exit
	 *            from the editor
	 * @param disposeRunnable
	 *            the runnable that should be run when exiting the editor
	 */
	public void init(final Container parent, String fileToBeLoaded,
			boolean showSplash, final boolean displayFrame,
			boolean quitDisabled, boolean exitFromJVM, Runnable disposeRunnable) {

		// graphUnitManager = new NCIMultiXMLGraphUnitManager();
		iniNativeSwingAndLAF();
		getSupportFileType();
		iniComp_background();
		if (getGCParam("webflag") == null
				|| ((String) getGCParam("webflag")).equals("1")) {

			iniEquipSymboList();
		}

		// colorChooser = new ColorChooser(this);

		if (parent instanceof JApplet) {

			parentContainer = SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
			applet = true;

		} else {

			parentContainer = parent;
		}

		this.isQuitActionDisabled = quitDisabled;
		this.canExitFromJVM = exitFromJVM;

		if (disposeRunnable != null) {

			this.disposeRunnables.add(disposeRunnable);
		}

		// setting the values for the tooltip manager
		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setReshowDelay(100);

		// creating the toolkit object
		toolkit = new EditorToolkit();

		// creating the managers
		resourcesManager = new ResourcesManager(this);

		ioManager = new IOManager(this);

		iniManagers();

		// the bounds of the widgets contained in the main frame
		Map<String, Rectangle> map = getWidgetBoundsMap();

		if (map != null) {

			widgetBounds.putAll(map);
		}
		statusBar = new NCIStatusBar(this);
		// creating the desktop
		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

		// creating the selection manager
		selectionManager = new fr.itris.glips.svgeditor.selection.SelectionInfoManager(
				this);

		// the module loader is created and initialized
		iniModulesManager();

		split.setBottomComponent(desktop);
		if (!getModeManager().isOutlookPaneCreat()) {
			split.setOneTouchExpandable(false);
			split.setTopComponent(new OutlookPane(this));
			split.setDividerLocation(Constants.EDITOR_SPLIT_DIVIDERSIZE_MIN);
			split.setDividerSize(Constants.EDITOR_SPLIT_DIVIDERSIZE_MIN);
		} else {
			split.setTopComponent(new JLabel("正在计算图元信息..."));
			split.setOneTouchExpandable(true);
			split.setDividerLocation(1);
			split.setDividerSize(1);
		}
		// 显示菜单和工具条
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				while (moduleManager == null || !moduleManager.isInitFinished()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (parent instanceof JFrame) {
					JFrame parentFrame = (JFrame) parent;
					if (getModeManager().isMenubarShown()) {
						parentFrame.setJMenuBar(moduleManager.getMenuBar());
					}
					if (getModeManager().isToolbarShown()) {
						parentFrame.getContentPane()
								.add(
										moduleManager.getToolBarManager()
												.getToolsBar(),
										BorderLayout.NORTH);
					}
					// parentFrame.setVisible(true);
				} else if (parent instanceof JApplet) {
					JApplet applet = (JApplet) parent;
					if (getModeManager().isMenubarShown()) {
						applet.setJMenuBar(moduleManager.getMenuBar());
					}
					if (getModeManager().isToolbarShown()) {
						applet.getContentPane()
								.add(
										moduleManager.getToolBarManager()
												.getToolsBar(),
										BorderLayout.NORTH);
					}
				}
			}
		});

		if (parent instanceof JFrame) {

			JFrame parentFrame = (JFrame) parent;

			// setting the icon
			ImageIcon icon2 = ResourcesManager.getIcon("Editor", false);

			if (icon2 != null && icon2.getImage() != null) {

				parentFrame.setIconImage(icon2.getImage());
			}

			// handling the frame content
			parentFrame.getContentPane().setLayout(new BorderLayout());

			parentFrame.getContentPane().add(statusBar, BorderLayout.SOUTH);

			// parentFrame.getContentPane().add(desktop,
			// BorderLayout.CENTER);//wql 注释掉，换成JSPlitPane

			parentFrame.getContentPane().add(getSplitPane(),
					BorderLayout.CENTER);

			// computing the bounds of the main frame
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
					parentFrame.getGraphicsConfiguration());
			final Rectangle frameBounds = new Rectangle(screenInsets.left,
					screenInsets.top, screenSize.width - screenInsets.left
							- screenInsets.right, screenSize.height
							- screenInsets.top - screenInsets.bottom);
			widgetBounds.put("mainframe", frameBounds);
			parentFrame.pack();
			// parentFrame.setSize(800, 600);
			parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			// getModeManager().initMode();
		} else if (parent instanceof JApplet) {

			JApplet applet = (JApplet) parent;

			applet.getContentPane().setLayout(new BorderLayout());

			applet.getContentPane().add(getSplitPane(), BorderLayout.CENTER);
			applet.getContentPane().add(statusBar, BorderLayout.SOUTH);

			applet.validate();
		}
		// displays the main frame
		if (parentContainer instanceof JFrame) {
			// Invoker.show((JFrame) parentContainer,displayFrame);
			((JFrame) parentContainer).setVisible(displayFrame);
		}
		if (getModeManager().isOutlookPaneCreat()) {
			SwingWorker<JTabbedPane, JTabbedPane> worker = new SwingWorker<JTabbedPane, JTabbedPane>() {
				@Override
				protected JTabbedPane doInBackground() throws Exception {
					JTabbedPane p = createTabbedPane();
					publish(p);
					return p;
				}

				protected void process(List<JTabbedPane> tabs) {
					for (int i = 0; i < tabs.size(); i++) {
						split
								.setDividerLocation(Constants.EDITOR_SPLIT_DIVIDERLOCATION);
						split
								.setDividerSize(Constants.EDITOR_SPLIT_DIVIDERSIZE_NORMAL);
						split.setTopComponent(tabs.get(i));
					}
				}
			};
			worker.execute();
		}
		// LongtermTaskManager.getInstance(this).addAndStartLongtermTask(
		// new LongtermTask("正在计算图元信息...", worker));

		// opening the file specified in the constructor arguments
		File initialFile = null;

		if (fileToBeLoaded != null
				&& !fileToBeLoaded.equals("")
				&& (fileToBeLoaded.endsWith(EditorToolkit.SVG_FILE_EXTENSION) || fileToBeLoaded
						.endsWith(EditorToolkit.SVGZ_FILE_EXTENSION))) {

			// computing the file that should be opened
			try {
				initialFile = new File(new URI(fileToBeLoaded));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (initialFile != null && initialFile.exists()) {
			while (ioManager == null) {
				try {
					System.out.println("IOManager null, waiting...");
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			ioManager.getFileOpenManager().open(initialFile, null);
		}
		// added by wangql,假设起始就是编辑模式
		// if (getModule(NCIViewEditModule.NCI_View_Edit_ModuleID) != null) {
		// if (getModeManager().getMode().equals(
		// EditorModeAdapter.SVGTOOL_MODE_EDIT)) {
		// ((NCIViewEditModule)
		// getModule(NCIViewEditModule.NCI_View_Edit_ModuleID))
		// .setViewEdit(NCIViewEditModule.EDIT_MODE);
		// } else if (getModeManager().getMode().equals(
		// EditorModeAdapter.SVGTOOL_MODE_VIEW_PARTVIEW)) {
		// ((NCIViewEditModule)
		// getModule(NCIViewEditModule.NCI_View_Edit_ModuleID))
		// .setViewEdit(NCIViewEditModule.VIEW_MODE);
		// }
		// }

		// new AppletProxy().setFileContent(strContent);
	}

	/**
	 * 初始化模式管理器
	 */
	private void iniModeManager() {
		String mode = "";
		// 为了朗新的页面设置而采取的判断，新世纪的判断都应该在mode属性中写明
		if (getGConfig().getParam(NCIGlobal.SHOW_TOOLBAR_Str) != null) {
			boolean showToolbar = ((String) getGConfig().getParam(
					NCIGlobal.SHOW_TOOLBAR_Str)).equalsIgnoreCase("false");
			if (showToolbar)
				mode = EditorModeAdapter.SVGTOOL_MODE_VIEW_ONLYVIEW;
			else
				mode = (String) getGConfig().getParam(NCIGlobal.APP_MODE_Str);
		} else {
			mode = (String) getGConfig().getParam(NCIGlobal.APP_MODE_Str);
		}
		try {
			String className = Utilities.findOneAttributeValue("class",
					"/classes/modeAdapter[@mode='" + mode + "']",
					ResourcesManager.getXMLDocument("modeAdapters.xml")
							.getDocumentElement());
			System.out.println(className);
			Class<?>[] classargs = { Editor.class };
			Object[] args = { editor };

			// creates instances of each static module
			System.out.println(className);
			modeManager = (EditorModeAdapter) Class.forName(className)
					.getConstructor(classargs).newInstance(args);

		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void iniManagers() {

		clipboardManager = new ClipboardManager();
		squareModeManager = new SquareModeManager();
		remanentModeManager = new RemanentModeManager();
		closePathModeManager = new ClosePathModeManager();

		// graphUnitManager = new NCISingleXMLGraphUnitManager();

		constraintLinesModeManager = new ConstraintLinesModeManager();
		toolTipManager = new CanvasToolTipManager(editor);
		searchManager = new NCISvgSearchManager();
		svgSession = new NCISVGSession(editor);
		symbolSession = new NCISymbolSession();
		layerSelectionManager = new LayerSelectionManager(this);
	}

	private void iniModulesManager() {
		System.out.println("开始初始化编辑器ModuleManager...");
		// long start = System.nanoTime();
		// System.out.println("New HandlesManager");
		svgHandlesManager = new HandlesManager(this);
		// System.out.println("New ModuleManager");
		moduleManager = new ModuleManager(this);
		System.out.println("ModuleManager start init...");
		new Thread() {
			public void run() {
				moduleManager.init();
				moduleManager.initializeParts();
				getModeManager().initMode();
			}
		}.start();
	}

	/**
	 * 创建左侧TabPane
	 * 
	 * @return
	 */
	private JTabbedPane createTabbedPane() {

		final JTabbedPane tabPane = new JTabbedPane();
		final ArrayList<OutlookPane> outLookPaneList = new ArrayList<OutlookPane>();
		tabPane.setTabPlacement(JTabbedPane.LEFT);
		if (getModeManager().isOutlookPaneCreat()) {
			if (getGraphUnitManager() != null
					&& getGraphUnitManager().getSymbolsTypesDefine().size() > 0) {
				outlook = new OutlookPane[getGraphUnitManager()
						.getSymbolsTypesDefine().size()];
				for (int i = 0; i < getGraphUnitManager()
						.getSymbolsTypesDefine().size(); i++) {
					final String strLabel = getGraphUnitManager()
							.getSymbolsTypesDefine().get(i);
					VTextIcon textIcon = new VTextIcon(tabPane, strLabel, 0);
					OutlookPane oPane = createOutlookPane(i, strLabel);
					tabPane.addTab(null, textIcon, oPane);
					outLookPaneList.add(oPane);
				}
			}
			tabPane.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					int index = tabPane.getSelectedIndex();
					showOtherTypeGraphUnits(outLookPaneList.get(index));

				}

			});
		}
		return tabPane;
	}

	/**
	 * 根据图元大类显示图元
	 * 
	 * @param typeName
	 */
	private void showOtherTypeGraphUnits(OutlookPane oPane) {
		ArrayList<Component> paneComponents = oPane.getPaneComponents();
		if (paneComponents.size() > 0)
			((NCIGraphUnitPanel) paneComponents.get(paneComponents.size() - 1))
					.initGraph();
	}

	private OutlookPane[] outlook;

	/**
	 * 创建图元分类的面板
	 * 
	 * @return
	 */
	private OutlookPane createOutlookPane(int iIndex, String strLabel) {
		outlook[iIndex] = new OutlookPane(this);
		outlook[iIndex].setAnimated(false);
		initOutlookPane(outlook[iIndex], strLabel);
		return outlook[iIndex];
	}

	/**
	 * 在Outlookpane显示给用户后，其实在内部组件中并未生成任何图形对象，在后台需逐步生成。
	 * 
	 * @param outlook
	 */
	private void initOutlookPane(final OutlookPane outlook,
			final String strLabel) {
		synchronized (symbolInitLock) {
			SwingWorker<Object, NCIGraphUnitPanel> worker = new SwingWorker<Object, NCIGraphUnitPanel>() {

				NCIGraphUnitPanel p = null;
				NCIGraphUnitTypeBean bean = null;

				@Override
				protected Object doInBackground() throws Exception {
					try {
						for (int i = 0; i < symboTypeList.size(); i++) {
							// for (NCIGraphUnitTypeBean bean : symboTypeList) {
							bean = symboTypeList.get(i);
							int index = bean.getGraphUnitType().indexOf(
									"=" + strLabel);
							if (index > 0) {
								p = new NCIGraphUnitPanel(bean, getEditor(),
										i == symboTypeList.size() - 1);
								outlookGraphUnitPanelMap.put(
										bean.getGraphUnitType().substring(0,
												index), p);
								publish(p);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					return null;
				}

				@Override
				protected void process(List<NCIGraphUnitPanel> panels) {
					for (int i = 0; i < panels.size(); i++) {
						int index = panels.get(i).getEquipSymbol()
								.getGraphUnitType().indexOf("=" + strLabel);
						outlook.addPane(panels.get(i).getEquipSymbol()
								.getGraphUnitType().substring(0, index), panels
								.get(i));

						if (i % 5 == 0) {// 每加载5个刷新一次
							outlook.repaint();
						}
					}
				}

				@Override
				public void done() {
					outlook.repaint();
				}
			};
			LongtermTaskManager.getInstance(this).addAndStartLongtermTask(
					new LongtermTask("正在加载图元...", worker));
		}
	}

	private void iniEquipSymboList() {
		Thread t = new Thread(new Runnable() {

			public void run() {

				try {
					synchronized (symbolInitLock) {
						thumnailsMap = new LinkedHashMap<String, LinkedHashMap<String, NCIThumbnailPanel>>(
								(int) (getGraphUnitManager().getSymbolsTypes()
										.size() * 1.5));
						long start = System.nanoTime();
						symboTypeList = getGraphUnitManager()
								.getEquipSymbolTypeList();
						System.out
								.println("初始化符号耗时："
										+ (System.nanoTime() - start)
										/ 1000000l + "ms");
					}
				} catch (Exception e) {
					JOptionPane.showConfirmDialog(null, ResourcesManager.bundle
							.getString("nci_svg_initialize_failed"), "Error",
							JOptionPane.CLOSED_OPTION,
							JOptionPane.ERROR_MESSAGE);

					e.printStackTrace();
				}

			}
		});
		t.start();
	}

	private byte[] symbolInitLock = new byte[0];

	/**
	 * exits the editor
	 */
	public void exit() {
		ioManager.getEditorExitManager().exit();
	}

	/**
	 * @return the svg handles manager
	 */
	public HandlesManager getHandlesManager() {
		return svgHandlesManager;
	}

	/**
	 * @return the main frame
	 */
	public Container getParent() {
		return parentContainer;
	}

	/**
	 * shows or hides the editor frame
	 * 
	 * @param visible
	 *            whether the editor frame should be visible or not
	 */
	public void setVisible(final boolean visible) {

		if (parentContainer instanceof JFrame) {

			((JFrame) parentContainer).setVisible(visible);

			if (visible) {

				((JFrame) parentContainer).toFront();

			} else {

				((JFrame) parentContainer).toBack();
			}
		}
	}

	/**
	 * @return whether the user can exit the jvm
	 */
	public boolean isQuitActionDisabled() {
		return isQuitActionDisabled;
	}

	/**
	 * @return whether the mutli windowing method has to be used or not
	 */
	public boolean isMultiWindow() {
		return isMultiWindow;
	}

	/**
	 * @return Returns the dragSource.
	 */
	public DragSource getDragSource() {
		return dragSource;
	}

	/**
	 * @return Returns the colorChooser.
	 */
	public static ColorChooser getColorChooser() {
		while (colorChooser == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		return colorChooser;
	}

	/**
	 * sets the new color chooser
	 * 
	 * @param newColorChooser
	 *            The colorChooser to set.
	 */
	public void setColorChooser(ColorChooser newColorChooser) {

		colorChooser = newColorChooser;
	}

	/**
	 * @return the map containing the bounds of each widget in the main frame
	 */
	protected Map<String, Rectangle> getWidgetBoundsMap() {

		Map<String, Rectangle> map = new HashMap<String, Rectangle>();
		Document doc = ResourcesManager.getXMLDocument("config.xml");

		if (doc != null) {

			Element root = doc.getDocumentElement();

			if (root != null) {

				// getting the node containing the nodes giving the bounds of
				// the widgets in the main frame
				Node cur = null, bounds = null;

				for (cur = root.getFirstChild(); cur != null; cur = cur
						.getNextSibling()) {

					if (cur instanceof Element
							&& cur.getNodeName().equals("bounds")) {

						bounds = cur;
						break;
					}
				}

				if (bounds != null) {

					// filling the map with the bounds
					Rectangle rectBounds = null;
					int x = 0, y = 0, width = 0, height = 0;
					String name, strX, strY, strW, strH;
					Element el = null;

					for (cur = bounds.getFirstChild(); cur != null; cur = cur
							.getNextSibling()) {

						if (cur instanceof Element
								&& cur.getNodeName().equals("widget")) {

							el = (Element) cur;

							// the name of the widget
							name = el.getAttribute("name");

							// getting each value of the bounds
							strX = el.getAttribute("x");
							strY = el.getAttribute("y");
							strW = el.getAttribute("width");
							strH = el.getAttribute("height");

							x = 0;
							y = 0;
							width = 0;
							height = 0;

							try {
								x = Integer.parseInt(strX);
								y = Integer.parseInt(strY);
								width = Integer.parseInt(strW);
								height = Integer.parseInt(strH);
							} catch (Exception ex) {
							}

							// creating the rectangle
							rectBounds = new Rectangle(x, y, width, height);

							// putting the bounds in the map
							if (name != null && !name.equals("")) {

								map.put(name, rectBounds);
							}
						}
					}
				}
			}
		}

		return map;
	}

	/**
	 * @param name
	 *            the name of a widget
	 * @return the preferred bounds of a widget
	 */
	public Rectangle getPreferredWidgetBounds(String name) {

		Rectangle rect = null;

		if (name != null && !name.equals("")) {

			try {
				rect = widgetBounds.get(name);
			} catch (Exception ex) {
			}
		}

		return rect;
	}

	/**
	 * @return the component into which all internal frames will be placed
	 */
	public JDesktopPane getDesktop() {
		return desktop;
	}

	/**
	 * @return the menubar
	 */
	public EditorMenuBar getMenuBar() {
		return moduleManager.getMenuBar();
	}

	/**
	 * @return the tool bar manager
	 */
	public ToolBarManager getToolBarManager() {

		return moduleManager.getToolBarManager();
	}

	/**
	 * @return the popup manager
	 */
	public PopupManager getPopupManager() {
		return moduleManager.getPopupManager();
	}

	/**
	 * @return Returns the resourceImageManager.
	 */
	public ResourceImageManager getResourceImageManager() {
		return moduleManager.getResourceImageManager();
	}

	/**
	 * @return the clip board manager
	 */
	public ClipboardManager getClipboardManager() {
		return clipboardManager;
	}

	/**
	 * @return the module loader
	 */
	public ModuleManager getSVGModuleLoader() {
		return moduleManager;
	}

	/**
	 * @return the toolkit object containing utility methods
	 */
	public EditorToolkit getSVGToolkit() {
		return toolkit;
	}

	/**
	 * @return the selection manager
	 */
	public SelectionInfoManager getSelectionManager() {
		return selectionManager;
	}

	/**
	 * 获取tooltip管理器
	 * 
	 * @return
	 */
	public CanvasToolTipManager getToolTipManager() {
		return this.toolTipManager;
	}

	/**
	 * @return the painter manager
	 */
	public ColorManager getSVGColorManager() {
		return moduleManager.getColorManager();
	}

	/**
	 * @return an object of the class managing the resources
	 */
	public ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public HashMap<String, String> getRequiredNameSpaces() {
		return requiredNameSpaces;
	}

	/**
	 * @return the collection of the shape modules
	 */
	public Set<AbstractShape> getShapeModules() {

		return moduleManager.getShapeModules();
	}

	/**
	 * @param name
	 *            the name of the module
	 * @return the module object
	 */
	public Object getModule(String name) {
		return moduleManager.getModule(name);
	}

	/**
	 * returns a shape module given its id
	 * 
	 * @param moduleId
	 *            the id of a module
	 * @return a shape module given its id
	 */
	public AbstractShape getShapeModule(String moduleId) {

		return moduleManager.getShapeModule(moduleId);
	}

	/**
	 * @return the square mode manager
	 */
	public SquareModeManager getSquareModeManager() {
		return squareModeManager;
	}

	/**
	 * @return the remanent mode manager
	 */
	public RemanentModeManager getRemanentModeManager() {
		return remanentModeManager;
	}

	/**
	 * @return the close path manager
	 */
	public ClosePathModeManager getClosePathModeManager() {
		return closePathModeManager;
	}

	/**
	 * @return the constraint line manager
	 */
	public ConstraintLinesModeManager getConstraintLinesModeManager() {
		return constraintLinesModeManager;
	}

	/**
	 * @return the IO manager
	 */
	public IOManager getIOManager() {
		return ioManager;
	}

	/**
	 * @return whether the JVM will be exited when the user requires to exit
	 *         from the editor
	 */
	public boolean canExitFromJVM() {
		return canExitFromJVM;
	}

	/**
	 * disposes the editor
	 */
	public void dispose() {

		// running the dispose runnables
		for (Runnable run : disposeRunnables) {

			run.run();
		}
	}

	// /**
	// * @return the svg editor
	// */
	// public static Editor getEditor() {
	//
	// return editor;
	// }
	public JSplitPane getSplitPane() {
		return split;
	}

	public void setSplitPane(JSplitPane split) {
		this.split = split;
	}

	public NCIStatusBar getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(NCIStatusBar statusBar) {
		this.statusBar = statusBar;
	}

	/**
	 * 获取图元管理器
	 * 
	 * @return
	 */
	public synchronized AbstractNCIGraphUnitManager getGraphUnitManager() {
		try {
			if (graphUnitManager == null) {
				String className = Utilities.findOneAttributeValue("class",
						"/Managers/GraphUnitManager", ResourcesManager
								.getXMLDocument("ncimanagers.xml")
								.getDocumentElement());
				Class<?>[] classargs = { Editor.class };
				Object[] args = { editor };

				// creates instances of each static module
				graphUnitManager = (AbstractNCIGraphUnitManager) Class.forName(
						className).getConstructor(classargs).newInstance(args);
			}
			return graphUnitManager;
		} catch (Exception e) {
			System.err.println("初始化GraphUnitManager失败！");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取OutlookPane
	 * 
	 * @return
	 */
	public OutlookPane getOutlookPane() {
		JTabbedPane tabPane = (JTabbedPane) split.getTopComponent();
		return this.outlook[tabPane.getSelectedIndex()];
	}

	/**
	 * 获取Editor的Frame对象
	 * 
	 * @return
	 */
	public Frame findParentFrame() {
		Container c = parentContainer;
		while (c != null) {
			if (c instanceof Frame)
				return (Frame) c;

			c = c.getParent();
		}
		return (Frame) null;
	}

	/**
	 * 获取图元缩略面板中的缩略对象集合，key是图元名称
	 * 
	 * @return
	 */
	public LinkedHashMap<String, LinkedHashMap<String, NCIThumbnailPanel>> getThumnailsMap() {
		return thumnailsMap;
	}

	/**
	 * 获取outlook栏中的图元面板panel哈希Map（按图元类型分）
	 * 
	 * @return
	 */
	public HashMap<String, NCIGraphUnitPanel> getOutlookGraphUnitPanelMap() {
		return outlookGraphUnitPanelMap;
	}

	/**
	 * 获取编辑器搜索管理器
	 * 
	 * @return
	 */
	public NCISvgSearchManager getSearchManager() {
		return searchManager;
	}

	/**
	 * 获取编辑器session
	 * 
	 * @return
	 */
	public NCISVGSession getSvgSession() {
		return svgSession;
	}

	/**
	 * 获取图元session
	 * 
	 * @return
	 */
	public NCISymbolSession getSymbolSession() {
		return symbolSession;
	}

	/**
	 * 获取编辑器模式适配器对象
	 * 
	 * @return
	 */
	public synchronized EditorModeAdapter getModeManager() {
		if (modeManager == null) {
			iniModeManager();
		}
		return modeManager;
	}

	/**
	 * 获取图层管理器
	 * 
	 * @return 图层管理器LayerSelectionManager
	 */
	public LayerSelectionManager getLayerSelectionManager() {
		return layerSelectionManager;
	}

	public Editor getEditor() {
		return this;
	}

	/**
	 * @return the gConfig
	 */
	public NCIGlobal getGConfig() {
		return gConfig;
	}

	/**
	 * @param config
	 *            the gConfig to set
	 */
	public void setGConfig(HashMap<String, Object> map_config) {
		if (map_config != null)
			gConfig.putAll(map_config);
	}

	/**
	 * 根据输入的参数名，获取参数值
	 * 
	 * @param strName
	 *            ：参数名
	 * @return：存在的参数值，如不存在则返回null
	 */
	public Object getGCParam(String strName) {
		return gConfig.getParam(strName);
	}

	/**
	 * 根据输入的参数名，设置参数值
	 * 
	 * @param strName
	 *            ：参数名
	 * @param obValue
	 *            ：待设置的参数值
	 */
	public void setGCParam(String strName, Object obValue) {
		gConfig.setParam(strName, obValue);
		return;
	}

	@SuppressWarnings("unchecked")
	public synchronized void getSupportFileType() {
		new Thread() {
			public void run() {
				if (getGCParam("webflag") == null
						|| ((String) getGCParam("webflag")).equals("1")) {
					ArrayList<String> list = null;
					try {
						list = (ArrayList<String>) Utilities
								.communicateWithURL((String) editor
										.getGCParam("appRoot")
										+ (String) editor
												.getGCParam("servletPath")
										+ "?action=getSupportFileType", null);
						if (list != null && list.size() > 0) {
							map_SupportFileType = new HashMap<String, String>();
							Iterator<String> iterators = list.iterator();
							while (iterators.hasNext()) {
								String str = iterators.next();
								String[] strTmp = str.split("=");
								if (strTmp.length == 2) {
									map_SupportFileType.put(strTmp[1],
											strTmp[0]);
								}
							}
							// System.out.println(map_SupportFileType);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (map_SupportFileType == null) {
					map_SupportFileType = new HashMap<String, String>();
					map_SupportFileType.put("系统图", "0");

				}
			}
		}.start();

	}

	public synchronized String getSupportTypeKeyFromMap(int index) {
		if (map_SupportFileType == null || map_SupportFileType.size() <= index)
			return null;
		return (String) map_SupportFileType.keySet().toArray()[index];
	}

	public synchronized String getSupportTypeValueFromMap(int index) {
		if (map_SupportFileType == null || map_SupportFileType.size() <= index)
			return null;
		return (String) map_SupportFileType.values().toArray()[index];
	}

	public synchronized int getSupportFileTypeSize() {
		if (map_SupportFileType == null)
			return 0;
		return map_SupportFileType.size();
	}

	/**
	 * 获取CSS管理器
	 * 
	 * @return
	 */
	public synchronized CSSManager getCSSManager() {
		try {
			if (cssManager == null) {
				String className = Utilities.findOneAttributeValue("class",
						"/Managers/CSSManager", ResourcesManager
								.getXMLDocument("ncimanagers.xml")
								.getDocumentElement());
				Class<?>[] classargs = { Editor.class };
				Object[] args = { editor };
				cssManager = (CSSManager) Class.forName(className)
						.getConstructor(classargs).newInstance(args);
			}
			return cssManager;
		} catch (Exception e) {
			System.err.println("初始化CSSManager失败！");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取metadata管理器
	 * 
	 * @return
	 */
	public synchronized MetaDataManager getMetaDataManager() {
		try {
			if (metaDataManager == null) {
				String className = Utilities.findOneAttributeValue("class",
						"/Managers/MetaDataManager", ResourcesManager
								.getXMLDocument("ncimanagers.xml")
								.getDocumentElement());
				Class<?>[] classargs = { Editor.class };
				Object[] args = { editor };
				metaDataManager = (MetaDataManager) Class.forName(className)
						.getConstructor(classargs).newInstance(args);
			}
			return metaDataManager;
		} catch (Exception e) {
			System.err.println("初始化MetaDataManager失败！");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断是否是applet
	 * 
	 * @return
	 */
	public boolean isApplet() {
		return applet;
	}

	/**
	 * 获取打开文件的锁
	 * 
	 * @return
	 */
	public Lock getSettingFileLock() {
		return settingFileLock;
	}
}
