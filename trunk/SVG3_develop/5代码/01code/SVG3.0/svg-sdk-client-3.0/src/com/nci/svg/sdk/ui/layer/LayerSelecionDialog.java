/*
 * LayerSelecionDialog.java
 *
 * Created on 2008年7月10日, 下午1:44
 */
package com.nci.svg.sdk.ui.layer;

import fr.itris.glips.svgeditor.resources.ResourcesManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.EditorAdapter;

/**
 *
 * @author  Qil.Wong
 */
public class LayerSelecionDialog extends javax.swing.JDialog {

    /**
     * 所有可能过滤的特性面板
     */
    private ArrayList<LayerSelectionPanel> layerPanels = new ArrayList<LayerSelectionPanel>();
    /**
     * 编辑器对象
     */
    private EditorAdapter editor;
    private NodeList panelNodes;

    /** Creates new form LayerSelecionDialog */
    public LayerSelecionDialog(EditorAdapter editor, boolean modal) {
        super(editor.findParentFrame(), modal);
        this.editor = editor;
        initComponents();
        this.setLocationRelativeTo(editor.findParentFrame());
    }

    /**
     * 根据layers.xml中的配置，初始化TabbedPane
     */
    public void initTabbedPane() {
        layerTab.removeAll();
        layerPanels.clear();
        if (panelNodes == null)
            iniPanelNodes();
        String layerName = null;
        String layerCode = null;
        String layerNodeName = null;
        LayerSelectionPanel layerPanel = null;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                checkSelection();
            }
        };
        for (int i = 0; i < panelNodes.getLength(); i++) {
            layerName = ((Element) panelNodes.item(i)).getAttribute("name");
            layerCode = ((Element) panelNodes.item(i)).getAttribute("code");
            layerNodeName = ((Element) panelNodes.item(i)).getAttribute("nodeName");
            layerPanel = new LayerSelectionPanel(editor, layerName, layerCode,layerNodeName);
            layerPanel.getLayerTable().addMouseListener(mouseAdapter);
            layerTab.addTab(layerName, layerPanel);
            layerPanels.add(layerPanel);
        }
    }

    /**
     * 判断选择的事件，如果有选择的层，就可以进行层过滤，否则不允许点击按钮
     */
    private void checkSelection() {
        boolean hasSelectedRow = false;
        DefaultTableModel tableModel = null;
        for (LayerSelectionPanel layerPanel : layerPanels) {
            tableModel = layerPanel.getLayerTableModel();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((Boolean) tableModel.getValueAt(i, 1)) {
                    hasSelectedRow = true;
                    break;
                }
            }
            if (hasSelectedRow)
                break;
        }
        this.viewBtn.setEnabled(hasSelectedRow);
        this.okBtn.setEnabled(hasSelectedRow);
    }

    /**
     * 初始化能显示的、可以被过滤的层
     */
    private void iniPanelNodes() {
        Document doc = ResourcesManager.getXMLDocument("layers.xml");
        panelNodes = doc.getElementsByTagName("layer");
    }

    /**
     * 显示图层
     */
    private void showLayers() {
        editor.getHandlesManager().getCurrentHandle().getSelection().clearSelection();
        HashMap<String, ArrayList<String>> allSelectedLayersMap = new HashMap<String, ArrayList<String>>();
        HashMap<String, ArrayList<String>> fullLayersMap = new HashMap<String, ArrayList<String>>();
        for (LayerSelectionPanel selectionPanel : layerPanels) {
            allSelectedLayersMap.put(selectionPanel.getLayerTypeCode(), selectionPanel.getSelectLayers());
            fullLayersMap.put(selectionPanel.getLayerTypeCode(), selectionPanel.getAllLayers());
        }
        editor.getHandlesManager().getCurrentHandle().getCanvas().getSelectedLayerMap().putAll(allSelectedLayersMap);
//        editor.getLayerSelectionManager().setFullLayersMap(fullLayersMap);
//        editor.getLayerSelectionManager().showLayers(allSelectedLayersMap,textKeepCheckBox.isSelected());        
        editor.getLayerSelectionManager().showLayers(allSelectedLayersMap);
    }

    /**
     * 重置为初始状态
     */
    private void reset() {
        for (LayerSelectionPanel selectionPanel : layerPanels) {
            for (int i = 0; i < selectionPanel.getLayerTableModel().getRowCount(); i++) {
                selectionPanel.getLayerTableModel().setValueAt(false, i, 1);
            }
        }
//        textKeepCheckBox.setSelected(true);
        editor.getLayerSelectionManager().restore(editor.getHandlesManager().getCurrentHandle().getCanvas().getDocument());
        editor.getSvgSession().refreshCurrentHandleImediately();
        checkSelection();
        editor.getHandlesManager().getCurrentHandle().getCanvas().getSelectedLayerMap().clear();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        layerTab = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        viewBtn = new javax.swing.JButton();

        okBtn.setText(ResourcesManager.bundle.getString("OK")); // NOI18N
        okBtn.setEnabled(false);
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText(ResourcesManager.bundle.getString("Cancel")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("请选择需要显示的层：");

        resetBtn.setText("还原");
        resetBtn.setToolTipText("将过滤层重置为最初始状态");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        viewBtn.setText("查看");
        viewBtn.setToolTipText("不关闭当前窗口，查看过滤效果");
        viewBtn.setEnabled(false);
        viewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layerTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(viewBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 118, Short.MAX_VALUE)
                        .add(okBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelBtn))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .add(105, 105, 105)
                        .add(resetBtn)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelBtn, okBtn, resetBtn, viewBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(resetBtn))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layerTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelBtn)
                    .add(okBtn)
                    .add(viewBtn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
    showLayers();
    this.setVisible(false);
}//GEN-LAST:event_okBtnActionPerformed

private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed

    this.setVisible(false);
}//GEN-LAST:event_cancelBtnActionPerformed

private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
    reset();
}//GEN-LAST:event_resetBtnActionPerformed

private void viewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBtnActionPerformed
    showLayers();
}//GEN-LAST:event_viewBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane layerTab;
    private javax.swing.JButton okBtn;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton viewBtn;
    // End of variables declaration//GEN-END:variables
}
