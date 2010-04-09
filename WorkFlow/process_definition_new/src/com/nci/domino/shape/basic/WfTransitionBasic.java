package com.nci.domino.shape.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingUtilities;

import com.nci.domino.GlobalConstants;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoTransitionBean;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;
import com.nci.domino.shape.WfTransition;
import com.nci.domino.shape.WfTransitionCenter;

public class WfTransitionBasic extends AbstractShape {
	private static final long serialVersionUID = 6142532302459183234L;
	public WfActivityBasic previousActivity;// ���
	public WfActivityBasic nextActivity;// ��ֹ��
	// public String condition;//Ǩ������
	public boolean isDefault;// �Ƿ�Ĭ��·��

	// Ǩ�����֣�Ǩ���������ƣ�
	protected WfTextShapeBasic textShape = new WfTextShapeBasic("");

	// ���ĵ�뾶
	public static final int CENTERPOINT_RADIUS = 5;

	protected List<Point2D> cornerPoints = new ArrayList<Point2D>();

	protected WfTransitionCenter currentCenterPoint = new WfTransitionCenter();

	protected static Color defaultColor = new Color(117, 164, 242);

	/**
	 * @param firstAct
	 *            ��һ���
	 * @param secondAct
	 *            �ڶ����
	 */
	public WfTransitionBasic(WfActivityBasic firstAct, WfActivityBasic secondAct) {
		this.previousActivity = firstAct;
		this.nextActivity = secondAct;
		textShape.setDeletable(false);
		wofoBean = new WofoTransitionBean(Functions.getUID());
		((WofoTransitionBean) wofoBean).setWordsPos(textShape.getX() + ","
				+ textShape.getY());
		Point2D firstPoint = new Point2D.Double(firstAct.getX(), firstAct
				.getY());
		Point2D lastPoint = new Point2D.Double(secondAct.getX(), secondAct
				.getY());
		cornerPoints.add(firstPoint);
		cornerPoints.add(lastPoint);
		previousConerCenter[0] = (firstPoint.getX() + lastPoint.getX()) / 2;
		previousConerCenter[1] = (firstPoint.getY() + lastPoint.getY()) / 2;
	}

	public void updatePaste(PaintBoardBasic board) {
		// Do Nothing
	}

	/**
	 * ��ȡͼ�����Ǩ�ƶ�Ӧ�ľ�ֵ���Ǹ�����ֵ������ͼ�ε���Ч�����������ģ���ֵ��ʾͼ�����ĵ㵽Ǩ����ͼ�ν���ľ���
	 * 
	 * @param bounds
	 *            ͼ����Ч�߽�
	 * @param x1
	 * @param y1
	 * @return ��ֵ��������ʾֱ�����һ��ϣ�������ʾ��ֱ�������
	 */
	private RadBean getRad(Rectangle2D bounds, double x1, double y1) {
		double x0 = bounds.getCenterX();
		double y0 = bounds.getCenterY();
		RadBean radBean = new RadBean();
		// ��ˮƽ�ཻ���Ǵ�ֱ�ཻ��trueΪˮƽ�ཻ��falseΪ��ֱ�ཻ
		radBean.horizontalInsect = (Math.abs((y1 - y0) / (x1 - x0))) > (bounds
				.getHeight() / bounds.getWidth());
		double rad = -1;
		double pad = 0;
		radBean.wholeLen = Math.sqrt(Math.pow(x1 - x0, 2)
				+ Math.pow(y1 - y0, 2));

		if (radBean.horizontalInsect) {
			if (Math.abs(y1 - y0) < 0.1)
				rad = bounds.getHeight() / 2;
			else
				rad = bounds.getHeight() / 2 * radBean.wholeLen / (y1 - y0);
			pad = Math.abs(rad) * (x1 - x0) / radBean.wholeLen;
			radBean.insectPoint.setLocation(x0 + pad, y0 + bounds.getHeight()
					/ 2 * rad / Math.abs(rad));
		} else {
			if (Math.abs(x1 - x0) < 0.1)
				rad = bounds.getWidth() / 2;
			else {
				rad = bounds.getWidth() / 2 * radBean.wholeLen / (x1 - x0);
			}
			pad = Math.abs(rad) * (y1 - y0) / radBean.wholeLen;
			radBean.insectPoint.setLocation(x0 + bounds.getWidth() / 2 * rad
					/ Math.abs(rad), y0 + pad);
		}
		radBean.rad = rad;
		return radBean;
	}

	/**
	 * ��ȡ�ཻ��
	 * 
	 * @return
	 */
	public Point2D getInsectPoint(Rectangle2D bounds, Point2D anotherPoint) {
		RadBean radBean = getRad(bounds, anotherPoint.getX(), anotherPoint
				.getY());
		return radBean.insectPoint;
	}

