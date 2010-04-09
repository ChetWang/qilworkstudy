package com.nci.domino.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.jidesoft.swing.DefaultOverlayable;

/**
 * 自定义的Overlayable，解决在Overlayable隐藏状态下，窗口resize后不能在正确位置显示的bug
 * @author Qil.Wong
 *
 */
public class WfOverlayable extends DefaultOverlayable {

	public WfOverlayable() {
		super();
	}

	public WfOverlayable(JComponent component) {
		super();
		setActualComponent(component);
	}

	public WfOverlayable(JComponent actualComponent,
			JComponent overlayComponent, int overlayLocation) {
		super();
		setActualComponent(actualComponent);
		addOverlayComponent(overlayComponent, overlayLocation);
	}

	public WfOverlayable(JComponent actualComponent, JComponent overlayComponent) {
		super();
		setActualComponent(actualComponent);
		addOverlayComponent(overlayComponent, SwingConstants.CENTER);
	}

	protected Rectangle getOverlayComponentBounds(JComponent component) {
		Component relativeComponent = getActualComponent();

		Rectangle bounds = relativeComponent.getBounds();
		if (relativeComponent != getActualComponent()) {
			bounds = SwingUtilities.convertRectangle(relativeComponent
					.getParent(), bounds, getActualComponent());
		}
		Rectangle overlayBounds = new Rectangle(bounds);
		Insets insets = getOverlayLocationInsets();
		overlayBounds.x -= insets.left;
		overlayBounds.y -= insets.top;
		overlayBounds.width += insets.left + insets.right;
		overlayBounds.height += insets.top + insets.bottom;

		int cx = 0;
		int cy = 0;

		Dimension size = component.getPreferredSize();
		int cw = size.width;
		int ch = size.height;

		switch (getOverlayLocation(component)) {
		case CENTER:
			cx = bounds.x + (bounds.width - cw) / 2;
			cy = bounds.y + (bounds.height - ch) / 2;
			break;
		case NORTH:
			cx = bounds.x + (bounds.width - cw) / 2;
			cy = overlayBounds.y;
			break;
		case SOUTH:
			cx = bounds.x + (bounds.width - cw) / 2;
			cy = overlayBounds.y + overlayBounds.height - ch;
			break;
		case WEST:
			cx = overlayBounds.x;
			cy = bounds.y + (bounds.height - ch) / 2;
			break;
		case EAST:
			cx = overlayBounds.x + overlayBounds.width - cw;
			cy = bounds.y + (bounds.height - ch) / 2;
			break;
		case NORTH_WEST:
			cx = overlayBounds.x;
			cy = overlayBounds.y;
			break;
		case NORTH_EAST:
			cx = overlayBounds.x + overlayBounds.width - cw;
			cy = overlayBounds.y;
			break;
		case SOUTH_WEST:
			cx = overlayBounds.x;
			cy = overlayBounds.y + overlayBounds.height - ch;
			break;
		case SOUTH_EAST:
			cx = overlayBounds.x + overlayBounds.width - cw;
			cy = overlayBounds.y + overlayBounds.height - ch;
			break;
		}

		return new Rectangle(cx, cy, cw, ch);
	}
	
	public void componentResized(ComponentEvent e) {
		JComponent[] components = getOverlayComponents();
		for (JComponent c : components) {
			if (c == null) {
				return;
			}
			Rectangle r = getOverlayComponentBounds(c);
			c.setBounds(r);
		}
	}

}
