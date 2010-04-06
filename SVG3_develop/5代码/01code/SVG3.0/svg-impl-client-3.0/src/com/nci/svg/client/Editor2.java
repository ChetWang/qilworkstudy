package com.nci.svg.client;

import java.awt.Container;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JSplitPane;

import com.nci.svg.client.layer.LayerSelectionManagerImpl;
import com.nci.svg.client.logger.ClientLoggerImpl;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.GraphManagerAdapter;
import com.nci.svg.sdk.client.communication.CommunicationAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.NCISVGSession;
import com.nci.svg.sdk.client.util.NCISymbolSession;
import com.nci.svg.sdk.graphmanager.property.BusinessPropertyPanel;
import com.nci.svg.sdk.graphmanager.property.GraphModel;
import com.nci.svg.sdk.graphmanager.property.GraphPropertyPanel;
import com.nci.svg.sdk.graphunit.AbstractSymbolManager;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.mode.EditorModeAdapter;
import com.nci.svg.sdk.module.IndunormModuleAdapter;
import com.nci.svg.sdk.module.ModuleStopedException;
import com.nci.svg.sdk.search.NCISvgSearchManager;
import com.nci.svg.sdk.tooltip.CanvasToolTipManager;
import com.nci.svg.sdk.ui.NCIStatusBar;
import com.nci.svg.sdk.ui.graphunit.GraphUnitOutlookPanel;
import com.nci.svg.sdk.ui.graphunit.NCISymbolPanel;

import fr.itris.glips.svgeditor.ColorManager;
import fr.itris.glips.svgeditor.ModuleManager;
import fr.itris.glips.svgeditor.actions.clipboard.ClipboardManager;
import fr.itris.glips.svgeditor.actions.menubar.EditorMenuBar;
import fr.itris.glips.svgeditor.actions.popup.PopupManager;
import fr.itris.glips.svgeditor.actions.toolbar.ToolBarManager;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.options.ClosePathModeManager;
import fr.itris.glips.svgeditor.options.ConstraintLinesModeManager;
import fr.itris.glips.svgeditor.options.RemanentModeManager;
import fr.itris.glips.svgeditor.options.SquareModeManager;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;

public class Editor2 extends EditorAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public IndunormModuleAdapter getIndunormManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 模块的UUID
	 */
	public final static String MODULE_ID = "a2fe82c2-fd73-4dd3-bab7-ee52a3487395";	
	
	public Editor2(){
		super();
		moduleUUID = MODULE_ID;
	}

	@Override
	public DataManageAdapter getDataManage() {
		// 
		return null;
	}

	public Object handleOper(int index, Object obj)
			throws ModuleStopedException {
		return null;
	}

	public String getModuleType() {
		return "Editor Main Manager";
	}

	@Override
	public ClipboardManager getClipboardManager() {
		// 
		return null;
	}

	@Override
	public HandlesManager getHandlesManager() {
		// 
		return null;
	}

	@Override
	public Container getParent() {
		// 
		return null;
	}

	@Override
	public PopupManager getPopupManager() {
		// 
		return null;
	}

	@Override
	public RemanentModeManager getRemanentModeManager() {
		// 
		return null;
	}

	@Override
	public HashMap<String, String> getRequiredNameSpaces() {
		// 
		return null;
	}

	@Override
	public ModuleManager getSVGModuleLoader() {
		// 
		return null;
	}

	@Override
	public SelectionInfoManager getSelectionManager() {
		// 
		return null;
	}

	@Override
	public SquareModeManager getSquareModeManager() {
		// 
		return null;
	}

	@Override
	public NCISVGSession getSvgSession() {
		// 
		return null;
	}

	@Override
	public NCISymbolSession getSymbolSession() {
		// 
		return null;
	}

	@Override
	public Object getGCParam(String key) {
		// 
		return null;
	}

	@Override
	public ModuleAdapter getModule(String moduleName) {
		// 
		return null;
	}

	@Override
	public EditorToolkit getSVGToolkit() {
		// 
		return null;
	}


	@Override
	public ClosePathModeManager getClosePathModeManager() {
		// 
		return null;
	}


	@Override
	public AbstractSymbolManager getSymbolManager() {
		// 
		return null;
	}

	@Override
	public IOManager getIOManager() {
		// 
		return null;
	}

	@Override
	public EditorMenuBar getMenuBar() {
		// 
		return null;
	}

	@Override
	public EditorModeAdapter getModeManager() {
		// 
		return null;
	}

	@Override
	public ColorManager getSVGColorManager() {
		// 
		return null;
	}

	@Override
	public Set<AbstractShape> getShapeModules() {
		// 
		return null;
	}

	@Override
	public void dispose() {
		// 
		
	}

	@Override
	public Frame findParentFrame() {
		// 
		return null;
	}

	@Override
	public ConstraintLinesModeManager getConstraintLinesModeManager() {
		// 
		return null;
	}

	@Override
	public LayerSelectionManagerImpl getLayerSelectionManager() {
		// 
		return null;
	}


	@Override
	public HashMap<SymbolTypeBean, NCISymbolPanel> getOutlookSymbolPanelMap() {
		// 
		return null;
	}

	@Override
	public ResourcesManager getResourcesManager() {
		// 
		return null;
	}

	@Override
	public NCISvgSearchManager getSearchManager() {
		// 
		return null;
	}

	@Override
	public JSplitPane getSplitPane() {
		// 
		return null;
	}

	@Override
	public NCIStatusBar getStatusBar() {
		// 
		return null;
	}

	@Override
	public int getSupportFileTypeSize() {
		// 
		return 0;
	}

	@Override
	public String getSupportTypeKeyFromMap(int index) {
		// 
		return null;
	}

	@Override
	public String getSupportTypeValueFromMap(int index) {
		// 
		return null;
	}


	public ToolBarManager getToolBarManager() {
		// 
		return null;
	}

	@Override
	public CanvasToolTipManager getToolTipManager() {
		// 
		return null;
	}

	/**
	 * 客户端日志记录器
	 */
	protected LoggerAdapter logger = null;

	/**
	 * 获取日志记录器
	 * @return
	 */
	public synchronized LoggerAdapter getLogger() {
		if(logger ==null){
			logger = new ClientLoggerImpl(this);
		}
		return logger;
	}

	@Override
	public GraphUnitOutlookPanel getOutlookPanel() {
		// 
		return null;
	}

	@Override
	public GraphManagerAdapter getGraphManager() {
		// 
		return null;
	}

	@Override
	public CommunicationAdapter getCommunicator() {
		// 
		return null;
	}

	@Override
	public ModuleAdapter getModuleByID(String moduleID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean refreshData(int dataType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isApplet() {
		// TODO Auto-generated method stub
		return false;
	}




}
