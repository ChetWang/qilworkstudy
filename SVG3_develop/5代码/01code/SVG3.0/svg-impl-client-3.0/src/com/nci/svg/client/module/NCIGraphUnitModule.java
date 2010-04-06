package com.nci.svg.client.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.business.BusinessInfoLocator;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.StringPinyinComparator;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolModelChooserPanel;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.GraphUnitModuleAdapter;
import com.nci.svg.sdk.ui.autocomplete.DefaultCompletionFilter;
import com.nci.svg.sdk.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.sdk.ui.graphunit.SymbolChooserPanel;
import com.nci.svg.swing.model.NCITreeModel;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 图元管理模块，负责图元的新建、管理
 * 
 * @author Qil.Wong
 * 
 */
public class NCIGraphUnitModule extends GraphUnitModuleAdapter {

	private String[] ids = { "nci_graphunit_new", "nci_template_new",
			"nci_graphunit_manage" };
	private JMenuItem[] guMenuItems = new JMenuItem[ids.length];
	private ImageIcon[] icons = new ImageIcon[ids.length];

	/**
	 * 图元选择器
	 */
	private JDialog symbolChooser = null;
	/**
	 * 图元上传时的属性输入对话框
	 */
	// private JDialog symbolPropertiesInputer = null;
	//
	// private JDialog templatePropertiesInputer = null;
	private JDialog symbolModelChooser = null;
	/**
	 * 图元选择面板
	 */
	private SymbolChooserPanel symbolChooserPanel = null;

	// /**
	// * 图元上传时的属性输入面板
	// */
	// private SymbolSavePanel propertiesInputPanel = null;
	private SymbolModelChooserPanel modelChooserPanel = null;

	// private SVGHandle handle;

	/**
	 * 当前模块对象
	 */
	private NCIGraphUnitModule symbolModel = null;

	/**
	 * 图元模板的模型对象
	 */
	private ModelBean modelObj = null;

	/**
	 * 待绘或待修改的图元模板模型对象的状态
	 */
	private String symbolModelStatus = null;

	/**
	 * 模板或图元的名字
	 */
	private String symbolName = null;

	/**
	 * 模型树根节点
	 */
	private DefaultMutableTreeNode root = null;

