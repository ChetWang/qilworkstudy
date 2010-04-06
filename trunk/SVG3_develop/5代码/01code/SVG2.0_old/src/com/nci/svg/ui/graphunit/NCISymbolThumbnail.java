package com.nci.svg.ui.graphunit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.svg.SVGDocument;

/**
 * 用于图元缩略图
 * 
 * @author Qil.Wong
 * 
 */
@Deprecated
public class NCISymbolThumbnail extends JPanel {

    private JSVGCanvas canvas;
    private SVGDocument document;
    private JLabel symbolNameLabel;

    public NCISymbolThumbnail() {
        init();
    }

    /**
     * 初始化缩略图组件，包含一个label和一个显示图的JSVGCanvas
     */
    private void init() {
        canvas = new JSVGCanvas();
        canvas.setSize(new Dimension(22, 22));
        canvas.setRecenterOnResize(true);
        symbolNameLabel = new JLabel();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        layout.rowWeights = new double[]{0.0};
        layout.columnWeights = new double[]{1.0, 0.0};
        GridBagConstraints cons_label = new GridBagConstraints();
        cons_label.gridx = 0;
        cons_label.gridy = 0;
        cons_label.fill = GridBagConstraints.HORIZONTAL;
        cons_label.anchor = GridBagConstraints.WEST;
        layout.setConstraints(symbolNameLabel, cons_label);
        GridBagConstraints cons_canvas = new GridBagConstraints();
        cons_canvas.gridx = 1;
        cons_canvas.gridy = 0;
//		cons_canvas.fill = GridBagConstraints.NONE;
        cons_canvas.anchor = GridBagConstraints.EAST;
        layout.setConstraints(canvas, cons_canvas);
        add(symbolNameLabel);
        add(canvas);
    }

    public void setSVGDocument(SVGDocument doc) {
        this.document = doc;
        canvas.setDocument(document);
    }

    public void setText(String text) {
        symbolNameLabel.setText(text);
    }

    public SVGDocument getSVGDocument() {
        return document;
    }

    public void setURI(String uri) {
        canvas.setURI(uri);
        canvas.updateUI();
    }

    public String getText() {
        return symbolNameLabel.getText();
    }
}
