/*
 * UMSTestDemo.java
 *
 * Created on 2007锟斤拷11锟斤拷5锟斤拷, 锟斤拷锟斤拷7:12
 */
package com.ums.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.nci.ums.channel.inchannel.email.EmailMsgPlus;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.AppInfoV3;
import com.nci.ums.v3.service.impl.UMS3_MsgSendStub;
import com.thoughtworks.xstream.XStream;
import com.ums.demo.panels.ContactPanel;
import com.ums.demo.util.UUMBean;

/**
 *
 * @author  Qil.Wong
 */
public class UMSTestDemo extends javax.swing.JFrame {

    private AppInfoV3 app;
    private ContactPanel contactPanel;
    private UMS3_MsgSendStub stub;
    private XStream xstream;
    private boolean webServiceFlag = false;
	private static String appBinDir = System.getProperty("user.dir");

   
    public UMSTestDemo() {
        initComponents();
        contactPanel = new ContactPanel(false);
        this.jSplitPane1.setLeftComponent(contactPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon/logo.png"));
        this.setIconImage(icon.getImage());
    }

    /**
     * 锟斤拷菘锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷墓锟斤拷旌拷锟�
     * @param app 锟斤拷录锟斤拷应锟矫诧拷锟斤拷
     */
    public UMSTestDemo(AppInfoV3 app) {
//        try {
//            UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
            initComponents();
            contactPanel = new ContactPanel(true);
            contactPanel.getAddBtn().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    addContactByFilter();
                }
            });
            contactPanel.getAddrTable().addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        addContactByAddrTable();
                    }
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
            this.contactPanel.getAddressTypeCombo().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    addressTypeAction();
                }
            });
            this.msgDefinePanel.getSendBtn().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    sendMsg();
                }
            });
            this.msgDefinePanel.setApp(app);
            this.msgDefinePanel.iniAppServices();
            this.msgDefinePanel.iniOutMediaChannel();
            this.msgDefinePanel.iniFeeServiceNOCombo();
            this.jSplitPane1.setLeftComponent(contactPanel);
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon/logo.png"));
            this.setIconImage(icon.getImage());
            this.app = app;
            iniXStream();
            iniWebService();
        } 

    public void addContactByFilter() {
        String contactor = contactPanel.getAutoCompletionField().getText().trim();
        String s = msgDefinePanel.getReceiversField().getText();
        s = s + contactor + ";";
        msgDefinePanel.getReceiversField().setText(s);
    }

    public void addContactByAddrTable() {
        String contactor = contactPanel.getAddrTable().getModel().getValueAt(contactPanel.getAddrTable().getSelectedRow(), 1).toString();
        String s = msgDefinePanel.getReceiversField().getText();
        s = s + contactor + ";";
        msgDefinePanel.getReceiversField().setText(s);
    }

    public void addressTypeAction() {
        switch (this.contactPanel.getAddressTypeCombo().getSelectedIndex()) {
            case 0:
                contactPanel.clearTable();
                this.msgDefinePanel.setReceiverIDType(-1);
                this.msgDefinePanel.getContentModeCombo().setEnabled(true);
                break;
            case 1:
                contactPanel.showAddr(UUMBean.SHOW_TYPE_MOBLE);
                this.msgDefinePanel.getContentModeCombo().setEnabled(true);
                this.msgDefinePanel.setReceiverIDType(Participant.PARTICIPANT_ID_MOBILE);
                break;
            case 2:
                contactPanel.showAddr(UUMBean.SHOW_TYPE_EMAIL);
                this.msgDefinePanel.getContentModeCombo().setSelectedIndex(0);
                this.msgDefinePanel.getContentModeCombo().setEnabled(false);
                this.msgDefinePanel.setReceiverIDType(Participant.PARTICIPANT_ID_EMAIL);
                break;
            case 3:
                contactPanel.showAddr(UUMBean.SHOW_TYPE_LCS);
                this.msgDefinePanel.getContentModeCombo().setSelectedIndex(0);
                this.msgDefinePanel.getContentModeCombo().setEnabled(false);
                this.msgDefinePanel.setReceiverIDType(Participant.PARTICIPANT_ID_LCS);
                break;
        }
    }

    public void sendMsg() {
        int times = 1;
        UMS3_MsgSendStub.SendWithAck axisSend = new UMS3_MsgSendStub.SendWithAck();
        if (!msgDefinePanel.getSendTimesField().getText().equals("")) {
            times = new Integer(msgDefinePanel.getSendTimesField().getText()).intValue();
        }
        final BasicMsg basic = msgDefinePanel.getBasicMsg();

        for (int i = 0; i < times; i++) {
            try {
                BasicMsg[] basics = new BasicMsg[1];
//                basic.getMsgContent().setContent("xxx" + i);
                for (int n = 0; n < 1; n++) {
                    basics[n] = basic;
                }
                if (getStub() == null) {
                    iniWebService();
                }
//                SendWithoutAck axisSend = new SendWithoutAck();

                axisSend.setAppID(app.getAppID());
                axisSend.setPassword(app.getPassword());
                String xml = xstream.toXML(basics);
                axisSend.setBasicMsgsXML(xml);
                getStub().sendWithAck(axisSend);
//                    String result = response.get_return();
//                    msgDefinePanel.getResultLabel().setText(result);
            } catch (RemoteException ex) {
                Logger.getLogger(UMSTestDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void iniXStream() {
        xstream = new XStream();
        xstream.setClassLoader(getClass().getClassLoader());
        xstream.alias("UMSMsg", UMSMsg.class);
        xstream.alias("BasicMsg", BasicMsg.class);
        xstream.alias("MsgAttachment", MsgAttachment.class);
        xstream.alias("MsgContent", MsgContent.class);
        xstream.alias("Participant", Participant.class);
        xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
    }

    private void iniWebService() {
        InputStream in = null;
        try {
            String endpoint = "";
            Properties props = new Properties();
            in = new FileInputStream(new File(appBinDir+"/conf/wsdl.props"));
            props.load(in);
            in.close();
            endpoint = props.getProperty("wsdl_msg_send");
            setStub(new UMS3_MsgSendStub(endpoint));
            this.webServiceFlag = true;
//            System.out.println("Web Service Connection has been established!");
        } catch (IOException ex) {
            Logger.getLogger(UMSTestDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                if (!webServiceFlag) {
                    JOptionPane.showConfirmDialog(this, "Can NOT establish web service connection.", "WebService Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                    this.setTitle("UMS3.0 Test Demo -- Web Service Error!");
                }
            } catch (IOException ex) {
                Logger.getLogger(UMSTestDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        AppInfoV3 app = new AppInfoV3();
        app.setAppID("1");
        app.setPassword("monk");
        UMSTestDemo demo = new UMSTestDemo(app);
        LoginFrame.centerOnScreen(demo);
        demo.setVisible(true);
        demo.msgDefinePanel.getReceivePanel().connectReceiveCenter();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        msgDefinePanel = new com.ums.demo.panels.MsgDefinePanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        settingMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("UMS3.0 Test Demo");

        jSplitPane1.setDividerLocation(190);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setResizeWeight(0.3);
        jSplitPane1.setRightComponent(msgDefinePanel);

        jMenu1.setText("File");

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tools");

        settingMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        settingMenuItem.setText("Setting");
        settingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(settingMenuItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
}//GEN-LAST:event_exitMenuItemActionPerformed

    private void settingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingMenuItemActionPerformed
        SettingDialog dialog = new SettingDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
}//GEN-LAST:event_settingMenuItemActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSplitPane jSplitPane1;
    private com.ums.demo.panels.MsgDefinePanel msgDefinePanel;
    private javax.swing.JMenuItem settingMenuItem;
    // End of variables declaration//GEN-END:variables
    public UMS3_MsgSendStub getStub() {
        return stub;
    }

    public void setStub(UMS3_MsgSendStub stub) {
        this.stub = stub;
    }
}
