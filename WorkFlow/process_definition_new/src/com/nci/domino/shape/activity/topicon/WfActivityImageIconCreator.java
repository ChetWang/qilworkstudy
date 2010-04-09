package com.nci.domino.shape.activity.topicon;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.help.ImageUtil;
import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * 图片对象生成器，活动右上角图标都通过图片文件来显示，此时所有类型都公用此对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityImageIconCreator implements WfActivityIconCreator {

	/**
	 * 图片image对象
	 */
	private BufferedImage image;

	public WfActivityImageIconCreator() {
	}

	public void paint(Graphics2D g, WfActivityBasic activity) {
		Rectangle2D mainBounds = activity.getMainArea().getBounds2D();
		// 根据活动图宽高来设置基准值
		double relativeWH = WfActivityBasic.DEFAULT_CENTERSHAPE_WIDTH;
		if (PaintBoardBasic.topIconResizable) {
			relativeWH = activity.getWidth() > activity.getHeight() ? activity
					.getHeight() : activity.getWidth();
		}
		int iconWidth = (int) relativeWH / 7;
		int iconHeight = (int) relativeWH / 6;

		// 计算图标的x
		double x = mainBounds.getX() + mainBounds.getWidth() - iconWidth
				* (activity.getTopRightShowIndex() + 1) - relativeWH / 14
				* activity.getTopRightShowIndex() - 10;
		// 计算图标的y
		double y = mainBounds.getY() + relativeWH / 20;
		// 先画个矩形
		Rectangle2D rect = new Rectangle2D.Double(x, y, iconWidth, iconHeight);
		GeneralPath mainPath = activity.getTopRightAreas()[activity
				.getTopRightShowIndex()];
		// 这个rect不做任何绘制，只是给鼠标移动过程中可以计算有效空间
		mainPath.append(rect, false);

		g.setStroke(new BasicStroke(1.3f));
		Composite old = g.getComposite();
		if (activity.getTopRightUnderMouseIndex() == activity
				.getTopRightShowIndex()) {
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.6f));
		}
		g.drawImage(ImageUtil.resize(image, iconWidth, iconHeight, false),
				(int) x, (int) y, null);
		g.setComposite(old);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
