package com.nci.svg.sdk.other;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Element;

import com.nci.svg.sdk.bean.TopologyBean;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * 类名：com.nci.svg.other.LinkPointManager 创建人:yx 创建日期：2008-5-6 类作用:保存所有的连接点信息,
 * 修改日志： 2009.1.2 重新构建 2009.1.4 增加图线联动效果
 */
public class LinkPointManager {
	/**
	 * add by yux,2009-1-4 线起点连接图元编号描述符
	 */
	public final static String BEGIN_LINE_POINT = "p0";
	/**
	 * add by yux,2009-1-4 线终点连接图元编号描述符
	 */
	public final static String END_LINE_POINT = "p1";
	/**
	 * add by yux,2009-1-4 线起点连接图元拓扑点描述符
	 */
	public final static String BEGIN_LINE_TERMINAL = "t0";
	/**
	 * add by yux,2009-1-4 线终点连接图元拓扑点描述符
	 */
	public final static String END_LINE_TERMINAL = "t1";

	/**
	 * add by yux,2009-1-4 图元新增操作
	 */
	public final static int SYMBOL_ACTION_ADD = 1;

	/**
	 * add by yux,2009-1-4 图元删除操作
	 */
	public final static int SYMBOL_ACTION_DEL = -1;

	/**
	 * add by yux,2009-1-4 图元修改操作
	 */
	public final static int SYMBOL_ACTION_MODIFY = 0;
	/**
	 * add by yux,2009-1-4 图线关联存储空间
	 */
	protected HashMap<String, ArrayList<LineData>> mapLine = new HashMap<String, ArrayList<LineData>>();
	/**
	 * add by yux,2009-1-4 图线关联痕迹空间
	 */
	protected HashMap<String, ArrayList<LineData>> mapHis = new HashMap<String, ArrayList<LineData>>();

	private SVGHandle svgHandle = null;

	public LinkPointManager(SVGHandle svgHandle) {
		this.svgHandle = svgHandle;
	}

	/**
	 * add by yux,2009-1-4 根据线编号、图元编号、图元拓扑点名称、线拓扑点名称构建图线关联
	 * 
	 * @param lineID：线编号
	 * @param symbolID：图元编号
	 * @param symbolTerminalName：图元拓扑点名称
	 * @param lineTerminalName：线拓扑点名称
	 */
	public void addLinkPoint(String lineID, String symbolID,
			String symbolTerminalName, String lineTerminalName) {
		if (lineID == null || lineID.length() == 0 || symbolID == null
				|| symbolID.length() == 0)
			return;
		ArrayList<LineData> list = mapLine.get(symbolID);
		if (list == null) {
			list = new ArrayList<LineData>();
			mapLine.put(symbolID, list);
		}
		LineData data = new LineData();
		data.setLineID(lineID);
		data.setSymbolID(symbolID);
		data.setSymbolTerminalName(symbolTerminalName);
		data.setLineTerminalName(lineTerminalName);
		if (!list.contains(data)) {
			list.add(data);
		}
		return;
	}

	/**
	 * add by yux,2009-1-4 根据图元编号和图线数据，增加关联关系
	 * 
	 * @param symbolID:图元编号
	 * @param data:关联数据
	 */
	public void addLinkPoint(String symbolID, LineData data) {
		ArrayList<LineData> list = mapLine.get(symbolID);
		if (list == null) {
			list = new ArrayList<LineData>();
			mapLine.put(symbolID, list);
		}
		if (!list.contains(data)) {
			list.add(data);
		}
		return;
	}

	/**
	 * add by yux,2009-1-4 根据图元编号和线编号删除相应的图线关联信息
	 * 
	 * @param symbolID：图元编号
	 * @param lineID：线编号
	 */
	public void removeLinkPoint(String symbolID) {
		ArrayList<LineData> list = mapLine.get(symbolID);
		if (list == null)
			return;

		list.clear();
		return;
	}
	
	public void removeLinkPoint(String lineID,String linePoint) {
		Iterator<ArrayList<LineData>> iterator = mapLine.values().iterator();
		while(iterator.hasNext())
		{
			ArrayList<LineData> list = iterator.next();
			ArrayList<LineData> removelist = new ArrayList<LineData>();
			for(LineData data:list)
			{
				if(data.getLineID().equals(lineID) && data.getLineTerminalName().equals(linePoint))
				{
					removelist.add(data);
					
				}
			}
			list.removeAll(removelist);
		}
		return;
	}

	public ArrayList<LineData> getLineData(String id) {
		return mapLine.get(id);
	}

