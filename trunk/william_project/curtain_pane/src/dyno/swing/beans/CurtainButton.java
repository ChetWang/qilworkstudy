/*
 * CurtainPaneButton.java
 *
 * Created on June 9, 2007, 11:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author William Chen
 */
public class CurtainButton extends JComponent{
    
    private ArrayList<ActionListener> listeners=new ArrayList<ActionListener>();
    private String text;
    private Icon icon;
    private float alignment=LEFT_ALIGNMENT;
    
    /** Creates a new instance of CurtainPaneButton */
    public CurtainButton(){
        this(null, null);
    }
    public CurtainButton(String text, Icon icon) {
        this.text=text;
        this.icon=icon;
        setUI(new CurtainButtonUI());
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
}
