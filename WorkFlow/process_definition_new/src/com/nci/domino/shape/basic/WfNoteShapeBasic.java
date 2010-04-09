package com.nci.domino.shape.basic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.GlobalConstants;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.plugin.note.WofoNoteBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;
import com.nci.domino.shape.pipe.WfPipeShape;

/**
 * 文字备注图形对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfNoteShapeBasic extends SimpleLocationShape {

	protected double width = 90d;
	protected double height = 70d;
	protected static final int curveHeight = 10;
	protected Area area = new Area();

	/**
	 * 被注释的图形的id
	 */
	protected List<AbstractShape> notedShapes = new ArrayList<AbstractShape>();

	/**
	 * 是否允许resize,此时鼠标变形，允许拖拽来改变宽度和高度
	 */
	protected boolean resizable = false;

	private static Color noteLinkColor = new Color(234, 98, 136);

	public WfNoteShapeBasic() {
		WofoNoteBean noteBean = new WofoNoteBean(Functions.getUID());
		wofoBean = noteBean;
	}

	public void drawShape(Graphics2D g2, PaintBoardBasic pb) {

		WofoNoteBean noteBean = (WofoNoteBean) wofoBean;

		List<String> notedShapeIDs = noteBean.getNotedShapeBeans();

		// 第一次要遍历图形，找出关联图形
		if (notedShapes.size() == 0 && notedShapeIDs.size() > 0) {
			for (String id : notedShapeIDs) {
				AbstractShape s = pb.getShapeById(id);
				if (s != null) {
					notedShapes.add(s);
				}
			}
		}
		for (AbstractShape notedShape : notedShapes) {
			if (notedShape != null) {
				Rectangle2D r1 = new Rectangle2D.Double(getMinXPos(),
						getMinYPos(), getMaxXPos() - getMinXPos(), getMaxYPos()
								- getMinYPos());
				Rectangle2D r2 = new Rectangle2D.Double(
						notedShape.getMinXPos(), notedShape.getMinYPos(),
						notedShape.getMaxXPos() - notedShape.getMinXPos(),
						notedShape.getMaxYPos() - notedShape.getMinYPos());
				Line2D l = new Line2D.Double(r1.getCenterX(), r1.getCenterY(),
						r2.getCenterX(), r2.getCenterY());
				g2.setColor(noteLinkColor);
				g2.setStroke(GlobalConstants.DASH_LINE_STROKE);
				g2.draw(l);
			}
		}

		// 是否鼠标移动到图形下
		if (isUnderMouse) {
			g2.setColor(backColor_from_undermouse);
		} else {
			g2.setColor(backColor_from);
		}
		Rectangle2D rr = new Rectangle2D.Double(x, y, width, height);
		QuadCurve2D qq1 = new QuadCurve2D.Double(x, y + height, x + width / 4,
				y + height + curveHeight, x + width / 2, y + height);
		QuadCurve2D qq2 = new QuadCurve2D.Double(x + width / 2, y + height, x
				+ width / 4 * 3, y + height - curveHeight, x + width, y
				+ height);
		area.reset();
		area.add(new Area(rr));
		area.add(new Area(qq1));
		area.subtract(new Area(qq2));
		g2.fill(area);
		g2.setColor(Color.black);
		if (selected) {
			g2.setStroke(GlobalConstants.DASH_LINE_STROKE);
		} else {
			g2.setStroke(GlobalConstants.SMALL_LINE_STROKE);
		}
		g2.draw(area);
		if (noteBean.getValue() != null
				&& !noteBean.getValue().trim().equals("")) {
			g2.setColor(Color.black);
			drawTextCenterShape(noteBean.getValue(), width, height, x, y, g2);
		}

	}

	@Override
	public AbstractShape cloneShape() {
		WfNoteShapeBasic s = new WfNoteShapeBasic();
		s.x = x;
		s.y = y;
		s.width = width;
		s.height = height;
		for (AbstractShape notedShape : notedShapes) {
			s.notedShapes.add(notedShape.cloneShape());
		}
		s.wofoBean = ((WofoNoteBean) wofoBean).cloneNote();
		return s;
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

	@Override
	public void mouseEntered(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = true;
		board.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = false;
		resizable = false;
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		board.repaint();
	}

	@Override
	public void addShape(PaintBoardBasic board) {
		List<AbstractShape> shapes = board.getGraphVector();
		if (shapes.size() == 0) {
			shapes.add(this);
		} else {
			// 泳道图被设计放在最前段，WfNoteShape放在泳道图后
			int index = 0;
			for (int i = 0; i < shapes.size(); i++) {
				index = i;
				if (shapes.get(i) instanceof WfPipeShape == false) {
					break;
				}
			}
			shapes.add(index, this);
		}
	}

	@Override
	public void trans(double x, double y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}

	@Override
	public void updatePaste(PaintBoardBasic board) {
		WofoNoteBean note = (WofoNoteBean) wofoBean;
		note.setID(Functions.getUID());
		note.setProcessId(board.getProcessBean().getProcessId());
	}

	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4) {
		return WfMath.isDotInRect(x, y, p1, p2, p3, p4);
	}

	public boolean isOnRange(double wx, double wy, double e) {
		return area.contains(wx, wy);
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		WofoNoteBean noteBean = (WofoNoteBean) wofoBean;
		noteBean.setHeight(height);
		noteBean.setWidth(width);
		ArrayList clonedNotedShapeIDs = new ArrayList();
		for (AbstractShape s : notedShapes) {
			clonedNotedShapeIDs.add(s.getWofoBean().getID());
		}
		noteBean.setNotedShapeBeans(clonedNotedShapeIDs);
		noteBean.setPosX(x);
		noteBean.setPosY(y);
		noteBean.setProcessId(board.getProcessBean().getProcessId());
		shapeBeans.add(noteBean);
	}

	public void setWofoBean(WofoBaseBean wofoBean) {
		super.setWofoBean(wofoBean);
		WofoNoteBean note = (WofoNoteBean) wofoBean;
		x = note.getPosX();
		y = note.getPosY();
		width = note.getWidth();
		height = note.getHeight();
	}

	public List<AbstractShape> getNotedShapes() {
		return notedShapes;
	}

	/**
	 * 设置被注释的图形
	 * 
	 * @param notedShape
	 */
	public void addNotedShape(AbstractShape notedShape) {
		this.notedShapes.add(notedShape);
	}

}
