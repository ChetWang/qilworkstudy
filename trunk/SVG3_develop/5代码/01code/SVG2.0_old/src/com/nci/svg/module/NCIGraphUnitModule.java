package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.graphunit.NCISymbolStatusBean;
import com.nci.svg.ui.graphunit.GraphUnitChooserPanel;
import com.nci.svg.ui.graphunit.GraphUnitSavePanel;
import com.nci.svg.ui.graphunit.NCIGraphUnitPanel;
import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.util.Constants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 
 * @author Qil.Wong
 * 
 */
public class NCIGraphUnitModule extends ModuleAdapter {

	public static final String idGraphUnitManage = "nci_menu_graphunit_manage";
	private String[] ids = { idGraphUnitManage, "nci_graphunit_new",
			"nci_graphunit_open", "nci_graphunit_delete" };
	private JMenuItem[] guMenuItems = new JMenuItem[ids.length];
	private ImageIcon[] icons = new ImageIcon[ids.length];

	/**
	 * ͼԪѡ����
	 */
	private JDialog chooser = null;
	/**
	 * ͼԪ�ϴ�ʱ����������Ի���
	 */
	private JDialog propertiesInputer = null;
	/**
	 * ͼԪѡ�����
	 */
	private GraphUnitChooserPanel chooserPanel = null;

	/**
	 * ͼԪ�ϴ�ʱ�������������
	 */
	private GraphUnitSavePanel propertiesInputPanel = null;

	private SVGHandle handle;

