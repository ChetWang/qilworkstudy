/*
 * Animator.java
 *
 * Created on 2007年6月24日, 上午12:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author William Chen
 */
public interface Animator {
    void init(Component pane);
    void paint(Component c, Graphics g, Point point, int index, int total);
    void destroy();
}
