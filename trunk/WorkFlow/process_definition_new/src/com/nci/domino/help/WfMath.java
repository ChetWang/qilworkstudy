package com.nci.domino.help;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import com.nci.domino.GlobalConstants;

/**
 * @author ������ ���ﶨ����һϵ�е���ѧ�㷨
 */
public class WfMath {
	/**
	 * �õ�ʸ���ĳ���
	 * 
	 * @param vx
	 *            (vx,vy)��ʸ��
	 * @param vy
	 * @return ʸ������
	 */
	public static double getVecLen(double vx, double vy) {
		return Math.sqrt(vx * vx + vy * vy);
	}

	/**
	 * �õ�ʸ���ĳ���
	 * 
	 * @param v
	 *            ʸ��
	 * @return ʸ������
	 */
	public static double getVecLen(double[] v) {
		return getVecLen(v[0], v[1]);
	}

	/**
	 * �õ�����ľ���
	 * 
	 * @param p1
	 *            p1 p2������
	 * @param p2
	 * @return ����ľ���
	 */
	public static double getPointPointDis(double[] p1, double[] p2) {
		return getVecLen(p2[0] - p1[0], p2[1] - p1[1]);
	}

	/**
	 * ��תʸ��
	 * 
	 * @param px
	 *            (px,py)��ʸ��
	 * @param py
	 * @param ang
	 *            ת�ǻ���
	 * @param newLen
	 *            �³���
	 * @return ��ʸ��
	 */
	public static double[] rotateVec(double px, double py, double ang,
			double newLen) {
		double cosang = Math.cos(ang);
		double sinang = Math.sin(ang);
		double vx = px * cosang - py * sinang;
		double vy = px * sinang + py * cosang;
		double d = getVecLen(vx, vy);
		return new double[] { vx / d * newLen, vy / d * newLen };
	}

	/**
	 * �жϵ��Ƿ���ֱ����
	 * 
	 * @param xp
	 *            (xp,yp) ��ֱ�����һ��
	 * @param yp
	 * @param x1
	 *            (x1,y1) ��ֱ�ߵ�һ���˵�
	 * @param y1
	 * @param x2
	 *            (x2,y2) ��ֱ�ߵ�һ���˵�
	 * @param y2
	 * @param epsl
	 *            �ֱ���
	 * @return ���򷵻�true
	 */
	public static boolean isDotOnLine(double xp, double yp, double x1,
			double y1, double x2, double y2, double epsl) {
		return getPointLineDis(xp, yp, x1, y1, x2, y2) < epsl
				&& (x1 - xp) * (x2 - xp) + (y1 - yp) * (y2 - yp) < 0;
	}

	/**
	 * ��㵽ֱ�ߵľ���
	 * 
	 * @param xp
	 *            (xp,yp) ��ֱ����һ��
	 * @param yp
	 * @param x1
	 *            (x1,y1)��(x2,y2)��ֱ���ϵ�����
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getPointLineDis(double xp, double yp, double x1,
			double y1, double x2, double y2) {
		double s = getVecLen(xp - x1, yp - y1);
		if (s == 0)
			return 0;
		double sarr = getVecPro(xp - x1, yp - y1, x2 - x1, y2 - y1);
		return Math.sqrt(s * s - sarr * sarr);
	}

	/**
	 * ��ʸ����ͶӰ
	 * 
	 * @param vx1
	 *            (vx1, vy1)��ʸ��v1
	 * @param vy1
	 * @param vx2
	 *            (vx2, vy2)��ʸ��v2
	 * @param vy2
	 * @return ʸ��v1��v2�ϵ�ͶӰ
	 */
	public static double getVecPro(double vx1, double vy1, double vx2,
			double vy2) {
		return getDotProduct(vx1, vy1, vx2, vy2) / getVecLen(vx2, vy2);
	}

