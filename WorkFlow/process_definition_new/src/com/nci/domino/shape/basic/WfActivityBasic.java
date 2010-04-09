package com.nci.domino.shape.basic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.SwingUtilities;

import com.nci.domino.GlobalConstants;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;
import com.nci.domino.shape.WfActivity;
import com.nci.domino.shape.activity.topicon.WfActivityIconSituation;

/**
 * 基本活动图对象，只实现显示，不涉及业务
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityBasic extends SimpleLocationShape {

	private static final long serialVersionUID = -7256623302552716425L;

	public static final String ACT_TYPE_CENTER = "center";
	public static final String ACT_TYPE_BOTTOM = "bottom";

	protected static Color from = new Color(240, 240, 240), to = new Color(185,
			215, 235);

	/**
	 * logo 的大小
	 */
	protected static int logoSize = 20;

	/**
	 * logo与图形边界的间距
	 */
	protected static int logoPad = 6;

	public static final int DEFAULT_CENTERSHAPE_WIDTH = 84;

	public static final int DEFAULT_CENTERSHAPE_HEIGHT = 64;

	/**
	 * 图形的宽高
	 */
	protected double width = DEFAULT_CENTERSHAPE_WIDTH,
			height = DEFAULT_CENTERSHAPE_HEIGHT;

	/**
	 * 是否可以伸缩拖拽
	 */
	protected boolean resizable = false;

	/**
	 * 主图的绘制区域
	 */
	protected GeneralPath mainArea = new GeneralPath();
	/**
	 * 图形左上角
	 */
	protected GeneralPath topLeftArea = new GeneralPath();

	/**
	 * 图形右上角,暂定3个组合区间
	 */
	protected GeneralPath[] topRightAreas = new GeneralPath[] {
			new GeneralPath(), new GeneralPath(), new GeneralPath() };

	protected String[] topRightSituations = new String[topRightAreas.length];

	/**
	 * 左上角标记颜色
	 */
	protected static Color topLeftAreaColor = new Color(99, 80, 190);

	/**
	 * 拉伸允许的最小宽度
	 */
	protected static final int MIN_WIDTH = 50;

	/**
	 * 拉伸允许的最小高度
	 */
	protected static final int MIN_HEIGHT = 50;

	/**
	 * 实际绘制图形时，x，y偏移量的值，经过偏移，使图形不直接从l,t位置绘制，而是偏移后的位置绘制，用于线计算的方便
	 */
	protected int padX = 10;

	protected int padY = 10;

	/**
	 * 右上角图标显示的序号，如果当前GeneralPath为空，则不递增
	 */
	protected int topRightShowIndex = 0;

	/**
	 * 右上角图标在鼠标下的序号
	 */
	protected int topRightUnderMouseIndex = -1;

	/**
	 * 右上角图标是否在鼠标下的标签
	 */
	protected boolean topRightUnderMouse = false;

	public WfActivityBasic(double x, double y, String activityType) {
		wofoBean = new WofoActivityBean(Functions.getUID());
		this.x = x;
		this.y = y;
		((WofoActivityBean) wofoBean).setActivityType(activityType);
	}

	public void drawShape(Graphics2D g2, PaintBoardBasic pb) {
		mainArea.reset();
		topLeftArea.reset();
		for (GeneralPath a : topRightAreas) {
			a.reset();
		}
		topRightShowIndex = 0;
		if (ACT_TYPE_CENTER.equals(pb.getActivityShapeStyle())) {
			drawCenterActShape(g2, pb);
		} else {
			drawBottomActShape(g2, pb);
		}
		// topRightUnderMouseIndex = -1;
	}

	/**
	 * 绘制文字在中央的活动
	 * 
	 * @param g2
	 * @param pb
	 */
	protected void drawCenterActShape(Graphics2D g2, PaintBoardBasic pb) {
		// GlobalConstants.SYMBOL_SIZE = DEFAULT_CENTERSHAPE_HEIGHT;
		// GlobalConstants.SYMBOL_RAD = GlobalConstants.SYMBOL_SIZE / 2;
		g2.setFont(GlobalConstants.ACTTEXTFONT);
		g2.setColor(Color.black);
		if (selected)
			g2.setStroke(GlobalConstants.DASH_LINE_STROKE);
		else {
			g2.setStroke(GlobalConstants.SMALL_LINE_STROKE);
		}
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		String activityType = activityBean.getActivityType();
		double l = x - width / 2.0 + 10;
		double t = y - height / 2.0 + 10;
		if (WofoActivityBean.ACTIVITY_TYPE_BEGIN.equals(activityType)) {
			createStartShape(l, t, g2);
		} else if (WofoActivityBean.ACTIVITY_TYPE_END.equals(activityType)) {
			createEndCircle(l, t, g2);
		} else if (WofoActivityBean.ACTIVITY_TYPE_AUTO.equals(activityType)) {
			createAutoShape(l, t, g2);
		} else if (WofoActivityBean.ACTIVITY_TYPE_HUMAN.equals(activityType)) {
			createHumanShape(l, t, g2);
		} else if (WofoActivityBean.ACTIVITY_TYPE_JOIN.equals(activityType)) {
			createJoinSplitShape(l, t, g2);
		} else if (WofoActivityBean.ACTIVITY_TYPE_SPLIT.equals(activityType)) {
			createJoinSplitShape(l, t, g2);
		} else if (WofoActivityBean.ACTIVITY_TYPE_SUBFLOW.equals(activityType)) {
			createSubflowShape(l, t, g2);
		} else {
			createRoundRect(l, t, g2);
		}

		/******************** 渐进绘制 **********************/

		drawTextCenterShape(activityBean.getActivityName(), mainArea
				.getBounds2D().getWidth(), mainArea.getBounds2D().getHeight(),
				mainArea.getBounds2D().getX(), mainArea.getBounds2D().getY(),
				g2);
	}

	/**
	 * 绘制主图形
	 * 
	 * @param g2
	 * @param l
	 *            实际的x
	 * @param t
	 *            实际的y
	 */
	protected void drawMainCenterArea(Graphics2D g2, double l, double t) {
		Rectangle rect = mainArea.getBounds();
		Paint old = g2.getPaint();
		GradientPaint paint = new GradientPaint((int) l, (int) t + rect.height
				/ 2, isUnderMouse ? backColor_from_undermouse : from, (int) l,
				rect.height + (int) t, isUnderMouse ? backColor_to_undermouse
						: to, true);
		g2.setPaint(paint);
		g2.fill(mainArea);
		g2.setPaint(old);
		/******************** 渐进绘制 结束 **********************/
		g2.draw(mainArea);
	}

	/**
	 * 获取活动图像
	 */
	public Image getActivityImage() {
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		String defaultIcon = "workflow_activity_"
				+ activityBean.getActivityType() + ".gif";
		if (activityBean.getActivityIcon() != null
				&& !activityBean.getActivityIcon().trim().equals("")) {
			defaultIcon = activityBean.getActivityIcon();
		}
		return Functions.getImageIcon(defaultIcon).getImage();
	}

	/**
	 * 绘制文字在底部的活动
	 * 
	 * @param g2
	 * @param pb
	 */
	protected void drawBottomActShape(Graphics2D g2, PaintBoardBasic pb) {
		g2.setColor(Color.BLACK);
		g2.setFont(GlobalConstants.ACTTEXTFONT);
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		double l = x - GlobalConstants.SYMBOL_SIZE / 2.0;
		double t = y - GlobalConstants.SYMBOL_SIZE / 2.0;

		g2.drawImage(getActivityImage(), WfMath.doubleToInt(l), WfMath
				.doubleToInt(t), pb);
		g2.setColor(Color.black);
		int stringLength = SwingUtilities.computeStringWidth(g2
				.getFontMetrics(), activityBean.getActivityName());
		float px = (float) (l - (stringLength - GlobalConstants.SYMBOL_SIZE) / 2);
		float py = (float) (t + GlobalConstants.SYMBOL_SIZE + 15);
		if (activityBean.getActivityName() != null) {
			g2.drawString(activityBean.getActivityName(), px, py);
		}
		if (selected) {
			g2.setStroke(GlobalConstants.SDASH_LINE_STROKE);
			g2.draw(new Rectangle2D.Double(l - 1, t - 1,
					GlobalConstants.SYMBOL_SIZE + 2,
					GlobalConstants.SYMBOL_SIZE + 2));
		}
	}

	/**
	 * 创建基本活动图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createRoundRect(double l, double t, Graphics2D g) {
		RoundRectangle2D roundRect = new RoundRectangle2D.Double(l, t,
				width - 20, height - 20, width / 5, height / 5);
		mainArea.append(roundRect, false);
		drawMainCenterArea(g, l, t);
	}

	/**
	 * 创建自动活动图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createAutoShape(double l, double t, Graphics2D g) {
		int pointsCount = 6;// 6边形
		GeneralPath path = new GeneralPath();
		double outerAngleIncrement = 2 * Math.PI / pointsCount;

		double outerAngle = 0.0;
		double innerAngle = outerAngleIncrement / 2.0;
		int outerRadius = (int) width / 2;
		//
		l += outerRadius;
		t += outerRadius;

		float x1 = (float) (Math.cos(outerAngle) * outerRadius + l);
		float y1 = (float) (Math.sin(outerAngle) * outerRadius + t);

		path.moveTo(x1, y1);
		path.lineTo(x1, y1);

		outerAngle += outerAngleIncrement;
		innerAngle += outerAngleIncrement;

		for (int i = 1; i < pointsCount; i++) {
			x1 = (float) (Math.cos(outerAngle) * outerRadius + l);
			y1 = (float) (Math.sin(outerAngle) * outerRadius + t);

			path.lineTo(x1, y1);

			outerAngle += outerAngleIncrement;
			innerAngle += outerAngleIncrement;
		}
		path.closePath();
		mainArea.append(path, false);
		drawMainCenterArea(g, l, t);
	}

	/**
	 * 创建分支聚合图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createJoinSplitShape(double l, double t, Graphics2D g) {
		int pointsCount = 4;// 6边形
		GeneralPath path = new GeneralPath();
		double outerAngleIncrement = 2 * Math.PI / pointsCount;

		double outerAngle = 0.0;
		double innerAngle = outerAngleIncrement / 2.0;
		int pad = 16; // 为了菱形效果，需要压缩一下
		int outerRadius = (int) width / 2 + pad;
		// 边距缩短10
		l = l - pad;
		t = t - pad;

		//
		l += outerRadius;
		t += outerRadius;

		float x1 = (float) (Math.cos(outerAngle) * outerRadius + l);
		float y1 = (float) (Math.sin(outerAngle) * outerRadius + t);

		path.moveTo(x1, y1);
		path.lineTo(x1, y1);

		outerAngle += outerAngleIncrement;
		innerAngle += outerAngleIncrement;

		for (int i = 1; i < pointsCount; i++) {
			if (i % 2 == 1) {
				outerRadius = (int) width / 2 - pad / 3;
			} else {
				outerRadius = (int) width / 2 + pad;
			}
			x1 = (float) (Math.cos(outerAngle) * outerRadius + l);
			y1 = (float) (Math.sin(outerAngle) * outerRadius + t);

			path.lineTo(x1, y1);
			outerAngle += outerAngleIncrement;
			innerAngle += outerAngleIncrement;
		}
		path.closePath();
		mainArea.append(path, false);
		drawMainCenterArea(g, l, t);
	}

	/**
	 * 创建子流程图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createSubflowShape(double l, double t, Graphics2D g) {
		t = t + 5;
		l = l - 5;
		RoundRectangle2D roundRect = new RoundRectangle2D.Double(l, t, width,
				height - 10, 0, 0);
		mainArea.append(roundRect, false);
		drawMainCenterArea(g, l, t);
		// 绘制小图标
		g.setColor(topLeftAreaColor);
		g.setStroke(GlobalConstants.SMALL_LINE_STROKE);

		double wh = DEFAULT_CENTERSHAPE_HEIGHT;
		if (PaintBoardBasic.topIconResizable) {
			wh = width > height ? height : width;
		}
		double radius = wh / 24;
		double x1 = l + radius;
		double y1 = t + radius * 3;
		double x2 = l + radius * 2.5;
		double y2 = t + radius * 3;
		// 左边第一条横线
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		g.fill(new Ellipse2D.Double(x1 - radius / 2, y1 - radius / 2, radius,
				radius));
		x1 = x2;
		y1 = y2;
		x2 = x1;
		y2 = y2 - wh / 24;
		// 中间往上的竖线
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		y2 = y2 + wh / 12;
		// 中间往下的竖线
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		// x1=x1;
		y1 = y1 - wh / 24;
		x2 = x1 + wh / 12;
		y2 = y1;
		// 右右上的横线
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		g.fill(new Ellipse2D.Double(x2 - radius / 2, y2 - radius / 2, radius,
				radius));
		// x1=x1;
		y1 = y1 + wh / 12;
		// x2=x1+8;
		y2 = y1;
		// 右下的横线
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		g.fill(new Ellipse2D.Double(x2 - radius / 2, y2 - radius / 2, radius,
				radius));
	}

	/**
	 * 绘制人工活动图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createHumanShape(double l, double t, Graphics2D g) {
		// t = t + 5;
		// l = l - 5;
		RoundRectangle2D roundRect = new RoundRectangle2D.Double(l, t, width,
				height, width / 5, height / 5);
		mainArea.append(roundRect, false);
		// 画个人形出来
		drawMainCenterArea(g, l, t);
		g.setColor(topLeftAreaColor);
		double relativeWH = DEFAULT_CENTERSHAPE_WIDTH;
		if (PaintBoardBasic.topIconResizable) {
			relativeWH = width > height ? height : width;
		}
		double iconWidth = relativeWH / 8;
		double iconHeight = iconWidth + relativeWH / 14;
		double radiusS = iconHeight - iconWidth;
		double radiusL = iconWidth;
		double x1 = l + relativeWH / 10;
		double y1 = t + relativeWH / 10;
		double x2 = x1;
		double y2 = y1 + radiusS + 2;
		topLeftArea.append(new Ellipse2D.Double(x1 - radiusS / 2, y1 - radiusS
				/ 2, radiusS, radiusS), false);
		topLeftArea.append(new Ellipse2D.Double(x2 - radiusL / 2, y2 - radiusL
				/ 2, radiusL, radiusL / 2), false);
		g.fill(topLeftArea);
		// 其它业务上需要展示的小图标，for test
		showSituationIcon(g, "attachments");
		showSituationIcon(g, "attachments");
		showSituationIcon(g, "messages");
	}

	/**
	 * 创建三角形的开始图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createStartShape(double l, double t, Graphics2D g) {
		int pad = 10;
		double startWidth = width - pad;
		mainArea.append(new Ellipse2D.Double(l, t, startWidth, startWidth),
				false);
		drawMainCenterArea(g, l, t);
		// 一个渐变的色调，随便写的
		GradientPaint paint = new GradientPaint((int) l + 5, (int) t + 5
				+ (int) width / 2, isUnderMouse ? new Color(78, 218, 42)
				: backColor_from_undermouse, (int) l, (int) width + (int) t,
				isUnderMouse ? new Color(230, 230, 230) : new Color(43, 213,
						102), true);
		g.setPaint(paint);
		double wh = startWidth - 20;
		g.fill(new Ellipse2D.Double(l + startWidth / 2 - wh / 2, t + startWidth
				/ 2 - wh / 2, wh, wh));
	}

	/**
	 * 创建圆形的结束图形
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createEndCircle(double l, double t, Graphics2D g) {
		double endHeight = height - 5;
		mainArea.append(new Ellipse2D.Double(l, t, width, endHeight), false);
		drawMainCenterArea(g, l, t);
		GradientPaint paint = new GradientPaint((int) l + 5, (int) t + 5
				+ (int) width / 2, isUnderMouse ? new Color(218, 88, 112)
				: backColor_from_undermouse, (int) l, (int) width + (int) t,
				isUnderMouse ? new Color(230, 230, 230) : new Color(218, 88,
						112), true);
		g.setPaint(paint);
		// 实际的椭圆高度是endHeight
		double wh = (width > endHeight ? endHeight : width) - 10;
		g.fill(new Ellipse2D.Double(l + width / 2 - wh / 2, t + endHeight / 2
				- wh / 2, wh, wh));
	}

	@Override
	public void mouseEntered(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = true;
		board.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e, PaintBoardBasic board) {
		isUnderMouse = false;
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			board.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		board.repaint();
	}

	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
		super.mouseMoved(e, board);
		boolean tempTopRightUnderMouse = false;
		// int tempIndex = -2;
		for (int i = 0; i < topRightAreas.length; i++) {
			if (topRightAreas[i].getBounds2D().contains(e.getPoint())) {
				tempTopRightUnderMouse = true;
				topRightUnderMouseIndex = i;
				break;
			}
		}
		if (!tempTopRightUnderMouse) {
			topRightUnderMouseIndex = -1;
		}
		if (tempTopRightUnderMouse != topRightUnderMouse) {
			topRightUnderMouse = tempTopRightUnderMouse;
			board.repaint();
		}
	}

	@Override
	public AbstractShape cloneShape() {
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		WfActivity newOne = new WfActivity(x, y, activityBean.getActivityType());
		WofoActivityBean newBean = activityBean.cloneActivity();
		// id也应该保留，否则会影响到数据库id关联
		// newBean.setActivityId(Functions.getUID());
		newOne.setWofoBean(newBean);
		return newOne;
	}

	@Override
	public double getMaxXPos() {
		return x + GlobalConstants.SYMBOL_RAD;
	}

	@Override
	public double getMaxYPos() {
		return y + GlobalConstants.SYMBOL_RAD;
	}

	@Override
	public double getMinXPos() {
		return x - GlobalConstants.SYMBOL_RAD;
	}

	@Override
	public double getMinYPos() {
		return y - GlobalConstants.SYMBOL_RAD;
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		// DO NOTHING
	}

	@Override
	public void trans(double x, double y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}

	@Override
	public void updatePaste(PaintBoardBasic board) {
		// DO NOTHING
	}

	public boolean isOnRange(double wx, double wy, double e) {
		return mainArea.contains(wx, wy);
		// return WfMath.getVecLen(x - wx, y - wy) < GlobalConstants.SYMBOL_RAD;
	}

	public boolean isInRect(double[] p1, double[] p2, double[] p3, double[] p4) {
		return WfMath.isDotInRect(x, y, p1, p2, p3, p4);
	}

	@Override
	public void setX(double x) {
		if (x < GlobalConstants.SYMBOL_SIZE / 2) {
			this.x = GlobalConstants.SYMBOL_SIZE / 2;
		} else
			this.x = x;
	}

	@Override
	public void setY(double y) {
		if (y < GlobalConstants.SYMBOL_SIZE / 2) {
			this.y = GlobalConstants.SYMBOL_SIZE / 2;
		} else
			this.y = y;
	}

	/**
	 * 获取图形宽度
	 * 
	 * @return
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * 设置图形宽度
	 * 
	 * @param width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * 是否可以调整大小
	 * 
	 * @return
	 */
	public boolean isResizable() {
		return resizable;
	}

	/**
	 * 设置是否可以调整大小
	 * 
	 * @param resizable
	 */
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public String toString() {
		return wofoBean.toString();
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * 获取X中心点
	 * 
	 * @return
	 */
	public double getCenterX() {
		return mainArea.getBounds2D().getCenterX();
	}

	/**
	 * 获取Y中心点
	 * 
	 * @return
	 */
	public double getCenterY() {
		return mainArea.getBounds2D().getCenterY();
	}

	public int getPadX() {
		return padX;
	}

	public void setPadX(int padX) {
		this.padX = padX;
	}

	public int getPadY() {
		return padY;
	}

	public void setPadY(int padY) {
		this.padY = padY;
	}

	public GeneralPath getMainArea() {
		return mainArea;
	}

	/**
	 * 重新计算右上角的绘制索引
	 */
	public void reIndexTopRight() {
		for (int i = 0; i < topRightAreas.length; i++) {
			if (topRightAreas[i].getBounds2D().getWidth() != 0
					|| topRightAreas[i].getBounds2D().getHeight() != 0) {
				topRightShowIndex = i + 1;
			}
		}
	}

	/**
	 * 显示右上角图标场景
	 * 
	 * @param g
	 * @param situationName
	 */
	public void showSituationIcon(Graphics2D g, String situationName) {
		reIndexTopRight();
		WfActivityIconSituation iconSituation = PaintBoardBasic.topRightConfig
				.get(situationName.toLowerCase());
		topRightSituations[topRightShowIndex] = situationName.toLowerCase();
		iconSituation.getIconCreator().paint(g, this);
	}

	public int getTopRightShowIndex() {
		return topRightShowIndex;
	}

	public void setTopRightShowIndex(int topRightShowIndex) {
		this.topRightShowIndex = topRightShowIndex;
	}

	public int getTopRightUnderMouseIndex() {
		return topRightUnderMouseIndex;
	}

	public void setTopRightUnderMouseIndex(int topRightUnderMouseIndex) {
		this.topRightUnderMouseIndex = topRightUnderMouseIndex;
	}

	public boolean isTopRightUnderMouse() {
		return topRightUnderMouse;
	}

	public void setTopRightUnderMouse(boolean topRightUnderMouse) {
		this.topRightUnderMouse = topRightUnderMouse;
	}

	public GeneralPath getTopLeftArea() {
		return topLeftArea;
	}

	public GeneralPath[] getTopRightAreas() {
		return topRightAreas;
	}

	public String[] getTopRightSituations() {
		return topRightSituations;
	}
}
