package com.nci.domino.help;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import com.nci.domino.GlobalConstants;

/**
 * @author 陈新中 这里定义了一系列的数学算法
 */
public class WfMath {
	/**
	 * 得到矢量的长度
	 * 
	 * @param vx
	 *            (vx,vy)是矢量
	 * @param vy
	 * @return 矢量长度
	 */
	public static double getVecLen(double vx, double vy) {
		return Math.sqrt(vx * vx + vy * vy);
	}

	/**
	 * 得到矢量的长度
	 * 
	 * @param v
	 *            矢量
	 * @return 矢量长度
	 */
	public static double getVecLen(double[] v) {
		return getVecLen(v[0], v[1]);
	}

	/**
	 * 得到两点的距离
	 * 
	 * @param p1
	 *            p1 p2是两点
	 * @param p2
	 * @return 两点的距离
	 */
	public static double getPointPointDis(double[] p1, double[] p2) {
		return getVecLen(p2[0] - p1[0], p2[1] - p1[1]);
	}

	/**
	 * 旋转矢量
	 * 
	 * @param px
	 *            (px,py)是矢量
	 * @param py
	 * @param ang
	 *            转角弧度
	 * @param newLen
	 *            新长度
	 * @return 新矢量
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
	 * 判断点是否在直线上
	 * 
	 * @param xp
	 *            (xp,yp) 是直线外的一点
	 * @param yp
	 * @param x1
	 *            (x1,y1) 是直线的一个端点
	 * @param y1
	 * @param x2
	 *            (x2,y2) 是直线的一个端点
	 * @param y2
	 * @param epsl
	 *            分辨率
	 * @return 是则返回true
	 */
	public static boolean isDotOnLine(double xp, double yp, double x1,
			double y1, double x2, double y2, double epsl) {
		return getPointLineDis(xp, yp, x1, y1, x2, y2) < epsl
				&& (x1 - xp) * (x2 - xp) + (y1 - yp) * (y2 - yp) < 0;
	}

	/**
	 * 求点到直线的距离
	 * 
	 * @param xp
	 *            (xp,yp) 是直线外一点
	 * @param yp
	 * @param x1
	 *            (x1,y1)和(x2,y2)是直线上的两点
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
	 * 求矢量的投影
	 * 
	 * @param vx1
	 *            (vx1, vy1)是矢量v1
	 * @param vy1
	 * @param vx2
	 *            (vx2, vy2)是矢量v2
	 * @param vy2
	 * @return 矢量v1在v2上的投影
	 */
	public static double getVecPro(double vx1, double vy1, double vx2,
			double vy2) {
		return getDotProduct(vx1, vy1, vx2, vy2) / getVecLen(vx2, vy2);
	}

	/**
	 * 求两个矢量的点积
	 * 
	 * @param vx1
	 *            (vx1, vy1)是矢量v1
	 * @param vy1
	 * @param vx2
	 *            (vx2, vy2)是矢量v2
	 * @param vy2
	 * @return 矢量的点积
	 */
	public static double getDotProduct(double vx1, double vy1, double vx2,
			double vy2) {
		return vx1 * vx2 + vy1 * vy2;
	}

	/**
	 * 求直线的缩减直线 两头各少掉gene * Global.EX_value
	 * 
	 * @param x1
	 *            (x1,y1)起点中心
	 * @param y1
	 * @param x2
	 *            (x2,y2)末尾点中心
	 * @param y2
	 * @param gene
	 *            偏移因子
	 * @return 新的直线
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
	 * 确定直线的偏移线位置
	 * 
	 * @param x1
	 *            (x1,y1),(x2,y2)是一个有向直线
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param moveValue
	 *            moveValue 是偏移量 可正可负
	 * @return 新直线
	 */
	public static double[][] getLineExcursion(double x1, double y1, double x2,
			double y2, double moveValue) {
		double[] newCoor = rotateVec90(x2 - x1, y2 - y1, false, moveValue);
		return new double[][] { { x1 + newCoor[0], y1 + newCoor[1] },
				{ x2 + newCoor[0], y2 + newCoor[1] } };
	}

	/**
	 * 一个矢量旋转一个特定的角度 这里的角度单位使用弧度
	 * 
	 * @param px
	 *            px py 分别是矢量的两个分量
	 * @param py
	 * @param ang
	 *            是旋转角度 单位是弧度
	 * @return 新矢量
	 */
	public static double[] rotateVec(double px, double py, double ang) {
		double cosang = Math.cos(ang);
		double sinang = Math.sin(ang);
		return new double[] { px * cosang - py * sinang,
				px * sinang + py * cosang };
	}

	/**
	 * 求矢量的方向角
	 * 
	 * @param vx
	 *            (vx,vy)是矢量
	 * @param vy
	 * @return 矢量的方向角
	 */
	public static double getVecAng(double vx, double vy) {
		double l = getVecLen(vx, vy);
		if (l == 0)
			return 0;
		return Math.atan2(vy, vx);
	}

