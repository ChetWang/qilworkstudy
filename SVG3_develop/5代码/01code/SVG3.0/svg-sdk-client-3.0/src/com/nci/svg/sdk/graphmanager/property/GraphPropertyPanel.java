package com.nci.svg.sdk.graphmanager.property;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JComboBox;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphmanager.property.editor.ColorChoosePropertyEditor;
import com.nci.svg.sdk.graphmanager.property.editor.EditorAndSliderPropertyEditor;
import com.nci.svg.sdk.graphmanager.property.editor.NciComboBoxPropertyEditor;
import com.nci.svg.sdk.graphmanager.property.editor.NumberSpinnerPropertyEditor;
import com.nci.svg.sdk.graphmanager.property.editor.TwoRadioButtonsPropertyEditor;
import com.nci.svg.sdk.ui.EditorPanel;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.properties.SVGComboResourceItem;
import fr.itris.glips.svgeditor.properties.SVGProperties;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * ͼ��������壬�ɰ���ͼ���ڲ��������Ժ�ҵ������
 * 
 * @author Qil.Wong
 * 
 */
public class GraphPropertyPanel extends EditorPanel {

	private static final long serialVersionUID = -3973481245399014491L;

	public static final String RENDERCHOOSER = "renderchooser";
	public static final String COMBO = "combo";
	public static final String EDITABLECOMBO = "editablecombo";
	public static final String MARKERCHOOSER = "markerchooser";
	public static final String TWORADIOBUTTONS = "tworadiobuttons";
	public static final String ENTRY = "entry";
	public static final String SINGLEENTRY = "singleentry";
	public static final String VALIDATEDENTRY = "validatedentry";
	public static final String FONTCHOOSER = "fontchooser";
	public static final String FONTSIZECHOOSER = "fontsizechooser";
	public static final String POSITIVENUMBERCHOOSER = "positivenumberchooser";
	public static final String NUMBERCHOOSER = "numberchooser";
	public static final String PRESERVEASPECTRATIOCHOOSER = "preserveaspectratiochooser";
	public static final String SLIDER = "slider";
	/**
	 * add by yux,2009-2-4 ���Ա仯����
	 */
	private PropertyChangeListener listener = null;
	/**
	 * add by yux,2009-2-4 ��ǰѡ�еĽڵ�
	 */
	private Set<Element> curElements = new HashSet<Element>();
	/**
	 * add by yux,2009-2-4 ��ǰ��ʾ�������б�
	 */
	private ArrayList<NciGraphProperty> propertyList = new ArrayList<NciGraphProperty>();
	/**
	 * add by yux,2009-2-4 ��ǰ��ʾ����������
	 */
	private NciGraphProperty[] propertyArray = null;
	/**
	 * add by yux,2009-2-4 ��ǰ��ʾ��������༭���Ĺ���ע��
	 */
	private PropertyEditorRegistry registry = new PropertyEditorRegistry();
	/**
	 * add by yux,2009-2-4 ���Զ���
	 */
	private SVGProperties properties = null;
	/**
	 * add by yux,2009-2-4 �����ĵ�
	 */
	private Document propertiesDoc = null;

	/**
	 * ���������Ե����
	 */
	private PropertySheetPanel sheet = null;

