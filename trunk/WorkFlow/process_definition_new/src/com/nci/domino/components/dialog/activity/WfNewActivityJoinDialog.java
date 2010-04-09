package com.nci.domino.components.dialog.activity;

import java.io.Serializable;

import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;
/**
 * 聚合属性对话框
 * @author Qil.Wong
 *
 */
public class WfNewActivityJoinDialog extends WfNewActivityJoinSplit {

	public WfNewActivityJoinDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
	}
	

	public void showWfDialog(int width, int height, Serializable defaultValue) {
		getBannerPanel().setTitle("聚合活动");
		getBannerPanel().setSubtitle("聚合活动是——DongXF补充");
		basic.joinTypeLabel.setText("聚合类型：");
		basic.joinConditionLabel.setText("聚合条件：");
		super.showWfDialog(width, height, defaultValue);
	}

	@Override
	public String getConditionID(WofoActivityBean activity) {
		return activity.getJoinConditionId()==null?"":activity.getJoinConditionId();
	}

	@Override
	public String getCurrentNeedAndMode() {
		return WofoActivityBean.JOIN_MODE_AND;
	}

	@Override
	public String getCurrentNeedConditionMode() {
		return WofoActivityBean.JOIN_MODE_CONDITION;
	}

	@Override
	public String getCurrentNeedOrMode() {
		return WofoActivityBean.JOIN_MODE_OR;
	}

	@Override
	public String getJoinSplitMode(WofoActivityBean activity) {
		return activity.getJoinMode();
	}

	@Override
	public void setConditionID(String conditionID, WofoActivityBean activity) {
		activity.setJoinConditionId(conditionID);
	}

	@Override
	public void setJoinSplitMode(WofoActivityBean activity, String mode) {
		activity.setJoinMode(mode);
	}

}
