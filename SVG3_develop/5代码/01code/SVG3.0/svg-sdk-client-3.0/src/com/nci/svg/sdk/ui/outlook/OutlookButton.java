/*
 * CurtainPaneButton.java
 *
 * Created on June 9, 2007, 11:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.nci.svg.sdk.ui.outlook;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 *
 * @author Qil.Wong
 */
public class OutlookButton extends JComponent{
    
    private ArrayList<ActionListener> listeners=new ArrayList<ActionListener>();
    private String text;
    private Icon icon;
    private float alignment=LEFT_ALIGNMENT;
    
    private Object UserObject = null;    
    
    /** Creates a new instance of CurtainPaneButton */
    public OutlookButton(){
        this(null, null);
    }
    public OutlookButton(String text, Icon icon) {
        this.text=text;
        this.icon=icon;

        setUI(new OutlookButtonUI());
    }
    public void addActionListener(ActionListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        if(listeners.contains(listener))
            listeners.remove(listener);
    }
    
    protected void fireActionPerformed(ActionEvent e){
        for(ActionListener listener:listeners)
            listener.actionPerformed(e);
    }
    public void setText(String text) {
        this.text=text;
        repaint();
    }
    
    public String getText() {
        return text;
    }
    
    public Icon getIcon() {
        return icon;
    }
    
    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    public float getAlignment() {
        return alignment;
    }
    
    public void setAlignment(float alignment) {
        this.alignment = alignment;
        repaint();
    }
	public Object getUserObject() {
		return UserObject;
	}
	public void setUserObject(Object userObject) {
		UserObject = userObject;
	}
    
}
