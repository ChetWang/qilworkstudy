package com.nci.svg.sdk.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * 背景色拥有渐进效果的JPanel
 * 
 * @author Qil.Wong
 * 
 */
public class GradientPanel extends JPanel {

	/**
	 * 默认的起始颜色
	 */
	private Color startColor = Color.white;
	/**
	 * 默认的结束颜色
	 */
	private Color endColor = new Color(159, 188, 233);
	private static final long serialVersionUID = 1L;

	public GradientPanel(Color startColor, Color endColor) {
		super();
		this.startColor = startColor;
		this.endColor = endColor;
	}

	public GradientPanel() {
		super();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gradient = new GradientPaint(0, 0, startColor, this
				.getWidth(), this.getHeight(), endColor);
		g2d.setPaint(gradient);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	// private void initialize() {
	// this.setSize(300, 200);
	// this.setLayout(new GridBagLayout());
	// }

	public Color getEndColor() {
		return endColor;
	}

	public void setEndColor(Color endColor) {
		this.endColor = endColor;
	}

	public Color getStartColor() {
		return startColor;
	}

	public void setStartColor(Color startColor) {
		this.startColor = startColor;
	}

}
