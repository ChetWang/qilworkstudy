package com.nci.svg.sdk.client.business;

import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 * ҵ��ͼ��ͼԪ��ģ�屻ѡ�����ܻᴥ����ҵ����صĴ���ʵ����
 */
public class BusinessInfoLocator implements BusinessSelectionIfc {

	private SVGHandle svgHandle = null;

	public BusinessInfoLocator(SVGHandle svgHandle) {
		this.svgHandle = svgHandle;
	}

	public void handleSelection() {
		if (svgHandle.getEditor().getModeManager().isPropertyPaneCreate()) {
			Utilities.executeRunnable(new Runnable() {
				public void run() {
					doIT();
				}
			});
		}
	}

	private synchronized void doIT() {
		Set<Element> selectedEles = svgHandle.getSelection()
				.getSelectedElements();
		boolean located = false;
		DefaultMutableTreeNode selectNode = null;
		if (selectedEles != null && selectedEles.size() == 1) {
			// ֻ����ѡ�񵥸�������µĲ��ж�
			final Element selectEle = selectedEles.iterator().next();
			Map<Element, DefaultMutableTreeNode> map = svgHandle.getEditor()
					.getPropertyModelInteractor().getGraphModel()
					.getMapElement();
			if (map != null) {
				selectNode = map.get(selectEle);
				if (selectNode != null) {
					located = true;
				}
			}

		} else if(selectedEles == null || selectedEles.size() == 0){
			
			svgHandle.getEditor().getPropertyModelInteractor()
					.getGraphBusiProperty().setElement(null);
		}
		else
		{
			svgHandle.getEditor().getPropertyModelInteractor()
			    .getGraphBusiProperty().setElement(selectedEles);
		}
		if (located) {
			TreePath path = new TreePath(((DefaultTreeModel) svgHandle
					.getEditor().getPropertyModelInteractor().getGraphModel()
					.getModelTree().getModel()).getPathToRoot(selectNode));
			svgHandle.getEditor().getPropertyModelInteractor().getGraphModel()
					.getModelTree().setSelectionPath(path);
		} else {
			// ģ����ȡ��ѡ��
			svgHandle.getEditor().getPropertyModelInteractor().getGraphModel()
					.getModelTree().clearSelection();
			// FIXME ҵ��propertysheet���

		}
	}

}