	public void drawShape(Graphics2D g2, PaintBoardBasic pb) {
		g2.setStroke(selected ? GlobalConstants.DASH_LINE_STROKE
				: GlobalConstants.SOLID_LINE_STROKE);

		g2.setFont(GlobalConstants.TRANTEXTFONT);
//		int gen = pb.getExcursionGene(this);
		// double[][] tDotFirst = WfMath.getLineCoord(Math.sqrt(Math
		// .pow(previousActivity.getWidth() / 2
		// - previousActivity.getPadX(), 2)
		// + Math.pow(previousActivity.getHeight() / 2
		// - previousActivity.getPadY(), 2)),
		// this.previousActivity.getCenterX(), this.previousActivity
		// .getCenterY(), cornerPoints.get(1).getX(), cornerPoints
		// .get(1).getY(), gen);// ���ʵ�ʵ���ֹ����
		// double[][] tDotLast = WfMath.getLineCoord(Math.sqrt(Math.pow(
		// nextActivity.getWidth() / 2 - nextActivity.getPadX(), 2)
		// + Math.pow(nextActivity.getHeight() / 2
		// - nextActivity.getPadY(), 2)), cornerPoints.get(
		// cornerPoints.size() - 2).getX(), cornerPoints.get(
		// cornerPoints.size() - 2).getY(),
		// this.nextActivity.getCenterX(), this.nextActivity.getCenterY(),
		// gen);
		Rectangle2D preRect = previousActivity.getMainArea().getBounds2D();
		Rectangle2D nextRect = nextActivity.getMainArea().getBounds2D();
		// double[][] tDotFirst = WfMath.getLineCoord(getRad(preRect,
		// cornerPoints
		// .get(1).getX(), cornerPoints.get(1).getY()), preRect
		// .getCenterX(), preRect.getCenterY(),
		// cornerPoints.get(1).getX(), cornerPoints.get(1).getY(), gen);//
		// ���ʵ�ʵ���ֹ����
		// double[][] tDotLast = WfMath.getLineCoord(getRad(nextRect,
		// cornerPoints
		// .get(cornerPoints.size() - 2).getX(), cornerPoints.get(
		// cornerPoints.size() - 2).getY()), cornerPoints.get(
		// cornerPoints.size() - 2).getX(), cornerPoints.get(
		// cornerPoints.size() - 2).getY(), nextRect.getCenterX(),
		// nextRect.getCenterY(), gen);
		Point2D firstInsectPoint = getInsectPoint(preRect, cornerPoints.get(1));
		Point2D nextInsectPoint = getInsectPoint(nextRect, cornerPoints
				.get(cornerPoints.size() - 2));
		Point2D firstPoint = cornerPoints.get(0);
		// firstPoint.setLocation(tDotFirst[0][0], tDotFirst[0][1]);
		firstPoint.setLocation(firstInsectPoint);
		Point2D lastPoint = cornerPoints.get(cornerPoints.size() - 1);
		// lastPoint.setLocation(tDotLast[1][0], tDotLast[1][1]);
		lastPoint.setLocation(nextInsectPoint);

		// ������

		for (int i = 1; i < cornerPoints.size(); i++) {
			autoAlign(g2, pb, i);
			g2.draw(new Line2D.Double(cornerPoints.get(i - 1), cornerPoints
					.get(i)));
		}

		WfMath.drawArrow(g2, cornerPoints);// ����ͷ

		if (!currentCenterPoint.isEmpty()) {
			g2.setColor(Color.ORANGE);
			g2.fillOval((int) currentCenterPoint.getCenterPoint().getX()
					- CENTERPOINT_RADIUS, (int) currentCenterPoint
					.getCenterPoint().getY()
					- CENTERPOINT_RADIUS, CENTERPOINT_RADIUS * 2,
					CENTERPOINT_RADIUS * 2);
		}
		// ����������Ϣ
		drawCondition(g2, pb);
		previousConerCenter = computeCornerCenter();
	}

	protected void autoAlign(Graphics2D g2, PaintBoardBasic pb, int index) {
		// ����ͼ�β���Ҫ�Զ�����
	}

	private double[] computeCornerCenter() {
		double stringX = 0, stringY = 0;
		for (Point2D p : cornerPoints) {
			stringX = stringX + p.getX();
			stringY = stringY + p.getY();
		}
		double[] d = { stringX / cornerPoints.size(),
				stringY / cornerPoints.size() };
		return d;
	}

