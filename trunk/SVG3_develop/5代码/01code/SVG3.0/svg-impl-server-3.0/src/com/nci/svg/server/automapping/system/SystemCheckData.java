package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * ���⣺SystemCheckData.java
 * </p>
 * <p>
 * ������ ϵͳͼ�Զ���ͼ���ݼ�У��
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
public class SystemCheckData {
	/**
	 * ��վ��Ŷ���
	 */
	private ArrayList stationIds;
	/**
	 * ��վ��
	 */
	private HashMap stationMap;
	/**
	 * ��·��
	 */
	private ArrayList lineList;

	/**
	 * @���� ���캯��
	 * @param stationIds:��վ��Ŷ���
	 * @param stationMap:HashMap:��վ��
	 * @param linelist:ArrayList:��·��
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
		// ��ʼ����վ����
		// ************
		result = initStationData();
		if (!result.isFlag())
			return result;

		// **********
		// ��У��·����
		// **********
		result = initLineData();

		return result;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��·��У
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
				result.setErrMsg("��·��У����");
			}
		}
		return result;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��ʼ����վ����
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
				// ��ʼ������
				result = initStationCoordinate(station);
				if (!result.isFlag())
					return result;
			} else {
				result.setFlag(false);
				result.setErrMsg("��վ����δ�ҵ�ָ������");
				return result;
			}
		}
		return result;
	}

	/**
	 * 2009-7-16 Add by ZHM
	 * 
	 * @���� ��ʼ����վ������
	 * @param station:SystemSubstation:��վ����
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
			result.setErrMsg("��ʼ����վ����ʱʧ�ܣ�");
		}
		return result;
	}
}
