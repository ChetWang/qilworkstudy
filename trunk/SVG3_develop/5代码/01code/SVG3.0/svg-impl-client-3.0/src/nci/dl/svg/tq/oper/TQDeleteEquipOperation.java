package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.oper.common.TQOperation;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.other.LinkPointManager;

import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * 删除设备操作
 * 
 * @author qi
 * 
 */
public class TQDeleteEquipOperation extends TQOperation {

	public TQDeleteEquipOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		if (elementAtPoint != null
				&& elementAtPoint.getNodeName().equals("use")) {
			canvas.setCursor(Cursor
					.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
		} else {
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
	}

	@Override
	public void mousePress(ModeSelectionEvent evt) {
		final Element deletedEquip = tqModule.getPseudoStartElement();
		if (deletedEquip == null) {
			return;
		}
		final List<Node> deletedLines = new ArrayList<Node>();
		final Node parentNode = deletedEquip.getParentNode();
		Runnable executeRunnable = new Runnable() {
			public void run() {
				executeDeleteEquip(deletedEquip, deletedLines);
				reset();
			}
		};
		Runnable undoRunnable = new Runnable() {
			public void run() {
				parentNode.insertBefore(deletedEquip, null);
				for (int i = 0; i < deletedLines.size(); i++) {
					parentNode.insertBefore(deletedLines.get(i), parentNode.getFirstChild());

				}

				deletedLines.clear();
				reset();
				tqModule.getEditor().getSvgSession()
						.refreshCurrentHandleImediately();
			}
		};
		Set<Element> elements = new HashSet<Element>();
		elements.add(deletedEquip);
		ShapeToolkit.addUndoRedoAction(evt.getSource().getHandle(), tqModule
				.getCurrentAction(), executeRunnable, undoRunnable, elements);
		if (!tqModule.getEditor().getRemanentModeManager().isRemanentMode()) {
			tqModule.setCurrentAction("");
		}
	}

	/**
	 * 删除设备的具体执行
	 * 
	 * @param deletedEquip
	 *            删除的设备
	 * @param deletedLines
	 *            被删除的设备关联的线路，与其相连的线路也应删去
	 */
	private void executeDeleteEquip(Element deletedEquip,
			List<Node> deletedLines) {
		String uuid = deletedEquip.getAttribute("id");
		Node parentNode = deletedEquip.getParentNode();
		try {
			NodeList connectedStartLines = Utilities.findNodes("//*[@"
					+ LinkPointManager.BEGIN_LINE_POINT + "='" + uuid + "']",
					deletedEquip);
			for (int i = 0; i < connectedStartLines.getLength(); i++) {
				Node line = connectedStartLines.item(i);
				deletedLines.add(line);
				parentNode.removeChild(line);
			}
			connectedStartLines = Utilities.findNodes("//*[@"
					+ LinkPointManager.END_LINE_POINT + "='" + uuid + "']",
					deletedEquip);
			for (int i = 0; i < connectedStartLines.getLength(); i++) {
				Node line = connectedStartLines.item(i);
				deletedLines.add(line);
				parentNode.removeChild(line);
			}
			parentNode.removeChild(deletedEquip);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public void doMenuAction() {
		tqModule.getEditor().getHandlesManager().getCurrentHandle()
				.getSelection().setSelectionFilter(null);
	}

}
