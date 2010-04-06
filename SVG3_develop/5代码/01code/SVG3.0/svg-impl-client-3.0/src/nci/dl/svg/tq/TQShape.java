package nci.dl.svg.tq;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.xml.xpath.XPathExpressionException;

import nci.dl.svg.tq.oper.common.TQDrawOperation;
import nci.dl.svg.tq.oper.common.TQOperation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.swing.JideSplitButton;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.selection.ModeSelectionListener;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.logger.LoggerAdapter;

import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.path.ConnectedListener;
import fr.itris.glips.svgeditor.shape.path.DrawingHandler;
import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * ����̨��ͼģ��
 * 
 * @author qi
 * 
 */
public class TQShape extends PathShape implements ActionListener {

	public static final String MODULE_ID = "0a11442c-530e-4bc3-a578-55aaec455807";

	/**
	 * ̨��ͼ�ļ�����
	 */
	public static final String TQ_FILE_TYPE = "district";

	private ImageIcon[] icons = null;

	public static final int TQ_DRAW_MAINPATH_MODE = 1144;

	public String symbolStatus = "����";

	private JideSplitButton splitBtn = null;

	/**
	 * �ڵ�ǰ�Ķ������Ƿ�ɻ��ƣ�ĳЩ��������Ҫ����������Ż��ƣ���֧�߻���
	 */
	private boolean drawable = true;

	/**
	 * ֧�߻�ӻ��ߵĵ�һ���ڵ㣨�Ѿ����ڵ�ͼԪ��,���豸������ɾ�����ڵĽڵ�
	 */
	private Element pseudoStartElement = null;

	/**
	 * �Ƿ����ڻ���
	 */
	private boolean drawingStarted = false;

	private TQOperation currentTQOperation;

	private Map<String, TQOperation> operations = new LinkedHashMap<String, TQOperation>();

	private Timer timer = new Timer(1500, this);

	public enum Operations {
		������·, ����ͬ��, ���ƽ���, ���ƽӻ�, ����, ��·����, ��·�ϲ�, �˺�����, �����豸, ɾ���豸, ���ݹ���
	}

	public enum LinesDraw {
		���Ƹ���, ����֧��, ���ƽӻ�
	}

	public enum LineExtend {
		�����֧��, ����ӻ���
	}

	public enum EquipOperEnum {
		��������, ����������, ɾ������, ɾ��������
	}

	public TQShape(EditorAdapter editor) {
		super(editor);
		timer.start();
	}

