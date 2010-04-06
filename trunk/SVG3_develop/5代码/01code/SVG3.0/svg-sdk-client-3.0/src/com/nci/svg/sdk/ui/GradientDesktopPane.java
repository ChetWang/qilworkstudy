package com.nci.svg.sdk.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDesktopPane;

/**
 * 背景渐进的JDesktopPane对象
 * @author Qil.Wong
 *
 */
public class GradientDesktopPane extends JDesktopPane{

	/**
	 * 默认的起始颜色
	 */
	private Color startColor = Color.white;
	/**
	 * 默认的结束颜色
	 */
	private Color endColor = new Color(159, 188, 233);
	private static final long serialVersionUID = 1L;

	public GradientDesktopPane(Color startColor, Color endColor) {
		super();
		this.startColor = startColor;
		this.endColor = endColor;
	}

	public GradientDesktopPane() {
		super();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gradient = new GradientPaint(0, 0, startColor, this
				.getWidth(), this.getHeight(), endColor);
		g2d.setPaint(gradient);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

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
