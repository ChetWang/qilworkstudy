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
 * ͼԪ����ģ�飬����ͼԪ���½�������
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
	 * ͼԪѡ����
	 */
	private JDialog symbolChooser = null;
	/**
	 * ͼԪ�ϴ�ʱ����������Ի���
	 */
	// private JDialog symbolPropertiesInputer = null;
	//
	// private JDialog templatePropertiesInputer = null;
	private JDialog symbolModelChooser = null;
	/**
	 * ͼԪѡ�����
	 */
	private SymbolChooserPanel symbolChooserPanel = null;

	// /**
	// * ͼԪ�ϴ�ʱ�������������
	// */
	// private SymbolSavePanel propertiesInputPanel = null;
	private SymbolModelChooserPanel modelChooserPanel = null;

	// private SVGHandle handle;

	/**
	 * ��ǰģ�����
	 */
	private NCIGraphUnitModule symbolModel = null;

	/**
	 * ͼԪģ���ģ�Ͷ���
	 */
	private ModelBean modelObj = null;

	/**
	 * �������޸ĵ�ͼԪģ��ģ�Ͷ����״̬
	 */
	private String symbolModelStatus = null;

	/**
	 * ģ���ͼԪ������
	 */
	private String symbolName = null;

	/**
	 * ģ�������ڵ�
	 */
	private DefaultMutableTreeNode root = null;

	/**
	 * ���캯��
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
	 * ��ʼ��ģ��ѡ����
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
		// �����ѡ����Ӧ
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
		// ���ֱ仯��Ӧ
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
	 * �½�ͼԪ��ģ��ʱ��������仯�¼�
	 */
	private void symbolNameChanged() {
		this.symbolName = modelChooserPanel.getNameField().getText().trim();
		checkInputLegal();
	}

	/**
	 * ���˺��Զ�ѡ�����ýڵ�
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
	 * ģ�����ڵ㱻ѡ������Ӧ
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
		// comboModel.addElement("--��ѡ��--");
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
				comboModel.addElement("--��ѡ��--");
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
	 * ģ������ѡ���¼�
	 */
	private void symbolTypeSelected() {
		checkInputLegal();
	}

	/**
	 * ����������Ч��
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
	 * ��ʼ��ͼԪѡ����
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
	 * ��ѡ���ͼԪ
	 * 
	 * @param bean
	 *            ѡ���ͼԪbean
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
	 * ɾ��ѡ���ͼԪ
	 * 
	 * @param bean
	 *            ѡ���ͼԪbean
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
			if (result instanceof String) {// ������String��˵������ʧ��
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
			} else {// ����ɹ�
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
	 * ��ʼ���˵�
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
	 * �˵��Ķ���
	 * 
	 * @param index
	 *            �˵����
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
	 * �½�ģ��
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
	 * ����ͼԪ��ͬʱ���浽�������ͱ��أ���Ӱ�쵽�������ء��ڴ������Editor�еģ�
	 * 
	 * @param handle
	 */
	public boolean saveSymbol(final SVGHandle handle, Monitor monitor) {
		while (monitor.getWaitDialog() == null
				|| !monitor.getWaitDialog().isVisible()) {
			try {
				Thread.sleep(10);// �ȴ�monitor��ʾ�������������봰�ڽ���monitor��һ��
			} catch (InterruptedException e) {
			}
		}
		if (isEmptySymbol(handle.getCanvas().getDocument().getDocumentElement())) {
			JOptionPane.showConfirmDialog(handle.getCanvas(), "ͼԪ/ģ��û��ʵ��ͼ������!",
					"ERROR", JOptionPane.CLOSED_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		boolean update = handle.getSymbolBean().getId() != null;
		NCIEquipSymbolBean symbolBean = handle.getSymbolBean() == null ? new NCIEquipSymbolBean()
				: handle.getSymbolBean();

		// �����contentҪĬ��״̬��content,����ǰ�������ǰ����Ĭ��״̬����Ҫת����
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
			if (rs.getReturnFlag() == ResultBean.RETURN_SUCCESS) {// ����ɹ�
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
			} else {// ����ʧ��
				JOptionPane.showConfirmDialog(editor.findParentFrame(), rs
						.getErrorText(), "ERROR", JOptionPane.CLOSED_OPTION,
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return false;
	}

	/**
	 * ͼԪ�½����޸��ϴ��������ɹ��󣬶Ա����ļ��ͽ����ϵ�һЩ����
	 * 
	 * @param bean
	 * @param booelan
	 *            flag,trueΪupdate�޸ģ�falseΪnew�½�
	 */
	public void updateLocalInfo(SVGHandle handle,
			final NCIEquipSymbolBean bean, boolean flag) {
		// �����ļ����޸�,�����ϡ��ڴ����ݵ��޸ģ���Editor��thumnailsMap��AbstractNCIGraphUnitManager��symbolMap
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
		// ��Ҫ��ӵ�����ͼhashmap����
		Map<String, NCIThumbnailPanel> thumMap = editor.getSymbolManager()
				.getThumbnailShownMap().get(symbolTypeBean);
		if (thumMap == null) {
			thumMap = new LinkedHashMap<String, NCIThumbnailPanel>();
		}
		editor.getSymbolManager().getThumbnailShownMap().put(symbolTypeBean,
				thumMap);
		if (flag == false) {// ����
			thumb = new NCIThumbnailPanel(NCIThumbnailPanel.THUMBNAIL_OUTLOOK,
					editor);
			thumb.setText(bean.getName());
			thumb.setSymbolBean(bean.cloneSymbolBean());

			// outlookpanel��Ҫ���£������µ�NCIThumbnailPanel����
			if (editor.getOutlookSymbolPanelMap().get(symbolTypeBean)
					.isInitialized()) {
				editor.getOutlookSymbolPanelMap().get(symbolTypeBean)
						.getScrollPanel().add(thumb);
			}

			thumMap.put(bean.getName(), thumb);
		}
		editor.getSymbolManager().getAllSymbols().get(symbolTypeBean).put(
				bean.getName(), bean);
		// �����������Ļ����޸ĵĶ���Editor��thumnailsMapȡ
		thumb = editor.getSymbolManager().getThumbnailShownMap().get(
				symbolTypeBean).get(bean.getName());
		if (thumb != null) { // �Ѿ���ʾ����
			thumb.setDocument(svgDoc);
			// ����outlookpanel����ϵ���ʾ
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
	 * �½�ͼԪ
	 */
	private void newGraphUnit() {
		// ModuleAdapter browser =
		// editor.getModuleByID(BrowserModule.MODULE_ID);
		// ((BrowserModule)browser).showBrowserDialog("www.163.com");
		createHandle(SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL);
	}

	/**
	 * ����ͼԪģ���SVGHandle
	 * 
	 * @param handleType
	 *            ͼԪ��ģ��
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
				// ����״̬�������û�ͼSelection��parentElement
				handle.setSymbolModelObj(modelObj);
				handle.setSymbolStatus(symbolModelStatus, false);
				//
				handle.getSelection().addBusinessSelection(
						new BusinessInfoLocator(handle));

			}
		}
	}

	/**
	 * ��ȡģ���б�
	 * 
	 * @return ģ���б�
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
			root = new DefaultMutableTreeNode("ģ���б�");
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
	 * ��ģ��д�뱾�أ���Զ�̲��ܶ�ȡʱ����ȡ����
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
	 * �ӱ��ض�ȡģ����
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
	// root = new DefaultMutableTreeNode("ģ���б�");
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
	 * �ڽ�ͼԪ֮ǰ��ҪԤ���������
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
				// ��λ�ȡԶ��ģ���б�������̱ȽϺ�ʱ������Ҫ��swingworker
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
							"����ģ����ʧ�ܣ��̱߳����ţ�");
				} catch (ExecutionException e) {
					editor.getLogger().log(symbolModel, LoggerAdapter.ERROR,
							"����ģ����ʧ�ܣ��߳�ִ�й����г��ִ���");
				}
			}

		};
		worker.execute();
		List<SymbolTypeBean> types = editor.getSymbolManager()
				.getSymbolTypeNames(Utilities.getHandleSymbolType(handleType));
		DefaultComboBoxModel typeComboModel = new DefaultComboBoxModel();
		typeComboModel.addElement("--��ѡ��--");
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
	 * �������͵Ĳ�ͬȷ���������ǡ�ͼԪ�����ǡ�ģ�塰
	 * 
	 * @param handleType
	 */
	private void setPropertiesByHandleType(int handleType) {
		String titleName = "";
		String nameInputTitle = "";
		String typeComboTitle = "";
		switch (handleType) {
		case SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL:
			titleName = "�½�ͼԪ";
			nameInputTitle = "������ͼԪ������";
			typeComboTitle = "��ѡ��ͼԪ����";
			break;
		case SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE:
			titleName = "�½�ģ��";
			nameInputTitle = "������ģ�������";
			typeComboTitle = "��ѡ��ģ������";
			break;
		default:
			editor.getLogger().log(this, LoggerAdapter.FATAL, "�����ڳ�ͼԪ��ģ��֮�������");
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
	 * ��ʾͼԪѡ����
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
