package com.nci.svg.sdk.client.business;

import java.util.Set;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 *
 */
public class ShapeInfoLocator implements BusinessSelectionIfc {

	private SVGHandle svgHandle = null;

	public ShapeInfoLocator(SVGHandle svgHandle) {
		this.svgHandle = svgHandle;
	}

	@Override
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
		if (selectedEles != null ) {
			svgHandle.getEditor().getPropertyModelInteractor()
					.getGraphProperty().setElement(selectedEles);
		} else {
			svgHandle.getEditor().getPropertyModelInteractor()
					.getGraphProperty().setElement(null);
		}

	}

}
