/*
 * EquipClassPanel.java
 *
 * Created on 2008年8月18日, 上午10:10
 */

package com.nci.svg.equip;

import javax.swing.JDialog;

import org.w3c.dom.Element;

import fr.itris.glips.svgeditor.Editor;

/**
 *
 * @author  yx.nci
 */
public class EquipClassPanel extends javax.swing.JPanel {

    private Element element = null;
    private JDialog dialog = null;
    private Editor editor = null;
    /** Creates new form EquipClassPanel */
    public EquipClassPanel(Element element,JDialog dialog,Editor editor) {
        this.dialog = dialog;
        this.element = element;
        this.editor = editor;
        initComponents();
    }
    
    public void initData()
    {
        String strClass = element.getAttribute("class");
        listClass.setSelectedValue(strClass, true);
    }
    
    private void saveData()
    {
        String strClass = (String)listClass.getSelectedValue();
        element.setAttribute("class",strClass);
        editor.getSvgSession().refreshCurrentHandleImediately();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listClass = new javax.swing.JList();
        buttonOK = new javax.swing.JButton();
        buttonCanel = new javax.swing.JButton();

        listClass.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "KV500", "KV330", "KV220", "KV110", "KV35", "KV10", "KV6", "V380", "KV18", "KV20", "kv35", "kv220", "kv500", "kv110", "kv10", "KV中性点", "un", "choice", "ch", "l35", "l220", "l500", "l110", "l10", "line", "ared", "ablue", "高亮", "红色", "黄色", "蓝色", "绿色", "黑色", "白色", "紫色", "灰色", "中性点" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listClass);

        buttonOK.setText("确定");
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        buttonCanel.setText("取消");
        buttonCanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCanelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 290, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonOK)
                    .add(buttonCanel))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(22, 22, 22)
                        .add(buttonOK)
                        .add(18, 18, 18)
                        .add(buttonCanel)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
// TODO add your handling code here:
    saveData();
    dialog.setVisible(false);
}//GEN-LAST:event_buttonOKActionPerformed

private void buttonCanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCanelActionPerformed
// TODO add your handling code here:
    dialog.setVisible(false);
}//GEN-LAST:event_buttonCanelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCanel;
    private javax.swing.JButton buttonOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listClass;
    // End of variables declaration//GEN-END:variables

    public void setElement(Element element) {
        this.element = element;
        initData();
    }

}
