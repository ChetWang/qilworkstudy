package com.nci.svg.sdk.ui.graphunit;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

public class PersonalColorFilter extends RGBImageFilter {
	
//	private static int PERSONAL_RGB = ~Constants.THUMBNAIL_PERSONAL_BACKGROUND.getRGB() & 0x00FFFFFF;
//	
//	private static int RELEASED_RGB = ~Constants.THUMBNAIL_RELEASED_BACKGROUND.getRGB() & 0x00FFFFFF;

	public static Image createPersonalColoImage(Image image) {
		PersonalColorFilter grayfilter = new PersonalColorFilter();
		FilteredImageSource filteredimagesource = new FilteredImageSource(image
				.getSource(), grayfilter);
		Image image1 = Toolkit.getDefaultToolkit().createImage(
				filteredimagesource);
		return image1;
	}

	public PersonalColorFilter() {
		canFilterIndexColorModel = true;
	}

	public int filterRGB(int x, int y, int rgb) {
		if(rgb == -3355393) // 9999ff（Constants.THUMBNAIL_RELEASED_BACKGROUND的二进制值)的补码
//			//要换成别的颜色
			return -1467924; //e9999eb（Constants.THUMBNAIL_PERSONAL_BACKGROUND的二进制值)的补码
//		if(rgb == -RELEASED_RGB) 
//			return -PERSONAL_RGB;
		return rgb;
	}

}
