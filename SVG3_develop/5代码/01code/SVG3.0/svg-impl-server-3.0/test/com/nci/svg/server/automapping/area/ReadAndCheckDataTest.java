package com.nci.svg.server.automapping.area;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.nci.svg.server.automapping.comm.AutoMapComm;
import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.util.XmlUtil;

public class ReadAndCheckDataTest extends TestCase {

	public ReadAndCheckDataTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDoubleNaN() {
		double d = Double.NaN;
		assertEquals(Double.isNaN(d), true);
	}

	/**
	 * ���Դ���̨��ͼ����
	 */
	public void testCreateAreaMap() {
		String dataString = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\AutoMappingArea.xml");
		ReadData ptd = new ReadData(dataString);

		ArrayList areaList = ptd.getAreaList();
		AutoMapResultBean resultBean = null;
		CheckData checkData = new CheckData();

		Area area = (Area) areaList.get(0);
		resultBean = checkData.checkAreaData(area);
		if (!resultBean.isFlag()) {
			System.out.println(resultBean.getErrMsg());
			return;
		} else {
			System.out.println("���Ϊ'" + area.getProperty("code")
					+ "'��̨�����ݼ�У�ɹ���");
		}
		CoordinateCalculate cc = new CoordinateCalculate(area);
		resultBean = cc.calculate();
		if (!resultBean.isFlag()) {
			System.out.println(resultBean.getErrMsg());
			return;
		} else {
			System.out.println("���Ϊ'" + area.getProperty("code")
					+ "'��̨����������ɹ���");
		}
		area = (Area) resultBean.getMsg();
		printPoleCoordinate(area);
		CreateAreaMap cam = new CreateAreaMap(area);
		resultBean = cam.createSVG();
		if (resultBean.isFlag()) {
			Document doc = (Document) resultBean.getMsg();
			String filename = "log/area.svg";
			writeSVGFile(doc, filename);
		} else {
			return;
		}
	}

	/**
	 * ���Խ�document����д�뵽�ļ���
	 */
	public void testWriteFile() {
		String dataString = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\AutoMappingArea.xml");
		Document document = XmlUtil.getXMLDocumentByString(dataString);
		document.getDocumentElement().normalize();

		String filename = "log/area.svg";
		writeSVGFile(document, filename);
	}

	/**
	 * �������ݶ�ȡ�๦��
	 */
	public void testReadData() {
		String dataString = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\AutoMappingArea.xml");
		// System.out.println(dataString);
		ReadData ptd = new ReadData(dataString);
		System.out.println(ptd.getDataString());
	}

	/**
	 * �������ݼ�У�๦��
	 */
	public void testCheckData() {
		String dataString = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\AutoMappingArea.xml");
		ReadData ptd = new ReadData(dataString);

		ArrayList areaList = ptd.getAreaList();
		AutoMapResultBean resultBean = null;
		CheckData checkData = new CheckData();

		for (int i = 0, size = areaList.size(); i < size; i++) {
			Area area = (Area) areaList.get(i);
			resultBean = checkData.checkAreaData(area);
			if (!resultBean.isFlag()) {
				System.out.println(resultBean.getErrMsg());
			} else {
				System.out.println("���Ϊ'" + area.getProperty("code")
						+ "'��̨�����ݼ�У�ɹ���");
			}
		}
	}

	/**
	 * �����������㹦��
	 */
	public void testCalculateData() {
		String dataString = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\AutoMappingArea.xml");
		ReadData ptd = new ReadData(dataString);

		ArrayList areaList = ptd.getAreaList();
		AutoMapResultBean resultBean = null;
		CheckData checkData = new CheckData();

		CoordinateCalculate cc;

		for (int i = 0, size = areaList.size(); i < size; i++) {
			Area area = (Area) areaList.get(i);
			resultBean = checkData.checkAreaData(area);
			if (!resultBean.isFlag()) {
				System.out.println(resultBean.getErrMsg());
			} else {
				System.out.println("���Ϊ'" + area.getProperty("code")
						+ "'��̨�����ݼ�У�ɹ���");
				cc = new CoordinateCalculate(area);
				resultBean = cc.calculate();
				if (!resultBean.isFlag()) {
					System.out.println(resultBean.getErrMsg());
				} else {
					System.out.println("���Ϊ'" + area.getProperty("code")
							+ "'��̨����������ɹ���");
					area = (Area) resultBean.getMsg();
					printPoleCoordinate(area);
				}
			}
		}
	}

	private void printPoleCoordinate(Area area) {
		LinkedHashMap polelist = area.getElectricPoleList();
		Iterator it = polelist.values().iterator();
		while (it.hasNext()) {
			AreaPole pole = (AreaPole) it.next();
			System.out.println(pole.getProperty("name") + ":"
					+ pole.getProperty("code") + ":x:" + pole.getCoordinateX()
					+ ",y:" + pole.getCoordinateY());
		}
	}

	/**
	 * ��documentд��ָ���ļ���
	 * 
	 * @param doc
	 * @param filename
	 */
	private void writeSVGFile(Document doc, String filename) {
		try {
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			DOMSource source = new DOMSource(doc);
			// StreamResult rs = new StreamResult(new File(filename));
			StreamResult rs = new StreamResult(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF-8"));
			tf.setOutputProperty("encoding", "UTF-8");
			tf.transform(source, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDoubleArrayList() {
		double[] a = { 1.0, 2.0 };
		double[] b = (double[]) a.clone();
		printDoubleArrayList(a);
		printDoubleArrayList(b);
		b[0] = 0.0;
		printDoubleArrayList(a);
		printDoubleArrayList(b);
	}

	private void printDoubleArrayList(double[] a) {
		for (int i = 0, size = a.length; i < size; i++) {
			System.out.print(a[i] + ",");
		}
		System.out.println("");
	}

	public void testArrayListAdd() {
		ArrayList list = new ArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(new Integer(i));
		}
		list.add(5, "s");
		assertEquals("s", (String) list.get(5));
		assertEquals(0, ((Integer) list.get(0)).intValue());
	}
}
