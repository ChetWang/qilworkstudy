package com.nci.svg.sdk.topology;

import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.shape.GraphUnitImageShape;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-3-5
 * @���ܣ�ͼ�����˹��������ӿ�
 *
 */
public interface TopologyManagerIF {
    /**
     * ����ͼ�����˽ṹ
     * @return �����ɹ�����true,ʧ�ܷ���false
     */
    public boolean createTopology();
    
    /**
     * ��ָ��λ�ù���ͼԪ�ڵ�
     * @param bounds Ŀ��λ��
     * @param doc �ĵ�����
     * @param symboldoc ͼԪ����
     * @param name ͼԪ����
     * @param symbolID ͼԪ���
     * @param shape ͼԪ��������
     * @return �ɹ��򷵻ر������ɵ�ͼԪ�ڵ�,ʧ���򷵻�null
     */
    public abstract Element createElementAsGraphUnit(
			Rectangle2D bounds, Document doc, Document symboldoc, String name,
			String symbolID, GraphUnitImageShape shape);

	/**
	 * ��ָ��λ�ù���ģ��ڵ�
	 * @param bounds Ŀ��λ��
	 * @param doc �ĵ�����
	 * @param symboldoc ģ�����
     * @param name ͼԪ����
     * @param symbolID ͼԪ���
     * @param shape ͼԪ��������
     * @return �ɹ��򷵻ر������ɵ�ͼԪ�ڵ�,ʧ���򷵻�null
	 */
	public Element createElementAsTemplate(
			Rectangle2D bounds, Document doc, Document symboldoc, String name,
			String symbolID, GraphUnitImageShape shape);

	/**
	 * ��ȡ������model
	 * @return ������model
	 */
	public TreeModel getTreeModel();
	
	/**
	 * ��ȡ���˹������
	 * @return ���˹������,�粻�����򷵻�null
	 *         ÿ�����˶�����������ʵ���������
	 */
	public Object getTopologyObject();
	
	/**
	 * ��ȡ������������
	 * @return ����������������
	 */
	public MouseListener getTreeMouseListener();
	
	/**
	 * ��ȡ��������չ�ڵ㶨��
	 * @return ��������չ�ڵ㶨��
	 */
	public DefaultTreeCellRenderer getTreeCellRenderer();

	/**
	 * ����ָ�������λ�û�ȡ�����״
	 * @param point �������
	 * @return �������������,�ޱ仯�򷵻�null
	 */
	public Cursor getCursor(Point2D point);
	
	/**
	 * ��������¼�
	 * @param mouseEventType ����¼�����
	 * @param initPoint ��ʼ��
	 * @param curPoint ��ǰ��
	 * @return �¼��������
	 */
	public boolean nodifyMouseEvent(int mouseEventType,Point2D initPoint,Point2D curPoint);
    
}
