/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-2-3
 * @���ܣ���ɫѡ����
 *
 */
package com.nci.svg.sdk.graphmanager.property.editor;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.PercentLayout;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.graphmanager.property.NciGraphProperty;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author yx.nci
 * 
 */
public class ColorChoosePropertyEditor extends AbstractPropertyEditor {
	/**
	 * add by yux,2009-2-4
	 * ��ʷ��ɫ��ť
	 */
	private JButton previewColor = null;
	/**
	 * add by yux,2009-2-4
	 * ��ǰ��ɫ��ʾ����
	 */
	private JPanel colorPanel = null;
	/**
	 * add by yux,2009-2-4
	 * ����ȡɫ��ť
	 */
	private JToggleButton colorPickerButton = null;
	/**
	 * add by yux,2009-2-4
	 * ��ǰ��ɫ
	 */
	private Color currentColor = null;
	/**
	 * add by yux,2009-2-4
	 *
	 */
	private EditorAdapter nciEditor = null;

	public ColorChoosePropertyEditor(final EditorAdapter nciEditor,
			final NciGraphProperty property, final Set<Element> elements, Color color) {
		super();
		this.nciEditor = nciEditor;
		//������˫��ť�Ļ���
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0)) {
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				previewColor.setEnabled(enabled);
				colorPanel.setEnabled(enabled);
				colorPickerButton.setEnabled(enabled);
			}

		};
		// the icons
		final ImageIcon colorPickerIcon = ResourcesManager.getIcon(
				"ColorPickerSmall", false), colorPickerDisabledIcon = ResourcesManager
				.getIcon("ColorPickerSmall", true);
		String labelColorPicker = ResourcesManager.bundle
				.getString("labelcolorpicker");
		// the button used to preview the color
		previewColor = new JButton();
		Insets buttonInsets = new Insets(0, 0, 0, 0);
		previewColor.setMargin(buttonInsets);
		previewColor.setPreferredSize(new Dimension(22, 22));

		colorPanel = new JPanel();
		colorPanel.setPreferredSize(new Dimension(18, 18));

		if (color != null) {
			currentColor = color;
		} else {
			currentColor = Color.black;
		}
		//���õ�ǰ��ɫ
		colorPanel.setBackground(currentColor);
		colorPanel.setBorder(new LineBorder(Color.white, 1));
		previewColor.add(colorPanel);

		//��������ȡɫ��ť
		colorPickerButton = new JToggleButton(colorPickerIcon);
		colorPickerButton.setMargin(new Insets(0, 0, 0, 0));
		colorPickerButton.setToolTipText(labelColorPicker);
		colorPickerButton.setPreferredSize(new Dimension(22, 22));
		((JPanel) editor).add(previewColor);
		previewColor.setOpaque(false);
		((JPanel) editor).add(colorPickerButton);
		colorPickerButton.setOpaque(false);
		//����ǰ��ɫ��ť������
		final ActionListener previewColorListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Color col = nciEditor.getColorChooser().showColorChooserDialog(
						currentColor == null ? Color.BLACK : currentColor);

				if (col != null) {

					String scl = nciEditor.getColorChooser()
							.getColorString(col);

					colorPanel.setBackground(col);
					currentColor = col;
					nciEditor.getSVGColorManager().setCurrentColor(col);
					if (property != null) {
						property.writeToObject(elements);
					}
				}
			}
		};

		previewColor.addActionListener(previewColorListener);
		//������ȡɫ��ť������
		final ActionListener colorPickerListener = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				nciEditor.getSelectionManager().setToNoneMode();

				// the listener to the events in the application
				AWTEventListener listener = new AWTEventListener() {

					public void eventDispatched(AWTEvent event) {

						if (event instanceof MouseEvent) {

							MouseEvent mevt = (MouseEvent) event;
							mevt.consume();

							Point point = mevt.getPoint();

							if (mevt.getID() == MouseEvent.MOUSE_PRESSED) {

								// converting the point
								SwingUtilities.convertPointToScreen(point,
										(Component) mevt.getSource());

								// getting the color at the clicked point
								Color col = nciEditor.getSVGToolkit()
										.pickColor(point);

								if (col != null) {

									// setting the new color value
									String scl = nciEditor.getColorChooser()
											.getColorString(col);
									colorPanel.setBackground(col);
									nciEditor.getSVGColorManager()
											.setCurrentColor(col);
									if (property != null) {
										property.writeToObject(elements);
									}
								}

							} else if (mevt.getID() == MouseEvent.MOUSE_RELEASED) {

								// remove this listener and set the default
								// state of the editor
								Toolkit.getDefaultToolkit()
										.removeAWTEventListener(this);
								colorPickerButton.setSelected(false);
								nciEditor.getSelectionManager()
										.setToRegularMode();
							}
						}
					}
				};

				// adding the listener
				Toolkit.getDefaultToolkit().addAWTEventListener(listener,
						AWTEvent.MOUSE_EVENT_MASK);
			}
		};

		// adds the listener to the colorPicker button
		colorPickerButton.addActionListener(colorPickerListener);
		((JPanel) editor).setOpaque(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#getValue()
	 */
	@Override
	public Object getValue() {
		String value = nciEditor.getColorChooser().getColorString(currentColor);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		//���������ɫ�ַ�������ת����������ʾ�ڵ�չʾ
		currentColor = nciEditor.getColorChooser().getColor(nciEditor.getHandlesManager().getCurrentHandle(), (String)value);
		colorPanel.setBackground(currentColor);
		nciEditor.getSVGColorManager().setCurrentColor(currentColor);
	}

}
