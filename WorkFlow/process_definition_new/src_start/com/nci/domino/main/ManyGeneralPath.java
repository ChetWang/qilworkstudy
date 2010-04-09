package com.nci.domino.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ManyGeneralPath extends JApplet {
  DrawingCanvas canvas;

  public static void main(String[] a) {
    JFrame f = new JFrame();
    ManyGeneralPath path = new ManyGeneralPath();
    path.init();
    f.getContentPane().add(path);
    f.setDefaultCloseOperation(1);
    f.setSize(650, 250);
    f.setVisible(true);
  }

  public void init() {
    Container container = getContentPane();
    JPanel panel = new JPanel();
    canvas = new DrawingCanvas();
    container.add(canvas);
  }

  class DrawingCanvas extends Canvas {
    Vector generalPaths;

    GeneralPath selectedGPath = null;

    Rectangle2D boundingRec = null;

    int selectedRule = GeneralPath.WIND_NON_ZERO;

    boolean drawNoFill = false;

    public DrawingCanvas() {
      setBackground(Color.white);
      setSize(400, 200);
      generalPaths = new Vector();

      GeneralPath gp1, gp2, gp3, gp4, gp5, gp6, gp7, gp8;

      gp1 = new GeneralPath();
      gp1.moveTo(50, 10);
      gp1.lineTo(70, 80);
      gp1.lineTo(90, 40);
      gp1.lineTo(10, 40);
      gp1.lineTo(50, 80);
      gp1.closePath();
      
      generalPaths.addElement(gp1);

      gp2 = new GeneralPath();
      gp2.moveTo(120, 20);
      gp2.lineTo(180, 20);
      gp2.lineTo(120, 80);
      gp2.lineTo(180, 80);
      gp2.closePath();
      generalPaths.addElement(gp2);

      gp3 = new GeneralPath();
      gp3.moveTo(220, 20);
      gp3.lineTo(280, 20);
      gp3.lineTo(280, 60);
      gp3.lineTo(240, 60);
      gp3.lineTo(240, 40);
      gp3.lineTo(260, 40);
      gp3.lineTo(260, 80);
      gp3.lineTo(220, 80);
      gp3.closePath();
      generalPaths.addElement(gp3);

      gp4 = new GeneralPath();
      gp4.moveTo(310, 20);
      gp4.lineTo(380, 20);
      gp4.lineTo(380, 80);
      gp4.lineTo(320, 80);
      gp4.lineTo(320, 10);
      gp4.lineTo(340, 10);
      gp4.lineTo(340, 60);
      gp4.lineTo(360, 60);
      gp4.lineTo(360, 40);
      gp4.lineTo(310, 40);
      gp4.closePath();
      generalPaths.addElement(gp4);

      gp5 = new GeneralPath();
      gp5.moveTo(50, 120);
      gp5.lineTo(70, 180);
      gp5.lineTo(20, 140);
      gp5.lineTo(80, 140);
      gp5.lineTo(30, 180);
      gp5.closePath();
      generalPaths.addElement(gp5);

      gp6 = new GeneralPath();
      gp6.moveTo(120, 180);
      gp6.quadTo(150, 120, 180, 180);
      gp6.closePath();
      generalPaths.addElement(gp6);

      gp7 = new GeneralPath();
      gp7.moveTo(220, 150);
      gp7.curveTo(240, 130, 280, 160, 300, 140);
      gp7.lineTo(300, 180);
      gp7.quadTo(260, 160, 220, 180);
      gp7.closePath();
      generalPaths.addElement(gp7);

      gp8 = new GeneralPath();
      gp8.moveTo(360, 100);
      gp8.lineTo(360, 200);
      gp8.lineTo(400, 140);
      gp8.lineTo(320, 120);
      gp8.lineTo(400, 180);
      gp8.lineTo(320, 180);
      gp8.closePath();
      generalPaths.addElement(gp8);
    }

    public void paint(Graphics g) {
      Graphics2D g2D = (Graphics2D) g;

      for (int i = 0; i < generalPaths.size(); i++) {
        if (drawNoFill) {
          g2D.draw((GeneralPath) generalPaths.elementAt(i));
        } else {
          g2D.fill((GeneralPath) generalPaths.elementAt(i));
        }
      }
    }
  }
}