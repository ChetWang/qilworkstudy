package com.nci.svg.sdk.client.selection;

public abstract class ModeSelectionListener {

	public boolean isMousePressedFire = false;
	public boolean isMouseReleasedFire = false;
	public boolean isMouseWheelFire = false;
	public boolean isMouseDragedFire = false;
	public boolean isMouseMoveFire = false;

	
	public abstract void modeSelected(ModeSelectionEvent evt);
	//通过((ModeObject)evt.getSource()).getMouseEvent().getID()来判断具体的鼠标动作
	//滚轮动作MouseWheelEvent也类似

}
