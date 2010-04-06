package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.xpath.XPathExpressionException;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import chrriis.dj.nativeswing.ui.JWebBrowser;

import com.nci.svg.equip.EquipClassPanel;
import com.nci.svg.equip.EquipObjrefPanel;
import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;

import com.nci.svg.other.ComputeID;
import com.nci.svg.shape.GraphUnitImageShape;

import com.nci.svg.ui.equip.EquipPropery;
import com.nci.svg.ui.equip.EquipRelation;
import com.nci.svg.ui.equip.EquipRelationDialog;
import com.nci.svg.ui.equip.RelateStationOrLine;
import com.nci.svg.ui.liifecycle.VoltageClassDialog;
import com.nci.svg.util.Constants;
import com.nci.svg.util.EquipPool;
import com.nci.svg.util.NCIEquipPropConstants;
import com.nci.svg.util.ServletActionConstants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.actions.popup.PopupSubMenu;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 
 * @author Qil.Wong
 */
public class EquipmentPropertyModule extends ModuleAdapter {

	public static final String equipmentPropertyID = "nci_equip_property";
	// private JMenuItem equipmentPropertyMenuItem = null;
	// private ImageIcon equipmentPropertyIcon = null;
	private String equipmentPropertylabel = null;
	// private JWebBrowser browser;

	// private ToolsFrame equipPropertiesFrame;
	private EquipPropery property;
	// 关联设备、查看属性


	private VoltageClassDialog voltageClassDialog;

	private String[] popupID = { "nci_relateEquip", "nci_viewProperties",
			"nci_putOnUseage", "nci_special", "nci_dangerousPoint",
			"nci_stopElectric","nci_classProperties","nci_relateStationOrLine"};


	private JDialog objrefDialog,classDialog;
	private EquipObjrefPanel objrefPanel = null;
	private EquipClassPanel classPanel = null;
	private EquipRelationDialog equipRelationDialog = null;
	private RelateStationOrLine relateDialog = null;
	/**
	 * 构造函数
	 * 
	 * @param editor
	 */
	public EquipmentPropertyModule(Editor editor) {
		super(editor);
		ResourceBundle bundle = ResourcesManager.bundle;
		equipmentPropertylabel = bundle.getString(equipmentPropertyID);
		property = new EquipPropery(equipmentPropertyID, editor);

		objrefDialog = new JDialog(editor.findParentFrame());
		objrefDialog.setTitle("metadata属性设置");
		classDialog = new JDialog(editor.findParentFrame());
		classDialog.setTitle("格式设置");
		objrefPanel = new EquipObjrefPanel(null,objrefDialog);
		classPanel = new EquipClassPanel(null,classDialog,editor);
		objrefDialog.add(objrefPanel);
		classDialog.add(classPanel);
		objrefDialog.pack();
		classDialog.pack();
		objrefDialog.setLocationRelativeTo(editor.findParentFrame());
		classDialog.setLocationRelativeTo(editor.findParentFrame());
		objrefDialog.setVisible(false);
		classDialog.setVisible(false);
		
		equipRelationDialog = new EquipRelationDialog(editor.findParentFrame(),true,editor);
		equipRelationDialog.setTitle("设备关联");
		equipRelationDialog.pack();
		equipRelationDialog.setLocationRelativeTo(editor.findParentFrame());
		equipRelationDialog.setVisible(false);
		
		relateDialog = new RelateStationOrLine(editor.findParentFrame(),true,editor);
		relateDialog.setTitle("设备关联");
		relateDialog.pack();
		relateDialog.setLocationRelativeTo(editor.findParentFrame());
		relateDialog.setVisible(false);
		// equipmentPropertyMenuItem = new JMenuItem(equipmentPropertylabel,
		// equipmentPropertyIcon);
		// equipmentPropertyMenuItem.addActionListener(equipPropMenuListener);
		// NativeComponent.getNextInstanceOptions().setFiliationType(
		// FiliationType.COMPONENT_PROXYING);
		// NativeComponent.getNextInstanceOptions().setVisibilityConstraint(
		// VisibilityConstraint.FULL_COMPONENT_TREE);
		// browser = new JWebBrowser();

		// browser.setURL("http://www.163.com");
		// JPanel panel = new JPanel();
		// panel.add(browser);

		// equipPropertiesFrame = new ToolsFrame(editor, equipmentPropertyID,
		// equipmentPropertylabel, browser);

	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		HashMap<String, AbstractButton> toolItems = new HashMap<String, AbstractButton>();
		// toolItems.put(equipmentPropertyID, equipPropertiesFrame
		// .getToolBarButton());
//		toolItems.put(equipmentPropertyID, property.getToolBarButton());
		return toolItems;

	}

