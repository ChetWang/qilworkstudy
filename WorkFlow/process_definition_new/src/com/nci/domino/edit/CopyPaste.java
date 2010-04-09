package com.nci.domino.edit;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoBeanComparator;
import com.nci.domino.help.Functions;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.utils.CopyableUtils;

/**
 * ��������󣬸�����ճ������
 * 
 * @author Qil.Wong
 * 
 */
public class CopyPaste {

	/**
	 * ����ѡ�е�shape���������,��xml���л���ʽת��ϵͳ������
	 * 
	 * @param selectedShapes
	 */
	public void copyShapes(List<AbstractShape> selectedShapes) {
		List<WofoBaseBean> beans = new ArrayList<WofoBaseBean>();
		for (AbstractShape s : selectedShapes) {
			beans.add(s.getWofoBean());
		}
		try {
			// ���л���xml
			String xml = CopyableUtils.toString(beans);
			StringSelection stringSelection = new StringSelection(xml);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					stringSelection, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ָ����paintboard��ͼ��ճ���������ͼ�Σ�ͼ���ǴӼ������xml�ַ������л�����
	 * 
	 * @param board
	 */
	@SuppressWarnings("unchecked")
	public synchronized void pasteShapes(PaintBoard board) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(this);
		DataFlavor flavor = DataFlavor.stringFlavor;
		if (contents.isDataFlavorSupported(flavor)) {
			try {
				String xml = (String) contents.getTransferData(flavor);
				// ������id��Ϣ���൱�����������°汾
				xml = Functions.applyNewVersion(xml);
				List<WofoBaseBean> shapeBeans = (List<WofoBaseBean>) CopyableUtils
						.toObject(xml);
				// ��֤WofoTransitionBean��������
				Collections.sort(shapeBeans, new WofoBeanComparator());
				Map<String, Class> shapeWofoBeanConfig = new HashMap<String, Class>();
				Document shapeWofoBeanDoc = Functions.getXMLDocument(getClass()
						.getResource("/resources/shape_wofobeans.xml"));
				NodeList configList = shapeWofoBeanDoc.getDocumentElement()
						.getElementsByTagName("shape");
				for (int i = 0; i < configList.getLength(); i++) {
					Element e = (Element) configList.item(i);
					try {
						shapeWofoBeanConfig.put(e.getAttribute("id"), e
								.getAttribute("generator").equals("") ? null
								: Class.forName(e.getAttribute("generator")));
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				for (WofoBaseBean baseBean : shapeBeans) {
					if (baseBean == null)
						continue;
					Class genClass = shapeWofoBeanConfig.get(baseBean
							.getClass().getName());
					if (genClass != null) {
						try {
							// ���������ļ�/resources/shape_wofobeans.xml����Ķ�Ӧ��ϵ����ָ����Generator���е���
							// generateShapeFromWofoBean��̬���������������ͳһ�̶��ģ����Ƽ��޸�
							Method m = genClass.getMethod(
									"generateShapeFromWofoBean", new Class[] {
											WofoBaseBean.class,
											PaintBoardBasic.class });
							List<AbstractShape> pastedShapes = new ArrayList<AbstractShape>();
							AbstractShape s = (AbstractShape) m.invoke(null,
									new Object[] { baseBean, board });
							if (s != null) {
								board.addGraph(s);
								pastedShapes.add(s);
								board.getUndomgr().addEdit(
										new ActionGroup(pastedShapes, board));
							} else {
								Logger.getLogger(CopyPaste.class.getName())
										.log(Level.SEVERE,
												"�Ӷ���ӳ�������AbstractShape�ǿ�");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						Logger.getLogger(CopyPaste.class.getName()).log(
								Level.SEVERE,
								"�Ӷ���ӳ�������" + baseBean.getClass().getName()
										+ "��Ӧ��ͼ��������Ϊ��");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				board.repaint();
			}
		}
	}
}
