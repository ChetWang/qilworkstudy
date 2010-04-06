package com.nci.ums.jmx.desktop.panels;

import java.io.IOException;

public interface Setting {
    /**
     * 应用设置
     */
    public void implSetting() throws IOException;
    /**
     * 初始化设置信息
     */
    public void initSetting() throws IOException;

}
