/**
 * 类名：com.nci.svg.other
 * 创建人:yx.nci
 * 创建日期：2008-7-21
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.other;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.text.View;

public class PolarmanTabbedPaneUI extends MetalTabbedPaneUI {

    protected int minTabWidth = 20;

    public PolarmanTabbedPaneUI(){
        
    }
    
    public static ComponentUI createUI( JComponent x ) {
        return new PolarmanTabbedPaneUI();
    }
    
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        if(tabPlacement != JTabbedPane.LEFT && tabPlacement != JTabbedPane.RIGHT)
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        else {
            int h = 0;
            FontMetrics fm = tabPane.getFontMetrics(tabPane.getFont());
            Icon icon = tabPane.getIconAt(tabIndex);
            String title = tabPane.getTitleAt(tabIndex);
            if(title == null)
                title = "";
            h = fm.stringWidth(title) + textIconGap + 6;
            if(icon != null)
                h += icon.getIconWidth();
     
            return h;
        }
    }

    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics)  {
        if(tabPlacement != JTabbedPane.LEFT &&    tabPlacement != JTabbedPane.RIGHT)
            return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        else {
            int w = PolarmanTabbedPaneUI.this.minTabWidth;
            FontMetrics fm = tabPane.getFontMetrics(tabPane.getFont());
            if(w < fm.getHeight() + 4)
                w = fm.getHeight() + 4;
            
            Icon icon = tabPane.getIconAt(tabIndex);
            if(icon != null && w < icon.getIconHeight() + 6)
                    w =  icon.getIconHeight() + 6;
            
            return w;
        }
    }

    protected void layoutLabel(int tabPlacement, FontMetrics metrics,
            int tabIndex, String title, Icon icon, Rectangle tabRect,
            Rectangle iconRect, Rectangle textRect, boolean isSelected)  {
        
        if(tabPlacement != JTabbedPane.LEFT &&    tabPlacement != JTabbedPane.RIGHT) {
            super.layoutLabel(tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);
            return;
        }
        
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        View v = getTextViewForTab(tabIndex);
        if (v != null)  {
            tabPane.putClientProperty("html", v);
        }

        SwingUtilities.layoutCompoundLabel((JComponent) tabPane, metrics,
                title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER,
                SwingUtilities.CENTER, SwingUtilities.TRAILING, tabRect,
                iconRect, textRect, textIconGap);

        tabPane.putClientProperty("html", null);

        textRect.height = metrics.stringWidth(title);
        textRect.width = metrics.getHeight();
        iconRect.width = icon.getIconHeight();
        iconRect.height = icon.getIconWidth();
        
        textRect.x = (tabRect.width - metrics.getHeight()) / 2 + tabRect.x;
        textRect.y = (tabRect.height - textRect.height - iconRect.height - textIconGap) / 2 + tabRect.y;
        
        iconRect.x = (tabRect.width - iconRect.width) / 2 + tabRect.x;
        iconRect.y = textRect.y + textRect.height + textIconGap;
    }

    protected void paintIcon(Graphics g, int tabPlacement, int tabIndex, Icon icon, Rectangle iconRect, boolean isSelected)  {
        if(tabPlacement != JTabbedPane.LEFT &&    tabPlacement != JTabbedPane.RIGHT) {
            super.paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);
            return;
        }
            
        int a = icon.getIconHeight() > icon.getIconWidth() ? icon.getIconHeight() : icon.getIconWidth();
        BufferedImage memImage = new BufferedImage(a, a, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)memImage.getGraphics();
        AffineTransform at = AffineTransform.getTranslateInstance(a / 2, a / 2);
        at.rotate(1.5 * Math.PI);
        g2d.transform(at);
        icon.paintIcon(null, g2d, -icon.getIconWidth()/2, -icon.getIconHeight()/2);
        
        int dx1 = iconRect.x + (iconRect.width - icon.getIconHeight())/2;
        int dy1 = iconRect.y + (iconRect.height - icon.getIconWidth())/2;
        int dx2 = dx1 + icon.getIconHeight();
        int dy2 = dy1 + icon.getIconWidth();
        int sx1 = (a - icon.getIconHeight()) / 2;
        int sx2 = sx1 + icon.getIconHeight();
        int sy1 = (a - icon.getIconWidth()) / 2;
        int sy2 = sy1 + icon.getIconWidth();
            
        g.drawImage(memImage,
                dx1,dy1,dx2,dy2,
                sx1,sy1,sx2,sy2,
                tabPane);
    }

    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected)  {
        if(tabPlacement != JTabbedPane.LEFT &&    tabPlacement != JTabbedPane.RIGHT) {
            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
            return;
        }
        
        if(title == null || title.equals(""))
            return;
        
        int w = metrics.stringWidth(title);
        int h = metrics.getHeight();
        
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(tabPane.getForeground());
        g2d.setFont(font);

        AttributedString as = new AttributedString(title);
        AttributedCharacterIterator aci; 
        AffineTransform fontAT = new AffineTransform();
        fontAT.rotate(Math.toRadians(-90));
        Font fx = font.deriveFont(fontAT);
        as.addAttribute(TextAttribute.FONT, fx, 0, title.length());
        aci = as.getIterator();
        
        FontRenderContext frc = g2d.getFontRenderContext();
        LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
        
        float x = textRect.x + (textRect.width + h) / 2 - 2;
        float y = textRect.y + (textRect.height + w) / 2;
        
        lbm.setPosition(0);
        while (lbm.getPosition() < title.length()) {
            TextLayout tl = lbm.nextLayout(w-x);
            if (!tl.isLeftToRight()) {
                x = w - tl.getAdvance();
            }
            tl.draw(g2d, x, y += tl.getAscent());
            y += tl.getDescent() + tl.getLeading();
        }
    }
    
}
