package com.nci.svg.graphunit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.w3c.dom.Document;

import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.util.CharacterSetToolkit;
import com.nci.svg.util.Constants;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.ServletActionConstants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;

/**
 * 单个图元以单个xml（.svg）形式存放时的图元管理器
 * 
 * @author Qil.Wong
 * 
 */
public class NCIMultiXMLGraphUnitManager extends AbstractNCIGraphUnitManager {

    /**
     * 获取设备信息的url
     */
    private String url_getEquip = new StringBuffer((String)editor.getGCParam("appRoot")).append(
            (String)editor.getGCParam("servletPath")).append("?action=").append(ServletActionConstants.GET_EQUIPMENT).toString();
    /**
     * 获取符号信息的url
     */
    private String url_getSymbol = new StringBuffer((String)editor.getGCParam("appRoot")).append(
            (String)editor.getGCParam("servletPath")).append(
            "?action=").append(ServletActionConstants.GET_SYMBOLS_BY_EQUIPMENT).toString();
    /**
     * 获取服务器文件版本信息的url
     */
    private String url_getServerModVersion = new StringBuffer((String)editor.getGCParam("appRoot")).append((String)editor.getGCParam("servletPath")).append("?action=").append(ServletActionConstants.GET_SERVER_MODVERSION).toString();

    public NCIMultiXMLGraphUnitManager(Editor editor){
    	super(editor);
    }
    
