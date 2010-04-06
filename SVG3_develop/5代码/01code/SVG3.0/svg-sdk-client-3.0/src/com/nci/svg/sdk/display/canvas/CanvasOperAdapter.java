package com.nci.svg.sdk.display.canvas;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.shape.GroupBreakerIF;
import com.nci.svg.sdk.topology.TopologyManagerAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-5
 * @���ܣ�����ͼ�β�����װ��
 * 
 */
public abstract class CanvasOperAdapter {
	/**
	 * add by yux,2009-3-5 ѡ�����Group�ڵ�ӿ�
	 */
	protected GroupBreakerIF groupBreaker = null;

	/**
	 * add by yux,2009-3-5 ͼ�����˽����ӿ�
	 */
	protected TopologyManagerAdapter topologyManager = null;

	/**
	 * �༭������
	 */
	protected EditorAdapter editor;
	
	protected SVGHandle handle;

	/**
	 * ���캯����
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
	 * ����ָ�������µ�GroupBreaker
	 * @return
	 */
	protected abstract GroupBreakerIF createGroupBreaker();

	/**
	 * ����ָ�������µ�TopologyManager
	 * @return
	 */
	protected abstract TopologyManagerAdapter createTopologyManagerAdapter();

	/**
	 * ���ؽ���Group�ڵ�ӿ�
	 * 
	 * @return the groupBreak
	 */
	public GroupBreakerIF getGroupBreaker() {
		return groupBreaker;
	}

	/**
	 * ����ͼ�����˽����ӿ�
	 * 
	 * @return the topology
	 */
	public TopologyManagerAdapter getTopologyManager() {
		return topologyManager;
	}

	/**
	 * ��ȡ�༭������
	 * 
	 * @return
	 */
	public EditorAdapter getEditor() {
		return editor;
	}

}
