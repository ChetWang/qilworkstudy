package com.nci.svg.server.innerface;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-17
 * @���ܣ�SVG��ʽ�ļ�ת����jpg��
 * 
 */
public class TransformSVGModule extends OperationServiceModuleAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8524392467408008190L;

	public TransformSVGModule(HashMap parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.service.OperationServiceModuleAdapter#handleOper(java.lang.Object)
	 */
	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ת��SVG���ܣ�δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ת��SVG���ܣ�δ�ܻ�ȡ��־��������!");
		}

		log.log(this, LoggerAdapter.DEBUG, "ת��SVG���ܣ���ȡ����" + actionName + "���������");
		// �ж����������Ƿ�ΪgetSvgCodes
		if (actionName.equalsIgnoreCase(ActionNames.TRANSFORM_SYMBOL_TO_JPG)) {
			// *****************
			// ��ͼԪת����JPG��ʽ
			// *****************
			String name = (String) getRequestParameter(requestParams, ActionParams.NAME);
			rb = transformSymbolToJpg(name);
			// try {
			// name = isoToUtf8(name);
			// } catch (UnsupportedEncodingException e) {
			// log.log(this, LoggerAdapter.ERROR, e);
			// rb.setReturnFlag(ResultBean.RETURN_ERROR);
			// rb.setErrorText("ת��SVG���ܣ�ͼԪ��������");
			// return rb;
			// }
		} else if (actionName
				.equalsIgnoreCase(ActionNames.TRANSFORM_SVGFILE_TO_JPG)) {
			// *****************
			// ��ͼ��ת����JPG��ʽ
			// *****************
			String bussID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String id = (String) getRequestParameter(requestParams, ActionParams.ID);
			String name = (String) getRequestParameter(requestParams, ActionParams.NAME);
			rb = transformSvgFileToJpg(bussID, id, name);
		} else {
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * ת��ͼԪ�ļ���jpg��ʽ����
	 * 
	 * @param name��ͼԪ����
	 * @return��jpg�ַ�����ʧ���򷵻�ָ��demo�ļ�
	 */
	public ResultBean transformSymbolToJpg(String name) {
		// Create a JPEG transcoder
		if (name == null || name.length() == 0) {
			return returnErrMsg("ͼԪת����JPG����ȡͼԪ����Ϊ�գ�");
		}

		byte[] result = null;
		if (controller != null) {
			Object obj = controller.getGraphStorageManager().loadSymbol(name)
					.getReturnObj();
			String content = null;
			if (obj instanceof String)
				content = (String) obj;
			if (content != null) {
				result = transformSVGToJpg(content);
			}
		}

		if (result != null && result.length > 0)
			return returnSuccMsg("byte[]", result);
		else
			return returnErrMsg("ͼԪת��JPG��ͼ�θ�ʽת��ʧ�ܣ�");
	}

	/**
	 * ת��ͼԪ�ļ���jpg��ʽ����
	 * 
	 * @param bussID:ҵ��ϵͳ���
	 * @param id��ͼԪ���
	 * @param name��ͼԪ����
	 * @return��jpg�ַ�����ʧ���򷵻�ָ��demo�ļ�
	 */
	public ResultBean transformSvgFileToJpg(String bussID, String id, String name) {
		return returnErrMsg("����δ��ɣ�");
	}

	/**
	 * ���������svg�ַ�����ת����jpg��ʽ�ַ���
	 * 
	 * @param source��svg��ʽ�ַ���
	 * @return��jpg��ʽ�ַ���
	 */
	public byte[] transformSVGToJpg(String source) {
		byte[] content = null;
		JPEGTranscoder t = new JPEGTranscoder();

		// Set the transcoding hints.
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
		t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(300));
		t.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(300));

		// Create the transcoder input.
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(source
					.getBytes("UTF-8"));
			TranscoderInput input = new TranscoderInput(inputStream);

			// Create the transcoder output.
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(ostream);
			// Save the image.
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
			log.log(this, LoggerAdapter.ERROR, "SVGת����JPGʱʧ�ܣ�");
			content = null;
		}
		return content;
	}

}
