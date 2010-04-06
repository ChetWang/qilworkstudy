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
 * 图形属性面板，可包括图形内部基本属性和业务属性
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
	 * add by yux,2009-2-4 属性变化监听
	 */
	private PropertyChangeListener listener = null;
	/**
	 * add by yux,2009-2-4 当前选中的节点
	 */
	private Set<Element> curElements = new HashSet<Element>();
	/**
	 * add by yux,2009-2-4 当前显示的属性列表
	 */
	private ArrayList<NciGraphProperty> propertyList = new ArrayList<NciGraphProperty>();
	/**
	 * add by yux,2009-2-4 当前显示的属性数组
	 */
	private NciGraphProperty[] propertyArray = null;
	/**
	 * add by yux,2009-2-4 当前显示的属性与编辑器的管理注册
	 */
	private PropertyEditorRegistry registry = new PropertyEditorRegistry();
	/**
	 * add by yux,2009-2-4 属性对象
	 */
	private SVGProperties properties = null;
	/**
	 * add by yux,2009-2-4 属性文档
	 */
	private Document propertiesDoc = null;

	/**
	 * 真正放属性的面板
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
	 * 初始化
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
	 * 获取属性面板
	 * 
	 * @return
	 */
	public PropertySheetPanel getPropertySheet() {
		return sheet;
	}

	/**
	 * add by yux,2009-2-4 设置节点
	 * 
	 * @param element
	 *            ：当前设置的节点
	 */
	public synchronized void setElement(final Set<Element> elements) {
		// // 比对当前节点是否变化，没变化则无需变更属性
		// if (curElement == element)
		// return;
		// 清除上次的属性内容
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
		// 选中符合当前节点类型的图形属性节点
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

		// 根据属性节点，构建属性信息至propertyList中
		insertGraphProperties((Element) moduleNode);
		// 设置关联信息
		sheet.setEditorFactory(registry);

		propertyArray = new NciGraphProperty[propertyList.size()];
		sheet.setProperties(propertyList.toArray(propertyArray));
		sheet.setVisible(true);
		if (curElements.size() == 1)
			sheet.readFromObject(curElements.iterator().next());

		// 构建监听
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
	 * add by yux,2009-2-4 按传入的图形属性节点，逐一加入队列中
	 * 
	 * @param element
	 *            ：图形属性根节点
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
	 * add by yux,2009-2-4 根据传入的图形属性种类名称和图形属性子节点，生成属性加入队列中
	 * 
	 * @param propertyName
	 *            ：图形属性种类名称
	 * @param element
	 *            ：图形属性子节点
	 */
	private void insertGraphProperty(String propertyName, Element element) {
		// 读取数据
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
			// 绘制，资源和颜色
			insertRenderChooseProperty(name, showText, type, defaultvalue,
					constraint);
		} else if (valuetype.equals(COMBO)) {
			// 不可编辑下拉框
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, false);
			readElementToCombo(comboBox, element);
		} else if (valuetype.equals(EDITABLECOMBO)) {
			// 可编辑下拉框
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, true);
			readElementToCombo(comboBox, element);
		} else if (valuetype.equals(FONTCHOOSER)) {
			// 字体选择
			NciComboBoxPropertyEditor comboBox = insertComboProperty(showText,
					name, type, defaultvalue, constraint, false);
			readElementToCombo(comboBox, element);
		} else if (valuetype.equals(FONTSIZECHOOSER)) {
			// 字体大小选择
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
			// 双单选框组合
			insertTwoRadioButtonsProperty(showText, name, type, defaultvalue,
					constraint, element);
		} else if (valuetype.equals(SLIDER)) {
			// 滑块
			insertSliderProperty(showText, name, type, defaultvalue, constraint);
		} else if (valuetype.equals(POSITIVENUMBERCHOOSER)) {
			// 非负数字选择框
			insertNumberSpinnerProperty(showText, name, type, defaultvalue,
					constraint, false);
		} else if (valuetype.equals(NUMBERCHOOSER)) {
			// 数字选择框
			insertNumberSpinnerProperty(showText, name, type, defaultvalue,
					constraint, true);
		} else if (valuetype.equals(PRESERVEASPECTRATIOCHOOSER)) {
			// 图像组合选择
		} else if (valuetype.equals(ENTRY)) {
			// 输入框
			insertEntryProperty(showText, name, type, defaultvalue, constraint,
					ENTRY);
		} else if (valuetype.equals(SINGLEENTRY)) {
			// 单行输入框
			insertEntryProperty(showText, name, type, defaultvalue, constraint,
					SINGLEENTRY);
		} else if (valuetype.equals(VALIDATEDENTRY)) {
			// 校验数据的输入框
			insertEntryProperty(showText, name, type, defaultvalue, constraint,
					VALIDATEDENTRY);
		}

	}

	/**
	 * add by yux,2009-2-4 加入文字输入属性
	 * 
	 * @param showText
	 *            ：显示的种类名称
	 * @param name
	 *            ：属性标识
	 * @param type
	 *            ：属性类型
	 * @param defaultvalue
	 *            ：默认值
	 * @param constraint
	 *            ：必填标记
	 * @param entryFlag
	 *            ：输入框类型
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
	 * add by yux,2009-2-4 构建数字Spinner属性
	 * 
	 * @param showText
	 *            ：显示的种类名称
	 * @param name
	 *            ：属性标识
	 * @param type
	 *            ：属性类型
	 * @param defaultvalue
	 *            ：默认值
	 * @param constraint
	 *            ：必填标记
	 * @param bPositive
	 *            ：非负数标记
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
	 * add by yux,2009-2-4 构建编辑框混合滑块属性
	 * 
	 * @param showText
	 *            ：显示的种类名称
	 * @param name
	 *            ：属性标识
	 * @param type
	 *            ：属性类型
	 * @param defaultvalue
	 *            ：默认值
	 * @param constraint
	 *            ：必填标记
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
	 * add by yux,2009-2-4 构建双单选框属性
	 * 
	 * @param showText
	 *            ：显示的种类名称
	 * @param name
	 *            ：属性标识
	 * @param type
	 *            ：属性类型
	 * @param defaultvalue
	 *            ：默认值
	 * @param constraint
	 *            ：必填标记
	 * @param bPositive
	 *            ：非负数标记
	 * @param element
	 *            :当前图形属性子节点，用来获取属性描述
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
	 * add by yux,2009-2-4 构建下拉框属性
	 * 
	 * @param showText
	 *            ：显示的种类名称
	 * @param name
	 *            ：属性标识
	 * @param type
	 *            ：属性类型
	 * @param defaultvalue
	 *            ：默认值
	 * @param constraint
	 *            ：必填标记
	 * @param aflag
	 *            ：可编辑标识，true标识可编辑
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
	 * add by yux,2009-2-3 插入绘制属性
	 * 
	 * @param propertyName
	 *            ：属性名称
	 * @param showText
	 *            ：显示文字
	 * @param type
	 *            ：类型
	 * @param defaultvalue
	 *            ：默认值
	 * @param constraint
	 *            ：必填标记
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

		// 资源或颜色选择
		NciGraphProperty propertyHZ = new NciGraphProperty(editor, handle,
				null, "绘制", null, showText, type, defaultChoose, constraint);
		// 资源选择combo
		NciComboBoxPropertyEditor comboBox = new NciComboBoxPropertyEditor();
		NciGraphProperty propertyZY = new NciGraphProperty(editor, handle,
				propertyName, "资源", null, showText, comboBox, type, values[1],
				constraint);
		// 颜色选择

		NciGraphProperty propertyYS = new NciGraphProperty(editor, handle,
				propertyName, "颜色", null, showText, type, values[0], constraint);
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

		items.add("无");

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
				sheet, null, null, "资源", "resource", "颜色", "color", propertyZY,
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
	 * add by yux,2009-2-3 设置下拉框属性为可填写或不可填写
	 * 
	 * @param property
	 *            ：下拉框属性对象
	 * @param aFlag
	 *            ：填写标记，true表示可被填写，false表示不可被填写
	 */
	private void setComboBoxEditable(ComboBoxPropertyEditor property,
			boolean aFlag) {
		JComboBox comboBox = (JComboBox) property.getCustomEditor();
		comboBox.setEditable(aFlag);
	}

	/**
	 * add by yux,2009-2-4 从图形属性子节点中读取数据至下拉属性编辑器中
	 * 
	 * @param property
	 *            ：下拉属性编辑器
	 * @param element
	 *            ：图形属性子节点
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
