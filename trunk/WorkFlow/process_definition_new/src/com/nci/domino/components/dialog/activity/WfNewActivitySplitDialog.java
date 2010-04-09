package com.nci.domino.components.dialog.activity;

import java.io.Serializable;

import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;

/**
 * 分支属性对话框
 * @author Qil.Wong
 *
 */
public class WfNewActivitySplitDialog extends WfNewActivityJoinSplit {

	public WfNewActivitySplitDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
	}

	public void showWfDialog(int width, int height, Serializable defaultValue) {
		getBannerPanel().setTitle("分支活动");
		getBannerPanel().setSubtitle("分支活动是——DongXF补充");
		basic.joinTypeLabel.setText("分支类型：");
		basic.joinConditionLabel.setText("分支条件：");
		super.showWfDialog(width, height, defaultValue);
	}

	@Override
	public String getConditionID(WofoActivityBean activity) {
		return activity.getSplitConditionId() == null ? "" : activity
				.getSplitConditionId();
	}

	@Override
	public String getCurrentNeedAndMode() {
		return WofoActivityBean.SPLIT_MODE_AND;
	}

	@Override
	public String getCurrentNeedConditionMode() {
		return WofoActivityBean.SPLIT_MODE_CONDITION;
	}

	@Override
	public String getCurrentNeedOrMode() {
		return WofoActivityBean.SPLIT_MODE_OR;
	}

	@Override
	public String getJoinSplitMode(WofoActivityBean activity) {
		return activity.getSplitMode();
	}

	@Override
	public void setConditionID(String conditionID, WofoActivityBean activity) {
		activity.setSplitConditionId(conditionID);
	}

	@Override
	public void setJoinSplitMode(WofoActivityBean activity, String mode) {
		activity.setSplitMode(mode);
	}

}
