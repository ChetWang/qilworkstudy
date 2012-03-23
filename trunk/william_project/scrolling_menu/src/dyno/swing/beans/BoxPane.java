/*
 * GlassBox.java
 *
 * Created on 2007å¹?æœ?8æ—? ä¸‹åˆ1:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

/**
 * @author William Chen
 */
public class BoxPane extends JComponent implements MouseInputListener, ActionListener {

    private static final int ANIMATION_FRAMES = 10;
    private static final int ANIMATION_INTERVAL = 10;

    private Insets highlightInsets=new Insets(5, 5, 5, 5);

    private Color topColor=Color.white;
    private Color bottomColor=new Color(222, 222, 222);
    
    private boolean paintGradient;
    
    public void setHighlightInsets(Insets insets){
        highlightInsets=insets;
    }
    public Insets getHighlightInsets(){
        return highlightInsets;
    }
    private Component last;

    private Component current;

    private Color xorColor = new Color(12, 11, 32);

    public void setXorColor(Color xor){
        xorColor=new Color(255-xor.getRed(), 255-xor.getGreen(), 255-xor.getBlue());
        repaint();
    }
    
    public Color getXorColor(){
        return new Color(255-xorColor.getRed(), 255-xorColor.getGreen(), 255-xorColor.getBlue());
    }
    
    private Color borderColor = Color.black;
    
    public void setBorderColor(Color bc){
        borderColor=bc;
        repaint();
    }
    public Color getBorderColor(){
        return borderColor;
    }
    
    private boolean forceTranslucent;

    private Timer timer;

    private int frameIndex;

    /** Creates a new instance of GlassBox */
    public BoxPane() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        frameIndex++;
        repaint();
        if(frameIndex>=ANIMATION_FRAMES)
            closeTimer();        
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        if (comp instanceof JComponent && forceTranslucent && ((JComponent) comp).isOpaque()) {
            ((JComponent) comp).setOpaque(false);
        }
        addHandler(comp);
        super.addImpl(comp, constraints, index);
    }

    public void remove(Component comp) {
        removeHandler(comp);
        super.remove(comp);
    }
    
    public void paint(Graphics g) {
        if(isPaintGradient())
            paintGradient(g);
        super.paint(g);
        if(isAnimating()){
            paintCurrentFrame(g);
        }else if (current != null) {
            paintCurrent(g);
        }
    }
    private Rectangle getIntermediateBounds(Rectangle lastBounds, Rectangle currentBounds) {
        double ratio=(double)frameIndex/(double)ANIMATION_FRAMES;
        int x=(int)(lastBounds.x+(currentBounds.x-lastBounds.x)*ratio);
        int y=(int)(lastBounds.y+(currentBounds.y-lastBounds.y)*ratio);
        int width=(int)(lastBounds.width+(currentBounds.width-lastBounds.width)*ratio);
        int height=(int)(lastBounds.height+(currentBounds.height-lastBounds.height)*ratio);
        return new Rectangle(x, y, width, height);
    }
    private void paintCurrentFrame(Graphics g) {
        Rectangle lastBounds=getOuterBound(last);
        Rectangle currentBounds=getOuterBound(current);
        Rectangle intermediateBounds=getIntermediateBounds(lastBounds, currentBounds);
        paintBounds(g, intermediateBounds);
    }
    private Rectangle getOuterBound(Component comp){
        Rectangle bounds = comp.getBounds();
        Point mos = this.getLocationOnScreen();
        Point pos = comp.getLocationOnScreen();
        pos.translate(-mos.x, -mos.y);
        bounds.setLocation(pos);
        bounds.x -= highlightInsets.left;
        bounds.y -= highlightInsets.top;
        bounds.width += highlightInsets.right + highlightInsets.left;
        bounds.height += highlightInsets.bottom + highlightInsets.top;
        return bounds;
    }
    private void paintCurrent(Graphics g) {
        Rectangle bounds=getOuterBound(current);
        paintBounds(g, bounds);
    }
    private void paintBounds(Graphics g, Rectangle bounds){
        g.setXORMode(xorColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setPaintMode();
        g.setColor(borderColor);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);        
    }
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        Object obj = e.getSource();
        if (obj != this && obj instanceof Component) {
            if (current != null) {
                last = current;
            }
            current = (Component) obj;
            if (last == null) {
                repaint();
            } else {
                closeTimer();
                timer=new Timer(ANIMATION_INTERVAL, this);
                timer.start();
            }
        }
    }
    private void closeTimer(){
        if(isAnimating()){
            timer.stop();
            frameIndex=0;
            timer=null;
        }
    }
    private boolean isAnimating(){
        return timer!=null && timer.isRunning();
    }
    public void mouseExited(MouseEvent e) {
        Object obj=e.getSource();
        Point p=e.getLocationOnScreen();
        Rectangle bounds=getBounds();
        Point ap=getLocationOnScreen();
        bounds.x=ap.x;
        bounds.y=ap.y;
        
        if(obj==this && !bounds.contains(p)){
            closeTimer();
            last=null;
            current=null;
            repaint();
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    private void removeHandler(Component comp) {
        comp.removeMouseListener(this);
        comp.removeMouseMotionListener(this);
    }

    private void addHandler(Component comp) {
        comp.addMouseListener(this);
        comp.addMouseMotionListener(this);
    }

    private void paintGradient(Graphics g) {
        GradientPaint gp=new GradientPaint(0,0,getTopColor(), 0,getHeight(), getBottomColor());
        ((Graphics2D)g).setPaint(gp);
        g.fillRect(0,0,getWidth(),getHeight());
    }

    public Color getTopColor() {
        return topColor;
    }

    public void setTopColor(Color topColor) {
        this.topColor = topColor;
        repaint();
    }

    public Color getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(Color bottomColor) {
        this.bottomColor = bottomColor;
        repaint();
    }

    public boolean isPaintGradient() {
        return paintGradient;
    }

    public void setPaintGradient(boolean paintGradient) {
        this.paintGradient = paintGradient;
        repaint();
    }

}
