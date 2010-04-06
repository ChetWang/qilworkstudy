/*
 * SettingDialog.java
 *
 * Created on __DATE__, __TIME__
 */
package com.nci.ums.desktop;

import java.awt.Component;
import java.io.IOException;

import com.nci.ums.desktop.bean.SwingWorker;
import com.nci.ums.desktop.panels.CMPPSettingPanel;
import com.nci.ums.desktop.panels.DBSettingPanel;
import com.nci.ums.desktop.panels.EmailSettingPanel;
import com.nci.ums.desktop.panels.EuiSettingPanel;
import com.nci.ums.desktop.panels.HuaWeiCMSettingPanel;
import com.nci.ums.desktop.panels.MobileSettingPanel;
import com.nci.ums.desktop.panels.MsgReceiveSettingPanel;
import com.nci.ums.desktop.panels.Setting;
import com.nci.ums.desktop.panels.SmsSettingPannel;
import com.nci.ums.desktop.panels.WebServiceSettingPanel;
import com.nci.ums.util.Res;

/**
 * 
 * @author Qil.Wong
 */
public class SettingDialog extends javax.swing.JDialog implements HotKey {

    DBSettingPanel dbPanel = new DBSettingPanel();
    CMPPSettingPanel cmppPanel = new CMPPSettingPanel();
    MobileSettingPanel mobilePanel = new MobileSettingPanel();
    EmailSettingPanel emailPanel = new EmailSettingPanel();
    HuaWeiCMSettingPanel huaweiPanel = new HuaWeiCMSettingPanel();
    SmsSettingPannel smsPanel = new SmsSettingPannel();
    MsgReceiveSettingPanel receivePanel = new MsgReceiveSettingPanel();
    WebServiceSettingPanel wsPanel = new WebServiceSettingPanel();
    EuiSettingPanel euiPanel = new EuiSettingPanel();

    /** Creates new form SettingDialog */
    public SettingDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        okBtn.setText(ConsoleUtil.getLocaleString("OK"));
        closeBtn.setText(ConsoleUtil.getLocaleString("Close"));
        initSettingTabs();
        initSettingsAndHotKey();
        this.setTitle(ConsoleUtil.getLocaleString("Setting"));
        pack();
    }

    private void initSettingTabs() {
        settingTab.addTab(ConsoleUtil.getLocaleString("Database"), null, dbPanel,
                ConsoleUtil.getLocaleString("Database_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("CMPP"), null, cmppPanel,
                ConsoleUtil.getLocaleString("CMPP_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("Mobile"), null,
                mobilePanel, ConsoleUtil.getLocaleString("Mobile_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("Email"), null, emailPanel,
                ConsoleUtil.getLocaleString("Email_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("HuaWei"), null,
                huaweiPanel, ConsoleUtil.getLocaleString("Huawei_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("SMS_Signing"), null,
                smsPanel, ConsoleUtil.getLocaleString("SMS_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("Receiving"), null,
                receivePanel, ConsoleUtil.getLocaleString("Receiving_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("WebService"), null,
                wsPanel, ConsoleUtil.getLocaleString("WebService_tooltip"));
        settingTab.addTab(ConsoleUtil.getLocaleString("EnterpriseUser"), null, euiPanel, ConsoleUtil.getLocaleString("Enterprise_user_tip"));
    }

    public void addHotKey() {
        okBtn.setMnemonic(java.awt.event.KeyEvent.VK_O);
        closeBtn.setMnemonic(java.awt.event.KeyEvent.VK_C);
        ConsoleUtil.addEscapeHotKey(this);
    }

    private void implSettings() {
        SwingWorker worker = new SwingWorker() {

            public Object construct() {
                Component[] comps = settingTab.getComponents();
                for (int i = 0; i < comps.length; i++) {
                    if (comps[i] instanceof Setting) {
                        try {
                            ((Setting) comps[i]).implSetting();
                        } catch (IOException e) {
                        	Res.logExceptionTrace(e);
                        }
                    }
                }
                return null;
            }
            };
        worker.start();

    }

    private void initSettingsAndHotKey() {
        SwingWorker worker = new SwingWorker() {

            public Object construct() {
                addHotKey();
                Component[] comps = settingTab.getComponents();
                for (int i = 0; i < comps.length; i++) {
                    if (comps[i] instanceof Setting) {
                        try {
                            ((Setting) comps[i]).initSetting();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (comps[i] instanceof HotKey) {
                        ((HotKey) comps[i]).addHotKey();
                    }
                }
                return null;
            }
        };
        worker.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settingTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(349, Short.MAX_VALUE)
                .add(okBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeBtn))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {closeBtn, okBtn}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, okBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, closeBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, settingTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(settingTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {

        implSettings();

        this.setVisible(false);
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SettingDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okBtn;
    private javax.swing.JTabbedPane settingTab;
    // End of variables declaration//GEN-END:variables
}
