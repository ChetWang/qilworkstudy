/*
 * GraphUnitSavePanel.java
 *
 * Created on 2008年6月23日, 下午3:54
 */
package com.nci.svg.ui.graphunit;

import java.awt.event.ItemEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.graphunit.NCISymbolStatusBean;
import com.nci.svg.ui.EditorPanel;
import com.nci.svg.util.Constants;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 图元保存前的属性输入框
 * 
 * @author Qil.Wong
 */
public class GraphUnitSavePanel extends EditorPanel {

	// 选择的项，表示选择“确定”还是“取消”
	private int selectOption = 1;
	/**
	 * 选择“确定”
	 */
	public static final int SELECT_OPTION_OK = 0;
	/**
	 * 选择“取消”
	 */
	public static final int SELECT_OPTION_CANCEL = 1;
	/**
	 * 什么都没有的状态
	 */
	
	private String select = ResourcesManager.bundle
			.getString("nci_combos_please_select");
	private boolean dataIniting = false;

	private boolean updateMode = false;

	/**
     * @return the defineCombo
     */
    public javax.swing.JComboBox getDefineCombo() {
        return defineCombo;
    }

    /** Creates new form GraphUnitSavePanel */
	public GraphUnitSavePanel(Editor editor) {
		super(editor);
		initComponents();

		relatedCombo.setRenderer(new NCIThumbnailRenderer());
	}

	/**
	 * 初始化最最初始的数据
	 */
	public void initData() {
		dataIniting = true;
		((DefaultComboBoxModel) typeCombo.getModel()).removeAllElements();
		((DefaultComboBoxModel) typeCombo.getModel()).addElement(select);
		// 初始化类型combobox
		for (String type : editor.getGraphUnitManager().getSymbolsTypes()) {
		    String[] strTmp = type.split("=");
		    if(strTmp.length == 1)
			((DefaultComboBoxModel) typeCombo.getModel()).addElement(type);
		    else
		        ((DefaultComboBoxModel) typeCombo.getModel()).addElement(strTmp[0]);
		}
		((DefaultComboBoxModel) statusCombo.getModel()).removeAllElements();
		((DefaultComboBoxModel) statusCombo.getModel()).addElement(select);
		// 初始化状态combobox
		for (String status : editor.getGraphUnitManager().getSymbolsStatus()) {
			((DefaultComboBoxModel) statusCombo.getModel()).addElement(status);
		}
		dataIniting = false;
	}

	/**
	 * 
	 * @param flag
	 */
	public void setUpdateMode(boolean flag) {
		updateMode = flag;
		nameText.setEditable(!flag);
		typeCombo.setEnabled(!flag);
	}

	/**
	 * 给保存面板赋值，尤其是在修改图元的时候，保存是应将图元的前一段属性显示出来
	 * 
	 * @param bean
	 */
	public void setValue(NCIEquipSymbolBean bean) {
		if (bean.getGraphUnitName() == null
				|| bean.getGraphUnitName().equals("")) {
			return;
		}
		try
		{
		this.nameText.setText(bean.getGraphUnitName());
		this.typeCombo.setSelectedItem(bean.getGraphUnitType());
		this.statusCombo.setSelectedItem(bean.getGraphUnitStatus());
		String group = bean.getGraphUnitGroup();
		if (group != null && !group.equals("")) {
			int size = relatedCombo.getModel().getSize();
			for (int i = 0; i < size; i++) {
				Object o = relatedCombo.getModel().getElementAt(i);
				if (o instanceof NCIThumbnailPanel) {
					if (((NCIThumbnailPanel) o).getSymbolBean()
							.getGraphUnitGroup().equals(group)) {
						this.relatedCombo.setSelectedIndex(i);
						break;
					}
				}
			}
		}
		}
		catch(Exception ex)
		{
		    ex.printStackTrace();
		}
	}

	/**
	 * 刷新图元信息
	 * 
	 * @param bean
	 */
	public void refreshSymbolBean(NCIEquipSymbolBean bean) {
		if (relatedCombo.getSelectedIndex() > 0) {
			bean.setGraphUnitGroup(((NCIThumbnailPanel) relatedCombo
					.getSelectedItem()).getSymbolBean().getGraphUnitGroup());
		}

		bean.setGraphUnitName(nameText.getText().trim());
		bean.setGraphUnitStatus(statusCombo.getSelectedItem().toString());
		bean.setGraphUnitType(typeCombo.getSelectedItem().toString());
	}