	/**
	 * ������ʸ���ĵ��
	 * 
	 * @param vx1
	 *            (vx1, vy1)��ʸ��v1
	 * @param vy1
	 * @param vx2
	 *            (vx2, vy2)��ʸ��v2
	 * @param vy2
	 * @return ʸ���ĵ��
	 */
	public static double getDotProduct(double vx1, double vy1, double vx2,
			double vy2) {
		return vx1 * vx2 + vy1 * vy2;
	}

	/**
	 * ��ֱ�ߵ�����ֱ�� ��ͷ���ٵ�gene * Global.EX_value
	 * 
	 * @param x1
	 *            (x1,y1)�������
	 * @param y1
	 * @param x2
	 *            (x2,y2)ĩβ������
	 * @param y2
	 * @param gene
	 *            ƫ������
	 * @return �µ�ֱ��
	 */
	public static double[][] getLineCoord(double symbolRad, double x1,
			double y1, double x2, double y2, int gene) {
		double originLen = getVecLen(x2 - x1, y2 - y1);
		double lmd = symbolRad / originLen;
		double deltx = lmd * (x2 - x1);
		double delty = lmd * (y2 - y1);
		return getLineExcursion(x1 + deltx, y1 + delty, x2 - deltx, y2 - delty,
				gene * GlobalConstants.EX_value);
	}

	/**
	 * ȷ��ֱ�ߵ�ƫ����λ��
	 * 
	 * @param x1
	 *            (x1,y1),(x2,y2)��һ������ֱ��
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param moveValue
	 *            moveValue ��ƫ���� �����ɸ�
	 * @return ��ֱ��
	 */
	public static double[][] getLineExcursion(double x1, double y1, double x2,
			double y2, double moveValue) {
		double[] newCoor = rotateVec90(x2 - x1, y2 - y1, false, moveValue);
		return new double[][] { { x1 + newCoor[0], y1 + newCoor[1] },
				{ x2 + newCoor[0], y2 + newCoor[1] } };
	}

	/**
	 * һ��ʸ����תһ���ض��ĽǶ� ����ĽǶȵ�λʹ�û���
	 * 
	 * @param px
	 *            px py �ֱ���ʸ������������
	 * @param py
	 * @param ang
	 *            ����ת�Ƕ� ��λ�ǻ���
	 * @return ��ʸ��
	 */
	public static double[] rotateVec(double px, double py, double ang) {
		double cosang = Math.cos(ang);
		double sinang = Math.sin(ang);
		return new double[] { px * cosang - py * sinang,
				px * sinang + py * cosang };
	}

	/**
	 * ��ʸ���ķ����
	 * 
	 * @param vx
	 *            (vx,vy)��ʸ��
	 * @param vy
	 * @return ʸ���ķ����
	 */
	public static double getVecAng(double vx, double vy) {
		double l = getVecLen(vx, vy);
		if (l == 0)
			return 0;
		return Math.atan2(vy, vx);
	}

	/**
	 * һ����������һ������ת�� �õ�һ���µ�λ��
	 * 
	 * @param x
	 *            (x,y)����ת�ĵ�
	 * @param y
	 * @param cx
	 *            (cx,cy)��ת����
	 * @param cy
	 * @param ang
	 *            ת�� �Ի��ȶ���
	 * @return �µĵ�
	 */
	public static double[] getRotatePoint(double x, double y, double cx,
			double cy, double ang) {
		double[] reCoord = rotateVec(x - cx, y - cy, ang);
		return new double[] { cx + reCoord[0], cy + reCoord[1] };
	}

	/**
	 * �õ�x��y������ ע���������������� x = m * y + c ���� y > 0 0<=c<y
	 * 
	 * @param x
	 *            ������
	 * @param y
	 *            ����
	 * @return ������������
	 */
	public static double getResidue(double x, double y) {
		return x > 0 ? x % y : (x % y + y); // return x - Math.floor(x / y) * y;
	}

