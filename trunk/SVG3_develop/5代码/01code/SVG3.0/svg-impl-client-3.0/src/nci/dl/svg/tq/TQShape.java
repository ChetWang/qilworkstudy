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
 * 电力台区图模块
 * 
 * @author qi
 * 
 */
public class TQShape extends PathShape implements ActionListener {

	public static final String MODULE_ID = "0a11442c-530e-4bc3-a578-55aaec455807";

	/**
	 * 台区图文件类型
	 */
	public static final String TQ_FILE_TYPE = "district";

	private ImageIcon[] icons = null;

	public static final int TQ_DRAW_MAINPATH_MODE = 1144;

	public String symbolStatus = "正常";

	private JideSplitButton splitBtn = null;

	/**
	 * 在当前的动作下是否可绘制，某些操作必须要满足条件后才绘制，如支线绘制
	 */
	private boolean drawable = true;

	/**
	 * 支线或接户线的第一个节点（已经存在的图元）,及设备新增、删除所在的节点
	 */
	private Element pseudoStartElement = null;

	/**
	 * 是否正在绘制
	 */
	private boolean drawingStarted = false;

	private TQOperation currentTQOperation;

	private Map<String, TQOperation> operations = new LinkedHashMap<String, TQOperation>();

	private Timer timer = new Timer(1500, this);

	public enum Operations {
		绘制线路, 绘制同回, 绘制交跨, 绘制接户, 拉线, 线路延伸, 线路合并, 杆号重排, 新增设备, 删除设备, 数据关联
	}

	public enum LinesDraw {
		绘制干线, 绘制支线, 绘制接户
	}

	public enum LineExtend {
		延伸干支线, 延伸接户线
	}

