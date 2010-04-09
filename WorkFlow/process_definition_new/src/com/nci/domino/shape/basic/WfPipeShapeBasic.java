package com.nci.domino.shape.basic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.nci.domino.GlobalConstants;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.plugin.pipe.WofoPipeBaseBean;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;
import com.nci.domino.shape.pipe.WfPipeConfig;
import com.nci.domino.shape.pipe.WfPipeShape;

public class WfPipeShapeBasic extends AbstractShape {

	protected double x;
	protected double y;
	protected double width = WfPipeConfig.defaultPipeWidth;
	protected double height = WfPipeConfig.defaultPipeHeight;
	protected int index;
	private static int ipadx = (int) WfPipeConfig.defaultPipeHeight + 1;
	private static int ipady = 4;

	/**
	 * 左部虚线或下部虚线
	 */
	private Line2D leftLine = new Line2D.Double(0, 0, 0, 0);
	/**
	 * 右部虚线或下部虚线
	 */
	private Line2D rightLine = new Line2D.Double(0, 0, 0, 0);

	/**
	 * 是否是垂直泳道标签
	 */
	protected boolean vertical = true;

	/**
	 * 是否允许resize,此时鼠标变形，允许拖拽来改变宽度
	 */
	protected boolean resizable = false;

	private static Color horizontalFrom = new Color(215, 216, 216);
	private static Color horizontalTo = new Color(186, 173, 173);

	private Color from, to;

