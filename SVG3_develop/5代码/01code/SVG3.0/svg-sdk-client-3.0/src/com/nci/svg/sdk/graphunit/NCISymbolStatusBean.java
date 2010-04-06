/**
 * 类名：com.nci.svg.graphunit.NCISymbolStatusBean
 * 创建人:yx.nci
 * 创建日期：2008-7-7
 * 类作用:图元状态结构包
 * 修改日志：
 */
package com.nci.svg.sdk.graphunit;

import java.io.Serializable;
import java.util.ArrayList;

public class NCISymbolStatusBean implements Serializable {
    private String[] strStatusSymbol;
    /**
     * @return the nStatusFlag
     */
    public String getStatusSymbol(int nPos) {
        return strStatusSymbol[nPos];
    }
    /**
     * @param statusFlag the nStatusFlag to set
     */
    public void setStatusSymbol(int nPos,String statusSymbol) {
        strStatusSymbol[nPos] = statusSymbol;
    }
    
    /**
     * @return the nStatusFlag
     */
    public String getStatusSymbol(ArrayList<String> symbolsStatus,String strName) {
        for(int i = 0;i < symbolsStatus.size();i++)
        {
            if(symbolsStatus.get(i).equals(strName))
                return strStatusSymbol[i];
        }
        return null;
    }
    /**
     * @param statusFlag the nStatusFlag to set
     */
    public void setStatusSymbol(ArrayList<String> symbolsStatus,String strName,String statusSymbol) {
        for(int i = 0;i < symbolsStatus.size();i++)
        {
            if(symbolsStatus.get(i).equals(strName))
                strStatusSymbol[i] = statusSymbol;
        }
        return;
    }
    
    public NCISymbolStatusBean(ArrayList<String> symbolsStatus)
    {
        strStatusSymbol = new String[symbolsStatus.size()];
        for(int i = 0;i < symbolsStatus.size();i++)
        {
            strStatusSymbol[i] = "";
        }
    }
    
}