	/**
	 * (x1,y1)��(x2,y2)���߶ε������˵� (xp,yp)��ֱ��12��һ�� �жϵ�(xp,yp)�����߶��ϻ��������ӳ�����
	 * 
	 * @param xp
	 *            (xp,yp)��ֱ��12��һ��
	 * @param yp
	 * @param x1
	 *            (x1,y1)��(x2,y2)���߶ε������˵�
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return ���߶��Ϸ���true
	 */
	public static boolean isInboardLine(double xp, double yp, double x1,
			double y1, double x2, double y2) {
		return getDotProduct(xp - x1, yp - y1, xp - x2, yp - y2) < 0.0;
	}

	/**
	 * ����ʸ���Ƿ�ƽ��
	 * 
	 * @param vx1
	 *            (vx1,vy1)��һ��ʸ��
	 * @param vy1
	 * @param vx2
	 *            (vx2,vy2)����һ��ʸ��
	 * @param vy2
	 * @return ƽ�з���true
	 */
	public static boolean isParalled(double vx1, double vy1, double vx2,
			double vy2) {
		return Math.abs(getForkProduct(vx1, vy1, vx2, vy2)) < edfbl;
	}

	/**
	 * ��㵽ֱ�ߵĴ���ʸ �ӵ�ָ����
	 * 
	 * @param xp
	 *            (xp,yp) ��ֱ����һ��
	 * @param yp
	 * @param x1
	 *            (x1,y1)��(x2,y2)��ֱ���ϵ�����
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return �㵽ֱ�ߵĴ���ʸ
	 */
	public static double[] getFootVec(double xp, double yp, double x1,
			double y1, double x2, double y2) {
		if (isThreePointsOnLine(xp, yp, x1, y1, x2, y2))
			return new double[] { 0, 0 };
		double pro = getVecPro(x1 - xp, y1 - yp, x2 - x1, y2 - y1);
		double len12 = getVecLen(x2 - x1, y2 - y1);
		return new double[] { x1 - xp - (x2 - x1) / len12 * pro,
				y1 - yp - (y2 - y1) / len12 * pro };
	}

	/**
	 * ��ʸ����ʸ��
	 * 
	 * @param vx1
	 *            (vx1,vy1)��һ��ʸ��
	 * @param vy1
	 * @param vx2
	 *            (vx2,vy2)����һ��ʸ��
	 * @param vy2
	 * @return ʸ����ʸ��
	 */
	public static double getForkProduct(double vx1, double vy1, double vx2,
			double vy2) {
		return vx1 * vy2 - vx2 * vy1;
	}

	/**
	 * �ж��Ƿ����㹲��
	 * 
	 * @param xp
	 *            (x1,y1) (x2,y2) (xp,yp) ��������
	 * @param yp
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return ���߷���true
	 */
	public static boolean isThreePointsOnLine(double xp, double yp, double x1,
			double y1, double x2, double y2) {
		return Math.abs((x1 - xp) * (y2 - yp) - (x2 - xp) * (y1 - yp)) < edfbl;
	}

	/**
	 * ˫������ת��Ϊ����
	 * 
	 * @param x
	 *            ˫������
	 * @return ����
	 */
	public static int doubleToInt(double x) {
		return (int) Math.round(x);
	}

	/**
	 * �õ�ʸ��v1ת��ʸ��v2�Ľ� �û��ȱ�ʾ ��Χ������[-Math.PI,Math.PI]֮��
	 * 
	 * @param v1
	 *            һ��ʸ��
	 * @param v2
	 *            ��һ��ʸ��
	 * @return ʸ��v1ת��ʸ��v2�Ľ�
	 */
	public static double getVecToVecAng(double[] v1, double[] v2) {
		return getAngToAng(getVecAng(v2) - getVecAng(v1), -Math.PI);
	}

	/**
	 * �õ�ang�ĵȼ۽��д���minAng����С�� �û��ȱ�ʾ
	 * 
	 * @param ang
	 * @param minAng
	 * @return ����minAng����С�ȼ۽�
	 */
	public static double getAngToAng(double ang, double minAng) {
		return Math.ceil((minAng - ang) / CIRCLE_ANG) * CIRCLE_ANG + ang;
	}

