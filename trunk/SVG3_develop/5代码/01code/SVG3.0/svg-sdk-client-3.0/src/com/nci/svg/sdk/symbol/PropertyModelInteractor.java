package com.nci.svg.sdk.symbol;

import java.util.Set;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.graphmanager.property.BusinessPropertyPanel;
import com.nci.svg.sdk.graphmanager.property.GraphModel;
import com.nci.svg.sdk.graphmanager.property.GraphPropertyPanel;

import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * 模型－图元（模板）－属性交互影响类
 * 
 * @author qi
 * 
 */
public class PropertyModelInteractor {

	private EditorAdapter editor;

	/**
	 * add by yux,2009-1-19 图形属性框
	 */
	private GraphPropertyPanel graphProperty = null;

	/**
	 * add by yux,2009-1-19 业务属性框
	 */
	private BusinessPropertyPanel graphBusiProperty = null;

	/**
	 * add by yux,2009-1-19 模型树框
	 */
	private GraphModel graphModel = null;

	public PropertyModelInteractor(EditorAdapter editorAdapter) {
		this.editor = editorAdapter;
		graphModel = new GraphModel(editor);
		graphProperty = new GraphPropertyPanel(editor);
		graphBusiProperty = new BusinessPropertyPanel(editor);
		editor.getHandlesManager().addHandlesListener(new HandlesListener() {

			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				if (editor.getModeManager().isPropertyPaneCreate()) {
					graphModel.notifyRightPanel(GraphModel.REINIT_MODELTREE,
							null);
					graphModel.notifyRightPanel(GraphModel.SELECT_TREENODE,
							null);
				}
			}

		});
	}

	public EditorAdapter getEditor() {
		return editor;
	}

	public GraphPropertyPanel getGraphProperty() {
		return graphProperty;
	}

	public BusinessPropertyPanel getGraphBusiProperty() {
		return graphBusiProperty;
	}

	public GraphModel getGraphModel() {
		return graphModel;
	}

}