	public void reset() {
		setSelectOption(SELECT_OPTION_CANCEL);
		this.getNameText().setText("");
		this.getTypeCombo().setModel(new DefaultComboBoxModel());
		this.getStatusCombo().setModel(new DefaultComboBoxModel());
		this.getRelatedCombo().setModel(new DefaultComboBoxModel());
	}

	/**
	 * 
	 * 检查输入的有效性
	 * 
	 * @return boolean true为有效；false为无效，需重新输入
	 */
	public boolean checkInput(NCIEquipSymbolBean checkedSymbol) {

		if (nameText.getText().trim().equals("")) {
			String s = ResourcesManager.bundle
					.getString("nci_graphunit_save_errmsg_noname");
			showErrorOptionPane(s);
			return false;
		}
		if (typeCombo.getSelectedIndex() == 0) {
			showErrorOptionPane(ResourcesManager.bundle
					.getString("nci_graphunit_save_errmsg_notype"));
			return false;
		}
		if (statusCombo.getSelectedIndex() == 0) {
			showErrorOptionPane(ResourcesManager.bundle
					.getString("nci_graphunit_save_errmsg_nostatus"));
			return false;
		}
		if (statusCombo.getSelectedIndex() > 0) {
			String status = statusCombo.getSelectedItem().toString();
			if (relatedCombo.getSelectedIndex() > 0) {
				NCIEquipSymbolBean relatedSymbol = ((NCIThumbnailPanel) relatedCombo
						.getSelectedItem()).getSymbolBean();

				String group = relatedSymbol.getGraphUnitGroup();
				NCISymbolStatusBean statusBean = editor.getGraphUnitManager()
						.getStatusMap().get(group);
				String existSymbolID = statusBean.getStatusSymbol(editor.getGraphUnitManager().getSymbolsStatus(), status);
				if(existSymbolID!=null && !existSymbolID.equals("")){
					showErrorOptionPane(ResourcesManager.bundle
							.getString("nci_graphunit_save_errmsg_status_existed"));
					return false;
				}				
			}
		}

		return true;
	}

