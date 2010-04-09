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
 * �����ͼ����ֻʵ����ʾ�����漰ҵ��
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
	 * logo �Ĵ�С
	 */
	protected static int logoSize = 20;

	/**
	 * logo��ͼ�α߽�ļ��
	 */
	protected static int logoPad = 6;

	public static final int DEFAULT_CENTERSHAPE_WIDTH = 84;

	public static final int DEFAULT_CENTERSHAPE_HEIGHT = 64;

	/**
	 * ͼ�εĿ��
	 */
	protected double width = DEFAULT_CENTERSHAPE_WIDTH,
			height = DEFAULT_CENTERSHAPE_HEIGHT;

	/**
	 * �Ƿ����������ק
	 */
	protected boolean resizable = false;

	/**
	 * ��ͼ�Ļ�������
	 */
	protected GeneralPath mainArea = new GeneralPath();
	/**
	 * ͼ�����Ͻ�
	 */
	protected GeneralPath topLeftArea = new GeneralPath();

	/**
	 * ͼ�����Ͻ�,�ݶ�3���������
	 */
	protected GeneralPath[] topRightAreas = new GeneralPath[] {
			new GeneralPath(), new GeneralPath(), new GeneralPath() };

	protected String[] topRightSituations = new String[topRightAreas.length];

	/**
	 * ���ϽǱ����ɫ
	 */
	protected static Color topLeftAreaColor = new Color(99, 80, 190);

	/**
	 * �����������С���
	 */
	protected static final int MIN_WIDTH = 50;

	/**
	 * �����������С�߶�
	 */
	protected static final int MIN_HEIGHT = 50;

	/**
	 * ʵ�ʻ���ͼ��ʱ��x��yƫ������ֵ������ƫ�ƣ�ʹͼ�β�ֱ�Ӵ�l,tλ�û��ƣ�����ƫ�ƺ��λ�û��ƣ������߼���ķ���
	 */
	protected int padX = 10;

	protected int padY = 10;

	/**
	 * ���Ͻ�ͼ����ʾ����ţ������ǰGeneralPathΪ�գ��򲻵���
	 */
	protected int topRightShowIndex = 0;

	/**
	 * ���Ͻ�ͼ��������µ����
	 */
	protected int topRightUnderMouseIndex = -1;

	/**
	 * ���Ͻ�ͼ���Ƿ�������µı�ǩ
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
	 * ��������������Ļ
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

		/******************** �������� **********************/

		drawTextCenterShape(activityBean.getActivityName(), mainArea
				.getBounds2D().getWidth(), mainArea.getBounds2D().getHeight(),
				mainArea.getBounds2D().getX(), mainArea.getBounds2D().getY(),
				g2);
	}

	/**
	 * ������ͼ��
	 * 
	 * @param g2
	 * @param l
	 *            ʵ�ʵ�x
	 * @param t
	 *            ʵ�ʵ�y
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
		/******************** �������� ���� **********************/
		g2.draw(mainArea);
	}

	/**
	 * ��ȡ�ͼ��
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
	 * ���������ڵײ��Ļ
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
	 * ���������ͼ��
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
	 * �����Զ��ͼ��
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createAutoShape(double l, double t, Graphics2D g) {
		int pointsCount = 6;// 6����
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
	 * ������֧�ۺ�ͼ��
	 * 
	 * @param l
	 * @param t
	 * @return
	 */
	protected void createJoinSplitShape(double l, double t, Graphics2D g) {
		int pointsCount = 4;// 6����
		GeneralPath path = new GeneralPath();
		double outerAngleIncrement = 2 * Math.PI / pointsCount;

		double outerAngle = 0.0;
		double innerAngle = outerAngleIncrement / 2.0;
		int pad = 16; // Ϊ������Ч������Ҫѹ��һ��
		int outerRadius = (int) width / 2 + pad;
		// �߾�����10
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
	 * ����������ͼ��
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
		// ����Сͼ��
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
		// ��ߵ�һ������
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		g.fill(new Ellipse2D.Double(x1 - radius / 2, y1 - radius / 2, radius,
				radius));
		x1 = x2;
		y1 = y2;
		x2 = x1;
		y2 = y2 - wh / 24;
		// �м����ϵ�����
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		y2 = y2 + wh / 12;
		// �м����µ�����
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		// x1=x1;
		y1 = y1 - wh / 24;
		x2 = x1 + wh / 12;
		y2 = y1;
		// �����ϵĺ���
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		g.fill(new Ellipse2D.Double(x2 - radius / 2, y2 - radius / 2, radius,
				radius));
		// x1=x1;
		y1 = y1 + wh / 12;
		// x2=x1+8;
		y2 = y1;
		// ���µĺ���
		g.draw(new Line2D.Double(x1, y1, x2, y2));
		g.fill(new Ellipse2D.Double(x2 - radius / 2, y2 - radius / 2, radius,
				radius));
	}

	/**
	 * �����˹��ͼ��
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
		// �������γ���
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
		// ����ҵ������Ҫչʾ��Сͼ�꣬for test
		showSituationIcon(g, "attachments");
		showSituationIcon(g, "attachments");
		showSituationIcon(g, "messages");
	}

	/**
	 * ���������εĿ�ʼͼ��
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
		// һ�������ɫ�������д��
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
	 * ����Բ�εĽ���ͼ��
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
		// ʵ�ʵ���Բ�߶���endHeight
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
		// idҲӦ�ñ����������Ӱ�쵽���ݿ�id����
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
	 * ��ȡͼ�ο��
	 * 
	 * @return
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * ����ͼ�ο��
	 * 
	 * @param width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * �Ƿ���Ե�����С
	 * 
	 * @return
	 */
	public boolean isResizable() {
		return resizable;
	}

	/**
	 * �����Ƿ���Ե�����С
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
	 * ��ȡX���ĵ�
	 * 
	 * @return
	 */
	public double getCenterX() {
		return mainArea.getBounds2D().getCenterX();
	}

	/**
	 * ��ȡY���ĵ�
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
	 * ���¼������ϽǵĻ�������
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
	 * ��ʾ���Ͻ�ͼ�곡��
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
