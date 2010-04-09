package com.nci.domino;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * @author Qil.Wong ȫ�ֱ�������
 */
public class GlobalConstants {

	/**
	 * ���̶������汾
	 */
	public static String DEFINE_VERSION = "4.1";
	/**
	 * Ǩ���߿��
	 */
	public static float LINE_WIDTH = 1.6f;
	/**
	 * ��ʵ��
	 */
	public static BasicStroke SOLID_LINE_STROKE = new BasicStroke(LINE_WIDTH);// Ǩ���߿�ȵĶ�����ʽ
	public static BasicStroke SMALL_LINE_STROKE = new BasicStroke(
			LINE_WIDTH / 1.6f);// Ǩ���߿�ȵĶ�����ʽ
	
	/**
	 * �ֵ㻮��
	 */
	public static BasicStroke DASH_LINE_STROKE = new BasicStroke(LINE_WIDTH,
			BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE, 8.0f, new float[] {
					4.0f, 4.0f }, 0.0f);
	/**
	 * ϸ�㻮��
	 */
	public static BasicStroke SDASH_LINE_STROKE = new BasicStroke(
			LINE_WIDTH / 1.5f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE,
			4.0f, new float[] { 2.0f, 2.0f }, 0.0f);
	/**
	 * ����ֶ���
	 */
	public static Font ACTTEXTFONT = new Font("Dialog", Font.PLAIN, 13);

	/**
	 * Ǩ�������ֶ���
	 */
	public static Font TRANTEXTFONT = new Font("Dialog", Font.BOLD, 12);

	public final static int LABEL_ALIGH_POSITION = SwingConstants.LEFT;

	/**
	 * ���Ŵ�С
	 */
	public static int SYMBOL_SIZE = 39;
	/**
	 * ���Ű뾶
	 */
	public static int SYMBOL_RAD = (int)(SYMBOL_SIZE / Math.sqrt(2.0));
	// ------------------------
	/**
	 * ʱ�������� ����ǰ��̨ʱ��Ĳ�ֵ
	 */
	public static long correctTime = 0;
	/**
	 * ѡ��Ǩ����ʱ��ķֱ���
	 */
	public static float fbl = 2.0f;
	/**
	 * ѡ��Ǩ����ʱ�ķֱ��� ����Ļ������� ���˫�������Ǩ��������λ���޿��ֱ�ߵĴ�ֱ����С�ڸ�ֵ��ʱ�����ѡ�и���
	 * ����ֵ�����������������С���߿��һ�� Ӧ��ȡ�߿��һ����뺯��isDotOnLine��epsl����
	 */
	public static Border borderTop = BorderFactory.createLineBorder(new Color(
			200, 200, 200), 1); // �Ϲ������ı߿�
	public static double scaleValueOrigin = 1.0;// Ĭ����������
	public static double EX_value = 10;// Ǩ������С��� ʵ�ʼ��Ϊ��ֵ��������

	public static double ARROW_LENGTH = 7;// ��ͷ����

	public static double MAX_SCALE = 20.0;// ������������

	public static String tempFile = System.getProperty("user.home")
			+ "/.nci/wrokflow/default.woml";
}