	/**
	 * ����ʸ����λ�ý�
	 * 
	 * @param v
	 *            ʸ��
	 * @return ʸ����λ�ý�
	 */
	public static double getVecAng(double v[]) {
		return getVecAng(v[0], v[1]);
	}

	/**
	 * ��ת90�Ȼ���-90��
	 * 
	 * @param px
	 *            (px,py)�Ǵ���תʸ��
	 * @param py
	 * @param isPos
	 *            ��90����-90 true��ʾ90
	 * @param newLen
	 *            �µ�ʸ������
	 * @return �µ�ʸ��
	 */
	public static double[] rotateVec90(double px, double py, boolean isPos,
			double newLen) {
		double sinang = isPos ? 1 : -1;
		double npx = -py * sinang;
		double npy = px * sinang;
		double len = getVecLen(npx, npy);
		return new double[] { npx / len * newLen, npy / len * newLen };
	}

	/**
	 * x�Ƿ���ab֮��
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * @return x��ab֮�䷵��true
	 */
	public static boolean isBetween(double x, double a, double b) {
		return (x - a) * (x - b) <= 0;
	}

	/**
	 * ���Ƿ����ڱ�׼������
	 * 
	 * @param xp
	 *            (xp,yp)��ƽ���ϵ���һ��
	 * @param yp
	 * @param l
	 *            (l,t)�����Ͻ�����
	 * @param t
	 * @param w
	 *            ���
	 * @param h
	 *            �߶�
	 * @return ���ڷ���true
	 */
	public static boolean isDotInRect(double xp, double yp, double l, double t,
			double w, double h) {
		return isBetween(xp, l, l + w) && isBetween(yp, t, t + h);
	}

	/**
	 * ���Ƿ�����͹�ı�����
	 * 
	 * @param xp
	 *            (xp,yp)��ƽ���ϵ���һ��
	 * @param yp
	 * @param p1
	 *            p1 p2 p3 ��͹�ı��ε��ĸ�����
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return ���ڷ���true
	 */
	public static boolean isDotInRect(double xp, double yp, double[] p1,
			double[] p2, double[] p3, double[] p4) {
		return isDotInTriangle(xp, yp, p1, p2, p3)
				|| isDotInTriangle(xp, yp, p1, p3, p4);
	}

	/**
	 * ���Ƿ�������������
	 * 
	 * @param xp
	 *            (xp,yp)��ƽ���ϵ���һ��
	 * @param yp
	 * @param p1
	 *            p1 p2 p3 �������ε���������
	 * @param p2
	 * @param p3
	 * @return ���ڷ���true
	 */
	public static boolean isDotInTriangle(double xp, double yp, double[] p1,
			double[] p2, double[] p3) {
		double ang = getVecAng(p2[0] - p1[0], p2[1] - p1[1]);
		double p1m[] = rotateVec(p1[0], p1[1], -ang);// ��ת-ang
		double p2m[] = rotateVec(p2[0], p2[1], -ang);// ��ת-ang
		double p3m[] = rotateVec(p3[0], p3[1], -ang);// ��ת-ang
		double pt[] = rotateVec(xp, yp, -ang);// ��ת-ang
		return isBetween(pt[1], p1m[1], p3m[1])
				&& isBetween(pt[0], calInsertValue(p1m[1], p1m[0], p3m[1],
						p3m[0], pt[1]), calInsertValue(p2m[1], p2m[0], p3m[1],
						p3m[0], pt[1]));
	}

	/**
	 * ���x������[x1,x2]�ϵ����β�ֵ
	 * 
	 * @param x1
	 *            (x1,y1)�ǵ�һ��
	 * @param y1
	 * @param x2
	 *            (x2,y2)�ǵڶ���
	 * @param y2
	 * @param x
	 *            �����ֵ�����ĵ�
	 * @return ��ֵ����ֵ
	 */
	public static double calInsertValue(double x1, double y1, double x2,
			double y2, double x) {
		return (y2 - y1) / (x2 - x1) * (x - x1) + y1;
	}

