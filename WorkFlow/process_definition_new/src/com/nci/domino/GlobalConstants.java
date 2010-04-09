package com.nci.domino;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * @author Qil.Wong 全局变量定义
 */
public class GlobalConstants {

	/**
	 * 流程定义器版本
	 */
	public static String DEFINE_VERSION = "4.1";
	/**
	 * 迁移线宽度
	 */
	public static float LINE_WIDTH = 1.6f;
	/**
	 * 粗实线
	 */
	public static BasicStroke SOLID_LINE_STROKE = new BasicStroke(LINE_WIDTH);// 迁移线宽度的对象化形式
	public static BasicStroke SMALL_LINE_STROKE = new BasicStroke(
			LINE_WIDTH / 1.6f);// 迁移线宽度的对象化形式
	
	/**
	 * 粗点划线
	 */
	public static BasicStroke DASH_LINE_STROKE = new BasicStroke(LINE_WIDTH,
			BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE, 8.0f, new float[] {
					4.0f, 4.0f }, 0.0f);
	/**
	 * 细点划线
	 */
	public static BasicStroke SDASH_LINE_STROKE = new BasicStroke(
			LINE_WIDTH / 1.5f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE,
			4.0f, new float[] { 2.0f, 2.0f }, 0.0f);
	/**
	 * 活动文字对象
	 */
	public static Font ACTTEXTFONT = new Font("Dialog", Font.PLAIN, 13);

	/**
	 * 迁移线文字对象
	 */
	public static Font TRANTEXTFONT = new Font("Dialog", Font.BOLD, 12);

	public final static int LABEL_ALIGH_POSITION = SwingConstants.LEFT;

	/**
	 * 符号大小
	 */
	public static int SYMBOL_SIZE = 39;
	/**
	 * 符号半径
	 */
	public static int SYMBOL_RAD = (int)(SYMBOL_SIZE / Math.sqrt(2.0));
	// ------------------------
	/**
	 * 时间修正量 这是前后台时间的差值
	 */
	public static long correctTime = 0;
	/**
	 * 选择迁移线时候的分辨率
	 */
	public static float fbl = 2.0f;
	/**
	 * 选择迁移线时的分辨率 以屏幕坐标计算 鼠标双击点距离迁移线中心位置无宽度直线的垂直距离小于该值的时候就算选中该线
	 * 但该值换算成世界坐标后如果小于线宽的一半 应该取线宽的一半代入函数isDotOnLine的epsl参数
	 */
	public static Border borderTop = BorderFactory.createLineBorder(new Color(
			200, 200, 200), 1); // 上工具栏的边框
	public static double scaleValueOrigin = 1.0;// 默认缩放因子
	public static double EX_value = 10;// 迁移线最小间距 实际间距为该值的整倍数

	public static double ARROW_LENGTH = 7;// 箭头长度

	public static double MAX_SCALE = 20.0;// 最大的缩放因子

	public static String tempFile = System.getProperty("user.home")
			+ "/.nci/wrokflow/default.woml";
}