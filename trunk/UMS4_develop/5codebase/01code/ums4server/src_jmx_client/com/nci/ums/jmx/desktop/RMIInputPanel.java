/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RMIInputPanel.java
 *
 * Created on Sep 16, 2009, 3:42:16 PM
 */
package com.nci.ums.jmx.desktop;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * @author Qil.Wong
 */
public class RMIInputPanel extends javax.swing.JPanel {

	// ip地址
	private String ip = "127.0.0.1";
	// 端口
	private int port = 19820;
	// 错误提示
	private String errorInfo = "不合法的输入";
	// 控制台主类
	private DesktopConsole console;

	// 选择结果
	int result = -1;
	// 对话框
	JDialog dialog;

	/** Creates new form RMIInputPanel */
	public RMIInputPanel(DesktopConsole console) {
		initComponents();
		this.console = console;
		addressField.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				parse();
			}

			public void removeUpdate(DocumentEvent e) {
				parse();
			}

			public void changedUpdate(DocumentEvent e) {
				parse();
			}
		});
		okBtn.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/connect.png")));
		cancelBtn.setIcon(new ImageIcon(getClass().getResource(
				"/com/nci/ums/jmx/desktop/icons/close.png")));
		errorLabel.setVisible(false);
	}

	private void parse() {
		String input = addressField.getText().trim();
		if (input.equals("")) {
			showError(false, true);
			return;
		}
		String[] s = input.split(":");
		try {
			String[] ips = s[0].split("\\.");
			ip = s[0];
			for (int i = 0; i < 4; i++) {
				int t = Integer.parseInt(ips[i]);
				if (t > 255 || t < 0) {
					showError(true, false);
					return;
				}
			}
			port = Integer.parseInt(s[1]);
		} catch (Exception e) {
			showError(true, false);
			return;
		}
		showError(false, false);
	}

	/**
	 * 显示错误提示
	 * 
	 * @param error
	 *            错误提示
	 */
	private void showError(boolean error, boolean empty) {
		if (empty) {
			okBtn.setEnabled(!empty);
		} else {
			okBtn.setEnabled(!error);
		}
		if (error) {
			errorLabel.setText(errorInfo + ":" + addressField.getText());
		}
		errorLabel.setVisible(error);
		dialog.pack();
	}

	/**
	 * 获取IP
	 * 
	 * @return
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * 获取端口
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	public void reset() {
		errorLabel.setText("");
		errorLabel.setVisible(false);
		addressField.setText("127.0.0.1:19820");
	}

	public int showDialog() {
		if (dialog == null) {
			dialog = new JDialog(console, true);
			// dialog.setResizable(false);
			dialog.setTitle("连接UMS");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.getContentPane().add(this);
			dialog.addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					closeRmiInput();
				}
			});
		}
		parse();
		dialog.pack();
		dialog.setLocationRelativeTo(console);
		dialog.setVisible(true);
		return result;
	}

	public void closeRmiInput() {
		result = -1;
		dialog.setVisible(false);
		reset();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipLabel = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
        errorLabel = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();

        tipLabel.setText("请输入UMS地址和端口, <IP>:<port>");

        addressField.setText("127.0.0.1:19820");
        addressField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                addressFieldKeyReleased(evt);
            }
        });

        errorLabel.setForeground(new java.awt.Color(255, 51, 51));

        cancelBtn.setText("撤销");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        okBtn.setText("连接");
        okBtn.setEnabled(false);
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .add(addressField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .add(tipLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(tipLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(errorLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(okBtn)
                    .add(cancelBtn))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addressFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addressFieldKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            okBtnActionPerformed(null);
        }
    }//GEN-LAST:event_addressFieldKeyReleased

	private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelBtnActionPerformed
		closeRmiInput();
	}// GEN-LAST:event_cancelBtnActionPerformed

	private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okBtnActionPerformed
		result = 0;
		dialog.setVisible(false);
	}// GEN-LAST:event_okBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JButton okBtn;
    private javax.swing.JLabel tipLabel;
    // End of variables declaration//GEN-END:variables
}
