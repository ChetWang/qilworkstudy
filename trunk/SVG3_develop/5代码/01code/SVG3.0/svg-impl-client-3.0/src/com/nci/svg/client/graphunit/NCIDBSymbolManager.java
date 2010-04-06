package com.nci.svg.client.graphunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nci.svg.sdk.CodeConstants;
import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.AbstractSymbolManager;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.OwnerVersionBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.graphunit.SymbolVersionParser;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.ui.graphunit.NCIThumbnailPanel;

import fr.itris.glips.library.util.XMLPrinter;

/**
 * 图元管理器，所有图元都存放在数据库中
 * 
 * @author Qil.Wong
 * 
 */
public class NCIDBSymbolManager extends AbstractSymbolManager {

	String personalCacheSymbolPath = Constants.NCI_SVG_CACHE_DIR
			+ editor.getSvgSession().getUser() + "_symbol.nci";

	String releasedCacheSymbolPath = Constants.NCI_SVG_CACHE_DIR
			+ OwnerVersionBean.OWNER_RELEASED + "_symbol.nci";

	public NCIDBSymbolManager(EditorAdapter editor) {
		super(editor);
		getSymbolsTypesDefine();
//		boolean[] flags = parseVersion();

//		initSymbolMap(flags);
		initSymbolMap(new boolean[]{true,true});

		// if (parseVersion()[0] || parseVersion()[1]) {// 只要任何一类变动，全部重新创建
		// createSymbols();
		// }
	}

