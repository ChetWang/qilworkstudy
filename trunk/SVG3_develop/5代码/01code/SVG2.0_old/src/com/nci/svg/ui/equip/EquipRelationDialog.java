/*
 * EquipRelationDialog.java
 *
 * Created on 2008年9月1日, 上午7:59
 */

package com.nci.svg.ui.equip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.nci.svg.bean.EquipInfoBean;
import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.ui.graphunit.GraphUnitTreeCellRenderer;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 *
 * @author  yx.nci
 */
public class EquipRelationDialog extends javax.swing.JDialog {

    private Editor editor = null;
    private String strScadaID="";
    private String strPsmsID="";
    /**
     * 树显示窗口
     */
    private boolean bTreeFlag = false;
    /** Creates new form EquipRelationDialog */
    public EquipRelationDialog(java.awt.Frame parent, boolean modal,Editor editor) {
        super(parent, modal);
        initComponents();
        this.editor = editor;
    }
    
    /**根据输入的信息初始化对话框
     * @param nType：类型，0为站内，1为站外
     * @param strName：当nType＝＝0时，为场站名称，当nType==1时，为线路名称
     * @param strCimType:设备类型
     * @param strScadaID:设备编号
     */
    public void initData(int nType,String strName,String strCimType,String strScadaID)
    {
        TextSCADAID.setText(strScadaID);
        TextSCADAID.setEnabled(false);
        //先获取已存在的PSMS编号
        String strResult = getPSMSID(strScadaID);
        TextPSMSID.setText(strResult);
        //构建该场站或线路该设备类型的树
        createTree(nType,strName,strCimType);
        bTreeFlag = false;
        treePanel.setVisible(false);
        this.pack();
        
    }
    private DefaultMutableTreeNode root = null;
    private DefaultTreeModel graphTreeModel = null;
    protected void createTree(int nType,String strName,String strCimType)
    {
        this.jTree1.setModel(null);
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=hn_equipList");
        String[][] param = new String[3][2];
        param[0][0] = "type";
        param[0][1] = String.valueOf(nType);
        param[1][0] = "name";
        param[1][1] = strName;
        param[2][0] = "equipType";
        param[2][1] = strCimType;
        ArrayList list = null;
        try {
            list = (ArrayList) Utilities.communicateWithURL(
                    baseUrl.toString(), param);
            if(list != null && list.size() > 0)
            {
                root = new DefaultMutableTreeNode(ResourcesManager.bundle.getString("nci_equip_list"));
                graphTreeModel = new DefaultTreeModel(root);
                this.jTree1.setCellRenderer(new GraphUnitTreeCellRenderer());
                this.jTree1.setModel(graphTreeModel);
                DefaultMutableTreeNode typeNode = null;
                for(int i = 0;i < list.size();i++)
                {
                    EquipInfoBean bean = (EquipInfoBean)list.get(i);
                    typeNode = new DefaultMutableTreeNode(bean);
                    root.add(typeNode);
                    
                }
                jTree1.expandRow(0);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ; 
    }
    

    protected int saveEquipMap(String strScadaID,String strPsmsID)
    {
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=hn_equipManualMapSave");
        String[][] param = new String[2][2];
        param[0][0] = "scadaID";
        param[0][1] = strScadaID;
        param[1][0] = "psmsID";
        param[1][1] = strPsmsID;
        String strResult = null;
        try {
            strResult = (String) Utilities.communicateWithURL(
                    baseUrl.toString(), param);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            strResult = "-1";
        }
        return Integer.parseInt(strResult);
    }
    
    protected String getPSMSID(String strScadaID)
    {
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=hn_equipManualMapSearch");
        final String[][] param = new String[1][2];
        param[0][0] = "scadaID";
        param[0][1] = strScadaID;
        String strResult = null;
        try {
            strResult = (String) Utilities.communicateWithURL(
                    baseUrl.toString(), param);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            strResult = "-1";
        }
        if(strResult.equals("-1"))
            strResult="";
        return strResult;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TextSCADAID = new javax.swing.JTextField();
        TextPSMSID = new javax.swing.JTextField();
        ButtonOK = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        ButtonCanel = new javax.swing.JButton();
        treePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("SCADA编号");

        jLabel2.setText("PSMS编号");

        ButtonOK.setText("确定");
        ButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonOKActionPerformed(evt);
            }
        });

        jButton2.setText("查找");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        ButtonCanel.setText("取消");
        ButtonCanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCanelActionPerformed(evt);
            }
        });

        jTree1.setModel(null);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        org.jdesktop.layout.GroupLayout treePanelLayout = new org.jdesktop.layout.GroupLayout(treePanel);
        treePanel.setLayout(treePanelLayout);
        treePanelLayout.setHorizontalGroup(
            treePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(treePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        treePanelLayout.setVerticalGroup(
            treePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(treePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 237, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(257, Short.MAX_VALUE)
                .add(ButtonOK)
                .add(18, 18, 18)
                .add(ButtonCanel)
                .add(34, 34, 34))
            .add(layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(TextPSMSID)
                    .add(TextSCADAID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 273, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(treePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(layout.createSequentialGroup()
                        .add(TextSCADAID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(TextPSMSID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2)
                            .add(jButton2))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(treePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ButtonCanel)
                    .add(ButtonOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
    if(bTreeFlag)
    {
        bTreeFlag = false;
        treePanel.setVisible(false);
        pack();
    }
    else
    {
        bTreeFlag = true;
        treePanel.setVisible(true);
        pack();
    }
}//GEN-LAST:event_jButton2ActionPerformed

private void ButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonOKActionPerformed
// TODO add your handling code here:
    if(!TextPSMSID.getText().equals(this.strPsmsID))
    {
        int nRet = saveEquipMap(TextSCADAID.getText(),TextPSMSID.getText());
        if(nRet == 0)//保存成功
        {
            System.out.println("关联成功");
        }
        else
        {
            JOptionPane.showConfirmDialog(editor.findParentFrame(),
                    "关联过程中发生异常，关联失败！", "提示", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
    }
    this.setVisible(false);
}//GEN-LAST:event_ButtonOKActionPerformed

private void ButtonCanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCanelActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
}//GEN-LAST:event_ButtonCanelActionPerformed

private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
// TODO add your handling code here:
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
    if(node != null && node.getUserObject() instanceof EquipInfoBean)
    {
        EquipInfoBean bean = (EquipInfoBean)node.getUserObject();
        TextPSMSID.setText(bean.getStrObjectID());
    }
}//GEN-LAST:event_jTree1ValueChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EquipRelationDialog dialog = new EquipRelationDialog(new javax.swing.JFrame(), true,null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCanel;
    private javax.swing.JButton ButtonOK;
    private javax.swing.JTextField TextPSMSID;
    private javax.swing.JTextField TextSCADAID;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JPanel treePanel;
    // End of variables declaration//GEN-END:variables

}
