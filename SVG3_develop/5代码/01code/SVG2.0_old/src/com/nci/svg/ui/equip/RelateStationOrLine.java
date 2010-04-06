/*
 * RelateStationOrLine.java
 *
 * Created on 2008年9月3日, 上午5:10
 */

package com.nci.svg.ui.equip;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.nci.svg.bean.EquipInfoBean;
import com.nci.svg.ui.graphunit.GraphUnitTreeCellRenderer;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 *
 * @author  yx.nci
 */
public class RelateStationOrLine extends javax.swing.JDialog {

    private Editor editor = null;
    private String strMapID = "";
    /** Creates new form RelateStationOrLine */
    public RelateStationOrLine(java.awt.Frame parent, boolean modal,Editor editor) {
        super(parent, modal);
        initComponents();
        this.editor = editor;
        textName.setEnabled(false);
        textPsmsID.setEnabled(false);
    }
    protected int saveMapID(String strPsmsID,String strMapID)
    {
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=save_relateMapID");
        String[][] param = new String[2][2];
        param[0][0] = "psmsID";
        param[0][1] = strPsmsID;
        param[1][0] = "mapID";
        param[1][1] = strMapID;
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
    protected String getMapID(String strPsmsID)
    {
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=get_relateMapID");
        final String[][] param = new String[1][2];
        param[0][0] = "psmsID";
        param[0][1] = strPsmsID;
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
    private DefaultMutableTreeNode root = null;
    private DefaultTreeModel graphTreeModel = null;
    protected void createTree(int nType,String strName)
    {
        this.treePsms.setModel(null);
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=get_relateList");
        String[][] param = new String[1][2];
        param[0][0] = "name";
        param[0][1] = strName;
        ArrayList list = null;
        try {
            list = (ArrayList) Utilities.communicateWithURL(
                    baseUrl.toString(), param);
            if(list != null && list.size() > 0)
            {
                root = new DefaultMutableTreeNode(ResourcesManager.bundle.getString("nci_equip_list"));
                graphTreeModel = new DefaultTreeModel(root);
                this.treePsms.setCellRenderer(new GraphUnitTreeCellRenderer());
                this.treePsms.setModel(graphTreeModel);
                DefaultMutableTreeNode typeNode = null;
                for(int i = 0;i < list.size();i++)
                {
                    EquipInfoBean bean = (EquipInfoBean)list.get(i);
                    typeNode = new DefaultMutableTreeNode(bean);
                    root.add(typeNode);
                    if(bean.getStrObjectID().equals(strMapID))
                    {
                        textName.setText(bean.getStrName());
                    }
                    
                }
                treePsms.expandRow(0);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ; 
    }
    public int initData(int nType,String strName,String codeType,String code)
    {
        String strPsmsID = "";
        if(codeType.equals("psms"))
        {
            strPsmsID = code;
        }
        else 
        {
            strPsmsID = getPSMSID(code);
        }
        if(strPsmsID == null || strPsmsID.length() == 0)
        {
            //提示先关联设备，然后退出
            return -1;
        }
        textPsmsID.setText(strPsmsID);
        strMapID = getMapID(strPsmsID);
        textName.setText("");
        createTree(nType,strName);
        return 0;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treePsms = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textPsmsID = new javax.swing.JTextField();
        textName = new javax.swing.JTextField();
        buttonOK = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        treePsms.setModel(null);
        treePsms.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treePsmsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treePsms);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("设备编号");

        jLabel2.setText("关联编号");

        buttonOK.setText("确定");
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        buttonCancel.setText("取消");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2))
                        .add(43, 43, 43)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(textName)
                            .add(textPsmsID, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(buttonOK)
                        .add(18, 18, 18)
                        .add(buttonCancel)
                        .add(18, 18, 18)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(textPsmsID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(textName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonCancel)
                    .add(buttonOK))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
// TODO add your handling code here:
    
    this.setVisible(false);
}//GEN-LAST:event_buttonCancelActionPerformed

private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
// TODO add your handling code here:
    int nRet = saveMapID(textPsmsID.getText(),strMapID);
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
    this.setVisible(false);
}//GEN-LAST:event_buttonOKActionPerformed

private void treePsmsValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treePsmsValueChanged
// TODO add your handling code here:
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePsms.getLastSelectedPathComponent();
    if(node != null && node.getUserObject() instanceof EquipInfoBean)
    {
        EquipInfoBean bean = (EquipInfoBean)node.getUserObject();
        textName.setText(bean.getStrName());
        strMapID = bean.getStrObjectID();
    }
}//GEN-LAST:event_treePsmsValueChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RelateStationOrLine dialog = new RelateStationOrLine(new javax.swing.JFrame(), true,null);
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
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField textName;
    private javax.swing.JTextField textPsmsID;
    private javax.swing.JTree treePsms;
    // End of variables declaration//GEN-END:variables

}