	/**
	 * 一个点绕着另一个点旋转后 得到一个新的位置
	 * 
	 * @param x
	 *            (x,y)待旋转的点
	 * @param y
	 * @param cx
	 *            (cx,cy)旋转中心
	 * @param cy
	 * @param ang
	 *            转角 以弧度度量
	 * @return 新的点
	 */
	public static double[] getRotatePoint(double x, double y, double cx,
			double cy, double ang) {
		double[] reCoord = rotateVec(x - cx, y - cy, ang);
		return new double[] { cx + reCoord[0], cy + reCoord[1] };
	}

	/**
	 * 得到x对y的余数 注意余数必须是正数 x = m * y + c 其中 y > 0 0<=c<y
	 * 
	 * @param x
	 *            被除数
	 * @param y
	 *            除数
	 * @return 返回正的余数
	 */
	public static double getResidue(double x, double y) {
		return x > 0 ? x % y : (x % y + y); // return x - Math.floor(x / y) * y;
	}

	/**
	 * (x1,y1)和(x2,y2)是线段的两个端点 (xp,yp)是直线12上一点 判断点(xp,yp)是在线段上还是在其延长线上
	 * 
	 * @param xp
	 *            (xp,yp)是直线12上一点
	 * @param yp
	 * @param x1
	 *            (x1,y1)和(x2,y2)是线段的两个端点
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 在线段上返回true
	 */
	public static boolean isInboardLine(double xp, double yp, double x1,
			double y1, double x2, double y2) {
		return getDotProduct(xp - x1, yp - y1, xp - x2, yp - y2) < 0.0;
	}

	/**
	 * 两个矢量是否平行
	 * 
	 * @param vx1
	 *            (vx1,vy1)是一个矢量
	 * @param vy1
	 * @param vx2
	 *            (vx2,vy2)是另一个矢量
	 * @param vy2
	 * @return 平行返回true
	 */
	public static boolean isParalled(double vx1, double vy1, double vx2,
			double vy2) {
		return Math.abs(getForkProduct(vx1, vy1, vx2, vy2)) < edfbl;
	}

	/**
	 * 求点到直线的垂线矢 从点指向垂足
	 * 
	 * @param xp
	 *            (xp,yp) 是直线外一点
	 * @param yp
	 * @param x1
	 *            (x1,y1)和(x2,y2)是直线上的两点
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 点到直线的垂线矢
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
	 * 求矢量的矢积
	 * 
	 * @param vx1
	 *            (vx1,vy1)是一个矢量
	 * @param vy1
	 * @param vx2
	 *            (vx2,vy2)是另一个矢量
	 * @param vy2
	 * @return 矢量的矢积
	 */
	public static double getForkProduct(double vx1, double vy1, double vx2,
			double vy2) {
		return vx1 * vy2 - vx2 * vy1;
	}

	/**
	 * 判断是否三点共线
	 * 
	 * @param xp
	 *            (x1,y1) (x2,y2) (xp,yp) 是三个点
	 * @param yp
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 共线返回true
	 */
	public static boolean isThreePointsOnLine(double xp, double yp, double x1,
			double y1, double x2, double y2) {
		return Math.abs((x1 - xp) * (y2 - yp) - (x2 - xp) * (y1 - yp)) < edfbl;
	}

	/**
	 * 双精度数转化为整数
	 * 
	 * @param x
	 *            双精度数
	 * @return 整数
	 */
	public static int doubleToInt(double x) {
		return (int) Math.round(x);
	}

	/**
	 * 得到矢量v1转到矢量v2的角 用弧度表示 范围限制在[-Math.PI,Math.PI]之间
	 * 
	 * @param v1
	 *            一个矢量
	 * @param v2
	 *            另一个矢量
	 * @return 矢量v1转到矢量v2的角
	 */
	public static double getVecToVecAng(double[] v1, double[] v2) {
		return getAngToAng(getVecAng(v2) - getVecAng(v1), -Math.PI);
	}

	/**
	 * 得到ang的等价角中大于minAng的最小角 用弧度表示
	 * 
	 * @param ang
	 * @param minAng
	 * @return 大于minAng的最小等价角
	 */
	public static double getAngToAng(double ang, double minAng) {
		return Math.ceil((minAng - ang) / CIRCLE_ANG) * CIRCLE_ANG + ang;
	}

	/**
	 * 返回矢量的位置角
	 * 
	 * @param v
	 *            矢量
	 * @return 矢量的位置角
	 */
	public static double getVecAng(double v[]) {
		return getVecAng(v[0], v[1]);
	}

	/**
	 * 旋转90度或者-90度
	 * 
	 * @param px
	 *            (px,py)是待旋转矢量
	 * @param py
	 * @param isPos
	 *            是90还是-90 true表示90
	 * @param newLen
	 *            新的矢量长度
	 * @return 新的矢量
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
	 * x是否在ab之间
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * @return x在ab之间返回true
	 */
	public static boolean isBetween(double x, double a, double b) {
		return (x - a) * (x - b) <= 0;
	}

