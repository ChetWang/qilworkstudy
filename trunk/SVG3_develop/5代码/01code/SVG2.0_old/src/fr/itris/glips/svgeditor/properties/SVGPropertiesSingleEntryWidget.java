package fr.itris.glips.svgeditor.properties;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class SVGPropertiesSingleEntryWidget extends SVGPropertiesWidget {

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
    public SVGPropertiesSingleEntryWidget(SVGPropertyItem propertyItem) {

        super(propertyItem);
        
        buildComponent();
    }
    
    /**
     * builds the component that will be displayed
     */
    protected void buildComponent(){

        //the text field in which the value will be entered
//      final JTextField textField=new JTextField(propertyItem.getGeneralPropertyValue(), 10);
        final JTextField textField=new JTextField(propertyItem.getGeneralPropertyValue(), 8);
        textField.moveCaretPosition(0);
        
        //adds a key listener to the textfield
        final KeyAdapter keyListener=new KeyAdapter(){
            
            public void keyPressed(KeyEvent evt) {
                
                if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                    
                    //modifies the widgetValue of the property item
                    propertyItem.changePropertyValue(textField.getText());
                }
            }
        };
        
        textField.addKeyListener(keyListener);
        final CaretListener listener=new CaretListener(){
            
            public void caretUpdate(CaretEvent e) {
                
                //modifies the widgetValue of the property item
                propertyItem.changePropertyValue(textField.getText());
            }
        };
        textField.addCaretListener(listener);
        JPanel singlePanel=new JPanel();

        singlePanel.setLayout(new BorderLayout());
        singlePanel.add(textField, BorderLayout.CENTER);
        
        component=singlePanel;

        //creates the disposer
        disposer=new Runnable(){

            public void run() {

                textField.removeKeyListener(keyListener);
                textField.removeCaretListener(listener);
            }
        };
    }
}
