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
 * ����ͼԪ�Ե���xml��.svg����ʽ���ʱ��ͼԪ������
 * 
 * @author Qil.Wong
 * 
 */
public class NCIMultiXMLGraphUnitManager extends AbstractNCIGraphUnitManager {

    /**
     * ��ȡ�豸��Ϣ��url
     */
    private String url_getEquip = new StringBuffer((String)editor.getGCParam("appRoot")).append(
            (String)editor.getGCParam("servletPath")).append("?action=").append(ServletActionConstants.GET_EQUIPMENT).toString();
    /**
     * ��ȡ������Ϣ��url
     */
    private String url_getSymbol = new StringBuffer((String)editor.getGCParam("appRoot")).append(
            (String)editor.getGCParam("servletPath")).append(
            "?action=").append(ServletActionConstants.GET_SYMBOLS_BY_EQUIPMENT).toString();
    /**
     * ��ȡ�������ļ��汾��Ϣ��url
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
                    // ����Ҫ����׺ȥ��
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
     * ��ʼ��ͼԪ�����ص��豸��ͼԪ������������ϵ�ͼԪһ��
     * 
     * @throws IOException
     */
    public void initEquipAndSymbols() throws IOException {
        File symbolCacheDir = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR);
        if (symbolCacheDir.exists()) {// �����Ѿ�����

            System.out.println("���»��棺" + symbolCacheDir.getAbsolutePath());
            String[] equipments_server = this.getServerEquipments();
            String[] equipments_local = this.getLocalEquipments();
            this.handleEquipment(equipments_server, equipments_local);// �����豸��Ϣ

            HashMap<String, Properties> serverModMap = this.getServerModVersion();
            HashMap<String, Properties> localModMap = this.getLocalModVersion(equipments_local);
            this.handleSymbols(serverModMap, localModMap);// ����ͼԪ��Ϣ

        } else {// ��һ�γ�ʼ��

            symbolCacheDir.mkdirs();
            System.out.println("�������棺" + symbolCacheDir.getAbsolutePath());
            loadAllSymbols();
        }
    }

    /**
     * ����ͼԪ��Ϣ.����ͼԪ�ͷ�����ͼԪ���ܴ��ڲ�һ�£��������Ҫ���ڲ�һ�µ�������Է�����Ϊ׼ͬ��˫�������ݡ�
     * 
     * @param serverModMap
     *            �������˵�ͼԪ�汾��Ϣ
     * @param localModMap
     *            ���ص�ͼԪ�汾��Ϣ
     */
    private void handleSymbols(HashMap<String, Properties> serverModMap,
            HashMap<String, Properties> localModMap) {
        Iterator<String> it_server = serverModMap.keySet().iterator();
        // Iterator<String> it_local = localModMap.keySet().iterator();
        // �ҳ���Щ���Ѿ��ڷ������ϸ��µ�
        while (it_server.hasNext()) {
            boolean changed = false;
            String equipName = it_server.next();
            Properties p_server = serverModMap.get(equipName);
            Properties p_local = localModMap.get(equipName);
            // ���ﲻ�ÿ����豸�Ƿ���������˵�һ���ԣ�֮ǰ�Ĵ����Ѿ����豸�ڱ��غͷ�������ͬ��
            Iterator it_p_server = p_server.keySet().iterator();
            while (it_p_server.hasNext()) {
                String symbolName_server = (String) it_p_server.next();
                String serial_server = p_server.getProperty(symbolName_server);
                if (p_local.containsKey(symbolName_server)) {
                    String serial_local = p_local.getProperty(symbolName_server);
                    if (!serial_local.equals(serial_server)) {// �������serial�ͷ����serial��һ�£���˵��������ѱ��޸ģ������

                        changed = true;
                        copyOneFile(equipName, symbolName_server, true);
                    }
                } else {// ���ذ汾��û�е�ǰ��ͼԪ�����ʾ��������µ�ͼԪ������������

                    changed = true;
                    copyOneFile(equipName, symbolName_server, true);
                }
            }
            Iterator it_p_local = p_local.keySet().iterator();
            while (it_p_local.hasNext()) {
                String symbolName_local = (String) it_p_local.next();
                if (!p_server.containsKey(symbolName_local)) {// ������Ѿ�û�и�ͼԪ,���ػ�����ɾ��

                    changed = true;
                    deleteLocalSymbol(equipName, symbolName_local);
                }
            }
            if (changed) {// �����ǰ�豸�µ��������޸Ķ����������޸ı���mod�ļ���������һ��

                copyOneFile(equipName, equipName, false);
            }
        }
    }

    /**
     * ɾ�����ص�ͼԪ
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
     *            true��svg�ļ���false��mod�ļ�
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

        if (!isSvg) {// �������svgͼԪ�ļ����汾mod�ļ���������childFile_serverû�к�׺���������������������Ӻ�׺

            url = url_getOneFile.append(Constants.NCI_SVG_MOD_FILE_EXTENSION).toString();
            filePath.append(Constants.NCI_SVG_MOD_FILE_EXTENSION);
        }else{
            url = url_getOneFile.toString();
        }
        try {
            URL u = new URL(url);
            ObjectInputStream ois = new ObjectInputStream(u.openStream());
            String content = (String) ois.readObject();
            if (!isSvg) {//����svg�ļ���ֱ��д��

                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        filePath.toString(), false));
                writer.write(content);
                writer.close();
            } else {//��svg�ļ���Ҫͨ��XMLPrinterд�ļ�

                Document doc = Utilities.getXMLDocumentByString(content);
                XMLPrinter.printXML(doc, new File(filePath.toString()), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡserver�˵�ͼԪ�汾��Ϣ
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
     * ��ȡ����ͼԪ�汾��Ϣ
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
     * �����豸���͵���Ϣ�������Ƿ����������豸����Ҫ�����أ����Ƿ�ĳЩ�豸�Ѿ���ɾ������Ҫ�ӱ���ɾ������ �������ܱ�֤���ػ���ͷ�������ͬ����
     * 
     * @param equipments_server
     *            �������ϵ��豸����
     * @param equipments_local
     *            ���ص��豸����
     * @throws IOException
     */
    private void handleEquipment(String[] equipments_server,
            String[] equipments_local) throws IOException {
        // �ж���Щ�豸��������
        for (String equipName_server : equipments_server) {

            boolean flag_add = true;
            for (String equipName_local : equipments_local) {
                if (equipName_server.equals(equipName_local)) {
                    flag_add = false;
                    break;
                }
            }
            if (flag_add) {// ������������豸�������ص�����

                File newEquip = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipName_server);
                newEquip.mkdir();
                this.copySymbolsFromEquipment(equipName_server);
            }
        }
        // �ж���Щ�豸�Ѿ��ӷ�������ɾ��
        for (String equipName_local : equipments_local) {
            boolean flag_delete = true;
            for (String equipName_server : equipments_server) {
                if (equipName_local.equals(equipName_server)) {
                    flag_delete = false;
                    break;
                }
            }
            if (flag_delete) {// ������������Ѿ�û�и��豸����ӱ���ɾ��

                this.deleteFile(new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + equipName_local));
            }
        }

    }

    /**
     * ��ȡ�������ϵ��豸���Ƽ���
     * 
     * @return �豸���Ƽ���
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
     * ��ȡ�������ϵ��豸���Ƽ���
     * 
     * @return �豸���Ƽ���
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
     * һ�μ������е�ͼԪ
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
     * ��ָ���豸�µ�ͼԪcopy������·��
     * 
     * @param equipName
     *            ָ�����豸����
     * @throws IOException
     */
    private void copySymbolsFromEquipment(String equipName) throws IOException {
    	String[][] param = new String[1][2];
    	param[0][0] = "equipID";
    	param[0][1] = equipName;
        String[][] content = null;
        try {
        	// ��һ�����ļ������ڶ������ļ�����
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
     * ɾ��ָ���ļ����ļ���
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
