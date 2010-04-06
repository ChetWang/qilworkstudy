/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-20
 * @���ܣ�TODO
 *
 */
package com.nci.svg.sdk.graphmanager.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphmanager.property.editor.NciCustomPropertyEditor;
import com.nci.svg.sdk.ui.EditorPanel;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 * 
 */
public class BusinessPropertyPanel extends EditorPanel {
	private static final long serialVersionUID = -3973481245399014493L;
	private PropertyChangeListener listener = null;
	private Set<Element> curElements = new HashSet<Element>();
	private ArrayList<NciBussProperty> propertyList = new ArrayList<NciBussProperty>();
	private NciBussProperty[] propertyArray = null;
	private PropertyEditorRegistry registry = new PropertyEditorRegistry();
	/**
	 * ���������Ե����
	 */
	private PropertySheetPanel sheet = null;

	public BusinessPropertyPanel(EditorAdapter editor) {
		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				init();
				updateUI();
			}
		});
	}

	private void init() {
		setLayout(LookAndFeelTweaks.createVerticalPercentLayout());
		sheet = new PropertySheetPanel();
		sheet.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		sheet.setToolBarVisible(false);
		sheet.setSortingCategories(true);
		sheet.setSortingProperties(true);
		sheet.setRestoreToggleStates(true);
		sheet.setDescriptionVisible(false);

		add(sheet, "*");
	}

	public synchronized void setElement(final Set<Element> elements) {
		// �ȶԵ�ǰ�ڵ��Ƿ�仯��û�仯������������
		if(elements != null && curElements.containsAll(elements) 
				&& elements.containsAll(elements))
			return;
		// ����ϴε���������
		try
		{
		curElements.clear();
		if(elements !=null && elements.size() > 0)
		    curElements.addAll(elements);
		if (listener != null) {
			sheet.removePropertySheetChangeListener(listener);
			listener = null;
		}
		propertyList.clear();
		if (propertyArray != null && propertyArray.length > 0) {
			int len = propertyArray.length;
			for (int i = len; i > 0; i--) {
				registry.unregisterEditor(propertyArray[i - 1]);
			}
			propertyArray = null;
            sheet.setVisible(false);
		}
		}
		catch(Exception ex)
		{
			System.out.println("prop "+ex);
		}

		if (elements == null || elements.size() == 0)
			return;

		// ��ȡͼԪ��ģ������
		//ģ�ͱ���һ��
		String symbolName = null;
		for(Element element :elements)
		{
			if(symbolName == null)
				symbolName = editor.getSvgSession().getSymbolName(element);
			else
			{
				
				if(editor.getSvgSession().getSymbolName(element) == null || editor.getSvgSession().getSymbolName(element).equals(symbolName) == false)
				{
					return;
				}
			}
		}

		// ��ȡģ����淶��Ӧ��ϵ
		ArrayList<ModelRelaIndunormBean> list = editor.getSvgSession()
				.getModelRelaInfoBySymbolName(symbolName);
		if (list == null || list.size() == 0)
			return;

		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();

		// �������Կ�
		for (ModelRelaIndunormBean bean : list) {
			if (bean.getDescShortName() == null) {
				if(curElements.size() > 1 && !bean.getModelPropertyType().equals(ModelPropertyBean.TYPE_BASEPROPERTY))
				{
					continue;
				}
				if (bean.getModelPropertyCode() == null) {
					NciBussEditProperty property = new NciBussEditProperty(
							editor, bean, handle);
					propertyList.add(property);
				} else {
					// modify by yux,2009.2.19
					// �����������ô���
					String code = bean.getModelPropertyCode();
					if (code.indexOf("CODE:") == 0) {
						// ��Ӧ�����
						String strTmp[] = code.split(":");
						if (strTmp.length < 2)
							continue;
						ComboBoxPropertyEditor propertyEditor = new ComboBoxPropertyEditor();
						NciBussComboProperty property = new NciBussComboProperty(
								editor, bean, handle, propertyEditor);
						propertyList.add(property);
						registry.registerEditor(property, propertyEditor);
					} else if (code.indexOf("CLASS:") == 0) {
						// ��Ӧ��ִ�д���
						String strTmp[] = code.split(":");
						if (strTmp.length < 3)
							continue;
						NciCustomPropertyEditor propertyEditor = new NciCustomPropertyEditor(
								editor, curElements, strTmp[1], strTmp[2]);
						NciBussProperty property = new NciBussProperty(
								editor, bean, handle);
						propertyList.add(property);
						registry.registerEditor(property, propertyEditor);

					}

				}
			}
		}
		sheet.setEditorFactory(registry);

		propertyArray = new NciBussProperty[propertyList.size()];
		sheet.setProperties(propertyList.toArray(propertyArray));
		sheet.setVisible(true);
		if(curElements.size() == 1)
	    	sheet.readFromObject(curElements.iterator().next());

		listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				NciBussProperty prop = (NciBussProperty) evt.getSource();
				prop.writeToObject(curElements);
			}
		};
		sheet.addPropertySheetChangeListener(listener);
	}

}
