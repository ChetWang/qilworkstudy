/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ComboPanel.java
 *
 * Created on 2008-12-26, 16:33:07
 */

package com.nci.svg.sdk.ui;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;

/**
 * 
 * @author yx.nci
 */
public class ComboPanel extends javax.swing.JPanel {
	private DefaultComboBoxModel codeModel = new DefaultComboBoxModel();

	
	public ComboPanel() {
		initComponents();
	}

	public javax.swing.JLabel getShowText() {
		return showText;
	}

	public javax.swing.JComboBox getSonCombo() {
		return sonCombo;
	}

   private void initComponents() {

        showText = new javax.swing.JLabel();
        sonCombo = new javax.swing.JComboBox();
        sonCombo.setModel(codeModel);
        this.setLayout(new FlowLayout());
        this.add(new DoubleCompPanel(showText,sonCombo));      

    }


    private javax.swing.JLabel showText;
    private javax.swing.JComboBox sonCombo;


	public void setSonComboData(HashMap<String, CodeInfoBean> map) {
		codeModel.removeAllElements();
		Iterator<CodeInfoBean> iterator = map.values().iterator();
		while (iterator.hasNext()) {
			CodeInfoBean bean = iterator.next();
			SimpleCodeBean codeBean = new SimpleCodeBean();
			codeBean.setCode(bean.getValue());
			codeBean.setName(bean.getName());
			codeModel.addElement(codeBean);
		}

		codeModel.setSelectedItem(null);
	}
	
	public void setSonComboData(ArrayList<String> list)
	{
		codeModel.removeAllElements();
		for(String text:list)
		{
			SimpleCodeBean codeBean = new SimpleCodeBean();
			codeBean.setCode(text);
			codeBean.setName(text);
			codeModel.addElement(codeBean);
		}

		codeModel.setSelectedItem(null);
	}
	
	public void setSelectValue(String values)
	{
		int size = codeModel.getSize();
		for(int i = 0;i < size;i++)
		{
			SimpleCodeBean codeBean = (SimpleCodeBean)codeModel.getElementAt(i);
			if(codeBean.getCode().equals(values))
			{
				codeModel.setSelectedItem(codeBean);
			}
		}
	}
}
