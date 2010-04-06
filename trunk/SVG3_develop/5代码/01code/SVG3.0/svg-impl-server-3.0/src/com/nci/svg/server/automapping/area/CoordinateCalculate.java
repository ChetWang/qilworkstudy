package com.nci.svg.server.automapping.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * 标题：CoordinateCalculate.java
 * </p>
 * <p>
 * 描述：坐标推算
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-06
 * @version 1.0
 */
public class CoordinateCalculate {
	/**
	 * 距离默认参数
	 */
	private static int DEFAULT_DISTANCE = 50;
	/**
	 * 线路绘制标志
	 */
	private HashMap lineCalculated;
	/**
	 * 主线名数组
	 */
	private ArrayList mainLinesCode;
	/**
	 * 支线名数组
	 */
	private ArrayList branchLinesCode;
	/**
	 * 台区对象
	 */
	private Area area;

	/**
	 * 构造函数
	 * 
	 * @param area:Area:台区对象
	 */
	public CoordinateCalculate(Area area) {
		lineCalculated = new LinkedHashMap();
		mainLinesCode = new ArrayList();
		branchLinesCode = new ArrayList();
		this.area = area;
	}

	/**
	 * 计算数据
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
	 * 计算线路各杆塔坐标
	 * 
	 * @param startPoint:double[2]:起始点坐标
	 * @param line:Line:线路对象
	 * @return
	 */
	private AutoMapResultBean calculateLine(double[] startPoint, AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();
		ArrayList polelist = line.getPoles();
		LinkedHashMap poleMap = area.getElectricPoleList();

		// 获取线路起始角
		double basicAngle = ((Double) line.getProperty("startAngle"))
				.doubleValue();
		// 获取线路基准点坐标
		double[] basicPoint = (double[]) startPoint.clone();
		if (line.isMiddle()) {
			// **************
			// 线路是中间杆接入
			// **************
			// 处理杆塔数组
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
				// 杆塔数据中缺少接入杆数据
				reBuffer.append("计算编号为'").append(line.getProperty("code"))
						.append("'的线路坐标时缺少接入杆数据");
				resultBean.setFlag(false);
				resultBean.setErrMsg(reBuffer.toString());
				return resultBean;
			}
			if (line.isBranch()) {
				// *********
				// 线路是支线
				// *********

				// ////////////////
				// 主线十字接杆塔坐标
				// ////////////////
				String crossPoleCode = (String) line.getProperty("startPole");
				AreaPole crossPole = (AreaPole) poleMap
						.get(crossPoleCode);
				// 增加一个逻辑杆塔
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
				// 十字接杆塔坐标
				double[] crossPoint = crossLogicPole.getCoordinate();
				// /////////////////////
				// 逆推接入杆前面的杆塔坐标
				// /////////////////////
				double basicDis = DEFAULT_DISTANCE;
				calculateBack(poles, poleMap, crossPoint, basicAngle, basicDis,
						pluginNum);
				// ////////////////////
				// 推算接入杆后面的杆塔坐标
				// ////////////////////
				calculatePoleList(polelist, poleMap, crossPoint,
						360 - basicAngle, pluginNum + 2);
			} else {
				// *********
				// 线路是主线
				// *********

				// /////////////
				// 计算接入杆坐标
				// /////////////
				AreaPole pole = (AreaPole) poleMap
						.get(poles[pluginNum]);
				double plugAngle = ((Double) pole.getProperty("contactAngle"))
						.doubleValue();// 接入杆接入角
				double[] plugPoint = calNextPoint(basicPoint, DEFAULT_DISTANCE,
						plugAngle);
				pole.setCoordinate(plugPoint);
				// /////////////////////
				// 逆推接入杆前面的杆塔坐标
				// /////////////////////
				double basicDis = ((Double) pole.getProperty("distance"))
						.doubleValue();// 获取接入杆档距
				calculateBack(poles, poleMap, plugPoint, basicAngle, basicDis,
						pluginNum - 1);
				// ////////////////////
				// 推算接入杆后面的杆塔坐标
				// ////////////////////
				double plugTurnAngle = ((Double) pole.getProperty("angle"))
						.doubleValue();// 接入杆转角
				calculatePoleList(polelist, poleMap, plugPoint, basicAngle
						- 180 + plugTurnAngle, pluginNum + 1);
			}
		} else {
			// **************
			// 线路是起始杆接入
			// **************
			calculatePoleList(polelist, poleMap, basicPoint, basicAngle, 0);
		}
		// ***********
		// 推算支线坐标
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
	 * 逆序推算指定杆塔队列中杆塔坐标
	 * 
	 * @param polelist:String[]:杆塔编号组
	 * @param poleMap:LinkedHashMap:台区杆塔对象组
	 * @param basicPoint:double[2]:起始坐标
	 * @param basicAngle:double:起始角度
	 * @param basicDistance:double:起始距离
	 * @param index:int:从指定杆塔开始推算
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
	 * 顺序推算指定杆塔队列中杆塔坐标
	 * 
	 * @param polelist:ArrayList:杆塔编号组
	 * @param poleMap:LinkedHashMap:台区杆塔对象组
	 * @param basicPoint:double[2]:起始坐标
	 * @param basicAngle:double:起始角度
	 * @param index:int:从指定杆塔开始推算
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
	 * 获取指定线路下所有的支线
	 * 
	 * @param line:Line:支线
	 * @return [0]支线编号，[1]连接支线的杆塔编号
	 */
	private ArrayList getBranchLines(AreaLine line) {
		ArrayList subLines = new ArrayList();
		// 获取线路编号
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
	 * 计算下个点坐标位置
	 * 
	 * @param start:double[2]:基准点坐标
	 * @param s:double:长度
	 * @param angle:double:角度
	 * @return
	 */
	private double[] calNextPoint(double[] start, double s, double angle) {
		double[] point = new double[2];
		// 转换成弧度
		double rad = angle / 180.0 * Math.PI;
		point[0] = start[0] + s * Math.cos(rad);
		point[1] = start[1] - s * Math.sin(rad);
		return point;
	}

	/**
	 * 初始化函数
	 * 
	 * @param area:Area:台区对象
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
	 * 检查台区对象中是否包含主线路对象
	 * 
	 * @return
	 */
	private AutoMapResultBean checkMainLine() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();
		if (mainLinesCode.size() <= 0) {
			// 台区中没有主线路
			reBuffer.append("编号为'").append(area.getProperty("code")).append(
					"'没有主线数据！");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		return resultBean;
	}
}
