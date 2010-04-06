/*
 * LayerSelectionPanel.java
 *
 * Created on 2008年7月10日, 下午1:41
 */
package com.nci.svg.ui.layer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.equip.NCIPropertyComboBean;
import com.nci.svg.ui.EditorPanel;
import com.nci.svg.util.EquipPool;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;

/**
 * 
 * @author Qil.Wong
 */
public class LayerSelectionPanel extends EditorPanel {

	/**
	 * 图元所在层的名称
	 */
	private String layerType;
	private String layerTypeCode;
	private String layerNodeName;
	private DefaultTableModel tableModel;

	/** Creates new form LayerSelectionPanel */
	public LayerSelectionPanel(Editor editor, String layerType,
			String layerTypeCode, String layerNodeName) {
		super(editor);
		this.layerType = layerType;
		this.layerTypeCode = layerTypeCode;
		this.layerNodeName = layerNodeName;
		initComponents();
		tableModel = (DefaultTableModel) layerTable.getModel();
		iniData();
	}

	/**
	 * 初始化数据
	 */
	private void iniData() {
		SwingWorker<String, String> worker = new SwingWorker<String, String>() {

			@Override
			protected String doInBackground() throws Exception {
				try {
					editor.getLayerSelectionManager().getLayerElementMap().clear();
					NodeList layerNodes = Utilities.findNodes("//*[@"
							+ layerTypeCode + "]", editor.getHandlesManager()
							.getCurrentHandle().getCanvas().getDocument()
							.getDocumentElement());
					ArrayList<String> layerValues = new ArrayList<String>();
					Element ele = null;
					String layerValue = null;
					for (int i = 0; i < layerNodes.getLength(); i++) {
						boolean isDefsEleChild = false;
						Node node = layerNodes.item(i);
						while(node.getParentNode()!=null){
							if(node.getNodeName().equals("defs")){
								isDefsEleChild = true;
								break;
							}
							node = node.getParentNode();
						}
						if(isDefsEleChild){
							continue;
						}
						if (layerNodeName.equals("")) {
							if (layerNodes.item(i) instanceof Element) {
								ele = (Element) layerNodes.item(i);
								layerValue = ele.getAttribute(layerTypeCode);
								if (!layerValues.contains(layerValue)) {
									layerValues.add(layerValue);
									//仅在海宁适用
									
									editor.getLayerSelectionManager().getLayerElementMap().put(layerValue, ele);
									publish(layerValue);
								}
							}
						} else {
							if (layerNodes.item(i) instanceof Element) {
								if (layerNodes.item(i).getNodeName().equals(
										layerNodeName) && ((Element)layerNodes.item(i)).getAttribute(layerTypeCode).indexOf("ayer")>=0) {
									ele = (Element) layerNodes.item(i);
									layerValue = ele
											.getAttribute(layerTypeCode);
									if (!layerValues.contains(layerValue)) {
										layerValues.add(layerValue);
										//仅在海宁适用
										
										editor.getLayerSelectionManager().getLayerElementMap().put(layerValue, ele);
										publish(layerValue);
									}
								}
							}
						}
					}
				} catch (XPathExpressionException ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			public void process(List<String> values) {
				for (String value : values) {
					addOneRow(value);
				}
			}
		};
		worker.execute();
	}

	/**
	 * 获取选中的层，即需要显示的层
	 * 
	 * @return
	 */
	public ArrayList<String> getSelectLayers() {
		ArrayList<String> layerNames = new ArrayList<String>();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if ((Boolean) tableModel.getValueAt(i, 1)) {
				Object o = tableModel.getValueAt(i, 0);
				if (o instanceof String) {
					layerNames.add((String) o);
				} else if (o instanceof NCIPropertyComboBean) {
					layerNames.add(((NCIPropertyComboBean) o).getComboBeanID());
				}
			}
		}
		return layerNames;
	}

	/**
	 * 获取所有的层
	 * 
	 * @return
	 */
	public ArrayList<String> getAllLayers() {
		ArrayList<String> layerNames = new ArrayList<String>();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			Object o = tableModel.getValueAt(i, 0);
			if (o instanceof String) {
				layerNames.add((String) o);
			} else if (o instanceof NCIPropertyComboBean) {
				layerNames.add(((NCIPropertyComboBean) o).getComboBeanID());
			}
		}
		return layerNames;
	}

	/**
	 * 获取层类型名称
	 * 
	 * @return
	 */
	public String getLayerTypeName() {
		return layerType;
	}

	/**
	 * 获取层类型代码
	 * 
	 * @return
	 */
	public String getLayerTypeCode() {
		return layerTypeCode;
	}

	public DefaultTableModel getLayerTableModel() {
		return tableModel;
	}

	/**
	 * 往表格中添加一行值
	 * 
	 * @param value
	 */
	private void addOneRow(String value) {
		// 前一次过滤显示的层
		ArrayList<String> selectedLayers = editor.getHandlesManager()
				.getCurrentHandle().getCanvas().getSelectedLayerMap().get(
						layerTypeCode);
		Object obj = EquipPool.getComboMap().get(layerType).get(value);
		boolean isSelected = false;
		if (obj == null)
			obj = value;
		if (selectedLayers != null) {
			if (obj instanceof String) {
				if (selectedLayers.contains(obj)) {
					isSelected = true;
				}
			} else if (obj instanceof NCIPropertyComboBean) {
				if (selectedLayers.contains(((NCIPropertyComboBean) obj)
						.getComboBeanID())) {
					isSelected = true;
				}
			}
		}
		tableModel.addRow(new Object[] { obj, isSelected });
	}

	/**
	 * 获取层选择所在的表JTable
	 * 
	 * @return
	 */
	public JTable getLayerTable() {
		return layerTable;
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

		jScrollPane1 = new javax.swing.JScrollPane();
		layerTable = new javax.swing.JTable();

		layerTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {}, new String[] { layerType, "选择" }) {
			Class[] types = new Class[] { java.lang.Object.class,
					java.lang.Boolean.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			boolean[] canEdit = new boolean[] { false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(layerTable);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 336,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 136,
				Short.MAX_VALUE));
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable layerTable;
	// End of variables declaration//GEN-END:variables
}