	private int initModule() {
		// tqMenuItems = new JMenuItem[Operations.values().length];
		icons = new ImageIcon[Operations.values().length];
		drawingHandler = new TQContinuesDrawingHandler(this);
		moduleUUID = MODULE_ID;
		currentAction = null;
		// Ԫ��ѡ��仯����Ĳ˵��仯
		final SelectionChangedListener scl = new SelectionChangedListener() {

			@Override
			public void selectionChanged(Set<Element> selectedElements) {

				selectionChange(getEditor().getHandlesManager()
						.getCurrentHandle());
			}
		};
		// ģʽ�仯�µĲ�����������������¼�״̬��press\release....��
		final ModeSelectionListener msl = new ModeSelectionListener() {
			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				tqModeSelected(evt);
			}
		};
		// ģʽ�仯�£�ֻ������ƶ�ʱ�������״�仯
		final ModeSelectionListener cursorLis = new ModeSelectionListener() {
			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				cursorChange(evt);
			}
		};
		// ������ɾ���豸ʱ������¼�
		final ModeSelectionListener tqPressListener = new ModeSelectionListener() {
			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				tqMousePress(evt);
			}
		};

		// PathShape pathsShape = (PathShape) editor
		// .getModuleByID(PathShape.MODULE_ID);
		PathShape pathsShape = null;
		while ((pathsShape = (PathShape) editor
				.getModuleByID(PathShape.MODULE_ID)) == null) {
			try {
				Thread.currentThread().sleep(200);
			} catch (InterruptedException e1) {
			}
		}
		pathsShape.addConnectedListener(this, new ConnectedListener() {

			@Override
			public void notifyConnected(Element connectedPathElement) {
				reformConnectedElements(connectedPathElement);
			}

		});
		final PathShape temppath = pathsShape;
		final ModuleAdapter tempModule = this;
		editor.getHandlesManager().addHandlesListener(new HandlesListener() {
			@Override
			public void handleCreated(SVGHandle currentHandle) {
				currentHandle.getSelection().addModeSelectionListener(msl);
				currentHandle.getSelection().addMouseMoveListener(cursorLis);
				currentHandle.getSelection().addMousePressedListener(
						tqPressListener);
				currentHandle.getSelection().addSelectionChangedListener(scl);
				temppath.setCurrentWorkingModule(tempModule);
			}

			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				if (currentHandle != null
						&& TQ_FILE_TYPE.equals(currentHandle.getCanvas()
								.getFileType())) {
					splitBtn.setEnabled(true);
				} else {
					splitBtn.setEnabled(false);
					splitBtn.setButtonSelected(false);
				}
				selectionChange(currentHandle);
			}
		});
		editor.getSelectionManager().addModeChangeListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						modeChanged(e);
					}
				});
		return TQShape.MODULE_INITIALIZE_COMPLETE;
	}

	/**
	 * Ԫ��ѡ��仯����Ĳ˵��仯
	 * 
	 * @param currentHandle
	 */
	private void selectionChange(final SVGHandle currentHandle) {
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				if (currentHandle != null
						&& TQ_FILE_TYPE.equals(currentHandle.getCanvas()
								.getFileType())) {
					Set<Element> selectedEles = currentHandle.getSelection()
							.getSelectedElements();
					resetMenusToDefault();
					if (selectedEles == null || selectedEles.size() == 0) {
						return;
					} else {
						Iterator<TQOperation> it = operations.values()
								.iterator();
						while (it.hasNext()) {
							it.next().selectionChange(selectedEles);
						}
					}
				}
			}
		});

	}

	/**
	 * �˵��ĳ�ʼ״̬
	 */
	private void resetMenusToDefault() {
		Iterator<TQOperation> it = operations.values().iterator();
		while (it.hasNext()) {
			it.next().resetMenuToDefault();
		}
	}

	@Override
	public Element createElement(SVGHandle handle, Shape continuesShape,
			String lineID) {
		return ((TQDrawOperation) currentTQOperation).createElement(handle,
				continuesShape, lineID);
	}

	/**
	 * �����Ҫר���ƶ�����Ϊ�������֧�ߣ���Ҫ����ʼ���������ĵ����
	 * 
	 * @param point
	 *            ԭʼ���������
	 * @return ���������ľ�ȷ����ʼ��
	 */
	private Point2D convertStartPoint(SVGHandle handle, Point2D point) {
		if (drawingStarted) {
			return point;
		}
		if (currentTQOperation instanceof TQDrawOperation) {
			if (((TQDrawOperation) currentTQOperation)
					.isPointNeedCentralByDrawing()) {
				Element e = pseudoStartElement;
				if (e != null) {
					point = EditorToolkit.getCenterPoint(e);
					point = handle.getTransformsManager().getScaledPoint(point,
							false);
				}
			}
		}
		return point;
	}

	@Override
	public void notifyDrawingAction(SVGHandle handle, Point2D point,
			int modifier, int type) {

		if (drawable) {

			switch (type) {

			case DRAWING_MOUSE_PRESSED:
				point = convertStartPoint(handle, point);

				if (getCurrentAction().equals(Operations.����.name())) {
					((TQContinuesDrawingHandler) drawingHandler)
							.simpleMousePressed(handle, point);
					drawingStarted = !drawingStarted;
				} else {
					drawingStarted = true;
					drawingHandler.mousePressed(handle, point);
				}

				break;

			case DRAWING_MOUSE_RELEASED:
				drawingHandler.mouseReleased(handle, point);
				// press��Ӧ���ȣ�release��Ӧ�ں�����ڽ�������ʱ��press�Ѿ���Ӧ��ʹdrawingStarted��false
				if (!drawingStarted
						&& getCurrentAction().equals(Operations.����.name())) {
					drawingHandler.mouseDoubleClicked(handle, point);
				}
				break;

			case DRAWING_MOUSE_DRAGGED:
				// drawingHandler.mouseDragged(handle, point);
				break;

			case DRAWING_MOUSE_MOVED:
				if (getCurrentAction().equals(Operations.����.name())) {
					((TQContinuesDrawingHandler) drawingHandler)
							.simpleMouseMoved(handle, point);
				} else {
					drawingHandler.mouseMoved(handle, point);
				}
				break;

			case DRAWING_MOUSE_DOUBLE_CLICK:
				if (getCurrentAction().equals(Operations.����.name())) {
					((TQContinuesDrawingHandler) drawingHandler).mousePressed(
							handle, point);
				} else {
					drawingHandler.mouseDoubleClicked(handle, point);
					resetDrawing();
					drawingStarted = false;
				}
				break;

			case DRAWING_END:

				drawingHandler.reset(handle);
				editor.getHandlesManager().getCurrentHandle().getScrollPane()
						.setListenersEnabled(true);
				break;
			case DRAWING_MOUSE_WHEEL_DOWN: {
				drawingHandler.mouseWheelDown(handle, point);
				break;
			}
			case DRAWING_MOUSE_WHEEL_UP: {
				drawingHandler.mouseWheelUp(handle, point);
				break;
			}
			}
		}
	}

	/**
	 * ��ͬ�����µ���ʾ��ϢҲ��ͬ ֻ����̨��ͼ����ʱ����currentaction�����������Ϊ��
	 * 
	 * @param mode
	 */
	private void modeChanged(ActionEvent e) {
		AbstractShape shape = ((SelectionInfoManager) e.getSource())
				.getDrawingShape();
		if (shape instanceof TQShape) {
			// TQShape tqModule = (TQShape) shape;
			//			
			// if (tqModule.getCurrentTQOperation() != null) {
			// tqModule.getCurrentTQOperation().reset();
			// }
			splitBtn.setToolTipText("��ǰ״̬: " + getCurrentAction());
		} else {
			splitBtn.setToolTipText("̨��ͼ");
			setCurrentAction("");
			setCurrentTQOperation(null);
		}
	}

	/**
	 * ̨��ͼ�¸���ѡ��ģʽ�Ĵ����¼�
	 * 
	 * @param evt
	 */
	private void tqModeSelected(ModeSelectionEvent evt) {
		int currentSelectionMode = evt.getSource().getHandle().getEditor()
				.getSelectionManager().getSelectionMode();
		MouseEvent mouseEvent = evt.getSource().getMouseEvent();
		switch (currentSelectionMode) {
		case TQ_DRAW_MAINPATH_MODE:

			System.out.println("TQ_DRAW_MAINPATH_MODE");
			break;
		case SelectionInfoManager.DRAWING_MODE:
			if (mouseEvent instanceof MouseWheelEvent) {
				// FIXME ����ʱ���ֲ���
				System.out.println("SelectionInfoManager.DRAWING_MODE");
			} else {
				// ������������
				// System.out.println("������������");
			}
		default:
			break;
		}
	}

	/**
	 * ����ڸ���ģʽ��ı仯
	 * 
	 * @param evt
	 */
	private void cursorChange(ModeSelectionEvent evt) {
		if (drawingStarted || currentTQOperation == null
				|| getCurrentAction() == null || getCurrentAction().equals("")) {
			return;
		}
		Element e = evt.getSource().getElementAtMousePoint();
		pseudoStartElement = e;
		SVGCanvas canvas = evt.getSource().getHandle().getCanvas();
		currentTQOperation.cursorChange(e, canvas);
	}

	/**
	 * ҵ�������������¼�
	 * 
	 * @param evt
	 */
	private void tqMousePress(ModeSelectionEvent evt) {
		String currentAction = getCurrentAction();
		if (currentAction == null || currentAction.equals("")
				|| currentTQOperation == null)
			return;
		currentTQOperation.mousePress(evt);
	}

	/**
	 * ̨��ͼ�����½������ӹ�ϵ��ͼԪ��Ҫ������ϣ��絥���ĸ���������
	 * 
	 * @param connectedPathElement
	 */
	private void reformConnectedElements(Element connectedPathElement) {
		Element[] connectedElements = EditorToolkit
				.getConnectedElements(connectedPathElement);
		if (connectedElements != null) {
			// �Ϸ����ж�
			boolean isP0Online = TQToolkit
					.isComponnetOnLine(connectedElements[0]);
			boolean isP1Online = TQToolkit
					.isComponnetOnLine(connectedElements[1]);
			// �������·ID�����������е�֧�ߣ����������е����ߣ��������½���֧�ߣ��������½�������
			Element combinedLine = null;
			// ������·�ϵĲ�������
			if (isP0Online && isP1Online) {
				connectedPathElement.getParentNode().removeChild(
						connectedPathElement);
				editor.getSvgSession().refreshCurrentHandleImediately();
				JideOptionPane.showMessageDialog(editor.getHandlesManager()
						.getCurrentHandle().getCanvas(), "����·�ϵ������豸����������",
						"����", JideOptionPane.WARNING_MESSAGE);
				return;
			}
			// �����һ���Ѿ���ĳ����·��
			else if (isP0Online || isP1Online) {
				Element onlineEle = isP0Online ? connectedElements[0]
						: connectedElements[1];
				Element offlineEle = isP1Online ? connectedElements[1]
						: connectedElements[0];
				// ָ�����豸��Ӧ������·
				Element realLineEle = (Element) onlineEle.getParentNode();
				TQName realLineName = TQToolkit.getComponentName(realLineEle);
				String[] branchLines = TQToolkit.getBranchlines(onlineEle);
				String selectedLineID = realLineName.getUuid();
				// ���ӵĸ���һ������֧��
				if (branchLines != null) {
					JList nameList = new JList();
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setViewportView(nameList);
					TQName[] names = new TQName[branchLines.length + 1];
					names[0] = realLineName;
					DefaultListModel listModel = (DefaultListModel) nameList
							.getModel();
					nameList.getSelectionModel().setSelectionMode(
							ListSelectionModel.SINGLE_SELECTION);
					listModel.addElement(names[0]);

					for (int i = 1; i < names.length; i++) {
						names[i] = TQToolkit.getComonentNameByID(
								branchLines[i - 1], onlineEle);
						listModel.addElement(names[i]);
					}
					nameList.setSelectedIndex(0);
					int result = JideOptionPane.showConfirmDialog(
							editor.getHandlesManager().getCurrentHandle()
									.getCanvas(), scrollPane,
							"��ѡ������������·����\"ȷ��\", ���\"ȡ��\"����Ϊ�µ�֧�߽���",
							JideOptionPane.OK_CANCEL_OPTION,
							JideOptionPane.INFORMATION_MESSAGE);
					if (result == JideOptionPane.OK_OPTION) {
						selectedLineID = ((TQName) listModel
								.getElementAt(nameList.getSelectedIndex()))
								.getUuid();
						// ������¸��������ն˸���,���¸˽���Ϊ�ն˸ˣ�ԭ�ն˸�ȡ���ն˸�����
						if (TQToolkit.isTerminalPole(onlineEle)) {
							// ȡ��ԭ�ն˸����Խڵ�
							Element metadataEle = (Element) onlineEle
									.getElementsByTagName(
											Constants.NCI_SVG_METADATA).item(0);
							NodeList terminalPoles = metadataEle
									.getElementsByTagName(TQToolkit.TQ_METADATA_TERMINAL_POLE);
							if (terminalPoles != null) {
								for (int n = 0; n < terminalPoles.getLength(); n++) {
									Element terminalEle = (Element) terminalPoles
											.item(n);
									if (terminalEle.getAttribute(
											TQToolkit.MODEL_BRANCH_LINE)
											.equals(selectedLineID)) {
										// �Ƴ�ԭ�ն˸˵�����
										metadataEle.removeChild(terminalEle);
										// ���¸��ϼ��ն˸�����
										TQToolkit
												.addMetadata(
														offlineEle,
														TQToolkit.TQ_METADATA_TERMINAL_POLE,
														TQToolkit.TQ_METADATA_LINE_ID,
														selectedLineID, false);
										break;
									}
								}
							}
						}
					} else {// ���µ�֧��

					}
					try {
						combinedLine = (Element) Utilities.findNode("//*[@id='"
								+ selectedLineID + "']", connectedPathElement
								.getOwnerDocument().getDocumentElement());
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}

				}
				// path �ڵ��Ϊ��һ��
				combinedLine.insertBefore(connectedPathElement, onlineEle
						.getParentNode().getFirstChild());
				// use �ڵ�����һ��
				combinedLine.insertBefore(offlineEle, null);
			}
			// ����������
			else {
				Element gEle = connectedPathElement.getOwnerDocument()
						.createElement("g");
				gEle.setAttribute(TQToolkit.MODEL_ATTR,
						TQToolkit.MODEL_MAIN_LINE);
				connectedPathElement.getOwnerDocument().getDocumentElement()
						.appendChild(gEle);
				// gEle.insertBefore(newChild, refChild)
			}
		}
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();

		return map;
	}

	public Collection<PopupItem> getPopupItems() {

		return null;
	}

	public HashMap<String, AbstractButton> getToolItems() {
		HashMap<String, AbstractButton> map = new HashMap<String, AbstractButton>();
		splitBtn = new JideSplitButton("̨��");

		Runnable run = new Runnable() {
			public void run() {
				initActions();
			}
		};
		Utilities.executeRunnable(run);
		map.put("tq", splitBtn);
		splitBtn.setEnabled(false);
		return map;
	}

	private void initActions() {
		Document operDoc = Utilities.getXMLDocumentByStream(getClass()
				.getResourceAsStream("oper-conf.xml"));
		NodeList opers = operDoc.getElementsByTagName("oper");
		JMenuItem menuItem = null;
		for (int i = 0; i < opers.getLength(); i++) {
			Element e = (Element) opers.item(i);
			final String name = e.getAttribute("name");
			String cls = e.getAttribute("class");
			menuItem = new JMenuItem(name);
			menuItem.setAction(new AbstractAction(name) {
				public void actionPerformed(ActionEvent e) {
					doOperation(name);
				}
			});
			splitBtn.add(menuItem);
			if (cls == null || cls.equals("")) {
				editor.getLogger().log(this, LoggerAdapter.INFO,
						"\"" + name + "\"����δ����ʵ���������");
				continue;
			}
			Class<?>[] classargs = { TQShape.class };
			Object[] args = { this };
			try {
				TQOperation oper = (TQOperation) Class.forName(cls)
						.getConstructor(classargs).newInstance(args);
				operations.put(name, oper);
				oper.setMenuItem(menuItem);
			} catch (Exception ex) {
				editor.getLogger().log(this, LoggerAdapter.ERROR, ex);
			}

		}
		// ���ڲ�Ӧ�ü���Ĳ˵�
		resetMenusToDefault();
	}

	private void doOperation(String oper) {
		if (currentTQOperation != null)
			currentTQOperation.reset();
		currentTQOperation = operations.get(oper);
		this.setCurrentAction(oper);
		splitBtn.setToolTipText("��ǰ����:" + oper);
		if (currentTQOperation == null) {
			editor.getLogger().log(this, LoggerAdapter.ERROR, "��Ч��������:" + oper);
			return;
		}
		drawable = false;

		currentTQOperation.doMenuAction();

		if (currentTQOperation instanceof TQDrawOperation) {
			editor.getSelectionManager().setSelectionMode(
					SelectionInfoManager.DRAWING_MODE, this);
			// ��ѡ���symbol��shape��Ϊ��ѡ��״̬
			editor.getSymbolSession().clearSelectedBtnGroup();
			splitBtn.setButtonSelected(true);
			drawingHandler.setDefaultOffset();
			editor.getHandlesManager().getCurrentHandle().getScrollPane()
					.setListenersEnabled(false);
		}
	}

	/**
	 * ��ȡ��������ʼElement����
	 * 
	 * @return
	 */
	public Element getPseudoStartElement() {
		return pseudoStartElement;
	}

	/**
	 * ���ò�������ʼElement����
	 * 
	 * @param e
	 */
	public void setPseudoStartElement(Element e) {
		this.pseudoStartElement = e;
	}

	/**
	 * ��ȡ���ƴ������
	 * 
	 * @return
	 */
	public DrawingHandler getDrawingHandler() {
		return drawingHandler;
	}

	/**
	 * ����̨��ͼģ���Ƿ�ɻ���ͼ��
	 * 
	 * @param flag
	 *            trueΪ�ɻ��ƣ�falseΪ���ɻ���
	 */
	public void setDrawable(boolean flag) {
		drawable = flag;
	}

	/**
	 * �ж�̨��ͼģ���Ƿ�ɻ���ͼ��
	 * 
	 * @return
	 */
	public boolean isDrawable() {
		return drawable;
	}

	/**
	 * �ж�̨��ͼģ���Ƿ��Ѿ���ʼ������
	 * 
	 * @return
	 */
	public boolean isDrawingStarted() {
		return drawingStarted;
	}

	/**
	 * ��ȡ��ǰ̨��ͼ��������
	 * 
	 * @return
	 */
	public TQOperation getCurrentTQOperation() {
		return currentTQOperation;
	}

	/**
	 * ���õ�ǰ̨��ͼ��������
	 * 
	 * @param tqoper
	 */
	public void setCurrentTQOperation(TQOperation tqoper) {
		currentTQOperation = tqoper;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initModule();
			}
		});
		timer.stop();
	}

}
