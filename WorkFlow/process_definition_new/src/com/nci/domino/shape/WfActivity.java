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
 * 流程活动类 。
 * 
 * @author Qil.Wong
 */
public class WfActivity extends WfActivityBasic {

	/**
	 * @param x
	 *            中心坐标x
	 * @param y
	 *            中心坐标y
	 * @param activityType
	 *            活动类型
	 */
	public WfActivity(double x, double y, String activityType) {
		// 活动属性初始化
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
			dialog = manager.getDialog(WfNewActivityAutoDialog.class, "自动环节定义",
					true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_BEGIN.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityStartEndDialog.class,
					"开始环节定义", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_END.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityStartEndDialog.class,
					"结束环节定义", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_HUMAN.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityHumanDialog.class,
					"业务环节定义", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_JOIN.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivityJoinDialog.class, "聚合环节定义",
					true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_NOTIFY.equals(activityType)) {

		} else if (WofoActivityBean.ACTIVITY_TYPE_SPLIT.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivitySplitDialog.class,
					"分支环节定义", true);
		} else if (WofoActivityBean.ACTIVITY_TYPE_SUBFLOW.equals(activityType)) {
			dialog = manager.getDialog(WfNewActivitySubflowDialog.class,
					"子流程环节定义", true);
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
			// 如果是右上角图标选上，则触发右上角点击事件，这里是个demo
			if (topRightUnderMouse) {
				WfActivityIconSituation situatioin = PaintBoardBasic.topRightConfig
						.get(topRightSituations[topRightUnderMouseIndex]);
				// 取消掉鼠标索引，因为这时界面去处理其它任务了，否则活动图会一直任务该图标还在鼠标下
				topRightUnderMouseIndex = -1;
				situatioin.getIconAction().onClick(board, this);
			} else {
				setActivityProperty(board);
			}
		}
	}

	/**
	 * 打开设置活动属性对话框
	 * 
	 * @param act
	 *            WfActivity实例
	 */
	public void setActivityProperty(PaintBoardBasic board) {
		Class<? extends WfActivityDialog> className = null;
		String title = "";
		WofoActivityBean activityBean = (WofoActivityBean) wofoBean;
		String activityType = activityBean.getActivityType();
		if (WofoActivityBean.ACTIVITY_TYPE_AUTO.equals(activityType)) {
			className = WfNewActivityAutoDialog.class;
			title = "自动活动属性";
		} else if (WofoActivityBean.ACTIVITY_TYPE_BEGIN.equals(activityType)) {
			className = WfNewActivityStartEndDialog.class;
			title = "开始活动属性";
		} else if (WofoActivityBean.ACTIVITY_TYPE_END.equals(activityType)) {
			className = WfNewActivityStartEndDialog.class;
			title = "结束活动属性";
		} else if (WofoActivityBean.ACTIVITY_TYPE_HUMAN.equals(activityType)) {
			className = WfNewActivityHumanDialog.class;
			title = "人工活动属性";
		} else if (WofoActivityBean.ACTIVITY_TYPE_JOIN.equals(activityType)) {
			className = WfNewActivityJoinDialog.class;
			title = "聚合活动属性";
		} else if (WofoActivityBean.ACTIVITY_TYPE_NOTIFY.equals(activityType)) {

		} else if (WofoActivityBean.ACTIVITY_TYPE_SPLIT.equals(activityType)) {
			className = WfNewActivitySplitDialog.class;
			title = "分支活动属性";
		} else if (WofoActivityBean.ACTIVITY_TYPE_SUBFLOW.equals(activityType)) {
			className = WfNewActivitySubflowDialog.class;
			title = "子流程活动属性";
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
			double fbl = 5 / board.getTrans()[0]; // 分辨率
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
			// Activity的X，Y值是在图形的中央
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
	 * 图形删除后的动作, 活动操作区域要有更新
	 * 
	 * @param board
	 */
	public void removeShape(PaintBoardBasic boardBasic) {
		boardBasic.getGraphVector().remove(this);
		PaintBoard board = (PaintBoard) boardBasic;
		// 模拟一次，重新刷新数据
		board.getEditor().getOperationArea().getActivityArea()
				.treeSelectionChanged(null);
	}

	/**
	 * 新添加图形，默认是增加在已有图形队列的最后一个, 活动操作区域要有更新
	 * 
	 * @param board
	 */
	public void addShape(PaintBoardBasic boardBasic) {
		boardBasic.getGraphVector().add(this);
		PaintBoard board = (PaintBoard) boardBasic;
		// 模拟一次，重新刷新数据
		board.getEditor().getOperationArea().getActivityArea()
				.treeSelectionChanged(null);
	}
}