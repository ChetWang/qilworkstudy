package fr.itris.glips.svgeditor.io.managers.export.handler;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.w3c.dom.Document;

import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.io.managers.export.FileExport;
import fr.itris.glips.svgeditor.io.managers.export.handler.dialog.ExportDialog;
import fr.itris.glips.svgeditor.io.managers.export.handler.dialog.JPGExportDialog;
import fr.itris.glips.svgeditor.io.managers.export.image.SVGDocumentImageCreator;
import fr.itris.glips.svgeditor.io.managers.export.monitor.ExportMonitor;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;

/**
 * the class used to export images in a jpg format
 * 
 * @author ITRIS, Jordi SUC
 */
public class JPGExport extends Export {

	/**
	 * the quality of the export
	 */
	private float jpgQuality;

	/**
	 * whether the Huffman tables are optimized
	 */
	private boolean isOptimized;

	/**
	 * whether the compression is progressive
	 */
	private boolean isProgressive;
	

	/**
	 * the constructor of the class
	 * 
	 * @param fileExport
	 *            the object manager the export
	 */
	protected JPGExport(FileExport fileExport) {

		super(fileExport);

		// creating the dialog
		if (fileExport.getEditor().getParent() instanceof Frame) {

			exportDialog = new JPGExportDialog((Frame) fileExport.getEditor().getParent(),fileExport.getEditor());

		} else {

			exportDialog = new JPGExportDialog((JDialog) fileExport.getEditor().getParent(),fileExport.getEditor());
		}
	}

	@Override
	public void export(JComponent relativeComponent, Document document,
			File destFile, Rectangle rect) {

		if (getFileExport().getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.SCREEN_CAST_MODE) {
			// monitor.setProgressMessage("正在计算图形...");

			Point2D imageSize = SVGDocumentImageCreator
					.getGeometryCanvasSize(document);
			double scale = getFileExport().getEditor().getHandlesManager()
					.getCurrentHandle().getCanvas().getZoomManager()
					.getCurrentScale();
			width = imageSize.getX() * scale;
			height = imageSize.getY() * scale;
			// width = Editor.getEditor().getSelectionManager()
			// .getScreenCastRect().getWidth();
			// height = Editor.getEditor().getSelectionManager()
			// .getScreenCastRect().getHeight();
			jpgQuality = 1.0f;
			isOptimized = true;
			isProgressive = true;
			monitor = null;
			createImage(document, destFile, true);
		} else {
			monitor = new ExportMonitor(getFileExport().getEditor().getParent(), 0, 100,
					FileExport.prefixLabels[0]);
			monitor.setRelativeComponent(relativeComponent);
			JPGExportDialog jpgExportDialog = (JPGExportDialog) exportDialog;
			if (getFileExport().getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.SCREEN_CAST_MODE) {
				jpgExportDialog.getSizeChooser().setVisible(false);
			} else {
				jpgExportDialog.getSizeChooser().setVisible(true);
			}
			// showing the dialog used to select the values of the parameters
			// for
			// the export
			int res = exportDialog.showExportDialog(document);
			if (res == ExportDialog.OK_ACTION) {

				// getting the parameters
				width = jpgExportDialog.getExportSize().getX();
				height = jpgExportDialog.getExportSize().getY();
				jpgQuality = jpgExportDialog.getJpgQuality();
				isOptimized = jpgExportDialog.isOptimized();
				isProgressive = jpgExportDialog.isProgressive();

				// creating the image
				createImage(document, destFile, false);
			}
		}
	}

	@Override
	public void export(JComponent relativeComponent, Document document,
			File destFile) {
		export(relativeComponent, document, destFile, null);
	}

	@Override
	protected void writeImage(final BufferedImage image, final File destFile) {
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

			 if (getFileExport().getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.SCREEN_CAST_MODE) {
				params.setSourceRegion(getFileExport().getEditor().getSelectionManager()
						.getScreenCastRect());
			}

			params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			params.setCompressionQuality(jpgQuality);
			params.setOptimizeHuffmanTables(isOptimized);
			if (isProgressive) {

				params.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);

			} else {

				params.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
			}

			// writing the image
			ImageOutputStream out = ImageIO.createImageOutputStream(destFile);
			writer.setOutput(out);

			// adding the listener to the writer
			writer.addIIOWriteProgressListener(new IIOWriteProgressListener() {

				public void imageComplete(ImageWriter wr) {

					writer.removeIIOWriteProgressListener(this);
					if (monitor != null)
						monitor.stop();
				}

				public void writeAborted(ImageWriter wr) {

					writer.removeIIOWriteProgressListener(this);
					handleExportFailure();
				}

				public void imageProgress(ImageWriter wr, float value) {
					if (monitor != null) {
						monitor.setProgress((int) (50 + value / 2));

						if (monitor.isCancelled()) {

							writer.abort();
						}
					}
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
			if (monitor != null)
				monitor.stop();
			writer.dispose();
			out.close();
			// 将内容复制到剪贴板
//			if (Editor.getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.SCREEN_CAST_MODE) {
//				writeToClipBoard(destFile);
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
			handleExportFailure();
		}

	}
}
