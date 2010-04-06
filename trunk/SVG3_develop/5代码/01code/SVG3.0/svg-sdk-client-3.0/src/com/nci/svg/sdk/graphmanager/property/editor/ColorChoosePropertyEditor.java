/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-2-3
 * @功能：颜色选择器
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
	 * 历史颜色按钮
	 */
	private JButton previewColor = null;
	/**
	 * add by yux,2009-2-4
	 * 当前颜色显示窗体
	 */
	private JPanel colorPanel = null;
	/**
	 * add by yux,2009-2-4
	 * 界面取色按钮
	 */
	private JToggleButton colorPickerButton = null;
	/**
	 * add by yux,2009-2-4
	 * 当前颜色
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
		//构建带双按钮的画板
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
		//设置当前颜色
		colorPanel.setBackground(currentColor);
		colorPanel.setBorder(new LineBorder(Color.white, 1));
		previewColor.add(colorPanel);

		//构建界面取色按钮
		colorPickerButton = new JToggleButton(colorPickerIcon);
		colorPickerButton.setMargin(new Insets(0, 0, 0, 0));
		colorPickerButton.setToolTipText(labelColorPicker);
		colorPickerButton.setPreferredSize(new Dimension(22, 22));
		((JPanel) editor).add(previewColor);
		previewColor.setOpaque(false);
		((JPanel) editor).add(colorPickerButton);
		colorPickerButton.setOpaque(false);
		//给当前颜色按钮赋监听
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
		//给界面取色按钮赋监听
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
		//将传入的颜色字符串进行转换，并在显示节点展示
		currentColor = nciEditor.getColorChooser().getColor(nciEditor.getHandlesManager().getCurrentHandle(), (String)value);
		colorPanel.setBackground(currentColor);
		nciEditor.getSVGColorManager().setCurrentColor(currentColor);
	}

}
