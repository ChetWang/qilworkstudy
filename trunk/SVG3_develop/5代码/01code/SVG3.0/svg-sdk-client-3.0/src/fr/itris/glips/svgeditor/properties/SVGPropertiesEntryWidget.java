/*
 * Created on 19 janv. 2005
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2004 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.svgeditor.properties;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesEntryWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesEntryWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		//the text field in which the value will be entered
//		final JTextField textField=new JTextField(propertyItem.getGeneralPropertyValue(), 10);
		final JTextArea textArea = new JTextArea(propertyItem.getGeneralPropertyValue());
		textArea.setLineWrap(false);
		
		textArea.moveCaretPosition(0);
		
		final CaretListener listener=new CaretListener(){
		    
			public void caretUpdate(CaretEvent e) {
				
				//modifies the widgetValue of the property item
				propertyItem.changePropertyValue(textArea.getText(),true);
			}
		};
			
		//adds a listener to the text field
		textArea.addCaretListener(listener);
		
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(textArea);
		textArea.setSize(200, 100);
		//add by yuxiang
		//让输入框初始化为8行多行输入模式，
		textArea.setRows(8);
//		panel.add(textArea);
		panel.add(scroll);
		
		component=panel;

		//creates the disposer
		disposer=new Runnable(){

            public void run() {

				textArea.removeCaretListener(listener);
            }
		};
	}
}