	/**
	 * ���������Լ�ȷ������������ ������ϵͳ���е����� �ӳ��ڵ�ķֱ���
	 */
	public static double edfbl = 0.0001;
	/**
	 * ��С�ֱ满��
	 */
	public static double angmin = 0.0000001;
	/**
	 * Բ�ܽ� ����
	 */
	public static final double CIRCLE_ANG = 2.0 * Math.PI;

	/**
	 * ����Ļ����õ���������
	 * 
	 * @param px
	 * @param py
	 * @return һ������Ϊ2��һά����
	 */
	public static double[] getWorldCoord(double px, double py, double[] trans) {
		double d[] = new double[] { px / trans[0] - trans[1],
				py / trans[0] - trans[2] };
		return getRotatePoint(d[0], d[1], trans[4], trans[5], -trans[3]);
	}

	/**
	 * ����������õ���Ļ����
	 * 
	 * @param wx
	 *            ��������x
	 * @param wy
	 *            ��������y
	 * @return һ������Ϊ2��һά����
	 */
	public static double[] getScreenCoord(double wx, double wy, double[] trans) {
		double d2[] = getRotatePoint(wx, wy, trans[4], trans[5], trans[3]);
		return new double[] { (d2[0] + trans[1]) * trans[0],
				(d2[1] + trans[2]) * trans[0] };
	}

	/**
	 * ������ͷ
	 * 
	 * @param g2
	 *            Graphics2Dʵ��
	 * @param x1
	 *            (x1,y1)�Ǽ�ͷ�ߵ����
	 * @param y1
	 * @param x2
	 *            (x1,y1)�Ǽ�ͷ�ߵ�ʸ��
	 * @param y2
	 */
	public static void drawArrow(Graphics2D g2, List<Point2D> cornerPoints) {
		// g2.draw(new Line2D.Double(x1, y1, x2, y2));

		// ��������սǵ�Ϊ��ͷ�����ݵ�
		double x1 = cornerPoints.get(cornerPoints.size() - 2).getX();
		double x2 = cornerPoints.get(cornerPoints.size() - 1).getX();
		double y1 = cornerPoints.get(cornerPoints.size() - 2).getY();
		double y2 = cornerPoints.get(cornerPoints.size() - 1).getY();
		double ret[] = WfMath.rotateVec(x1 - x2, y1 - y2, Math.PI / 10.0,
				GlobalConstants.ARROW_LENGTH);
		g2.draw(new Line2D.Double(x2, y2, x2 + ret[0], y2 + ret[1]));
		ret = WfMath.rotateVec(x1 - x2, y1 - y2, -Math.PI / 10.0,
				GlobalConstants.ARROW_LENGTH);
		g2.draw(new Line2D.Double(x2, y2, x2 + ret[0], y2 + ret[1]));
	}

	/**
	 * ������ͷ
	 * 
	 * @param g2
	 *            Graphics2Dʵ��
	 * @param x1
	 *            (x1,y1)�Ǽ�ͷ�ߵ����
	 * @param y1
	 * @param x2
	 *            (x1,y1)�Ǽ�ͷ�ߵ�ʸ��
	 * @param y2
	 */
	public static void drawArrow(Graphics2D g2, double x1, double y1,
			double x2, double y2) {
		g2.draw(new Line2D.Double(x1, y1, x2, y2));
		double ret[] = WfMath.rotateVec(x1 - x2, y1 - y2, Math.PI / 8.0,
				GlobalConstants.ARROW_LENGTH);
		g2.draw(new Line2D.Double(x2, y2, x2 + ret[0], y2 + ret[1]));
		ret = WfMath.rotateVec(x1 - x2, y1 - y2, -Math.PI / 8.0,
				GlobalConstants.ARROW_LENGTH);
		g2.draw(new Line2D.Double(x2, y2, x2 + ret[0], y2 + ret[1]));
	}
}