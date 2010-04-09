package com.nci.domino.importexport;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.nci.domino.PaintBoard;
import com.nci.domino.WfEditor;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.shape.basic.AbstractShape;

/**
 * png文件过滤导出器
 * 
 * @author Qil.Wong
 * 
 */
public class PngExport extends WfFileExport {

	public PngExport(WfEditor editor, String acceptExtention, String description) {
		super(editor, acceptExtention, description);
	}

	@Override
	public void export(String fileName) {
		final File file = new File(fileName);
		final PaintBoard currentBoard = editor.getOperationArea()
				.getCurrentPaintBoard();
		currentBoard.setAllUnselected();// 必须要移除，否则打印出来也带选择框
		WfSwingWorker<Object, Object> worker = new WfSwingWorker<Object, Object>(
				"正在导出PNG图形") {

			@Override
			protected Object doInBackground() throws Exception {
				try {

					List<AbstractShape> shapes = currentBoard.getGraphVector();
					int width = currentBoard.getSize().width;
					int height = currentBoard.getSize().height;
					BufferedImage bufferedImage = new BufferedImage(width,
							height, BufferedImage.TYPE_BYTE_BINARY);

					Graphics2D g2d = bufferedImage.createGraphics();
					RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				            RenderingHints.VALUE_ANTIALIAS_ON);
					rh.put(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY);
					rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					rh.put(RenderingHints.KEY_FRACTIONALMETRICS,
							RenderingHints.VALUE_FRACTIONALMETRICS_ON);
					g2d.setRenderingHints(rh);
					bufferedImage = g2d.getDeviceConfiguration()
							.createCompatibleImage(width, height,
									Transparency.TRANSLUCENT);
					g2d.dispose();
					g2d = bufferedImage.createGraphics();

					for (AbstractShape shape : shapes) {
						shape.drawShape(g2d, currentBoard);
					}

					g2d.dispose();

					ImageIO.write(bufferedImage, "png", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			public void wfDone() {
				editor.getStatusBar().showGlassInfo(
						"已将图形导出至 " + file.getAbsolutePath());
			}
		};
		editor.getBackgroundManager().enqueueOpertimeQueue(worker);

	}

}
