package com.nci.svg.sdk.client.selection;

public abstract class ModeSelectionListener {

	public boolean isMousePressedFire = false;
	public boolean isMouseReleasedFire = false;
	public boolean isMouseWheelFire = false;
	public boolean isMouseDragedFire = false;
	public boolean isMouseMoveFire = false;

	
	public abstract void modeSelected(ModeSelectionEvent evt);
	//ͨ��((ModeObject)evt.getSource()).getMouseEvent().getID()���жϾ������궯��
	//���ֶ���MouseWheelEventҲ����

}
