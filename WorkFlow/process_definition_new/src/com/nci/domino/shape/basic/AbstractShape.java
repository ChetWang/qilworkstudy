package com.nci.domino.shape.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;

/**
 * 抽象流程图形，所有流程图形的基类，实现了BasicShape
 * 
 * @author Qil.Wong
 * 
 */
public abstract class AbstractShape implements BasicShape, Serializable {

	public int state = WofoBaseBean.CHANGED;// 默认是更改状态

	protected boolean selected = false;// 是否选中
	/**
	 * 选择事件集合
	 */
	protected List<ShapeSelectionListener> selectionListeners = new Vector<ShapeSelectionListener>();

	protected WofoBaseBean wofoBean;

	protected boolean isUnderMouse = false;

	protected boolean deletable = true;

	protected static Color backColor_from = new Color(235, 245, 235);
	protected static Color backColor_to = new Color(180, 180, 228);
	// protected static Color backColor_to = new Color(205, 215, 235);
	protected static Color backColor_from_undermouse = new Color(235, 215, 235);
	protected static Color backColor_to_undermouse = new Color(188, 102, 188);

	/**
	 * 判断是否被选中
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * 设置是否选中
	 * 
	 * @param selected
	 *            布尔值 选中与否
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		for (ShapeSelectionListener l : selectionListeners) {
			l.shapeSelected(this);
		}
	}

	/**
	 * 添加图形选择事件，在选择和取消选择时都触发
	 * 
	 * @param l
	 */
	public void addShapeSelectionListener(ShapeSelectionListener l) {
		selectionListeners.add(l);
	}

	public void removeShapeSelectionListener(ShapeSelectionListener l) {
		selectionListeners.remove(l);
	}

	/**
	 * 一模一样克隆当前对象
	 * 
	 * @return
	 */
	public abstract AbstractShape cloneShape();

	/**
	 * 新建或粘贴后的属性更改
	 */
	public abstract void updatePaste(PaintBoardBasic board);

	/**
	 * 获取最小的X坐标
	 */
	public abstract double getMinXPos();

	/**
	 * 获取最小的Y坐标
	 */
	public abstract double getMinYPos();

	/**
	 * 获取最大的X坐标
	 */
	public abstract double getMaxXPos();

	/**
	 * 获取最大的Y坐标
	 */
	public abstract double getMaxYPos();

	/**
	 * 平移
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void trans(double x, double y);

	/**
	 * 鼠标拖动
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseDragged(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标双击
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseDoubleClicked(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标单击
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseClicked(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标按下
	 * 
	 * @param e
	 * @param board
	 */
	public void mousePressed(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标抬起
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseReleased(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标进入图形范围
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseEntered(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标离开图形范围
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseExited(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 鼠标在图形内移动
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * 图形保存
	 * 
	 * @param shapeBeans
	 * @param board
	 */
	public abstract void saveShape(List<WofoBaseBean> shapeBeans,
			PaintBoardBasic board);

	/**
	 * 获取图形对应的业务对象
	 * 
	 * @return
	 */
	public WofoBaseBean getWofoBean() {
		return wofoBean;
	}

	/**
	 * 设置业务对象
	 * 
	 * @param wofoBean
	 */
	public void setWofoBean(WofoBaseBean wofoBean) {
		this.wofoBean = wofoBean;
	}

	/**
	 * 但前图形是否可被单独删除
	 * 
	 * @return
	 */
	public boolean isDeletable() {
		return deletable;
	}

	/**
	 * 设置图形是否可单独删除
	 * 
	 * @param deletable
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	/**
	 * 图形删除后的动作
	 * 
	 * @param board
	 */
	public void removeShape(PaintBoardBasic board) {
		board.getGraphVector().remove(this);
	}

	/**
	 * 新添加图形，默认是增加在已有图形队列的最后一个
	 * 
	 * @param board
	 */
	public void addShape(PaintBoardBasic board) {
		board.getGraphVector().add(this);
	}

	/**
	 * 文字自动换行
	 * 
	 * @param text
	 *            换行的文本
	 * @param g2
	 * @param valueList
	 * @return
	 */
	protected int autoWrapText(String text, double shapeWidth, Graphics2D g2,
			List<String> valueList) {
		int textWidth = SwingUtilities.computeStringWidth(g2.getFontMetrics(),
				text);
		// 所有行中的最大宽度
		int maxWidth = 0;
		if (shapeWidth < textWidth) {
			int i = 1;
			int w = 0;
			while (true) {
				w = SwingUtilities.computeStringWidth(g2.getFontMetrics(), text
						.substring(0, text.length() - i));
				if (w > shapeWidth - 4) {
					i++;
				} else {
					break;
				}
			}
			if (w > maxWidth) {
				maxWidth = w;
			}
			valueList.add(text.substring(0, text.length() - i));
			int anotherW = autoWrapText(text.substring(text.length() - i),
					shapeWidth, g2, valueList);
			if (anotherW > maxWidth)
				maxWidth = anotherW;
		} else {
			maxWidth = SwingUtilities.computeStringWidth(g2.getFontMetrics(),
					text);
			valueList.add(text);
		}
		return maxWidth;
	}

	protected void drawTextCenterShape(String text, double shapeWidth,
			double shapeHeight, double shapeX, double shapeY, Graphics2D g2) {
		g2.setColor(Color.black);
		drawTextCenterShape(text, shapeWidth, shapeHeight, shapeX, shapeY, g2,
				false);
	}

	/**
	 * 文字居中显示，如果单行超过图形宽度，则换行
	 * 
	 * @param text
	 *            要显示的文本
	 * @param shapeWidth
	 *            图形的宽度
	 * @param shapeHeight
	 *            图形的高度
	 * @param shapeX
	 *            图形x位置
	 * @param shapeY
	 *            图形y位置
	 * @param g2
	 */
	protected void drawTextCenterShape(String text, double shapeWidth,
			double shapeHeight, double shapeX, double shapeY, Graphics2D g2,
			boolean verticalText) {
		if (text != null && !text.trim().equals("")) {
			String[] xs = text.split("\n");
			List<String> valueList = new ArrayList<String>();
			int maxW = 0;// 最大一列文字的宽度
			for (String s : xs) {
				int w = autoWrapText(s,
						verticalText ? shapeHeight : shapeWidth, g2, valueList);
				if (w > maxW)
					maxW = w;
			}
			int textHeight = valueList.size() * g2.getFont().getSize();
			int actualTextYPos = (int) shapeY;
			if (textHeight < shapeHeight) {
				actualTextYPos = (int) (shapeY + (shapeHeight - textHeight) / 2);
			}
			int actualTextXPos = (int) (shapeX + (shapeWidth - maxW) / 2);
			for (int i = 0; i < valueList.size(); i++) {
				g2.drawString(valueList.get(i), (int) actualTextXPos + 2,
						actualTextYPos + g2.getFont().getSize() * (i + 1) + 1);
			}
		}
	}

}
