package com.nci.svg.sdk.display.canvas;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.shape.GroupBreakerIF;
import com.nci.svg.sdk.topology.TopologyManagerAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-3-5
 * @功能：具体图形操作封装类
 * 
 */
public abstract class CanvasOperAdapter {
	/**
	 * add by yux,2009-3-5 选择解析Group节点接口
	 */
	protected GroupBreakerIF groupBreaker = null;

	/**
	 * add by yux,2009-3-5 图形拓扑解析接口
	 */
	protected TopologyManagerAdapter topologyManager = null;

	/**
	 * 编辑器对象
	 */
	protected EditorAdapter editor;
	
	protected SVGHandle handle;

	/**
	 * 构造函数，
	 * 
	 * @param editor
	 */
	public CanvasOperAdapter(EditorAdapter editor,SVGHandle handle) {
		this.editor = editor;
		this.handle = handle;
		groupBreaker = createGroupBreaker();
		topologyManager = createTopologyManagerAdapter();
	}

	/**
	 * 创建指定类型下的GroupBreaker
	 * @return
	 */
	protected abstract GroupBreakerIF createGroupBreaker();

	/**
	 * 创建指定类型下的TopologyManager
	 * @return
	 */
	protected abstract TopologyManagerAdapter createTopologyManagerAdapter();

	/**
	 * 返回解析Group节点接口
	 * 
	 * @return the groupBreak
	 */
	public GroupBreakerIF getGroupBreaker() {
		return groupBreaker;
	}

	/**
	 * 返回图形拓扑解析接口
	 * 
	 * @return the topology
	 */
	public TopologyManagerAdapter getTopologyManager() {
		return topologyManager;
	}

	/**
	 * 获取编辑器对象
	 * 
	 * @return
	 */
	public EditorAdapter getEditor() {
		return editor;
	}

}
