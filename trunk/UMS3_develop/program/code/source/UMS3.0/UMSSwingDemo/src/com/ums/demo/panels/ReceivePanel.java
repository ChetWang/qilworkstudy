/*
 * ReceivePanel.java
 *
 * Created on 2007��11��29��, ����2:28
 */
package com.ums.demo.panels;

import com.nci.ums.v3.service.client.DuplicateServiceException;
import com.ums.demo.util.DemoReceiver;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
//import org.openswing.swing.client.InsertButton;

/**
 *
 * @author  Administrator
 */
public class ReceivePanel extends javax.swing.JPanel {

    /** Creates new form ReceivePanel */
    public ReceivePanel() {

        initComponents();
    }

    public void connectReceiveCenter() {
        try {
            DemoReceiver receiver = new DemoReceiver("resources/ums.props", this.receiveArea);
            receiver.startListening();
        } catch (IOException ex) {
            Logger.getLogger(ReceivePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReceivePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DuplicateServiceException ex) {
            Logger.getLogger(ReceivePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JTextArea getReceiveArea(){
        return receiveArea;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        receiveArea = new javax.swing.JTextArea();

        receiveArea.setColumns(20);
        receiveArea.setRows(5);
        jScrollPane1.setViewportView(receiveArea);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(209, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea receiveArea;
    // End of variables declaration//GEN-END:variables
}