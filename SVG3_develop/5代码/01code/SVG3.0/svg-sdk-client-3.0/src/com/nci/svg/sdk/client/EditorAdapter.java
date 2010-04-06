package com.nci.svg.sdk.client;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.nci.svg.sdk.client.communication.CommunicationAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.NCISVGSession;
import com.nci.svg.sdk.client.util.NCISymbolSession;
import com.nci.svg.sdk.graphunit.AbstractSymbolManager;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.layer.LayerSelectionManager;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.mode.EditorModeAdapter;
import com.nci.svg.sdk.module.IndunormModuleAdapter;
import com.nci.svg.sdk.module.ModuleControllerAdapter;
import com.nci.svg.sdk.module.ModuleInitializeFailedException;
import com.nci.svg.sdk.module.ModuleStartFailedException;
import com.nci.svg.sdk.search.NCISvgSearchManager;
import com.nci.svg.sdk.shape.ShapePositionHandler;
import com.nci.svg.sdk.symbol.PropertyModelInteractor;
import com.nci.svg.sdk.tooltip.CanvasToolTipManager;
import com.nci.svg.sdk.ui.NCIStatusBar;
import com.nci.svg.sdk.ui.graphunit.GraphUnitOutlookPanel;
import com.nci.svg.sdk.ui.graphunit.NCISymbolPanel;

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
 * @author yx.nci
 * 客户端主管理组件基类
 */
public abstract class EditorAdapter extends ModuleControllerAdapter {


	public static boolean isRtdaAnimationsVersion = false;

	protected boolean canExitFromJVM = false;

	protected boolean isQuitActionDisabled = false;

	protected JDesktopPane desktop = null;

	protected final Map<String, Rectangle> widgetBounds = new HashMap<String, Rectangle>();

	/**
	 * the color chooser of the editor
	 */
	protected ColorChooser colorChooser;

	protected Container parentContainer;

	/**
	 * add by yux,2008.12.10 业务系统编号
	 */
	protected String busiSysID = null;

	protected PropertyModelInteractor propertyModelInteractor = null;

	/**
	 * add by yux,2008.12.10
	 * 
	 * @return通讯组件对象实例
	 */
	public abstract CommunicationAdapter getCommunicator();

	/**
	 * add by yux,2008.12.11
	 * 
	 * @return数据管理组件实例
	 */
	public abstract DataManageAdapter getDataManage();

	public EditorAdapter() {
		super();
		int result = init();
		if (result != MODULE_INITIALIZE_COMPLETE) {
			new ModuleInitializeFailedException(this).printStackTrace();
		}
		result = start();
		if (result != MODULE_START_COMPLETE) {
			new ModuleStartFailedException(this).printStackTrace();
		}
	}

	public String getModuleType() {
		return "editor";
	}

	public abstract PopupManager getPopupManager();

	public abstract ClipboardManager getClipboardManager();

	public abstract HandlesManager getHandlesManager();

	/**
	 * 获取主界面
	 * 
	 * @return 主界面Container
	 */
	public abstract Container getParent();

	public abstract NCISVGSession getSvgSession();

	public abstract ModuleManager getSVGModuleLoader();

	public abstract SelectionInfoManager getSelectionManager();

	public abstract RemanentModeManager getRemanentModeManager();

	public abstract SquareModeManager getSquareModeManager();

	public abstract NCISymbolSession getSymbolSession();

	public abstract AbstractSymbolManager getSymbolManager();

	public abstract HashMap<String, String> getRequiredNameSpaces();

	public ColorChooser getColorChooser() {
		// TODO 对这个静态方法要从新处理，不建议使用静态方法
		while (colorChooser == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		return colorChooser;
	}

	public abstract ColorManager getSVGColorManager();

	public abstract ClosePathModeManager getClosePathModeManager();

	public abstract EditorMenuBar getMenuBar();

	public abstract EditorModeAdapter getModeManager();

	public abstract IOManager getIOManager();

	public abstract EditorToolkit getSVGToolkit();

	public abstract Object getGCParam(String key);

	public abstract Set<AbstractShape> getShapeModules();

	public abstract ModuleAdapter getModule(String moduleName);

	public abstract ModuleAdapter getModuleByID(String moduleID);

	public abstract Frame findParentFrame();

	public abstract ToolBarManager getToolBarManager();

	public abstract JSplitPane getSplitPane();

	// public abstract Map<SymbolTypeBean, Map<String, NCIThumbnailPanel>>
	// getThumnailsMap();

	public abstract LayerSelectionManager getLayerSelectionManager();

	public abstract Map<SymbolTypeBean, NCISymbolPanel> getOutlookSymbolPanelMap();

	public abstract ResourcesManager getResourcesManager();

	public abstract CanvasToolTipManager getToolTipManager();

	public abstract ConstraintLinesModeManager getConstraintLinesModeManager();

	public abstract void dispose();

	public abstract NCISvgSearchManager getSearchManager();

	public abstract NCIStatusBar getStatusBar();

	public abstract int getSupportFileTypeSize();

	public abstract String getSupportTypeValueFromMap(int index);

	public abstract String getSupportTypeKeyFromMap(int index);

	public abstract LoggerAdapter getLogger();

	public abstract GraphUnitOutlookPanel getOutlookPanel();

	public abstract GraphManagerAdapter getGraphManager();

	public abstract IndunormModuleAdapter getIndunormManager();

	public boolean canExitFromJVM() {
		return canExitFromJVM;
	}

	public void exit() {
		getIOManager().getEditorExitManager().exit();
	}

	public boolean isQuitActionDisabled() {
		return isQuitActionDisabled;
	}

	public void setVisible(boolean visible) {
		if (parentContainer instanceof JFrame) {

			((JFrame) parentContainer).setVisible(visible);

			if (visible) {

				((JFrame) parentContainer).toFront();

			} else {

				((JFrame) parentContainer).toBack();
			}
		}
	}

	public JDesktopPane getDesktop() {
		return desktop;
	}

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

	public ResourceImageManager getResourceImageManager() {
		return getSVGModuleLoader().getResourceImageManager();
	}

	public String getBusiSysID() {
		return busiSysID;
	}

	public void setBusiSysID(String busiSysID) {
		this.busiSysID = busiSysID;
	}
	
	public PropertyModelInteractor getPropertyModelInteractor(){
		return propertyModelInteractor;
	}

	/**
	 * add by yux,2009-1-18 刷新指定数据类型的数据
	 * 
	 * @param dataType：数据类型
	 * @return：刷新结果
	 */
	public abstract boolean refreshData(int dataType);
	
	public abstract boolean isApplet();
	
	/**
	 * 新产生的Element的位置适配器
	 */
	protected ShapePositionHandler positionHandler;

	public ShapePositionHandler getPositionHandler() {
		return positionHandler;
	}

	public void setPositionHandler(ShapePositionHandler positionHandler) {
		this.positionHandler = positionHandler;
	}
}
