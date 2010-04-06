package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * 标题：SystemCoordinateCalculate.java
 * </p>
 * <p>
 * 描述： 系统图自动成图坐标计算类
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
public class SystemCoordinateCalculate {
	/**
	 * 厂站编号队列
	 */
	private ArrayList stationIds;
	/**
	 * 厂站集
	 */
	private HashMap stationMap;
	/**
	 * 稀疏矩阵
	 */
	private ArrayList stationMatrix;

	/**
	 * @功能 构造函数
	 * @param stationIds:ArrayList:厂站编号队列
	 * @param stationMap:HashMap:厂站对象集
	 */
	public SystemCoordinateCalculate(ArrayList stationIds, HashMap stationMap) {
		this.stationIds = stationIds;
		this.stationMap = stationMap;
		this.stationMatrix = new ArrayList();
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 坐标推算
	 * @return
	 */
	public AutoMapResultBean calculate(String baseId) {
		// 设置中心厂站
		setCenterStation(baseId);
		// 计算各厂站数据
		double[] distances = calculateStationDatas();
		// 排序厂站数据
		sortStations(distances);
		// 构造稀疏矩阵
		createMatrix();
		// 计算稀疏矩阵中各厂站的图形坐标
		calculateMatrix();

		AutoMapResultBean result = new AutoMapResultBean();
		result.setMsg(stationMatrix);
		return result;
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 计算稀疏矩阵中各厂站坐标
	 */
	private void calculateMatrix() {
		// 获取绘图最短界限
		int min = (SystemGlobal.CANVAS_HEIGHT < SystemGlobal.CANVAS_WIDTH) ? SystemGlobal.CANVAS_HEIGHT
				: SystemGlobal.CANVAS_WIDTH;
		// 环的个数
		int raSize = stationMatrix.size();
		// 步进长度
		double stepRadius = 1.0 * min / raSize;
		// 环计数器
		int raCount = 1;
		// 获取图形中心点坐标
		int originX = SystemGlobal.CANVAS_WIDTH / 2;
		int originY = SystemGlobal.CANVAS_HEIGHT / 2;
		// 获取中心点
		String centerId = (String) stationIds.get(0);
		SystemSubstation centerSub = (SystemSubstation) stationMap
				.get(centerId);
		centerSub.setMapCoord(originX, originY);
		// 循环计算稀疏矩阵中各厂站图像坐标
		for (int i = 0, size = stationMatrix.size(); i < size; i++) {
			String[] subMatrix = (String[]) stationMatrix.get(i);
			for (int j = 0, subSize = subMatrix.length; j < subSize; j++) {
				String id = subMatrix[j];
				if (id != null) {
					SystemSubstation sub = (SystemSubstation) stationMap
							.get(id);
					double stepAngle = 360.0 / size;
					double dAngle = ((j + 0.5) * stepAngle) * Math.PI / 180;
					double dx = (raCount + 0.5) * stepRadius * Math.cos(dAngle);
					double dy = (raCount + 0.5) * stepRadius * Math.sin(dAngle);
					double rowX = originX + dx;
					double rowY = originY + dy;
					sub.setMapCoord(rowX, rowY);
				}
			}
			raCount++;
		}
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 创建稀疏矩阵
	 */
	private void createMatrix() {
		int rCount = 1;
		String[] subMatrix = new String[SystemGlobal.AREA_START_COUNT];
		for (int i = 1, size = stationIds.size(); i < size; i++) {
			String id = (String) stationIds.get(i);
			SystemSubstation sub = (SystemSubstation) stationMap.get(id);
			int index = getAreaIndex(sub.getAngle(), rCount);
			if (subMatrix[index] == null) {
				subMatrix[index] = id;
			} else {
				stationMatrix.add(subMatrix);
				rCount++;
				subMatrix = new String[SystemGlobal.AREA_START_COUNT * rCount];
				subMatrix[index] = id;
			}
		}
		stationMatrix.add(subMatrix);
		stationMatrix.trimToSize();
	}

	/**
	 * 获取指定角度属于第几个区域
	 * 
	 * @param angle:double:角度
	 * @param rCount:int:第几同心圆
	 * @return 区域编号
	 */
	private int getAreaIndex(double angle, int rCount) {
		double unit = 360.0 / (SystemGlobal.AREA_START_COUNT * rCount);
		return (int) (angle / unit);
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 根据距离排序厂站
	 */
	private void sortStations(double[] distances) {
		// 创建临时厂站队列
		ArrayList tempList = new ArrayList();
		for (int i = 0, size = stationIds.size(); i < size; i++) {
			String id = (String) stationIds.get(i);
			double d = distances[i];
			StationDistance sd = new StationDistance(id, d);
			tempList.add(sd);
		}
		tempList.trimToSize();
		// 排序
		StationComparator sc = new StationComparator();
		Collections.sort(tempList, sc);
		// 更新厂站名队列
		stationIds.clear();
		for (int i = 0, size = tempList.size(); i < size; i++) {
			StationDistance sd = (StationDistance) tempList.get(i);
			stationIds.add(sd.getId());
		}
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 计算各厂站相对于基准厂站的距离和角度
	 * @return
	 */
	private double[] calculateStationDatas() {
		int size = stationIds.size();
		if (size <= 0)
			return null;

		String baseId = (String) stationIds.get(0);
		SystemSubstation base = (SystemSubstation) stationMap.get(baseId);
		double[] distances = new double[size];
		for (int i = 0; i < size; i++) {
			String id = (String) stationIds.get(i);
			SystemSubstation sub = (SystemSubstation) stationMap.get(id);
			distances[i] = getDistance(base.getCoordinate(), sub
					.getCoordinate());
			// sub.setDistance(Util.getDistance(base, sub));
			sub.setAngle(getAngle(base.getCoordinate(), sub.getCoordinate()));
		}
		return distances;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 将指定编号的厂站设为中心厂站
	 * @功能 如果指定厂站不存在则计算中心厂站
	 * @param baseId:String:指定厂站编号
	 */
	private void setCenterStation(String baseId) {
		int i = 0;
		int count = stationIds.size();
		while (i < count) {
			String id = (String) stationIds.get(i);
			if (id.equals(baseId)) {
				stationIds.remove(i);
				stationIds.add(0, baseId);
				break;
			}
			i++;
		}
		if (i < count) {
			// 没有找到指定编号厂站
			setCenterStation();
		}
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 计算获取中心厂站，并将其放到队列第一个
	 */
	private void setCenterStation() {
		int count = stationIds.size(); // 厂站个数
		double x; // 所有厂站中心位置X轴坐标
		double y; // 所有厂站中心位置Y轴坐标
		double sumX = 0.0; // 所有厂站X轴坐标
		double sumY = 0.0; // 所有厂站Y轴坐标

		if (count <= 0)
			return;

		// *************
		// 获取中心点坐标
		// *************
		for (int i = 0; i < count; i++) {
			String id = (String) stationIds.get(i);
			SystemSubstation sub = (SystemSubstation) stationMap.get(id);
			sumX += sub.getCoordinate().getX();
			sumY += sub.getCoordinate().getY();
		}
		x = sumX / count;
		y = sumY / count;

		// *******************
		// 计算各厂站到中心点距离
		// *******************
		Point base = new Point(x, y);
		double min = Double.MAX_VALUE;
		int minIndex = -1;
		double[] distances = new double[count];
		for (int i = 0; i < count; i++) {
			String id = (String) stationIds.get(i);
			SystemSubstation sub = (SystemSubstation) stationMap.get(id);
			distances[i] = getDistance(base, sub.getCoordinate());

			if (min > distances[i]) {
				min = distances[i];
				minIndex = i;
			}
		}

		// *****************************
		// 将最短距离那个厂站放置到队列第一位
		// *****************************
		String baseId = (String) stationIds.get(minIndex);
		stationIds.remove(minIndex);
		stationIds.add(0, baseId);
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 获取两点间的距离
	 * @param base:Point:点1
	 * @param target:Point:点2
	 * @return
	 */
	private double getDistance(Point base, Point target) {
		double distance = Double.NaN;
		double dx = base.getX() - target.getX();
		double dy = base.getY() - target.getY();
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @功能 计算目标点先对于基准点的角度
	 * @param base:Point:基准点
	 * @param target:Point:目标点
	 * @return
	 */
	private double getAngle(Point base, Point target) {
		double angle = 0.0;
		double dx = target.getX() - base.getX();
		double dy = target.getY() - base.getY();
		angle = Math.atan2(dy, dx) / Math.PI * 180;
		if (angle < 0)
			angle += 360;
		return angle;
	}

	/**
	 * <p>
	 * 描述： 用来进行厂站排序用的内部类
	 * </p>
	 * 
	 * @author ZHM
	 * @时间: 2009-7-17
	 * @version 1.0
	 */
	public class StationDistance {
		/**
		 * 厂站编号
		 */
		private String id;
		/**
		 * 距离
		 */
		private double distance;

		public StationDistance(String id, double distance) {
			this.id = id;
			this.distance = distance;
		}

		public String getId() {
			return id;
		}

		public double getDistance() {
			return distance;
		}
	}

	/**
	 * <p>
	 * 描述： 排序类
	 * </p>
	 * 
	 * @author ZHM
	 * @时间: 2009-7-17
	 * @version 1.0
	 */
	public class StationComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			StationDistance sd1 = (StationDistance) o1;
			StationDistance sd2 = (StationDistance) o2;
			if (sd1.getDistance() < sd2.getDistance()) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}
