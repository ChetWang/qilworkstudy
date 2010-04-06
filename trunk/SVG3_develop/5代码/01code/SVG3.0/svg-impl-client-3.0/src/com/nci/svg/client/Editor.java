package com.nci.svg.client;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

import com.nci.svg.client.communication.ServletImpl;
import com.nci.svg.client.datamanage.DataManageImpl;
import com.nci.svg.client.graphmanager.GraphManagerImpl;
import com.nci.svg.client.graphunit.NCIDBSymbolManager;
import com.nci.svg.client.indunorm.IndunormManager;
import com.nci.svg.client.layer.LayerSelectionManagerImpl;
import com.nci.svg.client.logger.ClientLoggerImpl;
import com.nci.svg.sdk.CodeConstants;
import com.nci.svg.sdk.NCIClassLoader;
import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleIndunormBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.GraphManagerAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.communication.CommunicationAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.NCISVGSession;
import com.nci.svg.sdk.client.util.NCISymbolSession;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.AbstractSymbolManager;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.layer.LayerSelectionManager;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.logntermtask.LongtermTask;
import com.nci.svg.sdk.logntermtask.LongtermTaskManager;
import com.nci.svg.sdk.mode.EditorModeAdapter;
import com.nci.svg.sdk.module.IndunormModuleAdapter;
import com.nci.svg.sdk.search.NCISvgSearchManager;
import com.nci.svg.sdk.symbol.PropertyModelInteractor;
import com.nci.svg.sdk.tooltip.CanvasToolTipManager;
import com.nci.svg.sdk.ui.GradientDesktopPane;
import com.nci.svg.sdk.ui.NCIStatusBar;
import com.nci.svg.sdk.ui.graphunit.GraphUnitOutlookPanel;
import com.nci.svg.sdk.ui.graphunit.NCISymbolPanel;
import com.nci.svg.sdk.ui.graphunit.search.GraphUnitSimpleSearchPanel;
import com.nci.svg.sdk.ui.outlook.OutlookPane;
import com.nci.svg.sdk.ui.outlook.VTextIcon;

import fr.itris.glips.svgeditor.ColorManager;
import fr.itris.glips.svgeditor.ModuleManager;
import fr.itris.glips.svgeditor.ResourceImageManager;
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
public class Editor extends EditorAdapter {

	/**
	 * ģ���UUID
	 */
	public final static String MODULE_ID = "75bd9c5d-4dd6-4fd0-a61a-353ff1b82d65";
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
	// /**
	// * �������͵ļ���
	// */
	// protected ArrayList<SymbolTypeBean> symboTypeList;
	// /**
	// * ͼԪ���ϣ�key������,�ڲ�LinkedHashMap key��ͼԪ����
	// */
	// private LinkedHashMap<String, LinkedHashMap<String, NCIThumbnailPanel>>
	// thumnailsMap = null;
	/**
	 * outlookpane�����ͼԪ������壬����ͼԪ�������֣�key��ͼԪ����
	 */
	private HashMap<SymbolTypeBean, NCISymbolPanel> outlookSymbolPanelMap = new HashMap<SymbolTypeBean, NCISymbolPanel>();
	/**
	 * the svg editor
	 */
	private Editor editor;
	/**
	 * svg�༭��session
	 */
	private NCISVGSession svgSession;
	/**
	 * ͼԪsession
	 */
	private NCISymbolSession symbolSession;
	/**
	 * ��splitpane����outlookpane��subsplit
	 */
	private JSplitPane split = new JSplitPane();
	/**
	 * �ڶ���splitpane����jdesktop��subsubsplit
	 */
	private JSplitPane subsplit = new JSplitPane();
	/**
	 * ������splitpane
	 */
	private JSplitPane subsubSplit = null;
	/**
	 * �༭��״̬��
	 */
	private NCIStatusBar statusBar;

