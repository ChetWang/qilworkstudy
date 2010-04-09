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
 * ��������ͼ�Σ���������ͼ�εĻ��࣬ʵ����BasicShape
 * 
 * @author Qil.Wong
 * 
 */
public abstract class AbstractShape implements BasicShape, Serializable {

	public int state = WofoBaseBean.CHANGED;// Ĭ���Ǹ���״̬

	protected boolean selected = false;// �Ƿ�ѡ��
	/**
	 * ѡ���¼�����
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
	 * �ж��Ƿ�ѡ��
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * �����Ƿ�ѡ��
	 * 
	 * @param selected
	 *            ����ֵ ѡ�����
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		for (ShapeSelectionListener l : selectionListeners) {
			l.shapeSelected(this);
		}
	}

	/**
	 * ���ͼ��ѡ���¼�����ѡ���ȡ��ѡ��ʱ������
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
	 * һģһ����¡��ǰ����
	 * 
	 * @return
	 */
	public abstract AbstractShape cloneShape();

	/**
	 * �½���ճ��������Ը���
	 */
	public abstract void updatePaste(PaintBoardBasic board);

	/**
	 * ��ȡ��С��X����
	 */
	public abstract double getMinXPos();

	/**
	 * ��ȡ��С��Y����
	 */
	public abstract double getMinYPos();

	/**
	 * ��ȡ����X����
	 */
	public abstract double getMaxXPos();

	/**
	 * ��ȡ����Y����
	 */
	public abstract double getMaxYPos();

	/**
	 * ƽ��
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void trans(double x, double y);

	/**
	 * ����϶�
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseDragged(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ���˫��
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseDoubleClicked(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ��굥��
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseClicked(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ��갴��
	 * 
	 * @param e
	 * @param board
	 */
	public void mousePressed(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ���̧��
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseReleased(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ������ͼ�η�Χ
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseEntered(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ����뿪ͼ�η�Χ
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseExited(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * �����ͼ�����ƶ�
	 * 
	 * @param e
	 * @param board
	 */
	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
	}

	/**
	 * ͼ�α���
	 * 
	 * @param shapeBeans
	 * @param board
	 */
	public abstract void saveShape(List<WofoBaseBean> shapeBeans,
			PaintBoardBasic board);

	/**
	 * ��ȡͼ�ζ�Ӧ��ҵ�����
	 * 
	 * @return
	 */
	public WofoBaseBean getWofoBean() {
		return wofoBean;
	}

	/**
	 * ����ҵ�����
	 * 
	 * @param wofoBean
	 */
	public void setWofoBean(WofoBaseBean wofoBean) {
		this.wofoBean = wofoBean;
	}

	/**
	 * ��ǰͼ���Ƿ�ɱ�����ɾ��
	 * 
	 * @return
	 */
	public boolean isDeletable() {
		return deletable;
	}

	/**
	 * ����ͼ���Ƿ�ɵ���ɾ��
	 * 
	 * @param deletable
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	/**
	 * ͼ��ɾ����Ķ���
	 * 
	 * @param board
	 */
	public void removeShape(PaintBoardBasic board) {
		board.getGraphVector().remove(this);
	}

	/**
	 * �����ͼ�Σ�Ĭ��������������ͼ�ζ��е����һ��
	 * 
	 * @param board
	 */
	public void addShape(PaintBoardBasic board) {
		board.getGraphVector().add(this);
	}

	/**
	 * �����Զ�����
	 * 
	 * @param text
	 *            ���е��ı�
	 * @param g2
	 * @param valueList
	 * @return
	 */
	protected int autoWrapText(String text, double shapeWidth, Graphics2D g2,
			List<String> valueList) {
		int textWidth = SwingUtilities.computeStringWidth(g2.getFontMetrics(),
				text);
		// �������е������
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
	 * ���־�����ʾ��������г���ͼ�ο�ȣ�����
	 * 
	 * @param text
	 *            Ҫ��ʾ���ı�
	 * @param shapeWidth
	 *            ͼ�εĿ��
	 * @param shapeHeight
	 *            ͼ�εĸ߶�
	 * @param shapeX
	 *            ͼ��xλ��
	 * @param shapeY
	 *            ͼ��yλ��
	 * @param g2
	 */
	protected void drawTextCenterShape(String text, double shapeWidth,
			double shapeHeight, double shapeX, double shapeY, Graphics2D g2,
			boolean verticalText) {
		if (text != null && !text.trim().equals("")) {
			String[] xs = text.split("\n");
			List<String> valueList = new ArrayList<String>();
			int maxW = 0;// ���һ�����ֵĿ��
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
