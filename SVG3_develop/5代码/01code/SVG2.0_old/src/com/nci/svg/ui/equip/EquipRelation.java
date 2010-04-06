/*
 * EquipRelation.java
 *
 * Created on 2008年5月30日, 下午2:07
 */
package com.nci.svg.ui.equip;

import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.equip.EquipBean;
import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;
import com.nci.svg.util.Constants;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;

/**
 * 
 * @author Qil.Wong
 */
public class EquipRelation extends javax.swing.JDialog {

	private Element selectedEquipElement;
	
	private String equipID = "";

	private static String RELATE_NEW = "0";
	private static String RELATE_UPDATE = "1";
	
	private Editor editor;

	/** Creates new form EquipRelation */
	public EquipRelation(Editor edi, boolean modal,
			Element selectedEquipElement) {
		super(edi.findParentFrame(), modal);
		this.editor = edi;
		this.selectedEquipElement = selectedEquipElement;
		initComponents();
		equipsList.setModel(new DefaultListModel());
		equipsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setTitle("设备关联");
	}

	/**
	 * 往列表中添加元素，这些元素原先以list形式组合
	 * 
	 * @param elements
	 */
	public void addElements(List elements) {
		for (int i = 0; i < elements.size(); i++) {
			((DefaultListModel) equipsList.getModel()).addElement(elements
					.get(i));
		}
	}

	/**
	 * 往列表中添加元素，这些元素存放在xml Document对象中
	 * 
	 * @param doc
	 *            类型如： <equipments> <equipment equipName="110kV袁化变1#主变110kV开关"
	 *            equipID="5027-B-1002-N0000-1-s10080145"/> ...... <equipments>
	 */
	public void addElements(Document doc) {
		NodeList equipmentNodeList = doc.getElementsByTagName("equipment");
		for (int i = 0; i < equipmentNodeList.getLength(); i++) {
			Element equip = (Element) equipmentNodeList.item(i);
			EquipBean bean = new EquipBean();
			bean.setEquipName(equip.getAttribute("equipName"));
			bean.setEquipID(equip.getAttribute("equipID"));
			((DefaultListModel) equipsList.getModel()).addElement(bean);
		}
	}

	/**
	 * 进行设备关联
	 * 
	 * @param status
	 *            "0"代表新建，"1"代表更新
	 */
	private String relate(String status) {
		EquipBean selectBean = (EquipBean) equipsList.getSelectedValue();
		equipID = selectBean.getEquipID();
		// Element metadataElement = (Element) selectedEquipElement
		// .getNextSibling();
		Element metadataElement = Utilities.getSingleChildElement(Constants.NCI_SVG_METADATA,
				Utilities.parseSelectedElement(selectedEquipElement));
		Element objRefElement = (Element) metadataElement.getElementsByTagName(
				Constants.NCI_SVG_PSR_OBJREF).item(0);
		String scadaID = objRefElement.getAttribute(Constants.NCI_SVG_SCADAID_ATTR);
		StringBuffer xml = new StringBuffer();
		xml.append("<equipRelation>").append("<scadaObjectID>").append(scadaID)
				.append("</scadaObjectID>").append("<psmsID>").append(equipID)
				.append("</psmsID>").append("</equipRelation>");
		StringBuffer baseURL = new StringBuffer((String)editor.getGCParam("appRoot")).append(
		        (String)editor.getGCParam("servletPath")).append("?action=equipManualMapSave");
		String[][] param = new String[2][2];
		param[0][0] = "operType";
		param[0][1] = status;
		param[1][0] = "metaData";
		param[1][1] = xml.toString();
		String ret = "";
		try {
			ret = (String) Utilities.communicateWithURL(baseURL.toString(),
					param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 当服务器上已经有相应设备的信息时，强制覆盖掉原信息
	 */
	private void relateUpdate() {
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				String ret = relate(RELATE_UPDATE);
				if (ret.equals("0")) {
					refresh(true);
					dispose();
				}
				return null;
			}

		};
		LongtermTask lt = new LongtermTask("正在更新关联信息...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(lt);
	}

	private void refresh(boolean successFlag) {
		if (successFlag) {		
			
			Element realShapeEle = Utilities
					.parseSelectedElement(selectedEquipElement);
			Utilities.assighAppCode(equipID, realShapeEle);
			Utilities.markShapeAsEquipRelated(realShapeEle);
			editor.getSvgSession().refreshCurrentHandleImediately();
			
			//成功时应保存
			editor.getIOManager().getFileSaveManager()
			.saveHandleDocument(
					editor.getHandlesManager()
							.getCurrentHandle(), false,
							editor.getDesktop());
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		equipsList = new javax.swing.JList();
		okBtn = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jLabel1.setText("请选择进行关联的设备：");

		equipsList
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						equipsListValueChanged(evt);
					}
				});
		jScrollPane1.setViewportView(equipsList);

		okBtn.setText("确定");
		okBtn.setEnabled(false);
		okBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okBtnAction(evt);
			}
		});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
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
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																jLabel1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																358,
																Short.MAX_VALUE)
														.add(
																jScrollPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																358,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																okBtn,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																71,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup().addContainerGap().add(jLabel1)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED).add(
								jScrollPane1,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								264, Short.MAX_VALUE).addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED).add(
								okBtn).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void okBtnAction(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okBtnAction
		final JDialog o = this;
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				return relate(RELATE_NEW);
			}

			@Override
			public void done() {
				try {
					String ret = (String) get();
					if (ret.equals("0")) {// 成功保存关联信息
						refresh(true);
						dispose();
					} else {
						int result = JOptionPane.showConfirmDialog(o,
								"您所选的设备已经有关联记录，确定覆盖?", "提示",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
						if (result == JOptionPane.OK_OPTION)
							relateUpdate();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		LongtermTask lt = new LongtermTask("正在提交关联信息...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(lt);
	}// GEN-LAST:event_okBtnAction

	private void equipsListValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_equipsListValueChanged
		okBtn.setEnabled(true);
	}// GEN-LAST:event_equipsListValueChanged

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JList equipsList;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton okBtn;
	// End of variables declaration//GEN-END:variables
}
