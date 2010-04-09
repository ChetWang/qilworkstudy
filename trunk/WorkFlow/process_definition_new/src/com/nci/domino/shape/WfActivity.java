package com.nci.domino.shape;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JOptionPane;

import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.components.dialog.DialogManager;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.activity.WfActivityDialog;
import com.nci.domino.components.dialog.activity.WfNewActivityAutoDialog;
import com.nci.domino.components.dialog.activity.WfNewActivityHumanDialog;
import com.nci.domino.components.dialog.activity.WfNewActivityJoinDialog;
import com.nci.domino.components.dialog.activity.WfNewActivitySplitDialog;
import com.nci.domino.components.dialog.activity.WfNewActivityStartEndDialog;
import com.nci.domino.components.dialog.activity.WfNewActivitySubflowDialog;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.shape.activity.topicon.WfActivityIconSituation;
import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * ���̻�� ��
 * 
 * @author Qil.Wong
 */
public class WfActivity extends WfActivityBasic {

	/**
	 * @param x
	 *            ��������x
	 * @param y
	 *            ��������y
	 * @param activityType
	 *            �����
	 */
	public WfActivity(double x, double y, String activityType) {
		// ����Գ�ʼ��
		super(x, y, activityType);
	}

	public void updatePaste(PaintBoardBasic b) {
		PaintBoard board = (PaintBoard) b;
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		activityBean.setActivityId(Functions.getUID());
		activityBean.setProcessCode(board.getProcessBean().getProcessCode());
		activityBean.setProcessId(board.getProcessBean().getProcessId());
		activityBean.setProcessMasterId(board.getProcessBean()
				.getProcessMasterId());
		activityBean.setCreatorId(board.getEditor().getUserID());
		// activityBean.setPackageId(board.getProcessBean().getPackageId());

	}

	public int showInput(PaintBoardBasic board) {
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		String activityType = activityBean.getActivityType();
		int result = WfDialog.RESULT_CANCELLED;
		DialogManager manager = ((PaintBoard) board).getEditor()
				.getDialogManager();
		WfDialog dialog = null;
		if (WofoActivityBean.ACTIVITY_TYPE_AUTO.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityAutoDialog.class, "�Զ����ڶ���",
					true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_BEGIN.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityStartEndDialog.class,
					"��ʼ���ڶ���", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_END.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityStartEndDialog.class,
					"�������ڶ���", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_HUMAN.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityHumanDialog.class,
					"ҵ�񻷽ڶ���", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_JOIN.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityJoinDialog.class, "�ۺϻ��ڶ���",
					true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_NOTIFY.equals(activityType)) {

		} else if (WofoActivityBean.ACTIVITY_TYPE_SPLIT.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivitySplitDialog.class,
					"��֧���ڶ���", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_SUBFLOW.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivitySubflowDialog.class,
					"�����̻��ڶ���", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_WAIT.equals(activityType)) {

		}
		if (dialog != null) {
			dialog.showWfDialog(activityBean);
			result = dialog.getDialogResult();
			if (result == WfDialog.RESULT_AFFIRMED) {
				activityBean = (WofoActivityBean) dialog.getInputValues();
			}
		}
		dialog.getEditor().getOperationArea().getActivityArea()
				.treeSelectionChanged(null);
		return result;
	}

	@Override
	public void mouseDoubleClicked(MouseEvent e, PaintBoardBasic board) {
		int toolMode = board.getToolMode();
		if (toolMode == ToolMode.TOOL_SELECT_OR_DRAG) {
			// ��������Ͻ�ͼ��ѡ�ϣ��򴥷����Ͻǵ���¼��������Ǹ�demo
			if (topRightUnderMouse) {
				WfActivityIconSituation situatioin = PaintBoardBasic.topRightConfig
						.get(topRightSituations[topRightUnderMouseIndex]);
				// ȡ���������������Ϊ��ʱ����ȥ�������������ˣ�����ͼ��һֱ�����ͼ�껹�������
				topRightUnderMouseIndex = -1;
				situatioin.getIconAction().onClick(board, this);
			} else {
				setActivityProperty(board);
			}
		}
	}

