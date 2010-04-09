package com.nci.domino.shape.activity.topicon;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * ����ͼ����������������
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityAttachmentsIconCreator implements WfActivityIconCreator {

	public void paint(Graphics2D g, WfActivityBasic activity) {
		Rectangle2D mainBounds = activity.getMainArea().getBounds2D();
		// ���ݻͼ��������û�׼ֵ
		double relativeWH = WfActivityBasic.DEFAULT_CENTERSHAPE_WIDTH;
		if (PaintBoardBasic.topIconResizable) {
			relativeWH = activity.getWidth() > activity.getHeight() ? activity
					.getHeight() : activity.getWidth();
		}
		int iconWidth = (int) relativeWH / 8;
		// int iconHeight = (int) iconWidth + (int) relativeWH / 20;
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
		mainPath.append(rect, false);
		// �м仭3����
		for (int i = 0; i < 3; i++) {
			mainPath.append(
					new Line2D.Double(x + iconWidth / 3, y + iconHeight / 5
							* (i + 1), x + iconWidth, y + iconHeight / 5
							* (i + 1)), false);
		}
		g.setStroke(new BasicStroke(1.3f));
		// ������µ�����
		Composite old = g.getComposite();
		if (activity.getTopRightUnderMouseIndex() == activity
				.getTopRightShowIndex()) {
			g.setColor(Color.lightGray);

		} else {
			g.setColor(Color.white);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.6f));
		}
		g.fill(rect);
		g.setColor(Color.BLUE);
		g.draw(mainPath);
		g.setComposite(old);
	}

}
