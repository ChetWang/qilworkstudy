package com.nci.svg.util.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 第一次初始化服务器上的所有版本信息
 * @author Qil.Wong
 *
 */
public class VersionInitializer {
	
	String equipmentsPath ="D:\\nci\\cserv\\data\\convertedSymbols\\";
	
	public void startInitializer(){
		File folder = new File(equipmentsPath);
		File[] equips = folder.listFiles();
		for(File singleEquipFolder:equips){
			if(singleEquipFolder.isDirectory()){
				File[] symbols = singleEquipFolder.listFiles();
				File modFile = createModFile(singleEquipFolder);
				writeVersionInfo(modFile,symbols);
			}
		}
	}
	
	private File createModFile(File singleEquipFolder){
		String path = singleEquipFolder.getAbsolutePath();
		String modPath= path+"/"+singleEquipFolder.getName()+".ncimod";
		File modFile = new File(modPath);
		try {
			modFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modFile;		
	}
	
	private void writeVersionInfo(File modFile,File[] symbols){
//		String serialNum = "20080421144322";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(modFile.getAbsolutePath(),false));
			for(File symbolFile:symbols){
				writer.write(symbolFile.getName()+"=20080421144322\r\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		VersionInitializer vi = new VersionInitializer();
		vi.startInitializer();
	}

}
