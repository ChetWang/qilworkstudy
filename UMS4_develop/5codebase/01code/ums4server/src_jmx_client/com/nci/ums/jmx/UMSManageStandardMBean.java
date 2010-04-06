package com.nci.ums.jmx;

import java.util.List;
import java.util.Properties;

import com.nci.ums.media.MediaBean;

public interface UMSManageStandardMBean {

	public boolean startUMS();

	public boolean stopUMS();

	public void startMedia(MediaBean media);

	public void stopMedia(MediaBean media);

	public Properties getProperties(String resourceName);

	public void saveProperties(String resourceName, Properties p);

	public String getXMLDocument(String xmlName);

	public void setXMLDocument(String xmlName, String doc);
	
	public boolean isUMSStarted();
	
	public List getMediaList();

}
