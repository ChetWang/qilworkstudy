/*
 * HyperLink.java
 *
 * Created on 2007��3��26��, ����11:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.nci.svg.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.event.MouseInputListener;

public class HyperLinkLabel extends JLabel implements MouseInputListener {

    private String hyper_text;
    private ArrayList listeners = new ArrayList();

    public HyperLinkLabel() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public HyperLinkLabel(String label) {
        super(label);
        this.hyper_text = label;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setText(String text) {
        hyper_text = text;
        super.setText("<html><body>" + text + "</body></html>");
    }

    public void addActionListener(ActionListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void removeActionListener(ActionListener l) {
        if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    protected void fireActionPerformed(ActionEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((ActionListener) listeners.get(i)).actionPerformed(e);
        }
//        for(ActionListener listener:listeners)
//            listener.actionPerformed(e);
    }

    public void mouseClicked(MouseEvent e) {
        if (super.isEnabled()) {
            fireActionPerformed(new ActionEvent(this, 0, "hyper linkage action"));
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (this.isEnabled()) {
            super.setText("<html><body><div color=\"#0000ff\"><u>" + hyper_text + "</u></div></body></html>");
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void mouseExited(MouseEvent e) {
        if (this.isEnabled()) {
            super.setText("<html><body>" + hyper_text + "</body></html>");
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void setEnabled(boolean flag) {
        super.setEnabled(flag);
        if (flag == false) {
            super.setText(hyper_text);
            super.setForeground(Color.GRAY);
        } else {
            super.setForeground(Color.BLACK);
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

}
