package com.nci.svg.sdk.client.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import com.jidesoft.swing.JideSplitButton;
import com.nci.svg.sdk.ui.graphunit.NCIThumbnailPanel;

public class NCISymbolSession {

	private NCIThumbnailPanel selectedThumbnail = null;

	private ButtonGroup bg = null;
	
	private String[] pathDirectDrawNames = {"����"};

	public NCISymbolSession() {
	}
	
	/**
	 * �Ƿ���ֱ�ӻ��ߣ�������ͼԪ
	 * @param symbolName
	 * @return
	 */
	public boolean isDirectDrawPath(String symbolName){
		for(String s:pathDirectDrawNames){
			if(symbolName.equals(s)){
				return true;
			}
		}
		return false;
	}

	/**
	 * ����ѡ�������ͼԪ
	 * 
	 * @param thumbnail
	 */
	public void setSelectedThumbnail(NCIThumbnailPanel thumbnail) {
		clearSelectedBtnGroup();
		if (thumbnail == null) {
			throw new NullPointerException("Thumbnail could not be null!");
		}
		if (selectedThumbnail != null)
			selectedThumbnail.setBorder(new BevelBorder(BevelBorder.RAISED));
		selectedThumbnail = thumbnail;
		selectedThumbnail.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * ��յ�ǰѡ��Ĳ�����ť
	 */
	public void clearSelectedBtnGroup() {
		Enumeration<AbstractButton> enu = bg.getElements();
		while (enu.hasMoreElements()) {
			AbstractButton jtBtn = enu.nextElement();
			if (bg.isSelected(jtBtn.getModel())) {
				bg.remove(jtBtn);
				jtBtn.setSelected(false);
				bg.add(jtBtn);
			}
			if (jtBtn instanceof JideSplitButton) {
				((JideSplitButton) jtBtn).setButtonSelected(false);
			}
		}
		clearSelectedThumbnail();
	}

	/**
	 * ���ѡ�������ͼԪ
	 */
	public void clearSelectedThumbnail() {
		if (selectedThumbnail != null) {
			selectedThumbnail.setBorder(new BevelBorder(BevelBorder.RAISED));
		}
		selectedThumbnail = null;
	}

	/**
	 * ��ȡѡ�������ͼԪ
	 * 
	 * @return ѡ�������ͼԪ
	 */
	public NCIThumbnailPanel getSelectedThumbnail() {
		return selectedThumbnail;
	}

	/**
	 * ���ù�������ѡ�񼰻���ButtonGroup������
	 * 
	 * @param bg1
	 */
	public void setToolbarGraphUnitButtonGroup(ButtonGroup bg1) {
		bg = bg1;
		/**
		 * �Ƿ��Ѿ���ͼԪ�ͻ�ͼ��ť����������һ���й������򹤾�����ͼ��ť�����ͼԪ�����ڲ�ѡ��״̬
		 */
		Enumeration<AbstractButton> enu = bg.getElements();
		while (enu.hasMoreElements()) {
			final AbstractButton jtBtn = enu.nextElement();
			if (jtBtn instanceof JideSplitButton == false) {
				jtBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						clearSelectedThumbnail();
						Enumeration<AbstractButton> enu2 = bg.getElements();
						while (enu2.hasMoreElements()) {
							AbstractButton jtBtn2 = enu2.nextElement();
							if (jtBtn2 instanceof JideSplitButton
									&& jtBtn != jtBtn2) {
								((JideSplitButton) jtBtn2)
										.setButtonSelected(false);
							}
						}
					}
				});
			}
		}
	}

	/**
	 * ���ع������ĵ�һ��ͼԪButtonGroup
	 * 
	 * @return
	 */
	public ButtonGroup getToolBarButtonGroup() {
		return bg;
	}

}