	/**
	 * add by yux,2009-1-4 根据线编号，图元编号，图元拓扑点名称，线拓扑点名称生成存储数据
	 * 
	 * @param lineID：线编号
	 * @param symbolID：图元编号
	 * @param symbolTerminalName：图元拓扑点名称
	 * @param lineTerminalName：线拓扑点名称
	 * @return：如合理返回存储数据，失败则返回null
	 */
	public LineData createLineData(String lineID, String symbolID,
			String symbolTerminalName, String lineTerminalName) {
		if (lineID == null || lineID.length() == 0 || symbolID == null
				|| symbolID.length() == 0)
			return null;
		LineData data = new LineData();
		data.setLineID(lineID);
		data.setSymbolID(symbolID);
		data.setSymbolTerminalName(symbolTerminalName);
		data.setLineTerminalName(lineTerminalName);
		return data;
	}

	/**
	 * add by yux,2009-1-4 图元操作
	 * 
	 * @param action：操作类型
	 * @param elements：操作节点集合
	 */
	public void symbolAction(int action, Set<Element> elements) {
		switch (action) {
		case SYMBOL_ACTION_ADD: {
			//新增图元，一般用于图元被删除后复原
			String id = null;
			for (Element e : elements) {
				id = e.getAttribute("id");
				if (id != null && id.length() > 0) {
					ArrayList<LineData> list = mapHis.get(id);
					if (list != null) {
						mapLine.put(id, list);
						mapHis.remove(id);
					}
				}
			}
			break;
		}
		case SYMBOL_ACTION_DEL: {
			//删除图元用
			String id = null;
			for (Element e : elements) {
				id = e.getAttribute("id");
				if (id != null && id.length() > 0) {
					ArrayList<LineData> list = mapLine.get(id);
					if (list != null) {
						mapHis.put(id, list);
						mapLine.remove(id);
					}
				}
			}
			break;
		}
		case SYMBOL_ACTION_MODIFY: {
			//图元变化
			String id = null;
			for (Element e : elements) {
				id = e.getAttribute("id");
				if (id != null && id.length() > 0) {
					ArrayList<LineData> list = mapLine.get(id);
					if (list != null) {
						modifyLine(e,list);
					}
				}
			}
//			svgHandle.getEditor().getSvgSession().refreshCurrentHandleImediately();
			break;
		}
		default:
			break;
		}
	}

	/**
	 * add by yux,2009-1-5
	 * 增加图元关联
	 * @param elements
	 * @return
	 */
	protected UndoRedoAction addSymbol(final Set<Element> elements)
	{
		UndoRedoAction undoRedoAction = null;
		Runnable executeRunnable = new Runnable() {

            public void run() {
            	symbolAction(SYMBOL_ACTION_ADD,elements);
            }
        };

        // the undo runnable
        Runnable undoRunnable = new Runnable() {

            public void run() {
            	symbolAction(SYMBOL_ACTION_DEL,elements);
            }
        };

        // creating the undo/redo action, and adding it to the undo/redo stack

        undoRedoAction = ShapeToolkit.getUndoRedoAction("图线互动",
                executeRunnable, undoRunnable,elements);
		return undoRedoAction;
	}
	
	/**
	 * add by yux,2009-1-5
	 * 删除图元关联
	 * @param elements
	 * @return
	 */
	protected UndoRedoAction delSymbol(final Set<Element> elements)
	{
		UndoRedoAction undoRedoAction = null;
		Runnable executeRunnable = new Runnable() {

            public void run() {
            	symbolAction(SYMBOL_ACTION_DEL,elements);
            }
        };

        // the undo runnable
        Runnable undoRunnable = new Runnable() {

            public void run() {
            	symbolAction(SYMBOL_ACTION_ADD,elements);
            }
        };

        // creating the undo/redo action, and adding it to the undo/redo stack

        undoRedoAction = ShapeToolkit.getUndoRedoAction("图线互动",
                executeRunnable, undoRunnable,elements);
		return undoRedoAction;
	}
	
