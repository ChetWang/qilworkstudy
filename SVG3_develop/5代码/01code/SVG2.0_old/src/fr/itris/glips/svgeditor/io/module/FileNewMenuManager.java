package fr.itris.glips.svgeditor.io.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.managers.export.FileExport;

public class FileNewMenuManager {

    /**
     * the menu
     */
    private JMenu menu=new JMenu();
    
    /**
     * the array of the menu items
     */
    private JMenuItem[] menuItems=null;
    
    IOModule iomodule;
    
    /**
     * the constructor of the class
     */
    public FileNewMenuManager(IOModule iomod){
        this.iomodule = iomod;
        createMenuItems();
    }
    
    /**
     * creates the menu items
     */
    protected void createMenuItems() {
        
        //creating the export menuitems
        ActionListener listener=null;
        int size = iomodule.getEditor().getSupportFileTypeSize();
        menuItems = new JMenuItem[size];
        for(int i=0; i<size; i++){
            
            final int index=i;
            
            menuItems[i]=new JMenuItem(iomodule.getEditor().getSupportTypeValueFromMap(i));
            menuItems[i].setName(iomodule.getEditor().getSupportTypeKeyFromMap(i));
            menu.add(menuItems[i]);
            
            listener=new ActionListener(){
                
                public void actionPerformed(ActionEvent evt) {
                    
                    //getting the current handle
 
                    iomodule.getEditor().getIOManager().getFileNewManager()
                        .askForNewFileParameters(menuItems[index]);
                }
            };
            
            menuItems[i].addActionListener(listener);
        }
    }

    /**
     * @return the menu containing the export menu items
     */
    public JMenu getMenu() {
        return menu;
    }
    
}