	/**
	 * 构造函数
	 * 
	 * @param editor
	 */
	public NCIGraphUnitModule(EditorAdapter editor) {
		super(editor);
		symbolModel = this;
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initMenuItems();
			}
		});
	}

	private void modelOk() {
		symbolModelChooser.dispose();
		modelObj = (ModelBean) modelChooserPanel.getSelectedModel();
		Object statusObj = modelChooserPanel.getSelectedStatus();
		if (statusObj != null)
			symbolModelStatus = statusObj.toString();
	}

	/**
	 * 初始化模型选择器
	 */
	private void initModelChooser() {
		symbolModelChooser = new JDialog(editor.findParentFrame(), true);
		modelChooserPanel = new SymbolModelChooserPanel(editor);
		symbolModelChooser.getContentPane().add(modelChooserPanel);
		symbolModelChooser.pack();
		if (editor.getModeManager().isGraphUnitHasModel()) {
			symbolModelChooser.setSize(700, 550);
		} else {
			symbolModelChooser.setSize(300, 220);
		}
		symbolModelChooser.setLocationRelativeTo(editor.findParentFrame());
		modelChooserPanel.getStatusPanel().setVisible(false);
		modelChooserPanel.getCancelBtn().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						symbolModelChooser.dispose();
					}
				});
		modelChooserPanel.getOkBtn().setEnabled(true);
		modelChooserPanel.getOkBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelOk();
			}
		});
		// 添加树选择响应
		modelChooserPanel.getModelTree().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(
							TreeSelectionEvent treeselectionevent) {
						modelTreeSelectionChanged();
					}
				});
		modelChooserPanel.getRefreshItem().addActionListener(
				new ActionListener() {

					public void actionPerformed(ActionEvent arg0) {
						TreeModel model = getSymbolModelTree(null);
						modelChooserPanel.getModelTree().setModel(model);
						modelChooserPanel.getModelTree().setSelectionRow(0);
					}

				});
		// 文字变化响应
		modelChooserPanel.getNameField().getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						symbolNameChanged();
					}

					public void insertUpdate(DocumentEvent e) {
						symbolNameChanged();
					}

					public void removeUpdate(DocumentEvent e) {
						symbolNameChanged();
					}
				});
		modelChooserPanel.getFilterField().getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						setFilteredNodeSelected();
					}

					public void insertUpdate(DocumentEvent e) {
						setFilteredNodeSelected();
					}

					public void removeUpdate(DocumentEvent e) {
						setFilteredNodeSelected();
					}
				});
		modelChooserPanel.getSymbolTypeCombo().addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							symbolTypeSelected();
						}
					}
				});
		modelChooserPanel.getStatusCombo().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (modelChooserPanel.getStatusCombo().isVisible()
						&& e.getStateChange() == ItemEvent.SELECTED) {
					symbolTypeSelected();
				}
			}
		});
	}

	/**
	 * 新建图元或模板时名称输入变化事件
	 */
	private void symbolNameChanged() {
		this.symbolName = modelChooserPanel.getNameField().getText().trim();
		checkInputLegal();
	}

	/**
	 * 过滤后自动选择至该节点
	 */
	private void setFilteredNodeSelected() {
		DefaultMutableTreeNode node = filterMap.get(modelChooserPanel
				.getFilterField().getText().trim());
		if (node == null) {
			modelChooserPanel.getModelTree().setSelectionRow(-1);
		} else {
			TreePath path = new TreePath(((NCITreeModel) modelChooserPanel
					.getModelTree().getModel()).getPathToRoot(node));
			modelChooserPanel.getModelTree().setSelectionPath(path);
		}
	}

	/**
	 * 模型树节点被选择后的响应
	 */
	private void modelTreeSelectionChanged() {
		// Object o = modelChooserPanel.getSelectedModel();
		// if (o != null
		// && o instanceof SymbolModelBean
		// && modelChooserPanel.getSymbolType() ==
		// SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {
		// List statusList = ((SymbolModelBean) o).getStatusList();
		// if (statusList != null && statusList.size() > 0) {
		// DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
		// comboModel.addElement("--请选择--");
		// for (int i = 0; i < statusList.size(); i++) {
		// comboModel.addElement(statusList.get(i));
		// }
		// modelChooserPanel.getStatusCombo().setModel(comboModel);
		// modelChooserPanel.getStatusCombo().setVisible(true);
		// }
		// } else {
		// modelChooserPanel.getStatusCombo().setVisible(false);
		// }
		// checkInputLegal();
		Object o = modelChooserPanel.getSelectedModel();
		if (o != null
				&& o instanceof ModelBean
				&& modelChooserPanel.getSymbolType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {
			ModelBean bean = (ModelBean) o;
			HashMap<String, ModelPropertyBean> statusMap = (HashMap<String, ModelPropertyBean>) (bean
					.getProperties().get(ModelPropertyBean.TYPE_STATUS));
			if (statusMap != null && statusMap.size() > 0) {
				DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
				comboModel.addElement("--请选择--");
				Iterator<ModelPropertyBean> iterator = statusMap.values()
						.iterator();
				while (iterator.hasNext()) {
					comboModel.addElement(iterator.next());
				}
				modelChooserPanel.getStatusCombo().setModel(comboModel);
				modelChooserPanel.getStatusPanel().setVisible(true);
			}
		} else {
			modelChooserPanel.getStatusPanel().setVisible(false);
		}
		checkInputLegal();
	}

	/**
	 * 模型类型选定事件
	 */
	private void symbolTypeSelected() {
		checkInputLegal();
	}

	/**
	 * 检查输入的有效性
	 */
	private void checkInputLegal() {
		boolean legal = false;
		Object o = modelChooserPanel.getSelectedModel();
		if ((editor.getModeManager().isGraphUnitHasModel() && o != null && o instanceof ModelBean)
				|| !editor.getModeManager().isGraphUnitHasModel()) {
			if (!modelChooserPanel.getNameField().getText().trim().equals("")) {
				if (modelChooserPanel.getStatusCombo().isVisible() == false
						|| (modelChooserPanel.getStatusCombo().isVisible() && modelChooserPanel
								.getSymbolTypeCombo().getSelectedIndex() > 0)) {
					if (modelChooserPanel.getSymbolTypeCombo()
							.getSelectedIndex() > 0) {
						legal = true;
					}
				}
			}
		}
		modelChooserPanel.getOkBtn().setEnabled(legal);
	}

	/**
	 * 初始化图元选择器
	 */
	private void initSymbolChooser() {
		symbolChooser = new JDialog(editor.findParentFrame(), false);
		symbolChooserPanel = new SymbolChooserPanel(editor);
		symbolChooserPanel.getOkBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSelectedSymbol(symbolChooserPanel.getSelectedSymbol());
				symbolChooser.setVisible(false);
			}
		});
		symbolChooserPanel.getCancelBtn().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						symbolChooser.setVisible(false);
					}
				});
		symbolChooserPanel.getSymbolTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& e.getButton() == MouseEvent.BUTTON1) {
					TreePath selectPath = symbolChooserPanel.getSymbolTree()
							.getSelectionPath();
					if (selectPath != null) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectPath
								.getLastPathComponent();

						if (node.getUserObject() instanceof NCIEquipSymbolBean) {
							openSelectedSymbol(symbolChooserPanel
									.getSelectedSymbol());
							symbolChooser.setVisible(false);
						}
					}
				}
			}
		});

		symbolChooser.getContentPane().add(symbolChooserPanel);
		symbolChooser.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		symbolChooser.setTitle(ResourcesManager.bundle
				.getString("nci_graphunit_chooser_title"));

		symbolChooser.pack();
	}

	/**
	 * 打开选择的图元
	 * 
	 * @param bean
	 *            选择的图元bean
	 */
	private void openSelectedSymbol(NCIEquipSymbolBean bean) {
		// StringBuffer symbolFilePath = new StringBuffer(editor.getSvgSession()
		// .getSymbolPath(bean));
		int type = -1;
		if (bean.getType().equalsIgnoreCase(
				NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
			type = SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL;
		}
		if (bean.getType().equalsIgnoreCase(
				NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			type = SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE;
		}
		editor.getIOManager().getFileOpenManager().open(null, type, bean);
	}

	/**
	 * 删除选择的图元
	 * 
	 * @param bean
	 *            选择的图元bean
	 */
	private boolean deleteSelectedSymbol(NCIEquipSymbolBean bean) {

		String[][] params = new String[6][2];
		params[0][0] = "graphUnitType";
		params[0][1] = "";
		params[1][0] = "graphUnitID";
		// params[1][1] = bean.getGraphUnitID();
		params[2][0] = "graphUnitName";
		params[2][1] = "";
		params[3][0] = "graphUnitStatus";
		params[3][1] = "";
		params[4][0] = "graphUnitGroup";
		params[4][1] = "";
		params[5][0] = "content";
		params[5][1] = "";
		Object result = null;
		// try {
		// result = Utilities.communicateWithURL((String) editor
		// .getGCParam("appRoot")
		// + (String) editor.getGCParam("servletPath")
		// + "?action=saveSymbolsToDB", params);

		// } catch (IOException e) {
		// e.printStackTrace();
		// return false;
		// }
		ResultBean rs = editor.getCommunicator().communicate(
				new CommunicationBean("getSvgFileListOld", null));
		if (rs != null) {
			result = rs.getReturnObj();
			if (result instanceof String) {// 传回是String，说明保存失败
				JOptionPane
						.showConfirmDialog(
								editor.findParentFrame(),
								ResourcesManager.bundle
										.getString("nci_graphunit_delete_err"),
								ResourcesManager.bundle
										.getString("nci_graphunit_properties_inputer_error"),
								JOptionPane.CLOSED_OPTION,
								JOptionPane.ERROR_MESSAGE);
				return false;
			} else {// 保存成功
				JOptionPane.showConfirmDialog(editor.findParentFrame(),
						ResourcesManager.bundle
								.getString("nci_graphunit_delete_succ"),
						ResourcesManager.bundle
								.getString("nci_optionpane_infomation_title"),
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				// symbolPropertiesInputer.setVisible(false);

			}
		} else {
			return false;
		}

		return true;
	}

	/**
	 * 初始化菜单
	 */
	private void initMenuItems() {
		for (int i = 0; i < guMenuItems.length; i++) {
			icons[i] = ResourcesManager.getIcon(ids[i], false);
			final int index = i;
			guMenuItems[i] = new JMenuItem(ResourcesManager.bundle
					.getString(ids[i]), icons[i]);
			guMenuItems[i].addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					doAction(index);

				}
			});
		}
	}

	/**
	 * 菜单的动作
	 * 
	 * @param index
	 *            菜单序号
	 */
	private void doAction(int index) {
		switch (index) {
		case 0:
			newGraphUnit();
			break;
		case 1:
			newTemplate();
			break;
		case 2:
			showSymbolChooser();
			break;
		default:
			break;
		}
	}

	/**
	 * 新建模板
	 */
	private void newTemplate() {
		createHandle(SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE);
	}

	private boolean isEmptySymbol(Element element) {
		NodeList children = element.getChildNodes();
		boolean flag = true;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node node = children.item(i);
				if (node instanceof Element) {
					if (EditorToolkit.isElementAnActualShape((Element) node)) {
						flag = false;
						break;
					} else if (node.getNodeName().equals("g")) {
						flag = isEmptySymbol((Element) node);
						if (!flag)
							break;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 保存图元，同时保存到服务器和本地，并影响到界面因素、内存变量（Editor中的）
	 * 
	 * @param handle
	 */
	public boolean saveSymbol(final SVGHandle handle, Monitor monitor) {
		while (monitor.getWaitDialog() == null
				|| !monitor.getWaitDialog().isVisible()) {
			try {
				Thread.sleep(10);// 等待monitor显示，否则属性输入窗口将在monitor后一层
			} catch (InterruptedException e) {
			}
		}
		if (isEmptySymbol(handle.getCanvas().getDocument().getDocumentElement())) {
			JOptionPane.showConfirmDialog(handle.getCanvas(), "图元/模板没有实际图形内容!",
					"ERROR", JOptionPane.CLOSED_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		boolean update = handle.getSymbolBean().getId() != null;
		NCIEquipSymbolBean symbolBean = handle.getSymbolBean() == null ? new NCIEquipSymbolBean()
				: handle.getSymbolBean();

		// 这里的content要默认状态的content,保存前，如果当前不是默认状态，就要转过来
		handle.getStatusArea().getStatusComboBox().setSelectedItem(
				handle.getStatusArea().getDefaultSymbolStatus());

		String content = null;
		content = Utilities.printNode(handle.getCanvas().getDocument()
				.getDocumentElement(), false);

		symbolBean.setContent(content);
		String[][] params = new String[7][2];
		params[0][0] = ActionParams.SYMBOL_TYPE;
		params[0][1] = symbolBean.getType();
		params[1][0] = ActionParams.OPERATOR;
		params[1][1] = symbolBean.getOperator();
		params[2][0] = ActionParams.NAME;
		params[2][1] = symbolBean.getName();
		params[3][0] = ActionParams.CONTENT;
		params[3][1] = symbolBean.getContent();
		params[4][0] = ActionParams.VARIETY;
		params[4][1] = symbolBean.getVariety().getCode();
		params[5][0] = ActionParams.SYMBOL_ID;
		params[5][1] = symbolBean.getId();
		params[6][0] = ActionParams.MODEL_ID;
		params[6][1] = symbolBean.getModelID();
		ResultBean rs = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.SAVE_SYMBOL, params));
		if (rs != null) {
			if (rs.getReturnFlag() == ResultBean.RETURN_SUCCESS) {// 保存成功
				JOptionPane.showConfirmDialog(editor.findParentFrame(),
						ResourcesManager.bundle
								.getString("nci_graphunit_save_succ"),
						ResourcesManager.bundle
								.getString("nci_optionpane_infomation_title"),
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				symbolBean.setId(rs.getReturnObj().toString());
				updateLocalInfo(handle, symbolBean.cloneSymbolBean(), update);
				return true;
			} else {// 保存失败
				JOptionPane.showConfirmDialog(editor.findParentFrame(), rs
						.getErrorText(), "ERROR", JOptionPane.CLOSED_OPTION,
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return false;
	}

	/**
	 * 图元新建或修改上传服务器成功后，对本地文件和界面上的一些处理
	 * 
	 * @param bean
	 * @param booelan
	 *            flag,true为update修改，false为new新建
	 */
	public void updateLocalInfo(SVGHandle handle,
			final NCIEquipSymbolBean bean, boolean flag) {
		// 本地文件的修改,界面上、内存数据的修改，有Editor的thumnailsMap，AbstractNCIGraphUnitManager的symbolMap
		// File createFile = new
		// File(editor.getSvgSession().getSymbolPath(bean));
		// XMLPrinter.printStringToFile(bean.getContent(), createFile);
		// final Document svgDoc =
		// Utilities.getSVGDocumentByURL(createFile.toURI()
		// .toString());
		final Document svgDoc = Utilities.getSVGDocumentByContent(bean);
		SymbolTypeBean symbolTypeBean = new SymbolTypeBean(bean.getType(), bean
				.getVariety().cloneCodeBean());
		NCIThumbnailPanel thumb = null;
		// 需要添加的缩略图hashmap集合
		Map<String, NCIThumbnailPanel> thumMap = editor.getSymbolManager()
				.getThumbnailShownMap().get(symbolTypeBean);
		if (thumMap == null) {
			thumMap = new LinkedHashMap<String, NCIThumbnailPanel>();
		}
		editor.getSymbolManager().getThumbnailShownMap().put(symbolTypeBean,
				thumMap);
		if (flag == false) {// 新增
			thumb = new NCIThumbnailPanel(NCIThumbnailPanel.THUMBNAIL_OUTLOOK,
					editor);
			thumb.setText(bean.getName());
			thumb.setSymbolBean(bean.cloneSymbolBean());

			// outlookpanel需要更新，增加新的NCIThumbnailPanel对象
			if (editor.getOutlookSymbolPanelMap().get(symbolTypeBean)
					.isInitialized()) {
				editor.getOutlookSymbolPanelMap().get(symbolTypeBean)
						.getScrollPanel().add(thumb);
			}

			thumMap.put(bean.getName(), thumb);
		}
		editor.getSymbolManager().getAllSymbols().get(symbolTypeBean).put(
				bean.getName(), bean);
		// 不管是新增的还是修改的都从Editor的thumnailsMap取
		thumb = editor.getSymbolManager().getThumbnailShownMap().get(
				symbolTypeBean).get(bean.getName());
		if (thumb != null) { // 已经显示过了
			thumb.setDocument(svgDoc);
			// 更新outlookpanel面板上的显示
			thumb.getSvgCanvas()
					.setSize(NCIThumbnailPanel.outlookPrefferedSize);
			editor.getOutlookSymbolPanelMap().get(symbolTypeBean)
					.getScrollPanel().updateUI();
		}
		// handle.setModified(false);
		handle.setSymbolBean(bean);
		handle.setName(bean.getName());
	}

	/**
	 * 新建图元
	 */
	private void newGraphUnit() {
		// ModuleAdapter browser =
		// editor.getModuleByID(BrowserModule.MODULE_ID);
		// ((BrowserModule)browser).showBrowserDialog("www.163.com");
		createHandle(SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL);
	}

	/**
	 * 创建图元模板的SVGHandle
	 * 
	 * @param handleType
	 *            图元或模板
	 * @see SVGHandle#HANDLE_TYPE_SYMBOL_TEMPLATE
	 * @see SVGHandle#HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
	 */
	private void createHandle(int handleType) {
		if (preInitHandle(handleType)) {
			SVGHandle handle = editor.getHandlesManager().createSVGHandle(
					modelChooserPanel.getNameField().getText().trim(),
					handleType);
			if (handle != null) {
				handle.getCanvas().newDocument(
						Constants.GRAPH_UNIT_WIDTH_StringValue,
						Constants.GRAPH_UNIT_HEIGHT_StringValue);
				NCIEquipSymbolBean symbolBean = new NCIEquipSymbolBean();
				symbolBean.setType(Utilities.getHandleSymbolType(handle
						.getHandleType()));
				if (editor.getModeManager().isGraphUnitHasModel()) {
					symbolBean.setModelID(modelObj.getTypeBean().getId());
				}
				symbolBean.setOperator(editor.getSvgSession().getUser());
				symbolBean.setName(symbolName);
				symbolBean.setContent(Utilities.printNode(handle.getCanvas()
						.getDocument().getDocumentElement(), false));
				symbolBean.setVariety(((SymbolTypeBean) modelChooserPanel
						.getSymbolTypeCombo().getSelectedItem()).getVariety()
						.cloneCodeBean());
				handle.setSymbolBean(symbolBean);
				// 设置状态，并设置绘图Selection的parentElement
				handle.setSymbolModelObj(modelObj);
				handle.setSymbolStatus(symbolModelStatus, false);
				//
				handle.getSelection().addBusinessSelection(
						new BusinessInfoLocator(handle));

			}
		}
	}

	/**
	 * 获取模型列表
	 * 
	 * @return 模型列表
	 */
	public TreeModel getSymbolModelTree(String moduleID) {
		// String[][] params = new String[1][2];
		// params[0][0] = ActionParams.MODULE_ID;
		// params[0][1] = moduleID;
		// ResultBean result = editor.getCommunicator().communicate(
		// new CommunicationBean(ActionNames.GET_MODEL_LIST,
		// params));
		// TreeModel treeModel = null;
		// if (result != null
		// && result.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
		// parseToTreeModel((Map<String, SymbolModelBean>) result
		// .getReturnObj());
		// treeModel = new NCITreeModel(root);
		// writeTreeModel(treeModel);
		// } else {
		// treeModel = readTreeModel();
		// }
		// return treeModel;
		TreeModel treeModel = null;
		parseToTreeModel(moduleID);
		treeModel = new NCITreeModel(root);
		return treeModel;
	}

	private void parseToTreeModel(String moduleID) {
		filterMap.clear();
		ResultBean bean = editor.getDataManage().getData(
				DataManageAdapter.KIND_MODEL, null, null);
		if (bean != null && bean.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
			Vector<Object> filter = new Vector<Object>();
			HashMap<String, ModelBean> map = (HashMap<String, ModelBean>) bean
					.getReturnObj();
			root = new DefaultMutableTreeNode("模型列表");
			Iterator<ModelBean> iterator = map.values().iterator();
			while (iterator.hasNext()) {
				ModelBean modelBean = iterator.next();
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						modelBean);
				root.add(node);
				filter.add(modelBean.toString());
				filterMap.put(modelBean.toString(), node);
			}
			Collections.sort(filter, new StringPinyinComparator());
			this.modelChooserPanel.getFilterField().setFilter(
					new DefaultCompletionFilter(filter));
		}
	}

	/**
	 * 将模型写入本地，待远程不能读取时，读取本地
	 * 
	 * @param model
	 */
	private void writeTreeModel(TreeModel model) {
		try {
			if (modelChooserPanel == null)
				return;
			HashMap temp = new HashMap(4);
			temp.put("modelTree", model);
			temp.put("modelFilter",
					((DefaultCompletionFilter) modelChooserPanel
							.getFilterField().getFilter()).getFilterObjList());
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(Constants.NCI_SVG_CACHE_DIR
							+ "symblModel.ns")));
			oos.writeObject(temp);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从本地读取模型树
	 * 
	 * @return
	 */
	private TreeModel readTreeModel() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					new File(Constants.NCI_SVG_CACHE_DIR + "symblModel.ns")));
			Object o = ois.readObject();
			HashMap temp = (HashMap) o;
			Vector<Object> filter = (Vector) temp.get("modelFilter");
			Collections.sort(filter, new StringPinyinComparator());
			modelChooserPanel.getFilterField().setFilter(
					new DefaultCompletionFilter(filter));
			ois.close();
			return (TreeModel) temp.get("modelTree");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, DefaultMutableTreeNode> filterMap = new HashMap<String, DefaultMutableTreeNode>();

	// private void parseToTreeModel(Map<String, SymbolModelBean> models) {
	// filterMap.clear();
	// root = new DefaultMutableTreeNode("模型列表");
	// Iterator<String> it = models.keySet().iterator();
	// Map<String, DefaultMutableTreeNode> nodesMap = new HashMap<String,
	// DefaultMutableTreeNode>();
	// Vector<Object> filter = new Vector<Object>();
	// SymbolModelBean modelBean = null;
	// while (it.hasNext()) {
	// String clsID = (String) it.next();
	// modelBean = models.get(clsID);
	// filter.add(modelBean.getName());
	// DefaultMutableTreeNode node = new DefaultMutableTreeNode(modelBean);
	// nodesMap.put(clsID, node);
	// filterMap.put(modelBean.getName(), node);
	// }
	// if (modelChooserPanel != null) {
	// Collections.sort(filter, new StringPinyinComparator());
	// this.modelChooserPanel.getFilterField().setFilter(
	// new DefaultCompletionFilter(filter));
	// }
	// Iterator<String> it2 = models.keySet().iterator();
	// while (it2.hasNext()) {
	// modelBean = models.get(it2.next());
	// DefaultMutableTreeNode node = nodesMap.get(modelBean.getId());
	// if (nodesMap.get(modelBean.getParentId()) == null) {
	// root.add(node);
	// } else {
	// nodesMap.get(modelBean.getParentId()).add(node);
	// }
	// }
	// }

	/**
	 * 在建图元之前需要预输入的内容
	 * 
	 * @param handleType
	 * @return
	 */
	private boolean preInitHandle(final int handleType) {
		modelObj = null;
		if (symbolModelChooser == null) {
			initModelChooser();
		}
		modelChooserPanel.clear();
		modelChooserPanel.setSymbolType(handleType);
		SwingWorker<TreeModel, Object> worker = new SwingWorker<TreeModel, Object>() {

			@Override
			protected TreeModel doInBackground() throws Exception {
				// 如何获取远程模型列表，这个过程比较耗时，所以要用swingworker
				TreeModel treeModel = getSymbolModelTree(null);
				return treeModel;
			}

			protected void done() {
				try {
					TreeModel model = get();
					modelChooserPanel.getModelTree().setModel(model);
					if (model != null) {
						modelChooserPanel.getModelTree().expandRow(0);
					}
				} catch (InterruptedException e) {
					editor.getLogger().log(symbolModel, LoggerAdapter.ERROR,
							"构建模型树失败，线程被干扰！");
				} catch (ExecutionException e) {
					editor.getLogger().log(symbolModel, LoggerAdapter.ERROR,
							"构建模型树失败，线程执行过程中出现错误！");
				}
			}

		};
		worker.execute();
		List<SymbolTypeBean> types = editor.getSymbolManager()
				.getSymbolTypeNames(Utilities.getHandleSymbolType(handleType));
		DefaultComboBoxModel typeComboModel = new DefaultComboBoxModel();
		typeComboModel.addElement("--请选择--");
		if (types != null && types.size() > 0) {
			for (SymbolTypeBean bean : types) {
				typeComboModel.addElement(bean);
			}
		}
		modelChooserPanel.getSymbolTypeCombo().setModel(typeComboModel);
		setPropertiesByHandleType(handleType);
		if (handleType == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
			modelChooserPanel.getStatusPanel().setVisible(false);
		}
		symbolModelChooser.setVisible(true);
		if (editor.getModeManager().isGraphUnitHasModel() && modelObj == null)
			return false;
		return true;
	}

	/**
	 * 根据类型的不同确定名称中是”图元“还是”模板“
	 * 
	 * @param handleType
	 */
	private void setPropertiesByHandleType(int handleType) {
		String titleName = "";
		String nameInputTitle = "";
		String typeComboTitle = "";
		switch (handleType) {
		case SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL:
			titleName = "新建图元";
			nameInputTitle = "请输入图元的名称";
			typeComboTitle = "请选择图元类型";
			break;
		case SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE:
			titleName = "新建模板";
			nameInputTitle = "请输入模板的名称";
			typeComboTitle = "请选择模板类型";
			break;
		default:
			editor.getLogger().log(this, LoggerAdapter.FATAL, "不存在除图元、模板之外的类型");
			break;
		}
		symbolModelChooser.setTitle(titleName);
		((TitledBorder) ((CompoundBorder) modelChooserPanel.getNameInputPanel()
				.getBorder()).getOutsideBorder()).setTitle(nameInputTitle);
		((TitledBorder) ((CompoundBorder) modelChooserPanel
				.getSymbolTypeComboPanel().getBorder()).getOutsideBorder())
				.setTitle(typeComboTitle);
		// ((TitledBorder) modelChooserPanel.getNameInputPanel().getBorder())
		// .setTitle(nameInputTitle);
		// ((TitledBorder) modelChooserPanel.getSymbolTypeCombo().getBorder())
		// .setTitle(typeComboTitle);
	}

	/**
	 * 显示图元选择器
	 */
	private void showSymbolChooser() {
		if (symbolChooserPanel == null) {
			initSymbolChooser();
		}
		symbolChooserPanel.clear();
		symbolChooserPanel.initSymbolTree();
		symbolChooser.setLocationRelativeTo(editor.findParentFrame());
		symbolChooser.setVisible(true);
	}

	public String getName() {
		return idGraphUnitManage;
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		if (((String) editor.getGCParam(SysSetDefines.WEB_FLAG)).equals("1")) {
			for (int i = 0; i < ids.length; i++) {
				map.put(ids[i], this.guMenuItems[i]);
			}
		}
		return map;
	}
}