	/**
	 * ���캯��
	 * 
	 * @param editor
	 */
	public NCIGraphUnitModule(Editor editor) {
		super(editor);
		initMenuItems();
		HandlesListener svgHandlesListener = new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				if (currentHandle != null
						&& (currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_NORMAL || currentHandle
								.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_BAY)) {
					if (currentHandle.getGraphUnit() != null)
						guMenuItems[3].setEnabled(true);
				} else
					guMenuItems[3].setEnabled(false);

			}
		};
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
		initGraphUnitChooser();
		initGraphUnitPropertiesInputer();
	}

	/**
	 * ��ʼ��ͼԪѡ����
	 */
	private void initGraphUnitChooser() {
		chooser = new JDialog(editor.findParentFrame(), true);
		chooserPanel = new GraphUnitChooserPanel(editor);
		chooserPanel.getOkBtn().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openSelectedGraphUnit(chooserPanel.getSelectedGraphUnit());
				chooser.setVisible(false);
			}
		});
		chooserPanel.getCancelBtn().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooser.setVisible(false);
			}
		});
		chooserPanel.getDeleteButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) chooserPanel
						.getGraphUnitTree().getSelectionPath()
						.getLastPathComponent();

				if (node.getUserObject() instanceof NCIEquipSymbolBean) {
					if (deleteSelectedGraphUnit(chooserPanel
							.getSelectedGraphUnit()))
						((DefaultMutableTreeNode) node.getParent())
								.remove(node);
					LinkedHashMap<String, NCIEquipSymbolBean> map = editor
							.getGraphUnitManager().getSymbolMap_Type();
					String strkey = chooserPanel.getSelectedGraphUnit()
							.getGraphUnitType()
							+ "/"
							+ chooserPanel.getSelectedGraphUnit()
									.getGraphUnitName();
					map.remove(strkey);
					map = editor.getGraphUnitManager().getOriginalSymbolMap();
					map.remove(chooserPanel.getSelectedGraphUnit()
							.getGraphUnitID());
					NCIThumbnailPanel panel = editor.getThumnailsMap().get(
							chooserPanel.getSelectedGraphUnit()
									.getGraphUnitType()).get(
							chooserPanel.getSelectedGraphUnit()
									.getGraphUnitName());
					NCIGraphUnitPanel p = editor.getOutlookGraphUnitPanelMap()
							.get(
									chooserPanel.getSelectedGraphUnit()
											.getGraphUnitType());
					p.getScrollPanel().remove(panel);
                    editor.getThumnailsMap().get(chooserPanel.getSelectedGraphUnit().getGraphUnitType())
                    .remove(chooserPanel.getSelectedGraphUnit().getGraphUnitName());
                    p.getScrollPanel().updateUI();
                    chooserPanel.initGraphUnitTree();
				}
			}
		});
		chooserPanel.getGraphUnitTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& e.getButton() == MouseEvent.BUTTON1) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) chooserPanel
							.getGraphUnitTree().getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof NCIEquipSymbolBean) {
						openSelectedGraphUnit(chooserPanel
								.getSelectedGraphUnit());
						chooser.setVisible(false);
					}
				}
			}
		});

		chooser.getContentPane().add(chooserPanel);
		chooser.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		chooser.setTitle(ResourcesManager.bundle
				.getString("nci_graphunit_chooser_title"));

		chooser.pack();
	}

	/**
	 * ��ʼ��ͼԪ���������
	 */
	private void initGraphUnitPropertiesInputer() {
		propertiesInputer = new JDialog(editor.findParentFrame(), true);
		propertiesInputer.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		propertiesInputPanel = new GraphUnitSavePanel(editor);
		propertiesInputPanel.getOkBtn().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				propertiesInputPanel
						.setSelectOption(GraphUnitSavePanel.SELECT_OPTION_OK);
				propertiesInputer.setVisible(false);
			}

		});
		propertiesInputPanel.getCancelBtn().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						propertiesInputPanel
								.setSelectOption(GraphUnitSavePanel.SELECT_OPTION_CANCEL);
						propertiesInputer.setVisible(false);
					}
				});
		propertiesInputer.getContentPane().add(propertiesInputPanel);
		propertiesInputer.setTitle(ResourcesManager.bundle
				.getString("nci_graphunit_properties_inputer_title"));

		propertiesInputer.setResizable(false);
		propertiesInputer.pack();
	}

	/**
	 * ��ѡ���ͼԪ
	 * 
	 * @param bean
	 *            ѡ���ͼԪbean
	 */
	private void openSelectedGraphUnit(NCIEquipSymbolBean bean) {
		StringBuffer graphUnitFilePath = new StringBuffer(
				Constants.NCI_SVG_SYMBOL_CACHE_DIR).append(
				bean.getGraphUnitType()).append("/").append(
				bean.getGraphUnitName()).append(Constants.NCI_SVG_EXTENDSION);
		editor.getIOManager().getFileOpenManager().open(null,
				new File(graphUnitFilePath.toString()), null,
				SVGHandle.HANDLE_TYPE_GRAPH_UNIT_NORMAL, bean);
	}

	/**
	 * ɾ��ѡ���ͼԪ
	 * 
	 * @param bean
	 *            ѡ���ͼԪbean
	 */
	private boolean deleteSelectedGraphUnit(NCIEquipSymbolBean bean) {

		String[][] params = new String[6][2];
		params[0][0] = "graphUnitType";
		params[0][1] = "";
		params[1][0] = "graphUnitID";
		params[1][1] = bean.getGraphUnitID();
		;
		params[2][0] = "graphUnitName";
		params[2][1] = "";
		params[3][0] = "graphUnitStatus";
		params[3][1] = "";
		params[4][0] = "graphUnitGroup";
		params[4][1] = "";
		params[5][0] = "content";
		params[5][1] = "";
		Object result = null;
		try {
			result = Utilities.communicateWithURL((String) editor
					.getGCParam("appRoot")
					+ (String) editor.getGCParam("servletPath")
					+ "?action=saveSymbolsToDB", params);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (result instanceof String) {// ������String��˵������ʧ��
			JOptionPane
					.showConfirmDialog(
							propertiesInputer,
							ResourcesManager.bundle
									.getString("nci_graphunit_delete_err"),
							ResourcesManager.bundle
									.getString("nci_graphunit_properties_inputer_error"),
							JOptionPane.CLOSED_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return false;
		} else {// ����ɹ�
			JOptionPane.showConfirmDialog(propertiesInputer,
					ResourcesManager.bundle
							.getString("nci_graphunit_delete_succ"),
					ResourcesManager.bundle
							.getString("nci_optionpane_infomation_title"),
					JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			propertiesInputer.setVisible(false);

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
			if (i == 3)// ɾ���Ĳ˵���û��ͼԪ�༭��ʾ��ʱ��Ӧ�ÿ���
				guMenuItems[i].setEnabled(false);
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
			showGraphUnitManage();
			break;
		case 1:
			newGraphUnit();
			break;
		case 2:
			showGraphUnitChooser();
			break;
		case 3:
			deleteGraphUnit();
			break;
		default:
			break;
		}
	}

	public Document tranDoc(Document doc,NCIEquipSymbolBean graphUnit)
	{
	    DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        SVGDocument desDoc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
        desDoc.removeChild(desDoc.getDocumentElement());
        desDoc.appendChild(desDoc.importNode(doc.getDocumentElement(), true));
        
        Element symbolElement = desDoc.getElementById(graphUnit.getGraphUnitName());
        Element defs = null;
        if (desDoc.getElementsByTagName("defs").getLength() == 0) {
            defs = desDoc.createElementNS(doc.getDocumentElement()
                    .getNamespaceURI(), "defs");
            desDoc.getDocumentElement().appendChild(defs);
        } else
            defs = (Element) desDoc.getElementsByTagName("defs").item(0);
        if(symbolElement != null)
        {
            defs.removeChild(symbolElement);
            symbolElement = null;
        }
        if(symbolElement == null)
        {
            symbolElement = desDoc.createElementNS(desDoc.getDocumentElement().getNamespaceURI(),"symbol");
            
            symbolElement.setAttribute("id", graphUnit.getGraphUnitName());
            
            
            defs.appendChild(symbolElement);
        }
        
        String strViewBox = "0 0 " + desDoc.getDocumentElement().getAttribute("width") + " " + 
            desDoc.getDocumentElement().getAttribute("height");
        symbolElement.setAttribute("viewBox", strViewBox);
        symbolElement.setAttribute("preserveAspectRatio", "xMidYMid meet");
        
        NodeList list = desDoc.getDocumentElement().getChildNodes();
        for(int i = 0;i < list.getLength();i++)
        {
            if(list.item(i) instanceof Element && 
                    (!list.item(i).getNodeName().equals("defs")
                            && !list.item(i).getNodeName().equals("title")))
            {
                symbolElement.appendChild(desDoc.importNode(list.item(i), true));  
            }
                       
        }
        
        for(int i = 0;i < list.getLength();i++)
        {
            if(list.item(i) instanceof Element && 
                    (!list.item(i).getNodeName().equals("defs")
                            && !list.item(i).getNodeName().equals("title")))
            {
                desDoc.getDocumentElement().removeChild(list.item(i));
                i--;
            }
            
        }
        
        try {
            list = Utilities.findNodes("//*[@type='exchangeable' or @type='adjustable']", symbolElement);
            for(int i = 0;i < list.getLength();i++)
            {
                NodeList childList = list.item(i).getChildNodes();
                for(int j =0;j< childList.getLength();j++)
                {
                    if(childList.item(i) instanceof Element)
                    {
                        Element element = (Element)childList.item(i);
                        EditorToolkit.setStyleProperty(element, "stroke", "");
                        EditorToolkit.setStyleProperty(element, "fill", "");
                    }
                }
            }
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Element useElement = ((SVGOMDocument) desDoc).createElementNS(desDoc
                .getDocumentElement().getNamespaceURI(), "use");
        useElement.setAttribute("class", "fillnone");
        desDoc.getDocumentElement().appendChild(useElement);

        useElement.setAttribute("filterUnits", "userSpaceOnUse");
        useElement.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
                "#" + graphUnit.getGraphUnitName());
	    return desDoc;
	}
	/**
	 * ����ͼԪ��ͬʱ���浽�������ͱ��أ���Ӱ�쵽�������ء��ڴ������Editor�еģ�
	 * 
	 * @param handle
	 */
	public void saveGraphUnit(final SVGHandle handle, Monitor monitor) {
		this.handle = handle;
		propertiesInputPanel.reset();
		while (monitor.getWaitDialog() == null
				|| !monitor.getWaitDialog().isVisible()) {
			try {
				Thread.sleep(10);// �ȴ�monitor��ʾ�������������봰�ڽ���monitor��һ��
			} catch (InterruptedException e) {
			}
		}
		final boolean update = handle.getGraphUnit() != null;
		final NCIEquipSymbolBean graphUnit = handle.getGraphUnit() == null ? new NCIEquipSymbolBean()
				: handle.getGraphUnit();
		propertiesInputPanel.setUpdateMode(update);
		propertiesInputer.setLocationRelativeTo(monitor.getWaitDialog());
		propertiesInputPanel.initData();
		propertiesInputPanel.setValue(graphUnit);
		propertiesInputer.setVisible(true);
		while (propertiesInputPanel.getSelectOption() == GraphUnitSavePanel.SELECT_OPTION_OK) {
			propertiesInputPanel.refreshSymbolBean(graphUnit);
			if (propertiesInputPanel.checkInput(graphUnit)) {
				Element title = null;
				if(handle.getCanvas().getDocument().getElementsByTagName("title").getLength() == 0)
				{
				    title = handle.getCanvas().getDocument().createElement(
						"title");
				    handle.getCanvas().getDocument().getDocumentElement()
                    .appendChild(title);
				}
				else
				    title = (Element)handle.getCanvas().getDocument().getElementsByTagName("title").item(0);
				title.setAttribute("name", graphUnit.getGraphUnitName());
				
				Document doc = tranDoc(handle.getCanvas()
                        .getDocument(),graphUnit);
				graphUnit.setContent(Utilities.printNode(doc.getDocumentElement(), false));
				String[][] params = new String[6][2];
				params[0][0] = "graphUnitType";
				params[0][1] = graphUnit.getGraphUnitType();
				params[1][0] = "graphUnitID";
				params[1][1] = graphUnit.getGraphUnitID();
				params[2][0] = "graphUnitName";
				params[2][1] = graphUnit.getGraphUnitName();
				params[3][0] = "graphUnitStatus";
				params[3][1] = graphUnit.getGraphUnitStatus();
				params[4][0] = "graphUnitGroup";
				params[4][1] = graphUnit.getGraphUnitGroup();
				params[5][0] = "content";
				params[5][1] = graphUnit.getContent();
				Object result = "";
				try {
					result = Utilities.communicateWithURL((String) editor
							.getGCParam("appRoot")
							+ (String) editor.getGCParam("servletPath")
							+ "?action=saveSymbolsToDB", params);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (result instanceof String) {// ������String��˵������ʧ��
					JOptionPane
							.showConfirmDialog(
									propertiesInputer,
									ResourcesManager.bundle
											.getString("nci_graphunit_save_err"),
									ResourcesManager.bundle
											.getString("nci_graphunit_properties_inputer_error"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.ERROR_MESSAGE);
				} else {// ����ɹ�
					JOptionPane
							.showConfirmDialog(
									propertiesInputer,
									ResourcesManager.bundle
											.getString("nci_graphunit_save_succ"),
									ResourcesManager.bundle
											.getString("nci_optionpane_infomation_title"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
					NCIEquipSymbolBean bean = (NCIEquipSymbolBean) result;
					updateLocalInfo(bean, update);
					propertiesInputer.setVisible(false);

				}
				break;
			} else {
				propertiesInputPanel
						.setSelectOption(GraphUnitSavePanel.SELECT_OPTION_CANCEL);
				propertiesInputer.setVisible(true);
			}
		}
	}

	/**
	 * ͼԪ�½����޸��ϴ��������ɹ��󣬶Ա����ļ��ͽ����ϵ�һЩ����
	 * 
	 * @param bean
	 * @param booelan
	 *            flag,trueΪupdate�޸ģ�falseΪnew�½�
	 */
	private void updateLocalInfo(final NCIEquipSymbolBean bean, boolean flag) {
		// �����ļ����޸�,�����ϡ��ڴ����ݵ��޸ģ���Editor��thumnailsMap��AbstractNCIGraphUnitManager��symbolMap
		File createFile = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR
				+ bean.getGraphUnitType() + "/" + bean.getGraphUnitName()
				+ Constants.NCI_SVG_EXTENDSION);
		XMLPrinter.printStringToFile(bean.getContent(), createFile);
		final Document svgDoc = Utilities.getSVGDocument(createFile.toURI()
				.toString());
		NCIThumbnailPanel thumb = null;
		if (flag == false) {// ����
			thumb = new NCIThumbnailPanel(NCIThumbnailPanel.THUMBNAIL_OUTLOOK,
					editor);
			thumb.setText(bean.getGraphUnitName());
			thumb.setSymbolBean(bean);
			// outlookpanel��Ҫ���£������µ�NCIThumbnailPanel����
			if(editor.getOutlookGraphUnitPanelMap().get(bean.getGraphUnitType()).isInitialized()){
				editor.getOutlookGraphUnitPanelMap().get(bean.getGraphUnitType())
						.getScrollPanel().add(thumb);
			}
			editor.getThumnailsMap().get(bean.getGraphUnitType()).put(
					bean.getGraphUnitName(), thumb);
			editor.getGraphUnitManager().getOriginalSymbolMap().put(
					bean.getGraphUnitID(), bean);
			editor.getGraphUnitManager().getSymbolMap_Type().put(
					bean.getGraphUnitType() + "/" + bean.getGraphUnitName(),
					bean);

			if (!bean.getGraphUnitStatus().equals("��")) {
				NCISymbolStatusBean statusBean = editor.getGraphUnitManager()
						.getStatusMap().get(bean.getGraphUnitGroup());
				if (statusBean == null) {
					statusBean = new NCISymbolStatusBean(editor
							.getGraphUnitManager().getSymbolsStatus());
				}

				// ���������ɷ���������
				statusBean.setStatusSymbol(editor.getGraphUnitManager()
						.getSymbolsStatus(), bean.getGraphUnitStatus(), bean
						.getGraphUnitID());

				editor.getGraphUnitManager().getStatusMap().put(
						bean.getGraphUnitGroup(), statusBean);
			}
		}
		// �����������Ļ����޸ĵĶ���Editor��thumnailsMapȡ
		thumb = editor.getThumnailsMap().get(bean.getGraphUnitType()).get(
				bean.getGraphUnitName());
		thumb.setDocument(svgDoc);
		// ����outlookpanel����ϵ���ʾ
		thumb.getSvgCanvas().setSize(NCIThumbnailPanel.outlookPrefferedSize);
		editor.getOutlookGraphUnitPanelMap().get(bean.getGraphUnitType())
				.getScrollPanel().updateUI();
		// ji
		handle.setModified(false);
		handle.setGraphUnit(bean);
		handle.setName(createFile.getAbsolutePath());

	}

	/**
	 * ɾ��ͼԪ
	 */
	private void deleteGraphUnit() {

	}

	private void newGraphUnit() {
		SVGHandle handle = editor.getHandlesManager().createSVGHandle(" ",
				SVGHandle.HANDLE_TYPE_GRAPH_UNIT_NORMAL);
		if (handle != null) {
			handle.getScrollPane().getSVGCanvas().newDocument(
					Constants.GRAPH_UNIT_WIDTH_StringValue,
					Constants.GRAPH_UNIT_HEIGHT_StringValue);
		}
	}

	/**
	 * ��ʾͼԪѡ����
	 */
	private void showGraphUnitChooser() {
		chooserPanel.clear();
		chooserPanel.initGraphUnitTree();
		chooser.setLocationRelativeTo(editor.findParentFrame());
		chooser.setVisible(true);
	}

	/**
	 * ��ʾͼԪ�������,ͼԪ�������ֻ������ʾһ������������Ѿ��򿪵�ͼԪ������棬�򽫸ý�Ŀ�ö�
	 */
	private void showGraphUnitManage() {
		boolean isOpen = false;
		Collection<SVGHandle> handles = editor.getHandlesManager().getHandles();
		Iterator<SVGHandle> it = handles.iterator();
		while (it.hasNext()) {
			SVGHandle handle = it.next();
			// �����ͼԪ�����SVGHandle�������ö�
			if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD) {
				isOpen = true;
				editor.getHandlesManager().setCurrentUnitHandle(handle);
			}
		}
		if (!isOpen) {
			SVGHandle handle = editor.getHandlesManager().createSVGHandle(" ",
					SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD);
			if (handle != null) {
				handle.getScrollPane().getSVGCanvas().newSVGUnitDocument();
			}

		}
	}

	public String getName() {
		return idGraphUnitManage;
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		if (((String) editor.getGCParam("webflag")).equals("1")) {
			for (int i = 0; i < ids.length; i++) {
				map.put(ids[i], this.guMenuItems[i]);
			}
		}
		return map;
	}
}