	public enum EquipOperEnum {
		新增杆塔, 新增计量表, 删除杆塔, 删除计量表
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
		// 元素选择变化引起的菜单变化
		final SelectionChangedListener scl = new SelectionChangedListener() {

			@Override
			public void selectionChanged(Set<Element> selectedElements) {

				selectionChange(getEditor().getHandlesManager()
						.getCurrentHandle());
			}
		};
		// 模式变化下的操作，包括各类鼠标事件状态（press\release....）
		final ModeSelectionListener msl = new ModeSelectionListener() {
			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				tqModeSelected(evt);
			}
		};
		// 模式变化下，只在鼠标移动时的鼠标形状变化
		final ModeSelectionListener cursorLis = new ModeSelectionListener() {
			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				cursorChange(evt);
			}
		};
		// 新增或删除设备时鼠标点击事件
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
	 * 元素选择变化引起的菜单变化
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
	 * 菜单的初始状态
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
	 * 鼠标需要专门移动的行为，如绘制支线，需要在起始杆塔的中心点绘起
	 * 
	 * @param point
	 *            原始的鼠标点击点
	 * @return 经过换算后的精确的起始点
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

				if (getCurrentAction().equals(Operations.拉线.name())) {
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
				// press响应在先，release响应在后，因此在结束绘制时，press已经响应，使drawingStarted＝false
				if (!drawingStarted
						&& getCurrentAction().equals(Operations.拉线.name())) {
					drawingHandler.mouseDoubleClicked(handle, point);
				}
				break;

			case DRAWING_MOUSE_DRAGGED:
				// drawingHandler.mouseDragged(handle, point);
				break;

			case DRAWING_MOUSE_MOVED:
				if (getCurrentAction().equals(Operations.拉线.name())) {
					((TQContinuesDrawingHandler) drawingHandler)
							.simpleMouseMoved(handle, point);
				} else {
					drawingHandler.mouseMoved(handle, point);
				}
				break;

			case DRAWING_MOUSE_DOUBLE_CLICK:
				if (getCurrentAction().equals(Operations.拉线.name())) {
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
	 * 不同操作下的提示信息也不同 只有在台区图绘制时才有currentaction，其他情况都为空
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
			splitBtn.setToolTipText("当前状态: " + getCurrentAction());
		} else {
			splitBtn.setToolTipText("台区图");
			setCurrentAction("");
			setCurrentTQOperation(null);
		}
	}

	/**
	 * 台区图下各类选择模式的触发事件
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
				// FIXME 绘制时滚轮操作
				System.out.println("SelectionInfoManager.DRAWING_MODE");
			} else {
				// 正常的鼠标操作
				// System.out.println("正常的鼠标操作");
			}
		default:
			break;
		}
	}

	/**
	 * 鼠标在各类模式间的变化
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
	 * 业务操作中鼠标点击事件
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
	 * 台区图下重新建立连接关系的图元需要重新组合（如单独的杆塔相连）
	 * 
	 * @param connectedPathElement
	 */
	private void reformConnectedElements(Element connectedPathElement) {
		Element[] connectedElements = EditorToolkit
				.getConnectedElements(connectedPathElement);
		if (connectedElements != null) {
			// 合法性判断
			boolean isP0Online = TQToolkit
					.isComponnetOnLine(connectedElements[0]);
			boolean isP1Online = TQToolkit
					.isComponnetOnLine(connectedElements[1]);
			// 连向的线路ID，可能是已有的支线，可能是已有的主线，可能是新建的支线，可能是新建的主线
			Element combinedLine = null;
			// 都在线路上的不能相连
			if (isP0Online && isP1Online) {
				connectedPathElement.getParentNode().removeChild(
						connectedPathElement);
				editor.getSvgSession().refreshCurrentHandleImediately();
				JideOptionPane.showMessageDialog(editor.getHandlesManager()
						.getCurrentHandle().getCanvas(), "都线路上的两个设备不能相连！",
						"错误", JideOptionPane.WARNING_MESSAGE);
				return;
			}
			// 如果有一条已经在某条线路上
			else if (isP0Online || isP1Online) {
				Element onlineEle = isP0Online ? connectedElements[0]
						: connectedElements[1];
				Element offlineEle = isP1Online ? connectedElements[1]
						: connectedElements[0];
				// 指定的设备对应的主线路
				Element realLineEle = (Element) onlineEle.getParentNode();
				TQName realLineName = TQToolkit.getComponentName(realLineEle);
				String[] branchLines = TQToolkit.getBranchlines(onlineEle);
				String selectedLineID = realLineName.getUuid();
				// 连接的杆有一个或多个支线
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
							"请选择所关联的线路后点击\"确定\", 点击\"取消\"将作为新的支线接入",
							JideOptionPane.OK_CANCEL_OPTION,
							JideOptionPane.INFORMATION_MESSAGE);
					if (result == JideOptionPane.OK_OPTION) {
						selectedLineID = ((TQName) listModel
								.getElementAt(nameList.getSelectedIndex()))
								.getUuid();
						// 如果将新杆塔连在终端杆塔,则新杆将作为终端杆，原终端杆取消终端杆属性
						if (TQToolkit.isTerminalPole(onlineEle)) {
							// 取得原终端杆属性节点
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
										// 移出原终端杆的属性
										metadataEle.removeChild(terminalEle);
										// 在新杆上加终端杆属性
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
					} else {// 建新的支线

					}
					try {
						combinedLine = (Element) Utilities.findNode("//*[@id='"
								+ selectedLineID + "']", connectedPathElement
								.getOwnerDocument().getDocumentElement());
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}

				}
				// path 节点放为第一个
				combinedLine.insertBefore(connectedPathElement, onlineEle
						.getParentNode().getFirstChild());
				// use 节点放最后一个
				combinedLine.insertBefore(offlineEle, null);
			}
			// 都不在线上
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
		splitBtn = new JideSplitButton("台区");

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
						"\"" + name + "\"操作未定义实体操作对象");
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
		// 初期不应该激活的菜单
		resetMenusToDefault();
	}

	private void doOperation(String oper) {
		if (currentTQOperation != null)
			currentTQOperation.reset();
		currentTQOperation = operations.get(oper);
		this.setCurrentAction(oper);
		splitBtn.setToolTipText("当前操作:" + oper);
		if (currentTQOperation == null) {
			editor.getLogger().log(this, LoggerAdapter.ERROR, "无效操作对象:" + oper);
			return;
		}
		drawable = false;

		currentTQOperation.doMenuAction();

		if (currentTQOperation instanceof TQDrawOperation) {
			editor.getSelectionManager().setSelectionMode(
					SelectionInfoManager.DRAWING_MODE, this);
			// 将选择的symbol或shape置为非选择状态
			editor.getSymbolSession().clearSelectedBtnGroup();
			splitBtn.setButtonSelected(true);
			drawingHandler.setDefaultOffset();
			editor.getHandlesManager().getCurrentHandle().getScrollPane()
					.setListenersEnabled(false);
		}
	}

	/**
	 * 获取操作的起始Element对象
	 * 
	 * @return
	 */
	public Element getPseudoStartElement() {
		return pseudoStartElement;
	}

	/**
	 * 设置操作的起始Element对象
	 * 
	 * @param e
	 */
	public void setPseudoStartElement(Element e) {
		this.pseudoStartElement = e;
	}

	/**
	 * 获取绘制处理对象
	 * 
	 * @return
	 */
	public DrawingHandler getDrawingHandler() {
		return drawingHandler;
	}

	/**
	 * 设置台区图模块是否可绘制图形
	 * 
	 * @param flag
	 *            true为可绘制，false为不可绘制
	 */
	public void setDrawable(boolean flag) {
		drawable = flag;
	}

	/**
	 * 判断台区图模块是否可绘制图形
	 * 
	 * @return
	 */
	public boolean isDrawable() {
		return drawable;
	}

	/**
	 * 判断台区图模块是否已经开始绘制了
	 * 
	 * @return
	 */
	public boolean isDrawingStarted() {
		return drawingStarted;
	}

	/**
	 * 获取当前台区图动作对象
	 * 
	 * @return
	 */
	public TQOperation getCurrentTQOperation() {
		return currentTQOperation;
	}

	/**
	 * 设置当前台区图动作对象
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