	/*
	 * ͼԪ������
	 */
	private AbstractSymbolManager symbolManager;
	/**
	 * ����������
	 */
	private NCISvgSearchManager searchManager;
	// private NCIGlobal gConfig = new NCIGlobal();
	/**
	 * �༭����ģʽ����������
	 */
	private EditorModeAdapter modeManager;
	/**
	 * �༭��ͼ�������
	 */
	private LayerSelectionManager layerSelectionManager;
	/**
	 * ToolTip������
	 */
	private CanvasToolTipManager toolTipManager;
	/**
	 * ֧�ֵ��ļ����͸�ʽӳ���
	 */
	private Map<String, String> map_SupportFileType = null;
	private boolean applet = false;
	/**
	 * ���ڴ�ͼ�ε�lock
	 */
	private ReentrantLock settingFileLock = new ReentrantLock();
	private GraphUnitOutlookPanel outlookPanel = null;
	/**
	 * add by yux,2008.12.9 ͼ����Ӧ�������
	 */
	private GraphManagerAdapter graphManager = null;
	/**
	 * add by yux,2008.12.10 ͨѶ���
	 */
	private CommunicationAdapter communiactor = null;
	/**
	 * add by yux,2008.12.10 ���ݹ������
	 */
	private DataManageImpl dataManage = null;
	/**
	 * add by yux,2008.12.26����ҵ�淶���������
	 */
	private IndunormManager indunormManager = null;
	/**
	 * ���ԣ�ģ������symbol�໥������
	 */
	private PropertyModelInteractor propertyModelInteractor = null;

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
		moduleUUID = MODULE_ID;
		if (editor != null) {
			System.out.println("editor is not null");
			return;
		}
		editor = this;