	@Override
	public Collection<PopupItem> getPopupItems() {
		LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();
		String iconName = null;
		PopupSubMenu subMenu = new PopupSubMenu(editor, equipmentPropertyID,
				equipmentPropertylabel, equipmentPropertyID);
		// creating the about popup item
		for (int i = 0; i < popupID.length; i++) {
		    if(i>= 3 && i <6)
		        continue;
			final int index = i;
			PopupItem item = new PopupItem(editor, popupID[i],
					ResourcesManager.bundle.getString(popupID[i]), iconName) {

				@Override
				public JMenuItem getPopupItem(LinkedList<Element> nodes) {
					menuItem.setEnabled(false);
					if (nodes.size() == 1) {//0表示没选中任何图形，1表示图形选中一个图元，2及以上表示选中多个图元
						// adds the action listeners
						final Element element = Utilities
								.parseSelectedElement(nodes.get(0));
						boolean bEnable = false;
						// Node metadata = element.getNextSibling();
						Node metadata = Utilities.getSingleChildElement(
								Constants.NCI_SVG_METADATA, element);
						if(metadata == null)
						{
						    metadata = Utilities.getSingleSibingElement(
	                                Constants.NCI_SVG_METADATA, element);
						}
						String nciTypeValue = element
								.getAttribute(Constants.NCI_SVG_Type_Attr);
						// 如果是nciType的设备类型，或者是有metadata的非text类型节点，
						// 或者是包含metadata的g节点（含多个子设备），都当作是一个设备
						// if (metadata != null) {

						
						try {
							if ((nciTypeValue != null && Constants.nciGraphTypes
									.contains(nciTypeValue))
									|| (!element.getNodeName()
											.equalsIgnoreCase("text") && metadata
											.getNodeName().equalsIgnoreCase(
													Constants.NCI_SVG_METADATA))
									|| (element.getNodeName().equalsIgnoreCase(
											"g") && (element
											.getElementsByTagName(
													Constants.NCI_SVG_METADATA)
											.getLength() > 0))) {
							    bEnable = true;
							}

							    if(bEnable)
							    {
								menuItem
										.addActionListener(new ActionListener() {

											public void actionPerformed(
													ActionEvent e) {

												doAction(index, element);
											}
										});
								menuItem.setEnabled(true);
							}
						} catch (NullPointerException e) {

						}
					}

					return super.getPopupItem(nodes);
				}
			};
			subMenu.addPopupItem(item);
		}
		popupItems.add(subMenu);

		return popupItems;
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
		// menuItems.put("ToolFrame_" + equipmentPropertyID,
		// equipPropertiesFrame
		// .getMenuItem());
		menuItems.put("ToolFrame_" + equipmentPropertyID, property
				.getMenuItem());
		return menuItems;
	}