	/**
	 * 版本比对，第一个是personal版本结果，第二个是released版本结果
	 * 
	 * @return boolean数组，其中元素如果是true，说明对应的文件已经变化
	 */
	private boolean[] parseVersion() {
		boolean[] flag = new boolean[2];
		File symbolVersionFile = new File(Constants.NCI_SVG_SYMBOL_VERSION_FILE);
		try {
			String localPersonalVersion = null;
			String localReleaseVersion = null;
			Map<String, String> versionMap = null;
			if (!symbolVersionFile.exists()) {
				symbolVersionFile.createNewFile();
			} else {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(symbolVersionFile));
				versionMap = (Map<String, String>) ois.readObject();
				localPersonalVersion = versionMap.get(editor.getSvgSession()
						.getUser());
				localReleaseVersion = versionMap
						.get(OwnerVersionBean.OWNER_RELEASED);
				ois.close();
			}
			String[][] params = new String[1][2];
			params[0][0] = ActionParams.OWNERS;
			params[0][1] = SymbolVersionParser.createOwnersStr(new String[] {
					editor.getSvgSession().getUser(),
					OwnerVersionBean.OWNER_RELEASED });
			String remoteVersion = (String) editor.getCommunicator()
					.communicate(
							new CommunicationBean(
									ActionNames.GET_SYMBOLS_VERSION, params))
					.getReturnObj();
			String remotePersonalVersion = SymbolVersionParser.parseVersion(
					remoteVersion, editor.getSvgSession().getUser());
			String remoteReleasedVersion = SymbolVersionParser.parseVersion(
					remoteVersion, OwnerVersionBean.OWNER_RELEASED);
			if (localPersonalVersion == null || localPersonalVersion.equals("")
					|| !localPersonalVersion.equals(remotePersonalVersion)) {
				flag[0] = true;
			} else {
				flag[0] = false;
			}
			if (localReleaseVersion == null || localReleaseVersion.equals("")
					|| !localReleaseVersion.equals(remoteReleasedVersion)) {
				flag[1] = true;
			} else {
				flag[1] = false;
			}

			if (flag[0] || flag[1]) {
				if (versionMap == null) {
					versionMap = new HashMap<String, String>();
				}
				versionMap.put(editor.getSvgSession().getUser(),
						remotePersonalVersion);
				versionMap.put(OwnerVersionBean.OWNER_RELEASED,
						remoteReleasedVersion);
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(symbolVersionFile));
				oos.writeObject(versionMap);
				oos.flush();
				oos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 创建图元，根据图元类型进行分类
	 */
	private void createSymbols() {
		File symboCacheDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR);
		File graphUnitDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR
				+ NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT);
		File templateDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR
				+ NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE);

		if (symboCacheDir.exists()) {
			Utilities.deleteFile(symboCacheDir);
		}
		symboCacheDir.mkdirs();
		graphUnitDir.mkdirs();
		templateDir.mkdirs();

		NCIEquipSymbolBean symbolBean = null;
		File symbolFile = null;
		StringBuffer sb = null;
		SymbolTypeBean symbolTypeBean = null;
		Map<String, NCIEquipSymbolBean> temp = null;
		if (symbolsMap != null) {
			Iterator<SymbolTypeBean> it = symbolsMap.keySet().iterator();
			while (it.hasNext()) {
				// 取出大类
				symbolTypeBean = it.next();
				// 取出大类所属的图元
				temp = symbolsMap.get(symbolTypeBean);
				Iterator<NCIEquipSymbolBean> symbolIt = temp.values()
						.iterator();
				// 图元、模板大类代码bean
				ResultBean codeRes = null;
				String codeVariety = null;
				if (symbolTypeBean.getSymbolType().equalsIgnoreCase(
						NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
					codeVariety = CodeConstants.SVG_GRAPHUNIT_VARIETY;
				} else if (symbolTypeBean.getSymbolType().equalsIgnoreCase(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
					codeVariety = CodeConstants.SVG_TEMPLATE_VARIETY;
				}
				while (symbolIt.hasNext()) {
					symbolBean = symbolIt.next();
					codeRes = editor.getDataManage().getData(
							DataManageAdapter.KIND_CODES, codeVariety,
							symbolTypeBean.getVariety().getCode());
					String realTypeName = ((CodeInfoBean) codeRes
							.getReturnObj()).getName();
					sb = new StringBuffer(Constants.NCI_SVG_SYMBOL_CACHE_DIR)
							.append(symbolTypeBean.getSymbolType()).append("/")
							.append(realTypeName).append("/").append(
									symbolBean.getName()).append(
									Constants.NCI_SVG_EXTENDSION);
					symbolFile = new File(sb.toString());
					sb = null;
					XMLPrinter.printStringToFile(symbolBean.getContent(),
							symbolFile);
				}
			}
		}
	}

	/**
	 * 初始化图元哈希图
	 * 
	 * @param flags
	 *            是否需要更新标记,第一个是personal，第二个是released
	 */
	@SuppressWarnings("unchecked")
	private void initSymbolMap(final boolean[] flags) {
		Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> personalSymbolMap = null;
		Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> releasedSymbolMap = null;
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.OWNER;
		if (flags[0]) {// personal的symbol需要更新
			params[0][1] = editor.getSvgSession().getUser();
			ResultBean result = editor.getCommunicator()
					.communicate(
							new CommunicationBean(ActionNames.GET_SYMBOLS_LIST,
									params));
			personalSymbolMap = (Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>>) result
					.getReturnObj();
			if (personalSymbolMap == null) {
				personalSymbolMap = new LinkedHashMap<SymbolTypeBean, Map<String, NCIEquipSymbolBean>>();
			}
		} else {
			personalSymbolMap = readCacheSymbol(personalCacheSymbolPath);
		}
		editor.getLogger().log(editor, LoggerAdapter.DEBUG,
				"获取到Personal Symbols:" + personalSymbolMap);
		if (flags[1]) {// released的symbol需要更新
			params[0][1] = OwnerVersionBean.OWNER_RELEASED;
			ResultBean result = editor.getCommunicator()
					.communicate(
							new CommunicationBean(ActionNames.GET_SYMBOLS_LIST,
									params));
			releasedSymbolMap = (Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>>) result
					.getReturnObj();
			if (releasedSymbolMap == null) {
				releasedSymbolMap = new LinkedHashMap<SymbolTypeBean, Map<String, NCIEquipSymbolBean>>();
			}
		} else {
			releasedSymbolMap = readCacheSymbol(releasedCacheSymbolPath);
		}
		editor.getLogger().log(editor, LoggerAdapter.DEBUG,
				"获取到ReleasedSymbolMap Symbols:" + releasedSymbolMap);
		symbolsMap = new LinkedHashMap<SymbolTypeBean, Map<String, NCIEquipSymbolBean>>(
				(int) ((personalSymbolMap.size() + releasedSymbolMap.size()) / 0.7));
		if (releasedSymbolMap != null) {
			symbolsMap.putAll(releasedSymbolMap);
		}
		if (personalSymbolMap != null) {
			Iterator<SymbolTypeBean> typeBeanIt = personalSymbolMap.keySet()
					.iterator();
			while (typeBeanIt.hasNext()) {
				SymbolTypeBean typeBean = typeBeanIt.next();
				if (symbolsMap.containsKey(typeBean)) {
					symbolsMap.get(typeBean).putAll(
							personalSymbolMap.get(typeBean));
				} else {
					symbolsMap.put(typeBean, personalSymbolMap.get(typeBean));
				}
			}
		}
	}

	private void writeCacheSymbol(
			Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolMap,
			String path) {
		File f = new File(path);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(f));
			oos.writeObject(symbolMap);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			editor.getLogger().log(editor, LoggerAdapter.FATAL,
					"无法找到本地SYMBOL缓存文件：" + path);
			editor.getLogger().log(editor, LoggerAdapter.FATAL, e);
		} catch (IOException e) {
			editor.getLogger().log(editor, LoggerAdapter.FATAL,
					"无法本地写入SYMBOL缓存文件：" + path);
			editor.getLogger().log(editor, LoggerAdapter.FATAL, e);
		}

	}

	private Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> readCacheSymbol(
			String path) {
		File f = new File(path);
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(f));

			Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> map = (Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>>) is
					.readObject();
			return map;
		} catch (FileNotFoundException e) {
			editor.getLogger().log(editor, LoggerAdapter.FATAL,
					"无法找到本地SYMBOL缓存文件：" + path);
			editor.getLogger().log(editor, LoggerAdapter.FATAL, e);
		} catch (IOException e) {
			editor.getLogger().log(editor, LoggerAdapter.FATAL,
					"无法从本地读取SYMBOL缓存文件：" + path);
			editor.getLogger().log(editor, LoggerAdapter.FATAL, e);
		} catch (ClassNotFoundException e) {
			editor.getLogger().log(editor, LoggerAdapter.FATAL,
					"从本地读取SYMBOL缓存文件：" + path + "时对象转换错误！");
			editor.getLogger().log(editor, LoggerAdapter.FATAL, e);
		}
		return null;
	}

	@Override
	public ArrayList<NCIThumbnailPanel> createThumnailList(
			SymbolTypeBean typeBean, int type) {
		ArrayList<NCIThumbnailPanel> thumbnailArray = new ArrayList<NCIThumbnailPanel>();
		try {
			Map<String, NCIEquipSymbolBean> typeSymbols = editor
					.getSymbolManager().getAllSymbols().get(typeBean);
			Iterator<NCIEquipSymbolBean> it = typeSymbols.values().iterator();
			while (it.hasNext()) {
				NCIEquipSymbolBean symbolBean = it.next();
				NCIThumbnailPanel t = new NCIThumbnailPanel(type, editor);
				t.setText(symbolBean.getName());
				t.setSymbolBean(symbolsMap.get(typeBean).get(t.getText()));
				t.setDocument(Utilities.getSVGDocumentByContent(t
						.getSymbolBean().getContent(), "file://"
						+ t.getSymbolBean().getName()));
				t.getSvgCanvas()
						.setSize(NCIThumbnailPanel.outlookPrefferedSize);
				thumbnailArray.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thumbnailArray;
	}
	
	
}
