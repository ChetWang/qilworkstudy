/*
 * DictPopup.java
 *
 * Created on 2007年4月11日, 上午9:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.border.Border;

/**
 *
 * @author rehte
 */
public class DictToolTip extends JToolTip implements MouseListener{
    private static Icon LOCKED;
    private static Icon UNLOCKED;
    static final int RIGHT_PAD=4;
    static final int TOP_PAD=4;
    static{
        initializeIcons();
    }
    private static void initializeIcons(){
        if(LOCKED==null || UNLOCKED==null){
            Class clazz=DictToolTip.class;
            String packageName=clazz.getPackage().getName();
            String packagePath=packageName.replace('.', '/');
            String locked_icon_path=packagePath+"/locked.png";
            String unlocked_icon_path=packagePath+"/unlocked.png";
            ClassLoader loader=clazz.getClassLoader();
            if(loader!=null){
                LOCKED=new ImageIcon(loader.getResource(locked_icon_path));
                UNLOCKED=new ImageIcon(loader.getResource(unlocked_icon_path));
            }
        }
    }
    private boolean locked;
    private ArrayList<ActionListener> listeners=new ArrayList<ActionListener>();
    public void addActionListener(ActionListener l){
        if(!listeners.contains(l))
            listeners.add(l);
    }
    public void removeActionListener(ActionListener l){
        if(listeners.contains(l))
            listeners.remove(l);
    }
    protected void fireActionPerformed(){
        ActionEvent evt=new ActionEvent(this, 0, "locking");
        for(ActionListener l:listeners)
            l.actionPerformed(evt);
    }
    /** Creates a new instance of DictPopup */
    public DictToolTip() {
        super();
        addMouseListener(this);
        Border b=getBorder();
        int w=RIGHT_PAD;
        initializeIcons();
        if(LOCKED!=null)
            w+=LOCKED.getIconWidth();
        if(b==null)
            b=BorderFactory.createEmptyBorder(0, 0, 0, w);
        else
            b=BorderFactory.createCompoundBorder(b, BorderFactory.createEmptyBorder(0, 0, 0, w));
        setBorder(b);
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
        repaint();
    }
    public void paint(Graphics g){
        super.paint(g);
        if(LOCKED==null || UNLOCKED==null)
            initializeIcons();
        Icon icon;
        if(locked){
            icon=LOCKED;
        }else{
            icon=UNLOCKED;
        }
        if(icon!=null){
            int width=getWidth();
            int height=getHeight();
            int w=icon.getIconWidth();
            int h=icon.getIconHeight();
            int x=width-w-RIGHT_PAD;
            int y=TOP_PAD;
            icon.paintIcon(this, g, x, y);
        }
    }
    private boolean isHit(MouseEvent e){
        if(LOCKED==null || UNLOCKED==null)
            initializeIcons();
        Icon icon;
        if(locked){
            icon=LOCKED;
        }else{
            icon=UNLOCKED;
        }
        if(icon!=null){
            int width=getWidth();
            int height=getHeight();
            int w=icon.getIconWidth();
            int h=icon.getIconHeight();
            int x=width-w-RIGHT_PAD;
            int y=TOP_PAD;
            int mx=e.getX();
            int my=e.getY();
            return mx>=x && mx<=x+w && my>=y && my<=y+h;
        }else
            return false;
    }
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
        if(isHit(e)){
            setLocked(!isLocked());
            fireActionPerformed();
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}