	public WfPipeShapeBasic(int index, double formerLength, boolean vertical) {
		this.index = index;
		this.vertical = vertical;
		if (vertical) {
			x = formerLength + ipadx + ipady;
			y = ipady;
		} else {
			width = WfPipeConfig.defaultPipeHeight;
			height = WfPipeConfig.defaultPipeWidth;
			x = ipady;
			y = formerLength + ipadx + ipady;
		}
		String defautText = vertical ? WfPipeConfig.verticalDefaultText
				: WfPipeConfig.horizontalDefaultText;
		try {
			wofoBean = vertical ? (WofoPipeBaseBean) WfPipeConfig.verticalBeanClass
					.getConstructor(String.class).newInstance(
							Functions.getUID())
					: (WofoPipeBaseBean) WfPipeConfig.horizontalBeanClass
							.getConstructor(String.class).newInstance(
									Functions.getUID());
			((WofoPipeBaseBean) wofoBean).setShowText(defautText + (index + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updatePaste(PaintBoardBasic board) {

	}

	public void removeShape(PaintBoardBasic board) {
		// 其后的所有管道需要重新排列
		for (AbstractShape s : board.getGraphVector()) {
			if (s instanceof WfPipeShape) {
				WfPipeShape p = (WfPipeShape) s;
				if (p.isVertical() == vertical && p.getIndex() > index) {
					if (vertical) {
						p.setX(p.getX() - width);
					} else {
						p.setY(p.getY() - height);
					}
				}
			}
		}
		board.getGraphVector().remove(this);
	}

	public void drawShape(Graphics2D g2, PaintBoardBasic pb) {
		AffineTransform oldTransform = g2.getTransform();
		Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
		if (isUnderMouse) {
			from = backColor_from_undermouse;
			to = backColor_to_undermouse;

		} else {
			if (vertical) {
				from = backColor_from;
				to = backColor_to;
			} else {
				from = backColor_from;
				to = horizontalTo;
			}
		}
		Rectangle half_rect = rect.getBounds();
		Paint old = g2.getPaint();

		GradientPaint paint = new GradientPaint(vertical ? half_rect.x
				: half_rect.x + (int) width / 2, vertical ? half_rect.y
				+ half_rect.height / 2 : half_rect.y,
				isUnderMouse ? backColor_from_undermouse : from,
				vertical ? half_rect.x : half_rect.x + (int) width,
				vertical ? half_rect.y + half_rect.height : half_rect.y,
				isUnderMouse ? backColor_to_undermouse : to, true);
		g2.setPaint(paint);
		g2.fill(half_rect);
		g2.setPaint(old);

		g2.setColor(Color.black);
		if (selected) {
			g2.setStroke(GlobalConstants.SDASH_LINE_STROKE);
		} else {
			g2.setStroke(GlobalConstants.SMALL_LINE_STROKE);
		}
		g2.draw(rect);
		g2.setStroke(GlobalConstants.SDASH_LINE_STROKE);
		g2.setColor(Color.lightGray);
		if (vertical) {
			leftLine.setLine(x, y + height, x, y + pb.getSize().getHeight()
					/ pb.getTrans()[0]);
			rightLine.setLine(x + width, y + height, x + width, y
					+ pb.getSize().getHeight() / pb.getTrans()[0]);
		} else {
			leftLine.setLine(x + width, y, x + pb.getSize().getWidth()
					/ pb.getTrans()[0], y);
			rightLine.setLine(x + width, y + height, x
					+ pb.getSize().getWidth() / pb.getTrans()[0], y + height);
		}
		g2.draw(leftLine);
		g2.draw(rightLine);
		g2.setColor(Color.BLACK);
		if (!vertical) {
			g2.rotate(Math.PI / 2 * 3, x + width / 2, y + height / 2);
		}
		drawTextCenterShape(((WofoPipeBaseBean) wofoBean).getShowText(), width,
				height, x, y, g2, !vertical); // 垂直泳道是显示水平文字
		g2.setTransform(oldTransform);
	}

	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4) {
		return WfMath.isDotInRect(x, y, p1, p2, p3, p4);
	}

	public boolean isOnRange(double wx, double wy, double e) {
		Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
		return rect.contains(wx, wy);
	}

	@Override
	public AbstractShape cloneShape() {
		return null;
	}

	@Override
	public double getMaxXPos() {
		return x + width;
	}

	@Override
	public double getMaxYPos() {
		return y + height;
	}

	@Override
	public double getMinXPos() {
		return x;
	}

	@Override
	public double getMinYPos() {
		return y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public void trans(double x, double y) {
		// this.x = this.x + x;
		// this.y = this.y + y;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void mouseDragged(MouseEvent e, PaintBoardBasic board) {
		if (resizable) {
			if (vertical) {
				double pad = board.getWxNew() - x - width;
				if (board.getWxNew() - x < 24 / board.getTrans()[0]) {
					return;
				}
				width = board.getWxNew() - x;
				for (AbstractShape s : board.getGraphVector()) {
					if (s instanceof WfPipeShape) {
						WfPipeShape p = (WfPipeShape) s;
						if (p.isVertical() == vertical && p.getIndex() > index) {
							p.setX(p.getX() + pad);
						}
					}
				}
			} else {
				double pad = board.getWyNew() - y - height;
				if (board.getWyNew() - y < 24 / board.getTrans()[0]) { // 最小值为24，小于24将不被允许拖拽
					return;
				}
				height = board.getWyNew() - y;
				for (AbstractShape s : board.getGraphVector()) {
					if (s instanceof WfPipeShape) {
						WfPipeShape p = (WfPipeShape) s;
						if (p.isVertical() == vertical && p.getIndex() > index) {
							p.setY(p.getY() + pad);
						}
					}
				}
			}

		}
	}

	public void mouseEntered(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = true;
		board.repaint();
	}

	public void mouseExited(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = false;
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		resizable = false;
		board.repaint();
	}

	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
			if (vertical) {
				resizable = rect.contains(board.getWxNew(), board.getWyNew())
						&& x + width - board.getWxNew() <= 5 / board.getTrans()[0];
			} else {
				resizable = rect.contains(board.getWxNew(), board.getWyNew())
						&& y + height - board.getWyNew() <= 5 / board
								.getTrans()[0];
			}
			if (resizable) {
				if (vertical) {
					board.setCursor(Cursor
							.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				} else {
					board.setCursor(Cursor
							.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				}
			} else {
				board.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/**
	 * 是否是垂直泳道
	 * 
	 * @return
	 */
	public boolean isVertical() {
		return vertical;
	}

	/**
	 * 设置泳道垂直属性
	 * 
	 * @param vertical
	 */
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	public void addShape(PaintBoardBasic board) {
		List<AbstractShape> existedShapes = board.getGraphVector();
		existedShapes.add(0, this);
	}

	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		WofoPipeBaseBean p = (WofoPipeBaseBean) wofoBean;
		p.setHeight(height);
		p.setIndex(index);
		p.setVertical(vertical);
		p.setWidth(width);
		p.setX(x);
		p.setY(y);
		double formerLength = 0;
		for (AbstractShape s : board.getGraphVector()) {
			if (s instanceof WfPipeShape) {
				WfPipeShape temp = (WfPipeShape) s;
				// 相同方向，在此图形前的index符合条件
				if (temp.isVertical() == vertical && temp.getIndex() < index) {
					formerLength = formerLength
							+ (vertical ? temp.getWidth() : temp.getHeight());
				}
			}
		}
		p.setFormerLength(formerLength);
		shapeBeans.add(p);
	}

}
