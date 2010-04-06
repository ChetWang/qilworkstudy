package com.nci.svg.client.app;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.graphunit.OwnerVersionBean;

public class SymbolLocalViewer {
	
	private static String releasedSymbolPath = Constants.NCI_SVG_CACHE_DIR
	+ OwnerVersionBean.OWNER_RELEASED + "_symbol.nci";
	
	private static String personalSymbolPath = Constants.NCI_SVG_CACHE_DIR
	 + System.getProperty("user.name")+"_symbol.nci";

	public static void main(String[] xxx) throws Exception{
		FileInputStream fi = new FileInputStream(personalSymbolPath);
		ObjectInputStream ois = new ObjectInputStream(fi);
		System.out.println("personal: "+ois.readObject());
		FileInputStream fi2 = new FileInputStream(releasedSymbolPath);
		ObjectInputStream ois2 = new ObjectInputStream(fi2);
		
		System.out.println("released: "+ois2.readObject());
	}
}