	/**
	 * 提示错误信息
	 * 
	 * @param errMsg
	 *            错误信息
	 */
	private void showErrorOptionPane(String errMsg) {
		JOptionPane.showConfirmDialog(this, errMsg, ResourcesManager.bundle
				.getString("nci_graphunit_properties_inputer_error"),
				JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 显示相关联的设备
	 */
	private void showRelatedGraphUnits() {
		if (!dataIniting) {
			((DefaultComboBoxModel) relatedCombo.getModel())
					.removeAllElements();
			((DefaultComboBoxModel) relatedCombo.getModel()).addElement(select);
			if (!statusCombo.getSelectedItem().equals(Constants.GRAPHUNIT_STATUS_NONE)
					&& typeCombo.getSelectedIndex() != 0) {
				String type = typeCombo.getSelectedItem().toString();

				LinkedHashMap<String, NCIThumbnailPanel> thumbnailsMap = editor
						.getThumnailsMap().get(type);
				if(thumbnailsMap != null)
				{
				Iterator<String> it = thumbnailsMap.keySet().iterator();
				NCIThumbnailPanel outlookThumbnailPanel = null;
				NCIThumbnailPanel newComboPanel = null;
				while (it.hasNext()) {
					outlookThumbnailPanel = thumbnailsMap.get(it.next());
					newComboPanel = new NCIThumbnailPanel(
							NCIThumbnailPanel.THUMBNAIL_COMBOBOX, editor);
					newComboPanel.getSvgCanvas().setDocument(
							outlookThumbnailPanel.getDocument());
					newComboPanel.getTextLabel().setText(
							outlookThumbnailPanel.getTextLabel().getText());
					newComboPanel.getSvgCanvas().setSize(
							NCIThumbnailPanel.comboPrefferedSize);
					newComboPanel.setSymbolBean(outlookThumbnailPanel
							.getSymbolBean());
					((DefaultComboBoxModel) relatedCombo.getModel())
							.addElement(newComboPanel);
				}
				}

			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		nameText = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		defineCombo = new javax.swing.JComboBox();
		typeCombo = new javax.swing.JComboBox();
		jLabel3 = new javax.swing.JLabel();
		statusCombo = new javax.swing.JComboBox();
		jLabel4 = new javax.swing.JLabel();
		relatedCombo = new javax.swing.JComboBox();
		okBtn = new javax.swing.JButton();
		cancelBtn = new javax.swing.JButton();

		jLabel1.setText(ResourcesManager.bundle
				.getString("nci_graphunit_property_name")); // NOI18N

		jLabel2.setText(ResourcesManager.bundle
				.getString("nci_graphunit_property_type")); // NOI18N

		defineCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                typeComboItemStateChanged(evt);
            }
        });
		
		typeCombo.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				typeComboItemStateChanged(evt);
			}
		});

		jLabel3.setText(ResourcesManager.bundle
				.getString("nci_graphunit_property_status")); // NOI18N

		statusCombo.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				statusComboItemStateChanged(evt);
			}
		});

		jLabel4.setText(ResourcesManager.bundle
				.getString("nci_graphunit_property_related")); // NOI18N

		okBtn.setText(ResourcesManager.bundle.getString("OK")); // NOI18N

		cancelBtn.setText(ResourcesManager.bundle.getString("Cancel")); // NOI18N

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING,
																false)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								jLabel1)
																						.add(
																								jLabel2)
																						.add(
																								jLabel3)
																						.add(
																								jLabel4))
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								nameText,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								209,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								typeCombo,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								198,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								statusCombo,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								198,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																						.add(
																								relatedCombo,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																								199,
																								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				okBtn)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				cancelBtn)))
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { cancelBtn, okBtn },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout.linkSize(new java.awt.Component[] { nameText, relatedCombo,
				statusCombo, typeCombo },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel1)
														.add(
																nameText,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																23,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE,
																false)
														.add(jLabel2)
														.add(
																typeCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																25,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE,
																false)
														.add(jLabel3)
														.add(
																statusCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																25,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE,
																false)
														.add(jLabel4)
														.add(
																relatedCombo,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																25,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.UNRELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(cancelBtn).add(
																okBtn))
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { nameText, relatedCombo,
				statusCombo, typeCombo },
				org.jdesktop.layout.GroupLayout.VERTICAL);

	}// </editor-fold>//GEN-END:initComponents

	private void statusComboItemStateChanged(java.awt.event.ItemEvent evt) {// GEN
																			// -
																			// FIRST
																			// :
																			// event_statusComboItemStateChanged
		// TODO add your handling code here:
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			showRelatedGraphUnits();
		}
	}// GEN-LAST:event_statusComboItemStateChanged

	private void typeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-
																			// FIRST
																			// :
																			// event_typeComboItemStateChanged
		// TODO add your handling code here:
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			showRelatedGraphUnits();
		}
	}// GEN-LAST:event_typeComboItemStateChanged

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cancelBtn;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JTextField nameText;
	private javax.swing.JButton okBtn;
	private javax.swing.JComboBox relatedCombo;
	private javax.swing.JComboBox statusCombo;
	private javax.swing.JComboBox typeCombo;
	private javax.swing.JComboBox defineCombo;

	// End of variables declaration//GEN-END:variables
	public javax.swing.JButton getCancelBtn() {
		return cancelBtn;
	}

	public javax.swing.JButton getOkBtn() {
		return okBtn;
	}

	public int getSelectOption() {
		return selectOption;
	}

	public void setSelectOption(int selectOption) {
		this.selectOption = selectOption;
	}

	public javax.swing.JTextField getNameText() {
		return nameText;
	}

	public javax.swing.JComboBox getRelatedCombo() {
		return relatedCombo;
	}

	public javax.swing.JComboBox getStatusCombo() {
		return statusCombo;
	}

	public javax.swing.JComboBox getTypeCombo() {
		return typeCombo;
	}
}
