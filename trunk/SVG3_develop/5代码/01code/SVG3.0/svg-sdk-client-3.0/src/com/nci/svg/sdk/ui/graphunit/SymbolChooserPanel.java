package com.nci.svg.sdk.ui.graphunit;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.GraphUnitModuleAdapter;
import com.nci.svg.swing.model.NCITreeModel;

import fr.itris.glips.svgeditor.resources.ResourcesManager;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Qil.Wong
 */
public class SymbolChooserPanel extends javax.swing.JPanel {

	/**
	 * ���������ڵ�
	 */
	private DefaultMutableTreeNode root = null;
	/**
	 * ������model
	 */
	private TreeModel graphTreeModel = null;
	private NCIEquipSymbolBean selectedSymbol = null;
	EditorAdapter editor;

	/** Creates new form GraphUnitChooserPanel */
	public SymbolChooserPanel(EditorAdapter editor) {
		initComponents();
		deleteButton.setVisible(false);
		this.editor = editor;
		graphUnitTree.setModel(null);
		graphUnitTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		previewCanvas.setRecenterOnResize(true);
		previewCanvas.setOpaque(true);
		previewCanvas.setBackground(Constants.THUMBNAIL_RELEASED_BACKGROUND);
	}

	/**
	 * ��ʼ��������
	 */
	public void initSymbolTree() {
		clearDetails();
		// TODO ��ʼ��ͼԪ��ģ���� deprecated by wangql @2008.12.11-13:41
		// root = new
		// DefaultMutableTreeNode(ResourcesManager.bundle.getString("nci_graphunit_chooser_root"));
		root = new DefaultMutableTreeNode("����");
		graphTreeModel = new NCITreeModel(root);
		this.graphUnitTree.setCellRenderer(new GraphUnitTreeCellRenderer());
		this.graphUnitTree.setModel(graphTreeModel);
		Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> allSymbols = editor
				.getSymbolManager().getAllSymbols();
		Iterator<SymbolTypeBean> it = allSymbols.keySet().iterator();
		// ͼԪģ����໺��Map���ֽ׶���ʵ�ͷ������ڵ�
		Map<String, DefaultMutableTreeNode> tempTypeNodeMap = new HashMap<String, DefaultMutableTreeNode>();
		// ͼԪģ��С�໺��Map��������С�����ƿ����ظ��������ʹ��SymbolTypeBean��Ϊkey
		Map<SymbolTypeBean, DefaultMutableTreeNode> tempSubTypeNameNodeMap = new HashMap<SymbolTypeBean, DefaultMutableTreeNode>();
		// ͼԪģ���������ֶ������ظ�����ˣ�����ʹ��������Ϊkey�Ϳ���
		Map<String, DefaultMutableTreeNode> tempSymbolNodeMap = new HashMap<String, DefaultMutableTreeNode>();
		Map<String, NCIEquipSymbolBean> tempSymbolMap = null;
		while (it.hasNext()) {
			SymbolTypeBean symbolType = it.next();
			// ͼԪģ��ڵ�
			if (!tempTypeNodeMap.containsKey(symbolType.getSymbolType())) {
				String type = Utilities.getSinoSymbolType(symbolType);
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(type);
				tempTypeNodeMap.put(symbolType.getSymbolType(), node);
				root.add(node);
			}
			// ͼԪģ��Ĵ���ڵ�
			if (!tempSubTypeNameNodeMap.containsKey(symbolType)) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						symbolType.getVariety().getName());
				tempSubTypeNameNodeMap.put(symbolType, node);
				tempTypeNodeMap.get(symbolType.getSymbolType()).add(node);
			}
			// �����ͼԪģ��ڵ�
			tempSymbolMap = allSymbols.get(symbolType);
			Iterator<NCIEquipSymbolBean> symbolIt = tempSymbolMap.values()
					.iterator();
			while (symbolIt.hasNext()) {
				NCIEquipSymbolBean symbolBean = symbolIt.next();
				if (!tempSymbolNodeMap.containsKey(symbolBean.getName())) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(
							symbolBean);
					tempSymbolNodeMap.put(symbolBean.getName(), node);
					tempSubTypeNameNodeMap.get(symbolType).add(node);
				}
			}
		}
		graphUnitTree.expandRow(0);
	}

	public NCIEquipSymbolBean getSelectedSymbol() {
		return selectedSymbol;
	}

	/**
	 * �������ϵ��������ݣ�����������
	 */
	public void clear() {
		graphUnitTree.setModel(null);
		previewCanvas.setDocument(null);
		propertiesArea.setText("");
	}

	/**
	 * ���ͼԪ��ʾ������
	 */
	private void clearDetails() {
		previewCanvas.setDocument(null);
		previewCanvas.setBackground(Constants.THUMBNAIL_RELEASED_BACKGROUND);
		propertiesArea.setText("");
		DefaultComboBoxModel statusComboModel = (DefaultComboBoxModel) statusComboBox
				.getModel();
		statusComboModel.removeAllElements();
		statusComboBox.setVisible(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		graphUnitTree = new javax.swing.JTree();
		jPanel1 = new javax.swing.JPanel();
		jPanel1.setBorder(javax.swing.BorderFactory
				.createTitledBorder(ResourcesManager.bundle
						.getString("nci_titleBorder_properties")));
		jScrollPane2 = new javax.swing.JScrollPane();
		propertiesArea = new javax.swing.JTextArea();
		statusComboBox = new javax.swing.JComboBox();
		jPanel2 = new javax.swing.JPanel();
		jPanel2.setBorder(javax.swing.BorderFactory
				.createTitledBorder(ResourcesManager.bundle
						.getString("nci_titleBorder_preview")));
		previewCanvas = new org.apache.batik.swing.JSVGCanvas();
		cancelBtn = new javax.swing.JButton();
		okBtn = new javax.swing.JButton();
		deleteButton = new javax.swing.JButton();

		graphUnitTree
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(
							javax.swing.event.TreeSelectionEvent evt) {
						graphUnitTreeValueChanged(evt);
					}
				});
		jScrollPane1.setViewportView(graphUnitTree);

		propertiesArea.setColumns(20);
		propertiesArea.setEditable(false);
		propertiesArea.setRows(5);
		jScrollPane2.setViewportView(propertiesArea);

		statusComboBox.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				statusComboBoxItemStateChanged(evt);
			}
		});

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane2,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212,
				Short.MAX_VALUE).add(statusComboBox, 0, 212, Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout.createSequentialGroup().add(statusComboBox,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED).add(
								jScrollPane2,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								109, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout previewCanvasLayout = new org.jdesktop.layout.GroupLayout(
				previewCanvas);
		previewCanvas.setLayout(previewCanvasLayout);
		previewCanvasLayout.setHorizontalGroup(previewCanvasLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(0, 212, Short.MAX_VALUE));
		previewCanvasLayout.setVerticalGroup(previewCanvasLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(0, 147, Short.MAX_VALUE));

		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(previewCanvas,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212,
				Short.MAX_VALUE));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(previewCanvas,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 147,
				Short.MAX_VALUE));

		cancelBtn.setText(ResourcesManager.bundle.getString("Cancel")); // NOI18N

		okBtn.setText(ResourcesManager.bundle.getString("OK")); // NOI18N
		okBtn.setEnabled(false);

		deleteButton.setText("ɾ��");

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
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				jScrollPane1,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				280,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								jPanel1,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.add(
																								jPanel2,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addContainerGap())
														.add(
																org.jdesktop.layout.GroupLayout.TRAILING,
																layout
																		.createSequentialGroup()
																		.add(
																				deleteButton,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				67,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED,
																				281,
																				Short.MAX_VALUE)
																		.add(
																				okBtn)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.UNRELATED)
																		.add(
																				cancelBtn)
																		.add(
																				16,
																				16,
																				16)))));

		layout.linkSize(new java.awt.Component[] { cancelBtn, deleteButton,
				okBtn }, org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																jScrollPane1,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																288,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																layout
																		.createSequentialGroup()
																		.add(
																				jPanel2,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				jPanel1,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(okBtn).add(
																cancelBtn).add(
																deleteButton))
										.addContainerGap()));
	}// </editor-fold>//GEN-END:initComponents

	private void statusComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_statusComboBoxItemStateChanged

		comboBoxChanged(evt);

	}// GEN-LAST:event_statusComboBoxItemStateChanged

	/**
	 * ״̬ѡ����ĺ�ֻ��ʾ��ǰ״̬������״̬����
	 * 
	 * @param evt
	 */
	private void comboBoxChanged(java.awt.event.ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			TempStatus status = null;
			for (int i = 0; i < ((DefaultComboBoxModel) statusComboBox
					.getModel()).getSize(); i++) {
				status = (TempStatus) ((DefaultComboBoxModel) statusComboBox
						.getModel()).getElementAt(i);
				EditorToolkit.setStyleProperty(status.getStatusGroupEle(),
						"visibility", "hidden");
			}
			status = (TempStatus) ((DefaultComboBoxModel) statusComboBox
					.getModel()).getSelectedItem();
			EditorToolkit.setStyleProperty(status.getStatusGroupEle(),
					"visibility", "");
			// ��������һ��document�Ա�ˢ��
			previewCanvas.setSVGDocument(previewCanvas.getSVGDocument());
		}
	}

	private void graphUnitTreeValueChanged(
			javax.swing.event.TreeSelectionEvent evt) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) evt.getPath()
				.getLastPathComponent();
		if (node.getUserObject() instanceof String) {
			okBtn.setEnabled(false);
			selectedSymbol = null;
			clearDetails();
			previewCanvas
					.setBackground(Constants.THUMBNAIL_RELEASED_BACKGROUND);

		} else {
			selectedSymbol = (NCIEquipSymbolBean) node.getUserObject();

			Document doc = Utilities.getSVGDocumentByContent(selectedSymbol);
			previewCanvas.setDocument(doc);
			previewCanvas
					.setBackground(selectedSymbol.isReleased() ? Constants.THUMBNAIL_RELEASED_BACKGROUND
							: Constants.THUMBNAIL_PERSONAL_BACKGROUND);
			// Document svgDoc = previewCanvas.getSVGDocument();
			try {
				NodeList statusNodes = Utilities.findNodes("//*[@"
						+ Constants.SYMBOL_STATUS + "]", doc
						.getDocumentElement());
				if (statusNodes != null && statusNodes.getLength() > 0) {
					DefaultComboBoxModel statusComboModel = (DefaultComboBoxModel) statusComboBox
							.getModel();
					statusComboModel.removeAllElements();
					int selectIndex = -1;
					for (int i = 0; i < statusNodes.getLength(); i++) {
						Element ele = (Element) statusNodes.item(i);
						// ���ü��Ľڵ�
						String visibleValue = EditorToolkit.getStyleProperty(
								ele, "visibility");
						if (visibleValue.equalsIgnoreCase("visible")
								|| visibleValue.equals("")) {
							selectIndex = i;
						}
						String status = ele
								.getAttribute(Constants.SYMBOL_STATUS);
						if (status != null && !status.equals("")) {
							statusComboModel.addElement(new TempStatus(status,
									ele));
						}
					}
					statusComboBox.setSelectedIndex(selectIndex);
					statusComboBox.setVisible(true);
				} else {
					statusComboBox.setVisible(false);
				}
			} catch (XPathExpressionException ex) {
				editor
						.getLogger()
						.log(
								editor
										.getModule(GraphUnitModuleAdapter.idGraphUnitManage),
								LoggerAdapter.ERROR, ex);
			}
			jScrollPane2.setAutoscrolls(false);
			// propertiesArea.setText("");
			propertiesArea.setText(Utilities
					.getGraphUnitPropertyDesc(selectedSymbol));
			propertiesArea.setCaretPosition(0);
			okBtn.setEnabled(true);
		}

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cancelBtn;
	private javax.swing.JButton deleteButton;
	private javax.swing.JTree graphUnitTree;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JButton okBtn;
	private org.apache.batik.swing.JSVGCanvas previewCanvas;
	private javax.swing.JTextArea propertiesArea;
	private javax.swing.JComboBox statusComboBox;

	// End of variables declaration//GEN-END:variables

	public javax.swing.JButton getCancelBtn() {
		return cancelBtn;
	}

	public javax.swing.JButton getOkBtn() {
		return okBtn;
	}

	public javax.swing.JTree getSymbolTree() {
		return graphUnitTree;
	}

	public javax.swing.JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * ״̬��ʱ�õ�javabean
	 */
	private class TempStatus {

		private Element statusGroupEle = null;
		private String status = null;

		public TempStatus() {
		}

		public TempStatus(String status, Element statusGroupEle) {
			this.status = status;
			this.statusGroupEle = statusGroupEle;
		}

		/**
		 * @return the statusGroupEle
		 */
		public Element getStatusGroupEle() {
			return statusGroupEle;
		}

		/**
		 * @param statusGroupEle
		 *            the statusGroupEle to set
		 */
		public void setStatusGroupEle(Element statusGroupEle) {
			this.statusGroupEle = statusGroupEle;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status
		 *            the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		public String toString() {
			return status;
		}
	}
}
