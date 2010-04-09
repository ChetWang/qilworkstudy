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
 * ͼƬ����������������Ͻ�ͼ�궼ͨ��ͼƬ�ļ�����ʾ����ʱ�������Ͷ����ô˶���
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityImageIconCreator implements WfActivityIconCreator {

	/**
	 * ͼƬimage����
	 */
	private BufferedImage image;

	public WfActivityImageIconCreator() {
	}

	public void paint(Graphics2D g, WfActivityBasic activity) {
		Rectangle2D mainBounds = activity.getMainArea().getBounds2D();
		// ���ݻͼ��������û�׼ֵ
		double relativeWH = WfActivityBasic.DEFAULT_CENTERSHAPE_WIDTH;
		if (PaintBoardBasic.topIconResizable) {
			relativeWH = activity.getWidth() > activity.getHeight() ? activity
					.getHeight() : activity.getWidth();
		}
		int iconWidth = (int) relativeWH / 7;
		int iconHeight = (int) relativeWH / 6;

		// ����ͼ���x
		double x = mainBounds.getX() + mainBounds.getWidth() - iconWidth
				* (activity.getTopRightShowIndex() + 1) - relativeWH / 14
				* activity.getTopRightShowIndex() - 10;
		// ����ͼ���y
		double y = mainBounds.getY() + relativeWH / 20;
		// �Ȼ�������
		Rectangle2D rect = new Rectangle2D.Double(x, y, iconWidth, iconHeight);
		GeneralPath mainPath = activity.getTopRightAreas()[activity
				.getTopRightShowIndex()];
		// ���rect�����κλ��ƣ�ֻ�Ǹ�����ƶ������п��Լ�����Ч�ռ�
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
