package com.nci.svg.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.bean.RemoteFileInfoBean;
import com.nci.svg.bean.StationInfoBean;
import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;


public class NCISVGSession {

	private Editor editor;

	private ArrayList<String> javascriptCommands = new ArrayList<String>();

	/**
	 * ��¼���û�
	 */
	private String user = "";

	public NCISVGSession(Editor editor) {
		this.editor = editor;
		user = System.getProperty("user.name");
	}

	boolean increaseDecrese = false;
	int resizeSize = 0;

	/**
	 * ˢ���Ѿ��򿪵�����SVGHandle����
	 * 
	 * @param background
	 */
	public void refreshAllHandles(Color background) {
		increaseDecrese = !increaseDecrese;

		if (increaseDecrese) {
			resizeSize = -1;
		} else {
			resizeSize = 1;
		}
		for (SVGHandle handle : editor.getHandlesManager().getHandles()) {
			if (background != null)
				handle.getCanvas().setBackground(background, true);
			// Ŀ����Ϊ�˴ﵽupdateUIһ����Ч��������������ʱ�޷��ҵ�ȫ������ͼԪ�ķ������϶���ͼԪ�����µĹ켣���ᱻ��һ���������ǣ���
			// ����ֻ�������Ŵ���ʵ�֡�
			handle.getSVGFrame().setSize(
					handle.getSVGFrame().getWidth() + resizeSize,
					handle.getSVGFrame().getHeight() + resizeSize);
			// handle.getCanvas().updateUI();
		}
	}

	public byte[] lock = new byte[0];
	// һ�β���ˢ�µĴ���
	public int refreshCountsOnce = 0;

	public int getRefreshCountsOnce() {
		return refreshCountsOnce;
	}

	public void setRefreshCountsOnce(int refreshCountsOnce) {
		this.refreshCountsOnce = refreshCountsOnce;
	}

	/**
	 * ˢ��ָ��svghandle����(��ǰ)������ֻ��¼���������ͬʱ�ֶ�Σ����Ҳ����ˢ��һ��,
	 * ���ˢ���Ǵ���ص���refreshCurrentHandle()
	 */
	public void refreshHandle() {
		synchronized (lock) {
			refreshCountsOnce++;
		}
	}

