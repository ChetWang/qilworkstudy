package com.nci.svg.district;

import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import org.apache.batik.dom.svg.SVGOMUseElement;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.module.BusinessModuleAdapter;
import com.nci.svg.sdk.shape.DrawToolkit;
import com.nci.svg.sdk.shape.BusinessDrawingHandler.StoreData;
import com.nci.svg.sdk.shape.BusinessDrawingHandler.TextShowData;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2009-3-6
 * @功能：
 * 
 */
public class MainPathShape extends BusinessModuleAdapter {
	
	private int hzgx ;
	private int hzzx ;

	public MainPathShape(EditorAdapter editor) {
		super(editor);

	}

	@Override
	public StoreData draw(int index, int actionType, ExtendedGeneralPath path,
			Point2D beginPoint, Point2D endPoint,int modifier) {
		if (beginPoint == null || endPoint == null)
			return null;

		double scale = editor.getHandlesManager().getCurrentHandle()
				.getCanvas().getZoomManager().getCurrentScale();
		double scaleOffset = 50.0 * scale;
		DrawToolkit.reset(path);
		Point2D lastPoint = null;
		// 计算两点间距离
		double distance = Math.sqrt((beginPoint.getX() - endPoint.getX())
				* (beginPoint.getX() - endPoint.getX())
				+ (beginPoint.getY() - endPoint.getY())
				* (beginPoint.getY() - endPoint.getY()));

		long size = Math.round(distance / scaleOffset);
		if (size < 0) {
			return null;
		}
		if (true) {
			path.moveTo((float)beginPoint.getX(), (float)beginPoint.getY());
			DrawToolkit.drawCircle(path, beginPoint, 10);
			path.moveTo((float)beginPoint.getX(), (float)beginPoint.getY());
			lastPoint = beginPoint;
		}
		for (int i = 1; i < size; i++) {
			Point2D point = getPoint(beginPoint, endPoint, distance, i,
					scaleOffset);
			path.lineTo((float)point.getX(), (float)point.getY());
			DrawToolkit.drawCircle(path, point, 10);
			path.moveTo((float)point.getX(), (float)point.getY());
			lastPoint = point;
		}
		ArrayList<TextShowData> list = new ArrayList<TextShowData>();
		TextShowData textData = drawingHandler.newTextShowData();
		textData.setPoint(lastPoint);
		textData.setText("目前" + size + "个杆塔");
		textData.setFont(new Font("宋体",0,12));
		list.add(textData);
		StoreData data = drawingHandler.newStoreData();
		data.setObj(50);
		data.setPoint(lastPoint);
		data.setTextList(list);
		return data;
	}

	private Point2D getPoint(Point2D beginPoint, Point2D endPoint,
			double distance, int pos, double offset) {
		Point2D.Double point = new Point2D.Double();
		point.x = beginPoint.getX() + (endPoint.getX() - beginPoint.getX())
				* offset * pos / distance;
		point.y = beginPoint.getY() + (endPoint.getY() - beginPoint.getY())
				* offset * pos / distance;
		return point;
	}

	private Point2D getBeginPoint(Path path)
	{
		Point2D beginPoint = null;
		Segment seg = path.getSegment();
		beginPoint = seg.getEndPoint();
		return beginPoint;
	}
	