	/**
	 * ������������
	 * 
	 * @param g2
	 * @param pb
	 */
	private void drawCondition(Graphics2D g2, PaintBoardBasic pb) {
		if (textShape.getValue() != null
				&& !textShape.getValue().trim().equals("")
				&& !pb.getSelectedShapes().contains(textShape)) {

			double[] currentCornerCenter = computeCornerCenter();
			if (textShape.getX() == 0 && textShape.getY() == 0) {
				int stringWidth = SwingUtilities.computeStringWidth(g2
						.getFontMetrics(), textShape.getValue());
				// stringShape.x = currentCornerCenter[0] - stringWidth / 2;
				textShape.setX(currentCornerCenter[0] - stringWidth / 2);
				// stringShape.y = currentCornerCenter[1] - 10;
				textShape.setY(currentCornerCenter[1] - 10);
			}
			if (pb.isDragging()) {
				textShape.setX(textShape.getX() + currentCornerCenter[0]
						- previousConerCenter[0]);

				textShape.setY(textShape.getY() + currentCornerCenter[1]
						- previousConerCenter[1]);
			}
		}
	}

	private double[] previousConerCenter = new double[2];

	/*
	 * ���� Javadoc��
	 * 
	 * @see nci.graph.shape.BasicShape#isOnRange(double, double, double)
	 */
	public boolean isOnRange(double wx, double wy, double e) {
		for (int i = 1; i < cornerPoints.size(); i++) {
			Point2D p0 = cornerPoints.get(i - 1);
			Point2D p1 = cornerPoints.get(i);
			boolean flag = WfMath.isDotOnLine(wx, wy, p0.getX(), p0.getY(), p1
					.getX(), p1.getY(), Math.max(e,
					GlobalConstants.LINE_WIDTH * 2.0));
			if (flag) {
				return true;
			}
		}
		return false;

	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nci.graph.shape.BasicShape#isInRect(double[], double[], double[],
	 * double[])
	 */
	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4) {

		boolean flag = true;
		for (int i = 0; i < cornerPoints.size(); i++) {
			flag = flag
					& WfMath.isDotInRect(cornerPoints.get(i).getX(),
							cornerPoints.get(i).getY(), p1, p2, p3, p4);
		}
		return flag;
	}

	public WofoTransitionBean getTransitionBean() {
		return (WofoTransitionBean) wofoBean;
	}

	public void setTransitionBean(WofoTransitionBean transitionBean) {
		this.wofoBean = transitionBean;
		this.cornerPoints.clear();
		this.cornerPoints.addAll(Functions.parseCornerPoints(transitionBean
				.getAnchors()));
	}

	/**
	 * ����Ǩ������
	 * 
	 * @param name
	 */
	public void setTransitionName(String name) {
		getTransitionBean().setTransitionName(name);
		textShape.setValue(name);
	}

	@Override
	public AbstractShape cloneShape() {
		return null;
	}

	/**
	 * �жϸõ��Ƿ��������ĵ��λ�÷�Χ�ڣ�Ϊ��֤�������㣬���ĵ㽫����3-4������
	 * 
	 * @param wxNew
	 * @param wyNew
	 * @return
	 */
	private boolean checkRangeOfCenterPoint(double wxNew, double wyNew) {
		// Point2D previousCorner = null;
		Point2D centerPoint = null;
		int centerIndex = -1;
		for (int i = 0; i < cornerPoints.size(); i++) {
			Point2D p = cornerPoints.get(i);
			if (p.distance(wxNew, wyNew) <= WfTransition.CENTERPOINT_RADIUS * 2) {
				centerPoint = p;
				centerIndex = i;
				break;
			}
		}
		if (centerPoint == null) {
			centerPoint = new Point2D.Double(wxNew, wyNew);
			// ���㵱ǰ��centerpoint���ڵ�cornerpoints�е�λ��
			for (int i = 0; i < cornerPoints.size() - 1; i++) {
				Point2D f = cornerPoints.get(i); // firstPoint
				Point2D s = cornerPoints.get(i + 1); // secondPoint
				if ((f.getX() - wxNew) * (s.getX() - wxNew) < 0
						|| (f.getY() - wyNew) * (s.getY() - wyNew) < 0) {
					// ��Ҫ��2��֮�������(������ȫ�����ϣ��ڿɽ��ܵķ����������)
					double l = new Line2D.Double(f, s)
							.ptLineDist(new Point2D.Double(wxNew, wyNew));
					if (l < CENTERPOINT_RADIUS * 2) { // �ɽ��ܵķ�������
						centerIndex = i + 1;
						break;
					}
				}
			}
		}
		int index = cornerPoints.indexOf(centerPoint);
		if (index == 0 || index == cornerPoints.size() - 1) { // ��һ�������һ���յ㲻���κδ���
			centerPoint = null;
			centerIndex = -1;
		}
		boolean centerPointChanged = currentCenterPoint.getCenterPoint() != centerPoint;
		if (currentCenterPoint.getCenterPoint() != null && centerPoint != null) {
			centerPointChanged = currentCenterPoint.getCenterPoint().getX() != centerPoint
					.getX()
					|| currentCenterPoint.getCenterPoint().getY() != centerPoint
							.getY();
		}
		currentCenterPoint.setCenterPoint(centerPoint);
		currentCenterPoint.setTrans(this);
		currentCenterPoint.setCenterIndex(centerIndex);
		return centerPointChanged;
	}

	public List<Point2D> getCornerPoints() {
		return cornerPoints;
	}

	/**
	 * ��ָ��λ�õ�ת������ȥ��ָ�������ĵ�ֻ��λ�õ㣬����������ת�������
	 * 
	 * @param removedPoint
	 * @return
	 */
	public boolean removeCornerPoint(Point2D removedPoint) {
		boolean flag = false;
		for (Point2D p : cornerPoints) {
			if (p.getX() == removedPoint.getX()
					&& p.getY() == removedPoint.getY()) {
				cornerPoints.remove(p);
				flag = true;
				break;
			}
		}
		return flag;
	}

	public WfTextShapeBasic getTextShape() {
		return textShape;
	}

	public void setTextShape(WfTextShapeBasic stringNew) {
		textShape = stringNew;
	}

	private XComparator xc = new XComparator();
	private YComparator yc = new YComparator();
	// ���ڼ���x����Ĺսǵ㼯��
	private List<Point2D> xcList = new ArrayList<Point2D>();
	// ���ڼ���y����Ĺսǵ㼯��
	private List<Point2D> ycList = new ArrayList<Point2D>();

	@Override
	public double getMaxXPos() {
		xcList.clear();
		ycList.clear();
		xcList.addAll(cornerPoints);
		ycList.addAll(cornerPoints);
		Collections.sort(xcList, xc);
		Collections.sort(ycList, yc);
		return xcList.get(xcList.size() - 1).getX();
	}

	@Override
	public double getMaxYPos() {
		return ycList.get(ycList.size() - 1).getY();
	}

	@Override
	public double getMinXPos() {
		return xcList.get(0).getX();
	}

	@Override
	public double getMinYPos() {
		return ycList.get(0).getY();
	}

	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			boolean centerPointChanged = checkRangeOfCenterPoint((double) board
					.getWxNew(), (double) board.getWyNew());
			if (centerPointChanged) {
				board.repaint();
			}
		}
	}

	public void mouseEntered(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = true;
		board.repaint();
	}

	public void mouseExited(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = false;
		currentCenterPoint.clear();
		board.repaint();
	}

	private class XComparator implements Comparator<Point2D> {

		public int compare(Point2D o1, Point2D o2) {
			if (o1.getX() == o2.getX()) {
				return 0;
			}
			return o1.getX() > o2.getX() ? 1 : -1;
		}

	}

	private class YComparator implements Comparator<Point2D> {

		public int compare(Point2D o1, Point2D o2) {
			if (o1.getY() == o2.getY()) {
				return 0;
			}
			return o1.getY() > o2.getY() ? 1 : -1;
		}

	}

	@Override
	public void trans(double x, double y) {
		for (int i = 1; i < cornerPoints.size() - 1; i++) {
			Point2D p = cornerPoints.get(i);
			p.setLocation(p.getX() + x, p.getY() + y);
		}

	}

	public void mouseReleased(MouseEvent e, PaintBoardBasic board) {
		getTransitionBean().setWordsPos(
				getTextShape().getX() + "," + getTextShape().getY());
		StringBuffer anchor = new StringBuffer();
		for (int i = 0; i < cornerPoints.size(); i++) {
			Point2D p = cornerPoints.get(i);
			if (i > 0) {
				anchor.append("|");
			}
			anchor.append(p.getX()).append(",").append(p.getY());
		}
		getTransitionBean().setAnchors(anchor.toString());
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		// DO NOTHIGN
	}

	public void removeShape(PaintBoardBasic board) {
		super.removeShape(board);
		board.getGraphVector().remove(textShape);
	}

	public void addShape(PaintBoardBasic board) {
		List<AbstractShape> existedShapes = board.getGraphVector();
		existedShapes.add(this);
		if (existedShapes.contains(textShape)) {
			new Exception("�����ظ���TextShape:" + textShape.getValue())
					.printStackTrace();
		} else {
			existedShapes.add(textShape);
		}
	}

	private class RadBean {
		// ��ˮƽ�ཻ���Ǵ�ֱ�ཻ��ˮƽ�ཻΪtrue
		private boolean horizontalInsect = false;
		// �Ǹ�����ֵ������ͼ�ε���Ч�����������ģ���ֵ��ʾͼ�����ĵ㵽Ǩ����ͼ�ν���ľ���
		private double rad = -1;
		// �������ܳ�
		private double wholeLen = -1;
		// �����xƫ�ƻ�yƫ�ƣ���horizontalInsect��ֵ�йأ�horizontalInsectΪtrueʱ������xƫ�ƣ��������y����
		private double pad = 0;
		// ����
		private Point2D insectPoint = new Point2D.Double();
	}
}
