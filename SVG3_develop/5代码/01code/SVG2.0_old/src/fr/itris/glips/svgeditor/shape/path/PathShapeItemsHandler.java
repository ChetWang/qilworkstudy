package fr.itris.glips.svgeditor.shape.path;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

import org.w3c.dom.Element;

import fr.itris.glips.library.display.*;
import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the menu items and tool items for the path shape module
 * @author Jordi SUC
 */
public class PathShapeItemsHandler {
    private static final int CREATION_LINE_MODE = 0;
    private static final int CREATION_CURVE_MODE = 1;
    private static final int CREATION_FOLDLINE_MODE = 2;
    private static final int ADD_POINT_MODE = 3;
    private static final int REMOVE_POINT_MODE = 4;

	/**
	 * the path shape module
	 */
	private PathShape pathShapeModule;
	
	/**
	 * the button used for selecting the type of the action
	 */
	protected JToggleButton toolButton=new JToggleButton();
	
	/**
	 * the popup menu shown when the tool button is clicked
	 */
	private JPopupMenu popupMenu;
	
	/**
	 * the icon manager for the tool button
	 */
	private ArrowIcon arrowIcon=new ArrowIcon();
	
	/**
	 * the icons
	 */
	protected ImageIcon shapeCreatorIcon, shapeCreatorDisabledIcon, addPointIcon, 
		addPointDisabledIcon, removePointIcon, removePointDisabledIcon;
	
	/**
	 * the labels
	 */
	protected String itemLabel, itemTooltip, addPointLabel, addPointTooltip, 
		removePointLabel, removePointTooltip,foldLineTooltip;
	
	/**
	 * the menu items that will be added to the menu bar
	 */
	protected JMenuItem LineMenuItem,CurveMenuItem, FoldLineMenuItem,
		addPointMenuItem, removePointMenuItem;
	
	/**
	 * the current mode
	 */
	protected int currentMode=CREATION_LINE_MODE;
	
	/**
	 * the constructor of the class
	 * @param pathShapeModule the path shape module
	 */
	public PathShapeItemsHandler(PathShape pathShapeModule){
		
		this.pathShapeModule=pathShapeModule;
		createItems();
	}
	