	private Point2D getEndPoint(Path path)
	{
		Point2D endPoint = null;
		int num = path.getSegmentsNumber();
		Segment seg = path.getSegment();
		for(int j = 0;j < num -1;j++)
			seg = seg.getNextSegment();
		endPoint = seg.getEndPoint();
		return endPoint;
	}
	@Override
	public Element createElement(SVGHandle handle,int index,
			LinkedList<ExtendedGeneralPath> shapes,
			LinkedList<StoreData> dataList) {
		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
		// the edited document
		EditorToolkit.insertCurSymbolByName(handle, "杆塔");
		// g打包
		final Element gElement = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "g");
		gElement.setAttribute("id", UUID.randomUUID().toString());
		int size = shapes.size();
		Point2D beginPoint = null;
		Point2D endPoint = null;
		Point2D beforePoint = null;
		Point2D afterPoint = null;
		String beforeID = null, afterID = null;
		for(int i = 0;i < size ;i++)
		{
			Shape shape = shapes.get(i);
			
			Path path = null;
			// 首先根据continuesShape绘制图元
			if (shape != null
					&& shape instanceof ExtendedGeneralPath) {

				// creating the path object that computes the string
				// representation of the path shape
				path = new Path((ExtendedGeneralPath) shape);
			}
			if(path != null)
			{
				beginPoint = getBeginPoint(path);
				endPoint = getEndPoint(path);
				
				Integer offset = (Integer)(dataList.getFirst().getObj());
				double distance = Math.sqrt((beginPoint.getX() - endPoint
						.getX())
						* (beginPoint.getX() - endPoint.getX())
						+ (beginPoint.getY() - endPoint.getY())
						* (beginPoint.getY() - endPoint.getY()));
				long gantaSize = Math.round(distance / offset) + 1;
				Rectangle2D.Double rect = new Rectangle2D.Double();
				rect.x = beginPoint.getX() - 10;
				rect.y = beginPoint.getY() - 10;
				rect.width = 20;
				rect.height = 20;
				if (i == 0) {
					Element startEle = EditorToolkit.insertSymbolElement(handle, rect,
							gElement,null, "杆塔", "正常");
					afterID = startEle.getAttribute("id");
					afterPoint = beginPoint;
				}

				for (int j = 1; j < gantaSize; j++) {
					beforePoint = afterPoint;
					beforeID = afterID;
					afterPoint = getPoint(beginPoint, endPoint, distance, j,
							offset);
					rect.x = afterPoint.getX() - 10;
					rect.y = afterPoint.getY() - 10;
					rect.width = 20;
					rect.height = 20;
					Element startEle = EditorToolkit.insertSymbolElement(handle, rect,
							gElement,null, "杆塔", "正常");
					afterID = startEle.getAttribute("id");
					EditorToolkit.insertPathElement(handle, gElement,null,
							beforePoint, afterPoint, beforeID, afterID,
							TerminalModule.CENTER_TERMINAL_NAME,
							TerminalModule.CENTER_TERMINAL_NAME);
				}
			}
		}
		
		insertShapeElement(handle, gElement);
		return gElement;
	}


	@Override
	protected void doAction(int index) {
		drawingHandler.setIndex(index);
		editor.getSelectionManager().setSelectionMode(
				SelectionInfoManager.DRAWING_MODE, this);
		// 将选择的symbol或shape置为非选择状态
		editor.getSymbolSession().clearSelectedBtnGroup();
		editor.getHandlesManager().getCurrentHandle().getScrollPane()
				.setListenersEnabled(false);
	}

	@Override
	protected void initHandle(int index, SVGHandle handle) {

	}

	@Override
	protected void setMenuItemInfo() {
		hzgx = addMenu("绘制干线");
		hzzx = addMenu("绘制支线");
		return;
	}

	@Override
	protected boolean verifyValidity(int index, SVGHandle handle,
			Set<Element> selectedElements) {
        if(index == hzgx)
        {
        	if(handle == null || handle.getCanvas().getFileType() == null ||
        			!handle.getCanvas().getFileType().equals("district"))
        		return false;
        	
        	if(selectedElements!= null && selectedElements.size() > 0)
        		return false;
        }
        else if(index == hzzx)
        {
        	if(handle == null || handle.getCanvas().getFileType() == null ||
        			!handle.getCanvas().getFileType().equals("district"))
        		return false;
        	
        	if(selectedElements == null || selectedElements.size()!= 1)
        		return false;
        	
        	Element element = selectedElements.iterator().next();
        	if(!(element instanceof SVGOMUseElement))
        		return false;
        }
		return true;
	}

}