	public GraphPropertyPanel(EditorAdapter editor) {
		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				init();
				updateUI();
			}
		});
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		setLayout(LookAndFeelTweaks.createVerticalPercentLayout());
		sheet = new PropertySheetPanel();
		sheet.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		sheet.setToolBarVisible(false);
		sheet.setSortingCategories(false);
		sheet.setSortingProperties(false);
		sheet.setRestoreToggleStates(true);
		sheet.setDescriptionVisible(false);

		add(sheet, "*");

		properties = (SVGProperties) (editor.getModule("Properties"));
		propertiesDoc = properties.getDocProperties();
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	public PropertySheetPanel getPropertySheet() {
		return sheet;
	}

	/**
	 * add by yux,2009-2-4 ���ýڵ�
	 * 
	 * @param element
	 *            ����ǰ���õĽڵ�
	 */
	public synchronized void setElement(final Set<Element> elements) {
		// // �ȶԵ�ǰ�ڵ��Ƿ�仯��û�仯������������
		// if (curElement == element)
		// return;
		// ����ϴε���������
		if (!editor.getModeManager().isPropertyPaneCreate()) {
			return;
		}
		curElements.clear();
		if (elements != null)
			curElements.addAll(elements);
		if (listener != null) {
			sheet.removePropertySheetChangeListener(listener);
			listener = null;
		}
		propertyList.clear();
		if (propertyArray != null && propertyArray.length > 0) {
			int len = propertyArray.length;
			for (int i = len; i > 0; i--) {
				// sheet.removeProperty(propertyArray[i - 1]);
				registry.unregisterEditor(propertyArray[i - 1]);
			}
			propertyArray = null;
			sheet.setVisible(false);

		}

		if (elements == null || elements.size() == 0)
			return;

		String nodeName = null;
		for (Element element : elements) {
			if (nodeName == null)
				nodeName = element.getNodeName();
			else {
				if (element.getNodeName().equals(nodeName) == false) {
					return;
				}
			}
		}
		// ѡ�з��ϵ�ǰ�ڵ����͵�ͼ�����Խڵ�
		String xpathExpr = "//module[@name='" + nodeName + "']";
		Node moduleNode = null;
		try {
			moduleNode = Utilities.findNode(xpathExpr, propertiesDoc
					.getDocumentElement());
		} catch (XPathExpressionException e) {
			moduleNode = null;
		}
		if (moduleNode == null)
			return;

		// �������Խڵ㣬����������Ϣ��propertyList��
		insertGraphProperties((Element) moduleNode);
		// ���ù�����Ϣ
		sheet.setEditorFactory(registry);

		propertyArray = new NciGraphProperty[propertyList.size()];
		sheet.setProperties(propertyList.toArray(propertyArray));
		sheet.setVisible(true);
		if (curElements.size() == 1)
			sheet.readFromObject(curElements.iterator().next());

		// ��������
		listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				NciGraphProperty prop = (NciGraphProperty) evt.getSource();
				prop.writeToObject(curElements);

			}
		};
		sheet.addPropertySheetChangeListener(listener);
		sheet.updateUI();
	}

	/**
	 * add by yux,2009-2-4 �������ͼ�����Խڵ㣬��һ���������
	 * 
	 * @param element
	 *            ��ͼ�����Ը��ڵ�
	 */
	private void insertGraphProperties(Element element) {
		NodeList list = element.getElementsByTagName("tab");
		int size = list.getLength();
		String propertyName = null, muliFlag = null;
		for (int i = 0; i < size; i++) {
			Element childElement = (Element) list.item(i);
			propertyName = childElement.getAttribute("name");
			if (curElements.size() > 1) {
				muliFlag = childElement.getAttribute("muli");
				if (muliFlag != null && muliFlag.equals("false"))
					continue;
			}
			NodeList propertiesList = childElement
					.getElementsByTagName("property");
			int length = propertiesList.getLength();
			for (int j = 0; j < length; j++) {
				Element propertyElement = (Element) propertiesList.item(j);
				insertGraphProperty(propertyName, propertyElement);
			}
		}
	}

	/**
	 * add by yux,2009-2-4 ���ݴ����ͼ�������������ƺ�ͼ�������ӽڵ㣬�������Լ��������
	 * 
	 * @param propertyName
	 *            ��ͼ��������������
	 * @param element
	 *            ��ͼ�������ӽڵ�
	 */
	private void insertGraphProperty(String propertyName, Element element) {
		// ��ȡ����
		if (curElements.size() > 1) {
			String muliFlag = element.getAttribute("muli");
			if (muliFlag != null && muliFlag.equals("false"))
				return;
		}
		String showText = ResourcesManager.bundle.getString(propertyName);
		String type = element.getAttribute("type");
		String name = element.getAttribute("name");
		String valuetype = element.getAttribute("valuetype");
		String defaultvalue = element.getAttribute("defaultvalue");
		String constraint = element.getAttribute("constraint");

		if (valuetype.equals(RENDERCHOOSER)) {
			// ���ƣ���Դ����ɫ
			insertRenderChooseProperty(name, showText, type, defaultvalue,
					constraint);
		} else if (valuetype.equals(COMBO)) {
			// ���ɱ༭������
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, false);
			readElementToCombo(comboBox, element);
		} else if (valuetype.equals(EDITABLECOMBO)) {
			// �ɱ༭������
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, true);
			readElementToCombo(comboBox, element);
		} else if (valuetype.equals(FONTCHOOSER)) {
			// ����ѡ��
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, false);
			readElementToCombo(comboBox, element);
		} else if (valuetype.equals(FONTSIZECHOOSER)) {
			// �����Сѡ��
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, false);
			String[] items = { "6", "7", "8", "9", "10", "11", "12", "13",
					"14", "15", "16", "18", "20", "22", "24", "26", "28", "32",
					"36", "40", "44", "48", "54", "60", "66", "72", "80", "88",
					"96" };
			SimpleCodeBean[] valueBean = new SimpleCodeBean[items.length];
			for (int i = 0; i < items.length; i++) {
				valueBean[i] = new SimpleCodeBean();
				valueBean[i].setCode(items[i]);
				valueBean[i].setName(items[i]);
			}
			comboBox.setAvailableValues(valueBean);
		} else if (valuetype.equals(TWORADIOBUTTONS)) {
			// ˫��ѡ�����
			insertTwoRadioButtonsProperty(showText, name, type, defaultvalue,
					constraint, element);
		} else if (valuetype.equals(SLIDER)) {
			// ����
			insertSliderProperty(showText, name, type, defaultvalue, constraint);
		} else if (valuetype.equals(POSITIVENUMBERCHOOSER)) {
			// �Ǹ�����ѡ���
			insertNumberSpinnerProperty(showText, name, type, defaultvalue,
					constraint, false);
		} else if (valuetype.equals(NUMBERCHOOSER)) {
			// ����ѡ���
			insertNumberSpinnerProperty(showText, name, type, defaultvalue,
					constraint, true);
		} else if (valuetype.equals(PRESERVEASPECTRATIOCHOOSER)) {
			// ͼ�����ѡ��
		} else if (valuetype.equals(ENTRY)) {
			// �����
			insertEntryProperty(showText, name, type, defaultvalue, constraint,
					ENTRY);
		} else if (valuetype.equals(SINGLEENTRY)) {
			// ���������
			insertEntryProperty(showText, name, type, defaultvalue, constraint,
					SINGLEENTRY);
		} else if (valuetype.equals(VALIDATEDENTRY)) {
			// У�����ݵ������
			insertEntryProperty(showText, name, type, defaultvalue, constraint,
					VALIDATEDENTRY);
		}

	}

	/**
	 * add by yux,2009-2-4 ����������������
	 * 
	 * @param showText
	 *            ����ʾ����������
	 * @param name
	 *            �����Ա�ʶ
	 * @param type
	 *            ����������
	 * @param defaultvalue
	 *            ��Ĭ��ֵ
	 * @param constraint
	 *            ��������
	 * @param entryFlag
	 *            �����������
	 */
	private void insertEntryProperty(String showText, String name, String type,
			String defaultvalue, String constraint, String entryFlag) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		NciGraphProperty property = new NciGraphProperty(editor, handle, name,
				ResourcesManager.bundle.getString(name), null, showText,
				String.class, type, defaultvalue, constraint);
		propertyList.add(property);
	}

	/**
	 * add by yux,2009-2-4 ��������Spinner����
	 * 
	 * @param showText
	 *            ����ʾ����������
	 * @param name
	 *            �����Ա�ʶ
	 * @param type
	 *            ����������
	 * @param defaultvalue
	 *            ��Ĭ��ֵ
	 * @param constraint
	 *            ��������
	 * @param bPositive
	 *            ���Ǹ������
	 */
	private void insertNumberSpinnerProperty(String showText, String name,
			String type, String defaultvalue, String constraint,
			boolean bPositive) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		NciGraphProperty property = new NciGraphProperty(editor, handle, name,
				ResourcesManager.bundle.getString(name), null, showText, type,
				defaultvalue, constraint);
		NumberSpinnerPropertyEditor numberSpinner = new NumberSpinnerPropertyEditor(
				bPositive, property, curElements);
		registry.registerEditor(property, numberSpinner);
		propertyList.add(property);
	}

	/**
	 * add by yux,2009-2-4 �����༭���ϻ�������
	 * 
	 * @param showText
	 *            ����ʾ����������
	 * @param name
	 *            �����Ա�ʶ
	 * @param type
	 *            ����������
	 * @param defaultvalue
	 *            ��Ĭ��ֵ
	 * @param constraint
	 *            ��������
	 */
	private void insertSliderProperty(String showText, String name,
			String type, String defaultvalue, String constraint) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		NciGraphProperty property = new NciGraphProperty(editor, handle, name,
				ResourcesManager.bundle.getString(name), null, showText, type,
				defaultvalue, constraint);
		EditorAndSliderPropertyEditor slider = new EditorAndSliderPropertyEditor(
				editor, sheet, property, curElements);
		registry.registerEditor(property, slider);
		propertyList.add(property);
	}

	/**
	 * add by yux,2009-2-4 ����˫��ѡ������
	 * 
	 * @param showText
	 *            ����ʾ����������
	 * @param name
	 *            �����Ա�ʶ
	 * @param type
	 *            ����������
	 * @param defaultvalue
	 *            ��Ĭ��ֵ
	 * @param constraint
	 *            ��������
	 * @param bPositive
	 *            ���Ǹ������
	 * @param element
	 *            :��ǰͼ�������ӽڵ㣬������ȡ��������
	 */
	private void insertTwoRadioButtonsProperty(String showText, String name,
			String type, String defaultvalue, String constraint, Element element) {
		NodeList itemList = element.getElementsByTagName("item");
		int size = itemList.getLength();
		if (size != 2)
			return;
		String names[] = new String[size];
		String values[] = new String[size];
		for (int i = 0; i < size; i++) {
			names[i] = ResourcesManager.bundle.getString(((Element) itemList
					.item(i)).getAttribute("name"));
			values[i] = ((Element) itemList.item(i)).getAttribute("value");
		}
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		NciGraphProperty property = new NciGraphProperty(editor, handle, name,
				ResourcesManager.bundle.getString(name), null, showText, type,
				defaultvalue, constraint);
		TwoRadioButtonsPropertyEditor twoRadioButtons = new TwoRadioButtonsPropertyEditor(
				sheet, property, curElements, names[0], values[0], names[1],
				values[1], null, null);

		registry.registerEditor(property, twoRadioButtons);
		propertyList.add(property);
	}

	/**
	 * add by yux,2009-2-4 ��������������
	 * 
	 * @param showText
	 *            ����ʾ����������
	 * @param name
	 *            �����Ա�ʶ
	 * @param type
	 *            ����������
	 * @param defaultvalue
	 *            ��Ĭ��ֵ
	 * @param constraint
	 *            ��������
	 * @param aflag
	 *            ���ɱ༭��ʶ��true��ʶ�ɱ༭
	 */
	private NciComboBoxPropertyEditor insertComboProperty(String showText,
			String name, String type, String defaultvalue, String constraint,
			boolean aflag) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();

		NciComboBoxPropertyEditor comboBox = new NciComboBoxPropertyEditor();
		NciGraphProperty property = new NciGraphProperty(editor, handle, name,
				ResourcesManager.bundle.getString(name), null, showText,
				comboBox, type, defaultvalue, constraint);
		registry.registerEditor(property, comboBox);
		propertyList.add(property);
		setComboBoxEditable(comboBox, aflag);
		return comboBox;
	}

	/**
	 * add by yux,2009-2-3 �����������
	 * 
	 * @param propertyName
	 *            ����������
	 * @param showText
	 *            ����ʾ����
	 * @param type
	 *            ������
	 * @param defaultvalue
	 *            ��Ĭ��ֵ
	 * @param constraint
	 *            ��������
	 */
	private void insertRenderChooseProperty(String propertyName,
			String showText, String type, String defaultvalue, String constraint) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		String test = propertyName.substring(propertyName.indexOf("_") + 1,
				propertyName.length());
		String value = "";
		if (curElements.size() == 1)
			value = editor.getSVGToolkit().getStyleProperty(
					curElements.iterator().next(), test);
		Color currentColor = editor.getColorChooser().getColor(handle, value);

		final String[] values = new String[2];
		int selectedIndex = 0;
		String defaultChoose = null;
		if (currentColor != null) {

			values[0] = value;
			values[1] = "none";
			selectedIndex = 0;
			defaultChoose = "color";
		} else {

			currentColor = Color.black;
			values[0] = "#000000";

			if ("none".equals(value)) {

				values[1] = "none";

			} else {

				values[1] = fr.itris.glips.library.Toolkit.toUnURLValue(value);
			}

			selectedIndex = 1;
			defaultChoose = "resouce";
		}

		// ��Դ����ɫѡ��
		NciGraphProperty propertyHZ = new NciGraphProperty(editor, handle,
				null, "����", null, showText, type, defaultChoose, constraint);
		// ��Դѡ��combo
		NciComboBoxPropertyEditor comboBox = new NciComboBoxPropertyEditor();
		NciGraphProperty propertyZY = new NciGraphProperty(editor, handle,
				propertyName, "��Դ", null, showText, comboBox, type, values[1],
				constraint);
		// ��ɫѡ��

		NciGraphProperty propertyYS = new NciGraphProperty(editor, handle,
				propertyName, "��ɫ", null, showText, type, values[0], constraint);
		ColorChoosePropertyEditor colorProperty = new ColorChoosePropertyEditor(
				editor, propertyYS, curElements, currentColor);
		propertyYS.setPropertyEditor(colorProperty);
		registry.registerEditor(propertyZY, comboBox);
		setComboBoxEditable(comboBox, false);
		LinkedList<String> resourceTagNames = new LinkedList<String>();

		resourceTagNames.add("linearGradient");
		resourceTagNames.add("radialGradient");
		resourceTagNames.add("pattern");
		// the list of the items that will be contained in the combo used to
		// choose the id of a resource
		LinkedList<String> items = new LinkedList<String>();

		// the map associating the id of a resource contained in the "defs"
		// element to the resource node
		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
		HashMap<String, Element> resources = handle.getSvgResourcesManager()
				.getResourcesFromDefs(doc, resourceTagNames);

		Element cur = null;
		SVGComboResourceItem item = null;

		items.add("��");

		// for each resource element contained in the map
		for (String id : new LinkedList<String>(resources.keySet())) {

			cur = resources.get(id);

			if (cur != null && !id.equals("")) {

				items.add(id);
			}
		}

		// creates the combo for the uri
		Object[] itemArray = items.toArray();
		comboBox.setAvailableValues(itemArray);

		registry.registerEditor(propertyYS, colorProperty);

		TwoRadioButtonsPropertyEditor propertyEditor3 = new TwoRadioButtonsPropertyEditor(
				sheet, null, null, "��Դ", "resource", "��ɫ", "color", propertyZY,
				propertyYS);
		registry.registerEditor(propertyHZ, propertyEditor3);
		propertyList.add(propertyHZ);
		propertyList.add(propertyZY);
		propertyList.add(propertyYS);
		if (selectedIndex == 0) {
			propertyZY.setEditable(false);
		} else {
			propertyYS.setEditable(false);
		}
	}

	/**
	 * add by yux,2009-2-3 ��������������Ϊ����д�򲻿���д
	 * 
	 * @param property
	 *            �����������Զ���
	 * @param aFlag
	 *            ����д��ǣ�true��ʾ�ɱ���д��false��ʾ���ɱ���д
	 */
	private void setComboBoxEditable(ComboBoxPropertyEditor property,
			boolean aFlag) {
		JComboBox comboBox = (JComboBox) property.getCustomEditor();
		comboBox.setEditable(aFlag);
	}

	/**
	 * add by yux,2009-2-4 ��ͼ�������ӽڵ��ж�ȡ�������������Ա༭����
	 * 
	 * @param property
	 *            ���������Ա༭��
	 * @param element
	 *            ��ͼ�������ӽڵ�
	 */
	private void readElementToCombo(ComboBoxPropertyEditor property,
			Element element) {
		NodeList itemList = element.getElementsByTagName("item");
		int size = itemList.getLength();
		if (size > 0) {
			SimpleCodeBean[] valueBean = new SimpleCodeBean[size];
			for (int i = 0; i < size; i++) {
				String itemName = ((Element) itemList.item(i))
						.getAttribute("name");
				String itemValue = ((Element) itemList.item(i))
						.getAttribute("value");
				valueBean[i] = new SimpleCodeBean();
				valueBean[i].setCode(itemValue);
				try {
					valueBean[i].setName(ResourcesManager.bundle
							.getString(itemName));
				} catch (Exception ex) {
					valueBean[i].setName(itemName);
				}
			}
			property.setAvailableValues(valueBean);
		}
	}

}
