/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.graphunit;

import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Qil.Wong
 */
public class GraphUnitSelectionListener extends MouseAdapter implements MouseMotionListener {

    private NCIThumbnailPanel thumbnail;

    public GraphUnitSelectionListener() {
    }

    public GraphUnitSelectionListener(NCIThumbnailPanel thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        

    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }
}
