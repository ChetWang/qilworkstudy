/*
 * LayerDocSaveDialog.java
 *
 * Created on July 14, 2008, 11:08 AM
 */

package com.nci.svg.ui.layer;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 *
 * @author  Qil.Wong
 */
public class LayerDocSaveDialog extends javax.swing.JDialog {
    
    public static final int SAVETYPE_ORIGINAL_SVG = 0;
    
    public static final int SAVETYPE_FILTERED_SVG = 1;
    
    public static final int SAVETYPE_CANCEL = -1;
    
    private int saveType = SAVETYPE_CANCEL;

    /** Creates new form LayerDocSaveDialog */
    public LayerDocSaveDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
        this.setTitle(ResourcesManager.bundle.getString("nci_optionpane_infomation_title"));
    }
    
    public int getSaveType(){
        return saveType;
    }
     

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saveOriginBtn = new javax.swing.JButton();
        saveFilteredBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        saveOriginBtn.setText("保存原图");
        saveOriginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOriginBtnActionPerformed(evt);
            }
        });

        saveFilteredBtn.setText("保存过滤图");
        saveFilteredBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFilteredBtnActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("已被层过滤"));

        jLabel1.setText("当前文档已被层过滤!");

        jLabel2.setText("保存原图还是保存过滤后的图？");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(saveOriginBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(saveFilteredBtn))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {saveFilteredBtn, saveOriginBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(13, 13, 13)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveFilteredBtn)
                    .add(saveOriginBtn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void saveOriginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOriginBtnActionPerformed
// TODO add your handling code here:
    saveType = SAVETYPE_ORIGINAL_SVG;
    this.dispose();
}//GEN-LAST:event_saveOriginBtnActionPerformed

private void saveFilteredBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFilteredBtnActionPerformed
// TODO add your handling code here:
    saveType = SAVETYPE_FILTERED_SVG;
    this.dispose();
}//GEN-LAST:event_saveFilteredBtnActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LayerDocSaveDialog dialog = new LayerDocSaveDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton saveFilteredBtn;
    private javax.swing.JButton saveOriginBtn;
    // End of variables declaration//GEN-END:variables

}
