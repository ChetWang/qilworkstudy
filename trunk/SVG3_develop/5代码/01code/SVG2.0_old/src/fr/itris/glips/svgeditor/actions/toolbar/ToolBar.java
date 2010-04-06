package fr.itris.glips.svgeditor.actions.toolbar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToolBar;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.itris.glips.svgeditor.Editor;

/**
 * the class used to create the toolbar and its items
 * @author Jordi SUC
 */
public class ToolBar {
	
	/**
	 * node names
	 */
	protected final static String separatorName="separator";
	
	/**
	 * the attribute names
	 */
	protected final static String exclusiveAtt="exclusive", nameAtt="name"; 

	/**
	 * the jtoolbar that an instance of this class manages
	 */
	private JToolBar toolBar=new JToolBar();
	
	/**
	 * whether the buttons in the toolbar are exclusive or not
	 */
	private boolean isExclusive=false;
	
	Editor editor;
	
	/**
	 * the constructor of the class
	 * @param toolItems the map of all the tool items that exist in the application
	 * @param element a dom element describing the tool bar
	 */
	public ToolBar(Map<String, AbstractButton> toolItems, Element element,Editor editor){
		this.editor = editor;
		//setting the properties of the toolbar
		toolBar.setOpaque(false);
		toolBar.setRollover(true);
		toolBar.setFloatable(true);
		toolBar.setBorderPainted(false);
		toolBar.setMargin(new Insets(0, 0, 0, 0));
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		//build the tool bar items
		build(toolItems, element);
	}
	
	/**
	 * builds the toolbar given all the tool items of the application 
	 * and the dom element descsribing this toolbar
	 * @param toolItems the map of all the tool items that exist in the application
	 * @param element a dom element describing the tool bar
	 */
	protected void build(Map<String, AbstractButton> toolItems, Element element){
		
		try{
			isExclusive=Boolean.parseBoolean(element.getAttribute(exclusiveAtt));
		}catch (Exception ex){}
		
		//filling the toolbar
		Element childEl;
		String name="";
		AbstractButton button;
		ButtonGroup buttonGroup=null;
		
		//for each child node of the element describing the toolbar
		for(Node node=element.getFirstChild(); node!=null; node=node.getNextSibling()){
			
			if(node instanceof Element){
				
				childEl=(Element)node;
				
				if(childEl.getNodeName().equals(separatorName)){
					
					//adding a separator
					toolBar.addSeparator(new Dimension(4, 10));

				}else{
					
					//getting the name of the tool
					name=childEl.getAttribute(nameAtt);
					
					
					//getting the button corresponding to this name
					button=toolItems.get(name);
				
					if(button!=null){
						
						if(isExclusive){
							
							//adding the button to a button group
							if(buttonGroup==null){
								
								//creating the button group
								buttonGroup=new ButtonGroup();
								//added by wangql
								if(name.equals("RegularSelection"))
									editor.getSymbolSession().setToolbarGraphUnitButtonGroup(buttonGroup);
							}
							
							buttonGroup.add(button);
						}
						
						//adding the button to the toolbar
						toolBar.add(button);
						
						
					}
				}
			}
		}
	}

	/**
	 * @return the jtoolbar that an instance of this class manages
	 */
	public JToolBar getToolBar() {
		return toolBar;
	}
}
