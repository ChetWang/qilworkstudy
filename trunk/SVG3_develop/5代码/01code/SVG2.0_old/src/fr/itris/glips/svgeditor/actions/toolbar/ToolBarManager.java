package fr.itris.glips.svgeditor.actions.toolbar;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.Module;
import fr.itris.glips.svgeditor.display.canvas.zoom.Zoom;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class handling the toolbars
 * 
 * @author Jordi SUC
 */
public class ToolBarManager {

	/**
	 * 编辑专用的toolbar
	 */
	private JToolBar toolsBar = new JToolBar();
	// /**
	// * 浏览专用的toolbar
	// */
	// private JToolBar toolsBar_view=new JToolBar();
	// /**
	// * 浏览和编辑两种状态下都能用的toolbar
	// */
	// private JToolBar toolsBar_ev=new JToolBar();
	/**
	 * toolbar面板
	 */
	private JPanel toolBarPanel = new JPanel();

	/**
	 * 缩放比例下拉框
	 */
	protected JComboBox combo;

	protected Set<JButton> ft_buttons = new HashSet<JButton>();

	Map<String, AbstractButton> toolItems;
	
	private Editor editor;

	/**
	 * the constructor of the class
	 */
	public ToolBarManager(Editor editor) {
		
		this.editor = editor;
	}

	/**
	 * initializes the toolbar after all the parts are initialized
	 */
	public void initializeParts() {
//		toolBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//		toolBarPanel.add(toolsBar);
		// toolBarPanel.add(toolsBar_view);
		// toolBarPanel.add(toolsBar_ev);
		// setting the properties of the menu bar
		toolsBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		// getting all the tool items of the application
		Collection<Module> modules = editor.getSVGModuleLoader()
				.getModules();
		toolItems = new HashMap<String, AbstractButton>();
		Map<String, AbstractButton> items = null;

		for (Module module : modules) {

			if (module != null) {

				items = module.getToolItems();

				if (items != null) {

					toolItems.putAll(items);
				}
			}
		}

		// getting the xml document describing the tool bars
		Document doc = ResourcesManager.getXMLDocument("tool.xml");

		// getting the root node
		Element rootElement = doc.getDocumentElement();
		ToolBar toolBar = null;

		// creating all the toolbars defined in the xml document
		for (Node node = rootElement.getFirstChild(); node != null; node = node
				.getNextSibling()) {

			if (node instanceof Element) {

				// creating the toolbar
				toolBar = new ToolBar(toolItems, (Element) node,editor);

				// adding the tool bar to the menubar
				toolsBar.add(toolBar.getToolBar());
			}
		}

		// add by yuxiang
		// 增加缩放文本及下拉框
		toolsBar.addSeparator();
		JLabel label = new JLabel(ResourcesManager.bundle
				.getString("ZoomLabel")+" ");
		
//		toolsBar.addSeparator();

		String[] scales = new String[Zoom.defaultZoomFactors.length + 2];
		for (int i = 0; i < Zoom.defaultZoomFactors.length; i++) {
			scales[i + 2] = Integer
					.toString((int) (Zoom.defaultZoomFactors[i] * 100))
					+ " %";
		}
		scales[0] = "100%";
		scales[1] = "-----";
		combo = new JComboBox(scales);
		
		combo.setEditable(true);
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int nIndex = (int) ((JComboBox) e.getSource())
							.getSelectedIndex();
					
					SVGHandle handle = editor.getHandlesManager()
                    .getCurrentHandle();
					if (nIndex < 2)
					{
					    if(nIndex == -1)
					    {
		                        String value;
		                        try
		                        {
		                            value =combo.getSelectedItem().toString();
		                            if(value.indexOf("%") > -1)
		                            {
		                                value = value.substring(0,value.indexOf("%"));
		                            }
		                            value = value.trim();
		                            int nScale = 0;
		                            try
		                            {
		                                nScale = new Integer(value).intValue();
		                                handle.getCanvas().getZoomManager().scaleTo((double)nScale/100);
		                            }
		                            catch(Exception ex)
		                            {
		                                ex.printStackTrace();
		                                double scale = handle.getCanvas().getZoomManager()
		                                .getCurrentScale();
		                                handle.getCanvas().getZoomManager().scaleTo(scale);
		                            }
		                        }
		                        catch(Exception ex)
		                        {
		                            
		                        }
					    }
						return;
					}
					

					if (handle != null) {

						handle.getCanvas().getZoomManager().scaleTo(
								Zoom.defaultZoomFactors[nIndex - 2]);
						handle.getCanvas().requestFocus();
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JToolBar scaleBar = new JToolBar();
		scaleBar.add(label);
		scaleBar.add(combo);
		toolsBar.add(scaleBar);
		combo.setSelectedIndex(-1);
		combo.setEnabled(false);
//		combo.setEditable(false);

		final HandlesManager svgHandleManager = editor
				.getHandlesManager();

		svgHandleManager.addHandlesListener(new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {

				boolean existsHandle = svgHandleManager.getHandlesNumber() > 0;
				combo.setEnabled(existsHandle);
				if (existsHandle) {
					double scale = currentHandle.getCanvas().getZoomManager()
							.getCurrentScale();
					setComboShow(scale);
				} else {
					combo.setSelectedIndex(-1);
				}

			}
		});
		// TODO:增加图切换按钮
		/*
		 * toolsBar.addSeparator(); JButton button = new JButton("系统图");
		 * button.setToolTipText("打开系统图"); ft_buttons.add(button);
		 * button.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { try {
		 *  } catch (Exception ex) { ex.printStackTrace(); } } });
		 * toolsBar.add(button); button.setEnabled(false);
		 * button.setBorderPainted(false);
		 * 
		 * svgHandleManager.addHandlesListener(new HandlesListener(){
		 * 
		 * @Override public void handleChanged(SVGHandle currentHandle, Set<SVGHandle>
		 * handles) {
		 * 
		 * boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
		 *  } });
		 */
	}

	/**
	 * @return the component containing all the tool bars
	 */
	public JComponent getToolsBar() {
		// return toolsBar_edit;
//		return toolBarPanel;
		return toolsBar;
	}

	/**
	 * 根据输入的缩放比例，修改下拉框第一项显示
	 * 
	 * @param scale:缩放的比例
	 */
	public void setComboShow(double scale) {
		combo.removeItemAt(0);
		combo.insertItemAt(Integer.toString((int) (scale * 100)) + " %", 0);
		combo.setSelectedIndex(0);
		return;
	}

	public Map<String, AbstractButton> getToolItems() {
		return toolItems;
	}
}
