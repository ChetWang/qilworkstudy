package com.nci.svg.district;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import com.nci.svg.district.relate.DistrictReadData;
import com.nci.svg.district.relate.DistrictRelating;
import com.nci.svg.district.relate.bean.DistrictAreaBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.module.BusinessModuleAdapter;
import com.nci.svg.sdk.shape.BusinessDrawingHandler.StoreData;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-4-2
 * @���ܣ�
 * 
 */
public class DistrictAutoRela extends BusinessModuleAdapter {

	/**
	 * add by yux,2009-4-9
	 * 
	 */
	private static final long serialVersionUID = -6199321534115680572L;

	public DistrictAutoRela(EditorAdapter editor) {
		super(editor);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.BusinessModuleAdapter#createElement(fr.itris.glips.svgeditor.display.handle.SVGHandle,
	 *      int, java.util.LinkedList, java.util.LinkedList)
	 */
	@Override
	public Element createElement(SVGHandle handle, int index,
			LinkedList<ExtendedGeneralPath> shapes,
			LinkedList<StoreData> dataList) {
		// TODO Auto-generated method stub
		// add by yux,2009-4-2
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.BusinessModuleAdapter#doAction(int)
	 */
	@Override
	protected void doAction(int index) {
		// ����
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		// editor.getPropertyModelInteractor().getGraphModel().initTreeModel(
		// handle);
		// ��ȡ���˽��
		DistrictTopology topology = (DistrictTopology) handle.getCanvas()
				.getCanvasOper().getTopologyManager().getTopologyObject();

		// �����˽����ֱ�ӷ���
		if (topology == null)
			return;

		// ��ȡ����
		String[][] params = new String[1][2];
		params[0][0] = "filename";
		params[0][1] = null;
		CommunicationBean communicate = new CommunicationBean(
				ActionNames.GET_AREA_DEMO_DATA, params);
		ResultBean resultBean = editor.getCommunicator().communicate(
				communicate);
		DistrictAreaBean areaBean = null;
		if (resultBean.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
			String data = (String) resultBean.getReturnObj();
			DistrictReadData readData = new DistrictReadData();
			areaBean = readData.readData(data);
		}

		// ���ݹ���
		if (areaBean != null) {
			// �ɹ��ӷ�������ȡ��̨������
			relating = new DistrictRelating(areaBean,
					topology, handle.getCanvas().getDocument());
			ResultBean rb = relating.relating();

			// ��ʾ�������
			// DefaultMutableTreeNode root = new DefaultMutableTreeNode("����");
			boolean isRelated = false;
			if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS)
				isRelated = true;
			DefaultMutableTreeNode root = relating.createBusiTree(isRelated);
			showRelatedMess(root);

			if (rb.getReturnFlag() == ResultBean.RETURN_ERROR) {
				// ��������ʧ��
				// TODO
			} else {
				// ���ݹ����ɹ�
			}
		} else {
			// �ӷ�������ȡ̨������ʧ��
			// TODO
		}
	}

	/**
	 * ҵ�������������
	 */
	private DistrictRelating relating = null;
	/**
	 * ҵ��ģ�͹�������Ի���
	 */
	private JDialog dialog;
	/**
	 * ҵ��ģ�͹������Ի������ṹ
	 */
	private JTree tree;
	private DefaultTreeModel treeModel;

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @���� ��ʼ��ҵ��ģ�͹�������Ի���
	 * @param root
	 *            Ҫ��ʾ��������
	 * @param isRelated
	 *            �ɹ�������־
	 */
	private void initRelateDialog(DefaultMutableTreeNode root) {
		dialog = new JDialog(editor.findParentFrame(), false);
		dialog.getContentPane().setLayout(new BorderLayout(5, 5));
		JScrollPane scroll = new JScrollPane();
		dialog.getContentPane().add(scroll, BorderLayout.CENTER);
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		scroll.setViewportView(tree);
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		JButton okBtn = new JButton("OK");
		btnPanel.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				okAction();
			}
		});
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("ҵ��ģ�͹������");
		dialog.setSize(260, 300);
		dialog.setVisible(true);
		dialog.pack();
	}

	/**
	 * 2009-4-30 Add by ZHM
	 * 
	 * @���� ��ҵ��ģ�͹���������豸����ʾ����
	 * @param root
	 */
	private void showRelatedMess(DefaultMutableTreeNode root) {
		if (dialog == null) {
			initRelateDialog(root);
		} else {
			treeModel.setRoot(root);
			dialog.setVisible(true);
			dialog.setSize(260, 300);
		}
	}

	/**
	 * 2009-5-4 Add by ZHM
	 * 
	 * @���� ҵ��ģ�͹�������
	 * @param isRelated
	 */
	private void okAction() {
		if (relating.isRelated()) {
			// *****************
			// �޸�svg�ļ��е�dom
			// *****************
			relating.modifySvgDom();

			// **********************
			// �޸ı༭�����豸���ڵ�����
			// **********************
			relating.modifyMapTree();

			SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
			editor.getPropertyModelInteractor().getGraphModel().initTreeModel(handle);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.BusinessModuleAdapter#draw(int, int,
	 *      org.apache.batik.ext.awt.geom.ExtendedGeneralPath,
	 *      java.awt.geom.Point2D, java.awt.geom.Point2D, int)
	 */
	@Override
	public StoreData draw(int index, int actionType, ExtendedGeneralPath path,
			Point2D beginPoint, Point2D endPoint, int modifier) {
		// TODO Auto-generated method stub
		// add by yux,2009-4-2
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.BusinessModuleAdapter#initHandle(int,
	 *      fr.itris.glips.svgeditor.display.handle.SVGHandle)
	 */
	@Override
	protected void initHandle(int index, SVGHandle handle) {
		// TODO Auto-generated method stub
		// add by yux,2009-4-2

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.BusinessModuleAdapter#setMenuItemInfo()
	 */
	@Override
	protected void setMenuItemInfo() {
		addMenu("���ݹ���");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.module.BusinessModuleAdapter#verifyValidity(int,
	 *      fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set)
	 */
	@Override
	protected boolean verifyValidity(int index, SVGHandle handle,
			Set<Element> selectedElements) {
		if (handle == null || handle.getCanvas().getFileType() == null
				|| !handle.getCanvas().getFileType().equals("district"))
			return false;
		return true;
	}

}
