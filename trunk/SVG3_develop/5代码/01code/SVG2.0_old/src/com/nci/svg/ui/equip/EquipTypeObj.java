package com.nci.svg.ui.equip;

import com.nci.svg.equip.EquipSerialDocument;
import com.nci.svg.equip.EquipBean;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.nci.svg.util.EquipPool;

public class EquipTypeObj extends JPanel {

	private String name = null;
	private JComponent comp = null;
	private boolean autoCalculate = false;
	private NCIEquipPropertyManager equipPropManager = null;

	public EquipTypeObj(String name, JComponent comp, boolean autoCalculate,
			NCIEquipPropertyManager equipPropManager) {
		this.name = name;
		this.comp = comp;
		this.autoCalculate = autoCalculate;
		this.equipPropManager = equipPropManager;
		JLabel label = new JLabel(name + ":");
		label.setPreferredSize(new Dimension(70, 23));
		comp.setPreferredSize(new Dimension(130, 23));
		GridBagLayout glayout = new GridBagLayout();
		this.setLayout(glayout);
		glayout.columnWeights = new double[] { 0.0f, 0.5f };
		glayout.rowWeights = new double[] { 0.0f };
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		gbc_label.insets = new Insets(2, 0, 2, 0);
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		glayout.setConstraints(label, gbc_label);
		this.add(label);
		GridBagConstraints gbc_comp = new GridBagConstraints();
		gbc_comp.gridx = 1;
		gbc_comp.gridy = 0;
		gbc_comp.insets = new Insets(2, 0, 2, 0);
		gbc_comp.anchor = GridBagConstraints.EAST;
		gbc_comp.fill = GridBagConstraints.HORIZONTAL;
		glayout.setConstraints(comp, gbc_comp);
		this.add(comp);
		comp.setEnabled(!autoCalculate);
		// ���Զ����ؼ�ֵ�Ŀؼ�����Ҫ�ڸ���ֵ�仯ʱ�����Զ�����ֵ�ؼ��Ŀռ丳��һ���Ķ���
		if (!autoCalculate) {
			if (comp instanceof JComboBox) {
				((JComboBox) comp).addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							showAutoCalculateValues();
						}
					}
				});
			} else if (comp instanceof JTextField) {
				if (name.equals("���")) {
					((JTextField) comp).setDocument(new EquipSerialDocument());
				}
				comp.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent evt) {
						showAutoCalculateValues();
					}
				});
			}
		}
	}

	/**
	 * ��ʾ�Զ�ֵ��ʾ�ؼ���ֵ
	 */
	private void showAutoCalculateValues() {
		Iterator<String> allEquipTypeObjIterator = this.equipPropManager
				.getPropPanelMap().keySet().iterator();
		int index = 0;
		while (allEquipTypeObjIterator.hasNext()) {
			String panelName = allEquipTypeObjIterator.next();
			EquipTypeObj obj = this.equipPropManager.getPropPanelMap().get(
					panelName);
			if (obj.isAutoCalculate()) {
				switch (index) {
				case 0: // �豸�ı������xml�ĵ�һ��

					calculateAndShowEquipCode(obj);
					break;
				}
			}
		}
	}

	/**equipCoding
	 * ���㲢ʵʱ��ʾ�豸����
	 */
	private void calculateAndShowEquipCode(EquipTypeObj obj) {
		Iterator<String> allEquipTypeObjIterator = this.equipPropManager
				.getPropPanelMap().keySet().iterator();
		EquipBean equipBean = new EquipBean();
		boolean hasVol = false;// ���豸�Ƿ��е�ѹ�ȼ�����

		while (allEquipTypeObjIterator.hasNext()) {
			String panelName = allEquipTypeObjIterator.next();
			try {
				String value = this.equipPropManager.getPropPanelMap().get(
						panelName).getCompObjMappedValue();
				if (value != null) {
					if (panelName.equals("����")) {
						equipBean.setDistrict(value);
					} else if (panelName.equals("���������")) {
						equipBean.setSpdType(value);
					} else if (panelName.equals("��ѹ�ȼ�")) {
						equipBean.setBaseVol(value);
						hasVol = true;
					} else if (panelName.equals("���")) {
						equipBean.setUserInputCode(value);
					}
				}
			} catch (NullPointerException e) {
				// e.printStackTrace();
			}
			equipBean.setHasVol(hasVol);
			((JTextField) obj.getComp()).setText(equipBean.toString());

		}
	}

	public String getEquipTypeObjName() {
		return name;
	}

	public void setEquipTypeObjName(String name) {
		this.name = name;
	}

	public JComponent getComp() {
		return comp;
	}

	public void setComp(JComponent comp) {
		this.comp = comp;
	}

	/**
	 * ��ȡ�����ʾ���ַ���ֵ
	 * 
	 * @return
	 */
	public String getCompObjString() {
		if (comp instanceof JComboBox) {
			return ((JComboBox) comp).getSelectedItem().toString().trim();
		} else if (comp instanceof JTextField) {
			return ((JTextField) comp).getText().trim();
		}
		return null;
	}

	/**
	 * ��ȡ�����ʾ���ַ�����Ӧ��IDֵ�����ID�Ǹ�ҵ����ʱ������Ҫ��ֵ
	 * 
	 * @return
	 */
	public String getCompObjMappedValue() {
		String value_shown = this.getCompObjString();
		if (comp instanceof JComboBox) {
			return EquipPool.getComboMap().get(name).get(value_shown) == null ? ""
					: EquipPool.getComboMap().get(name).get(value_shown)
							.getComboBeanID();

		} else if (comp instanceof JTextField) {
			return value_shown;
		}
		return null;
	}

	public boolean isAutoCalculate() {
		return autoCalculate;
	}

	
	
}