		getLogger().log(
				this,
				LoggerAdapter.DEBUG,
				"ʵ����Editor�����ʱ��" + (System.nanoTime() - start) / 1000000l
						+ "ms");
	}

	public Editor(String bussSysID) {
		this.busiSysID = bussSysID;
		long start = System.nanoTime();
		moduleUUID = MODULE_ID;
		if (editor != null) {
			System.out.println("editor is not null");
			return;
		}
		editor = this;

		getLogger().log(
				this,
				LoggerAdapter.DEBUG,
				"ʵ����Editor�����ʱ��" + (System.nanoTime() - start) / 1000000l
						+ "ms");
	}

	/**
	 * ��������
	 * 
	 * @param f
	 */
	private void setUIFont(javax.swing.plaf.FontUIResource f) {

		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements()) {

			Object key = keys.nextElement();

			Object value = UIManager.get(key);

			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, f);
			}

		}

	}

	private void iniNativeSwingAndLAF() {
		try {
			// PlasticLookAndFeel
			// .setPlasticTheme(new
			// com.jgoodies.looks.plastic.theme.ExperienceRoyale());
			// PlasticLookAndFeel laf = new
			// com.jgoodies.looks.plastic.PlasticXPLookAndFeel();
			// UIManager.setLookAndFeel(laf);
			Utilities.installLAF();
			setUIFont(new javax.swing.plaf.FontUIResource("Dialog",
					Font.TRUETYPE_FONT, 12));
		} catch (Exception e) {
		}
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
		Utilities.executeRunnable(new Runnable() {

			public void run() {
				colorChooser = new ColorChooser(editor);
			}
		});
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
		if (parent instanceof JApplet) {

			parentContainer = SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
			applet = true;

		} else {

			parentContainer = parent;
		}
		// graphUnitManager = new NCIMultiXMLGraphUnitManager();

		dataManage = new DataManageImpl(this);

		// ����ϵͳ��������
		dataManage.loadLocal(DataManageAdapter.KIND_SYSSET, null);
		svgSession = new NCISVGSession(editor);
		communiactor = new ServletImpl(this);
		// communiactor.startHeartBeat();
		iniNativeSwingAndLAF();
		indunormManager = new IndunormManager(this);

		iniComp_background();
		// ��ʼ�������

		if (getGCParam(SysSetDefines.WEB_FLAG) == null
				|| ((String) getGCParam(SysSetDefines.WEB_FLAG)).equals("1")) {
			// Զ�������������
			System.out.println("������ʼ:" + System.currentTimeMillis());
			childUpgrade();

			// Զ�̻�ȡ�����
			dataManage.loadRemote(DataManageAdapter.KIND_CODES, null);
			// Զ�̻�ȡͼԪ��Ϣ
			dataManage.loadRemote(DataManageAdapter.KIND_SYMBOL, null);
			dataManage.loadRemote(DataManageAdapter.KIND_INDUNORM, null);
			dataManage.loadRemote(DataManageAdapter.KIND_MODEL, null);
			dataManage.loadRemote(DataManageAdapter.KIND_MODELRELAINDUNORM,
					null);
			System.out.println("��������:" + System.currentTimeMillis());
			HashMap<String, SimpleIndunormBean> map = indunormManager
					.getIndunormList();
			dataManage.setData(DataManageAdapter.KIND_GLOBAL,
					IndunormModuleAdapter.DATA_ID, map);
			// dataManage.loadRemote(DataManageAdapter.KIND_TEMPLATE, null);
		} else {
			// ��ȡ������������б�

			// ���ػ�ȡ�����
			dataManage.loadLocal(DataManageAdapter.KIND_CODES, null);
			// ���ض�ȡͼԪ��Ϣ
			dataManage.loadLocal(DataManageAdapter.KIND_SYMBOL, null);
			dataManage.loadLocal(DataManageAdapter.KIND_GLOBAL, null);
			// dataManage.loadLocal(DataManageAdapter.KIND_TEMPLATE, null);
		}
		// ���ظ����
		loadPlatModules();
		// ��ȡ��ϵͳҵ����֧�ֵ�ͼ����
		getSupportFileType();
		// colorChooser = new ColorChooser(this);

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
		// desktop = new JDesktopPane();
		desktop = new GradientDesktopPane();
		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

		// creating the selection manager
		selectionManager = new fr.itris.glips.svgeditor.selection.SelectionInfoManager(
				this);

		// the module loader is created and initialized
		iniModulesManager();

		split.setBottomComponent(subsplit);
		subsplit.setTopComponent(desktop);
		if (!getModeManager().isOutlookPaneCreate()) {
			split.setOneTouchExpandable(false);
			split.setTopComponent(new OutlookPane(this));
			split.setDividerLocation(Constants.EDITOR_SPLIT_DIVIDERSIZE_MIN);
			split.setDividerSize(Constants.EDITOR_SPLIT_DIVIDERSIZE_MIN);
		} else {
			split.setTopComponent(new JLabel("���ڼ���ͼԪ��Ϣ..."));
			split.setOneTouchExpandable(true);
			split.setDividerLocation(1);
			split.setDividerSize(1);
		}
		// ��ʾ�˵��͹�����
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
			// BorderLayout.CENTER);//wql ע�͵�������JSPlitPane

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

			// parentFrame.pack();
			// parentFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

			// getModeManager().initMode();
		} else if (parent instanceof JApplet) {

			JApplet applet = (JApplet) parent;

			applet.getContentPane().setLayout(new BorderLayout());

			applet.getContentPane().add(getSplitPane(), BorderLayout.CENTER);
			applet.getContentPane().add(statusBar, BorderLayout.SOUTH);

			applet.validate();
		}
		// displays the main frame

		if (getModeManager().isOutlookPaneCreate()) {
			SwingWorker<JComponent, JComponent> worker = new SwingWorker<JComponent, JComponent>() {

				@Override
				protected JComponent doInBackground() throws Exception {
					JComponent p = createTabbedPane();
					publish(p);
					return p;
				}

				protected void process(List<JComponent> tabs) {
					for (int i = 0; i < tabs.size(); i++) {
						split
								.setDividerLocation(Constants.EDITOR_SPLIT_DIVIDERLOCATION);
						split
								.setDividerSize(Constants.EDITOR_SPLIT_DIVIDERSIZE_NORMAL);
						split.setTopComponent(tabs.get(i));
					}
				}

				public void done() {
					createBottomSubSplit();
					setVisible(true);
				}
			};
			worker.execute();
		} else {
			createBottomSubSplit();
			setVisible(true);
		}
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
	}

	private void initSplitSize(boolean isExpandAction, int subSplitChangedSize) {
		if (!isApplet()) {
			if (isExpandAction) {
				subsplit.setDividerLocation(subsplit.getDividerLocation()
						- subSplitChangedSize / 2);
				subsplit.updateUI();
			} else {
				subsplit.setDividerLocation(Toolkit.getDefaultToolkit()
						.getScreenSize().width
						- 200
						- Constants.EDITOR_SPLIT_DIVIDERLOCATION
						+ subSplitChangedSize);
				subsubSplit.setDividerLocation(300);
			}
		} else {
			// FIXME appletλ�ü���, ����appletʱ�ټ��㣬����applet��С��ͬ����ò���ҳ���������
			subsplit
					.setDividerLocation(Integer
							.parseInt((String) getGCParam(SysSetDefines.SUBSPLIT_DIVIDERLOCATION)));
			subsubSplit
					.setDividerLocation(Integer
							.parseInt((String) getGCParam(SysSetDefines.SUBSUBSPLIT_DIVIDERLOCATION)));
		}

	}

	private void createBottomSubSplit() {
		propertyModelInteractor = new PropertyModelInteractor(this);
		getLogger().log(this, LoggerAdapter.DEBUG, "createBottomSubSplit");
		if (!getModeManager().isPropertyPaneCreate()) {
			JLabel label = new JLabel();
			subsplit.setBottomComponent(label);
			label.setVisible(false);
			subsplit.setDividerSize(Constants.EDITOR_SPLIT_DIVIDERSIZE_MIN);
			return;
		}
		
		subsubSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		subsubSplit.setOneTouchExpandable(true);
		split.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(
						JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
					Integer oldValue = (Integer) e.getOldValue();
					Integer newValue = (Integer) e.getNewValue();
					initSplitSize(true, newValue.intValue()
							- oldValue.intValue());
				}
			}
		});
		subsplit.setOneTouchExpandable(true);
		subsplit.setBottomComponent(subsubSplit);
		subsplit.setDividerSize(Constants.EDITOR_SPLIT_DIVIDERSIZE_NORMAL);
		initSplitSize(false, 0);
		JTabbedPane t1 = new JTabbedPane();
		JTabbedPane t2 = new JTabbedPane();
		subsubSplit.setTopComponent(t1);
		subsubSplit.setBottomComponent(t2);
		JPanel graphModel = propertyModelInteractor.getGraphModel();
		JPanel graphProperty = propertyModelInteractor.getGraphProperty();
		JPanel graphBusiProperty = propertyModelInteractor
				.getGraphBusiProperty();
		// Ϊ�˲���scrollbar���ڳ�ʼ����ʱ�����ʾ������������ѿ�����Ҫ��preferredsize��С
		graphModel.setPreferredSize(new Dimension(20, 60));
		graphProperty.setPreferredSize(new Dimension(20, 60));
		graphBusiProperty.setPreferredSize(new Dimension(20, 60));
		JScrollPane modelScrool = new JScrollPane();
		JScrollPane graphPropertyScroll = new JScrollPane();
		JScrollPane graphBusiPropertyScroll = new JScrollPane();
		modelScrool.setViewportView(graphModel);
		graphPropertyScroll.setViewportView(graphProperty);
		graphBusiPropertyScroll.setViewportView(graphBusiProperty);
		t1.addTab("ģ��", null, modelScrool);
		t2.addTab("ͼ������", null, graphPropertyScroll);
		t2.addTab("ҵ������", null, graphBusiPropertyScroll);
	}

	/**
	 * ��ʼ��ģʽ������
	 */
	private void iniModeManager() {
		String mode = "";
		// Ϊ�����µ�ҳ�����ö���ȡ���жϣ������͵��ж϶�Ӧ����mode������д��
		if (getGCParam(SysSetDefines.SHOW_TOOLBAR) != null) {
			boolean showToolbar = ((String) getGCParam(SysSetDefines.SHOW_TOOLBAR))
					.equalsIgnoreCase("false");
			if (showToolbar) {
				mode = EditorModeAdapter.SVGTOOL_MODE_VIEW_ONLYVIEW;
			} else {
				mode = (String) getGCParam(SysSetDefines.APP_MODE);
			}
		} else {
			mode = (String) getGCParam(SysSetDefines.APP_MODE);
		}
		try {
			String className = Utilities.findOneAttributeValue("class",
					"/classes/modeAdapter[@mode='" + mode + "']",
					ResourcesManager.getXMLDocument("modeAdapters.xml")
							.getDocumentElement());

			Class<?>[] classargs = { EditorAdapter.class };
			Object[] args = { editor };

			// creates instances of each static module
			System.out.println(className);
			modeManager = (EditorModeAdapter) Class.forName(className)
					.getConstructor(classargs).newInstance(args);

		} catch (Exception e) {
			e.printStackTrace();
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

		symbolSession = new NCISymbolSession();

		layerSelectionManager = new LayerSelectionManagerImpl(this);
		graphManager = new GraphManagerImpl(this);

	}

	private void iniModulesManager() {
		System.out.println("��ʼ��ʼ���༭��ModuleManager...");
		// long start = System.nanoTime();
		// System.out.println("New HandlesManager");
		svgHandlesManager = new HandlesManager(this);
		// System.out.println("New ModuleManager");
		moduleManager = new ModuleManager(this);
		System.out.println("ModuleManager start init...");
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				long start = System.nanoTime();
				moduleManager.init();
				moduleManager.initializeParts();
				getModeManager().initMode();
				getLogger().log(
						editor,
						LoggerAdapter.DEBUG,
						"iniModulesManager" + (System.nanoTime() - start)
								/ 1000000l + "ms");
			}
		});
	}

	// private void createObjects()
	// {
	// moduleManager.add(new
	// com.nci.svg.client.module.NCICustomGraphModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.NCIGraphUnitModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.NCIScreenCastModule(editor));
	// moduleManager.add(new com.nci.svg.client.module.DrawLineModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.DrawLineModifyModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.MongLineDrawingModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.NCIViewEditModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.NCISvgSearchModule(editor));
	// moduleManager.add(new com.nci.svg.client.module.NCIHelpModule(editor));
	// moduleManager.add(new com.nci.svg.client.module.NCILayerModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.GraphUnitDrawModule(editor));
	// moduleManager.add(new
	// com.nci.svg.client.module.DescribeTextModule(editor));
	// moduleManager.add(new nci.dl.svg.tq.TQShape(editor));
	// moduleManager.add(new com.nci.svg.district.DistrictRootModule(editor));
	// }

	/**
	 * �������TabPane
	 * 
	 * @return
	 */
	private GraphUnitOutlookPanel createTabbedPane() {
		GraphUnitSimpleSearchPanel gtSearchPanel = new GraphUnitSimpleSearchPanel(
				this);
		final JTabbedPane tabPane = new JTabbedPane();
		outlookPanel = new GraphUnitOutlookPanel(this, gtSearchPanel, tabPane);
		final ArrayList<OutlookPane> outLookPaneList = new ArrayList<OutlookPane>();
		tabPane.setTabPlacement(JTabbedPane.LEFT);
		if (getModeManager().isOutlookPaneCreate()) {
			if (getSymbolManager() != null
					&& getSymbolManager().getSymbolsTypesDefine().size() > 0) {
				outlook = new OutlookPane[getSymbolManager()
						.getSymbolsTypesDefine().size()];
				for (int i = 0; i < getSymbolManager().getSymbolsTypesDefine()
						.size(); i++) {
					final int index = i;
					final String strLabel = getSymbolManager()
							.getSymbolsTypesDefine().get(index);
					final VTextIcon textIcon = new VTextIcon(tabPane, strLabel,
							0);
					if (index == 0) {
						OutlookPane oPane = createOutlookPane(index, strLabel);
						tabPane.addTab(null, textIcon, oPane);
						outLookPaneList.add(oPane);
					} else { // ���ǵ�һ��tab����Ȳ���ʾ����ʼ�����ȵ���������ɣ��������Խ�ʡ����ʱ��
						tabPane.addTab(null, textIcon, null);
						tabPane.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent arg0) {
								if (tabPane.getComponentAt(index) == null) {
									Utilities.executeRunnable(new Runnable() {
										public void run() {
											OutlookPane oPane = createOutlookPane(
													index, strLabel);
											tabPane
													.setComponentAt(index,
															oPane);
											outLookPaneList.add(oPane);
										}
									});

								}
							}
						});
					}
				}
			}
			tabPane.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					int index = tabPane.getSelectedIndex();
					if (index >= tabPane.getTabCount()) { // ����tabcount��ʾ����仯�����˳���Ӧ�����
						return;
					}
					try {
						showOtherTypeGraphUnits(outLookPaneList.get(index));
					} catch (IndexOutOfBoundsException ex) {
					}
				}
			});
		}
		return outlookPanel;
	}

	/**
	 * ����ͼԪ������ʾͼԪ
	 * 
	 * @param typeName
	 */
	private void showOtherTypeGraphUnits(OutlookPane oPane) {
		ArrayList<Component> paneComponents = oPane.getPaneComponents();
		if (paneComponents.size() > 0) {
			((NCISymbolPanel) paneComponents.get(paneComponents.size() - 1))
					.initGraph();
		}
	}

	private OutlookPane[] outlook;

	/**
	 * ����ͼԪ��������
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
	 * ��Outlookpane��ʾ���û�����ʵ���ڲ�����в�δ�����κ�ͼ�ζ����ں�̨�������ɡ�
	 * 
	 * @param outlook
	 */
	private void initOutlookPane(final OutlookPane outlook,
			final String strLabel) {
		synchronized (symbolInitLock) {
			editor.getLogger().log(editor, LoggerAdapter.DEBUG,
					"inioutlook pane:" + strLabel);
			SwingWorker<Object, NCISymbolPanel> worker = new SwingWorker<Object, NCISymbolPanel>() {

				NCISymbolPanel p = null;
				SymbolTypeBean bean = null;

				@Override
				protected Object doInBackground() throws Exception {
					try {
						List<SymbolTypeBean> symboTypeList = null;
						if (strLabel
								.equalsIgnoreCase(Constants.SINO_NAMED_GRAPHUNIT)) {
							symboTypeList = editor
									.getSymbolManager()
									.getSymbolTypeNames(
											NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT);
						} else if (strLabel
								.equalsIgnoreCase(Constants.SINO_NAMED_TEMPLATE)) {
							symboTypeList = editor
									.getSymbolManager()
									.getSymbolTypeNames(
											NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE);
						}

						for (int i = 0; i < symboTypeList.size(); i++) {
							// for (NCIGraphUnitTypeBean bean : symboTypeList) {
							bean = symboTypeList.get(i);
							p = new NCISymbolPanel(bean, getEditor(),
									i == symboTypeList.size() - 1);
							outlookSymbolPanelMap.put(bean, p);
							publish(p);
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					return null;
				}

				@Override
				protected void process(List<NCISymbolPanel> panels) {
					for (int i = 0; i < panels.size(); i++) {
						outlook.addPane(panels.get(i).getSymbolTypeBean()
								.getVariety().getName(), panels.get(i)
								.getSymbolTypeBean(), panels.get(i));
						if (i % 5 == 0) {// ÿ����5��ˢ��һ��
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
					new LongtermTask("���ڼ���ͼԪ...", worker));
		}
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

			((JFrame) parentContainer).setExtendedState(JFrame.MAXIMIZED_BOTH);
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
	public ColorChooser getColorChooser() {
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
	 * ��ȡtooltip������
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
	public ModuleAdapter getModule(String name) {
		return (ModuleAdapter) moduleManager.getModule(name);
	}

	public ModuleAdapter getModuleByID(String moduleID) {
		return (ModuleAdapter) moduleManager.getModuleByID(moduleID);
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

		getDataManage().saveLocal(DataManageAdapter.KIND_CODES);
		getDataManage().saveLocal(DataManageAdapter.KIND_SYMBOL);
		getDataManage().saveLocal(DataManageAdapter.KIND_GLOBAL);
		// getDataManage().saveLocal(DataManageAdapter.KIND_TEMPLATE);
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
	 * ��ȡͼԪ������
	 * 
	 * @return
	 */
	public synchronized AbstractSymbolManager getSymbolManager() {
		try {
			if (symbolManager == null) {
				// String className = Utilities.findOneAttributeValue("class",
				// "/Managers/GraphUnitManager", ResourcesManager
				// .getXMLDocument("ncimanagers.xml")
				// .getDocumentElement());
				// Class<?>[] classargs = { EditorAdapter.class };
				// Object[] args = { editor };
				// System.out.println(className);
				// // creates instances of each static module
				// graphUnitManager = (AbstractNCIGraphUnitManager)
				// Class.forName(
				// className).getConstructor(classargs).newInstance(args);
				symbolManager = new NCIDBSymbolManager(this);
			}
			return symbolManager;
		} catch (Exception e) {
			System.err.println("��ʼ��GraphUnitManagerʧ�ܣ�");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡEditor��Frame����
	 * 
	 * @return
	 */
	public Frame findParentFrame() {
		Container c = parentContainer;
		while (c != null) {
			if (c instanceof Frame) {
				return (Frame) c;
			}

			c = c.getParent();
		}
		return (Frame) null;
	}

	// /**
	// * ��ȡͼԪ��������е����Զ��󼯺ϣ�key��ͼԪ����
	// *
	// * @return
	// */
	// public Map<SymbolTypeBean, Map<String, NCIThumbnailPanel>>
	// getThumnailsMap() {
	// return getSymbolManager().getThumbnailShownMap();
	// }
	/**
	 * ����ͼԪ��ģ������ƻ�ȡNCIEquipSymbolBean����ԭ����ͼԪģ���������Ψһ��
	 * 
	 * @param symbolName
	 *            ����ͼԪ��ģ�������
	 * @return
	 */
	public NCIEquipSymbolBean getSymbolBeanByName(String symbolName) {
		Iterator<Map<String, NCIEquipSymbolBean>> it = getSymbolManager()
				.getAllSymbols().values().iterator();
		Map<String, NCIEquipSymbolBean> typeThumbnailMap = null;
		while (it.hasNext()) {
			typeThumbnailMap = it.next();
			Iterator<String> it2 = typeThumbnailMap.keySet().iterator();
			while (it2.hasNext()) {
				String key = it2.next();
				if (key.equals(symbolName)) {
					return typeThumbnailMap.get(key);
				}
			}
		}
		return null;
	}

	/**
	 * ��ȡoutlook���е�ͼԪ���panel��ϣMap����ͼԪ���ͷ֣�
	 * 
	 * @return
	 */
	public HashMap<SymbolTypeBean, NCISymbolPanel> getOutlookSymbolPanelMap() {
		return outlookSymbolPanelMap;
	}

	/**
	 * ��ȡ�༭������������
	 * 
	 * @return
	 */
	public NCISvgSearchManager getSearchManager() {
		return searchManager;
	}

	/**
	 * ��ȡ�༭��session
	 * 
	 * @return
	 */
	public NCISVGSession getSvgSession() {
		return svgSession;
	}

	/**
	 * ��ȡͼԪsession
	 * 
	 * @return
	 */
	public NCISymbolSession getSymbolSession() {
		return symbolSession;
	}

	/**
	 * ��ȡ�༭��ģʽ����������
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
	 * ��ȡͼ�������
	 * 
	 * @return ͼ�������LayerSelectionManager
	 */
	public LayerSelectionManager getLayerSelectionManager() {
		return layerSelectionManager;
	}

	public Editor getEditor() {
		return this;
	}

	/**
	 * ��������Ĳ���������ȡ����ֵ
	 * 
	 * @param strName
	 *            ��������
	 * @return�����ڵĲ���ֵ���粻�����򷵻�null
	 */
	public Object getGCParam(String strName) {
		ResultBean result = dataManage.getData(DataManageAdapter.KIND_SYSSET,
				strName, null);
		if (result == null || result.getReturnFlag() == OPER_ERROR) {
			return null;
		}
		Object o = result.getReturnObj();
		if (o == null && result.getReturnType().equalsIgnoreCase("String")) {
			return "";
		}
		return o;
	}

	/**
	 * ��������Ĳ����������ò���ֵ
	 * 
	 * @param strName
	 *            ��������
	 * @param obValue
	 *            �������õĲ���ֵ
	 */
	public void setGCParam(String strName, Object obValue) {
		dataManage.setData(DataManageAdapter.KIND_SYSSET, strName, obValue);
	}

	/**
	 * modi by yux ,2008.12.11 �޸��������ӷ�ʽ
	 */
	@SuppressWarnings("unchecked")
	public synchronized void getSupportFileType() {
		Utilities.executeRunnable(new Runnable() {

			public void run() {
				if (getGCParam(SysSetDefines.WEB_FLAG) == null
						|| ((String) getGCParam(SysSetDefines.WEB_FLAG))
								.equals("1")) {
					CommunicationBean reqBean = new CommunicationBean(
							ActionNames.GET_SUPPORT_FILE_TYPE, null);
					ResultBean resultBean = getCommunicator().communicate(
							reqBean);
					if (resultBean != null
							&& resultBean.getReturnFlag() == OPER_SUCCESS
							&& resultBean.getReturnType().toLowerCase()
									.indexOf("hashmap") != -1) {
						map_SupportFileType = new HashMap<String, String>(
								(HashMap) resultBean.getReturnObj());
					}

				}

				if (map_SupportFileType == null) {
					map_SupportFileType = new HashMap<String, String>();
					ResultBean bean = dataManage.getData(
							DataManageAdapter.KIND_CODES,
							CodeConstants.SVG_BUSINESS_GRAPH_TYPE, null);
					if (bean != null) {
						HashMap<String, CodeInfoBean> map = (HashMap<String, CodeInfoBean>) (bean
								.getReturnObj());
						Iterator<CodeInfoBean> iterator = map.values()
								.iterator();
						while (iterator.hasNext()) {
							CodeInfoBean codeBean = (CodeInfoBean) iterator
									.next();
							map_SupportFileType.put(codeBean.getName(),
									codeBean.getValue());

						}
					} else {
						map_SupportFileType.put("ϵͳͼ", "0");
					}

				}
			}
		});
	}

	public synchronized String getSupportTypeKeyFromMap(int index) {
		if (map_SupportFileType == null || map_SupportFileType.size() <= index) {
			return null;
		}
		return (String) map_SupportFileType.keySet().toArray()[index];
	}

	public synchronized String getSupportTypeValueFromMap(int index) {
		if (map_SupportFileType == null || map_SupportFileType.size() <= index) {
			return null;
		}
		return (String) map_SupportFileType.values().toArray()[index];
	}

	public synchronized int getSupportFileTypeSize() {
		if (map_SupportFileType == null) {
			return 0;
		}
		return map_SupportFileType.size();
	}

	/**
	 * �ж��Ƿ���applet
	 * 
	 * @return
	 */
	public boolean isApplet() {
		return applet;
	}

	/**
	 * ��ȡ���ļ�����
	 * 
	 * @return
	 */
	public Lock getSettingFileLock() {
		return settingFileLock;
	}

	/**
	 * �ͻ�����־��¼��
	 */
	protected LoggerAdapter logger = null;

	/**
	 * ��ȡ��־��¼��
	 * 
	 * @return
	 */
	public synchronized LoggerAdapter getLogger() {
		if (logger == null) {
			logger = new ClientLoggerImpl(this);
		}
		return logger;
	}

	public GraphUnitOutlookPanel getOutlookPanel() {
		return outlookPanel;
	}

	@Override
	public GraphManagerAdapter getGraphManager() {

		return graphManager;
	}

	@Override
	public CommunicationAdapter getCommunicator() {
		return communiactor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.EditorAdapter#getDataManage() add by
	 * yux,2008.12.11
	 */
	@Override
	public DataManageAdapter getDataManage() {

		return dataManage;
	}

	@Override
	public IndunormModuleAdapter getIndunormManager() {
		return indunormManager;
	}

	/**
	 * add by yux,2009-1-9 �ͻ�������
	 * 
	 * @return:�������
	 */
	public boolean childUpgrade() {

		boolean b = upgradeModule(Constants.MODULE_TYPE_SYSTEM);
		if (!b) {
			return false;
		}

		b = upgradeModule(Constants.MODULE_TYPE_PALTROOF);
		return true;
	}

	/**
	 * add by yux,2009-1-9 ��ģ�������������
	 * 
	 * @param moduleType
	 *            :�������
	 * @return�����ؽ��
	 */
	@SuppressWarnings("unchecked")
	public boolean upgradeModule(String moduleType) {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.MODULE_TYPE;
		params[0][1] = moduleType;
		ResultBean bean = this.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_UPGRADE_MODULE, params));
		if (bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return false;
		}

		return downloadModules((ArrayList<ModuleInfoBean>) bean.getReturnObj());
	}

	/**
	 * add by yux,2009-1-9 �����ص�����б�
	 * 
	 * @param Modulelist
	 * @return
	 */
	public boolean downloadModules(ArrayList<ModuleInfoBean> moduleList) {
		for (ModuleInfoBean bean : moduleList) {
			downloadModule(bean);
		}
		return false;
	}

	/**
	 * add by yux,2009-1-14 ���������Ϣ�������ģ��,��������jvm�ڴ���
	 * 
	 * @param bean
	 *            :ģ����Ϣ
	 * @return:���ؼ����ؽ��
	 */
	public boolean downloadModule(ModuleInfoBean bean) {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.MODULE_SHORT_NAME;
		params[0][1] = bean.getModuleShortName();
		ResultBean resultBean = this.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_UPGRADE_MODULE, params));
		if (resultBean == null
				|| resultBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return false;
		}

		byte[] content = (byte[]) resultBean.getReturnObj();
		if (content != null) {
			String jarName = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
					+ bean.getModuleShortName() + bean.getEdition() + ".jar";
			try {
				FileOutputStream fos = new FileOutputStream(new File(jarName));
				fos.write(content, 0, content.length);
				fos.close();
				File localJarFile = new File(jarName);
				if (localJarFile.exists()) {
					NCIClassLoader.addClassPath(localJarFile);
				} else {
					return false;
				}

			} catch (Exception e) {
			}
		}
		return true;
	}

	/**
	 * add by yux,2009-1-12 ����ƽ̨���
	 * 
	 * @return:���ؽ��
	 */
	private boolean loadPlatModules() {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.EditorAdapter#refreshData(java.lang.String)
	 */
	@Override
	public boolean refreshData(int dataType) {
		boolean ret = false;
		// ��������Чʱ�����ܽ��и���
		if (getGCParam(SysSetDefines.WEB_FLAG) == null
				|| ((String) getGCParam(SysSetDefines.WEB_FLAG)).equals("1")) {
			if (dataManage.loadRemote(dataType, null) == OPER_SUCCESS) {
				ret = true;
			}
		}
		return ret;
	}

	public PropertyModelInteractor getPropertyModelInteractor() {
		return propertyModelInteractor;
	}

}
