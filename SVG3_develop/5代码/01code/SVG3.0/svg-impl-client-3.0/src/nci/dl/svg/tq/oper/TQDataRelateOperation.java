package nci.dl.svg.tq.oper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.w3c.dom.Element;

import com.nci.svg.district.DistrictTopology;
import com.nci.svg.district.relate.DistrictReadData;
import com.nci.svg.district.relate.DistrictRelating;
import com.nci.svg.district.relate.bean.DistrictAreaBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.communication.ActionNames;

import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.oper.common.TQOperation;

public class TQDataRelateOperation extends TQOperation{
	private EditorAdapter editor = tqModule.getEditor();

	public TQDataRelateOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		return;		
	}
	
	public void doMenuAction() {
//		tqModule.getEditor().getHandlesManager().getCurrentHandle()
//				.getSelection().setSelectionFilter(selectionFilter);
		doAction();
	}
	private void doAction() {
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
			dialog.setSize(260, 300);
			treeModel.setRoot(root);
			dialog.setVisible(true);
//			dialog.pack();
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


}