	/**
	 * �����û���ԶԻ���
	 * 
	 * @param act
	 *            WfActivityʵ��
	 */
	public void setActivityProperty(PaintBoardBasic board) {
		Class<? extends WfActivityDialog> className = null;
		String title = "";
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		String activityType = activityBean.getActivityType();
		if (WofoActivityBean.ACTIVITY_TYPE_AUTO.equals(activityType)) {
			className = WfNewActivityAutoDialog.class;
			title = "�Զ������";
		} else if (WofoActivityBean.ACTIVITY_TYPE_BEGIN.equals(activityType)) {
			className = WfNewActivityStartEndDialog.class;
			title = "��ʼ�����";
		} else if (WofoActivityBean.ACTIVITY_TYPE_END.equals(activityType)) {
			className = WfNewActivityStartEndDialog.class;
			title = "���������";
		} else if (WofoActivityBean.ACTIVITY_TYPE_HUMAN.equals(activityType)) {
			className = WfNewActivityHumanDialog.class;
			title = "�˹������";
		} else if (WofoActivityBean.ACTIVITY_TYPE_JOIN.equals(activityType)) {
			className = WfNewActivityJoinDialog.class;
			title = "�ۺϻ����";
		} else if (WofoActivityBean.ACTIVITY_TYPE_NOTIFY.equals(activityType)) {

		} else if (WofoActivityBean.ACTIVITY_TYPE_SPLIT.equals(activityType)) {
			className = WfNewActivitySplitDialog.class;
			title = "��֧�����";
		} else if (WofoActivityBean.ACTIVITY_TYPE_SUBFLOW.equals(activityType)) {
			className = WfNewActivitySubflowDialog.class;
			title = "�����̻����";
		} else if (WofoActivityBean.ACTIVITY_TYPE_WAIT.equals(activityType)) {

		}
		WfDialog dialog = ((PaintBoard) board).getEditor().getDialogManager()
				.getDialog(className, title, true);
		dialog.showWfDialog(activityBean);
		if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			setWofoBean((WofoActivityBean) dialog.getInputValues());
		}
		dialog.getEditor().getOperationArea().getActivityArea()
				.treeSelectionChanged(null);
	}

	@Override
	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
		super.mouseMoved(e, board);
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			Rectangle2D rect = mainArea.getBounds2D();
			double fbl = 5 / board.getTrans()[0]; // �ֱ���
			resizable = rect.contains(board.getWxNew(), board.getWyNew())
					&& (rect.getX() + rect.getWidth() - board.getWxNew() <= fbl || rect
							.getY()
							+ rect.getHeight() - board.getWyNew() <= fbl);
			if (resizable) {
				board.setCursor(Cursor
						.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			} else {
				board.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void mouseDragged(MouseEvent e, PaintBoardBasic board) {
		if (!resizable) {
			super.mouseDragged(e, board);
		} else {
			board.setSelectedShape(this);
			// Activity��X��Yֵ����ͼ�ε�����
			double l = mainArea.getBounds2D().getX();
			double t = mainArea.getBounds2D().getY();
			if (board.getWxNew() - l < MIN_WIDTH / board.getTrans()[0]) {
				width = MIN_WIDTH;
			} else {
				width = board.getWxNew() - l;
			}
			if (board.getWyNew() - t < MIN_HEIGHT / board.getTrans()[0]) {
				height = MIN_HEIGHT;
			} else {
				height = board.getWyNew() - t;
			}
		}
	}

	public void mouseReleased(MouseEvent e, PaintBoardBasic board) {
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		activityBean.setPosX(x);
		activityBean.setPosY(y);
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		activityBean.setPosX(x);
		activityBean.setPosY(y);
		activityBean.setWidth(width);
		activityBean.setHeight(height);
		shapeBeans.add(activityBean);
	}

	/**
	 * ͼ��ɾ����Ķ���, ���������Ҫ�и���
	 * 
	 * @param board
	 */
	public void removeShape(PaintBoardBasic boardBasic) {
		boardBasic.getGraphVector().remove(this);
		PaintBoard board = (PaintBoard) boardBasic;
		// ģ��һ�Σ�����ˢ������
		board.getEditor().getOperationArea().getActivityArea()
				.treeSelectionChanged(null);
	}

	/**
	 * �����ͼ�Σ�Ĭ��������������ͼ�ζ��е����һ��, ���������Ҫ�и���
	 * 
	 * @param board
	 */
	public void addShape(PaintBoardBasic boardBasic) {
		boardBasic.getGraphVector().add(this);
		PaintBoard board = (PaintBoard) boardBasic;
		// ģ��һ�Σ�����ˢ������
		board.getEditor().getOperationArea().getActivityArea()
				.treeSelectionChanged(null);
	}
}