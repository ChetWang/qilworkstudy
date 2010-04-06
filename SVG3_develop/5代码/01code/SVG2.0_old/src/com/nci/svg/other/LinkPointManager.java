/**
 * 类名：com.nci.svg.other.LinkPointManager
 * 创建人:yx
 * 创建日期：2008-5-6
 * 类作用:保存所有的连接点信息
 * 修改日志：
 */
package com.nci.svg.other;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Element;

/**
 * @author yx
 *
 */
public class LinkPointManager {
    protected HashMap<String,Set<LineData>> mapLine = new HashMap<String,Set<LineData>>();
    public void testPrint()
    {
        Iterator<String> iterators = mapLine.keySet().iterator();
        while(iterators.hasNext())
        {
            String strKey = iterators.next();
            Set<LineData> sets = mapLine.get(strKey);
            System.out.println(strKey+":");
            Iterator iterator = sets.iterator();
            while(iterator.hasNext())
            {
                LineData data = (LineData)iterator.next();
                System.out.println(data.getStrLineID());
            }
        }
        System.out.println("size:"+isize);
    }
    int isize = 0;
    public void addLinkPoint(String strID,String strLineID,Element element,
            String strPointID,double dScaleX,double dScaleY,boolean useFlag,
            double dsw,double dsh)
    {
        Set<LineData> sets = mapLine.get(strID);
        if(sets == null)
        {
            sets = new HashSet<LineData>();
        }
        LineData data = new LineData();
        data.setStrLineID(strLineID);
        data.setElement(element);
        data.setStrPointID(strPointID);
        data.setDScaleX(dScaleX);
        data.setDScaleY(dScaleY);
        data.setBGUseFlag(useFlag);
        data.setDsh(dsh);
        data.setDsw(dsw);
        sets.add(data);
        mapLine.put(strID, sets);
        isize++;
        return;
    }
    
    public void removeLinkPoint(String strID,String strLineID)
    {
        Set<LineData> sets = mapLine.get(strID);
        if(sets == null)
            return;
        
        Iterator iterator = sets.iterator();
        while(iterator.hasNext())
        {
            LineData data = (LineData)iterator.next();
            if(data.getStrLineID().equals(strLineID))
            {
                sets.remove(data);
                return;
            }
        }
        return;
    }
    
    public Set<LineData> getLineData(String strID)
    {
         return mapLine.get(strID);   
    }
    /**
     * 线连接关系数据集合
     *
     */
    public class LineData
    {
        private String strLineID;
        private Element element;
        private String strPointID;
        private double dScaleX=0;
        private double dScaleY=0;
        private boolean bGUseFlag = false;
        private double dsw=0;
        private double dsh=0;
        
        /**
         * @return the bGUseFlag
         */
        public boolean isBGUseFlag() {
            return bGUseFlag;
        }
        /**
         * @param useFlag the bGUseFlag to set
         */
        public void setBGUseFlag(boolean useFlag) {
            bGUseFlag = useFlag;
        }
        /**
         * @return the dsw
         */
        public double getDsw() {
            return dsw;
        }
        /**
         * @param dsw the dsw to set
         */
        public void setDsw(double dsw) {
            this.dsw = dsw;
        }
        /**
         * @return the dsh
         */
        public double getDsh() {
            return dsh;
        }
        /**
         * @param dsh the dsh to set
         */
        public void setDsh(double dsh) {
            this.dsh = dsh;
        }
        /**
         * @return the dScaleX
         */
        public double getDScaleX() {
            return dScaleX;
        }
        /**
         * @param scaleX the dScaleX to set
         */
        public void setDScaleX(double scaleX) {
            dScaleX = scaleX;
        }
        /**
         * @return the dScaleY
         */
        public double getDScaleY() {
            return dScaleY;
        }
        /**
         * @param scaleY the dScaleY to set
         */
        public void setDScaleY(double scaleY) {
            dScaleY = scaleY;
        }
        /**
         * @return the element
         */
        public Element getElement() {
            return element;
        }
        /**
         * @param element the element to set
         */
        public void setElement(Element element) {
            this.element = element;
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
         * @return the strPointID
         */
        public String getStrPointID() {
            return strPointID;
        }
        /**
         * @param strPointID the strPointID to set
         */
        public void setStrPointID(String strPointID) {
            this.strPointID = strPointID;
        }
        
       
    }
    
    

}
