/**
 * 类名：com.nci.svg.other
 * 创建人:yx.nci
 * 创建日期：2008-6-20
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.other;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author yx.nci
 *
 */
public class MongLineData {
    private String strLineID,strLineName;
    
    private String bStationID,bStationName;
    private String eStationID,eStationName;
    
    private Set<TowerData> towerSets = new HashSet<TowerData>();
    
    public MongLineData()
    {
        
    }
    
    public boolean readData(String strFileName)
    {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(strFileName));
            
            Element eRoot = doc.getDocumentElement();
            Element desc = (Element)eRoot.getElementsByTagName("desc").item(0);
            if(desc == null)
                return false;
            
            setStrLineID(desc.getAttribute("lineid"));
            setStrLineName(desc.getAttribute("linename"));
            if(getStrLineID() == null || getStrLineName() == null)
            {
                return false;
            }
            Element beginstation = (Element)eRoot.getElementsByTagName("begin").item(0);
            if(beginstation == null)
                return false;
            
            setBStationID(beginstation.getAttribute("stationid"));
            setBStationName(beginstation.getAttribute("stationname"));
            
            Element endstation = (Element)eRoot.getElementsByTagName("end").item(0);
            if(endstation == null)
                return false;
            
            setEStationID(endstation.getAttribute("stationid"));
            setEStationName(endstation.getAttribute("stationname"));
            
            NodeList towers = eRoot.getElementsByTagName("tower");
            for(int i = 0; i < towers.getLength();i++)
            {
                Element element = (Element)towers.item(i);
                TowerData data = new TowerData();
                data.setStrTowerID(element.getAttribute("towerid"));
                data.setStrTowerName(element.getAttribute("towername"));
                data.setStrTowerType(element.getAttribute("towertype"));
                data.setNTowerOrder(new Integer(element.getAttribute("towerorder")).intValue());
                towerSets.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
    
    /**
     * @return the strLineID
     */
    public String getStrLineID() {
        return strLineID;
    }

    /**
     * @param strLineID the strLineID to set
     */
    public void setStrLineID(String strLineID) {
        this.strLineID = strLineID;
    }

    /**
     * @return the strLineName
     */
    public String getStrLineName() {
        return strLineName;
    }

    /**
     * @param strLineName the strLineName to set
     */
    public void setStrLineName(String strLineName) {
        this.strLineName = strLineName;
    }

    /**
     * @return the bStationID
     */
    public String getBStationID() {
        return bStationID;
    }

    /**
     * @param stationID the bStationID to set
     */
    public void setBStationID(String stationID) {
        bStationID = stationID;
    }

    /**
     * @return the bStationName
     */
    public String getBStationName() {
        return bStationName;
    }

    /**
     * @param stationName the bStationName to set
     */
    public void setBStationName(String stationName) {
        bStationName = stationName;
    }

    /**
     * @return the eStationID
     */
    public String getEStationID() {
        return eStationID;
    }

    /**
     * @param stationID the eStationID to set
     */
    public void setEStationID(String stationID) {
        eStationID = stationID;
    }

    /**
     * @return the eStationName
     */
    public String getEStationName() {
        return eStationName;
    }

    /**
     * @param stationName the eStationName to set
     */
    public void setEStationName(String stationName) {
        eStationName = stationName;
    }

    
    
    public class TowerData
    {
        private String strTowerID,strTowerName,strTowerType;
        int nTowerOrder;
        /**
         * @return the strTowerID
         */
        public String getStrTowerID() {
            return strTowerID;
        }
        /**
         * @param strTowerID the strTowerID to set
         */
        public void setStrTowerID(String strTowerID) {
            this.strTowerID = strTowerID;
        }
        /**
         * @return the strTowerName
         */
        public String getStrTowerName() {
            return strTowerName;
        }
        /**
         * @param strTowerName the strTowerName to set
         */
        public void setStrTowerName(String strTowerName) {
            this.strTowerName = strTowerName;
        }
        /**
         * @return the strTowerType
         */
        public String getStrTowerType() {
            return strTowerType;
        }
        /**
         * @param strTowerType the strTowerType to set
         */
        public void setStrTowerType(String strTowerType) {
            this.strTowerType = strTowerType;
        }
        /**
         * @return the nTowerOrder
         */
        public int getNTowerOrder() {
            return nTowerOrder;
        }
        /**
         * @param towerOrder the nTowerOrder to set
         */
        public void setNTowerOrder(int towerOrder) {
            nTowerOrder = towerOrder;
        }
    }
    
    public class LateralData
    {
        private String strLID,strLName,strPLID,strBeginTowerID;
        private int nTowerCount;
        /**
         * @return the strLID
         */
        public String getStrLID() {
            return strLID;
        }
        /**
         * @param strLID the strLID to set
         */
        public void setStrLID(String strLID) {
            this.strLID = strLID;
        }
        /**
         * @return the strLName
         */
        public String getStrLName() {
            return strLName;
        }
        /**
         * @param strLName the strLName to set
         */
        public void setStrLName(String strLName) {
            this.strLName = strLName;
        }
        /**
         * @return the strPLID
         */
        public String getStrPLID() {
            return strPLID;
        }
        /**
         * @param strPLID the strPLID to set
         */
        public void setStrPLID(String strPLID) {
            this.strPLID = strPLID;
        }
        /**
         * @return the strBeginTowerID
         */
        public String getStrBeginTowerID() {
            return strBeginTowerID;
        }
        /**
         * @param strBeginTowerID the strBeginTowerID to set
         */
        public void setStrBeginTowerID(String strBeginTowerID) {
            this.strBeginTowerID = strBeginTowerID;
        }
        /**
         * @return the nTowerCount
         */
        public int getNTowerCount() {
            return nTowerCount;
        }
        /**
         * @param towerCount the nTowerCount to set
         */
        public void setNTowerCount(int towerCount) {
            nTowerCount = towerCount;
        }
    }

}
