package fr.itris.glips.svgeditor.display.canvas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import libraries.URIEncoderDecoder;

import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.module.GraphUnitModuleAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class of the internal frame containing the svg canvas
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGFrame extends JInternalFrame {

	/**
	 * the svg handle associated to this frame
	 */
	private SVGHandle svgHandle;

	/**
	 * the menuitem associated with the frame in the menu bar
	 */
	private JRadioButtonMenuItem menuitem = new JRadioButtonMenuItem();

	/**
	 * the panel containing the svg scrollpane and other widgets
	 */
	private JPanel contentPanel = new JPanel();

	/**
	 * the svg scrollpane
	 */
	private SVGScrollPane scrollpane;

	// /**
	// * the additional svg scrollpane,it is used for graphunit
	// * management.用于图元管理的panel，同一设备需要两种状态
	// */
	// private SVGScrollPane scrollpane_addtional;
	//	
	// private SVGHandle svgHandle_addtional;

	/**
	 * the state bar
	 */
	private CanvasStateBar stateBar;

	/**
	 * the list of the runnables enabling to dispose the frame
	 */
	private LinkedList<Runnable> disposeRunnables = new LinkedList<Runnable>();

	// private MDIView view = new MDIView(Editor.mdi);
	/**
	 * 
	 * the constructor of the class
	 * 
	 * @param svgHandle
	 *            the svg handle associated to this frame
	 */
	public SVGFrame(SVGHandle svgHandle) {

		super("", true, svgHandle.getEditor().getModeManager()
				.isSVGFrameCloseable(), true, true);
		// super(Editor.mdi,new MDIView(Editor.mdi));
		this.svgHandle = svgHandle;
		// modified by wangql, it was "build()"only bellow.
		if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD) {
			svgHandle.getEditor().getLogger().log(null, LoggerAdapter.INFO,
					"从SVG3.0开始，对该类型图元管理不予支持");
		} else {
			build();
		}

	}

	/**
	 * builds the internal frame content pane
	 */
	protected void build() {

		// handlling the properties of the internal frame//

		// setting the icon
		final ImageIcon editorIcon = ResourcesManager.getIcon("EditorInner",
				false);
		setFrameIcon(editorIcon);

		// setting the location of the frame
		Rectangle bounds = svgHandle.getEditor().getPreferredWidgetBounds(
				"frame");
		int nb = svgHandle.getEditor().getHandlesManager().getHandlesNumber();
		int offset = 30;
		int beginX = 75;
		int beginY = 0;

		if (bounds != null) {

			beginX = bounds.x;
			beginY = bounds.y;
		}

		setLocation(beginX + nb * offset, beginY + nb * offset);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// creating and adding an internal frame listener
		final InternalFrameListener internalFrameListener = new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {

				svgHandle.close();
			}
		};

		addInternalFrameListener(internalFrameListener);

		// creating and adding the listener to the focus changes
		final VetoableChangeListener vetoableChangeListener = new VetoableChangeListener() {

			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {

				if (evt.getPropertyName() != null
						&& evt.getPropertyName().equals("selected")
						&& svgHandle.getEditor().getHandlesManager()
								.getCurrentHandle() != svgHandle
						&& ((Boolean) evt.getNewValue()).booleanValue()) {

					svgHandle.getEditor().getHandlesManager().setCurrentHandle(
							svgHandle.getName());
				}
			}
		};

		addVetoableChangeListener(vetoableChangeListener);

		// creating and handling the properties of the inner widgets of the
		// internal frame//

		// handling the properties of the content pane
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(3, 3, 3, 3));

		// creating and adding the state bar
		stateBar = new CanvasStateBar();
		contentPanel.add(stateBar, BorderLayout.SOUTH);

		// creating and adding the scrollpane
		scrollpane = new SVGScrollPane(svgHandle);
		contentPanel.add(scrollpane, BorderLayout.CENTER);

		// adding a dispose runnable
		disposeRunnables.add(new Runnable() {

			public void run() {

				removeInternalFrameListener(internalFrameListener);
				removeVetoableChangeListener(vetoableChangeListener);
			}
		});
	}
	
	/**
	 * displays this frame
	 * 
	 * @param canvasSize
	 *            the canvas size
	 */
	public void displayFrame(Dimension canvasSize) {

		if (canvasSize != null) {

			// adds the internal frame to the desktop panel contained in the
			// main frame
			svgHandle.getEditor().getDesktop().add(this);
			svgHandle.getEditor().getDesktop().setLayer(this, 0, 0);
			getContentPane().add(contentPanel);
			this.updateUI();
			svgHandle.getEditor().getDesktop().repaint();
			// packing
			pack();

			// computing the available space for the frame
			final Dimension availableSpace = new Dimension(svgHandle
					.getEditor().getDesktop().getWidth()
					- getX(), svgHandle.getEditor().getDesktop().getHeight()
					- getY());

			// computing the size of the frame so that it does
			// not extend outside the desktop pane
			Dimension currentSize = getSize();
			Dimension newSize = new Dimension(currentSize);

			if (currentSize.width > availableSpace.width) {

				newSize.width = availableSpace.width;
			}

			if (currentSize.height > availableSpace.height) {

				newSize.height = availableSpace.height;
			}

			// setting the new size and repacking
			setPreferredSize(newSize);
			pack();
			try {
				setMaximum(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			setVisible(true);
			svgHandle.getEditor().getHandlesManager()
					.getRulersParametersHandler().setRulersEnabled(
							svgHandle.getEditor().getHandlesManager()
									.getRulersParametersHandler()
									.areRulersEnabled());
		}
	}

	/**
	 * disposes this frame
	 */
	public void disposeFrame() {

		// executing the dispose runnables
		for (Runnable runnable : disposeRunnables) {

			runnable.run();
		}

		contentPanel.removeAll();
		stateBar.removeAll();
		scrollpane.dispose();

		removeAll();
		dispose();

		// removing all the listeners registered on the menuitem
		ActionListener[] listeners = menuitem.getActionListeners();

		if (listeners != null) {

			for (ActionListener listener : listeners) {

				menuitem.removeActionListener(listener);
			}
		}

		svgHandle = null;
		menuitem = null;
		contentPanel = null;
		scrollpane = null;
		stateBar = null;
		disposeRunnables = null;
	}

	/**
	 * sets the label of the internal frame
	 * 
	 * @param name
	 *            the string associated with the internal frame
	 */
	public void setSVGFrameLabel(String name) {

		String label = "";

		try {
			File file = new File(name);
			label = file.toURI().toASCIIString();

			int pos = label.lastIndexOf("/");

			if (pos != -1) {

				label = label.substring(pos + 1, label.length());
			}

			label = URIEncoderDecoder.decode(label);
			label = new String(label.getBytes(Charset.defaultCharset().name()));
		} catch (Exception ex) {
		}

		if (label.equals("") && name != null && !name.equals("")) {

			label = name;
		}

		// added by wangql,主要是对图元管理时的显示名称略加修改
		if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD
				|| svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE
				|| svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {
			// if (!label.equals(ResourcesManager.bundle
			// .getString(NCIGraphUnitModule.idGraphUnitManage)))
			// label = ResourcesManager.bundle
			// .getString(NCIGraphUnitModule.idGraphUnitManage)
			// + ": " + label.trim();
			if (svgHandle.getSymbolBean() == null) {
				String tempate = "";
				if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
					tempate = "(模板)";
				}
				label = ResourcesManager.bundle
						.getString(GraphUnitModuleAdapter.idGraphUnitManage)
						+ tempate + ":  " + label.trim();
			} else {
				label = ResourcesManager.bundle
						.getString(GraphUnitModuleAdapter.idGraphUnitManage)
						+ ":  "
						+ svgHandle.getSymbolBean().getVariety().getName()
						+ "/" + svgHandle.getSymbolBean().getName();
			}
		}

		if (svgHandle.isModified()) {

			label = label.concat("*");
		}
		// added end
		menuitem.setText(label);
		setTitle(label);
	}

	public void setSvgHandle(SVGHandle handle) {
		this.svgHandle = handle;
	}

	/**
	 * deselects this internal frame
	 */
	public void moveFrameToBack() {

		try {
			setSelected(false);
		} catch (Exception ex) {
		}
	}

	/**
	 * shows this frame in the main frame
	 */
	public void moveFrameToFront() {

		try {
			setSelected(true);
		} catch (Exception ex) {
		}
	}

	/**
	 * removes this internal from the desktop
	 */
	public void removeFromDesktop() {

		svgHandle.getEditor().getDesktop().remove(this);
		svgHandle.getEditor().getDesktop().repaint();
	}

	/**
	 * @return the menu item that will be displayed in the menu bar to switch
	 *         from one SVG picture to another
	 */
	public JMenuItem getFrameMenuItem() {
		return menuitem;
	}

	/**
	 * @return the svg scrollpane that is displayed in this internal frame
	 */
	public SVGScrollPane getScrollpane() {
		return scrollpane;
	}

	/**
	 * @return the svg canvas that is displayed in this internal frame
	 */
	public SVGCanvas getCanvas() {

		return scrollpane.getSVGCanvas();
	}

	/**
	 * @return the state bar
	 */
	public CanvasStateBar getStateBar() {
		return stateBar;
	}
}
