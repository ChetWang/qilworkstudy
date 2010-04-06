package com.nci.svg.server.automapping.area;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * ���⣺CheckData.java
 * </p>
 * <p>
 * ������������Ч�Լ�飬�������ݽ���Ԥ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-03
 * @version 1.0
 */
public class CheckData {

	/**
	 * ���캯��
	 * 
	 * @param area:Area:̨������
	 */
	public CheckData() {

	}

	/**
	 * ���̨�����ݺϷ���
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
	 * �����·���ݺϷ���
	 * 
	 * @return �����
	 */
	private AutoMapResultBean checkLine(Area area, AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		// *******************
		// �����·��ʼ���Ƿ�Ϸ�
		// *******************
		double angle = parseDouble((String) line.getProperty("startAngle"),
				Double.NaN);
		if (Double.isNaN(angle)) {
			resultBean.setFlag(false);
			reBuffer.append("���Ϊ'").append(line.getProperty("code")).append(
					"'����·��������ʼ�ǣ�");
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;
		// *****************
		// �����·�ܳ��Ƿ�Ϸ�
		// *****************
		double distance = parseDouble((String) line.getProperty("distance"),
				-1.0);
		if (distance <= 0.0) {
			reBuffer.append("���Ϊ'").append(line.getProperty("code")).append(
					"'����·����·�ܳ����Ϸ���");
			resultBean.setFlag(false);
		}
		if (!resultBean.isFlag())
			return resultBean;
		// *********
		// ���߸˼��
		// *********
		resultBean = checkPlugPoleOfLine(line);
		if (!resultBean.isFlag())
			return resultBean;
		// **************
		// ֧�ߵĸ���·���
		// **************
		resultBean = checkParentLine(area, line);
		if (!resultBean.isFlag())
			return resultBean;
		// **************
		// ��·�����������
		// **************
		resultBean = checkLinePoles(line);
		if (!resultBean.isFlag())
			return resultBean;
		else {
			Object poleMap = resultBean.getMsg();
			if (poleMap instanceof LinkedHashMap)
				area.getElectricPoleList().putAll((LinkedHashMap) poleMap);
		}

		// ������·��ʼ��
		line.setProperty("startAngle", new Double(angle));
		// ������·�ܳ�
		line.setProperty("distance", new Double(distance));

		// ����area�����е���·����
		area.getLinesList().put(line.getProperty("code"), line);
		return resultBean;
	}

	/**
	 * �����·�����и����Ϸ���
	 * 
	 * @param line:Line:��·����
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
				// ����������Ч
				String poleCode = (String) pole.getProperty("code");
				poleMap.put(poleCode, pole);
				tempList.add(poleCode);
			} else {
				// ����������Ч
				return resultBean;
			}
		}

		tempList.trimToSize();
		line.setPoles(tempList);
		resultBean.setMsg(poleMap);

		return resultBean;
	}

	/**
	 * ���ָ����������Ϸ���
	 * 
	 * @param pole:ElectricPole:��������
	 * @return
	 */
	private AutoMapResultBean checkPole(AreaPole pole) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		// ������ߺϷ���
		double height = parseDouble((String) pole.getProperty("height"),
				Double.NaN);
		if (Double.isNaN(height)) {
			reBuffer.append("���Ϊ'").append(pole.getProperty("code")).append(
					"'�ĸ���height��������");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;
		// ������Ϸ���
		double distance = parseDouble((String) pole.getProperty("distance"),
				Double.NaN);
		if (Double.isNaN(distance)) {
			reBuffer.append("���Ϊ'").append(pole.getProperty("code")).append(
					"'�ĸ���distance��������");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;
		// ��ȡ�����Ƕ�ֵ
		double angle = parseDouble((String) pole.getProperty("angle"), 0.0);
		if (Double.isNaN(angle)) {
			reBuffer.append("���Ϊ'").append(pole.getProperty("code")).append(
					"'�ĸ���angle��������");
			resultBean.setFlag(false);
			resultBean.setErrMsg(reBuffer.toString());
		}
		if (!resultBean.isFlag())
			return resultBean;

		// ��������
		pole.setProperty("height", new Double(height));
		// ���þ���
		pole.setProperty("distance", new Double(distance));
		// ���ø���ת��ֵ
		pole.setProperty("angle", new Double(angle));
		// ���ý���ת��ֵ
		String tempAngle = (String) pole.getProperty("contactAngle");
		if (tempAngle != null) {
			double contactAngle = parseDouble((String) pole
					.getProperty("contactAngle"), Double.NaN);
			if (Double.isNaN(contactAngle)) {
				reBuffer.append("���Ϊ'").append(pole.getProperty("code"))
						.append("'�ĸ���contactAngle�Ƕ�ֵ����");
				resultBean.setFlag(false);
				resultBean.setErrMsg(reBuffer.toString());
			}
			if (!resultBean.isFlag())
				return resultBean;
			pole.setProperty("contactAngle", new Double(contactAngle));
		}
		// ���ý��������־
		String contactTag = (String) pole.getProperty("contactTag");
		if (contactTag != null && contactTag.equalsIgnoreCase("true"))
			pole.setPlugin(true);

		return resultBean;
	}

	/**
	 * �����·�ĸ���·�Ƿ�Ϸ�
	 * 
	 * @param area:Area:̨������
	 * @param line:Line:��·����
	 * @return
	 */
	private AutoMapResultBean checkParentLine(Area area, AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		String pline = (String) line.getProperty("pline");
		if (pline != null && pline.length() > 0) {
			// ����·��Ϣ����
			Object obj = area.getLinesList().get(pline);
			if (obj instanceof AreaLine) {
				String startPole = (String) line.getProperty("startPole");
				if (!(startPole != null && startPole.length() > 0)) {
					// ��ȡ��������·�ӳ�����Ϣ
					reBuffer.append("���Ϊ'").append(line.getProperty("code"))
							.append("'��֧��ȱ�ٸ���·�Ľӳ�����Ϣ��");
					resultBean.setFlag(false);
					resultBean.setErrMsg(reBuffer.toString());
					return resultBean;
				}

				// ��ȡ������·
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
					// ����·�Ľӳ����ڸ���·�ϲ�����
					reBuffer.append("���Ϊ'").append(line.getProperty("code"))
							.append("'֧�ߵĸ���·�ӳ����ڸ���·�ϲ����ڣ�");
					resultBean.setFlag(false);
					resultBean.setErrMsg(reBuffer.toString());
				}
			} else {
				// δ��ȡ������·
				reBuffer.append("���Ϊ'").append(line.getProperty("code"))
						.append("'��֧��δ�ҵ��丸��·��Ϣ��");
				resultBean.setFlag(false);
				resultBean.setErrMsg(reBuffer.toString());
			}
			line.setBranch(true);
		}
		return resultBean;
	}

	/**
	 * ���ָ����·�Ľ�����Ƿ�Ϸ�
	 * 
	 * @param line:Line:��·����
	 * @return
	 */
	private AutoMapResultBean checkPlugPoleOfLine(AreaLine line) {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		StringBuffer reBuffer = new StringBuffer();

		String middleTag = (String) line.getProperty("middleTag");
		if (middleTag != null && middleTag.equalsIgnoreCase("true")) {
			// ������·ע��Ϊ�м������·
			line.setMiddle(true);

			boolean hasPlugPole = false;
			boolean hasContactAngle = false; // �����ϵĽ���Ǳ�־
			ArrayList polelist = line.getPoles();
			for (int i = 0, size = polelist.size(); i < size; i++) {
				AreaPole pole = (AreaPole) polelist.get(i);
				String contactTag = (String) pole.getProperty("contactTag");
				if (contactTag != null && contactTag.equalsIgnoreCase("true")) {
					String pline = (String) line.getProperty("pline");
					hasPlugPole = true;
					if (pline != null && pline.length() > 0) {
						// ����·Ϊ֧��
						hasContactAngle = true;
					} else {
						// ����Ϊ����
						String contactAngle = (String) pole
								.getProperty("contactAngle"); // ��ȡ�����
						if (contactAngle != null && contactAngle.length() > 0) {
							hasContactAngle = true;
						}
					}
					break;
				}
			}
			if (!hasPlugPole) {
				resultBean.setFlag(false);
				reBuffer.append("���Ϊ'").append(line.getProperty("code"))
						.append("'����·����ʶΪ����ʼ�˽��룬��ȱ�ٽ������Ϣ��");
				resultBean.setErrMsg(reBuffer.toString());
			}
			if (!hasContactAngle) {
				resultBean.setFlag(false);
				reBuffer.append("���Ϊ'").append(line.getProperty("code"))
						.append("'������·����ʶΪ����ʼ�˽��룬��ȱ����Ч�������Ϣ��");
				resultBean.setErrMsg(reBuffer.toString());
			}
		}
		return resultBean;
	}

	/**
	 * ���ַ�������ת���ɸ�������ֵ
	 * 
	 * @param data:String:�ַ�����ֵ
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
