package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;

/**
 * <p>
 * ���⣺SystemCoordinateCalculate.java
 * </p>
 * <p>
 * ������ ϵͳͼ�Զ���ͼ���������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-7-16
 * @version 1.0
 */
public class SystemCoordinateCalculate {
	/**
	 * ��վ��Ŷ���
	 */
	private ArrayList stationIds;
	/**
	 * ��վ��
	 */
	private HashMap stationMap;
	/**
	 * ϡ�����
	 */
	private ArrayList stationMatrix;

	/**
	 * @���� ���캯��
	 * @param stationIds:ArrayList:��վ��Ŷ���
	 * @param stationMap:HashMap:��վ����
	 */
	public SystemCoordinateCalculate(ArrayList stationIds, HashMap stationMap) {
		this.stationIds = stationIds;
		this.stationMap = stationMap;
		this.stationMatrix = new ArrayList();
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��������
	 * @return
	 */
	public AutoMapResultBean calculate(String baseId) {
		// �������ĳ�վ
		setCenterStation(baseId);
		// �������վ����
		double[] distances = calculateStationDatas();
		// ����վ����
		sortStations(distances);
		// ����ϡ�����
		createMatrix();
		// ����ϡ������и���վ��ͼ������
		calculateMatrix();

		AutoMapResultBean result = new AutoMapResultBean();
		result.setMsg(stationMatrix);
		return result;
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @���� ����ϡ������и���վ����
	 */
	private void calculateMatrix() {
		// ��ȡ��ͼ��̽���
		int min = (SystemGlobal.CANVAS_HEIGHT < SystemGlobal.CANVAS_WIDTH) ? SystemGlobal.CANVAS_HEIGHT
				: SystemGlobal.CANVAS_WIDTH;
		// ���ĸ���
		int raSize = stationMatrix.size();
		// ��������
		double stepRadius = 1.0 * min / raSize;
		// ��������
		int raCount = 1;
		// ��ȡͼ�����ĵ�����
		int originX = SystemGlobal.CANVAS_WIDTH / 2;
		int originY = SystemGlobal.CANVAS_HEIGHT / 2;
		// ��ȡ���ĵ�
		String centerId = (String) stationIds.get(0);
		SystemSubstation centerSub = (SystemSubstation) stationMap
				.get(centerId);
		centerSub.setMapCoord(originX, originY);
		// ѭ������ϡ������и���վͼ������
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
	 * @���� ����ϡ�����
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
	 * ��ȡָ���Ƕ����ڵڼ�������
	 * 
	 * @param angle:double:�Ƕ�
	 * @param rCount:int:�ڼ�ͬ��Բ
	 * @return ������
	 */
	private int getAreaIndex(double angle, int rCount) {
		double unit = 360.0 / (SystemGlobal.AREA_START_COUNT * rCount);
		return (int) (angle / unit);
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @���� ���ݾ�������վ
	 */
	private void sortStations(double[] distances) {
		// ������ʱ��վ����
		ArrayList tempList = new ArrayList();
		for (int i = 0, size = stationIds.size(); i < size; i++) {
			String id = (String) stationIds.get(i);
			double d = distances[i];
			StationDistance sd = new StationDistance(id, d);
			tempList.add(sd);
		}
		tempList.trimToSize();
		// ����
		StationComparator sc = new StationComparator();
		Collections.sort(tempList, sc);
		// ���³�վ������
		stationIds.clear();
		for (int i = 0, size = tempList.size(); i < size; i++) {
			StationDistance sd = (StationDistance) tempList.get(i);
			stationIds.add(sd.getId());
		}
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� �������վ����ڻ�׼��վ�ľ���ͽǶ�
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
	 * @���� ��ָ����ŵĳ�վ��Ϊ���ĳ�վ
	 * @���� ���ָ����վ��������������ĳ�վ
	 * @param baseId:String:ָ����վ���
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
			// û���ҵ�ָ����ų�վ
			setCenterStation();
		}
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� �����ȡ���ĳ�վ��������ŵ����е�һ��
	 */
	private void setCenterStation() {
		int count = stationIds.size(); // ��վ����
		double x; // ���г�վ����λ��X������
		double y; // ���г�վ����λ��Y������
		double sumX = 0.0; // ���г�վX������
		double sumY = 0.0; // ���г�վY������

		if (count <= 0)
			return;

		// *************
		// ��ȡ���ĵ�����
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
		// �������վ�����ĵ����
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
		// ����̾����Ǹ���վ���õ����е�һλ
		// *****************************
		String baseId = (String) stationIds.get(minIndex);
		stationIds.remove(minIndex);
		stationIds.add(0, baseId);
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��ȡ�����ľ���
	 * @param base:Point:��1
	 * @param target:Point:��2
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
	 * @���� ����Ŀ����ȶ��ڻ�׼��ĽǶ�
	 * @param base:Point:��׼��
	 * @param target:Point:Ŀ���
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
	 * ������ �������г�վ�����õ��ڲ���
	 * </p>
	 * 
	 * @author ZHM
	 * @ʱ��: 2009-7-17
	 * @version 1.0
	 */
	public class StationDistance {
		/**
		 * ��վ���
		 */
		private String id;
		/**
		 * ����
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
	 * ������ ������
	 * </p>
	 * 
	 * @author ZHM
	 * @ʱ��: 2009-7-17
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
