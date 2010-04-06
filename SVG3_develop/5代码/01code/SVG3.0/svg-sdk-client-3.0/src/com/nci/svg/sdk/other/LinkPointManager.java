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
 * ������com.nci.svg.other.LinkPointManager ������:yx �������ڣ�2008-5-6 ������:�������е����ӵ���Ϣ,
 * �޸���־�� 2009.1.2 ���¹��� 2009.1.4 ����ͼ������Ч��
 */
public class LinkPointManager {
	/**
	 * add by yux,2009-1-4 ���������ͼԪ���������
	 */
	public final static String BEGIN_LINE_POINT = "p0";
	/**
	 * add by yux,2009-1-4 ���յ�����ͼԪ���������
	 */
	public final static String END_LINE_POINT = "p1";
	/**
	 * add by yux,2009-1-4 ���������ͼԪ���˵�������
	 */
	public final static String BEGIN_LINE_TERMINAL = "t0";
	/**
	 * add by yux,2009-1-4 ���յ�����ͼԪ���˵�������
	 */
	public final static String END_LINE_TERMINAL = "t1";

	/**
	 * add by yux,2009-1-4 ͼԪ��������
	 */
	public final static int SYMBOL_ACTION_ADD = 1;

	/**
	 * add by yux,2009-1-4 ͼԪɾ������
	 */
	public final static int SYMBOL_ACTION_DEL = -1;

	/**
	 * add by yux,2009-1-4 ͼԪ�޸Ĳ���
	 */
	public final static int SYMBOL_ACTION_MODIFY = 0;
	/**
	 * add by yux,2009-1-4 ͼ�߹����洢�ռ�
	 */
	protected HashMap<String, ArrayList<LineData>> mapLine = new HashMap<String, ArrayList<LineData>>();
	/**
	 * add by yux,2009-1-4 ͼ�߹����ۼ��ռ�
	 */
	protected HashMap<String, ArrayList<LineData>> mapHis = new HashMap<String, ArrayList<LineData>>();

	private SVGHandle svgHandle = null;

	public LinkPointManager(SVGHandle svgHandle) {
		this.svgHandle = svgHandle;
	}

	/**
	 * add by yux,2009-1-4 �����߱�š�ͼԪ��š�ͼԪ���˵����ơ������˵����ƹ���ͼ�߹���
	 * 
	 * @param lineID���߱��
	 * @param symbolID��ͼԪ���
	 * @param symbolTerminalName��ͼԪ���˵�����
	 * @param lineTerminalName�������˵�����
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
	 * add by yux,2009-1-4 ����ͼԪ��ź�ͼ�����ݣ����ӹ�����ϵ
	 * 
	 * @param symbolID:ͼԪ���
	 * @param data:��������
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
	 * add by yux,2009-1-4 ����ͼԪ��ź��߱��ɾ����Ӧ��ͼ�߹�����Ϣ
	 * 
	 * @param symbolID��ͼԪ���
	 * @param lineID���߱��
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
	 * add by yux,2009-1-4 �����߱�ţ�ͼԪ��ţ�ͼԪ���˵����ƣ������˵��������ɴ洢����
	 * 
	 * @param lineID���߱��
	 * @param symbolID��ͼԪ���
	 * @param symbolTerminalName��ͼԪ���˵�����
	 * @param lineTerminalName�������˵�����
	 * @return��������ش洢���ݣ�ʧ���򷵻�null
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
	 * add by yux,2009-1-4 ͼԪ����
	 * 
	 * @param action����������
	 * @param elements�������ڵ㼯��
	 */
	public void symbolAction(int action, Set<Element> elements) {
		switch (action) {
		case SYMBOL_ACTION_ADD: {
			//����ͼԪ��һ������ͼԪ��ɾ����ԭ
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
			//ɾ��ͼԪ��
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
			//ͼԪ�仯
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
	 * ����ͼԪ����
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

        undoRedoAction = ShapeToolkit.getUndoRedoAction("ͼ�߻���",
                executeRunnable, undoRunnable,elements);
		return undoRedoAction;
	}
	
	/**
	 * add by yux,2009-1-5
	 * ɾ��ͼԪ����
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

        undoRedoAction = ShapeToolkit.getUndoRedoAction("ͼ�߻���",
                executeRunnable, undoRunnable,elements);
		return undoRedoAction;
	}
	
	/**
	 * add by yux,2009-1-5
	 * ����ͼԪ������Ϣ,ά����
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

        undoRedoAction = ShapeToolkit.getUndoRedoAction("ͼ�߻���",
                executeRunnable, undoRunnable,elements);
		return undoRedoAction;
	}
	/**
	 * add by yux,2009-1-5
	 * ͼ�߻���
	 * @param action:ִ������
	 * @param elements:ִ�нڵ㼯��
	 * @return:�ɱ�ִ�е�action
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
	 * add by yux,2009-1-4 �޸���
	 * ���ݴ����ͼ�߹������ݣ�ά����
	 * @param list:ͼ�߼���
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
                	//�յ�
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
	 * �����ӹ�ϵ���ݼ���
	 * 
	 */
	public class LineData {
		/**
		 * add by yux,2009-1-2 �����ߵ�ID
		 */
		private String lineID;
		/**
		 * add by yux,2009-1-2 ͼԪID
		 */
		private String symbolID;
		/**
		 * add by yux,2009-1-2 ����ͼԪ���˵�����
		 */
		private String symbolTerminalName;

		/**
		 * add by yux,2009-1-2 ���������˵�����
		 */
		private String lineTerminalName;

		/**
		 * ���������ߵ�ID
		 * 
		 * @return the lineID
		 */
		public String getLineID() {
			return lineID;
		}

		/**
		 * ���������ߵ�ID
		 * 
		 * @param lineID
		 *            the lineID to set
		 */
		public void setLineID(String lineID) {
			this.lineID = lineID;
		}

		/**
		 * ����ͼԪ���
		 * 
		 * @return the element
		 */
		public String getSymbolID() {
			return symbolID;
		}

		/**
		 * ����ͼԪ���
		 * 
		 * @param element
		 *            the element to set
		 */
		public void setSymbolID(String symbolID) {
			this.symbolID = symbolID;
		}

		/**
		 * ��������ͼԪ���˵�����
		 * 
		 * @return the terminalName
		 */
		public String getSymbolTerminalName() {
			return symbolTerminalName;
		}

		/**
		 * ��������ͼԪ���˵�����
		 * 
		 * @param terminalName
		 *            the terminalName to set
		 */
		public void setSymbolTerminalName(String symbolTerminalName) {
			this.symbolTerminalName = symbolTerminalName;
		}

		/**
		 * �������������˵�����
		 * 
		 * @return the lineTerminalName
		 */
		public String getLineTerminalName() {
			return lineTerminalName;
		}

		/**
		 * �������������˵�����
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
