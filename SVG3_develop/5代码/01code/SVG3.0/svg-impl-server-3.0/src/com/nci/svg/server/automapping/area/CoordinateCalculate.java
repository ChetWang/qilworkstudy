package com.nci.svg.server.automapping.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * ���⣺CoordinateCalculate.java
 * </p>
 * <p>
 * ��������������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-06
 * @version 1.0
 */
public class CoordinateCalculate {
	/**
	 * ����Ĭ�ϲ���
	 */
	private static int DEFAULT_DISTANCE = 50;
	/**
	 * ��·���Ʊ�־
	 */
	private HashMap lineCalculated;
	/**
	 * ����������
	 */
	private ArrayList mainLinesCode;
	/**
	 * ֧��������
	 */
	private ArrayList branchLinesCode;
	/**
	 * ̨������
	 */
	private Area area;

	/**
	 * ���캯��
	 * 
	 * @param area:Area:̨������
	 */
	public CoordinateCalculate(Area area) {
		lineCalculated = new LinkedHashMap();
		mainLinesCode = new ArrayList();
		branchLinesCode = new ArrayList();
		this.area = area;
	}

	/**
	 * ��������
	 */
	public AutoMapResultBean calculate() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		init();

		resultBean = checkMainLine();
		if (!resultBean.isFlag())
			return resultBean;

		for (int i = 0, size = mainLinesCode.size(); i < size; i++) {
			double[] startPoint = new double[] { 0.0, 0.0 };
			AreaLine line = (AreaLine) area.getLinesList().get(mainLinesCode.get(i));
			resultBean = calculateLine(startPoint, line);
		}
		
