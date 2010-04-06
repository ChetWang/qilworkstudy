package com.nci.svg.server.automapping.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;


/**
 * <p>
 * ���⣺GraphicsFormat.java
 * </p>
 * <p>
 * ������ SVGͼ�θ�ʽת��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-3
 * @version 1.0
 */
public class GraphicsFormat {
	
	public static byte[] transformSVGToJpg(String source, int width, int height) {
		byte[] content = null;
		try {
		JPEGTranscoder t = new JPEGTranscoder();

		// Set the transcoding hints.
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
		t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(width));
		t.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(height));

		// Create the transcoder input.
		
			ByteArrayInputStream inputStream = new ByteArrayInputStream(source
					.getBytes("UTF-8"));
			TranscoderInput input = new TranscoderInput(inputStream);

			// Create the transcoder output.
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(ostream);
			// System.out.println(System.currentTimeMillis());
			// ͼ��ת��
			t.transcode(input, output);
			// System.out.println(System.currentTimeMillis());
			// ������ز���ת�����
			// OutputStream ostream2 = new FileOutputStream(new
			// File("c:\\work\\jar\\1.jpg"));
			// ostream.writeTo(ostream2);
			// ostream2.flush();
			// ostream2.close();

			content = ostream.toByteArray();
			// ת����string��Ĳ���
			// byte[] b = content.getBytes("UTF-8");
			// System.out.println(b.length);
			// ByteArrayOutputStream ostream3 = new ByteArrayOutputStream();
			// ostream3.write(b);
			// OutputStream ostream4 = new FileOutputStream(new
			// File("c:\\work\\jar\\1.jpg"));
			// ostream3.writeTo(ostream4);
			// ostream4.flush();
			// ostream4.close();
			// ostream3.flush();
			// ostream3.close();
			// Flush and close the stream.
			ostream.flush();
			ostream.close();

		} catch (Exception e) {
			e.printStackTrace();
//			log.log(this, LoggerAdapter.ERROR, "SVGת����JPGʱʧ�ܣ�");
			content = null;
		}
		return content;
	}

}
