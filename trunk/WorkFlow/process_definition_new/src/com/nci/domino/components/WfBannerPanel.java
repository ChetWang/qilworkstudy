package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.swing.JideSwingUtilities;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 自定义Barnner类，简化信息
 * 
 * @author Qil.Wong
 * 
 */
public class WfBannerPanel extends BannerPanel {

	private static final long serialVersionUID = -2471971229231147225L;

	private String defaultTitle;

	private String defaultSubTitle;

	private Timer errorWaitTimer;
	
	private ImageIcon defaultImage;

	public WfBannerPanel(String title, String subTitle, ImageIcon image) {
		super(title, subTitle, image);
		defaultTitle = title;
		defaultSubTitle = subTitle;
		defaultImage = image;
		errorWaitTimer = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resumeDefault();
			}
		});
		FontUIResource o = (FontUIResource) UIManager.get("Label.font");

		setTitleFont(new FontUIResource(o.deriveFont(Font.BOLD, o.getSize())));
		setSubTitleFont(o);
	}
	
	public void paintComponent(Graphics g) {
		Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
		JideSwingUtilities.fillGradient((Graphics2D) g, rect, new Color(227,
				227, 227), Color.WHITE, false);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param errorInfo
	 */
	public void setError(String errorInfo) {
		setTitle(WofoResources.getValueByKey("error"));
		setTitleColor(Color.red);
		setSubTitleColor(Color.red);
		setSubtitle(errorInfo);
		setTitleIcon(Functions.getImageIcon("error.gif"));
		((JComponent) getParent()).updateUI();
		errorWaitTimer.start();
	}

	/**
	 * 设置提示信息
	 * @param tip 提示信息
	 */
	public void setTip(String tip) {
		setTitle(WofoResources.getValueByKey("tip"));
		setTitleColor(Color.YELLOW);
		setSubTitleColor(Color.YELLOW);
		setSubtitle(tip);
		setTitleIcon(Functions.getImageIcon("warn.gif"));
		updateUI();
		((JComponent) getParent()).updateUI();
		errorWaitTimer.start();
	}

	/**
	 * 错误信息显示后重新显示原先的提示信息
	 */
	public void resumeDefault() {
		setTitle(defaultTitle);
		setTitleColor(Color.BLACK);
		setSubTitleColor(Color.BLACK);
		setSubtitle(defaultSubTitle);
		setTitleIcon(defaultImage);
		updateUI();
		((JComponent) getParent()).updateUI();
		errorWaitTimer.stop();
	}

	public JComponent getGlassBanner() {
		GlassPanel glass = new GlassPanel();
		glass.setLayout(new BorderLayout());
		glass.add(this, BorderLayout.CENTER);
		return glass;
	}

}
