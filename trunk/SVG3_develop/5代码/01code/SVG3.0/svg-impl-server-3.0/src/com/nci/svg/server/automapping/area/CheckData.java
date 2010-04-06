package com.nci.svg.server.automapping.area;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * 标题：CheckData.java
 * </p>
 * <p>
 * 描述：数据有效性检查，并对数据进行预处理
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-03
 * @version 1.0
 */
public class CheckData {

	/**
	 * 构造函数
	 * 
	 * @param area:Area:台区对象
	 */
	public CheckData() {

	}

	/**
	 * 检查台区数据合法性
	 * 
	 * @return AutoMapResultBean
	 */
	public AutoMapResultBean checkAreaData(Area area) {
		AutoMapResultBean resultBean = new AutoMapResultBean();

		LinkedHashMap lineMap = area.getLinesList();
		Iterator it = lineMap.values().iterator();
		while (it.hasNext()) {
			resultBean = checkLine(area, (AreaLine) it.next());
			if (!resultBean.isFlag()) {
				break;
			}
		}
		return resultBean;
	}

	/**
	 * 检查线路数据合法性
	 * 
	 * @return 检查结果
	 */
	private AutoMapResultBean checkLine(Area area, AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		// *******************
		// 检查线路起始角是否合法
		// *******************
		double angle = parseDouble((String) line.getProperty("startAngle"),
				Double.NaN);
		if (Double.isNaN(angle)) {
			resultBean.setFlag(false);
			reBuffer.append("编号为'").append(line.getProperty("code")).append(
					"'的线路不存在起始角！");
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;
		// *****************
		// 检查线路总长是否合法
		// *****************
		double distance = parseDouble((String) line.getProperty("distance"),
				-1.0);
		if (distance <= 0.0) {
			reBuffer.append("编号为'").append(line.getProperty("code")).append(
					"'的线路的线路总长不合法！");
			resultBean.setFlag(false);
		}
		if (!resultBean.isFlag())
			return resultBean;
		// *********
		// 接线杆检查
		// *********
		resultBean = checkPlugPoleOfLine(line);
		if (!resultBean.isFlag())
			return resultBean;
		// **************
		// 支线的父线路检查
		// **************
		resultBean = checkParentLine(area, line);
		if (!resultBean.isFlag())
			return resultBean;
		// **************
		// 线路所属杆塔检查
		// **************
		resultBean = checkLinePoles(line);
		if (!resultBean.isFlag())
			return resultBean;
		else {
			Object poleMap = resultBean.getMsg();
			if (poleMap instanceof LinkedHashMap)
				area.getElectricPoleList().putAll((LinkedHashMap) poleMap);
		}

		// 设置线路起始角
		line.setProperty("startAngle", new Double(angle));
		// 设置线路总长
		line.setProperty("distance", new Double(distance));

		// 更新area对象中的线路对象
		area.getLinesList().put(line.getProperty("code"), line);
		return resultBean;
	}

	/**
	 * 检查线路下所有杆塔合法性
	 * 
	 * @param line:Line:线路对象
	 * @return
	 */
	private AutoMapResultBean checkLinePoles(AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		LinkedHashMap poleMap = new LinkedHashMap();
		ArrayList poleList = line.getPoles();
		ArrayList tempList = new ArrayList();

		for (int i = 0, size = poleList.size(); i < size; i++) {
			AreaPole pole = (AreaPole) poleList.get(i);
			resultBean = checkPole(pole);
			if (resultBean.isFlag()) {
				// 杆塔数据有效
				String poleCode = (String) pole.getProperty("code");
				poleMap.put(poleCode, pole);
				tempList.add(poleCode);
			} else {
				// 杆塔数据无效
				return resultBean;
			}
		}

		tempList.trimToSize();
		line.setPoles(tempList);
		resultBean.setMsg(poleMap);

		return resultBean;
	}

	/**
	 * 检查指定杆塔对象合法性
	 * 
	 * @param pole:ElectricPole:杆塔对象
	 * @return
	 */
	private AutoMapResultBean checkPole(AreaPole pole) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		// 检查塔高合法性
		double height = parseDouble((String) pole.getProperty("height"),
				Double.NaN);
		if (Double.isNaN(height)) {
			reBuffer.append("编号为'").append(pole.getProperty("code")).append(
					"'的杆塔height数据有误！");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;
		// 检查距离合法性
		double distance = parseDouble((String) pole.getProperty("distance"),
				Double.NaN);
		if (Double.isNaN(distance)) {
			reBuffer.append("编号为'").append(pole.getProperty("code")).append(
					"'的杆塔distance数据有误！");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;
		// 获取杆塔角度值
		double angle = parseDouble((String) pole.getProperty("angle"), 0.0);
		if (Double.isNaN(angle)) {
			reBuffer.append("编号为'").append(pole.getProperty("code")).append(
					"'的杆塔angle数据有误！");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;

		// 设置塔高
		pole.setProperty("height", new Double(height));
		// 设置距离
		pole.setProperty("distance", new Double(distance));
		// 设置杆塔转角值
		pole.setProperty("angle", new Double(angle));
		// 设置接入转角值
		String tempAngle = (String) pole.getProperty("contactAngle");
		if (tempAngle != null) {
			double contactAngle = parseDouble((String) pole
					.getProperty("contactAngle"), Double.NaN);
			if (Double.isNaN(contactAngle)) {
				reBuffer.append("编号为'").append(pole.getProperty("code"))
						.append("'的杆塔contactAngle角度值有误！");
				resultBean.setFlag(false);
				resultBean.setErrMsg(reBuffer.toString());
			}
			if (!resultBean.isFlag())
				return resultBean;
			pole.setProperty("contactAngle", new Double(contactAngle));
		}
		// 设置接入杆塔标志
		String contactTag = (String) pole.getProperty("contactTag");
		if (contactTag != null && contactTag.equalsIgnoreCase("true"))
			pole.setPlugin(true);

		return resultBean;
	}

	/**
	 * 检查线路的父线路是否合法
	 * 
	 * @param area:Area:台区对象
	 * @param line:Line:线路对象
	 * @return
	 */
	private AutoMapResultBean checkParentLine(Area area, AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		String pline = (String) line.getProperty("pline");
		if (pline != null && pline.length() > 0) {
			// 父线路信息存在
			Object obj = area.getLinesList().get(pline);
			if (obj instanceof AreaLine) {
				String startPole = (String) line.getProperty("startPole");
				if (!(startPole != null && startPole.length() > 0)) {
					// 获取不到父线路接出杆信息
					reBuffer.append("编号为'").append(line.getProperty("code"))
							.append("'的支线缺少父线路的接出杆信息！");
					resultBean.setFlag(false);
					resultBean.setErrMsg(reBuffer.toString());
					return resultBean;
				}

				// 获取到父线路
				AreaLine parentLine = (AreaLine) obj;
				ArrayList parentPoleList = parentLine.getPoles();
				boolean parentPoleExist = false;
				for (int i = 0, size = parentPoleList.size(); i < size; i++) {
					Object poleObj = parentPoleList.get(i);
					AreaPole pole = null;
					if (poleObj instanceof AreaPole)
						pole = (AreaPole) poleObj;
					else
						pole = (AreaPole) area.getElectricPoleList().get(
								poleObj);
					if (startPole.equals((String) pole.getProperty("code"))) {
						parentPoleExist = true;
						break;
					}
				}
				if (!parentPoleExist) {
					// 父线路的接出杆在父线路上不存在
					reBuffer.append("编号为'").append(line.getProperty("code"))
							.append("'支线的父线路接出杆在父线路上不存在！");
					resultBean.setFlag(false);
					resultBean.setErrMsg(reBuffer.toString());
				}
			} else {
				// 未获取到父线路
				reBuffer.append("编号为'").append(line.getProperty("code"))
						.append("'的支线未找到其父线路信息！");
				resultBean.setFlag(false);
				resultBean.setErrMsg(reBuffer.toString());
			}
			line.setBranch(true);
		}
		return resultBean;
	}

	/**
	 * 检查指定线路的接入杆是否合法
	 * 
	 * @param line:Line:线路对象
	 * @return
	 */
	private AutoMapResultBean checkPlugPoleOfLine(AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		String middleTag = (String) line.getProperty("middleTag");
		if (middleTag != null && middleTag.equalsIgnoreCase("true")) {
			// 将该线路注册为中间接入线路
			line.setMiddle(true);

			boolean hasPlugPole = false;
			boolean hasContactAngle = false; // 主线上的接入角标志
			ArrayList polelist = line.getPoles();
			for (int i = 0, size = polelist.size(); i < size; i++) {
				AreaPole pole = (AreaPole) polelist.get(i);
				String contactTag = (String) pole.getProperty("contactTag");
				if (contactTag != null && contactTag.equalsIgnoreCase("true")) {
					String pline = (String) line.getProperty("pline");
					hasPlugPole = true;
					if (pline != null && pline.length() > 0) {
						// 该线路为支线
						hasContactAngle = true;
					} else {
						// 该线为主线
						String contactAngle = (String) pole
								.getProperty("contactAngle"); // 获取接入角
						if (contactAngle != null && contactAngle.length() > 0) {
							hasContactAngle = true;
						}
					}
					break;
				}
			}
			if (!hasPlugPole) {
				resultBean.setFlag(false);
				reBuffer.append("编号为'").append(line.getProperty("code"))
						.append("'的线路被标识为非起始杆接入，但缺少接入杆信息！");
				resultBean.setErrMsg(reBuffer.toString());
			}
			if (!hasContactAngle) {
				resultBean.setFlag(false);
				reBuffer.append("编号为'").append(line.getProperty("code"))
						.append("'的主线路被标识为非起始杆接入，但缺少有效接入角信息！");
				resultBean.setErrMsg(reBuffer.toString());
			}
		}
		return resultBean;
	}

	/**
	 * 将字符串数据转换成浮点型数值
	 * 
	 * @param data:String:字符串数值
	 * @return
	 */
	private double parseDouble(String data, double defaultValue) {
		if (data != null && data.length() > 0) {
			try {
				return (Double.parseDouble(data));
			} catch (NumberFormatException e) {
				return Double.NaN;
			}
		} else
			return defaultValue;
	}

}