	/**
	 * creates all the items for the menubar and toolbar
	 */
	protected void createItems(){
		
		//getting the id of the path module
		String shapeModuleId=pathShapeModule.getId();
		
		//getting the icons
		shapeCreatorIcon=ResourcesManager.getIcon(shapeModuleId, false);
		shapeCreatorDisabledIcon=ResourcesManager.getIcon(shapeModuleId, true);
		addPointIcon=ResourcesManager.getIcon("AddPointPath", false);
		addPointDisabledIcon=ResourcesManager.getIcon("AddPointPath", true);
		removePointIcon=ResourcesManager.getIcon("RemovePointPath", false);
		removePointDisabledIcon=ResourcesManager.getIcon("RemovePointPath", true);
		
		//getting the labels
		itemLabel=ResourcesManager.bundle.getString(shapeModuleId+"ItemLabel");
		itemTooltip=ResourcesManager.bundle.getString(shapeModuleId+"ItemToolTip");
		foldLineTooltip = ResourcesManager.bundle.getString("PolylineShapeItemToolTip");
		addPointLabel=ResourcesManager.bundle.getString(shapeModuleId+"AddPointLabel");
		addPointTooltip=ResourcesManager.bundle.getString(shapeModuleId+"AddPointToolTip");
		removePointLabel=ResourcesManager.bundle.getString(shapeModuleId+"RemovePointLabel");
		removePointTooltip=ResourcesManager.bundle.getString(shapeModuleId+"RemovePointToolTip");
	
		//creating the menu items for the menu bar
		LineMenuItem=new JMenuItem("直线", shapeCreatorIcon);
		LineMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
		LineMenuItem.setEnabled(false);
		
		CurveMenuItem=new JMenuItem("曲线", shapeCreatorIcon);
		CurveMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
		CurveMenuItem.setEnabled(false);
        
        FoldLineMenuItem=new JMenuItem("折线", shapeCreatorIcon);
        FoldLineMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
        FoldLineMenuItem.setEnabled(false);
        
		
		addPointMenuItem=new JMenuItem(addPointLabel, addPointIcon);
		addPointMenuItem.setDisabledIcon(addPointDisabledIcon);
		addPointMenuItem.setEnabled(false);
		
		removePointMenuItem=new JMenuItem(removePointLabel, removePointIcon);
		removePointMenuItem.setDisabledIcon(removePointDisabledIcon);
		removePointMenuItem.setEnabled(false);
		
		//creating the menu items for the tool bar
		final JMenuItem LineMenuItemTool=new JMenuItem("直线", shapeCreatorIcon);
		LineMenuItemTool.setDisabledIcon(shapeCreatorDisabledIcon);
		LineMenuItemTool.setToolTipText(itemTooltip);
		
		final JMenuItem FoldLineMenuItemTool=new JMenuItem("折线", shapeCreatorIcon);
		FoldLineMenuItemTool.setDisabledIcon(shapeCreatorDisabledIcon);
		FoldLineMenuItemTool.setToolTipText(foldLineTooltip);
		
		final JMenuItem addPointMenuItemTool=new JMenuItem(addPointLabel, addPointIcon);
		addPointMenuItemTool.setDisabledIcon(addPointDisabledIcon);
		addPointMenuItemTool.setToolTipText(addPointTooltip);
		
		final JMenuItem removePointMenuItemTool=new JMenuItem(removePointLabel, removePointIcon);
		removePointMenuItemTool.setDisabledIcon(removePointDisabledIcon);
//		shapeCreatorMenuItemTool.setToolTipText(removePointTooltip);.itemTooltip
		removePointMenuItemTool.setToolTipText(removePointTooltip);
		
		//creating the listener to the menu and tool items
		ActionListener listener=new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
                if(e.getSource().equals(LineMenuItem)){
                    
				    pathShapeModule.setCurrentAction("LineShape");
                    pathShapeModule.setModuleMode(PathShape.CREATION_MODE);
                    pathShapeModule.notifyDrawingMode();
                    
                }else if(e.getSource().equals(CurveMenuItem)){
                    
                    pathShapeModule.setCurrentAction("CurveShape");
                    pathShapeModule.setModuleMode(PathShape.CREATION_MODE);
                    pathShapeModule.notifyDrawingMode();
                    
                }else if(e.getSource().equals(FoldLineMenuItem)){
                    
                    pathShapeModule.setCurrentAction("FoldLineShape");
                    pathShapeModule.setModuleMode(PathShape.CREATION_MODE);
                    pathShapeModule.notifyDrawingMode();
                    
                }else if(e.getSource().equals(LineMenuItemTool)){
					
                    pathShapeModule.setCurrentAction("LineShape");
					pathShapeModule.setModuleMode(PathShape.CREATION_MODE);
					setCurrentTool(CREATION_LINE_MODE);
					pathShapeModule.notifyDrawingMode();
					
				}else if(e.getSource().equals(FoldLineMenuItemTool)){
                    
                    pathShapeModule.setCurrentAction("FoldLineShape");
                    pathShapeModule.setModuleMode(PathShape.CREATION_MODE);
                    setCurrentTool(CREATION_FOLDLINE_MODE);
                    pathShapeModule.notifyDrawingMode();
                    
                }else if(e.getSource().equals(addPointMenuItem)){
					
				    pathShapeModule.setCurrentAction("AddPathShape");
					pathShapeModule.setModuleMode(PathShape.ADD_MODE);
					pathShapeModule.notifyItemsActionMode();
					
				}else if(e.getSource().equals(addPointMenuItemTool)){
					
				    pathShapeModule.setCurrentAction("AddPathShape");
					pathShapeModule.setModuleMode(PathShape.ADD_MODE);
					setCurrentTool(ADD_POINT_MODE);
					pathShapeModule.notifyItemsActionMode();
					
				}else if(e.getSource().equals(removePointMenuItem)){
					
				    pathShapeModule.setCurrentAction("RemovePathShape");
					pathShapeModule.setModuleMode(PathShape.REMOVE_MODE);
					pathShapeModule.notifyItemsActionMode();
					
				}else if(e.getSource().equals(removePointMenuItemTool)){
					
				    pathShapeModule.setCurrentAction("RemovePathShape");
					pathShapeModule.setModuleMode(PathShape.REMOVE_MODE);
					setCurrentTool(REMOVE_POINT_MODE);
					pathShapeModule.notifyItemsActionMode();
				}

//				toolButton.setSelected(true);
			}
		};
		
		//adding the listener to the tool and menu items
		LineMenuItem.addActionListener(listener);
		CurveMenuItem.addActionListener(listener);
		FoldLineMenuItem.addActionListener(listener);
		addPointMenuItem.addActionListener(listener);
		removePointMenuItem.addActionListener(listener);
		LineMenuItemTool.addActionListener(listener);
		FoldLineMenuItemTool.addActionListener(listener);
		addPointMenuItemTool.addActionListener(listener);
		removePointMenuItemTool.addActionListener(listener);
		
		//creating the tool item that will be displayed in the tool bar
		toolButton=new JToggleButton();
		toolButton.setEnabled(false);
		
		//creating a popup menu for the button
		popupMenu=new JPopupMenu();
		
		//adding the items
		popupMenu.add(LineMenuItemTool);
		popupMenu.add(FoldLineMenuItemTool);
		popupMenu.addSeparator();
		popupMenu.add(addPointMenuItemTool);
		popupMenu.add(removePointMenuItemTool);
		
		//computing the area where the arrow can be found
		final Rectangle area=new Rectangle(ArrowIcon.arrowIconBounds);
		area.x+=toolButton.getMargin().left;
		area.width+=toolButton.getMargin().right;

		//adding a listener to the tool button
		toolButton.addMouseListener(new MouseAdapter(){
			
			/**
			 * the timer
			 */
			private java.util.Timer timer;

			@Override
			public void mousePressed(MouseEvent evt) {
				
				if(evt.getX()<area.getX()){
					
					TimerTask task=new TimerTask(){
						
						@Override
						public void run() {
							
							showPopup();
							timer=null;
						}
					};
					
					timer=new Timer();
					timer.schedule(task, 500);
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent evt) {

				if(timer!=null){
					
					timer.cancel();
					timer=null;
				}
				
				//setting the global module mode to the current mode of the tool
				if(currentMode <= CREATION_FOLDLINE_MODE)
				{
				    pathShapeModule.setModuleMode(PathShape.CREATION_MODE) ; 
				    pathShapeModule.notifyDrawingMode();
				}
				else if(currentMode == ADD_POINT_MODE)
				{
				    pathShapeModule.setModuleMode(PathShape.ADD_MODE) ; 
                    pathShapeModule.notifyItemsActionMode();
				}
				else if(currentMode == REMOVE_POINT_MODE)
				{
				    pathShapeModule.setModuleMode(PathShape.REMOVE_MODE) ; 
                    pathShapeModule.notifyItemsActionMode();
				}
				

				if(evt.getX()>=area.getX()){

					//the arrow part has been clicked, the popup is then shown
					showPopup();
				}
			}
			
			/**
			 * shows the popup menu
			 */
			protected void showPopup(){
				
				//showing the popup menu
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

						Rectangle bounds=toolButton.getBounds();
						popupMenu.pack();
						popupMenu.show(toolButton, (int)(bounds.getWidth()/2), 
								(int)(bounds.getHeight()/2));
					}
				});
			}
		});

		setCurrentTool(currentMode);
		
		//adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager=
			pathShapeModule.getEditor().getHandlesManager();
		
		svgHandleManager.addHandlesListener(new HandlesListener(){
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
		
				boolean existsHandle=svgHandleManager.getHandlesNumber()>0;
				LineMenuItem.setEnabled(existsHandle);
				CurveMenuItem.setEnabled(existsHandle);
				FoldLineMenuItem.setEnabled(existsHandle);
				
				toolButton.setEnabled(existsHandle);
				
				boolean bFoldLineFlag = false;
				if(currentHandle != null && currentHandle.getSelection().getSelectedElements().size() == 1)
				{
				    Element element = currentHandle.getSelection().getSelectedElements().iterator().next();
				    if(element.getNodeName().equals("path"))
				    {
				        Path path = new Path(element.getAttribute("d")); 
                        if(path.getSegmentsNumber() > 2)
                            bFoldLineFlag = true;
				    }

				}

				{
				    addPointMenuItem.setEnabled(bFoldLineFlag);
                    removePointMenuItem.setEnabled(bFoldLineFlag);
                    addPointMenuItemTool.setEnabled(bFoldLineFlag);
                    removePointMenuItemTool.setEnabled(bFoldLineFlag);
                    
				}
				if(currentHandle !=null)
				{
				    if (currentHandle.getSelection().getPathshapeListener() != null)
                        currentHandle.getSelection()
                                .removeSelectionChangedListener(
                                        currentHandle.getSelection()
                                                .getPathshapeListener());
				SelectionChangedListener listener = new SelectionChangedListener() {

                    @Override
                    public void selectionChanged(
                            Set<Element> selectedElements) {

                        boolean bSelect = true;
                        if (selectedElements == null
                                || selectedElements.size() != 1)
                            bSelect = false;
                        else {
                            Element element = (Element) selectedElements
                                    .iterator().next();
                            if (!element.getNodeName().equals("path")) {
                                bSelect = false;
                            }
                            else
                            {
                                 Path path = new Path(element.getAttribute("d")); 
                                 if(path.getSegmentsNumber() <=2)
                                     bSelect = false;
                            }
                        }
                        addPointMenuItem.setEnabled(bSelect);
                        removePointMenuItem.setEnabled(bSelect);
                        addPointMenuItemTool.setEnabled(bSelect);
                        removePointMenuItemTool.setEnabled(bSelect);
                    }
                };
                currentHandle.getSelection().addSelectionChangedListener(
                        listener);
                currentHandle.getSelection().setPathshapeListener(listener);
				}
			}			
		});
	}
	
	/**
	 * sets the current tool item in the toolbar according 
	 * to the provided mode
	 * @param mode
	 */
	protected void setCurrentTool(int mode){
		
		this.currentMode=mode;
		
		//getting the new icons and the tooltip for the tool
		ImageIcon icon=null;
		String tooltip="";

		switch (mode){
			
			case CREATION_LINE_MODE:
			{
				
				icon=shapeCreatorIcon;
				tooltip=itemTooltip;
				break;
			}
				
           case CREATION_FOLDLINE_MODE :
                
                icon=shapeCreatorIcon;
                tooltip=foldLineTooltip;
                break;
				
			case ADD_POINT_MODE :
			
				icon=addPointIcon;
				tooltip=addPointTooltip;
				break;
			
			case REMOVE_POINT_MODE :
			
				icon=removePointIcon;
				tooltip=removePointTooltip;
				break;
		}
		
		//setting the new properties for the tool
		toolButton.setToolTipText(tooltip);
		arrowIcon.setIcon(icon);
		toolButton.setIcon(arrowIcon.getIcon());
		toolButton.setDisabledIcon(arrowIcon.getDisabledIcon());
		
		//hiding the popup menu
		popupMenu.setVisible(false);
	}
	
	/**
	 * @return the menu items for the path shape module
	 */
	public HashMap<String, JMenuItem> getMenuItems() {
		
		HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
		map.put(pathShapeModule.lineActionId, LineMenuItem);
		map.put(pathShapeModule.curveActionId, CurveMenuItem);
		map.put(pathShapeModule.foldlineActionId, FoldLineMenuItem);
		map.put(PathShape.addPointActionId, addPointMenuItem);
		map.put(PathShape.removePointActionId, removePointMenuItem);
		
		return map;
	}
	
	/**
	 * @return the tool item containing the menu items 
	 * for the path shape module
	 */
	public HashMap<String, AbstractButton> getTools(){
		
		HashMap<String, AbstractButton> map=new HashMap<String, AbstractButton>();
		map.put(pathShapeModule.getId(), toolButton);

		return map;
	}
}