	/**
	 * ������ʼˢ�µ�ǰ��SVGHandle����
	 */
	public void refreshCurrentHandleLater() {
		if (refreshCountsOnce > 0) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					SVGHandle handle = editor.getHandlesManager()
							.getCurrentHandle();
					handle.getCanvas().refreshCanvasContent(true, false, null);
					refreshCountsOnce = 0;
				}
			});
		}
	}

	public void refreshCurrentHandleImediately() {
		refreshHandle();
		refreshCurrentHandleLater();
	}

	public void refreshElement(Element oldEle, Element newEle) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				SVGHandle handle = editor.getHandlesManager()
						.getCurrentHandle();
				handle.getCanvas().refreshCanvasContent(false, false, null);
			}
		});
	}

	/**
	 * ��ָ���ļ�����SVG�ļ�
	 * 
	 * @param filename
	 *            �ļ���
	 */
	public void openRemoteSVGFile(String filename) {
		String[][] param = new String[1][2];
		param[0][0] = "filename";
		param[0][1] = filename;
		openRemoteSVGFile(param, filename);
	}

	/**
	 * ��ָ��SVG�ļ�
	 * 
	 * @param fileType
	 *            �ļ�����
	 * @param fileRemark�ļ�����
	 * @param voltageClass
	 *            ��ѹ�ȼ�
	 */
	public void openRemoteSVGFile(String fileType, String fileRemark,
			String voltageClass) {
		String[][] param = new String[3][2];
		param[0][0] = "fileType";
		param[0][1] = fileType;
		param[1][0] = "fileRemark";
		param[1][1] = fileRemark;
		param[2][0] = "voltageClass";
		param[2][1] = voltageClass;
		openRemoteSVGFile(param, fileRemark + ".svg");
	}

	/**
	 * ��ָ���ļ�
	 * 
	 * @param lineID
	 *            ��·���
	 * @param fileType
	 *            �ļ�����
	 * @param fileRemark
	 *            �ļ�����
	 * @param voltageClass
	 *            ��ѹ�ȼ�
	 */
	public void openRemoteSVGFile(String lineID, String fileType,
			String fileRemark, String voltageClass) {
		String[][] param = new String[4][2];
		param[0][0] = "fileType";
		param[0][1] = fileType;
		param[1][0] = "fileRemark";
		param[1][1] = fileRemark;
		param[2][0] = "voltageClass";
		param[2][1] = voltageClass;
		param[3][0] = "lineID";
		param[3][1] = lineID;
		openRemoteSVGFile(param, fileRemark + ".svg");
	}

	/**
	 * ��Զ�̵�SVG�ļ�
	 * 
	 * @param param
	 * @param filename
	 */
	public synchronized void openRemoteSVGFile(final String[][] param,
			final String filename) {
		// final Editor edf = editor;
		SwingWorker worker = new SwingWorker() {
			boolean networkOK = false;

			@Override
			protected Object doInBackground() throws Exception {
				System.out.println("filename:" + filename);
				String svgContent = "";

				// ��ȡԶ�̵�SVG�ļ�
				StringBuffer baseURL = new StringBuffer();
				baseURL.append((String) editor.getGCParam("appRoot")).append(
						(String) editor.getGCParam("servletPath")).append(
						"?action=").append(ServletActionConstants.GET_ONE_FILE);

				svgContent = (String) Utilities.communicateWithURL(baseURL
						.toString(), param);
				networkOK = true;
				if (svgContent == null || svgContent.equals("null")
						|| svgContent.equals("")) {
					return null;
				} else {
					String filePath = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
							+ filename;
					File downloadedFile = new File(filePath);
					XMLPrinter.printStringToFile(svgContent, downloadedFile);
					return downloadedFile;
				}
			}

			public void done() {
				Object o = null;
				try {
					o = get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (o != null)
					editor.getIOManager().getFileOpenManager().open((File) o,
							null);
				else if (networkOK) {
					JOptionPane
							.showConfirmDialog(
									editor.getParent(),
									ResourcesManager.bundle
											.getString("nci_open_cannot_find")
											+ filename,
									ResourcesManager.bundle
											.getString("nci_optionpane_infomation_title"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		LongtermTask task = new LongtermTask(ResourcesManager.bundle
				.getString("nci_opening")
				+ filename + "...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(task);
	}

	/**
	 * �����ڲ�ͼ����Ϣ
	 * 
	 * @param handle
	 */
	public void updateInnerImage(final SVGHandle handle) {
		SwingWorker<String, Element> worker = new SwingWorker<String, Element>() {

			@Override
			protected String doInBackground() throws Exception {
				while (handle.getCanvas().getDocument() == null) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}
				Document svgDoc = handle.getCanvas().getDocument();
				NodeList innerImages = Utilities.findNodes(
						"//*[name()='image'][@imageType='inner']", svgDoc
								.getDocumentElement());
				for (int i = 0; i < innerImages.getLength(); i++) {
					Element innerEle = (Element) innerImages.item(i);
					String innerSerial = innerEle.getAttribute("innerSerial");
					String[][] param = new String[1][2];
					param[0][0] = "innerSerial";
					param[0][1] = innerSerial;
					String base64Content = (String) Utilities
							.communicateWithURL(
									(String) editor.getGCParam("appRoot")
											+ (String) editor
													.getGCParam("servletPath")
											+ "?action="
											+ ServletActionConstants.GET_ONE_INNER_IMAGE,
									param);
					innerEle.removeAttributeNS(svgDoc.getNamespaceURI(),
							"xlink:href");
					innerEle.setAttributeNS(svgDoc.getNamespaceURI(),
							"xlink:href", base64Content);
					publish(innerEle);
				}
				return null;
			}

			@Override
			protected void process(List<Element> elements) {
				for (Element imageEle : elements) {
					refreshCurrentHandleLater();
				}
			}

		};
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(
				new LongtermTask("���ڸ����ڲ�ͼƬ����...", worker));

	}

	public synchronized void openRemoteSVGFileOld(final String[][] param,
			final String filename) {
		SwingWorker worker = null;
		worker = new SwingWorker() {
			boolean networkOK = false;

			@Override
			protected Object doInBackground() throws Exception {
				System.out.println("filename:" + filename);
				String svgContent = "";

				// ��ȡԶ�̵�SVG�ļ�
				StringBuffer baseURL = new StringBuffer();
				baseURL.append((String) editor.getGCParam("appRoot")).append(
						(String) editor.getGCParam("servletPath")).append(
						"?action=").append(ServletActionConstants.GET_SVG_OLD);

				svgContent = (String) Utilities.communicateWithURL(baseURL
						.toString(), param);
				networkOK = true;
				if (svgContent == null || svgContent.equals("null")
						|| svgContent.equals("")) {
					return null;
				} else {
					String filePath = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
							+ filename;
					File downloadedFile = new File(filePath);
					XMLPrinter.printStringToFile(svgContent, downloadedFile);
					return downloadedFile;
				}
			}

			public void done() {
				Object o = null;
				try {
					o = get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (o != null) {
					open((File) o);
				}
				// editor.getIOManager().getFileOpenManager()
				// .open((File) o, null);
				else if (networkOK) {
					JOptionPane
							.showConfirmDialog(
									editor.getParent(),
									ResourcesManager.bundle
											.getString("nci_open_cannot_find")
											+ filename,
									ResourcesManager.bundle
											.getString("nci_optionpane_infomation_title"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		// }
		LongtermTask task = new LongtermTask(ResourcesManager.bundle
				.getString("nci_opening")
				+ filename + "...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(task);
	}

	public void openRemoteSVGFileOld(String code, String filename) {
		String[][] param = new String[1][2];
		if (code != null && code.length() > 0) {
			param[0][0] = "code";
			param[0][1] = code;
			openRemoteSVGFileOld(param, code + ".svg");
		} else {
			param[0][0] = "filename";
			param[0][1] = filename;
			openRemoteSVGFileOld(param, filename);
		}

	}

	/**
	 * ����������豸������ͺ��豸��ţ��򿪶�Ӧ��svgͼ
	 * 
	 * @param codetype���豸���ͣ�Ŀǰ֧��psms��scada��mis����
	 * @param code���豸���
	 */
	public void openRemoteSVGFileOldByCode(final String codetype,
			final String code) {
		final String[][] param = new String[2][2];
		param[0][0] = "type";
		param[0][1] = codetype;
		param[1][0] = "code";
		param[1][1] = code;

		SwingWorker worker = null;
		worker = new SwingWorker() {
			boolean networkOK = false;

			@Override
			protected Object doInBackground() throws Exception {
				String svgContent = "";

				// ��ȡԶ�̵�SVG�ļ�
				StringBuffer baseURL = new StringBuffer();
				baseURL.append((String) editor.getGCParam("appRoot")).append(
						(String) editor.getGCParam("servletPath")).append(
						"?action=getSvgFileByCode");

				svgContent = (String) Utilities.communicateWithURL(baseURL
						.toString(), param);
				networkOK = true;
				if (svgContent == null || svgContent.equals("null")
						|| svgContent.equals("")) {
					return null;
				} else {
					String filePath = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
							+ code + ".svg";
					File downloadedFile = new File(filePath);
					XMLPrinter.printStringToFile(svgContent, downloadedFile);
					return downloadedFile;
				}
			}

			public void done() {
				Object o = null;
				try {
					o = get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (o != null) {
					open((File) o);
				}
				// editor.getIOManager().getFileOpenManager()
				// .open((File) o, null);
				else if (networkOK) {
					JOptionPane
							.showConfirmDialog(
									editor.getParent(),
									ResourcesManager.bundle
											.getString("nci_open_cannot_find")
											+ code,
									ResourcesManager.bundle
											.getString("nci_optionpane_infomation_title"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		// }
		LongtermTask task = new LongtermTask(ResourcesManager.bundle
				.getString("nci_opening")
				+ code + "...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(task);

	}

	public String getUser() {
		return user;
	}

	public void setUser(String loginUser) {
		user = loginUser;
	}

	public void open(File file) {
		final String path = file.toURI().toString();
		SVGHandle handle = editor.getHandlesManager().getHandle(path);

		if (handle != null) {

			editor.getHandlesManager().removeHandle(path);

		}
		{

			// creating a new handle
			handle = editor.getHandlesManager().createSVGHandle(path, 0);

			editor.getResourcesManager().notifyListeners();
			SVGCanvas canvas = handle.getScrollPane().getSVGCanvas();

			canvas.setURI(path, null);

		}
		;
	}

	/**
	 * ��ȡjs����
	 * 
	 * @return
	 */
	public ArrayList<String> getJavascriptCommands() {
		return javascriptCommands;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<RemoteFileInfoBean> getPersonalFileList(String strUserID) {
		String[][] param = new String[1][2];
		param[0][0] = "userID";
		param[0][1] = strUserID;
		ArrayList<RemoteFileInfoBean> list = null;
		try {
			list = (ArrayList<RemoteFileInfoBean>) Utilities
					.communicateWithURL((String) editor.getGCParam("appRoot")
							+ (String) editor.getGCParam("servletPath")
							+ "?action=" + "getPersonalFileList", param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized void getPersonalFile(final String strFileID,
			final String strFileName) {
		SwingWorker worker = new SwingWorker() {
			boolean networkOK = false;

			@Override
			protected Object doInBackground() throws Exception {
				System.out.println("filename:" + strFileID);
				String svgContent = "";
				String[][] param = new String[1][2];
				param[0][0] = "fileID";
				param[0][1] = strFileID;
				// ��ȡԶ�̵�SVG�ļ�
				StringBuffer baseURL = new StringBuffer();
				baseURL.append((String) editor.getGCParam("appRoot")).append(
						(String) editor.getGCParam("servletPath")).append(
						"?action=").append("getPersonalFile");

				svgContent = (String) Utilities.communicateWithURL(baseURL
						.toString(), param);
				networkOK = true;
				if (svgContent == null || svgContent.equals("null")
						|| svgContent.equals("")) {
					return null;
				} else {
					String filePath = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
							+ strFileName + ".svg";
					File downloadedFile = new File(filePath);
					XMLPrinter.printStringToFile(svgContent, downloadedFile);
					return downloadedFile;
				}
			}

			public void done() {
				Object o = null;
				try {
					o = get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (o != null)
					editor.getIOManager().getFileOpenManager().open((File) o,
							null);
				else if (networkOK) {
					JOptionPane
							.showConfirmDialog(
									editor.getParent(),
									ResourcesManager.bundle
											.getString("nci_open_cannot_find")
											+ strFileID,
									ResourcesManager.bundle
											.getString("nci_optionpane_infomation_title"),
									JOptionPane.CLOSED_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		LongtermTask task = new LongtermTask(ResourcesManager.bundle
				.getString("nci_opening")
				+ strFileName + "...", worker);
		LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(task);
	}

	public void openRemoteSVGFileByCode(Element element) {
		Element objref = Utilities.getSingleChildElement("PSR:ObjRef", element);
		if (objref == null)
			return;
		String strPsmsID = getPSMSID(objref);
		if (strPsmsID == null || strPsmsID.length() == 0)
			return;

		String strMapID = getMapID(strPsmsID);
		if (strMapID == null || strMapID.length() == 0)
			return;

		openRemoteSVGFileOldByCode("psms", strMapID);

	}

	/**
	 * ����psms�����ȡ��Ӧ�ĳ�վ����·����λ����
	 * 
	 * @param strPsmsID
	 * @return
	 */
	public String getMapID(String strPsmsID) {
		StringBuffer baseUrl = new StringBuffer().append(
				(String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append(
				"?action=get_relateMapID");
		final String[][] param = new String[1][2];
		param[0][0] = "psmsID";
		param[0][1] = strPsmsID;
		String strResult = null;
		try {
			strResult = (String) Utilities.communicateWithURL(baseUrl
					.toString(), param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			strResult = "-1";
		}
		if (strResult.equals("-1"))
			strResult = "";
		return strResult;
	}

	public String getPSMSID(Element objref) {
		String strPsmsID = "";
		// ����ȡpsmsid
		String strcode = objref.getAttribute("AppCode");
		if (strcode.length() != 0) {
			strPsmsID = strcode;
			return strPsmsID;
		}

		// psmsid�����ڣ���ȡscadaid
		strcode = objref.getAttribute("ScadaID");
		if (strcode.length() != 0) {
			strPsmsID = getPSMSID(strcode);
			return strPsmsID;
		}

		strcode = objref.getAttribute("ObjectID");
		if (strcode.length() != 0) {
			strPsmsID = getPSMSID(strcode);
			return strPsmsID;
		}

		// scadaid�����ڣ���ȡmisid
		strcode = objref.getAttribute("MisID");
		if (strcode.length() != 0) {
			strPsmsID = getPSMSID(strcode);
			return strPsmsID;
		}
		return "";
	}

	/**
	 * ����scada�����ȡpsms����
	 * 
	 * @param strScadaID
	 * @return
	 */
	public String getPSMSID(String strScadaID) {
		StringBuffer baseUrl = new StringBuffer().append(
				(String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append(
				"?action=hn_equipManualMapSearch");
		final String[][] param = new String[1][2];
		param[0][0] = "scadaID";
		param[0][1] = strScadaID;
		String strResult = null;
		try {
			strResult = (String) Utilities.communicateWithURL(baseUrl
					.toString(), param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			strResult = "-1";
		}
		if (strResult.equals("-1"))
			strResult = "";
		return strResult;
	}

	/**
	 * �������Ŷα�Ŵ�SVGͼ
	 * @param strLineID PSMS�����Ŷ˵ı�ţ��̺ţ�
	 * @param fileType ���ļ����ͣ�1��ʾվ��ͼ��2��ʾ����ͼ
	 * @param type ���type=1ʱ����һ�ν���ͼ����˸�������豸
	 */
	public void openFileByLineID(final String strLineID, final int fileType, final int type) {
		StringBuffer baseUrl = new StringBuffer().append(
				(String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append(
				"?action=getStationInfoByLineID");
		final String[][] param = new String[3][2];
		param[0][0] = "obj_id";
		param[0][1] = strLineID;
		param[1][0] = "contentFlag";
		param[1][1] = "1";
		param[2][0] = "fileType";
		param[2][1] = Integer.toString(fileType);
		
		StationInfoBean bean = null;
		try {
			bean = (StationInfoBean) Utilities.communicateWithURL(baseUrl
					.toString(), param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bean = null;
		}

		// System.err.println("length:"+bean.getStrStationID().length());
		if (bean == null || bean.getStrStationID().length() == 0)
			return;

		String filePath = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
				+ bean.getStrStationName() + ".svg";
		// System.err.println("filePath:"+filePath);
		File downloadedFile = new File(filePath);
		XMLPrinter.printStringToFile(bean.getStrContent(), downloadedFile);
		editor.getIOManager().getFileOpenManager().open(downloadedFile, null);
		// ѡ�й������豸
		if (type == 1) {
			ArrayList list = bean.getEquipList();
			System.err.println("ZHM:SIZE OF LIST:" + list.size());

			if (list == null || list.size() == 0)
				return;
			SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
			if (handle == null)
				return;
			for (int i = 0; i < list.size(); i++) {
				System.err.println("list details:" + list.get(i));
			}

			while (handle.getSelection() == null) {
				try {
					System.out.println("handle selection is null, waiting...");
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			handle.getSelection().handleSelection(list);
			handle.getSelection().setWink(true, 1);
		}
		return;
	}
	
	/**
	 * ����misID��SVGͼ
	 * @param misID String mis���
	 */
	public void openFileByMisID(String misID){
		StringBuffer baseUrl = new StringBuffer().append(
				(String) editor.getGCParam("appRoot")).append(
				(String) editor.getGCParam("servletPath")).append(
				"?action=openFileByMisID");
		final String[][] param = new String[1][2];
		param[0][0] = "misID";
		param[0][1] = misID;
		
		StationInfoBean bean = null;
		try {
			bean = (StationInfoBean) Utilities.communicateWithURL(baseUrl
					.toString(), param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bean = null;
		}

		// System.err.println("length:"+bean.getStrStationID().length());
		if (bean == null || bean.getStrStationID().length() == 0)
			return;

		String filePath = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
				+ bean.getStrStationName() + ".svg";
		// System.err.println("filePath:"+filePath);
		File downloadedFile = new File(filePath);
		XMLPrinter.printStringToFile(bean.getStrContent(), downloadedFile);
		editor.getIOManager().getFileOpenManager().open(downloadedFile, null);
		
	}

	/**
	 * ��������useǶ�׵�ͼԪ
	 * 
	 * @param doc
	 * @param firstSymbolEle
	 * @param defsEle
	 */
	public void iteratorUseElementInserting(Document doc, Element firstSymbolEle) {
		try {
			Element defsEle = null;
			NodeList defs = doc.getDocumentElement().getElementsByTagName(
					"defs");
			if (defs.getLength() <= 0) {
				defsEle = doc.createElement("defs");
				doc.getDocumentElement().appendChild(defsEle);
			} else {
				defsEle = (Element) defs.item(0);
			}
			Element element = (Element) doc.importNode(firstSymbolEle
					.getElementsByTagName("symbol").item(0), true);
			String id = element.getAttribute("id");

			NodeList nodes = defsEle.getElementsByTagName("symbol");
			boolean exist = false;
			for (int i = 0; i < nodes.getLength(); i++) {
				String existID = ((Element) nodes.item(i)).getAttribute("id");
				if (existID.equals(id)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				defsEle.appendChild(element);
			}
			//���ҳ���Ƕ�׵�use�ӽڵ�
			NodeList childUse = element.getElementsByTagName("use");
			NCIEquipSymbolBean symbolBean;
			Element useSymbolEle;
			for (int i = 0; i < childUse.getLength(); i++) {
				element = (Element) childUse.item(i);
				id = element.getAttribute("xlink:href").substring(1);
				symbolBean = editor.getGraphUnitManager().getSymbolMap_useID()
						.get(id);
				if (symbolBean != null && symbolBean.getContent() != null) {
					useSymbolEle = Utilities.getXMLDocumentByString(
							symbolBean.getContent()).getDocumentElement();
					iteratorUseElementInserting(doc, useSymbolEle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ͨ�÷���
	 * @param methodName
	 * @param params
	 * @param paramTypes
	 * @return
	 */
	public Object invoke(String methodName, Object[] params, Class[] paramTypes){
		try {
			return this.getClass().getMethod(methodName, paramTypes).invoke(this, params);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