	/**
	 * 设备投运
	 */
	private void putOnUsage(Element selectedEquipElement) {
		ArrayList<String> voltageNames = new ArrayList<String>();
		NodeList filters = editor.getHandlesManager().getCurrentHandle()
				.getCanvas().getDocument().getDocumentElement()
				.getElementsByTagName("filter");
		for (int i = 0; i < filters.getLength(); i++) {
			voltageNames.add(((Element) filters.item(i)).getAttribute("id")
					.replaceAll("_Image", ""));
		}
		if (voltageClassDialog == null) {
			voltageClassDialog = new VoltageClassDialog(editor, true);
		}
		voltageClassDialog.setValue(voltageNames);
		voltageClassDialog.setVisible(true);
		String voltageClass = voltageClassDialog.getSelectedVoltage();
		if (voltageClass != null && !voltageClass.equals("")) {
			selectedEquipElement.setAttributeNS(null, "filter", "url(#"
					+ voltageClass + "_Image" + ")");
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
	}

	/**
	 * 特殊区操作
	 * 
	 * @param selectedEquipElement
	 */
	private void speciArea(Element selectedEquipElement) {

	}

	/**
	 * 危险点处理
	 * 
	 * @param selectedEquipElement
	 */
	private void dangerousPoint(Element selectedEquipElement) {

		String strUrl = "http://10.147.218.235:50000/web/epms/reforward.do?action=pdqxtb&pageTitle=配电缺陷填报&menuInvoke=true&U=1216096195560?U=1216096195386";
		insertDangerousPoint(selectedEquipElement);
		// 调用页面
		Utilities.gotoWebSite(strUrl, "新增危险点");
	}

	private void insertDangerousPoint(Element selectedEquipElement) {
		if (selectedEquipElement == null)
			return;
		Rectangle2D bound = editor.getHandlesManager().getCurrentHandle()
				.getSvgElementsManager().getSensitiveBounds(
						selectedEquipElement);
		bound.setRect(bound.getMaxX() - 16, bound.getMinY(), 16, 16);
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		GraphUnitImageShape shape = new GraphUnitImageShape(editor);
		shape.createDangerousPoint(handle, bound);
	}

	


	/**
	 * 停电处理
	 * 
	 * @param selectedEquipElement
	 */
	private void stopElectric(Element selectedEquipElement) {
        //校验是否闭合状态
	}

	/**
>>>>>>> 1.22
	 * 子菜单的具体操作
	 * 
	 * @param index
	 *            子菜单序号
	 * @param e
	 */
	private void doAction(int index, Element selectedEquipElement) {
		// 如果是单个的包含单个use节点的<g>节点，就要将use节点剥离出来，使use节点成为真正作为选择的元素
		if (selectedEquipElement.getElementsByTagName("use").getLength() == 1
				&& selectedEquipElement.getElementsByTagName(
						Constants.NCI_SVG_METADATA).getLength() == 1)
			selectedEquipElement = (Element) selectedEquipElement
					.getElementsByTagName("use").item(0);
		switch (index) {
		case 0:// 关联设备
			this.relateEquipNew(selectedEquipElement);
			break;
		case 1:// 查看属性
//			this.showProperties(selectedEquipElement);
		    this.showMetadataProperties(selectedEquipElement);
			break;
		case 2:// 投运
			this.putOnUsage(selectedEquipElement);
			break;
		case 3: // 特殊区
			this.speciArea(selectedEquipElement);
			break;
		case 4:// 危险点处理
			this.dangerousPoint(selectedEquipElement);
			break;
		case 5:// 停电处理
			this.stopElectric(selectedEquipElement);
			break;
		case 6://格式设置 
		    this.classProperties(selectedEquipElement);
		    break;
		case 7://场站或线路关联
		    this.relateStationOrLine(selectedEquipElement);
		    break;
		}
	}
	/**场站或线路关联
	 * @param selectedEquipElement
	 */
	private void relateStationOrLine(Element selectedEquipElement)
	{
	    String strName = editor.getHandlesManager().getCurrentHandle()
                    .getSubstationName();
	    int nType = editor.getHandlesManager().getCurrentHandle().getNFileType();
	    if(strName == null || strName.length() == 0)
	        return;
        String equipType = editor.getMetaDataManager().getCIMType(selectedEquipElement);
        Element objref = Utilities.getSingleChildElement("PSR:ObjRef", selectedEquipElement);
        if(objref == null)
            return;
        
        //先提取psmsid
        String strcode = objref.getAttribute("AppCode");
        if(strcode.length() != 0)
        {
            if(relateDialog.initData(nType,strName,"psms",strcode)== 0)
                     relateDialog.setVisible(true);
            return;
        }
        
        //psmsid不存在，提取scadaid
        strcode = objref.getAttribute("ScadaID");
        if(strcode.length() != 0)
        {
            if(relateDialog.initData(nType,strName,"scada",strcode) == 0)
                relateDialog.setVisible(true);
            return;
        }
        
        strcode = objref.getAttribute("ObjectID");
        if(strcode.length() != 0)
        {
            if(relateDialog.initData(nType,strName,"scada",strcode)== 0)
                     relateDialog.setVisible(true);
            return;
        }
        
        //scadaid不存在，提取misid
        strcode = objref.getAttribute("MisID");
        if(strcode.length() != 0)
        {
            if(relateDialog.initData(nType,strName,"psms",strcode) == 0)
                relateDialog.setVisible(true);
            return;
        }
        
        
	}
	
	private void classProperties(Element selectedEquipElement)
	{
	    classPanel.setElement(selectedEquipElement);
        classDialog.setVisible(true);
	}

	private void relateEquipNew(final Element selectedEquipElement) {
	    int nType = 0;
	    String strName = editor.getHandlesManager().getCurrentHandle()
                .getSubstationName();
        String equipType = editor.getMetaDataManager().getCIMType(selectedEquipElement);
        String strScadaID = editor.getMetaDataManager().getScadaID(selectedEquipElement);
        if(strScadaID == null || strScadaID.length() == 0)
            return;
        equipRelationDialog.initData(nType, strName, equipType, strScadaID);
        equipRelationDialog.setVisible(true);
        
	    return;
	}
	/**
	 * 关联设备
	 * 
	 * @param selectedEquipElement
	 *            选定的设备节点
	 */
	private void relateEquip(final Element selectedEquipElement) {
		String substaionName = editor.getHandlesManager().getCurrentHandle()
				.getSubstationName();
		String equipType = editor.getMetaDataManager().getCIMType(selectedEquipElement);
		String voltageLevel = editor.getMetaDataManager().getVoltageLevel(selectedEquipElement);
		final StringBuffer baseUrl = new StringBuffer().append(
				(String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append(
				"?action=equipManualMapSearch");
		final String[][] param = new String[3][2];
		param[0][0] = "equipType";
		param[0][1] = equipType;
		param[1][0] = "subName";
		param[1][1] = substaionName;
		param[2][0] = "voltage";
		param[2][1] = voltageLevel;
		// 参数校验
		if (!checkRelateEquipParams(param))
			return;
		// 获取所有符合条件的设备的集合，形式是一个xml，格式为：
		/**
		 * <equipments> <equipment equipName="" equipID=""/> ... </equipments>
		 */
		SwingWorker<Document, String> worker = new SwingWorker<Document, String>() {

			@Override
			protected Document doInBackground() throws Exception {
				String equipListXML = (String) Utilities.communicateWithURL(
						baseUrl.toString(), param);
				Document listDoc = Utilities
						.getXMLDocumentByString(equipListXML);
				return listDoc;
			}

			@Override
			public void done() {
				try {
					showList(get(), selectedEquipElement);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}

		};
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(
				new LongtermTask(ResourcesManager.bundle
						.getString("nci_svg_getting_equipList"), worker));
	}

	/**
	 * 检查svg中及servlet端过来的参数
	 * 
	 * @param param
	 * @return
	 */
	private boolean checkRelateEquipParams(String[][] param) {
		String infomationTitle = ResourcesManager.bundle
				.getString("nci_optionpane_infomation_title");
		if (param[0][1] == null || param[0][1].equals("")) {
			JOptionPane
					.showConfirmDialog(
							editor.findParentFrame(),
							ResourcesManager.bundle
									.getString("nci_svg_equipRelate_cannot_locate_type"),
							infomationTitle, JOptionPane.CLOSED_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (param[1][1] == null || param[1][1].equals("")) {
			JOptionPane
					.showConfirmDialog(
							editor.findParentFrame(),
							ResourcesManager.bundle
									.getString("nci_svg_equipRelate_cannot_locate_substation"),
							infomationTitle, JOptionPane.CLOSED_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (param[2][1] == null || param[2][1].equals("")) {
			JOptionPane
					.showConfirmDialog(
							editor.findParentFrame(),
							ResourcesManager.bundle
									.getString("nci_svg_equipRelate_cannot_locate_voltage"),
							infomationTitle, JOptionPane.CLOSED_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * 显示可能被关联的设备
	 * 
	 * @param equipListXML
	 * @param selectedEquipElement
	 */
	private void showList(Document listDoc, Element selectedEquipElement) {
		// Document listDoc = Utilities.getXMLDocumentByString(equipListXML);
		EquipRelation relaDialog = new EquipRelation(editor, true,
				selectedEquipElement);
		relaDialog.setLocationRelativeTo(editor.findParentFrame());
		relaDialog.addElements(listDoc);
		relaDialog.setVisible(true);
	}
	
	private void showMetadataProperties(Element selectedEquipElement) {
	    objrefPanel.setElement(selectedEquipElement);
	    objrefPanel.setVisible(true);
	    objrefDialog.setVisible(true);
	}

	/**
	 * 显示属性对话框
	 */
	private void showProperties(Element selectedEquipElement) {

		// ADD BY ZHOUHM
		// 测试用
		String appcode = editor.getMetaDataManager().getPSMSID(selectedEquipElement);

		// String appcode = "5020-B-5001";
		String operation = editor.getMetaDataManager().getCIMType(selectedEquipElement); // "showSubstation"
		String url = null;
		// 获取设备信息查询url
		StringBuffer baseURL = new StringBuffer();
		baseURL.append((String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append("?action=")
				.append(ServletActionConstants.SEARCH_EQUIPMENT_INFORMATION);
		String[][] param = new String[2][2];
		param[0][0] = "equiptype";
		param[0][1] = operation;
		param[1][0] = "appcode";
		param[1][1] = appcode;
		try {
			url = (String) Utilities.communicateWithURL(baseURL.toString(),
					param);
			property.setURL(url);
			if (property.getDialog() == null) {
				property.getToolBarButton().doClick();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public JWebBrowser getBrowser() {
		return this.property.getBrowser();
	}

	public EquipPropery getPropertyDialog() {
		return property;
	}

	public String getName() {

		return equipmentPropertyID;
	}
}
