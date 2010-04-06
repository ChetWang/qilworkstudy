package com.nci.svg.graphunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.util.Constants;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;

/**
 * 图元管理器，所有图元都存放在数据库中
 * 
 * @author Qil.Wong
 * 
 */
public class NCIDBGraphUnitManager extends AbstractNCIGraphUnitManager {

	public NCIDBGraphUnitManager(Editor editor) {
		super(editor);
		getSymbolsTypesDefine();
		initSymbolMap();
		if (parseVersion()){
			System.out.println("create symbols");
			createSymbols();
		}else{
			System.out.println("version ok. No need to create symbols");
		}
	}

	private boolean parseVersion() {
		
		boolean flag = false;
		File symbolVersionFile = new File(Constants.NCI_SVG_SYMBOL_VERSION_FILE);
		Properties p = new Properties();
		try {
			if(!symbolVersionFile.exists()){
				symbolVersionFile.createNewFile();
			}
			p.load(new FileInputStream(symbolVersionFile));
			String localVersion = p.getProperty("version");
			String remoteVersion = (String) Utilities.communicateWithURL(
					(String) editor.getGCParam("appRoot")
							+ (String) editor.getGCParam("servletPath")
							+ "?action=getSymbolsSimpleVersion", null);
			if (localVersion == null || localVersion.equals("")
					|| !localVersion.equals(remoteVersion)) {
				flag = true;
			} else {
				flag = false;
			}
			if (flag) {
				FileOutputStream fos = new FileOutputStream(symbolVersionFile);
				fos.write("version".getBytes("utf-8"));
				fos.write("=".getBytes("utf-8"));
				fos.write(remoteVersion.getBytes("utf-8"));
				fos.write("\r".getBytes("utf-8"));
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 创建图元，根据图元类型进行分类
	 */
	private void createSymbols() {
		File symboCacheDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR);
		if (symboCacheDir.exists()) {
			Utilities.deleteFile(symboCacheDir);
		}
		symboCacheDir.mkdirs();
		NCIEquipSymbolBean bean = null;
		File symbolFile = null;
		File symbolFolder = null;
		StringBuffer sb = null;
		if (symbolMap_type != null) {
			Iterator<String> it = symbolMap_type.keySet().iterator();
			while (it.hasNext()) {
				bean = symbolMap_type.get(it.next());
				sb = new StringBuffer().append(
						Constants.NCI_SVG_SYMBOL_CACHE_DIR).append(
						bean.getGraphUnitType()).append("/").append(
						bean.getGraphUnitName()).append(
						Constants.NCI_SVG_EXTENDSION);
				symbolFile = new File(sb.toString());
				sb = null;
				sb = new StringBuffer().append(
						Constants.NCI_SVG_SYMBOL_CACHE_DIR).append(
						bean.getGraphUnitType()).append("/");
				symbolFolder = new File(sb.toString());
				if (!symbolFolder.exists()) {
					symbolFolder.mkdirs();
				}
				XMLPrinter.printStringToFile(bean.getContent(), symbolFile);
			}
		}
	}

	/**
	 * 初始化图元哈希图
	 */
	@SuppressWarnings("unchecked")
	private void initSymbolMap() {
		try {
			getSymbolsStatus();
			super.originalSymbolMap = (LinkedHashMap<String, NCIEquipSymbolBean>) Utilities
					.communicateWithURL((String) editor.getGCParam("appRoot")
							+ (String) editor.getGCParam("servletPath")
							+ "?action=getSymbolsFromDB", null);
			// System.out.println(originalSymbolMap);
			symbolMap_type = new LinkedHashMap<String, NCIEquipSymbolBean>(
					originalSymbolMap.size());
			// int nSize = symbolMap.size();
			Iterator<String> iterators = originalSymbolMap.keySet().iterator();
			String symbolName = null;
			NCIEquipSymbolBean bean = null;
			while (iterators.hasNext()) {
				symbolName = iterators.next();
				bean = originalSymbolMap.get(symbolName);
				symbolMap_type.put(bean.getGraphUnitType() + "/"
						+ bean.getGraphUnitName(), bean);
				symbolMap_useID.put(bean.getGraphUnitName(), bean);
				if (bean.getGraphUnitStatus() == null
						|| bean.getGraphUnitStatus().equals(
								Constants.GRAPHUNIT_STATUS_NONE))
					continue;

				NCISymbolStatusBean statusBean = statusMap.get(bean
						.getGraphUnitGroup());
				if (statusBean == null) {
					statusBean = new NCISymbolStatusBean(symbolsStatus);
				}

				// 将来修正成服务器编码
				statusBean.setStatusSymbol(symbolsStatus, bean
						.getGraphUnitStatus(), bean.getGraphUnitID());

				statusMap.put(bean.getGraphUnitGroup(), statusBean);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<NCIGraphUnitTypeBean> getEquipSymbolTypeList()
			throws IOException {

		ArrayList<NCIGraphUnitTypeBean> symbolTypeBeans = new ArrayList<NCIGraphUnitTypeBean>(
				getSymbolsTypes().size());
		for (int i = 0; i < getSymbolsTypes().size(); i++) {
			symbolTypeBeans.add(new NCIGraphUnitTypeBean(getSymbolsTypes().get(
					i)));
		}
		return symbolTypeBeans;
	}

	@Override
	public ArrayList<NCIThumbnailPanel> getThumnailList(
			NCIGraphUnitTypeBean bean, int type) {
		ArrayList<NCIThumbnailPanel> thumbnailArray = new ArrayList<NCIThumbnailPanel>();
		try {
			int index = bean.getGraphUnitType().indexOf("=");
			File dir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR
					+ bean.getGraphUnitType().substring(0, index));
			File[] symbols = dir.listFiles();
			if (symbols != null) {
				for (int i = 0; i < symbols.length; i++) {
					if (!symbols[i].getName().endsWith(
							Constants.NCI_SVG_MOD_FILE_EXTENSION)) {
						NCIThumbnailPanel t = new NCIThumbnailPanel(type,
								editor);
						// 名称要将后缀去除
						t.setText(symbols[i].getName()
								.substring(
										0,
										symbols[i].getName().length()
												- Constants.NCI_SVG_EXTENDSION
														.length()));
						t.setDocument(Utilities.getSVGDocument(symbols[i]
								.toURI().toASCIIString()));
						t.getSvgCanvas().setSize(
								NCIThumbnailPanel.outlookPrefferedSize);
						t.setSymbolBean(symbolMap_type.get(bean.getGraphUnitType()
								.substring(0, index)
								+ "/" + t.getText()));
						thumbnailArray.add(t);
						thumbnailMap.put(t.getText(), t);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thumbnailArray;
	}

}
