package com.nci.domino.shape.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.SwingUtilities;

import com.nci.domino.GlobalConstants;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.plugin.note.WofoNoteBean;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;

/**
 * 文字图形，以便可以和活动一样拖拽
 * 
 * @author Qil.Wong
 * 
 */
public class WfTextShapeBasic extends SimpleLocationShape {

	protected int degree;// 90，180，270

	private String value;

	private boolean valueChanged = false;

	public double fontSize = GlobalConstants.ACTTEXTFONT.getSize();

	private double textWidth = 0d;

	static double rectHeight = 18;

	public String uid = Functions.getUID();

	public WfTextShapeBasic(String value) {
		this.value = value;
	}

	@Override
	public AbstractShape cloneShape() {
		WfTextShapeBasic s = new WfTextShapeBasic(value);
		s.x = x;
		s.y = y;
		s.degree = degree;
		s.textWidth = textWidth;
		s.uid = uid;
		s.deletable = deletable;
		return s;
	}

	public void drawShape(Graphics2D g2, PaintBoardBasic pb) {
		if (value != null && !value.trim().equals("")) {
			g2.setColor(Color.black);
			g2.drawString(value, (float) x, (float) y);
			if (valueChanged) {
				textWidth = SwingUtilities.computeStringWidth(g2
						.getFontMetrics(), value) + 7;
				valueChanged = false;
			}
			if (selected) {
				double l = x - 3;
				double t = y - fontSize - 1;
				g2.setStroke(GlobalConstants.SDASH_LINE_STROKE);
				g2.draw(new Rectangle2D.Double(l, t, textWidth, rectHeight));
			}
		}
	}

	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4) {
		return WfMath.isDotInRect(x, y, p1, p2, p3, p4);
	}

	public boolean isOnRange(double wx, double wy, double e) {
		Rectangle2D rect = new Rectangle2D.Double(x, y - fontSize, textWidth,
				fontSize);
		return rect.contains(wx, wy);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		valueChanged = true;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	/**
	 * 获取文字像素长度
	 * 
	 * @return
	 */
	public double getTextWidth() {
		return textWidth;
	}

	@Override
	public void updatePaste(PaintBoardBasic board) {
		// 啥都不做

	}

	@Override
	public double getMaxXPos() {
		return x + textWidth;
	}

	@Override
	public double getMaxYPos() {
		return y;
	}

	@Override
	public double getMinXPos() {
		return x;
	}

	@Override
	public double getMinYPos() {
		if (y == 0)
			return 0;
		return y - rectHeight;
	}

	@Override
	public void trans(double x, double y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		// DO NOTHING
	}

	public void mouseReleased(MouseEvent e, PaintBoardBasic board) {
		if (wofoBean == null)
			return;
		WofoNoteBean noteBean = (WofoNoteBean) wofoBean;
		noteBean.setPosX(x);
		noteBean.setPosY(y);
	}

}
