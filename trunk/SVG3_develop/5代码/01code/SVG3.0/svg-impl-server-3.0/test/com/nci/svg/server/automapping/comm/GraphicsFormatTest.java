package com.nci.svg.server.automapping.comm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

public class GraphicsFormatTest extends TestCase {

	public GraphicsFormatTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testTransformSVGToJpg() {
		int width = 40;
		int height = 40;
		String source = getSVGContent();
		byte[] mapContent = GraphicsFormat.transformSVGToJpg(source, width, height);
		
		 ByteArrayOutputStream ostream3 = new ByteArrayOutputStream();
		 try {
			ostream3.write(mapContent);
			OutputStream ostream4 = new FileOutputStream(new
					File("d:\\1.jpg"));
			ostream3.writeTo(ostream4);
			ostream4.flush();
			ostream4.close();
			ostream3.flush();
			ostream3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getSVGContent() {
		StringBuffer str = new StringBuffer();
		str.append("<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\"").append(
				" xmlns=\"http://www.w3.org/2000/svg\"").append(
				" width=\"100%\" height=\"100%\"").append(
				" preserveAspectRatio=\"xMinYMin\" >").append(
				"<rect x=\"10\" y=\"10\" width=\"10\"").append(
				" height=\"10\" fill=\"red\"/></svg>");
		return str.toString();
	}
}
