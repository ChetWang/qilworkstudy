package com.nci.svg.sdk.ui.clipboard;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;

import javax.swing.ImageIcon;

/**
 * 图形剪贴板对象
 * @author Qil.Wong
 *
 */
public class ImageTransferable extends ImageIcon implements Transferable,
		ClipboardOwner {

	private static final long serialVersionUID = 8444892514777448106L;

	private DataFlavor flavor;

//	private File imageFile;
	
	private Image image;

	/**
	 * 失去当前剪贴对象时的响应
	 */
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
				
	}

	/**
	 * 构造函数，创建剪贴对象
	 * @param imageFile
	 */
	public ImageTransferable(File imageFile) {
		this.image = new ImageIcon(imageFile.getPath()).getImage();
		flavor = DataFlavor.imageFlavor;
	}
	
	public ImageTransferable(Image image) {
		this.image = image;
		flavor = DataFlavor.imageFlavor;
	}

	public DataFlavor getTransferDataFlavor() {
		return flavor;
	}

	public Object getTransferData(DataFlavor flavor) {
		return image;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(this.flavor))
			return true;
		return false;
	}

	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] dfs = new DataFlavor[1];
		dfs[0] = flavor;
		return dfs;
	}
}
