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
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-17
 * @功能：SVG格式文件转换成jpg等
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
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("转换SVG功能，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("转换SVG功能，未能获取日志操作对象!");
		}

		log.log(this, LoggerAdapter.DEBUG, "转换SVG功能，获取到‘" + actionName + "’请求命令！");
		// 判断请求命令是否为getSvgCodes
		if (actionName.equalsIgnoreCase(ActionNames.TRANSFORM_SYMBOL_TO_JPG)) {
			// *****************
			// 将图元转换成JPG格式
			// *****************
			String name = (String) getRequestParameter(requestParams, ActionParams.NAME);
			rb = transformSymbolToJpg(name);
			// try {
			// name = isoToUtf8(name);
			// } catch (UnsupportedEncodingException e) {
			// log.log(this, LoggerAdapter.ERROR, e);
			// rb.setReturnFlag(ResultBean.RETURN_ERROR);
			// rb.setErrorText("转换SVG功能，图元名称有误！");
			// return rb;
			// }
		} else if (actionName
				.equalsIgnoreCase(ActionNames.TRANSFORM_SVGFILE_TO_JPG)) {
			// *****************
			// 将图形转换成JPG格式
			// *****************
			String bussID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID);
			String id = (String) getRequestParameter(requestParams, ActionParams.ID);
			String name = (String) getRequestParameter(requestParams, ActionParams.NAME);
			rb = transformSvgFileToJpg(bussID, id, name);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 转换图元文件至jpg格式返回
	 * 
	 * @param name：图元名称
	 * @return：jpg字符串，失败则返回指定demo文件
	 */
	public ResultBean transformSymbolToJpg(String name) {
		// Create a JPEG transcoder
		if (name == null || name.length() == 0) {
			return returnErrMsg("图元转换成JPG，获取图元名称为空！");
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
			return returnErrMsg("图元转换JPG，图形格式转换失败！");
	}

	/**
	 * 转换图元文件至jpg格式返回
	 * 
	 * @param bussID:业务系统编号
	 * @param id：图元编号
	 * @param name：图元名称
	 * @return：jpg字符串，失败则返回指定demo文件
	 */
	public ResultBean transformSvgFileToJpg(String bussID, String id, String name) {
		return returnErrMsg("函数未完成！");
	}

	/**
	 * 根据输入的svg字符串，转换成jpg格式字符串
	 * 
	 * @param source：svg格式字符串
	 * @return：jpg格式字符串
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
			// 图形转换
			t.transcode(input, output);
			// System.out.println(System.currentTimeMillis());
			// 本地落地测试转换结果
			// OutputStream ostream2 = new FileOutputStream(new
			// File("c:\\work\\jar\\1.jpg"));
			// ostream.writeTo(ostream2);
			// ostream2.flush();
			// ostream2.close();

			content = ostream.toByteArray();
			// 转换成string后的测试
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
			log.log(this, LoggerAdapter.ERROR, "SVG转换成JPG时失败！");
			content = null;
		}
		return content;
	}

}
