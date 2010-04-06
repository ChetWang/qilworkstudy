/*
 * NCIEquipPropPanel.java
 *
 * Created on 2008��4��29��, ����10:23
 */
package com.nci.svg.ui.equip;

import com.nci.svg.bean.RemoteFileInfoBean;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.equip.NCIPropertyComboBean;
import com.nci.svg.module.EquipmentPropertyModule;
import com.nci.svg.util.Constants;
import com.nci.svg.util.EquipPool;
import com.nci.svg.util.ServletActionConstants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.actions.clipboard.ClipboardModule;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 
 * @author Qil.Wong
 */
public class NCIEquipPropertyManager extends javax.swing.JPanel {

	private String equipTypeValue = null;
	private HashMap<String, EquipTypeObj> propMap = new HashMap<String, EquipTypeObj>();
	private Document metadataDoc = null;
	/**
	 * �����豸���Գɹ�
	 */
	public static final int PROPERTY_SAVE_RESULT_SUCCESS = 0;
	/**
	 * �豸���Ա�������б�����豸�Ѿ�����
	 */
	public static final int PROPERTY_SAVE_RESULT_EXIST = 1;
	/**
	 * �豸���Ա��������servlet�˳��ִ���
	 */
	public static final int PROPERTY_SAVE_RESULT_SERVLET_ERR = -1;
	/**
	 * �豸���Ա�������У�servlet�˷���ֵ����ʾ���޸��豸��
	 */
	public static final int PROPERTY_SAVE_RESULT_NO_SUCH_EQUIP = -2;
	/**
	 * �豸���Ա�������У����ݸ�servlet�Ĳ���metadata��xml��ʽ������
	 */
	public static final int PROPERTY_SAVE_RESULT_ILLEGAL_XML = -3;
	/**
	 * �豸���Ա�������У�ҵ���߼����ʧ��
	 */
	public static final int PROPERTY_SAVE_LOGICAL_BUSINESS_CHECK_FAIL = 2;
	/**
	 * �豸���Ա��������ʽ��0��ʾ�½�
	 */
	public static final int PROPERTY_SAVE_NEW = 0;
	/**
	 * �豸���Ա��������ʽ��1��ʾ����
	 */
	public static final int PROPERTY_SAVE_UPDATE = 1;
	/**
	 * ����ʧ��ʱ����ʾ
	 */
	private ArrayList<String> logicalBusinessFailInfos = new ArrayList<String>();
	/**
	 * �豸image���ڵ�xml Element����
	 */
	private Element graphUnitElement = null;
	/**
	 * ����ͼ���Ǳ�׼ͼ,0��׼ͼ��1��ʾ����ͼ
	 */
	int private_standard = -1;
	Editor editor;

	public NCIEquipPropertyManager(Editor editor) {
		this.editor = editor;
	}

	/** Creates new form NCIEquipPropPanel */
	public NCIEquipPropertyManager(String equipType, Element graphUnitElement,
			Editor editor) {
		this.equipTypeValue = equipType;
		this.graphUnitElement = graphUnitElement;
		this.editor = editor;
		// initComponents();
		initByEquip();
	}

	/**
	 * �����豸���ͳ�ʼ������
	 */
	private void initByEquip() {
		this.setBorder(new TitledBorder(ResourcesManager.bundle
				.getString(EquipmentPropertyModule.equipmentPropertyID)));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Document propertyDoc = EquipPool.getEquipPropertiesDoc();
		// ���е��豸���ͼ���
		NodeList propertiesList = propertyDoc
				.getElementsByTagName("Properties");
		Element equipEle = null;
		for (int i = 0; i < propertiesList.getLength(); i++) {
			Node propertyNode = propertiesList.item(i);
			if (propertyNode instanceof Element) {
				if (((Element) propertyNode).getAttribute("value").equals(
						equipTypeValue)) {
					equipEle = (Element) propertyNode;
					break;
				}
			}
		}
		if (equipEle == null) {
			this.add(new JLabel(ResourcesManager.bundle
					.getString("nci_no_propery_define_in_equip")));
			return;
		} else {
			// �ҵ����豸���͵ľ������Լ���,������װ��panel
			NodeList properyType = equipEle.getChildNodes();
			for (int i = 0; i < properyType.getLength(); i++) {
				Node node = properyType.item(i);
				if (node instanceof Element) {
					parseTypeNodeToPanel((Element) node);
				}
			}
		}
	}

