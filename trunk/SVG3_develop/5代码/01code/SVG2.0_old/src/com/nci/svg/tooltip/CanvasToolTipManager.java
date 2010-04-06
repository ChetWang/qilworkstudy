package com.nci.svg.tooltip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.nci.svg.module.EquipmentPropertyModule;
import com.nci.svg.util.Constants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;

public class CanvasToolTipManager {

	private Editor editor;

	private Element element;

	public CanvasToolTipManager(Editor editor) {
		this.editor = editor;
	}

	/**
	 * ����ָ���ڵ��tooltip
	 * 
	 * @param element
	 */
	public void handleToolTip(Element originalElement) {
		this.element = originalElement;
		editor.getHandlesManager().getCurrentHandle().getCanvas()
				.setToolTipText(getToolTip(element));
	}

	/**
	 * ��ʾtooltip
	 * 
	 * @param element
	 */
	private String getToolTip(Element element) {
		editor.getHandlesManager().getCurrentHandle().getCanvas()
				.setToolTipText(null);
		if (element == null)
			return "";
		if (!EditorToolkit.isElementVisible(element))
			return "";
		// Node metadata = element.getNextSibling();
		Element metadata = Utilities.getSingleChildElement(
				Constants.NCI_SVG_METADATA, element);

		String nciTypeValue = element.getAttribute(Constants.NCI_SVG_Type_Attr);
		// �����nciType���豸���ͣ���������metadata�ķ�text���ͽڵ㣬�����ǰ���metadata��g�ڵ㣨��������豸������������һ���豸
		if (metadata != null) {
			if ((nciTypeValue != null && Constants.nciGraphTypes
					.contains(nciTypeValue))
					|| (!element.getNodeName().equalsIgnoreCase("text") && metadata
							.getNodeName().equalsIgnoreCase(
									Constants.NCI_SVG_METADATA))
					|| (element.getNodeName().equalsIgnoreCase("g") && (element
							.getElementsByTagName(Constants.NCI_SVG_METADATA)
							.getLength() > 0))) {
				// showToolTip(element);
				element = Utilities.parseSelectedElement(element);
				return editor.getMetaDataManager().getTooltipString(element);

			}
		}
		return "";
	}
}
