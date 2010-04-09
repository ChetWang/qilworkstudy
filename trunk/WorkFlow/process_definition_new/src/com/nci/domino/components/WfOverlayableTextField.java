package com.nci.domino.components;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.jidesoft.swing.OverlayTextField;
import com.jidesoft.swing.OverlayableIconsFactory;
import com.jidesoft.swing.OverlayableUtils;

/**
 * �����������ݵ�TextField
 * 
 * @author Qil.Wong
 * 
 */
public class WfOverlayableTextField extends OverlayTextField {

	private static final long serialVersionUID = 4034534417266340377L;

	private WfOverlayable overlayableTextField;

	public WfOverlayableTextField(Document doc) {
		super();
		init(doc);
	}

	public void init(Document doc) {
		setDocument(doc);
		getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				check();
			}

			public void insertUpdate(DocumentEvent e) {
				check();
			}

			public void changedUpdate(DocumentEvent e) {
				check();
			}
		});
		JLabel attentionIcon = new JLabel(OverlayableUtils
				.getPredefinedOverlayIcon(OverlayableIconsFactory.ATTENTION));
		overlayableTextField = new WfOverlayable(this, attentionIcon,
				WfOverlayable.NORTH_EAST);
	}

	private void check() {
		// ��ֹ�Ի���resize����overlay���λ���Ծɱ��ֲ���
		overlayableTextField.componentResized(null);
		overlayableTextField.setOverlayVisible(getText().trim().equals(""));
	}

	/**
	 * ��ȡ���帡���������������ͼ���TextField
	 * 
	 * @return
	 */
	public WfOverlayable getOverlayableTextField() {
		return overlayableTextField;
	}

}