    @Override
    public ArrayList<NCIGraphUnitTypeBean> getEquipSymbolTypeList() throws IOException{

    	ArrayList<NCIGraphUnitTypeBean> equipArray =new ArrayList<NCIGraphUnitTypeBean>();

        try {
            initEquipAndSymbols();
            File[] equips = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR).listFiles();
            for (File equip : equips) {
                equipArray.add(new NCIGraphUnitTypeBean(equip.getName(), null));
            }
        } catch (IOException e) {
        	
        	throw e;
            
        }
        return equipArray;
    }

    @Override
    public ArrayList<NCIThumbnailPanel> getThumnailList(
            NCIGraphUnitTypeBean bean, int type) {
        ArrayList<NCIThumbnailPanel> thumbnailArray = new ArrayList<NCIThumbnailPanel>();
        try {
            File dir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + bean.getGraphUnitType());
            File[] symbols = dir.listFiles();
            NCIThumbnailPanel t = null;
            for (int i = 0; i < symbols.length; i++) {
                if (!symbols[i].getName().endsWith(
                        Constants.NCI_SVG_MOD_FILE_EXTENSION)) {
                    t = new NCIThumbnailPanel(type,editor);
                    // 名称要将后缀去除
                    t.setText(symbols[i].getName().substring(
                            0,
                            symbols[i].getName().length() - Constants.NCI_SVG_EXTENDSION.length()));
                    t.setDocument(Utilities.getSVGDocument(symbols[i].toURI().toASCIIString()));
//                     t.setURI(symbols[i].toURI().toASCIIString());
                    t.getSvgCanvas().setSize(NCIThumbnailPanel.outlookPrefferedSize);
                    thumbnailArray.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbnailArray;
    }

    /**
     * 初始化图元，本地的设备和图元必须与服务器上的图元一致
     * 
     * @throws IOException
     */
    public void initEquipAndSymbols() throws IOException {
        File symbolCacheDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR);
        if (symbolCacheDir.exists()) {// 缓存已经存在

            System.out.println("更新缓存：" + symbolCacheDir.getAbsolutePath());
            String[] equipments_server = this.getServerEquipments();
            String[] equipments_local = this.getLocalEquipments();
            this.handleEquipment(equipments_server, equipments_local);// 处理设备信息

            HashMap<String, Properties> serverModMap = this.getServerModVersion();
            HashMap<String, Properties> localModMap = this.getLocalModVersion(equipments_local);
            this.handleSymbols(serverModMap, localModMap);// 处理图元信息

        } else {// 第一次初始化

            symbolCacheDir.mkdirs();
            System.out.println("创建缓存：" + symbolCacheDir.getAbsolutePath());
            loadAllSymbols();
        }
    }

    /**
     * 处理图元信息.本地图元和服务器图元可能存在不一致，这里就是要将在不一致的情况下以服务器为准同步双方的内容。
     * 
     * @param serverModMap
     *            服务器端的图元版本信息
     * @param localModMap
     *            本地的图元版本信息
     */
    private void handleSymbols(HashMap<String, Properties> serverModMap,
            HashMap<String, Properties> localModMap) {
        Iterator<String> it_server = serverModMap.keySet().iterator();
        // Iterator<String> it_local = localModMap.keySet().iterator();
        // 找出哪些是已经在服务器上更新的
        while (it_server.hasNext()) {
            boolean changed = false;
            String equipName = it_server.next();
            Properties p_server = serverModMap.get(equipName);
            Properties p_local = localModMap.get(equipName);
            // 这里不用考虑设备是否与服务器端的一致性，之前的处理已经让设备在本地和服务器端同步
            Iterator it_p_server = p_server.keySet().iterator();
            while (it_p_server.hasNext()) {
                String symbolName_server = (String) it_p_server.next();
                String serial_server = p_server.getProperty(symbolName_server);
                if (p_local.containsKey(symbolName_server)) {
                    String serial_local = p_local.getProperty(symbolName_server);
                    if (!serial_local.equals(serial_server)) {// 如果本地serial和服务端serial不一致，则说明服务端已被修改，需更新

                        changed = true;
                        copyOneFile(equipName, symbolName_server, true);
                    }
                } else {// 本地版本中没有当前的图元，则表示服务端有新的图元产生，需下载

                    changed = true;
                    copyOneFile(equipName, symbolName_server, true);
                }
            }
            Iterator it_p_local = p_local.keySet().iterator();
            while (it_p_local.hasNext()) {
                String symbolName_local = (String) it_p_local.next();
                if (!p_server.containsKey(symbolName_local)) {// 服务端已经没有该图元,本地缓存需删除

                    changed = true;
                    deleteLocalSymbol(equipName, symbolName_local);
                }
            }
            if (changed) {// 如果当前设备下的内容有修改动作，则需修改本地mod文件，与服务端一致

                copyOneFile(equipName, equipName, false);
            }
        }
    }

    /**
     * 删除本地的图元
     * 
     * @param equipName
     * @param symbolName_local
     */
    private void deleteLocalSymbol(String equipName, String symbolName_local) {
        File localSymbol = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipName + "/" + symbolName_local);
        this.deleteFile(localSymbol);
    }

    /**
     * 
     * @param equipName
     * @param symbolName_server
     * @param isSvg
     *            true是svg文件，false是mod文件
     */
    private void copyOneFile(String equipName, String childFile_server,
            boolean isSvg) {
//        childFile_server = CharacterSetToolkit.UnicodeToChinese(childFile_server);
    	String[][] param = new String[2][2];
    	param[0][0] = "equipID";
    	param[0][1] = equipName;
    	param[1][0] = "fileName";
    	param[1][1] = childFile_server;
        StringBuffer url_getOneFile = Utilities.parseURL(new StringBuffer((String)editor.getGCParam("appRoot")).append((String)editor.getGCParam("servletPath")).append(
                "?action=").append(ServletActionConstants.COPY_ONE_FILE).toString(), param);
        
        String url = null;
        StringBuffer filePath = new StringBuffer().append(
                Constants.NCI_SVG_SYMBOL_CACHE_DIR).append(equipName).append(
                "/").append(childFile_server);

        if (!isSvg) {// 如果不是svg图元文件（版本mod文件），由于childFile_server没有后缀名，则在这种情况下需添加后缀

            url = url_getOneFile.append(Constants.NCI_SVG_MOD_FILE_EXTENSION).toString();
            filePath.append(Constants.NCI_SVG_MOD_FILE_EXTENSION);
        }else{
            url = url_getOneFile.toString();
        }
        try {
            URL u = new URL(url);
            ObjectInputStream ois = new ObjectInputStream(u.openStream());
            String content = (String) ois.readObject();
            if (!isSvg) {//不是svg文件就直接写入

                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        filePath.toString(), false));
                writer.write(content);
                writer.close();
            } else {//是svg文件就要通过XMLPrinter写文件

                Document doc = Utilities.getXMLDocumentByString(content);
                XMLPrinter.printXML(doc, new File(filePath.toString()), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取server端的图元版本信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Properties> getServerModVersion() {
        HashMap<String, Properties> serverModVersionMap = null;
        try {
            URL url = new URL(url_getServerModVersion);
            URLConnection rConn = url.openConnection();
            rConn.setConnectTimeout(Constants.SERVLET_TIME_OUT);
            InputStream in = rConn.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            serverModVersionMap = (HashMap<String, Properties>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverModVersionMap;
    }

    /**
     * 获取本地图元版本信息
     * 
     * @param equipments_local
     * @return
     */
    private HashMap<String, Properties> getLocalModVersion(
            String[] equipments_local) {
        HashMap<String, Properties> localModVersionMap = new HashMap<String, Properties>();
        File modFile = null;
        for (String equip_local : equipments_local) {
            modFile = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equip_local + "/" + equip_local + Constants.NCI_SVG_MOD_FILE_EXTENSION);
            String s = readFile(modFile);
            // String unicode_s = CharacterSetToolkit.toUnicode(s, false);
            Properties p = new Properties();
            if (modFile.exists()) {
                try {
                    // p.load(new ByteArrayInputStream(unicode_s.getBytes()));
                    p.load(new ByteArrayInputStream(s.getBytes()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            localModVersionMap.put(equip_local, p);
        }
        return localModVersionMap;
    }

    /**
     * 处理设备类型的信息，包括是否有新增的设备（需要新下载）、是否某些设备已经被删除（需要从本地删除）， 这样才能保证本地缓存和服务器上同步。
     * 
     * @param equipments_server
     *            服务器上的设备集合
     * @param equipments_local
     *            本地的设备集合
     * @throws IOException
     */
    private void handleEquipment(String[] equipments_server,
            String[] equipments_local) throws IOException {
        // 判断哪些设备是新增的
        for (String equipName_server : equipments_server) {

            boolean flag_add = true;
            for (String equipName_local : equipments_local) {
                if (equipName_server.equals(equipName_local)) {
                    flag_add = false;
                    break;
                }
            }
            if (flag_add) {// 如果是新增的设备，则下载到本地

                File newEquip = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipName_server);
                newEquip.mkdir();
                this.copySymbolsFromEquipment(equipName_server);
            }
        }
        // 判断哪些设备已经从服务器中删除
        for (String equipName_local : equipments_local) {
            boolean flag_delete = true;
            for (String equipName_server : equipments_server) {
                if (equipName_local.equals(equipName_server)) {
                    flag_delete = false;
                    break;
                }
            }
            if (flag_delete) {// 如果服务器上已经没有该设备，则从本地删除

                this.deleteFile(new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipName_local));
            }
        }

    }

    /**
     * 获取服务器上的设备名称集合
     * 
     * @return 设备名称集合
     * @throws IOException
     */
    private String[] getLocalEquipments() {
        String[] equipments = null;
        File symbolsFolder = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR);
        File[] equipFolder = symbolsFolder.listFiles();
        equipments = new String[equipFolder.length];
        for (int i = 0; i < equipFolder.length; i++) {
            equipments[i] = equipFolder[i].getName();
        }
        return equipments;

    }

    /**
     * 获取服务器上的设备名称集合
     * 
     * @return 设备名称集合
     * @throws IOException
     */
    private String[] getServerEquipments() throws IOException {
        URL u = new URL(url_getEquip);
        URLConnection rConn = u.openConnection();
        rConn.setConnectTimeout(Constants.SERVLET_TIME_OUT);
        InputStream in = rConn.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(in);
        String[] equipments = null;
        try {
            equipments = (String[]) ois.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ois.close();
        return equipments;
    }

    /**
     * 一次加载所有的图元
     * 
     * @throws IOException
     */
    private void loadAllSymbols() throws IOException {
        String[] equipments = getServerEquipments();
        for (String equipmentName : equipments) {
            File equipDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + "/" + equipmentName);
            equipDir.mkdir();
            copySymbolsFromEquipment(equipmentName);
        }
    }

    /**
     * 将指定设备下的图元copy到缓存路径
     * 
     * @param equipName
     *            指定的设备名称
     * @throws IOException
     */
    private void copySymbolsFromEquipment(String equipName) throws IOException {
    	String[][] param = new String[1][2];
    	param[0][0] = "equipID";
    	param[0][1] = equipName;
        String[][] content = null;
        try {
        	// 第一个是文件名，第二个是文件内容
        	content = (String[][])Utilities.communicateWithURL(url_getSymbol, param);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String svgName = null;
        String svgContent = null;
        File svgFile = null;
        BufferedWriter writer = null;
        for (int i = 0; i < content.length; i++) {
            svgName = content[i][0];
            svgContent = content[i][1];
            svgFile = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipName + "/" + svgName);
            if (svgName.endsWith(Constants.NCI_SVG_EXTENDSION)) {
                Document doc = Utilities.getXMLDocumentByString(svgContent);
                XMLPrinter.printXML(doc, svgFile, null);
            } else {
                svgFile.createNewFile();
                writer = new BufferedWriter(new FileWriter(svgFile.getAbsolutePath(), false));
                writer.write(svgContent);
                writer.close();
            }
        }
    }

    /**
     * 删除指定文件或文件夹
     * 
     * @param f
     * @return
     */
    private boolean deleteFile(File f) {
        boolean flag = false;
        if (f.isDirectory()) {
            File[] children = f.listFiles();
            for (File child : children) {
                deleteFile(child);
            }
        }
        flag = f.delete();
        return flag;
    }

    private String readFile(File f) {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(CharacterSetToolkit.toUnicode(s, false)).append(
                        "\r\n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

}
