/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-21
 * @功能：业务动作解释器
 *
 */
package com.nci.svg.actionparser;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 *
 */
public class ActionParserModule {
	private EditorAdapter editor = null;
	private SVGHandle handle = null;
	private Element element = null;
	private String action = null;
	public ActionParserModule(EditorAdapter editor,SVGHandle handle,Element element,String action)
	{
		this.editor = editor;
		this.handle = handle;
		this.element = element;
		this.action = action;
		parseAction();
		
	}
	
	/**
	 * add by yux,2009-1-21
	 * 解析动作
	 */
	private void parseAction()
	{
		editor.getSvgSession().showMessageBox("动作执行", action);
	}
	
	/**
	 * add by yux,2009-1-22
	 * 解析
	 * @return
	 */
	public Runnable getAction()
	{
		return null;
	}

}
