package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.xpath.XPathExpressionException;

import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.TQToolkit;
import nci.dl.svg.tq.oper.common.TQOperation;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.other.LinkPointManager;

import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * �����豸����
 * 
 * @author qi
 * 
 */
public class TQAddEquipOperation extends TQOperation {

	private enum EquipAdd {
		��������, ����������
	}

	public TQAddEquipOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		if (elementAtPoint != null) {
			if (elementAtPoint.getNodeName().equals("path")
					&& TQToolkit.isEquipCanAdded((Element) elementAtPoint)) {
				if (TQToolkit.isBranchLine(elementAtPoint.getParentNode())
						|| TQToolkit.isMainLine(elementAtPoint.getParentNode())) {
					canvas.setCursor(Cursor
							.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					tqModule.setCurrentAction(EquipAdd.��������.name());
				} else if (TQToolkit
						.isJieHuLine(elementAtPoint.getParentNode())) {
					canvas.setCursor(Cursor
							.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					tqModule.setCurrentAction(EquipAdd.����������.name());
				}
			} else {
				tqModule.setCurrentAction(menuItem.getText());
				canvas
						.setCursor(Cursor
								.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
		}
	}

	@Override
	public void mousePress(ModeSelectionEvent evt) {
		if (tqModule.getPseudoStartElement() == null
				|| tqModule.getCurrentAction().equals(menuItem.getText()))
			return;
		final StringBuffer hrefName = new StringBuffer();
		if (tqModule.getCurrentAction().equals(EquipAdd.��������.name()))
			hrefName.append(TQToolkit.POLE_NAME).append(
					Constants.SYMBOL_STATUS_SEP).append("����");
		if (tqModule.getCurrentAction().equals(EquipAdd.����������.name()))
			hrefName.append(TQToolkit.JILIANGBIAO_NAME).append(
					Constants.SYMBOL_STATUS_SEP).append("����");
		final Node parentNode = tqModule.getPseudoStartElement()
				.getParentNode();

		final Element tempStartEle = tqModule.getPseudoStartElement();
		// path�ڵ����һ���ڵ�
		final Node nextNode = tempStartEle.getNextSibling();
		final Element[] newCreatedElement = new Element[3];// �´���������Ԫ�أ�����1���豸��2����·
		Runnable executeRunnable = new Runnable() {
			public void run() {
				executeAddEquip(tempStartEle, newCreatedElement, hrefName
						.toString());

				reset();
			}
		};
		Runnable undoRunnable = new Runnable() {
			public void run() {
				parentNode.insertBefore(tempStartEle, nextNode);
				for (int i = 0; i < newCreatedElement.length; i++) {
					parentNode.removeChild(newCreatedElement[i]);

				}
				reset();
				tqModule.getEditor().getSvgSession()
						.refreshCurrentHandleImediately();
			}
		};
		Set<Element> elements = new HashSet<Element>();
		elements.add(tempStartEle);
		ShapeToolkit.addUndoRedoAction(evt.getSource().getHandle(), tqModule
				.getCurrentAction(), executeRunnable, undoRunnable, elements);
		if (!tqModule.getEditor().getRemanentModeManager().isRemanentMode()) {
			tqModule.setCurrentAction("");
		}

	}

	/**
	 * �����豸�ľ���ִ��
	 * 
	 * @param tempStartEle
	 *            �����������ڵ���·
	 * @param newCreatedElements
	 *            �����ӵ��豸����·������������·��һ���豸
	 * @param equipHrefName
	 *            �豸��svg xlink:href�е����ƣ�������"#"
	 */
	private void executeAddEquip(Element tempStartEle,
			Element[] newCreatedElements, String hrefName) {
		try {
			// ɾ�����豸���ڵ���·
			String p0 = tempStartEle
					.getAttribute(LinkPointManager.BEGIN_LINE_POINT);
			String p1 = tempStartEle
					.getAttribute(LinkPointManager.END_LINE_POINT);
			String t0 = tempStartEle
					.getAttribute(LinkPointManager.BEGIN_LINE_TERMINAL);
			String t1 = tempStartEle
					.getAttribute(LinkPointManager.END_LINE_TERMINAL);
			Element p0Ele = (Element) Utilities.findNode("//*[@id='" + p0
					+ "']", tempStartEle);
			Element p1Ele = (Element) Utilities.findNode("//*[@id='" + p1
					+ "']", tempStartEle);
			Point2D equipCenterPoint = EditorToolkit.getCenterPoint(tempStartEle);
			Point2D p0ConnectedPoint = getConnectedPoint(t0, p0Ele);
			Point2D p1ConnectedPoint = getConnectedPoint(t1, p1Ele);

			String useEquipUUID = UUID.randomUUID().toString();
			Element newUseEle = TQToolkit.createEquipElement(tempStartEle
					.getOwnerDocument(), hrefName, equipCenterPoint);
			newUseEle.setAttribute("id", useEquipUUID);
			p1Ele.getParentNode().insertBefore(newUseEle, null);

			Element path0Ele = EditorToolkit.insertPathElement(tqModule
					.getEditor().getHandlesManager().getCurrentHandle(),
					(Element) tempStartEle.getParentNode(), tempStartEle,
					p0ConnectedPoint, equipCenterPoint, p0, useEquipUUID, t0,
					"nci_c");
			// ����Ϊ������·
			TQToolkit.addMetadata(path0Ele, TQToolkit.TQ_METADATA_PROPERTIES,
					TQToolkit.TQ_METADATA_LINE_TYPE,
					TQToolkit.TQ_METADATA_BASE_LINE, false);
			Element path1Ele = EditorToolkit.insertPathElement(tqModule
					.getEditor().getHandlesManager().getCurrentHandle(),
					(Element) tempStartEle.getParentNode(), tempStartEle,
					equipCenterPoint, p1ConnectedPoint, useEquipUUID, p1,
					"nci_c", t1);
			// ����Ϊ������·
			TQToolkit.addMetadata(path1Ele, TQToolkit.TQ_METADATA_PROPERTIES,
					TQToolkit.TQ_METADATA_LINE_TYPE,
					TQToolkit.TQ_METADATA_BASE_LINE, false);
			newCreatedElements[0] = newUseEle;
			newCreatedElements[1] = path0Ele;
			newCreatedElements[2] = path1Ele;
			tempStartEle.getParentNode().removeChild(tempStartEle);
			
			tqModule.getEditor().getSvgSession()
					.refreshCurrentHandleImediately();

		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doMenuAction() {
		tqModule.getEditor().getHandlesManager().getCurrentHandle()
				.getSelection().setSelectionFilter(null);
	}

	private List<Element> searchCrossedPoles() {
		List<Element> crossedPoles = new ArrayList<Element>();

		return crossedPoles;
	}
}
