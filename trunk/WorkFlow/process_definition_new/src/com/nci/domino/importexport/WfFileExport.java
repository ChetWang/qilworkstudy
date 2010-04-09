package com.nci.domino.importexport;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.nci.domino.WfEditor;

public abstract class WfFileExport extends FileFilter{
	
	/**
	 * 过滤器描述
	 */	
	protected String description = "";

	/**
	 * 接受的后缀
	 */
	protected String acceptExtension;
	
	protected WfEditor editor;
	
	public WfFileExport(WfEditor editor, String acceptExtention, String description){
		super();
		this.editor = editor;
		this.description = description;
		this.acceptExtension = acceptExtention;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	public String getAcceptExtension(){
		return acceptExtension;
	}
	
	@Override
	public boolean accept(File f) {
		String name = f.getName().toLowerCase();
		String ext = "";
		if (name.indexOf(".") >= 0) {
			ext = name.substring(name.lastIndexOf("."));
		}
		if (f.isDirectory()) {
			return true;
		} else if (acceptExtension.equals(ext)) {
			return true;
		}else if(acceptExtension.equals(".jpg")) {
			return ".jpeg".equals(ext);
		}
		else {
			return false;
		}
	}
	
	/**
	 * 导出图形
	 * @param fileName
	 */
	public abstract void export(String fileName);

}
