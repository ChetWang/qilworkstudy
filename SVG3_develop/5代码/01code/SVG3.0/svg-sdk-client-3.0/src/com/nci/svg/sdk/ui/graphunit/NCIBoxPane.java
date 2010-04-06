/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.sdk.ui.graphunit;

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

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;


/**
 *
 * @author Qil.Wong
 */
public class NCIBoxPane extends JPanel implements MouseInputListener, ActionListener {

    private static final int ANIMATION_FRAMES = 4;
    private static final int ANIMATION_INTERVAL = 10;
    private Insets highlightInsets = new Insets(3, 3, 3, 3);
    private Color topColor = Color.white;
    private Color bottomColor = new Color(222, 222, 222);
    private boolean paintGradient = true;
//    public static int mouseStatus = -1;
//    private Color borderColor = new Color(204, 129, 217);
    private Color borderColor = Color.red;
    public void setHighlightInsets(Insets insets) {
        highlightInsets = insets;
    }

    public Insets getHighlightInsets() {
        return highlightInsets;
    }
    private Component last;
    private Component current;
    private Color xorColor = new Color(12, 11, 32);

    public void setXorColor(Color xor) {
        xorColor = new Color(255 - xor.getRed(), 255 - xor.getGreen(), 255 - xor.getBlue());
        repaint();
    }

    /**
     * 获取边框内虚影颜色
     * @return
     */
    public Color getXorColor() {
        return new Color(255 - xorColor.getRed(), 255 - xorColor.getGreen(), 255 - xorColor.getBlue());
    }
    
    

    /**
     * 设置边框颜色
     * @param bc
     */
    public void setBorderColor(Color bc) {
        borderColor = bc;
        repaint();
    }

    /**
     * 获取边框颜色
     * @return
     */
    public Color getBorderColor() {
        return borderColor;
    }
    
    private boolean forceTranslucent;
    private Timer timer;
    private int frameIndex;
    private EditorAdapter editor;

    /** Creates a new instance of GlassBox */
    public NCIBoxPane(EditorAdapter editor) {
        addMouseListener(this);
        addMouseMotionListener(this);
        this.editor = editor;
    }

    //时钟响应，开始画动画
    public void actionPerformed(ActionEvent e) {
        frameIndex++;
        repaint();
        if (frameIndex >= ANIMATION_FRAMES)
            closeTimer();
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (comp instanceof JComponent && forceTranslucent && ((JComponent) comp).isOpaque()) {
            ((JComponent) comp).setOpaque(false);
        }
        addHandler(comp);
        super.addImpl(comp, constraints, index);
    }

    @Override
    public void remove(Component comp) {
        removeHandler(comp);
        super.remove(comp);
    }

    @Override
    public void paint(Graphics g) {
        if (isPaintGradient())
            paintGradient(g);
        super.paint(g);
        if (isAnimating()) {
            paintCurrentFrame(g);
        } else if (current != null) {
            paintCurrent(g);
        }
    }

    private Rectangle getIntermediateBounds(Rectangle lastBounds, Rectangle currentBounds) {
        double ratio = (double) frameIndex / (double) ANIMATION_FRAMES;
        int x = (int) (lastBounds.x + (currentBounds.x - lastBounds.x) * ratio);
        int y = (int) (lastBounds.y + (currentBounds.y - lastBounds.y) * ratio);
        int width = (int) (lastBounds.width + (currentBounds.width - lastBounds.width) * ratio);
        int height = (int) (lastBounds.height + (currentBounds.height - lastBounds.height) * ratio);
        return new Rectangle(x, y, width, height);
    }

    /**
     * 给当前帧绘制
     * @param g
     */
    private void paintCurrentFrame(Graphics g) {
        Rectangle lastBounds = getOuterBound(last);
        Rectangle currentBounds = getOuterBound(current);
        Rectangle intermediateBounds = getIntermediateBounds(lastBounds, currentBounds);
        paintBounds(g, intermediateBounds);
    }

    private Rectangle getOuterBound(Component comp) {
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
        Rectangle bounds = getOuterBound(current);
        paintBounds(g, bounds);
    }

    private void paintBounds(Graphics g, Rectangle bounds) {
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
        if (editor.getSymbolManager().getMouseStatus()!=Constants.MOUSE_PRESSED)
            paintBorders(e);      
    }

    /**
     * 显示动画在组件上画的边界
     * @param e
     */
    private void paintBorders(MouseEvent e) {
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
                timer = new Timer(ANIMATION_INTERVAL, this);
                timer.start();
            }
        }
    }

    /**
     * 关闭动画显示时钟
     */
    private void closeTimer() {
        if (isAnimating()) {
            timer.stop();
            frameIndex = 0;
            timer = null;
        }
    }

    /**
     * 返回是否是在动画显示
     * @return
     */
    private boolean isAnimating() {
        return timer != null && timer.isRunning();
    }

    public void mouseExited(MouseEvent e) {
    	
        Object obj = e.getSource();
        if (obj != this && obj instanceof Component) {
            Point p = new Point(((JComponent) obj).getLocationOnScreen().x + e.getX(), ((JComponent) obj).getLocationOnScreen().y + e.getY());
//            Point p = e.getLocationOnScreen();
            Rectangle bounds = getBounds();
            Point ap = getLocationOnScreen();
            bounds.x = ap.x;
            bounds.y = ap.y;

            if (obj == this && !bounds.contains(p)) {
                closeTimer();
                last = null;
                current = null;
                repaint();
            }
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
        GradientPaint gp = new GradientPaint(0, 0, getTopColor(), 0, getHeight(), getBottomColor());
        ((Graphics2D) g).setPaint(gp);
        g.fillRect(0, 0, getWidth(), getHeight());
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

//    /**
//     * 设置鼠标按下状态，如果是已经按下了，则动画不会响应
//     * @param pressed true为按下，false为松开
//     */
//    public static void setMousePressed(int pressedStatus) {
//        mouseStatus = pressedStatus;
//    }
}
