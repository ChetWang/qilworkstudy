package com.nci.domino.help;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageUtil {

	/**
	 * 缩放
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param blankWhite
	 *            比例不对时是否需要补白
	 */
	public static Image resize(BufferedImage src, int width, int height,
			boolean blankWhite) {
		double ratio = 0;
		// 计算比例,不失真时只需一个宽或高的其中一个比例
		ratio = (new Integer(width)).doubleValue() / src.getWidth();
		Image itemp = getFasterScaledInstance(src, (int) ((double) src
				.getWidth() * ratio), (int) ((double) src.getHeight() * ratio),
				RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

		if (blankWhite) {
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			if (width == itemp.getWidth(null))
				g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
						itemp.getWidth(null), itemp.getHeight(null),
						Color.white, null);
			else
				g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp
						.getWidth(null), itemp.getHeight(null), Color.white,
						null);
			g.dispose();
			return image;
		} else {
			return itemp;
		}
	}

	/**
	 * 快速缩放
	 * @param img
	 * @param targetWidth
	 * @param targetHeight
	 * @param hint
	 * @param progressiveBilinear
	 * @return
	 */
	public static BufferedImage getFasterScaledInstance(BufferedImage img,
			int targetWidth, int targetHeight, Object hint,
			boolean progressiveBilinear) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;
		int w, h;
		int prevW = ret.getWidth();
		int prevH = ret.getHeight();
		boolean isTranslucent = img.getTransparency() != Transparency.OPAQUE;

		if (progressiveBilinear) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
			w = w < targetWidth || h < targetHeight ? targetWidth : w;
			h = w < targetWidth || h < targetHeight ? targetHeight : h;
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (progressiveBilinear && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (progressiveBilinear && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			if (scratchImage == null || isTranslucent) {
				// Use a single scratch buffer for all iterations
				// and then copy to the final, correctly-sized image
				// before returning
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}
			RenderingHints rh = new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			rh.put(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			rh.put(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			rh.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			rh.put(RenderingHints.KEY_INTERPOLATION, hint);
			g2.setRenderingHints(rh);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
			prevW = w;
			prevH = h;

			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);

		if (g2 != null) {
			g2.dispose();
		}

		// If we used a scratch buffer that is larger than our target size,
		// create an image of the right size and copy the results into it
		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}

		return ret;
	}

}