	/**
	 * ��ÿ�����Խڵ�ת��Ϊһ��panel
	 * 
	 * @param ele
	 */
	private void parseTypeNodeToPanel(Element ele) {
		JComponent comp = null;
		String name = ele.getAttribute("name");
		String autoCalculateText = ele.getAttribute("autoCalculate");
		String compText = ele.getAttribute("component");
		if (compText.equals("text")) {
			comp = new JTextField();
		} else {
			if (compText.equals("combo")) {
				comp = new JComboBox();
				String componentName = ele.getAttribute("componentName");
				iniCombo((JComboBox) comp, componentName);
			}
		}
		EquipTypeObj typePanel = new EquipTypeObj(name, comp, autoCalculateText
				.equals("true"), this);
		this.add(typePanel);
		propMap.put(name, typePanel);
	}

	/**
	 * ���������úõ������б�̬����Combobox
	 * 
	 * @param combo
	 * @param componentName
	 */
	private void iniCombo(JComboBox combo, String componentName) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement("");
		if (componentName
				.equals(Constants.NCI_FROM_TO_SUBSTATION_COMPONENT_NAME)) {
		} else {
			HashMap<String, NCIPropertyComboBean> values = EquipPool
					.getComboMap().get(componentName);

			Iterator<NCIPropertyComboBean> it = values.values().iterator();
			ArrayList<NCIPropertyComboBean> comboBeanList = new ArrayList<NCIPropertyComboBean>();

			while (it.hasNext()) {
				comboBeanList.add(it.next());
				// model.addElement(it.next());
			}
			Collections.sort(comboBeanList,
					new Comparator<NCIPropertyComboBean>() {

						public int compare(NCIPropertyComboBean o1,
								NCIPropertyComboBean o2) {
							return o1.getComboBeanID().compareTo(
									o2.getComboBeanID());
						}
					});
			for (NCIPropertyComboBean bean : comboBeanList) {
				model.addElement(bean.getComboBeanValue());
			}

		}
		combo.setModel(model);
	}

	/**
	 * �����豸����
	 */
	public void handleProperties() {
		JButton okBtn = new JButton(ResourcesManager.bundle
				.getString("nci_equip_prop_saveBtn_ok"));
		JButton cancelBtn = new JButton(ResourcesManager.bundle
				.getString("nci_equip_prop_saveBtn_cancel"));
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());
		root.add(this, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanel.setLayout(buttonPanelLayout);
		GridBagConstraints gbc_ok = new GridBagConstraints();
		gbc_ok.gridx = 0;
		gbc_ok.ipadx = 10;
		gbc_ok.gridy = 0;
		gbc_ok.anchor = GridBagConstraints.WEST;
		gbc_ok.insets = new Insets(5, 0, 5, 10);
		gbc_ok.fill = GridBagConstraints.HORIZONTAL;
		buttonPanelLayout.setConstraints(okBtn, gbc_ok);
		buttonPanel.add(okBtn);
		GridBagConstraints gbc_cancel = new GridBagConstraints();
		gbc_cancel.gridx = 2;
		gbc_cancel.gridy = 0;
		gbc_cancel.ipadx = 10;
		gbc_cancel.anchor = GridBagConstraints.WEST;
		gbc_cancel.fill = GridBagConstraints.HORIZONTAL;
		buttonPanelLayout.setConstraints(cancelBtn, gbc_cancel);
		buttonPanel.add(cancelBtn);
		root.add(buttonPanel, BorderLayout.SOUTH);
		final JDialog dialog = new JDialog(editor.findParentFrame());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.getContentPane().add(root);
		dialog.pack();
		dialog.setLocationRelativeTo(editor.getHandlesManager()
				.getCurrentHandle().getSVGFrame());
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setTitle(ResourcesManager.bundle
				.getString("nci_equip_prop_save"));
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				deleteCurrent();
			}
		});
		okBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					if (propMap.size() > 0) {
						if (saveToSvg())
							dialog.dispose();
						// handleResult(saveProperties(PROPERTY_SAVE_NEW),
						// dialog);
					} else {
						dialog.dispose();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent evt) {
				// deleteCurrent();
			}
		});
		dialog.setVisible(true);

	}

	/**
	 * ��ȡ���������رնԻ����ɾ����֮ǰ�Ͻ�ȥ��ͼԪ
	 */
	private void deleteCurrent() {
		ClipboardModule clipModule = (ClipboardModule) editor
				.getModule("Clipboard");
		clipModule.delete(true);
	}

	// /**
	// * ��������
	// *
	// * @param result
	// * @throws XPathExpressionException
	// * @throws ParserConfigurationException
	// * @throws ClassNotFoundException
	// * @throws IOException
	// */
	// private void handleResult(int result, JDialog propDialog)
	// throws XPathExpressionException, ParserConfigurationException,
	// IOException, ClassNotFoundException {
	// switch (result) {
	// case PROPERTY_SAVE_RESULT_SUCCESS:
	// JOptionPane.showConfirmDialog(this, ResourcesManager.bundle
	// .getString("nci_equip_prop_save_succ_info"),
	// ResourcesManager.bundle
	// .getString("nci_equip_prop_save_info"),
	// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	// // ���������ݱ�����svg��
	// saveToSvg();
	// propDialog.dispose();
	// break;
	// case PROPERTY_SAVE_RESULT_EXIST:
	// int n = JOptionPane.showConfirmDialog(this, ResourcesManager.bundle
	// .getString("nci_equip_prop_save_exist_info"),
	// ResourcesManager.bundle
	// .getString("nci_equip_prop_save_info"),
	// JOptionPane.OK_CANCEL_OPTION,
	// JOptionPane.INFORMATION_MESSAGE);
	// if (n == JOptionPane.OK_OPTION) {
	// handleResult(saveProperties(PROPERTY_SAVE_UPDATE), propDialog);
	// }
	// break;
	// case PROPERTY_SAVE_RESULT_ILLEGAL_XML:
	// JOptionPane.showConfirmDialog(this, ResourcesManager.bundle
	// .getString("nci_equip_prop_save_illegalXML_info"),
	// ResourcesManager.bundle
	// .getString("nci_equip_prop_save_info"),
	// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	// break;
	//
	// case PROPERTY_SAVE_RESULT_NO_SUCH_EQUIP:
	// JOptionPane.showConfirmDialog(this, ResourcesManager.bundle
	// .getString("nci_equip_prop_save_no_equip_info"),
	// ResourcesManager.bundle
	// .getString("nci_equip_prop_save_info"),
	// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	// break;
	// case PROPERTY_SAVE_RESULT_SERVLET_ERR:
	// JOptionPane.showConfirmDialog(this, ResourcesManager.bundle
	// .getString("nci_equip_prop_save_servlet_err_info"),
	// ResourcesManager.bundle
	// .getString("nci_equip_prop_save_info"),
	// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	// break;
	// case PROPERTY_SAVE_LOGICAL_BUSINESS_CHECK_FAIL:
	// JOptionPane.showConfirmDialog(this, getLogicalBusinessFialString(),
	// ResourcesManager.bundle
	// .getString("nci_equip_prop_save_info"),
	// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	// logicalBusinessFailInfos.clear();
	// break;
	// }
	// }
	/**
	 * ����servlet���ص���Ϣ�������������һ�α���ͼ�������½��������豸ʱ
	 * 
	 * @param result
	 * @param propDialog
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private boolean checkSaveToServerSucc(SVGHandle handle, boolean fileExist,
			String retString) throws XPathExpressionException,
			ParserConfigurationException, IOException, ClassNotFoundException {
		String[] parsedRet = parseRetCode(retString);// parsedRet�ĵ�һ��Ԫ����equipID���ڶ���Ԫ���ǽ����
		// parsedRet = new String[]{"5010-B-0001","1"};
		int result = Integer.parseInt(parsedRet[1]);
		boolean resultFlag = false;// ����ɹ����ı��λ

		switch (result) {
		case PROPERTY_SAVE_RESULT_SUCCESS:// ����ɹ�

			if (handle != null) {
				JOptionPane.showConfirmDialog(editor.getParent(),
						"�Ѿ��ϴ���������.",
//						ResourcesManager.bundle
//								.getString("nci_equip_prop_save_info"),
								"����ɹ���",
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			}
			resultFlag = true;
			break;
		case PROPERTY_SAVE_RESULT_EXIST:// �豸�Ѵ���
			if (handle != null) {
				int n = JOptionPane.showConfirmDialog(editor.getParent(), "\""
						+ parsedRet[0]
						+ "\""
						+ "�Ѿ����ڣ�ȷ���ϴ���",
						"����",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (n == JOptionPane.OK_OPTION) {
					String xpathExpr = "//property[@value='" + parsedRet[0]
							+ "']";
					Element image = (Element) Utilities.findNode(
							xpathExpr,
							handle.getCanvas().getDocument()
									.getDocumentElement()).getParentNode()
							.getParentNode();
					// �����豸��ǳ�update��ǿ�Ƹ���
					image.setAttribute("statusFlag",
							Constants.NCI_SVG_Status_Update);
					this.saveToServer(handle, fileExist, false, false);
				}
			}
			break;
		case PROPERTY_SAVE_RESULT_ILLEGAL_XML:
			if (handle != null) {
				JOptionPane
						.showConfirmDialog(
								editor.getParent(),
								"�ϴ��������д��ڷǷ���xml��ʽ!",
								"ʧ��",
								JOptionPane.CLOSED_OPTION,
								JOptionPane.ERROR_MESSAGE);
			}
			break;

		case PROPERTY_SAVE_RESULT_NO_SUCH_EQUIP:
			if (handle != null) {
				JOptionPane
						.showConfirmDialog(
								editor.getParent(),
								"\""
										+ parsedRet[0]
										+ "\""
										+ ResourcesManager.bundle
												.getString("nci_equip_prop_save_no_equip_info"),
								ResourcesManager.bundle
										.getString("nci_equip_prop_save_info"),
								JOptionPane.CLOSED_OPTION,
								JOptionPane.INFORMATION_MESSAGE);

			}
			break;
		case PROPERTY_SAVE_RESULT_SERVLET_ERR:
			if (handle != null) {
				JOptionPane
						.showConfirmDialog(
								editor.getParent(),
								"�������������!",
								"����",
								JOptionPane.CLOSED_OPTION,
								JOptionPane.ERROR_MESSAGE);

			}
			break;
		case PROPERTY_SAVE_LOGICAL_BUSINESS_CHECK_FAIL:
			if (handle != null) {
				JOptionPane.showConfirmDialog(editor.getParent(),
						getLogicalBusinessFialString(), ResourcesManager.bundle
								.getString("nci_equip_prop_save_info"),
						JOptionPane.CLOSED_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				logicalBusinessFailInfos.clear();
			}
			break;
		}
		return resultFlag;
	}

	/**
	 * ת��servlet���صı�����.
	 * 
	 * @param retString
	 *            ��������£�retString�ĸ�ʽ����: <retCode> <equipID></equipID>(��ʾ��������豸��ţ��ɹ�ʱ��Ԫ����ֵ)
	 *            <cause></cause>(�ɹ�Ϊ0��ʧ��ʱ�Ĳ����μӸ�������PROPERTY_SAVE_RESULT_...)
	 *            </retCode>
	 * @return ������ϣ���һ��Ԫ����equipID���ڶ���Ԫ���ǽ������
	 */
	private String[] parseRetCode(String retString) {
		String[] psrsedRet = new String[2];
		if (retString != null && !retString.equals("")) {
			Document doc = Utilities.getXMLDocumentByString(retString);
			// <retCode><equipID></equipID><cause>-1</cause></retCode>
			Node equip = doc.getElementsByTagName("equipID").item(0);
			psrsedRet[0] = equip.getTextContent();
			Node cause = doc.getElementsByTagName("cause").item(0);
			psrsedRet[1] = cause.getTextContent();
		}
		return psrsedRet;
	}

	/**
	 * ��������豸������Ϣ���浽��ǰ������SVGDocument����
	 * 
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 */
	private boolean saveToSvg() throws XPathExpressionException,
			ParserConfigurationException {
		this.metadataDoc = createMetaDataDoc();
		if (!checkMetadata(metadataDoc)) {
			JOptionPane.showConfirmDialog(this, getLogicalBusinessFialString(),
					ResourcesManager.bundle
							.getString("nci_equip_prop_save_info"),
					JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			logicalBusinessFailInfos.clear();
			return false;
		}
		Element rootMetadata = this.metadataDoc.getDocumentElement();
		Document svgDoc = editor.getHandlesManager().getCurrentHandle()
				.getCanvas().getDocument();
		rootMetadata = (Element) svgDoc.importNode(rootMetadata, true);
		this.graphUnitElement.appendChild(rootMetadata);
		return true;
	}

	public boolean deleteRemotePersonalFile(RemoteFileInfoBean bean) {
		try {
			StringBuffer baseURL = new StringBuffer((String) editor
					.getGCParam("appRoot")).append(
					(String) editor.getGCParam("servletPath")).append(
					"?action=").append(ServletActionConstants.SAVE_REMOTE_SVG); // ��Ҫ��֣���SAVE_REMOTE_SVG
			String[][] params = new String[7][2];
			params[0][0] = "filename";
			params[0][1] = bean.getStrFileDesc();// ����ȡ��������Ȣbean��filename
			params[1][0] = "content";
			params[1][1] = "";// �ձ�ʾҪɾ���ļ�
			params[2][0] = "nci_class"; // ��ѹ�ȼ�
			params[2][1] = bean.getStrVoltage();
			params[3][0] = "nci_name"; // ����
			params[3][1] = bean.getStrFileDesc();
			params[4][0] = "nci_type"; // ͼ����
			params[4][1] = bean.getStrFileType();
			// params[2][0] = "nci_class";// ��ѹ�ȼ�
			// params[2][1] = strVoltage;
			params[5][0] = "user";
			params[5][1] = editor.getSvgSession().getUser();
			params[6][0] = "private_standard";
			params[6][1] = "1"; // String.valueOf(private_standard);
			String ret = (String) Utilities.communicateWithURL(baseURL
					.toString(), params);
			return this.checkSaveToServerSucc(null, true, ret);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * ��svg��svg�е��豸���������ݿ�
	 * 
	 * @param handle
	 *            null ��ʾ��ɾͼ������null���Ǳ��浱ǰhandle��ͼ
	 * @param fileExist
	 *            handle�ж�Ӧ��svg�ļ��Ƿ����
	 * @param inputName
	 *            �Ƿ���Ҫ����Զ�����ƣ�true��Ҫ��false����Ҫ
	 * @param equipFlag
	 *            �豸����״̬��ǩ��0��ʾnew��1��ʾupdate
	 */
	public void saveToServer(SVGHandle handle, boolean fileExist,
			boolean inputName, boolean delete) {
		String strVoltage = "";
		// ��ȡ����
		if (inputName) {
			SvgInput svgInput = new SvgInput();
			String remoteName = handle.getRemoteName();
			if (fileExist) {
				// �Ѿ���Զ������ʹ��Զ������û�о�Ĭ�������ļ�����ΪԶ����
				if (handle.getRemoteName() == null
						|| handle.getRemoteName().equals("")) {
					remoteName = handle.getName().substring(
							handle.getName().lastIndexOf("/") + 1,
							handle.getName().length()
									- Constants.NCI_SVG_EXTENDSION.length());
				}
			}
			svgInput.nameField.setText(remoteName);
			int result = JOptionPane.showConfirmDialog(
					editor.findParentFrame(), svgInput, ResourcesManager.bundle
							.getString("nciSaveToServerFileNameInfo"),
					JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			// ������̲�����ȡ��
			if (result != JOptionPane.OK_OPTION) {
				saveToServer(handle, fileExist, true, delete);
			}
			strVoltage = (String) svgInput.private_standardTypeCombo
					.getSelectedItem();
			remoteName = svgInput.nameField.getText();
			// ������ƺ���.svg�������
			if (remoteName.indexOf(Constants.NCI_SVG_EXTENDSION) >= 0)
				remoteName = remoteName.substring(0, remoteName
						.indexOf(Constants.NCI_SVG_EXTENDSION));
			handle.setRemoteName(remoteName);
		}
		// ����document string
		StringBuffer docString = new StringBuffer();
		if (!delete) {
			XMLPrinter.writeNode(handle.getCanvas().getDocument()
					.getDocumentElement(), docString, 0, false, null);
		}
		// ���͸�server
		StringBuffer baseURL = new StringBuffer((String) editor
				.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append("?action=")
				.append(ServletActionConstants.SAVE_REMOTE_SVG);
		String[][] params = new String[7][2];
		params[0][0] = "filename";
		params[0][1] = handle.getRemoteName();
		params[1][0] = "content";
		params[1][1] = docString.toString();
		try {
			Element descElement = (Element) handle.getCanvas().getDocument()
					.getElementsByTagName("desc").item(0);
			if (descElement != null) {
				params[2][0] = "nci_class";// ��ѹ�ȼ�
				params[2][1] = descElement.getAttribute(params[2][0]);
				params[3][0] = "nci_name";// ����
				params[3][1] = descElement.getAttribute(params[3][0]);
				params[4][0] = "nci_type";// ͼ����
				params[4][1] = descElement.getAttribute(params[4][0]);
			}
		} catch (Exception e) {
		}
		params[2][0] = "nci_class";// ��ѹ�ȼ�
		params[2][1] = strVoltage;
		params[5][0] = "user";
		params[5][1] = editor.getSvgSession().getUser();
		params[6][0] = "private_standard";
		params[6][1] = "1";// String.valueOf(private_standard);
		boolean savedFlag = false;
		try {
			String ret = (String) Utilities.communicateWithURL(baseURL
					.toString(), params);
			savedFlag = this.checkSaveToServerSucc(handle, fileExist, ret);
			// �������ɹ�����image��ǩ�е�statusFlagֵ��Ϊold��Ϊ�˱�ʾ����豸�Ѿ��½����ˣ�����ʹ��nci��ͼԪ�����豸ʱ����
			if (savedFlag) {
				NodeList images = handle.getCanvas().getDocument()
						.getElementsByTagName("image");
				for (int i = 0; i < images.getLength(); i++) {
					Node node = images.item(i);
					if (node instanceof Element) {
						Element image = (Element) node;
						String nciType = image
								.getAttribute(Constants.NCI_SVG_Type_Attr);
						if (nciType != null
								&& nciType
										.endsWith(Constants.NCI_SVG_Type_GraphUnit_Value)) {
							image.setAttribute(Constants.NCI_SVG_Status,
									Constants.NCI_SVG_Status_Previous);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(editor.findParentFrame(),
					"��������з����쳣������ʧ�ܣ�", "��ʾ", JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * ��ȡʧ����Ϣ����ʾ
	 * 
	 * @return
	 */
	private String getLogicalBusinessFialString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < logicalBusinessFailInfos.size() - 1; i++) {
			sb.append(logicalBusinessFailInfos.get(i)).append(",");
		}
		sb.append(logicalBusinessFailInfos
				.get(logicalBusinessFailInfos.size() - 1));
		return "\""
				+ sb.toString()
				+ "\""
				+ ResourcesManager.bundle
						.getString("nci_equip_property_cannot_be_null");

	}

	/**
	 * �����豸������servlet
	 * 
	 * @param saveType
	 * @return
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private int saveProperties(int saveType) throws XPathExpressionException,
			ParserConfigurationException, IOException, ClassNotFoundException {

		// �ҳ������豸�������ƶ�Ӧ��Ӣ������
		String equipmentTypeCode = Utilities.findOneAttributeValue("value",
				"/EquipProperties/Properties[@equipType='" + equipTypeValue
						+ "']", EquipPool.getEquipPropertiesDoc());
		StringBuffer xmlData = new StringBuffer();
		metadataDoc = createMetaDataDoc();
		if (!checkMetadata(metadataDoc)) {
			return PROPERTY_SAVE_LOGICAL_BUSINESS_CHECK_FAIL;
		}
		// ��xml document����д��һ��StringBuffer����
		XMLPrinter.writeNode(metadataDoc.getDocumentElement(), xmlData, 0,
				false, null);
		// System.out.println(xmlData.toString());
		StringBuffer baseURL = new StringBuffer();
		baseURL.append((String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append("?action=")
				.append(ServletActionConstants.EQUIPMENT_OPERATION);
		String[][] param = new String[3][2];
		param[0][0] = "equipmentType";
		param[0][1] = equipmentTypeCode;
		param[1][0] = "operType";
		param[1][1] = String.valueOf(saveType);
		param[2][0] = "metaData";
		param[2][1] = xmlData.toString();
		// StringBuffer saveURL = Utilities.parseURL(baseURL.toString(), param);
		// // System.out.println(saveURL);
		// URL saveActionURl = new URL(saveURL.toString());
		// ObjectInputStream ois = new ObjectInputStream(saveActionURl
		// .openStream());
		// return Integer.parseInt(ois.readObject().toString());
		return Integer.parseInt(Utilities.communicateWithURL(
				baseURL.toString(), param).toString());
	}

	/**
	 * ����豸����Ԫ����
	 * 
	 * @param metadataDoc
	 * @return
	 */
	private boolean checkMetadata(Document metadataDoc) {
		boolean flag = true;
		Element root = metadataDoc.getDocumentElement();
		NodeList params = root.getChildNodes();
		for (int i = 0; i < params.getLength(); i++) {
			Node param = params.item(i);
			if (param instanceof Element) {
				// ���name�Ƕ�Ӧmetadata xml�е�name����nciEquipProperties.xml����code
				String nameid = ((Element) param).getAttribute("name");
				String value = ((Element) param).getAttribute("value");
				if (value.equals("")) {
					try {
						// ��ȡ��ǰ�豸������codeΪnameid��Ԫ�ص�name����
						String name = Utilities
								.findOneAttributeValue("name",
										"/EquipProperties/Properties[@value='"
												+ equipTypeValue
												+ "']/Property[@code='"
												+ nameid + "']", EquipPool
												.getEquipPropertiesDoc());
						flag = false;
						logicalBusinessFailInfos.add(name);

					} catch (XPathExpressionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}

	/**
	 * ����metadata xml��Document����
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	private Document createMetaDataDoc() throws ParserConfigurationException,
			XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = factory.newDocumentBuilder().newDocument();
		Element root = doc.createElementNS(null, "equipmentPropties");
		doc.appendChild(root);
		Iterator<String> it = propMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			// �ҳ��豸���ʹ��룬��Ϊÿ���豸���ж���ͬ���Ե��ֶ������ܻ��в�ͬ�����Ҫ�ҳ�ӳ��xml�ж�Ӧ�Ĵ���
			String nameCode = Utilities.findOneAttributeValue("code",
					"/EquipProperties/Properties[@value='" + equipTypeValue
							+ "']/Property[@name='" + key + "']", EquipPool
							.getEquipPropertiesDoc());
			if (nameCode != null && !nameCode.equals("")) {
				Element param = doc.createElementNS(null, "property");
				param.setAttributeNS(null, "name", nameCode);
				param.setAttributeNS(null, "value", propMap.get(key)
						.getCompObjMappedValue());
				root.appendChild(param);
			}
		}
		return doc;
	}

	/**
	 * ��ȡ��ǰ�豸������panel�б�
	 * 
	 * @return
	 */
	public HashMap<String, EquipTypeObj> getPropPanelMap() {
		return propMap;
	}

	/**
	 * ��ȡ�豸����
	 * 
	 * @return
	 */
	public String getEquipType() {
		return equipTypeValue;
	}

	public Element getGraphUnitElement() {
		return graphUnitElement;
	}

	private class SvgInput extends JPanel {

		private static final long serialVersionUID = 6919610021603961676L;
		private JLabel nameLabel = null;
		private JTextField nameField = null;
		private JLabel private_standardTypeLabel = null;
		private JComboBox private_standardTypeCombo = null;

		// private JLabel private_standardSubTypeLabel = null;
		// private JComboBox private_standardSubTypeCombo = null;
		// private JLabel voltageLabel = null;
		// private JComboBox voltageCombo = null;
		public SvgInput() {
			build();
		}

		private void build() {
			nameLabel = new JLabel(ResourcesManager.bundle
					.getString("nciSaveToServerFileName"));
			private_standardTypeLabel = new JLabel(ResourcesManager.bundle
					.getString("nciSaveToServerVoltage"));
			nameField = new JTextField();
			private_standardTypeCombo = new JComboBox();
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			model.addElement("10kV");
			model.addElement("35kV");
			model.addElement("110kV");
			model.addElement("220kV");
			model.addElement("500kV");
			private_standardTypeCombo.setModel(model);
			Dimension labelDimension = new Dimension(50, 23);
			Dimension compDimension = new Dimension(150, 23);
			nameLabel.setPreferredSize(labelDimension);
			private_standardTypeLabel.setPreferredSize(labelDimension);
			nameField.setPreferredSize(compDimension);
			private_standardTypeCombo.setPreferredSize(compDimension);
			FlowLayout flowLayout = new FlowLayout();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JPanel namePanel = new JPanel();
			JPanel private_standardPanel = new JPanel();
			// JPanel private_standardSubTypePanel = new JPanel();
			// JPanel voltagePanel = new JPanel();
			namePanel.setLayout(flowLayout);
			private_standardPanel.setLayout(flowLayout);
			// private_standardSubTypePanel.setLayout(flowLayout);
			// voltagePanel.setLayout(flowLayout);
			namePanel.add(nameLabel);
			namePanel.add(nameField);
			private_standardPanel.add(private_standardTypeLabel);
			private_standardPanel.add(private_standardTypeCombo);
			this.add(namePanel);
			this.add(private_standardPanel);
			// this.add(private_standardSubTypePanel);
			// this.add(voltagePanel);
		}
	}
}
