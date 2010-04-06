package com.nci.svg.applet;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.client.Editor;
import com.nci.svg.client.module.NCIViewEditModule;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.mode.EditorModeAdapter;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.managers.export.image.SVGDocumentImageCreator;
import fr.itris.glips.svgeditor.io.managers.export.image.SVGDocumentImageCreatorListener;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;

public class AppletProxy {

	private DrawSvg applet;

	private Editor editor = null;

	public AppletProxy(DrawSvg applet) {
		this.applet = applet;
	}

	public AppletProxy() {
	}

	public void createEditor(HashMap<String, Object> map_config) {
		// creating the editor object
		editor = new Editor("1");
	}

	public void setGCParam(String strName, Object obValue) {
		editor.setGCParam(strName, obValue);
		return;
	}

	public Object getGCParam(String strName) {
		return editor.getGCParam(strName);
	}

	public void createAppletUI() {
		// intializing the editor
		editor.init(applet, "", false, false, true, false, null);
		String strParam = (String) (editor.getGCParam("showmenu"));
		if (strParam.equals("FALSE"))
			editor.getSVGModuleLoader().hideMenuBar();

		strParam = (String) (editor.getGCParam("showtoolbar"));
		if (strParam.equals("FALSE")) {
			editor.getSVGModuleLoader().hideToolBar();
		}
		strParam = (String) (editor.getGCParam("mode"));
		if (strParam.equals("1")) {
			((NCIViewEditModule) editor
					.getModule(NCIViewEditModule.NCI_View_Edit_ModuleID))
					.setViewEdit(NCIViewEditModule.VIEW_MODE);
		}
		// applet.setSize(800, 600);
		// openSVGFile("1", "巨宏厂", "KV-220");
		// openSVGFile("ZJ_电厂_紧水滩.fac.svg");
		// openSVGFile("0","嘉兴系统图dd","2");
	}

	public void destroyApplet() {
		if (editor != null) {

			editor.dispose();
		}
	}

	public String getSvgContent() {
		try {
			editor.getSettingFileLock().lock();// 如果正在打开文件，则等待同步完成后再开始
			if (applet.getInitFlag() == DrawSvg.INIT_COMPLETE) {
				Document doc = null;
				SVGHandle handle = editor.getHandlesManager()
						.getCurrentHandle();
				if (handle != null) {
					doc = handle.getCanvas().getDocument();
					// NodeList texts =
					// doc.getDocumentElement().getElementsByTagName(
					// "text");
					// for (int i = 0; i < texts.getLength(); i++) {
					// Node text = texts.item(i);
					// if (text instanceof Element) {
					// String fillValue = editor.getSVGToolkit()
					// .getStyleProperty((Element) text, "fill");
					// if (fillValue != null && !fillValue.equals("")) {
					// ((Element) text).setAttribute("fill", fillValue);
					// }
					// }
					// }
					// 将xml document数据写入一个StringBuffer对象
				}
				if (doc != null) {
					StringBuffer xmlData = new StringBuffer();
					XMLPrinter.writeNode(doc.getDocumentElement(), xmlData, 0,
							false, null);

					return xmlData.toString();
				}
			} else {
				System.out
						.println("DrawSvg is initializing..., getSVGContent failed!");
			}
		} finally {
			editor.getSettingFileLock().unlock();
		}
		return null;
	}

	public void setFileContent(final String strContent) {
		if (strContent == null)
			return;
		try {
			editor.getSettingFileLock().lock();
			if (applet.getInitFlag() == DrawSvg.INIT_COMPLETE) {
				System.out.println("setFileContent begin");
				SVGHandle handle = editor.getHandlesManager().getHandle(
						"FileContent");
				if (handle != null) {
					if (editor.getModeManager().getMode().equals(
							EditorModeAdapter.SVGTOOL_MODE_VIEW_ONLYVIEW)) {
						if (editor.getHandlesManager().getCurrentHandle() != null)
							editor.getHandlesManager().getCurrentHandle()
									.close();
					}
					handle = null;
				}

				if (handle == null) {
					System.out.println("createSVGHandle begin");
					handle = editor.getHandlesManager().createSVGHandle(
							"FileContent");
				}
				if (handle != null) {
					System.out.println("setStringToDocument begin");
					handle.getCanvas().setStringToDocument(strContent);
				}

				System.out.println("setFileContent end");
			}
		} finally {
			editor.getSettingFileLock().unlock();
		}
	}

	
	public int setSymbolTypeByID(String id, String type) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return -1;

		handle.getSelection().handleSelection(id);
		return 0;
	}

	public int setSymbolTypeByObjectID(String id, String type) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return -1;

		handle.getSelection().handleSelection(id);
		return 0;
	}

	public void setUser(String user) {
		editor.getSvgSession().setUser(user);
	}

	public void writeImage(final BufferedImage image, final File destFile) {
		try {
			// creating the IIOImage
			IIOImage iioImage = new IIOImage(image, null, null);
			Iterator<ImageWriter> it = ImageIO
					.getImageWritersByFormatName("jpeg");

			// getting the image writer
			ImageWriter w = null;

			while (it.hasNext()) {

				w = it.next();

				if (w != null
						&& w.getDefaultWriteParam() instanceof JPEGImageWriteParam) {

					break;
				}
			}

			final ImageWriter writer = w;

			// setting the parameters for the jpeg transcoding

			JPEGImageWriteParam params = new JPEGImageWriteParam(Locale
					.getDefault());

			if (editor.getSelectionManager().getSelectionMode() == SelectionInfoManager.SCREEN_CAST_MODE) {
				params.setSourceRegion(editor.getSelectionManager()
						.getScreenCastRect());
			}

			params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			params.setCompressionQuality(100);
			params.setOptimizeHuffmanTables(true);
			{

				params.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);

			}

			// writing the image
			ImageOutputStream out = ImageIO.createImageOutputStream(destFile);
			writer.setOutput(out);

			// adding the listener to the writer
			writer.addIIOWriteProgressListener(new IIOWriteProgressListener() {

				public void imageComplete(ImageWriter wr) {

					writer.removeIIOWriteProgressListener(this);
				}

				public void writeAborted(ImageWriter wr) {

					writer.removeIIOWriteProgressListener(this);
				}

				public void imageProgress(ImageWriter wr, float value) {
				}

				public void imageStarted(ImageWriter arg0, int arg1) {
				}

				public void thumbnailComplete(ImageWriter wr) {
				}

				public void thumbnailProgress(ImageWriter wr, float arg1) {
				}

				public void thumbnailStarted(ImageWriter wr, int arg1, int arg2) {
				}
			});

			writer.write(null, iioImage, params);

			// cleaning up
			out.flush();
			writer.removeAllIIOWriteProgressListeners();

			writer.dispose();
			out.close();

		} catch (Exception ex) {
		}

	}

	private byte[] image_byte_data(BufferedImage image) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
		try {
			encoder.encode(image);
		} catch (ImageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return os.toByteArray();
	}

	public String getSelectedDeviceID() {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return null;

		if (handle.getSelection().getSelectedElements().size() != 1)
			return null;

		Element element = handle.getSelection().getSelectedElements()
				.iterator().next();

		return getDeviceID(element);
	}

	public String getDeviceID(Element element) {
		return null;
	}

	public int setWink(boolean wink, int seccend) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return -1;
		try {
			handle.getSelection().setWink(wink, seccend);
			return 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public Editor getEditor() {
		return editor;
	}

	public Object invoke(String methodName, Object[] params, Class[] paramTypes) {
		return editor.getSvgSession().invoke(methodName, params, paramTypes);
	}

}
