/*
 * Dictionary.java
 *
 * Created on July 6, 2007, 12:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.awt.Component;
import javax.swing.JToolTip;

/**
 *
 * @author William Chen
 */
public interface Dictionary {
    String translate(JToolTip tip, String word);
    void asynchronizedTranslate(Clock clock, Text text, Component src, JToolTip tip, int x, int y);
}
