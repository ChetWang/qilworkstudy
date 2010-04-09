package com.nci.domino.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.nci.domino.help.Functions;

/**
 * 专用combobox，里面的值专放WfComboBean
 * 
 * @author Qil.Wong
 * 
 */
public class WfComboBox extends JComboBox {

	public static final WfComboBean first = new WfComboBean("", "请选择");

	DefaultComboBoxModel model;

	boolean needFirst;

	boolean iconElements;

	public WfComboBox() {
		this(true, false);
	}

	public WfComboBox(boolean needFirst) {
		this(needFirst, false);
	}

	public WfComboBox(boolean needFirst, boolean iconElements) {
		init(needFirst, iconElements);
	}

	/**
	 * 初始化
	 * 
	 * @param needFirst
	 * @param iconElements
	 */
	private void init(boolean needFirst, boolean iconElements) {
		this.needFirst = needFirst;
		this.iconElements = iconElements;
		if (needFirst) {
			model = new DefaultComboBoxModel(new WfComboBean[] { first });
		} else {
			model = new DefaultComboBoxModel();
		}
		this.setModel(model);
		if (iconElements)
			setRenderer(new WfIconComboBoxRenderer());
	}

	/**
	 * 根据id设置选择对象
	 * 
	 * @param id
	 */
	public void setSelectedItemByID(String id) {
		if (id == null) {
			setSelectedIndex(0);
		} else {
			Object o = null;
			int size = model.getSize();
			for (int i = 0; i < size; i++) {
				WfComboBean comboBean = (WfComboBean) model.getElementAt(i);
				if (id.equals(comboBean.id)) {
					o = comboBean;
					break;
				}
			}
			if (o != null) {
				super.setSelectedItem(o);
			}
		}
	}

	@Override
	public Object getSelectedItem() {
		if (super.getSelectedItem() == null && needFirst) {
			return first;
		}
		return super.getSelectedItem();
	}

	/**
	 * 重置模型内所有元素
	 * 
	 * @param values
	 *            新元素
	 * @param keepFirst
	 *            是否保留第一个“请选择”
	 */
	public void resetElements(List<WfComboBean> values, boolean keepFirst) {
		model.removeAllElements();
		if (keepFirst) {
			model.addElement(first);
		}
		for (WfComboBean o : values) {
			model.addElement(o);
		}
		updateUI();
	}

	public void setEnabled(boolean flag) {
		if (!flag) {
			setSelectedIndex(0);
		}
		super.setEnabled(flag);
	}

	public DefaultComboBoxModel getWfComboModel() {
		return model;
	}

	/**
	 * WfComboBox的专用对象
	 * 
	 * @author Qil.Wong
	 * 
	 */
	public static class WfComboBean {
		/**
		 * 隐藏字段，通常作为id或图片名
		 */
		public String id;

		/**
		 * 用来显示的字段
		 */
		public Object value;

		public WfComboBean(String id, Object value) {
			this.id = id;
			this.value = value;
		}

		public String toString() {
			if (value == null) {
				return "";
			}
			return value.toString();
		}
	}

	/**
	 * 带图片的combo renderer
	 * 
	 * @author Qil.Wong
	 * 
	 */
	public static class WfIconComboBoxRenderer extends JLabel implements
			ListCellRenderer {
		private Font uhOhFont;

		public WfIconComboBoxRenderer() {
			setOpaque(true);
			// setHorizontalAlignment(CENTER);
			// setVerticalAlignment(CENTER);
			setPreferredSize(new Dimension(120, 20));
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			WfComboBean comboBean = (WfComboBean) value;

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			ImageIcon icon = null;
			if (comboBean != null) {
				icon = Functions.getImageIcon(comboBean.id);
			}
			if (icon != null) {
				setText(comboBean.toString());
				setFont(list.getFont());
				setIcon(icon);
			} else {
				if (comboBean != null) {
					String ut = comboBean.toString();
					if (comboBean != first) {
						ut = ut + comboBean.toString();
					}
					setUhOhText(ut, list.getFont());
				}
			}

			return this;
		}

		protected void setUhOhText(String uhOhText, Font normalFont) {
			if (uhOhFont == null) {
				uhOhFont = normalFont.deriveFont(Font.ITALIC);
			}
			setFont(uhOhFont);
			setText(uhOhText);
		}
	}

}