	/**
	 * add by yux,2009-1-5
	 * 根据图元关联信息,维护线
	 * @param elements
	 * @return
	 */
	protected UndoRedoAction modifySymbol(final Set<Element> elements)
	{
		UndoRedoAction undoRedoAction = null;
		Runnable executeRunnable = new Runnable() {

            public void run() {
            	symbolAction(SYMBOL_ACTION_MODIFY,elements);
            }
        };

        // the undo runnable
        Runnable undoRunnable = new Runnable() {

            public void run() {
            	symbolAction(SYMBOL_ACTION_MODIFY,elements);
            }
        };

        // creating the undo/redo action, and adding it to the undo/redo stack

        undoRedoAction = ShapeToolkit.getUndoRedoAction("图线互动",
                executeRunnable, undoRunnable,elements);
		return undoRedoAction;
	}
	/**
	 * add by yux,2009-1-5
	 * 图线互动
	 * @param action:执行类型
	 * @param elements:执行节点集合
	 * @return:可被执行的action
	 */
	public UndoRedoAction validateAction(int action, final Set<Element> elements)
	{
		UndoRedoAction undoRedoAction = null;
		switch(action)
		{
		case SYMBOL_ACTION_ADD:
		{
			undoRedoAction = addSymbol(elements);
			break;
		}
		case SYMBOL_ACTION_DEL:
		{
			undoRedoAction = delSymbol(elements);
			break;
		}
		case SYMBOL_ACTION_MODIFY:
		{
			undoRedoAction = modifySymbol(elements);
			break;
		}
		}
		return undoRedoAction;

	}
	/**
	 * add by yux,2009-1-4 修改线
	 * 根据传入的图线关联数据，维护线
	 * @param list:图线集合
	 */
	private void modifyLine(Element symbolElement,ArrayList<LineData> list) {
		for (LineData data : list) {
			Element lineElement = svgHandle.getCanvas().getMapInfo(data.lineID);
			svgHandle.getEditor().getSvgSession().refreshHandle(lineElement);
			Point point = svgHandle.getSelection().getPointByTerminalName(
					symbolElement, data.symbolTerminalName);
			if (point != null) {
				if(lineElement == null)
					return;
				String sPath = lineElement.getAttribute("d");
		        Path path = new Path(sPath);
		        int nCount = path.getSegmentsNumber();
		        Segment se = path.getSegment();
                if(data.lineTerminalName.equals(BEGIN_LINE_POINT))
                {
                     path.modifyPoint(point, 0);
                     sPath = path.toString();
                }
                else if(data.lineTerminalName.equals(END_LINE_POINT))
                {
                	//终点
                	StringBuffer buffer = new StringBuffer();
                  sPath = String.format("M%d %d", (int) se.getEndPoint().getX(),
                          (int) se.getEndPoint().getY());
                  buffer.append(sPath);
                  for (int i = 0; i < nCount - 2; i++) {
                      if (se == null)
                          break;
                      se = se.getNextSegment();
                      sPath = String.format("L%d %d", (int) se.getEndPoint().getX(),
                              (int) se.getEndPoint().getY());
                      buffer.append(sPath);
                  }
                  sPath = String.format("L%d %d", (int)point.getX(), (int)point.getY());
                  buffer.append(sPath);
                  sPath = buffer.toString();
//                	 path.modifyPoint(point, nCount-1);
                }
                lineElement.setAttribute("d",sPath);
                svgHandle.getEditor().getSvgSession().refreshHandle(lineElement);
			}

		}
	}
	public ArrayList<TopologyBean> TopologyAnalyse()
	{
		ArrayList<TopologyBean> list = new ArrayList<TopologyBean>();
		Iterator<ArrayList<LineData>> iterator = mapLine.values().iterator();
		while(iterator.hasNext())
		{
			ArrayList<LineData> dataList = iterator.next();
			for(LineData data: dataList)
			{
				
			}
		}
		return null;
	}
	/**
	 * 线连接关系数据集合
	 * 
	 */
	public class LineData {
		/**
		 * add by yux,2009-1-2 连接线的ID
		 */
		private String lineID;
		/**
		 * add by yux,2009-1-2 图元ID
		 */
		private String symbolID;
		/**
		 * add by yux,2009-1-2 连接图元拓扑点名称
		 */
		private String symbolTerminalName;

		/**
		 * add by yux,2009-1-2 连接线拓扑点名称
		 */
		private String lineTerminalName;

		/**
		 * 返回连接线的ID
		 * 
		 * @return the lineID
		 */
		public String getLineID() {
			return lineID;
		}

		/**
		 * 设置连接线的ID
		 * 
		 * @param lineID
		 *            the lineID to set
		 */
		public void setLineID(String lineID) {
			this.lineID = lineID;
		}

		/**
		 * 返回图元编号
		 * 
		 * @return the element
		 */
		public String getSymbolID() {
			return symbolID;
		}

		/**
		 * 设置图元编号
		 * 
		 * @param element
		 *            the element to set
		 */
		public void setSymbolID(String symbolID) {
			this.symbolID = symbolID;
		}

		/**
		 * 返回连接图元拓扑点名称
		 * 
		 * @return the terminalName
		 */
		public String getSymbolTerminalName() {
			return symbolTerminalName;
		}

		/**
		 * 设置连接图元拓扑点名称
		 * 
		 * @param terminalName
		 *            the terminalName to set
		 */
		public void setSymbolTerminalName(String symbolTerminalName) {
			this.symbolTerminalName = symbolTerminalName;
		}

		/**
		 * 返回连接线拓扑点名称
		 * 
		 * @return the lineTerminalName
		 */
		public String getLineTerminalName() {
			return lineTerminalName;
		}

		/**
		 * 设置连接线拓扑点名称
		 * 
		 * @param lineTerminalName
		 *            the lineTerminalName to set
		 */
		public void setLineTerminalName(String lineTerminalName) {
			this.lineTerminalName = lineTerminalName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return symbolTerminalName + ":" + lineID + ":" + lineTerminalName;
		}

	}

}
