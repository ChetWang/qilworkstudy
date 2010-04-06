package com.nci.ums.periphery.parser;


import java.io.InputStream;
import java.util.*;
import com.nci.ums.util.Res;
import com.nci.ums.util.DynamicUMSStreamReader;


public class MobileCheck {

    private static Properties mobile;
    
    static{
    	if (mobile == null) {
            mobile = new Properties();
            try {
                InputStream ins = new DynamicUMSStreamReader().getInputStreamFromFile("/resources/Mobileserver.properties");
//            	InputStream ins = new FileInputStream(new File("Mobileserver.properties"));
//            	InputStream ins = ClassLoader.getSystemResourceAsStream("Mobileserver.properties");
                mobile.load(ins);
                Res.log(Res.INFO, "MOBILE_SERVER配置初始化完成");
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
                Res.log(Res.ERROR, "MOBILE_SERVER配置文件读取错误！" + ex);
                Res.logExceptionTrace(ex);
            }
        }
    }

    public static Map getChinaMobileMap() {        
        return mobile;
    }
    
    public static void main(String args[]){
    	Map p = MobileCheck.getChinaMobileMap();
    	Iterator it = p.keySet().iterator();
    	while(it.hasNext()){
    		Object key = it.next();
    		Object object = p.get(key);
    		System.out.println("KEY:"+key+", OBJECT:"+object);
    	}
    	
    }
    
}