		if(resultBean.isFlag())
			resultBean.setMsg(area);
		return resultBean;
	}

	/**
	 * ������·����������
	 * 
	 * @param startPoint:double[2]:��ʼ������
	 * @param line:Line:��·����
	 * @return
	 */
	private AutoMapResultBean calculateLine(double[] startPoint, AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();
		ArrayList polelist = line.getPoles();
		LinkedHashMap poleMap = area.getElectricPoleList();

		// ��ȡ��·��ʼ��
		double basicAngle = ((Double) line.getProperty("startAngle"))
				.doubleValue();
		// ��ȡ��·��׼������
		double[] basicPoint = (double[]) startPoint.clone();
		if (line.isMiddle()) {
			// **************
			// ��·���м�˽���
			// **************
			// �����������
			int size = polelist.size();
			int pluginNum = -1;
			String[] poles = new String[size];
			for (int i = 0; i < size; i++) {
				poles[i] = (String) polelist.get(i);
				AreaPole pole = (AreaPole) poleMap.get(poles[i]);
				if (pole.isPlugin())
					pluginNum = i;
			}
			if (pluginNum < 0) {
				// ����������ȱ�ٽ��������
				reBuffer.append("������Ϊ'").append(line.getProperty("code"))
						.append("'����·����ʱȱ�ٽ��������");
				resultBean.setFlag(false);
				resultBean.setErrMsg(reBuffer.toString());
				return resultBean;
			}
			if (line.isBranch()) {
				// *********
				// ��·��֧��
				// *********

				// ////////////////
				// ����ʮ�ֽӸ�������
				// ////////////////
				String crossPoleCode = (String) line.getProperty("startPole");
				AreaPole crossPole = (AreaPole) poleMap
						.get(crossPoleCode);
				// ����һ���߼�����
				String lineCode = (String) line.getProperty("code");
				String crossLogicPoleCode = lineCode + "_" + crossPoleCode;
				AreaPole crossLogicPole = new AreaPole(crossPole);
				crossLogicPole.setProperty("code", crossLogicPoleCode);
				crossLogicPole.setLogic(true);
				crossLogicPole.setProperty("distance", new Double(
						DEFAULT_DISTANCE));
				crossLogicPole.setProperty("angle",
						new Double(360 - basicAngle));
				poleMap.put(crossLogicPoleCode, crossLogicPole);
				line.getPoles().add(pluginNum + 1, crossLogicPoleCode);
				// ʮ�ֽӸ�������
				double[] crossPoint = crossLogicPole.getCoordinate();
				// /////////////////////
				// ���ƽ����ǰ��ĸ�������
				// /////////////////////
				double basicDis = DEFAULT_DISTANCE;
				calculateBack(poles, poleMap, crossPoint, basicAngle, basicDis,
						pluginNum);
				// ////////////////////
				// �������˺���ĸ�������
				// ////////////////////
				calculatePoleList(polelist, poleMap, crossPoint,
						360 - basicAngle, pluginNum + 2);
			} else {
				// *********
				// ��·������
				// *********

				// /////////////
				// ������������
				// /////////////
				AreaPole pole = (AreaPole) poleMap
						.get(poles[pluginNum]);
				double plugAngle = ((Double) pole.getProperty("contactAngle"))
						.doubleValue();// ����˽����
				double[] plugPoint = calNextPoint(basicPoint, DEFAULT_DISTANCE,
						plugAngle);
				pole.setCoordinate(plugPoint);
				// /////////////////////
				// ���ƽ����ǰ��ĸ�������
				// /////////////////////
				double basicDis = ((Double) pole.getProperty("distance"))
						.doubleValue();// ��ȡ����˵���
				calculateBack(poles, poleMap, plugPoint, basicAngle, basicDis,
						pluginNum - 1);
				// ////////////////////
				// �������˺���ĸ�������
				// ////////////////////
				double plugTurnAngle = ((Double) pole.getProperty("angle"))
						.doubleValue();// �����ת��
				calculatePoleList(polelist, poleMap, plugPoint, basicAngle
						- 180 + plugTurnAngle, pluginNum + 1);
			}
		} else {
			// **************
			// ��·����ʼ�˽���
			// **************
			calculatePoleList(polelist, poleMap, basicPoint, basicAngle, 0);
		}
		// ***********
		// ����֧������
		// ***********
		ArrayList subLines = getBranchLines(line);
		for (int i = 0, size = subLines.size(); i < size; i++) {
			String[] subMsg = (String[]) subLines.get(i);
			AreaLine subLine = (AreaLine) area.getLinesList().get(subMsg[0]);
			AreaPole outPole = (AreaPole) area.getElectricPoleList()
					.get(subMsg[1]);
			double[] point = outPole.getCoordinate();
			resultBean = calculateLine(point, subLine);
			if (!resultBean.isFlag())
				return resultBean;
		}

		return resultBean;
	}

	/**
	 * ��������ָ�����������и�������
	 * 
	 * @param polelist:String[]:���������
	 * @param poleMap:LinkedHashMap:̨������������
	 * @param basicPoint:double[2]:��ʼ����
	 * @param basicAngle:double:��ʼ�Ƕ�
	 * @param basicDistance:double:��ʼ����
	 * @param index:int:��ָ��������ʼ����
	 */
	private void calculateBack(String[] poles, LinkedHashMap poleMap,
			double[] basicPoint, double basicAngle, double basicDistance,
			int index) {
		double[] tempPoint = (double[]) basicPoint.clone();
		double nextAngle = basicAngle;
		double tempDis = basicDistance;
		for (int i = index; i >= 0; i--) {
			AreaPole pole = (AreaPole) poleMap.get(poles[i]);
			double turnAngle = ((Double) pole.getProperty("angle"))
					.doubleValue();
			tempPoint = calNextPoint(tempPoint, tempDis, nextAngle);
			nextAngle -= turnAngle;
			tempDis = ((Double) pole.getProperty("distance")).doubleValue();
			pole.setCoordinate(tempPoint);
		}
	}

	/**
	 * ˳������ָ�����������и�������
	 * 
	 * @param polelist:ArrayList:���������
	 * @param poleMap:LinkedHashMap:̨������������
	 * @param basicPoint:double[2]:��ʼ����
	 * @param basicAngle:double:��ʼ�Ƕ�
	 * @param index:int:��ָ��������ʼ����
	 */
	private void calculatePoleList(ArrayList polelist, LinkedHashMap poleMap,
			double[] basicPoint, double basicAngle, int index) {
		double[] tempPoint = (double[]) basicPoint.clone();
		double nextAngle = basicAngle;
		for (int i = index, size = polelist.size(); i < size; i++) {
			AreaPole pole = (AreaPole) poleMap.get(polelist.get(i));
			// ElectricPole nextpole = (ElectricPole) poleMap.get(polelist
			// .get(i + 1));
			double s = ((Double) pole.getProperty("distance")).doubleValue();
			double turnAngle = ((Double) pole.getProperty("angle"))
					.doubleValue();
			tempPoint = calNextPoint(tempPoint, s, nextAngle);
			nextAngle += turnAngle;
			pole.setCoordinate(tempPoint);
		}

	}

	/**
	 * ��ȡָ����·�����е�֧��
	 * 
	 * @param line:Line:֧��
	 * @return [0]֧�߱�ţ�[1]����֧�ߵĸ������
	 */
	private ArrayList getBranchLines(AreaLine line) {
		ArrayList subLines = new ArrayList();
		// ��ȡ��·���
		String code = (String) line.getProperty("code");

		String[] subMsg;
		for (int i = 0, size = branchLinesCode.size(); i < size; i++) {
			subMsg = new String[2];
			subMsg[0] = (String) branchLinesCode.get(i);
			AreaLine subLine = (AreaLine) area.getLinesList().get(subMsg[0]);
			if (code.equals((String) subLine.getProperty("pline"))) {
				subMsg[1] = (String) subLine.getProperty("startPole");
				subLines.add(subMsg);
			}
		}
		subLines.trimToSize();
		return subLines;
	}

	/**
	 * �����¸�������λ��
	 * 
	 * @param start:double[2]:��׼������
	 * @param s:double:����
	 * @param angle:double:�Ƕ�
	 * @return
	 */
	private double[] calNextPoint(double[] start, double s, double angle) {
		double[] point = new double[2];
		// ת���ɻ���
		double rad = angle / 180.0 * Math.PI;
		point[0] = start[0] + s * Math.cos(rad);
		point[1] = start[1] - s * Math.sin(rad);
		return point;
	}

	/**
	 * ��ʼ������
	 * 
	 * @param area:Area:̨������
	 */
	private void init() {
		LinkedHashMap lines = area.getLinesList();
		Iterator it = lines.values().iterator();
		while (it.hasNext()) {
			AreaLine line = (AreaLine) it.next();
			String code = (String) line.getProperty("code");
			if (line.isBranch()) {
				branchLinesCode.add(code);
			} else {
				mainLinesCode.add(code);
			}
			lineCalculated.put(code, new Boolean(false));
		}

	}

	/**
	 * ���̨���������Ƿ��������·����
	 * 
	 * @return
	 */
	private AutoMapResultBean checkMainLine() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();
		if (mainLinesCode.size() <= 0) {
			// ̨����û������·
			reBuffer.append("���Ϊ'").append(area.getProperty("code")).append(
					"'û���������ݣ�");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		return resultBean;
	}
}
