package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * 标题：SystemCheckData.java
 * </p>
 * <p>
 * 描述： 系统图自动成图数据检校类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-7-16
 * @version 1.0
 */
public class SystemCheckData {
	/**
	 * 厂站编号队列
	 */
	private ArrayList stationIds;
	/**
	 * 厂站集
	 */
	private HashMap stationMap;
	/**
	 * 线路集
	 */
	private ArrayList lineList;

	/**
	 * @功能 构造函数
	 * @param stationIds:厂站编号队列
	 * @param stationMap:HashMap:厂站集
	 * @param linelist:ArrayList:线路集
	 */
	public SystemCheckData(ArrayList stationIds, HashMap stationMap,
			ArrayList linelist) {
		this.stationIds = stationIds;
		this.stationMap = stationMap;
		this.lineList = linelist;
	}

	public AutoMapResultBean checkData() {
		AutoMapResultBean result;
		// ************
		// 初始化厂站数据
		// ************
		result = initStationData();
		if (!result.isFlag())
			return result;

		// **********
		// 检校线路数据
		// **********
		result = initLineData();

		return result;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 线路检校
	 * @return
	 */
	private AutoMapResultBean initLineData() {
		AutoMapResultBean result = new AutoMapResultBean();
		for (int i = lineList.size() - 1; i >= 0; i--) {
			try {
				SystemLine line = (SystemLine) lineList.get(i);
				String startId = (String) line.getProperty("StartSubId");
				String endId = (String) line.getProperty("EndSubId");
				Object startSub = stationMap.get(startId);
				Object endSub = stationMap.get(endId);
				if (startSub == null || endSub == null) {
					lineList.remove(i);
				}
			} catch (Exception ex) {
				result.setFlag(false);
				result.setErrMsg("线路检校错误！");
			}
		}
		return result;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 初始化厂站数据
	 * @return
	 */
	private AutoMapResultBean initStationData() {
		AutoMapResultBean result = new AutoMapResultBean();
		stationIds.trimToSize();
		for (int i = 0, size = stationIds.size(); i < size; i++) {
			String id = (String) stationIds.get(i);
			Object obj = stationMap.get(id);
			if (obj instanceof SystemSubstation) {
				SystemSubstation station = (SystemSubstation) obj;
				// 初始化坐标
				result = initStationCoordinate(station);
				if (!result.isFlag())
					return result;
			} else {
				result.setFlag(false);
				result.setErrMsg("厂站集中未找到指定对象");
				return result;
			}
		}
		return result;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 初始化厂站的坐标
	 * @param station:SystemSubstation:厂站对象
	 * @return
	 */
	private AutoMapResultBean initStationCoordinate(SystemSubstation station) {
		AutoMapResultBean result = new AutoMapResultBean();
		try {
			String x = (String) station.getProperty("X");
			String y = (String) station.getProperty("Y");
			double dx = Double.parseDouble(x);
			double dy = Double.parseDouble(y);
			station.setCoordinate(dx, dy);
		} catch (Exception ex) {
			result.setFlag(false);
			result.setErrMsg("初始化厂站坐标时失败！");
		}
		return result;
	}
}