	/**
	 * 点是否落在标准矩形中
	 * 
	 * @param xp
	 *            (xp,yp)是平面上的任一点
	 * @param yp
	 * @param l
	 *            (l,t)是左上角坐标
	 * @param t
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return 落在返回true
	 */
	public static boolean isDotInRect(double xp, double yp, double l, double t,
			double w, double h) {
		return isBetween(xp, l, l + w) && isBetween(yp, t, t + h);
	}

	/**
	 * 点是否落在凸四边形中
	 * 
	 * @param xp
	 *            (xp,yp)是平面上的任一点
	 * @param yp
	 * @param p1
	 *            p1 p2 p3 是凸四边形的四个顶点
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return 落在返回true
	 */
	public static boolean isDotInRect(double xp, double yp, double[] p1,
			double[] p2, double[] p3, double[] p4) {
		return isDotInTriangle(xp, yp, p1, p2, p3)
				|| isDotInTriangle(xp, yp, p1, p3, p4);
	}

	/**
	 * 点是否落在三角形中
	 * 
	 * @param xp
	 *            (xp,yp)是平面上的任一点
	 * @param yp
	 * @param p1
	 *            p1 p2 p3 是三角形的三个顶点
	 * @param p2
	 * @param p3
	 * @return 落在返回true
	 */
	public static boolean isDotInTriangle(double xp, double yp, double[] p1,
			double[] p2, double[] p3) {
		double ang = getVecAng(p2[0] - p1[0], p2[1] - p1[1]);
		double p1m[] = rotateVec(p1[0], p1[1], -ang);// 旋转-ang
		double p2m[] = rotateVec(p2[0], p2[1], -ang);// 旋转-ang
		double p3m[] = rotateVec(p3[0], p3[1], -ang);// 旋转-ang
		double pt[] = rotateVec(xp, yp, -ang);// 旋转-ang
		return isBetween(pt[1], p1m[1], p3m[1])
				&& isBetween(pt[0], calInsertValue(p1m[1], p1m[0], p3m[1],
						p3m[0], pt[1]), calInsertValue(p2m[1], p2m[0], p3m[1],
						p3m[0], pt[1]));
	}

	/**
	 * 求点x在区间[x1,x2]上的线形插值
	 * 
	 * @param x1
	 *            (x1,y1)是第一点
	 * @param y1
	 * @param x2
	 *            (x2,y2)是第二点
	 * @param y2
	 * @param x
	 *            待求插值函数的点
	 * @return 插值函数值
	 */
	public static double calInsertValue(double x1, double y1, double x2,
			double y2, double x) {
		return (y2 - y1) / (x2 - x1) * (x - x1) + y1;
	}

	/**
	 * 以下是我自己确定的两个参数 并非是系统固有的限制 视场内点的分辨率
	 */
	public static double edfbl = 0.0001;
	/**
	 * 最小分辨弧度
	 */
	public static double angmin = 0.0000001;
	/**
	 * 圆周角 弧度
	 */
	public static final double CIRCLE_ANG = 2.0 * Math.PI;

	/**
	 * 从屏幕坐标得到世界坐标
	 * 
	 * @param px
	 * @param py
	 * @return 一个长度为2的一维数组
	 */
	public static double[] getWorldCoord(double px, double py, double[] trans) {
		double d[] = new double[] { px / trans[0] - trans[1],
				py / trans[0] - trans[2] };
		return getRotatePoint(d[0], d[1], trans[4], trans[5], -trans[3]);
	}

	/**
	 * 从世界坐标得到屏幕坐标
	 * 
	 * @param wx
	 *            世界坐标x
	 * @param wy
	 *            世界坐标y
	 * @return 一个长度为2的一维数组
	 */
	public static double[] getScreenCoord(double wx, double wy, double[] trans) {
		double d2[] = getRotatePoint(wx, wy, trans[4], trans[5], trans[3]);
		return new double[] { (d2[0] + trans[1]) * trans[0],
				(d2[1] + trans[2]) * trans[0] };
	}

	/**
	 * 画出箭头
	 * 
	 * @param g2
	 *            Graphics2D实例
	 * @param x1
	 *            (x1,y1)是箭头线的起点
	 * @param y1
	 * @param x2
	 *            (x1,y1)是箭头线的矢端
	 * @param y2
	 */
	public static void drawArrow(Graphics2D g2, List<Point2D> cornerPoints) {
		// g2.draw(new Line2D.Double(x1, y1, x2, y2));

		// 最后两个拐角点为箭头的数据点
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
	 * 画出箭头
	 * 
	 * @param g2
	 *            Graphics2D实例
	 * @param x1
	 *            (x1,y1)是箭头线的起点
	 * @param y1
	 * @param x2
	 *            (x1,y1)是箭头线的矢端
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