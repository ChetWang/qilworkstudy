/*
 * ProxyGraphics.java
 *
 * Created on 2007年4月2日, 下午11:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author rehte
 */
public class ProxyGraphics extends Graphics2D {
    private Graphics2D proxy;
    private ArrayList<Text> texts;
    private StringBuilder builder;
    /** Creates a new instance of ProxyGraphics */
    public ProxyGraphics(Graphics2D g2d) {
        proxy = g2d;
        texts = new ArrayList<Text>();
        builder=new StringBuilder();
    }
    public void recycle(Graphics2D g2d){
        proxy=g2d;
        texts.clear();
        builder.setLength(0);
    }
    public void setTexts(ArrayList<Text> texts) {
        this.texts = texts;
    }
    public void setBuilder(StringBuilder b){
        this.builder=b;
    }
    public ArrayList<Text> getTexts() {
        return texts;
    }
    
    public void draw(Shape s) {
        proxy.draw(s);
    }
    
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return proxy.drawImage(img, xform, obs);
    }
    
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        proxy.drawImage(img, op, x, y);
    }
    
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        proxy.drawRenderedImage(img, xform);
    }
    
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        proxy.drawRenderableImage(img, xform);
    }
    
    public void drawString(String str, int x, int y) {
        if (str == null || str.length() == 0)
            return;
        AffineTransform transform=proxy.getTransform();
        if(transform!=null){
            Point orginal=new Point();
            transform.transform(new Point(x, y), orginal);
            analyseText(orginal.x, orginal.y, str);
        }else
            analyseText(x, y, str);
        proxy.drawString(str, x, y);
    }
    private boolean isLatinLetter(char c){
        return  isUpperLetter(c)
        || isLowerLetter(c);
    }
    private boolean isUpperLetter(char c){
        return c>='A' && c <= 'Z';
    }
    private boolean isLowerLetter(char c){
        return c>='a' && c <= 'z';
    }
    private void analyseText(int x, int y, String str) {
        int current = 0;
        int total=str.length();
        while (current < total) {
            char c = str.charAt(current++);
            if (isLatinLetter(c)) {
                int start=current-1;
                builder.setLength(0);
                builder.append(c);
                if(current!=total){
                    c=str.charAt(current++);
                    if(isLowerLetter(c)){
                        while(isLowerLetter(c)){
                            builder.append(c);
                            if (current != total)
                                c = str.charAt(current++);
                            else
                                break;
                        }
                        if(isUpperLetter(c))
                            current--;
                    }else if(isUpperLetter(c)){
                        while(isUpperLetter(c)){
                            builder.append(c);
                            if (current != total)
                                c = str.charAt(current++);
                            else
                                break;
                        }
                        if(isLowerLetter(c)){
                            current-=2;
                            builder.setLength(builder.length()-1);
                        }
                    }
                }
                if(builder.length()>0){
                    String s = builder.toString();
                    Text text = new Text(s);
                    FontMetrics metrics=proxy.getFontMetrics();
                    text.setBounds(
                            x+metrics.stringWidth(str.substring(0, start)),
                            y - metrics.getAscent(),
                            metrics.stringWidth(s),
                            metrics.getHeight());
                    texts.add(text);
                }
            }
        }
    }
    
    public void drawString(String str, float x, float y) {
        proxy.drawString(str, x, y);
    }
    
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        proxy.drawString(iterator, x, y);
    }
    
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        proxy.drawString(iterator, x, y);
    }
    
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        proxy.drawGlyphVector(g, x, y);
    }
    
    public void fill(Shape s) {
        proxy.fill(s);
    }
    
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return proxy.hit(rect, s, onStroke);
    }
    
    public GraphicsConfiguration getDeviceConfiguration() {
        return proxy.getDeviceConfiguration();
    }
    
    public void setComposite(Composite comp) {
        proxy.setComposite(comp);
    }
    
    public void setPaint(Paint paint) {
        proxy.setPaint(paint);
    }
    
    public void setStroke(Stroke s) {
        proxy.setStroke(s);
    }
    
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        proxy.setRenderingHint(hintKey, hintValue);
    }
    
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return proxy.getRenderingHint(hintKey);
    }
    
    public void setRenderingHints(Map<?, ?> hints) {
        proxy.setRenderingHints(hints);
    }
    
    public void addRenderingHints(Map<?, ?> hints) {
        proxy.addRenderingHints(hints);
    }
    
    public RenderingHints getRenderingHints() {
        return proxy.getRenderingHints();
    }
    
    public void translate(int x, int y) {
        proxy.translate(x, y);
    }
    
    public void translate(double tx, double ty) {
        proxy.translate(tx, ty);
    }
    
    public void rotate(double theta) {
        proxy.rotate(theta);
    }
    
    public void rotate(double theta, double x, double y) {
        proxy.rotate(theta, x, y);
    }
    
    public void scale(double sx, double sy) {
        proxy.scale(sx, sy);
    }
    
    public void shear(double shx, double shy) {
        proxy.shear(shx, shy);
    }
    
    public void transform(AffineTransform Tx) {
        proxy.transform(Tx);
    }
    
    public void setTransform(AffineTransform Tx) {
        proxy.setTransform(Tx);
    }
    
    public AffineTransform getTransform() {
        return proxy.getTransform();
    }
    
    public Paint getPaint() {
        return proxy.getPaint();
    }
    
    public Composite getComposite() {
        return proxy.getComposite();
    }
    
    public void setBackground(Color color) {
        proxy.setBackground(color);
    }
    
    public Color getBackground() {
        return proxy.getBackground();
    }
    
    public Stroke getStroke() {
        return proxy.getStroke();
    }
    
    public void clip(Shape s) {
        proxy.clip(s);
    }
    
    public FontRenderContext getFontRenderContext() {
        return proxy.getFontRenderContext();
    }
    
    public Graphics create() {
        ProxyGraphics pg = new ProxyGraphics((Graphics2D) proxy.create());
        pg.setTexts(this.texts);
        pg.setBuilder(this.builder);
        return pg;
    }
    
    public Color getColor() {
        return proxy.getColor();
    }
    
    public void setColor(Color c) {
        proxy.setColor(c);
    }
    
    public void setPaintMode() {
        proxy.setPaintMode();
    }
    
    public void setXORMode(Color c1) {
        proxy.setXORMode(c1);
    }
    
    public Font getFont() {
        return proxy.getFont();
    }
    
    public void setFont(Font font) {
        proxy.setFont(font);
    }
    
    public FontMetrics getFontMetrics(Font f) {
        return proxy.getFontMetrics(f);
    }
    
    public Rectangle getClipBounds() {
        return proxy.getClipBounds();
    }
    
    public void clipRect(int x, int y, int width, int height) {
        proxy.clipRect(x, y, width, height);
    }
    
    public void setClip(int x, int y, int width, int height) {
        proxy.setClip(x, y, width, height);
    }
    
    public Shape getClip() {
        return proxy.getClip();
    }
    
    public void setClip(Shape clip) {
        proxy.setClip(clip);
    }
    
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        proxy.copyArea(x, y, width, height, dx, dy);
    }
    
    public void drawLine(int x1, int y1, int x2, int y2) {
        proxy.drawLine(x1, y1, x2, y2);
    }
    
    public void fillRect(int x, int y, int width, int height) {
        proxy.fillRect(x, y, width, height);
    }
    
    public void clearRect(int x, int y, int width, int height) {
        proxy.clearRect(x, y, width, height);
    }
    
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        proxy.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }
    
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        proxy.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }
    
    public void drawOval(int x, int y, int width, int height) {
        proxy.drawOval(x, y, width, height);
    }
    
    public void fillOval(int x, int y, int width, int height) {
        proxy.fillOval(x, y, width, height);
    }
    
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        proxy.drawArc(x, y, width, height, startAngle, arcAngle);
    }
    
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        proxy.fillArc(x, y, width, height, startAngle, arcAngle);
    }
    
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        proxy.drawPolyline(xPoints, yPoints, nPoints);
    }
    
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        proxy.drawPolygon(xPoints, yPoints, nPoints);
    }
    
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        proxy.fillPolygon(xPoints, yPoints, nPoints);
    }
    
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return proxy.drawImage(img, x, y, observer);
    }
    
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return proxy.drawImage(img, x, y, width, height, observer);
    }
    
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return proxy.drawImage(img, x, y, bgcolor, observer);
    }
    
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return proxy.drawImage(img, x, y, width, height, bgcolor, observer);
    }
    
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return proxy.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }
    
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }
    
    public void dispose() {
        proxy.dispose();
    }
}
